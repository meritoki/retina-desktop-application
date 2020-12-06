package com.meritoki.app.desktop.retina.model.command;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.meritoki.app.desktop.retina.model.Model;
import com.meritoki.app.desktop.retina.model.document.Grid;
import com.meritoki.app.desktop.retina.model.document.Page;
import com.meritoki.app.desktop.retina.model.document.Point;
import com.meritoki.app.desktop.retina.model.document.Selector;
import com.meritoki.app.desktop.retina.model.document.Shape;

public class MoveSelector extends Command {
	private static Logger logger = LogManager.getLogger(MoveSelector.class.getName());

	public MoveSelector(Model model) {
		super(model, "moveSelector");
	}
	
	@Override
	public void execute() throws Exception {
		String pressedPageUUID = this.model.cache.pressedPageUUID;
		Page pressedPage = this.model.document.getPage(pressedPageUUID);
		Point releasedPoint = this.model.cache.releasedPoint;
		Point pressedPoint = this.model.cache.pressedPoint;
		Selector selector = this.model.cache.selector;
		List<Shape> shapeList = selector.shapeList;
		Shape undoShape = null;
		Shape redoShape = null;
		Shape newShape = null;
		Selector newSelector = null;
		Operation operation = new Operation();
		this.model.cache.selector = null;
		// operation.uuid = this.document.cache.pressedShape.uuid;
//		this.operationList.push(operation);
		for(Shape pressedShape:shapeList) {
			
			if(pressedShape instanceof Grid) {
				Grid pressedGrid = (Grid)pressedShape;
				undoShape = new Grid(pressedGrid,true);
			} else {
				undoShape = new Shape(pressedShape,true);
			}
			// Undo
			operation = new Operation();
			operation.object = undoShape;
			operation.sign = 0;
			operation.id = UUID.randomUUID().toString();
			// operation.uuid = this.document.cache.pressedShape.uuid;
			this.operationList.push(operation);
			newShape = pressedPage.moveShape(pressedShape, pressedPoint, releasedPoint);
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
			operation.object = redoShape;//new Shape(newShape, true);
			operation.sign = 1;
			operation.id = UUID.randomUUID().toString();
			// operation.uuid = this.document.cache.pressedShape.uuid;
			this.operationList.push(operation);
		}
	}
	
	public Point getMovedPoint(Point a, Point b) {
		return new Point(a.x - b.x, a.y - b.y);
	}
	
	@Override
	public void undo() {
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
	public void redo() {
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
}

