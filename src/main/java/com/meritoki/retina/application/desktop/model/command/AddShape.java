package com.meritoki.retina.application.desktop.model.command;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.meritoki.retina.application.desktop.model.Model;
import com.meritoki.retina.application.desktop.model.document.Operation;
import com.meritoki.retina.application.desktop.model.document.Shape;

public class AddShape extends Command {
	private static Logger logger = LogManager.getLogger(AddShape.class.getName());
	public AddShape(Model project) {
		this.model = project;
		this.name = "addShape";
	}
	
    @Override // Command
    public void execute() {
    	logger.info("execute()");
    	this.user = this.model.user;
    	this.model.getDocument().getProject().getPage().addShape(this.model.variable.pressedShape);
		Operation operation = new Operation();
		operation.object = new Shape(this.model.variable.pressedShape);
		operation.sign = 1;
		operation.id = UUID.randomUUID().toString();
		operation.uuid = this.model.variable.pressedShape.uuid;
		this.operationList.push(operation);
    }
}
