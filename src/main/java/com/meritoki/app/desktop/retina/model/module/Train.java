package com.meritoki.app.desktop.retina.model.module;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.meritoki.app.desktop.retina.controller.node.NodeController;
import com.meritoki.app.desktop.retina.model.Model;
//import com.meritoki.app.desktop.retina.model.document.Data;
import com.meritoki.app.desktop.retina.model.document.Shape;
import com.meritoki.app.desktop.retina.model.provider.Provider;
import com.meritoki.app.desktop.retina.model.provider.meritoki.Input;
import com.meritoki.app.desktop.retina.model.provider.meritoki.Meritoki;
import com.meritoki.library.cortex.model.Concept;
import com.meritoki.module.library.model.Data;
import com.meritoki.module.library.model.Module;
import com.meritoki.module.library.model.Node;

public class Train extends Node {
	public static final int WAIT = 1;
	public static final int SEARCH = 2;
	public static final int SCAN = 3;
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
		this.stateMap.put(WAIT, "WAIT");
		this.stateMap.put(SEARCH, "SEARCH");
		this.stateMap.put(SCAN, "SCAN");
		this.setState(SEARCH);
	}

	protected void machine(int state, Object object) {
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
		}
		super.machine(state, object);
	}

	private void wait(Object object) {
		if (object instanceof Data) {
			Data data = (Data) object;
			switch (data.getType()) {
			case Data.UNBLOCK: {
				this.setState(SEARCH);
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
			List<Shape> shapeList = this.model.document.getShapeList();
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
								this.meritoki.inputList.add(input);
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
				this.setState(SCAN);
			} 
//			else {
//				this.rootAdd(new Data(2, this.id, Data.UNBLOCK, 0, null, this.objectList));
//				this.setDelay(this.newDelay(this.inputDelay));
//				this.setState(WAIT);
//			}
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
			this.rootAdd(new Data(2, this.id, Data.UNBLOCK, 0, null, this.objectList));
			this.setDelay(this.newDelay(this.inputDelay));
			this.setState(WAIT);
		}
	}

	public void scan(BufferedImage bufferedImage, String concept) {
		logger.info("scan(" + bufferedImage + ", " + concept + ")");
		if (bufferedImage != null) {
			double scale = 1;
			int width = bufferedImage.getWidth();
			int height = bufferedImage.getHeight();
			int diameter = this.meritoki.document.group.size;
			int hInterval = height / diameter;
			int wInterval = width / diameter;
			for (int w = 0; w < width; w += wInterval) {
				for (int h = 0; h < height; h += hInterval) {
					this.meritoki.document.group.setOrigin(w, h);
					this.meritoki.document.group.update();
					if (concept != null) {
						this.meritoki.document.group.process(bufferedImage, scale, new Concept(concept));
					}
				}
			}
		}
	}
}
