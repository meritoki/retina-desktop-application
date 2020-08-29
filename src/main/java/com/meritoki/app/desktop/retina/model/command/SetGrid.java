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
	
	@Override // Command
	public void execute() {
		logger.info("execute()");
		// variables
		String shapeUUID = model.cache.shapeUUID;
		Shape shape = this.model.document.getPage().getShape();
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
			this.model.document.getPage().setGridShape(shapeUUID);
		}
		//redo
		operation = new Operation();
		operation.object = shapeUUID;
		operation.sign = 1;
		operation.id = UUID.randomUUID().toString();
		this.operationList.add(operation);
	}
}
