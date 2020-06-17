package com.meritoki.app.desktop.retina.model.provider.meritoki;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.meritoki.app.desktop.retina.controller.node.NodeController;
import com.meritoki.app.desktop.retina.model.provider.Provider;
import com.meritoki.app.desktop.retina.model.provider.meritoki.Document;

/**
 * As a provider there are several things that need to be stored in the provider folder for a particular document
 * The document UUID serves as a identifier for a file or folder, likely folder where everything can be stored
 * 
 * 
 * @author jorodriguez
 *
 */
public class Meritoki extends Provider {
	
	public Document document;
	public List<Input> inputList;
	public File cortexFile;
	public File inputFile;

	public Meritoki() {
		super("meritoki");
		this.init();
	}
	
	public void init() {
		File directory = new File(getMeritokiHome());
		if(!directory.exists()) {
			directory.mkdirs();
		}
	}
	
	public static String getMeritokiHome() {
		return NodeController.getProviderHome()+NodeController.getSeperator()+"meritoki";
	}
	
	public void openCortex(String documentUUID) {
		File directory = new File(getMeritokiHome()+NodeController.getSeperator()+documentUUID);
		this.cortexFile = new File(directory+NodeController.getSeperator()+"cortex.json");
		if(this.cortexFile.exists()) {
			this.document = (Document)NodeController.openJson(this.cortexFile, Document.class);
		} else {
			this.document = new Document();
			directory.mkdirs();
			NodeController.saveJson(this.cortexFile, this.document);
		}
		document.group.load();
	}
	
	public void saveCortex() {
		NodeController.saveJson(this.cortexFile, this.document);
	}
	
	public List<Input> openInput(String documentUUID) {
		File directory = new File(getMeritokiHome()+NodeController.getSeperator()+documentUUID);
		this.inputFile = new File(directory+NodeController.getSeperator()+documentUUID+NodeController.getSeperator()+"input.json");
		if(this.inputFile.exists()) {
			inputList = (List<Input>) NodeController.openJson(this.inputFile, new TypeReference<List<Input>>() {});
		} else {
			this.inputList = new ArrayList<>();
			directory.mkdirs();
			NodeController.saveJson(this.inputFile, this.inputList);
		}
		return inputList;
	}
	
	public void saveInput() {
		NodeController.saveJson(this.inputFile, this.inputList);
	}
}
