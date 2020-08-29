package com.meritoki.app.desktop.retina.model.command;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.meritoki.app.desktop.retina.model.Model;
import com.meritoki.app.desktop.retina.model.document.Grid;
import com.meritoki.app.desktop.retina.model.document.Image;
import com.meritoki.app.desktop.retina.model.document.Page;
import com.meritoki.app.desktop.retina.model.document.Point;
import com.meritoki.app.desktop.retina.model.document.Position;
import com.meritoki.app.desktop.retina.model.document.Shape;

public class MoveShape extends Command {
	private static Logger logger = LogManager.getLogger(MoveShape.class.getName());

	public MoveShape(Model document) {
		super(document, "moveShape");
	}

	/**
	 * PENDING, DO NOT ALLOW MOVE IF MOVEMENT IS OUTSIDE OF AN IMAGE
	 */
	@Override // Command
	public void execute() throws Exception {
		logger.info("execute()");
//    	this.user = this.document.cache.user;
		// Variables
		String pressedPageUUID = this.model.cache.pressedPageUUID;
		String pressedShapeUUID = this.model.cache.pressedShapeUUID;
		String pressedImageUUID = this.model.cache.pressedImageUUID;
		String releasedImageUUID = this.model.cache.releasedImageUUID;

		Page pressedPage = this.model.document.getPage(pressedPageUUID);
		Shape pressedShape = this.model.document.getShape(pressedShapeUUID);
		Image pressedImage = this.model.document.getImage(pressedImageUUID);
		Image releasedImage = this.model.document.getImage(releasedImageUUID);
//		Page pressedPage = this.model.document.getPage();
//		Shape pressedShape = this.model.cache.pressedShape;
//		Image pressedImage = this.model.cache.pressedImage;
//		Image releasedImage = this.model.cache.releasedImage;
		Point releasedPoint = this.model.cache.releasedPoint;
		Point pressedPoint = this.model.cache.pressedPoint;
		Shape undoShape = null;
		Shape redoShape = null;
		if(pressedShape instanceof Grid) {
			Grid pressedGrid = (Grid)pressedShape;
			undoShape = new Grid(pressedGrid,true);
		} else {
			undoShape = new Shape(pressedShape,true);
		}
		// Undo
		Operation operation = new Operation();
		operation.object = undoShape;
		operation.sign = 0;
		operation.id = UUID.randomUUID().toString();
		// operation.uuid = this.document.cache.pressedShape.uuid;
		this.operationList.push(operation);
		// Logic
		Point movedPoint = this.getMovedPoint(new Point(releasedPoint), new Point(pressedPoint));
		Shape newShape = null;
		if (pressedPage.contains(releasedPoint)) {
			if (releasedImage != null && !pressedImage.equals(releasedImage)) {
				newShape = pressedShape;// new Shape(pressedShape,true);//uncommented to fix bug
				newShape.position.move(movedPoint);
				// Testing new logic
				newShape.position = new Position(new Point(newShape.position.getStartPoint()),
						new Point(newShape.position.getStopPoint()), releasedImage.position.relativeScale,
						releasedImage.position.scale, releasedImage.position.offset, releasedImage.position.margin);
				newShape = new Shape(newShape, true);
				pressedImage.removeShape(newShape.uuid);
				releasedImage.addShape(newShape);
				if(newShape instanceof Grid) {
					((Grid)newShape).updateMatrix();
				}
			} else {
				pressedShape.position.move(movedPoint);
				newShape = pressedShape;
				if(newShape instanceof Grid) {
					((Grid)newShape).updateMatrix();
				}
			}
		} else {
			throw new Exception("Release Point Invalid");
		}
		// Redo
		if(newShape instanceof Grid) {
			logger.info("execute() (newShape instanceof Grid)");
			Grid newGrid = (Grid)newShape;
			redoShape = new Grid(newGrid,true);
		} else {
			redoShape = new Shape(newShape,true);
		}
		operation = new Operation();
		operation.object = redoShape;//new Shape(newShape, true);
		operation.sign = 1;
		operation.id = UUID.randomUUID().toString();
		// operation.uuid = this.document.cache.pressedShape.uuid;
		this.operationList.push(operation);
	}

	public Point getMovedPoint(Point a, Point b) {
		return new Point(a.x - b.x, a.y - b.y);
	}

//	Image i = this.document.cache.releasedImage;
//	Position p = this.document.cache.pressedShape.position;
//	shape.position = new Position(p.absolutePoint,p.absoluteDimension,p.addScale,i.position.offset,i.position.margin);
}
