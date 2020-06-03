package com.meritoki.app.desktop.retina.model.document.command;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.meritoki.app.desktop.retina.model.document.Document;
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
    	//undo
		Operation operation = new Operation();
		operation.object = new Page(pressedPage);
		operation.sign = 0;
		operation.id = UUID.randomUUID().toString();
		this.operationList.push(operation);
		//logic
		this.document.removePage(pressedPage.uuid);
	}
}
