package com.meritoki.app.desktop.retina.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.meritoki.app.desktop.retina.controller.document.DocumentController;
import com.meritoki.app.desktop.retina.model.document.Document;
import com.meritoki.app.desktop.retina.model.system.System;

public class Model {
	
	private static final Logger logger = LogManager.getLogger(Model.class.getName());
	public Document document = DocumentController.open("./archive/","Document20191121.json");
	public System system = new System();
	
	public Model() {
		logger.info("Model()");
	}

	public Document getDocument() {
		return this.document;
	}
	
	public System getSystem() {
		return this.system;
	}
}
