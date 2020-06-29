package com.meritoki.app.desktop.retina.controller.document;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.meritoki.app.desktop.retina.controller.Controller;
import com.meritoki.app.desktop.retina.controller.node.NodeController;
import com.meritoki.app.desktop.retina.model.document.Document;

public class DocumentController extends Controller {

	private static Logger logger = LogManager.getLogger(DocumentController.class.getName());
	
	@JsonIgnore
	public static void save(String filePath, String fileName, Object object) {
		logger.info("save()");
		NodeController.saveJson(filePath, fileName, object);
	}
	
	@JsonIgnore
	public static void save(File file, Document document) {
		logger.info("save("+file.getAbsolutePath()+", document)");
		NodeController.saveJson(file, document);
	}

	@JsonIgnore
	public static Document open(String filePath, String fileName) {	
		Document document = (Document) NodeController.openJson(new java.io.File(filePath+"/"+fileName), Document.class);
		logger.info("open(" + filePath+", "+fileName + ") document="+document);
		return document;
	}
	
	@JsonIgnore
	public static Document open(File file) {
		Document document = (Document) NodeController.openJson(file, Document.class);
		document.init();
		logger.info("open(" + file + ") document="+document);
		return document;
	}
}
