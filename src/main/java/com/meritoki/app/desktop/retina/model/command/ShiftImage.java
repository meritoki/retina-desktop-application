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
		Page page = this.model.document.getPage();
		Image pressedImage = this.model.cache.pressedImage;
		double margin = pressedImage.position.margin;
		double shiftFactor = this.model.cache.shiftFactor;
		char shiftOperator = this.model.cache.shiftOperator;
		//undo
		Operation operation = new Operation();
		operation.object = margin;
		operation.sign = 0;
		operation.id = UUID.randomUUID().toString();
		this.operationList.add(operation);
		//logic
		page.setBufferedImage(null);
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
		//redo
		operation = new Operation();
		operation.object = margin;
		operation.sign = 1;
		operation.id = UUID.randomUUID().toString();
		this.operationList.add(operation);
	}
}
