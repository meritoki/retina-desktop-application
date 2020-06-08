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
		Point pressedPoint = document.cache.pressedPoint;
		int imageIndex = document.cache.imageIndex;
    	String imageUUID = document.cache.imageUUID;
    	//undo
		Operation operation = new Operation();
		operation.object = document.getPage().getIndex();//(pressedImage != null)?pressedImage.uuid:null;
		operation.sign = 0;
		operation.id = UUID.randomUUID().toString();
		this.operationList.add(operation);
		//logic
		if(pressedPoint != null) {
			document.cache.pressedImage = document.getImage(pressedPoint);
			document.setImage(document.cache.pressedImage.uuid);
		} else if(imageIndex > -1) {
			document.setImage(imageIndex);
		} else if(imageUUID != null) {
			document.setImage(imageUUID);
		}
		//redo
		operation = new Operation();
		operation.object = document.getPage().getIndex();//document.cache.pressedImage.uuid;
		operation.sign = 1;
		operation.id = UUID.randomUUID().toString();
		this.operationList.add(operation);
	}
}
