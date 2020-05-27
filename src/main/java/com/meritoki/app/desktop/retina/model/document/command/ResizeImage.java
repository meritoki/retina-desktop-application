package com.meritoki.app.desktop.retina.model.document.command;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.meritoki.app.desktop.retina.model.document.Document;
import com.meritoki.app.desktop.retina.model.document.Image;

public class ResizeImage extends Command {
	private static Logger logger = LogManager.getLogger(ResizeImage.class.getName());
	
	public ResizeImage(Document document) {
		super(document, "resizeImage");
	}
	
    @Override // Command
    public void execute() {
    	logger.info("execute()");
		//Undo Operation
    	Operation operation = new Operation();
		operation.object = new Image(this.document.cache.pressedImage);
		operation.sign = 0;
		operation.id = UUID.randomUUID().toString();
		operation.uuid = this.document.cache.pressedImage.uuid;
		this.operationList.push(operation);
		//Logic
		double scale = this.document.cache.pressedImage.scale;
		scale *= this.document.cache.scaleFactor;
		this.document.cache.pressedImage.scale = scale;
		
		//Redo Operation
		operation = new Operation();
		operation.object = new Image(this.document.cache.pressedImage);
		operation.sign = 1;
		operation.id = UUID.randomUUID().toString();
		operation.uuid = this.document.cache.pressedImage.uuid;
		this.operationList.push(operation);
    }
}
