package com.meritoki.app.desktop.retina.model.module;

import com.meritoki.app.desktop.retina.model.Model;
import com.meritoki.module.library.model.Data;
import com.meritoki.module.library.model.Module;
import com.meritoki.module.library.model.Node;

public class Train extends Node {
	public static final int SEARCH = 1;
	public static final int SCAN = 2;
	public static final int NOTIFY = 3;
	private Model model;

	public Train(int intValue, Module module, Model model) {
		super(intValue, module);
		this.model = model;
	}

	public void initialize() {
		super.initialize();
		this.stateMap.put(SEARCH, "SEARCH");
		this.stateMap.put(SCAN, "SCAN");
		this.stateMap.put(NOTIFY, "NOTIFY");
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
		case NOTIFY: {
			this.notify();
			break;
		}
		}
		super.machine(state, object);
	}

	private void search(Object object) {
		if (object instanceof Data) {
			Data data = (Data) object;
		}
	}

	private void scan(Object object) {
		if (object instanceof Data) {
			Data data = (Data) object;
		}
	}

	private void notify(Object object) {
		if (object instanceof Data) {
			Data data = (Data) object;
		}
	}
}
