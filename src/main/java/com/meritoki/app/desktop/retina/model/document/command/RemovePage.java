package com.meritoki.app.desktop.retina.model.document.command;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.meritoki.app.desktop.retina.model.document.Document;
import com.meritoki.app.desktop.retina.model.document.Image;
import com.meritoki.app.desktop.retina.model.document.Page;

public class RemovePage extends Command {

	private static Logger logger = LogManager.getLogger(RemovePage.class.getName());
	
	public RemovePage(Document document) {
		super(document, "removePage");
	}
	
	@Override
	public void execute() {
    	logger.info("execute()");
    	Page pressedPage = this.document.cache.pressedPage;
    	int pageIndex = this.document.getIndex(pressedPage.uuid);
    	Object[] objectArray = new Object[2];
    	objectArray[0] = pageIndex;
    	objectArray[1] = new Page(pressedPage);
    	//undo
		Operation operation = new Operation();
		operation.object = objectArray;
		operation.sign = 0;
		operation.id = UUID.randomUUID().toString();
		this.operationList.push(operation);
		//logic
		this.document.removePage(pressedPage.uuid);
		//redo
		operation = new Operation();
		operation.object = pressedPage.uuid;
		operation.sign = 1;
		operation.id = UUID.randomUUID().toString();
		this.operationList.push(operation);
	}
}
