package com.meritoki.app.desktop.retina.model.document.command;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.meritoki.app.desktop.retina.model.document.Document;
import com.meritoki.app.desktop.retina.model.document.Grid;
import com.meritoki.app.desktop.retina.model.document.Shape;

public class SetGridShape extends Command {
	private static Logger logger = LogManager.getLogger(SetGridShape.class.getName());
	
	public SetGridShape(Document document) {
		super(document, "setGridShape");
	}
	
	@Override // Command
	public void execute() {
		logger.info("execute()");
		// variables
		String shapeUUID = document.cache.shapeUUID;
		Shape shape = this.document.getPage().getShape();
		Grid grid = null;
		if(shape instanceof Grid) {
			grid = (Grid)shape;
			shape = grid.getShape();
		}
//		//undo
		Operation operation = new Operation();
		operation.object = (shape != null)?shape.uuid:null;
		operation.sign = 0;
		operation.id = UUID.randomUUID().toString();
		this.operationList.add(operation);
		//logic
		if(shapeUUID != null) {
			this.document.getPage().setGridShape(shapeUUID);
		}
		//redo
		operation = new Operation();
		operation.object = shapeUUID;
		operation.sign = 1;
		operation.id = UUID.randomUUID().toString();
		this.operationList.add(operation);
	}
}
