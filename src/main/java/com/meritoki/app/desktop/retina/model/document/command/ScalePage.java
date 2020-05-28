package com.meritoki.app.desktop.retina.model.document.command;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.meritoki.app.desktop.retina.model.document.Document;

public class ScalePage extends Command {

	private static Logger logger = LogManager.getLogger(ScalePage.class.getName());
	
	public ScalePage(Document document) {
		super(document, "scalePage");
	}
	
    @Override // Command
    public void execute() {
    	logger.info("execute()");
    	Operation operation = new Operation();
		operation.object = this.document.cache.scale;
		operation.sign = 0;
		operation.id = UUID.randomUUID().toString();
		this.operationList.add(operation);
		double scale = this.document.cache.scale;
		double factor = 1.5;
		switch(this.document.cache.scaleOperator) {
		case '*':{
			scale *= factor;
			break;
		}
		case '/':{
			scale /= factor;
			break;
		}
		}
		this.document.cache.scale = scale;
		this.document.cache.pressedPage.setScale(this.document.cache.scale);
    }
}
