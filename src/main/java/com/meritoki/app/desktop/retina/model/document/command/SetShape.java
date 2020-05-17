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
    	this.user = this.document.state.user;

//		Operation operation = new Operation();
//		operation.object = new Shape(this.model.getDocument().getProject().getPage().getShape());
//		operation.sign = 0;
//		operation.id = UUID.randomUUID().toString();
//		operation.uuid = this.document.state.pressedShape.uuid;
//		this.operationList.add(operation);
	
    	if(this.document.state.pressedShape != null) {
	    	this.document.getPage().setShape(this.document.state.pressedShape.uuid);
			Operation operation = new Operation();
			operation.object = new Shape(this.document.getPage().getShape());
			operation.sign = 0;
			operation.id = UUID.randomUUID().toString();
			operation.uuid = this.document.state.pressedShape.uuid;
			this.operationList.add(operation);
    	}
    }
}
