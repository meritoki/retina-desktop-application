package com.meritoki.app.desktop.retina.model.document.command;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.meritoki.app.desktop.retina.model.document.Document;
import com.meritoki.app.desktop.retina.model.document.Image;
import com.meritoki.app.desktop.retina.model.document.Page;

public class ResizeImage extends Command {
	private static Logger logger = LogManager.getLogger(ResizeImage.class.getName());
	
	public ResizeImage(Document document) {
		super(document, "resizeImage");
	}
	
    @Override // Command
    public void execute() {
    	logger.info("execute()");
		//variable
    	Page page = this.document.getPage();
    	Image pressedImage = this.document.cache.pressedImage;//new Image(this.document.cache.pressedImage);
    	double scale = page.position.scale;
    	double scaleFactor = this.document.cache.scaleFactor;
    	char scaleOperator = this.document.cache.scaleOperator;
    	//undo
    	Operation operation = new Operation();
		operation.object = this.document.cache.pressedImage.position.relativeScale;//new Image(this.document.cache.pressedImage);
		operation.sign = 0;
		operation.id = UUID.randomUUID().toString();
		this.operationList.push(operation);
		//Logic
		page.setBufferedImage(null);
		pressedImage.setBufferedImage(null);
		double relativeScale = (scale == pressedImage.position.relativeScale)?scale/pressedImage.position.relativeScale:pressedImage.position.relativeScale;//This LINE IS ESSENTIAL TO FIXING THE BUG
		switch(scaleOperator) {
		case '*':{
			relativeScale *= scaleFactor;
			break;
		}
		case '/':{
			relativeScale /= scaleFactor;
			break;
		}
		}
		pressedImage.setRelativeScale(relativeScale);
		page.getBufferedImage();
		//Redo
		operation = new Operation();
		operation.object = pressedImage.position.relativeScale;
		operation.sign = 1;
		operation.id = UUID.randomUUID().toString();
		this.operationList.push(operation);
    }
}
