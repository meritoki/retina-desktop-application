package com.meritoki.app.desktop.retina.model.document.command;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.meritoki.app.desktop.retina.model.document.Document;
import com.meritoki.app.desktop.retina.model.document.Image;
import com.meritoki.app.desktop.retina.model.document.Page;

public class AddPage extends Command {
	
	private static Logger logger = LogManager.getLogger(AddPage.class.getName());
	
	public AddPage(Document document) {
		super(document, "addPage");
	}
	
    @Override
    public void execute() {
    	logger.info("execute()");
    	this.user = this.document.state.user;
    	Page page = null;
    	for(File file: this.document.state.fileArray) {
    		page = new Page();
			page.imageList.add(new Image(file.getParent(),file.getName()));
    		this.document.addPage(page);
    	}
    }
}
