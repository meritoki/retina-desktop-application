package com.meritoki.app.desktop.retina.model.document.command;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.meritoki.app.desktop.retina.model.document.Document;
import com.meritoki.app.desktop.retina.model.document.Page;

public class ScalePage extends Command {

	private static Logger logger = LogManager.getLogger(ScalePage.class.getName());
	
	public ScalePage(Document document) {
		super(document, "scalePage");
	}
	
    @Override // Command
    public void execute() {
    	logger.info("execute()");
    	//Variables
    	Page page = this.document.getPage();
		double scale = this.document.cache.pressedImage.position.scale;
		double scaleFactor = this.document.cache.scaleFactor;
		char scaleOperator = this.document.cache.scaleOperator;
    	//Undo
    	Operation operation = new Operation();
		operation.object = scale;
		operation.sign = 0;
		operation.id = UUID.randomUUID().toString();
		this.operationList.add(operation);
		//logic
		switch(scaleOperator) {
		case '*':{
			scale *= scaleFactor;
			break;
		}
		case '/':{
			scale /= scaleFactor;
			break;
		}
		}
		page.setScale(scale);
    	operation = new Operation();
		operation.object = scale;
		operation.sign = 1;
		operation.id = UUID.randomUUID().toString();
		this.operationList.add(operation);
    }
}
