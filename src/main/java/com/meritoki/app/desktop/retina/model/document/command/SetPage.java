package com.meritoki.app.desktop.retina.model.document.command;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.meritoki.app.desktop.retina.model.document.Document;
import com.meritoki.app.desktop.retina.model.document.Shape;

public class SetPage extends Command {
	
	private static Logger logger = LogManager.getLogger(SetPage.class.getName());
	
	public SetPage(Document document) {
		super(document, "setPage");
	}
	
    @Override // Command
    public void execute() {
    	logger.info("execute()");
    	Operation operation = new Operation();
		operation.object = document.getIndex();
		operation.sign = 0;
		operation.id = UUID.randomUUID().toString();
		this.operationList.add(operation);
    	if(document.cache.pageIndex > -1) {
    		document.setIndex(document.cache.pageIndex);
    	} else if (document.cache.pageUUID != null) {
    		document.setPage(document.cache.pageUUID);
    	}
    }
}
