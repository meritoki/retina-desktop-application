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
//    	this.user = this.document.cache.user;
    	File[] fileArray = this.document.cache.fileArray;
    	Operation operation = new Operation();
    	//save pagelist in operation as it is
    	
    	//logic
    	for(File file: fileArray) {
    		Page page = new Page();
			page.imageList.add(new Image(file));
    		this.document.addPage(page);
    	}
    }
}
