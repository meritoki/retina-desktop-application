package com.meritoki.app.desktop.retina.controller;

import com.meritoki.app.desktop.retina.model.Model;

public class Controller implements ControllerInterface {

	protected Model model;
	
	@Override
	public void setModel(Model model) {
		this.model = model;
	}	
}
