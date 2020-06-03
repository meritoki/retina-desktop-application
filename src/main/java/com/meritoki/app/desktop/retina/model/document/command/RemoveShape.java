package com.meritoki.app.desktop.retina.model.document.command;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.meritoki.app.desktop.retina.model.document.Document;
import com.meritoki.app.desktop.retina.model.document.Shape;

public class RemoveShape extends Command {
	
	private static Logger logger = LogManager.getLogger(RemoveShape.class.getName());
	
	public RemoveShape(Document document) {
		super(document, "removeShape");
	}
	
    @Override // Command
    public void execute() {
    	logger.info("execute()");
    	//variables
    	Shape pressedShape = this.document.cache.pressedShape;
		//undo
		Operation operation = new Operation();
		operation.object = new Shape(pressedShape,true);
		operation.sign = 0;
		operation.id = UUID.randomUUID().toString();
		this.operationList.push(operation);
		//logic
		this.document.getPage().removeShape(pressedShape);
    }
}
