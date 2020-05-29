package com.meritoki.app.desktop.retina.model.document.command;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.meritoki.app.desktop.retina.model.Model;
import com.meritoki.app.desktop.retina.model.document.Document;
import com.meritoki.app.desktop.retina.model.document.Point;
import com.meritoki.app.desktop.retina.model.document.Shape;

public class ResizeShape extends Command {
	
	private static Logger logger = LogManager.getLogger(ResizeShape.class.getName());
	
	public ResizeShape(Document document) {
		super(document, "resizeShape");
	}
	
    @Override // Command
    public void execute() {
    	logger.info("execute()");
    	this.user = this.document.cache.user;
		Operation operation = new Operation();
		operation.object = new Shape(this.document.cache.pressedShape,true);
		operation.sign = 0;
		operation.id = UUID.randomUUID().toString();
		operation.uuid = this.document.cache.pressedShape.uuid;
		this.operationList.push(operation);
		Point releasedPoint = new Point(this.document.cache.releasedPoint);
		Shape pressedShape = this.document.cache.pressedShape;
		pressedShape.position.resizePoint(releasedPoint, this.document.cache.selection);
		operation = new Operation();
		operation.object = new Shape(this.document.cache.pressedShape,true);
		operation.sign = 1;
		operation.id = UUID.randomUUID().toString();
		operation.uuid = this.document.cache.pressedShape.uuid;
		this.operationList.push(operation);
    }
}
