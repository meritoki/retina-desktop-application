package com.meritoki.app.desktop.retina.model.provider.meritoki;

import java.io.File;

import com.meritoki.app.desktop.retina.controller.node.NodeController;
import com.meritoki.app.desktop.retina.model.provider.Provider;
import com.meritoki.app.desktop.retina.model.provider.meritoki.Document;

public class Meritoki extends Provider {
	
	public Document document;

	public Meritoki() {
		super("meritoki");
	}
	
	public void open(String documentUUID) {
		File directory = new File(NodeController.getProviderHome()+NodeController.getSeperator()+"meritoki");
		File file = new File(directory+NodeController.getSeperator()+documentUUID+".json");
		if(file.exists()) {
			this.document = (Document)NodeController.openJson(file, Document.class);
		} else {
			this.document = new Document();
			directory.mkdirs();
			NodeController.saveJson(file, this.document);
		}
		document.group.load();
	}
}
