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
		Shape currentShape = this.model.getShape();
		if(currentShape instanceof Grid) {
			Grid grid = (Grid)currentShape;
			currentShape = grid.getShape();
		}
//		//undo
		Operation operation = new Operation();
		operation.object = (currentShape != null)?currentShape.uuid:null;
		operation.sign = 0;
		operation.id = UUID.randomUUID().toString();
		this.operationList.add(operation);
		//logic
		if(shapeUUID != null) {
			this.model.setGrid(shapeUUID);
		}
		//redo
		operation = new Operation();
		operation.object = shapeUUID;
		operation.sign = 1;
		operation.id = UUID.randomUUID().toString();
		this.operationList.add(operation);
	}
}
