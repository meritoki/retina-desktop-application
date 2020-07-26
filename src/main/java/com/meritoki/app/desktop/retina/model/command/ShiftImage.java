package com.meritoki.app.desktop.retina.model.command;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.meritoki.app.desktop.retina.model.Model;
import com.meritoki.app.desktop.retina.model.document.Image;
import com.meritoki.app.desktop.retina.model.document.Page;

public class ShiftImage extends Command {
	
	private static Logger logger = LogManager.getLogger(ShiftImage.class.getName());

	public ShiftImage(Model document) {
		super(document, "shiftImage");
	}
	
	public void execute() {
		logger.info("execute()");
		//variables
//		Page page = this.model.document.getPage();
//		Image pressedImage = this.model.document.getImage(this.model.cache.pressedImage.uuid);
		Page pressedPage = this.model.document.getPage(this.model.cache.pressedPageUUID);
		Image pressedImage = this.model.document.getImage(this.model.cache.pressedImageUUID);
		double margin = pressedImage.position.margin;
		double shiftFactor = this.model.cache.shiftFactor;
		char shiftOperator = this.model.cache.shiftOperator;
		//undo
		Operation operation = new Operation();
		operation.object = new Double(margin);
		operation.sign = 0;
		operation.id = UUID.randomUUID().toString();
		this.operationList.add(operation);
		//logic
		pressedPage.setBufferedImage(null);
		switch(shiftOperator) {
		case '+':{
			margin += shiftFactor;
			break;
		}
		case '-':{
			margin -= shiftFactor;
			break;
		}
		}
		pressedImage.setMargin(margin);
		System.out.println(pressedImage);
		//redo
		operation = new Operation();
		operation.object = margin;
		operation.sign = 1;
		operation.id = UUID.randomUUID().toString();
		this.operationList.add(operation);
	}
}
