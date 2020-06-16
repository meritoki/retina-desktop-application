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
import com.meritoki.app.desktop.retina.model.document.Shape;
import com.meritoki.app.desktop.retina.model.document.user.User;
import com.meritoki.app.desktop.retina.model.provider.Provider;
import com.meritoki.app.desktop.retina.model.provider.meritoki.Input;
import com.meritoki.app.desktop.retina.model.provider.meritoki.Meritoki;
import com.meritoki.cortex.library.model.Concept;
import com.meritoki.module.library.model.Module;
import com.meritoki.module.library.model.Node;

public class Train extends Node {
	public static final int SEARCH = 1;
	public static final int SCAN = 2;
	private Model model;
	private Meritoki meritoki;
	private List<Input> inputList = new ArrayList<>();

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
		}
		super.machine(state, object);
	}

	private void search(Object object) {
		

//		if (object instanceof Data) {
//			Data data = (Data) object;
//		}
		
		//load shapes it has already trained
		//get document shape list
		//filter all shapes with Data Text
		//create list of shapes to scan;
		//set state to scan;
		if(this.delayExpired()) {
			List<Input> trainList = this.loadInputList();
			List<Shape> shapeList = this.model.document.getShapeList();
			for(Shape s:shapeList) {
				if(s.data.text.value != null) {
					
				}
			}
			this.setDelay(this.newDelay(this.inputDelay));
		}
	}
	
	private List<Input> loadInputList() {
		List<Input> inputList = null;
		File file = new File(NodeController.getProviderHome()+NodeController.getSeperator()+"meritoki"+NodeController.getSeperator()+this.model.document.uuid);
		inputList = (List<Input>) NodeController.openJson(file, new TypeReference<List<Input>>() {});
		return inputList;
	}

	private void scan(Object object) {
//		if (object instanceof Data) {
//			Data data = (Data) object;
//		}
		//for each shape in ths scan list, train the Vision algorithm with the Data Text as the concept
		//when complete, send notification to Inference Module and return to search State.
		
	}
	
	public List<String> scan(BufferedImage bufferedImage, Concept concept) {
//		System.out.println("scan");
		List<String> phrase = new ArrayList<String>();
		if (bufferedImage != null) {
//			System.out.println("scan not null ");
			double scale = 1;
			int width = bufferedImage.getWidth();
			int height = bufferedImage.getHeight();
			Concept inference = null;
			List<Concept> conceptList = null;
			Map<String, Integer> conceptMap = new HashMap<>();
			int diameter = 10;//(2 * this.size) * (this.radius * 2);
			int radius = diameter / 2;
//			int w = width / 2;
			int hInterval = 10;// height / diameter;
			int wInterval = 20;//width / diameter;
			for (int w = 0; w < width; w += wInterval) {
				for (int h = 0; h < height; h += hInterval) {
					this.meritoki.document.group.setOrigin(w+radius, h+radius);
					this.meritoki.document.group.update();
//				if(concept != null)
//					this.meritoki.document.group.process(bufferedImage, scale, concept);
//					conceptList = this.network.process(bufferedImage, scale, concept);
//				System.out.println(conceptList);
//					for (Concept c : conceptList) {
//						Integer integer = conceptMap.get(c.toString());
//						integer = (integer == null) ? 0 : integer;
//						conceptMap.put(c.toString(), integer + 1);
//					}
				}
			}
//			inference = new Concept(this.getMaxConcept(conceptMap, 0.50));
//			if (inference != null) {
//				if (!phrase.contains(inference.toString())) {
//					phrase.add(inference.toString());
//				}
//			}
		}
		return phrase;
	}
}
