package com.meritoki.app.desktop.retina.model.module;

import com.meritoki.app.desktop.retina.model.Model;
import com.meritoki.module.library.model.Data;
import com.meritoki.module.library.model.Module;
import com.meritoki.module.library.model.Node;

public class Inference extends Node {
	
	public static final int WAIT = 1;
	public static final int SCAN = 2;
	private Model model;

	public Inference(int intValue, Module module, Model model) {
		super(intValue, module);
		this.model = model;
	}
	
	public void initialize() {
		super.initialize();
		this.stateMap.put(WAIT,"WAIT");
		this.stateMap.put(SCAN,"SCAN");
		this.setState(WAIT);
	}
	
	protected void machine(int state, Object object) {
		switch(state) {
		case WAIT: {
			this.wait(object);
			break;
		}
		case SCAN: {
			this.scan(object);
			break;
		}
		}
		super.machine(state, object);
	}
	
	private void wait(Object object) {
		if(object instanceof Data) {
			Data data = (Data)object;
		}
	}
	
	private void scan(Object object) {
		if(object instanceof Data) {
			Data data = (Data)object;
		}
	}
}
