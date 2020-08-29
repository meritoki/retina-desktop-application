package com.meritoki.app.desktop.retina.model.command;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.meritoki.app.desktop.retina.model.Model;
import com.meritoki.app.desktop.retina.model.document.Image;

public class SetImage extends Command {
	
	private static Logger logger = LogManager.getLogger(SetImage.class.getName());

	public SetImage(Model document) {
		super(document, "setImage");
	}
	
	public void execute() {
		logger.info("execute()");
		//variables
//		Point pressedPoint = document.cache.pressedPoint;
    	String pressedImageUUID = model.cache.pressedImageUUID;//for redo
    	String imageUUID = model.cache.imageUUID;//for undo
    	Image image = model.document.getImage(imageUUID);
    	//undo
		Operation operation = new Operation();
		operation.object = (image != null)?image.uuid:null;//(pressedImage != null)?pressedImage.uuid:null;
		operation.sign = 0;
		operation.id = UUID.randomUUID().toString();
		this.operationList.add(operation);
		//logic
		if(pressedImageUUID != null) {
			model.document.setImage(pressedImageUUID);
		}
		//redo
		operation = new Operation();
		operation.object = pressedImageUUID;
		operation.sign = 1;
		operation.id = UUID.randomUUID().toString();
		this.operationList.add(operation);
	}
}
