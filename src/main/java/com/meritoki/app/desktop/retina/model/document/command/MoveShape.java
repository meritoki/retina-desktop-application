package com.meritoki.app.desktop.retina.model.document.command;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.meritoki.app.desktop.retina.model.document.Dimension;
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
    	this.user = this.document.cache.user;
		Operation operation = new Operation();
		operation.object = new Shape(this.document.cache.pressedShape);
		operation.sign = 0;
		operation.id = UUID.randomUUID().toString();
		operation.uuid = this.document.cache.pressedShape.uuid;
		this.operationList.push(operation);
		this.document.cache.movedPoint = this.getMovedPoint(new Point(this.document.cache.releasedPoint), new Point(this.document.cache.pressedPoint));
		Shape shape = null;
		if (this.document.cache.releasedImage != null &&!this.document.cache.pressedImage.equals(this.document.cache.releasedImage)) {
			shape = new Shape(this.document.cache.pressedShape);
			shape.dimension = new Dimension(this.document.cache.pressedShape.dimension);
			shape.dimension.movePoint(this.document.cache.movedPoint);
			this.document.cache.pressedImage.removeShape(shape.uuid);
			this.document.cache.releasedImage.addShape(shape);
		} else {
			this.document.cache.pressedShape.dimension.movePoint(this.document.cache.movedPoint);
			shape = this.document.cache.pressedShape;
		}		
		operation = new Operation();
		operation.object = new Shape(shape);
		operation.sign = 1;
		operation.id = UUID.randomUUID().toString();
		operation.uuid = this.document.cache.pressedShape.uuid;
		this.operationList.push(operation);
    }
    
	public Point getMovedPoint(Point a, Point b) {
		return new Point(a.x - b.x, a.y - b.y);
	}
}
