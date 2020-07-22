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
		String shapeUUID = model.cache.shapeUUID;
		Shape shape = this.model.document.getPage().getShape();
		//undo
		Operation operation = new Operation();
		operation.object = (shape != null)?shape.uuid:null;
		operation.sign = 0;
		operation.id = UUID.randomUUID().toString();
		this.operationList.add(operation);
		//logic
		if(shapeUUID != null) {
			this.model.document.getPage().setShape(shapeUUID);
		}
		//redo
		operation = new Operation();
		operation.object = shapeUUID;
		operation.sign = 1;
		operation.id = UUID.randomUUID().toString();
		this.operationList.add(operation);
	}
}
