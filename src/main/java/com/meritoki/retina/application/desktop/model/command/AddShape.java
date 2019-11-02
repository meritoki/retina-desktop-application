package com.meritoki.retina.application.desktop.model.command;

import java.util.UUID;

import com.meritoki.retina.application.desktop.model.Model;
import com.meritoki.retina.application.desktop.model.document.Operation;

public class AddShape extends Command {
	
	public AddShape(Model project) {
		this.model = project;
		this.name = "addShape";
	}
	
    @Override // Command
    public void execute() {
    	this.user = this.model.user;
    	this.model.getDocument().getProject().addShape(this.model.variable.shape);
		Operation operation = new Operation();
		operation.object = this.model.variable.shape;
		operation.sign = 1;
		operation.id = UUID.randomUUID().toString();
		operation.uuid = this.model.variable.shape.uuid;
		this.operationList.push(operation);
    }
}
