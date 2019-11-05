package com.meritoki.retina.application.desktop.model.command;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.meritoki.retina.application.desktop.model.Model;
import com.meritoki.retina.application.desktop.model.document.Operation;
import com.meritoki.retina.application.desktop.model.document.Point;
import com.meritoki.retina.application.desktop.model.document.Shape;


public class MoveShape extends Command {
	private static Logger logger = LogManager.getLogger(MoveShape.class.getName());
	public MoveShape(Model project) {
		this.model = project;
		this.name = "moveShape";
	}
	
    @Override // Command
    public void execute() {
    	logger.info("execute()");
    	this.user = this.model.user;
		Operation operation = new Operation();
		operation.object = new Shape(this.model.variable.shape);
		operation.sign = 0;
		operation.id = UUID.randomUUID().toString();
		operation.uuid = this.model.variable.shape.uuid;
		this.operationList.push(operation);
		this.model.variable.shape.move(new Point(this.model.variable.movedPoint));
		operation = new Operation();
		operation.object = new Shape(this.model.variable.shape);
		operation.sign = 1;
		operation.id = UUID.randomUUID().toString();
		operation.uuid = this.model.variable.shape.uuid;
		this.operationList.push(operation);
    }
}
