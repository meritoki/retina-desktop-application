package com.meritoki.app.desktop.retina.model.document.command;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.meritoki.app.desktop.retina.model.document.Document;
import com.meritoki.app.desktop.retina.model.document.Shape;

public class SetShape extends Command {
	private static Logger logger = LogManager.getLogger(SetShape.class.getName());

	public SetShape(Document document) {
		super(document, "setShape");
	}

	@Override // Command
	public void execute() {
		logger.info("execute()");
		// variables
		Shape pressedShape = this.document.cache.pressedShape;
		Shape currentShape = this.document.getPage().getShape();
		String shapeUUID = document.cache.shapeUUID;
		//undo
		Operation operation = new Operation();
		operation.object = (currentShape != null)?currentShape.uuid:null;
		operation.sign = 0;
		operation.id = UUID.randomUUID().toString();
		this.operationList.add(operation);
		//logic
		String uuid = null;
		if(pressedShape != null) {
			uuid = pressedShape.uuid;
			this.document.getPage().setShape(uuid);
		} else if(shapeUUID != null) {
			uuid = shapeUUID;
			this.document.getPage().setShape(uuid);
		}
		//redo
		operation = new Operation();
		operation.object = uuid;
		operation.sign = 1;
		operation.id = UUID.randomUUID().toString();
		this.operationList.add(operation);
	}
}
