package com.meritoki.app.desktop.retina.model.document.command;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.meritoki.app.desktop.retina.model.document.Document;
import com.meritoki.app.desktop.retina.model.document.Point;
import com.meritoki.app.desktop.retina.model.document.Shape;

public class SetPage extends Command {
	
	private static Logger logger = LogManager.getLogger(SetPage.class.getName());
	
	public SetPage(Document document) {
		super(document, "setPage");
	}
	
    @Override // Command
    public void execute() {
    	logger.info("execute()");
    	//variables
    	int pageIndex = document.cache.pageIndex;
    	String pageUUID = document.cache.pageUUID;
    	//undo
    	Operation operation = new Operation();
		operation.object = document.getIndex();
		operation.sign = 0;
		operation.id = UUID.randomUUID().toString();
		this.operationList.add(operation);
		//logic
    	if(pageIndex > -1) {
    		document.setIndex(pageIndex);
    	} else if (pageUUID != null) {
    		document.setPage(pageUUID);
    	}
    	//redo
    	operation = new Operation();
		operation.object = document.getIndex();
		operation.sign = 1;
		operation.id = UUID.randomUUID().toString();
		this.operationList.add(operation);
    }
}
