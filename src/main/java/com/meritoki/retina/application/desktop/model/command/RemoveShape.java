package com.meritoki.retina.application.desktop.model.command;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.meritoki.retina.application.desktop.model.Model;
import com.meritoki.retina.application.desktop.model.document.Operation;
import com.meritoki.retina.application.desktop.model.document.Shape;

public class RemoveShape extends Command {
	private static Logger logger = LogManager.getLogger(RemoveShape.class.getName());
	public RemoveShape(Model model) {
		this.model = model;
		this.name = "removeShape";
	}
	
    @Override // Command
    public void execute() {
    	logger.info("execute()");
    	this.user = this.model.user;
		this.model.getDocument().project.getPage().getFile().removeShape(this.model.variable.shape);
		Operation operation = new Operation();
		operation.object = new Shape(this.model.variable.shape);
		operation.sign = 0;
		operation.id = UUID.randomUUID().toString();
		operation.uuid = this.model.variable.shape.uuid;
		this.operationList.push(operation);
    }
}
