package com.meritoki.app.desktop.retina.model.command;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.meritoki.app.desktop.retina.model.Model;
import com.meritoki.app.desktop.retina.model.document.Page;

public class ScanImage extends Command {

	private static Logger logger = LogManager.getLogger(ScanImage.class.getName());

	public ScanImage(Model document) {
		super(document, "scanImage");
	}

	@Override
	public void execute() throws Exception {
		logger.info("execute()");
		String pressedPageUUID = this.model.cache.pressedPageUUID;
		double scaleFactor = this.model.cache.scaleFactor;
		char scaleOperator = this.model.cache.scaleOperator;
    	
    	Page page = this.model.document.getPage(pressedPageUUID);
		double scale = page.position.scale;

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
		
		
		
		//redo
    	operation = new Operation();
		operation.object = scale;
		operation.sign = 1;
		operation.id = UUID.randomUUID().toString();
		this.operationList.add(operation);
	}
}
