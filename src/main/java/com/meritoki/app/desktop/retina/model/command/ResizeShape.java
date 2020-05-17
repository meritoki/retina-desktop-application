package com.meritoki.app.desktop.retina.model.command;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.meritoki.app.desktop.retina.model.ModelPrototype;
import com.meritoki.app.desktop.retina.model.document.Point;
import com.meritoki.app.desktop.retina.model.document.Shape;

public class ResizeShape extends Command {
	private static Logger logger = LogManager.getLogger(ResizeShape.class.getName());
	public ResizeShape(ModelPrototype model) {
		this.model = model;
		this.name = "resizeShape";
	}
	
    @Override // Command
    public void execute() {
    	logger.info("execute()");
    	this.user = this.model.user;
		Operation operation = new Operation();
		operation.object = new Shape(this.model.variable.pressedShape);
		operation.sign = 0;
		operation.id = UUID.randomUUID().toString();
		operation.uuid = this.model.variable.pressedShape.uuid;
		this.operationList.push(operation);
		Point releasedPoint = new Point(this.model.variable.releasedPoint);
		this.model.variable.pressedShape.resize(releasedPoint, this.model.variable.selection);
		operation = new Operation();
		operation.object = new Shape(this.model.variable.pressedShape);
		operation.sign = 1;
		operation.id = UUID.randomUUID().toString();
		operation.uuid = this.model.variable.pressedShape.uuid;
		this.operationList.push(operation);
    }
}
