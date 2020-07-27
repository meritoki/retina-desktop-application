package com.meritoki.app.desktop.retina.controller.node;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.meritoki.app.desktop.retina.model.document.Document;

public class NodeController extends com.meritoki.library.controller.node.NodeController {

	private static Logger logger = LogManager.getLogger(NodeController.class.getName());
	
	public static String getSystemHome() {
		return getUserHome() + getSeperator() + ".retina";
	}

	public static String getDocumentCache() {
		return getSystemHome() + getSeperator() + "document";
	}

	public static String getDocumentCache(String uuid) {
		return getDocumentCache() + getSeperator() + uuid;
	}

	public static String getResourceCache() {
		return getSystemHome() + getSeperator() + "resource";
	}

	public static String getImageCache() {
		return getSystemHome()+getSeperator()+"image";
	}

	public static String getPanoptesHome() {
		return getUserHome() + getSeperator() + ".panoptes";
	}

	public static String getProviderHome() {
		return getSystemHome() + getSeperator() + "provider";
	}
	
	public static void saveDocument(String filePath, String fileName, Document document) {
		logger.info("saveDocument("+filePath+", "+fileName+", "+document+")");
		NodeController.saveJson(filePath, fileName, document);
	}
	
	public static void saveDocument(File file, Document document) {
		logger.info("saveDocument("+file+", "+document+")");
		NodeController.saveJson(file, document);
	}

	public static Document openDocument(String filePath, String fileName) {	
		Document document = (Document) NodeController.openJson(new java.io.File(filePath+"/"+fileName), Document.class);
		logger.info("openDocument(" + filePath+", "+fileName + ") document="+document);
		return document;
	}
	
	public static Document openDocument(File file) {
		Document document = (Document) NodeController.openJson(file, Document.class);
		logger.info("openDocument(" + file + ") document="+document);
		return document;
	}
	
}