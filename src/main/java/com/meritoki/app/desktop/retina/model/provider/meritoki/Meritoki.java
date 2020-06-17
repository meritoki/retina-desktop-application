package com.meritoki.app.desktop.retina.model.provider.meritoki;

import java.io.File;

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
	public File file;

	public Meritoki() {
		super("meritoki");
	}
	
	public void open(String documentUUID) {
		File directory = new File(NodeController.getProviderHome()+NodeController.getSeperator()+"meritoki");
		this.file = new File(directory+NodeController.getSeperator()+documentUUID+".json");
		if(this.file.exists()) {
			this.document = (Document)NodeController.openJson(this.file, Document.class);
		} else {
			this.document = new Document();
			directory.mkdirs();
			NodeController.saveJson(this.file, this.document);
		}
		document.group.load();
	}
	
	public void save() {
		NodeController.saveJson(this.file, this.document);
	}
}
