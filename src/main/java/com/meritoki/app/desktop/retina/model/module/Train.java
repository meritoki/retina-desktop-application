package com.meritoki.app.desktop.retina.model.module;

import com.meritoki.app.desktop.retina.model.Model;
import com.meritoki.module.library.model.Data;
import com.meritoki.module.library.model.Module;
import com.meritoki.module.library.model.Node;

public class Train extends Node {
	public static final int SEARCH = 1;
	public static final int SCAN = 2;
	private Model model;

	public Train(int intValue, Module module, Model model) {
		super(intValue, module);
		this.model = model;
	}

	public void initialize() {
		super.initialize();
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
		
	}

	private void scan(Object object) {
//		if (object instanceof Data) {
//			Data data = (Data) object;
//		}
		//for each shape in ths scan list, train the Vision algorithm with the Data Text as the concept
		//when complete, send notification to Inference Module and return to search State.
		
	}
}
