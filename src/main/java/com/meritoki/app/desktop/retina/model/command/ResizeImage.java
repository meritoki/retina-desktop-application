package com.meritoki.app.desktop.retina.model.command;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.meritoki.app.desktop.retina.model.Model;
import com.meritoki.app.desktop.retina.model.document.Image;
import com.meritoki.app.desktop.retina.model.document.Page;

public class ResizeImage extends Command {
	private static Logger logger = LogManager.getLogger(ResizeImage.class.getName());
	
	public ResizeImage(Model document) {
		super(document, "resizeImage");
	}
	
    @Override // Command
    public void execute() {
    	logger.info("execute()");
		//variable
    	Page page = this.model.document.getPage();
    	Image pressedImage = this.model.cache.pressedImage;//new Image(this.document.cache.pressedImage);
    	double scale = page.position.scale;
    	double scaleFactor = this.model.cache.scaleFactor;
    	char scaleOperator = this.model.cache.scaleOperator;
    	//undo
    	Operation operation = new Operation();
		operation.object = this.model.cache.pressedImage.position.relativeScale;//new Image(this.document.cache.pressedImage);
		operation.sign = 0;
		operation.id = UUID.randomUUID().toString();
		this.operationList.push(operation);
		//Logic
		//Both set to null, to invoke a reload inside of the classes
		//Now the bufferedImage must be passed to the classes from Document;
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
		this.model.document.setBufferedImage(page);
		
////		this.document.setBufferedImage(pressedImage);
//		page.getBufferedImage();
		//Redo
		operation = new Operation();
		operation.object = pressedImage.position.relativeScale;
		operation.sign = 1;
		operation.id = UUID.randomUUID().toString();
		this.operationList.push(operation);
    }
}
