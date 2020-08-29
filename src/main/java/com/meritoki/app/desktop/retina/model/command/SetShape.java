package com.meritoki.app.desktop.retina.model.command;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.meritoki.app.desktop.retina.model.Model;
import com.meritoki.app.desktop.retina.model.document.Shape;

public class SetShape extends Command {
	private static Logger logger = LogManager.getLogger(SetShape.class.getName());

	public SetShape(Model document) {
		super(document, "setShape");
	}

	@Override // Command
	public void execute() {
		logger.info("execute()");
		// variables
		String pressedShapeUUID = model.cache.pressedShapeUUID;
		String shapeUUID = model.cache.shapeUUID;
		//undo
		Operation operation = new Operation();
		operation.object = shapeUUID;
		operation.sign = 0;
		operation.id = UUID.randomUUID().toString();
		this.operationList.add(operation);
		//logic
		if(pressedShapeUUID != null) {
			this.model.document.getPage().setShape(pressedShapeUUID);
		}
		//redo
		operation = new Operation();
		operation.object = pressedShapeUUID;
		operation.sign = 1;
		operation.id = UUID.randomUUID().toString();
		this.operationList.add(operation);
	}
}
