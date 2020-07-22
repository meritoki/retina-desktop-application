package com.meritoki.app.desktop.retina.model.command;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.meritoki.app.desktop.retina.model.Model;
import com.meritoki.app.desktop.retina.model.document.Grid;
import com.meritoki.app.desktop.retina.model.document.Shape;

public class SetGrid extends Command {
	private static Logger logger = LogManager.getLogger(SetGrid.class.getName());

	public SetGrid(Model document) {
		super(document, "setGrid");
	}

	@Override
	public void execute() throws Exception {
		logger.info("execute()");
		int row = model.cache.row;
		int column = model.cache.column;
		String shapeUUID = model.cache.shapeUUID;
		Shape shape = this.model.document.getPage().getShape();
		Grid grid = null;
		// undo
		if (row > 1 || column > 1) {
			if (shapeUUID != null) {
				Operation operation = new Operation();
				operation.object = new Shape(shape, true);// (shape != null)?shape.uuid:null;
				operation.sign = 0;
				operation.id = UUID.randomUUID().toString();
				this.operationList.add(operation);
				// logic
				this.model.document.getPage().setShape(shapeUUID);
				shape = this.model.document.getPage().getShape();
				if (!(shape instanceof Grid)) {
					grid = new Grid(shape, row, column);
				} else {
					grid = (Grid) shape;
				}
				this.model.document.getPage().removeShape(shape);
				this.model.document.getPage().addShape(grid);
				// redo
				operation = new Operation();
				operation.object = new Grid(grid, true);// shapeUUID;
				operation.sign = 1;
				operation.id = UUID.randomUUID().toString();
				this.operationList.add(operation);
			} else {
				throw new Exception("shape uuid is null");
			}
		} else {
			throw new Exception("row and column less than or equal to 1");
		}
	}
}
