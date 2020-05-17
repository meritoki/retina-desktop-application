package com.meritoki.app.desktop.retina.model.command;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.meritoki.app.desktop.retina.model.ModelPrototype;
import com.meritoki.app.desktop.retina.model.document.Point;
import com.meritoki.app.desktop.retina.model.document.Shape;


public class MoveShape extends Command {
	private static Logger logger = LogManager.getLogger(MoveShape.class.getName());
	public MoveShape(ModelPrototype project) {
		this.model = project;
		this.name = "moveShape";
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
		this.model.variable.movedPoint = this.getMovedPoint(new Point(this.model.variable.releasedPoint), new Point(this.model.variable.pressedPoint));
		Shape shape = null;
		if (this.model.variable.releasedFile != null &&!this.model.variable.pressedFile.equals(this.model.variable.releasedFile)) {
			shape = new Shape(this.model.variable.pressedShape);
			shape.pointList.get(0).x = shape.dimension.x;
			shape.pointList.get(0).y = shape.dimension.y;
			shape.pointList.get(1).x = shape.dimension.x + shape.dimension.w;
			shape.pointList.get(1).y = shape.dimension.y + shape.dimension.h;
			shape.move(this.model.variable.movedPoint);
			this.model.variable.pressedFile.removeShape(shape.uuid);
			this.model.variable.releasedFile.addShape(shape);
		} else {
			this.model.variable.pressedShape.move(this.model.variable.movedPoint);
			shape = this.model.variable.pressedShape;
		}		
		operation = new Operation();
		operation.object = new Shape(shape);
		operation.sign = 1;
		operation.id = UUID.randomUUID().toString();
		operation.uuid = this.model.variable.pressedShape.uuid;
		this.operationList.push(operation);
    }
    
	public Point getMovedPoint(Point a, Point b) {
		return new Point(a.x - b.x, a.y - b.y);
	}
}
