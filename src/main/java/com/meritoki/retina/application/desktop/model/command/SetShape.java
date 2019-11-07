package com.meritoki.retina.application.desktop.model.command;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.meritoki.retina.application.desktop.model.Model;
import com.meritoki.retina.application.desktop.model.document.Operation;
import com.meritoki.retina.application.desktop.model.document.Shape;

public class SetShape extends Command {
	private static Logger logger = LogManager.getLogger(SetShape.class.getName());
	public SetShape(Model project) {
		this.model = project;
		this.name = "setShape";
	}
	
    @Override // Command
    public void execute() {
    	logger.info("execute()");
    	this.user = this.model.user;
    	Shape shape = this.model.getDocument().getProject().getPage().getShape();
    	if(shape != null) {
			Operation operation = new Operation();
			operation.object = new Shape(shape);
			operation.sign = 0;
			operation.id = UUID.randomUUID().toString();
			operation.uuid = this.model.variable.pressedShape.uuid;
			this.operationList.add(operation);
    	}
    	this.model.getDocument().getProject().getPage().setShape(this.model.variable.pressedShape.uuid);
		shape = this.model.getDocument().getProject().getPage().getShape();
		if(shape != null) {
			Operation operation = new Operation();
			operation.object = new Shape();
			operation.sign = 1;
			operation.id = UUID.randomUUID().toString();
			operation.uuid = this.model.variable.pressedShape.uuid;
			this.operationList.add(operation);
		}
    }
}
