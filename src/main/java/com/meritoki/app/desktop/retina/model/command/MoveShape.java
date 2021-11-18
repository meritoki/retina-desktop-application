/*
 * Copyright 2021 Joaquin Osvaldo Rodriguez
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
		String pressedPageUUID = this.model.cache.pressedPageUUID;
		String pressedShapeUUID = this.model.cache.pressedShapeUUID;
		Page pressedPage = this.model.document.getPage(pressedPageUUID);
		Shape pressedShape = this.model.document.getShape(pressedShapeUUID);
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
		
		Shape newShape = pressedPage.moveShape(pressedShape, pressedPoint, releasedPoint);
		// Logic
		// Redo
		if(newShape instanceof Grid) {
			logger.info("execute() (newShape instanceof Grid)");
			Grid newGrid = (Grid)newShape;
			redoShape = new Grid(newGrid,true);
		} else {
			redoShape = new Shape(newShape,true);
		}
		operation = new Operation();
		operation.object = redoShape;
		operation.sign = 1;
		operation.id = UUID.randomUUID().toString();
		this.operationList.push(operation);
	}

	public Point getMovedPoint(Point a, Point b) {
		return new Point(a.x - b.x, a.y - b.y);
	}
	
	@Override
	public void undo() throws Exception {
		for (int i = 0; i < this.operationList.size(); i++) {
			Operation operation = this.operationList.get(i);
			if (operation.sign == 1) {
				if (operation.object instanceof Shape) {
					Shape shape = (Shape)operation.object;
					this.model.document.getPage().removeShape(shape);
				}
			} else 
			if (operation.sign == 0) {
				if (operation.object instanceof Shape) {
					Shape shape = (Shape)operation.object;
					if(shape instanceof Grid) {
						Grid grid = (Grid) shape;
						grid.updateMatrix();
					}
					this.model.document.getPage().getImage().addShape(shape);
				}
			}
		}
		
	}

	@Override
	public void redo() throws Exception {
		for (int i = 0; i < this.operationList.size(); i++) {
			Operation operation = this.operationList.get(i);
			if (operation.sign == 1) {
				if (operation.object instanceof Shape) {
					Shape shape = (Shape)operation.object;
					if(shape instanceof Grid) {
						Grid grid = (Grid) shape;
						grid.updateMatrix();
					}
					this.model.document.getPage().getImage().addShape((Shape) operation.object);
				}
			} 
			else if (operation.sign == 0) {
				if (operation.object instanceof Shape) {
					Shape shape = (Shape)operation.object;
					this.model.document.getPage().getImage().removeShape(shape);
				}
			}
		}
		
	}

//	Image i = this.document.cache.releasedImage;
//	Position p = this.document.cache.pressedShape.position;
//	shape.position = new Position(p.absolutePoint,p.absoluteDimension,p.addScale,i.position.offset,i.position.margin);
}

//Point movedPoint = this.getMovedPoint(new Point(releasedPoint), new Point(pressedPoint));
//Shape newShape = null;
//if (pressedPage.contains(releasedPoint)) {
//	if (releasedImage != null && !pressedImage.equals(releasedImage)) {
//		newShape = pressedShape;// new Shape(pressedShape,true);//uncommented to fix bug
//		newShape.position.move(movedPoint);
//		// Testing new logic
//		newShape.position = new Position(new Point(newShape.position.getStartPoint()),
//				new Point(newShape.position.getStopPoint()), releasedImage.position.relativeScale,
//				releasedImage.position.scale, releasedImage.position.offset, releasedImage.position.margin);
//		newShape = new Shape(newShape, true);
//		pressedImage.removeShape(newShape.uuid);
//		releasedImage.addShape(newShape);
//		if(newShape instanceof Grid) {
//			((Grid)newShape).updateMatrix();
//		}
//	} else {
//		pressedShape.position.move(movedPoint);
//		newShape = pressedShape;
//		if(newShape instanceof Grid) {
//			((Grid)newShape).updateMatrix();
//		}
//	}
//} else {
//	throw new Exception("Release Point Invalid");
//}
