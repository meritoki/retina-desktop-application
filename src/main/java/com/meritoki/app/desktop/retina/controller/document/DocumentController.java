package com.meritoki.app.desktop.retina.controller.document;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.annotate.JsonIgnore;

import com.meritoki.app.desktop.retina.controller.node.NodeController;
import com.meritoki.app.desktop.retina.model.document.Document;

public class DocumentController {

	private static Logger logger = LogManager.getLogger(DocumentController.class.getName());
	
	@JsonIgnore
	public static void save(String filePath, String fileName, Object object) {
		logger.info("save()");
		NodeController.saveJson(filePath, fileName, object);
	}
	
	@JsonIgnore
	public static void save(File file, Document object) {
		logger.info("save("+file.getAbsolutePath()+", object)");
		NodeController.saveJson(file, object);
	}

	@JsonIgnore
	public static Document open(String filePath, String fileName) {
		logger.info("open(" + filePath+", "+fileName + ")");
		Document document = (Document) NodeController.openJson(new java.io.File(filePath+"/"+fileName), Document.class);
		return document;
	}
	
	@JsonIgnore
	public static Document open(File file) {
		logger.info("open(" + file + ")");
		Document document = (Document) NodeController.openJson(file, Document.class);
		return document;
	}
	
}
