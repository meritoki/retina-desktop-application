package com.meritoki.app.desktop.retina.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.meritoki.app.desktop.retina.model.document.Document;
import com.meritoki.app.desktop.retina.model.resource.Resource;
import com.meritoki.app.desktop.retina.model.system.System;

public class Model {
	
	private static final Logger logger = LogManager.getLogger(Model.class.getName());
	public Document document = new Document();
	public System system = new System();
	public Resource resource = new Resource();
	
	public Model() {}
}
