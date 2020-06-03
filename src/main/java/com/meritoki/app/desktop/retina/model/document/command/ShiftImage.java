package com.meritoki.app.desktop.retina.model.document.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.meritoki.app.desktop.retina.model.document.Document;
import com.meritoki.app.desktop.retina.model.document.Image;
import com.meritoki.app.desktop.retina.model.document.Page;
import com.meritoki.app.desktop.retina.model.document.Position;

public class ShiftImage extends Command {
	
	private static Logger logger = LogManager.getLogger(ShiftImage.class.getName());

	public ShiftImage(Document document) {
		super(document, "shiftImage");
	}
	
	public void execute() {
		logger.info("execute()");
		//variables
		Page pressedPage = this.document.cache.pressedPage;
		Image pressedImage = this.document.cache.pressedImage;
		double margin = pressedImage.position.margin;
		double shiftFactor = this.document.cache.shiftFactor;
		char shiftOperator = this.document.cache.shiftOperator;
		
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
	}
}
