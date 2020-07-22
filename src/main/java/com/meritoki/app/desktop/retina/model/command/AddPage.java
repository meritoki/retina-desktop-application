package com.meritoki.app.desktop.retina.model.command;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.meritoki.app.desktop.retina.controller.memory.MemoryController;
import com.meritoki.app.desktop.retina.controller.pdf.PDFController;
import com.meritoki.app.desktop.retina.model.Model;
import com.meritoki.app.desktop.retina.model.document.Image;
import com.meritoki.app.desktop.retina.model.document.Page;

public class AddPage extends Command {
	
	private static Logger logger = LogManager.getLogger(AddPage.class.getName());
	
	public AddPage(Model document) {
		super(document, "addPage");
	}
	
    @Override
    public void execute() {
    	logger.info("execute()");
    	//undo
    	Operation operation = new Operation();
    	operation.object = new ArrayList<Page>(this.model.document.pageList);
    	operation.id = UUID.randomUUID().toString();
    	operation.sign = 0;
    	this.operationList.push(operation);	
    	//logic
    	File[] fileArray = this.model.cache.fileArray;
    	for(File file: fileArray) {
    		if(file.getName().contains(".pdf")) {
    			File[] pageArray = PDFController.openPDF(file, this.model.document.uuid);
    			for(File p:pageArray) {
    				Page page = new Page();
    				page.imageList.add(new Image(p));
    	    		this.model.document.addPage(page);
    	    		page.setBufferedImageNull();
    	    		MemoryController.log();
    			}
    		} else {
	    		Page page = new Page();
				page.imageList.add(new Image(file));
	    		this.model.document.addPage(page);
	    		page.setBufferedImageNull();
	    		MemoryController.log();
    		}
    	}
    	//redo
    	operation = new Operation();
    	operation.object = this.model.document.pageList;
    	operation.id = UUID.randomUUID().toString();
    	operation.sign = 1;
    	this.operationList.push(operation);
    }
}
