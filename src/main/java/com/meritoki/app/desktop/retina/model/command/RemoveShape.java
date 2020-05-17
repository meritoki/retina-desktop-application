package com.meritoki.app.desktop.retina.model.command;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.meritoki.app.desktop.retina.model.ModelPrototype;
import com.meritoki.app.desktop.retina.model.document.Shape;

public class RemoveShape extends Command {
	private static Logger logger = LogManager.getLogger(RemoveShape.class.getName());
	public RemoveShape(ModelPrototype model) {
		this.model = model;
		this.name = "removeShape";
	}
	
    @Override // Command
    public void execute() {
    	logger.info("execute()");
    	this.user = this.model.user;
		this.model.getDocument().project.getPage().removeShape(this.model.variable.pressedShape);
		Operation operation = new Operation();
		operation.object = new Shape(this.model.variable.pressedShape);
		operation.sign = 0;
		operation.id = UUID.randomUUID().toString();
		operation.uuid = this.model.variable.pressedShape.uuid;
		this.operationList.push(operation);
    }
}
