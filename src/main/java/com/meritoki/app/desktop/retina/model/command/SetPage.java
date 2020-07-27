package com.meritoki.app.desktop.retina.model.command;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.meritoki.app.desktop.retina.model.Model;
import com.meritoki.app.desktop.retina.model.document.Page;
import com.meritoki.library.controller.memory.MemoryController;

public class SetPage extends Command {
	
	private static Logger logger = LogManager.getLogger(SetPage.class.getName());
	
	public SetPage(Model document) {
		super(document, "setPage");
	}
	
    @Override // Command
    public void execute() {
    	logger.info("execute()");
    	//variables
    	String pageUUID = model.cache.pageUUID;
    	Page currentPage = model.getPage();
    	//undo
    	Operation operation = new Operation();
		operation.object = (currentPage != null)?currentPage.uuid:null;
		operation.sign = 0;
		operation.id = UUID.randomUUID().toString();
		this.operationList.add(operation);
		//logic
    	if (pageUUID != null) {
    		this.model.setPage(pageUUID);
    	}
    	//redo
    	operation = new Operation();
		operation.object = pageUUID;
		operation.sign = 1;
		operation.id = UUID.randomUUID().toString();
		this.operationList.add(operation);
		MemoryController.log();
    }
}
//int pageIndex = model.cache.pageIndex;
//else if(pageIndex >  -1) {
//this.model.document.setIndex(pageIndex);
//pageUUID = this.model.document.getPage().uuid;
//}
