package com.meritoki.app.desktop.retina.model.document.command;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.meritoki.app.desktop.retina.model.document.Document;
import com.meritoki.app.desktop.retina.model.document.Image;
import com.meritoki.app.desktop.retina.model.document.Point;

public class SetImage extends Command {
	
	private static Logger logger = LogManager.getLogger(SetImage.class.getName());

	public SetImage(Document document) {
		super(document, "setImage");
	}
	
	public void execute() {
		logger.info("execute()");
		//variables
		Image pressedImage = document.cache.pressedImage;
		Point pressedPoint = document.cache.pressedPoint;
    	//undo
		Operation operation = new Operation();
		operation.object = (pressedImage != null)?pressedImage.uuid:null;
		operation.sign = 0;
		operation.id = UUID.randomUUID().toString();
		this.operationList.add(operation);
		//logic
		document.cache.pressedImage = document.getImage(pressedPoint);
		document.setImage(document.cache.pressedImage.uuid);
		//redo
		operation = new Operation();
		operation.object = document.cache.pressedImage.uuid;
		operation.sign = 1;
		operation.id = UUID.randomUUID().toString();
		this.operationList.add(operation);
	}
}
