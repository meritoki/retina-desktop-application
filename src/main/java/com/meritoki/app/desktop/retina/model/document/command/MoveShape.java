package com.meritoki.app.desktop.retina.model.document.command;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.meritoki.app.desktop.retina.model.document.Position;
import com.meritoki.app.desktop.retina.model.document.Document;
import com.meritoki.app.desktop.retina.model.document.Image;
import com.meritoki.app.desktop.retina.model.document.Page;
import com.meritoki.app.desktop.retina.model.document.Point;
import com.meritoki.app.desktop.retina.model.document.Shape;


public class MoveShape extends Command {
	private static Logger logger = LogManager.getLogger(MoveShape.class.getName());
	public MoveShape(Document document) {
		super(document,"moveShape");
	}
	
	
	/**
	 * PENDING, DO NOT ALLOW MOVE IF MOVEMENT IS OUTSIDE OF AN IMAGE
	 */
    @Override // Command
    public void execute() throws Exception {
    	logger.info("execute()");
    	this.user = this.document.cache.user;
		Operation operation = new Operation();
		operation.object = new Shape(this.document.cache.pressedShape,true);
		operation.sign = 0;
		operation.id = UUID.randomUUID().toString();
		operation.uuid = this.document.cache.pressedShape.uuid;
		this.operationList.push(operation);
		//Logic
		Page pressedPage = this.document.getPage();
		Shape pressedShape = this.document.cache.pressedShape;
		Image pressedImage = this.document.cache.pressedImage;
		Image releasedImage = this.document.cache.releasedImage;
		Point releasedPoint = this.document.cache.releasedPoint;
		Point pressedPoint = this.document.cache.pressedPoint;
		Point movedPoint = this.getMovedPoint(new Point(releasedPoint), new Point(pressedPoint));
		Shape newShape = null;
		if(pressedPage.contains(releasedPoint)) { 
		if (releasedImage != null && !pressedImage.equals(releasedImage)) {
			newShape = pressedShape;//new Shape(pressedShape,true);//uncommented to fix bug
			newShape.position.move(movedPoint);
			//Testing new logic
			newShape.position = new Position(new Point(newShape.position.getStartPoint()),
					new Point(newShape.position.getStopPoint()), 
					releasedImage.position.relativeScale, 
					releasedImage.position.scale,//this.document.cache.scale,
					releasedImage.position.offset, 
					releasedImage.position.margin);
			newShape = new Shape(newShape, true);
			//end test
			
			pressedImage.removeShape(newShape.uuid);
			releasedImage.addShape(newShape);
		} else {
			pressedShape.position.move(movedPoint);
			newShape = pressedShape;
		}		
		} else {
			throw new Exception("Release Point Invalid");
		}
		operation = new Operation();
		operation.object = new Shape(newShape,true);
		operation.sign = 1;
		operation.id = UUID.randomUUID().toString();
		operation.uuid = this.document.cache.pressedShape.uuid;
		this.operationList.push(operation);
    }
    
	public Point getMovedPoint(Point a, Point b) {
		return new Point(a.x - b.x, a.y - b.y);
	}
	
//	Image i = this.document.cache.releasedImage;
//	Position p = this.document.cache.pressedShape.position;
//	shape.position = new Position(p.absolutePoint,p.absoluteDimension,p.addScale,i.position.offset,i.position.margin);
}
