package com.meritoki.app.desktop.retina.model.command;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.meritoki.app.desktop.retina.model.Model;
import com.meritoki.app.desktop.retina.model.document.Shape;

public class RemoveShape extends Command {
	
	private static Logger logger = LogManager.getLogger(RemoveShape.class.getName());
	
	public RemoveShape(Model document) {
		super(document, "removeShape");
	}
	
    @Override // Command
    public void execute() {
    	logger.info("execute()");
    	//variables
    	String pressedShapeUUID = this.model.cache.pressedShapeUUID;
    	Shape pressedShape = this.model.document.getShape(pressedShapeUUID);
		//undo
		Operation operation = new Operation();
		operation.object = new Shape(pressedShape,true);
		operation.sign = 0;
		operation.id = UUID.randomUUID().toString();
		this.operationList.push(operation);
		//logic
		this.model.document.getPage().removeShape(pressedShape);
    }
}
