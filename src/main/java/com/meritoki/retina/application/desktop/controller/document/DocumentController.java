package com.meritoki.retina.application.desktop.controller.document;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.annotate.JsonIgnore;

import com.meritoki.retina.application.desktop.controller.system.NodeController;
import com.meritoki.retina.application.desktop.model.document.Document;

public class DocumentController {

	private static Logger logger = LogManager.getLogger(DocumentController.class.getName());
	public static String filePath = "./";
	public static String fileName = "untitled.json";
	
	@JsonIgnore
	public static void save(String filePath, String fileName, Object object) {
		logger.info("save()");
		NodeController.save(filePath, fileName, object);
	}
	
	@JsonIgnore
	public static void save(File file, Object object) {
		logger.info("save()");
		NodeController.save(file, object);
	}

	@JsonIgnore
	public static Document open(String filePath, String fileName) {
		logger.info("open(" + filePath+", "+fileName + ")");
		Document document = (Document) NodeController.open(new java.io.File(filePath+"/"+fileName), Document.class);
		return document;
	}
	
	@JsonIgnore
	public static Document open(File file) {
		logger.info("open(" + file + ")");
		Document document = (Document) NodeController.open(file, Document.class);
		return document;
	}
	
}
