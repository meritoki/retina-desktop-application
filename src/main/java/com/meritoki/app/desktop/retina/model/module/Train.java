package com.meritoki.app.desktop.retina.model.module;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.meritoki.app.desktop.retina.model.Model;
//import com.meritoki.app.desktop.retina.model.document.Data;
import com.meritoki.app.desktop.retina.model.document.Shape;
import com.meritoki.app.desktop.retina.model.provider.Provider;
import com.meritoki.app.desktop.retina.model.provider.meritoki.Input;
import com.meritoki.app.desktop.retina.model.provider.meritoki.Meritoki;
import com.meritoki.library.cortex.model.Concept;
import com.meritoki.module.library.model.Module;
import com.meritoki.module.library.model.Node;
import com.meritoki.module.library.model.State;
import com.meritoki.module.library.model.data.Data;
import com.meritoki.module.library.model.data.DataType;

public class Train extends Node {
	private Model model;
	private Meritoki meritoki;
	private boolean scan = false;

	public Train(int intValue, Module module, Model model) {
		super(intValue, module);
		this.model = model;
	}

	public void initialize() {
		super.initialize();
		for (Provider provider : this.model.system.providerList) {
			if (provider instanceof Meritoki) {
				this.meritoki = (Meritoki) provider;
			}
		}
		this.setState(State.SEARCH);
	}

	protected void machine(State state, Object object) {
		switch (state) {
		case SEARCH: {
			this.search(object);
			break;
		}
		case SCAN: {
			this.scan(object);
			break;
		}
		case WAIT: {
			this.wait(object);
			break;
		}
		default: {
			super.machine(state, object);
		}
		}
	}

	private void wait(Object object) {
		if (object instanceof Data) {
			Data data = (Data) object;
			switch (data.getType()) {
			case UNBLOCK: {
				this.setState(State.SEARCH);
				this.setDelay(this.newDelay(this.inputDelay));
				break;
			}
			}
		}
	}

	private void search(Object object) {
		if (object instanceof Data) {
			Data data = (Data) object;
		}
		if (this.delayExpired()) {
			this.meritoki.openInput(this.model.document.uuid);
			List<Shape> shapeList = this.model.document.getGridShapeList();
			boolean flag;
			scan = false;
			for (Shape s : shapeList) {
				if (s.data.text.value != null) {
					flag = true;
					for (Input input : this.meritoki.inputList) {
						if (input.shape.uuid.equals(s.uuid)) {
							flag = false;
							if (!input.flag) {
								scan = true;
								input.shape.bufferedImage = s.bufferedImage;
								input.concept = s.data.text.value;
//								this.meritoki.inputList.add(input);
							}
						}
					}
					if (flag) {
						scan = true;
						Input input = new Input();
						input.shape = s;
						input.concept = s.data.text.value;
						input.flag = false;
						this.meritoki.inputList.add(input);
					}
				}
			}
			if (this.scan) {
				this.setState(State.SCAN);
			}
			this.setDelay(this.newDelay(this.inputDelay));
		}
	}

	private void scan(Object object) {
		if (this.delayExpired()) {
			for (Input i : this.meritoki.inputList) {
				if (!i.flag) {
					this.scan(i.shape.bufferedImage, i.concept);
					i.flag = true;
				}
			}
			this.meritoki.saveInput();
			this.meritoki.saveCortex();
			this.rootAdd(new Data(2, this.id, DataType.UNBLOCK, 0, null, this.objectList));
			this.setDelay(this.newDelay(this.inputDelay));
			this.setState(State.WAIT);
		}
	}

	public void scan(BufferedImage bufferedImage, String value) {
		logger.info("scan(" + bufferedImage + ", " + value + ")");
		Concept concept = null;
		if (bufferedImage != null) {
//			bufferedImage = this.scaleBufferedImage(bufferedImage, 0.25);
//			logger.info("scan(...) bufferedImaged="+bufferedImage);
//			NodeController.saveJpg("./test", UUID.randomUUID().toString()+".jpeg", bufferedImage);
			double width = bufferedImage.getWidth();
			double height = bufferedImage.getHeight();
			double diameter = this.meritoki.document.cortex.size;
			int wInterval = (int)(width/(width/diameter/2));
			int hInterval = (int)(height/(height/diameter/2));
			logger.info("scan(...) hInterval="+hInterval);
			logger.info("scan(...) wInterval="+wInterval);
			Map<String, Integer> conceptMap = new HashMap<>();
			for (int w = 0; w < width; w += wInterval) {
				for (int h = 0; h < height; h += hInterval) {
					this.meritoki.document.cortex.setOrigin(w, h);
					this.meritoki.document.cortex.update();
					if (value != null) {
						List<Concept> conceptList = this.meritoki.document.cortex.process(bufferedImage, new Concept(value));
						for (Concept c : conceptList) {
							Integer integer = conceptMap.get(c.toString());
							integer = (integer == null) ? 0 : integer;
							conceptMap.put(c.toString(), integer + 1);
						}
						concept = new Concept(this.getMaxConcept(conceptMap, 0.50));
					}
				}
			}
		}
		logger.info("scan(...) concept.value="+concept);
	}
	
	public String getMaxConcept(Map<String, Integer> conceptMap, double threshold) {
		String concept = null;
		double max = 0;
		double sum = 0;
		for (Map.Entry<String, Integer> entry : conceptMap.entrySet()) {
			String key = entry.getKey();
			Integer value = entry.getValue();
			sum += value;
			if (key != null && !key.equals("null") && value > max) {
				max = value;
				concept = key;
			}
		}
		double quotient = (max > 0 && sum > 0) ? max / sum : 0;
//		logger.info(quotient + " " + concept);
		if (quotient < threshold) {
			concept = null;
		}
		return concept;
	}
	
	public BufferedImage scaleBufferedImage(BufferedImage bufferedImage, double scale) {
		BufferedImage before = bufferedImage;
		int w = before.getWidth();
		int h = before.getHeight();
		BufferedImage after = new BufferedImage((int)(w*scale), (int)(h*scale), BufferedImage.TYPE_INT_ARGB);
		AffineTransform at = new AffineTransform();
		at.scale(scale, scale);
		AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
		after = scaleOp.filter(before, after);
		return after;
	}
}
