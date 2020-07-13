package com.meritoki.app.desktop.retina.model.document.command;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.meritoki.app.desktop.retina.model.document.Document;
import com.meritoki.app.desktop.retina.model.document.Grid;
import com.meritoki.app.desktop.retina.model.document.Shape;

public class SetGrid extends Command {
	private static Logger logger = LogManager.getLogger(SetGrid.class.getName());
	public SetGrid(Document document) {
		super(document, "setGrid");
	}
	
	@Override
	public void execute() throws Exception {
		logger.info("execute()");
		int row = document.cache.row;
		int column = document.cache.column;
		String shapeUUID = document.cache.shapeUUID;
		Shape shape = this.document.getPage().getShape();
		//undo
		Operation operation = new Operation();
		operation.object = (shape != null)?shape.uuid:null;
		operation.sign = 0;
		operation.id = UUID.randomUUID().toString();
		this.operationList.add(operation);
		//logic
		if(shapeUUID != null) {
			this.document.getPage().setShape(shapeUUID);
			shape = this.document.getPage().getShape();
			if(!(shape instanceof Grid)) {
				shape = new Grid(shape);
			}
			((Grid)shape).setRow(row);
			((Grid)shape).setColumn(column);
			((Grid)shape).initMatrix();
			((Grid)shape).updateMatrix();
			this.document.getPage().removeShape(shape);
			this.document.getPage().addShape(shape);
			
		}
		//redo
		operation = new Operation();
		operation.object = shapeUUID;
		operation.sign = 1;
		operation.id = UUID.randomUUID().toString();
		this.operationList.add(operation);
	}
}
