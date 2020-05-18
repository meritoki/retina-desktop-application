package com.meritoki.app.desktop.retina.model.document.command;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.meritoki.app.desktop.retina.model.document.Document;
import com.meritoki.app.desktop.retina.model.document.Point;
import com.meritoki.app.desktop.retina.model.document.Shape;


public class MoveShape extends Command {
	private static Logger logger = LogManager.getLogger(MoveShape.class.getName());
	public MoveShape(Document document) {
		super(document,"moveShape");
	}
	
    @Override // Command
    public void execute() {
    	logger.info("execute()");
    	this.user = this.document.state.user;
		Operation operation = new Operation();
		operation.object = new Shape(this.document.state.pressedShape);
		operation.sign = 0;
		operation.id = UUID.randomUUID().toString();
		operation.uuid = this.document.state.pressedShape.uuid;
		this.operationList.push(operation);
		this.document.state.movedPoint = this.getMovedPoint(new Point(this.document.state.releasedPoint), new Point(this.document.state.pressedPoint));
		Shape shape = null;
		if (this.document.state.releasedImage != null &&!this.document.state.pressedImage.equals(this.document.state.releasedImage)) {
			shape = new Shape(this.document.state.pressedShape);
			shape.pointList.get(0).x = shape.dimension.x;
			shape.pointList.get(0).y = shape.dimension.y;
			shape.pointList.get(1).x = shape.dimension.x + shape.dimension.w;
			shape.pointList.get(1).y = shape.dimension.y + shape.dimension.h;
			shape.move(this.document.state.movedPoint);
			this.document.state.pressedImage.removeShape(shape.uuid);
			this.document.state.releasedImage.addShape(shape);
		} else {
			this.document.state.pressedShape.move(this.document.state.movedPoint);
			shape = this.document.state.pressedShape;
		}		
		operation = new Operation();
		operation.object = new Shape(shape);
		operation.sign = 1;
		operation.id = UUID.randomUUID().toString();
		operation.uuid = this.document.state.pressedShape.uuid;
		this.operationList.push(operation);
    }
    
	public Point getMovedPoint(Point a, Point b) {
		return new Point(a.x - b.x, a.y - b.y);
	}
}
