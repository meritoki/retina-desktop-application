package com.meritoki.app.desktop.retina.model.document.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.meritoki.app.desktop.retina.model.document.Document;
import com.meritoki.app.desktop.retina.model.document.Image;
import com.meritoki.app.desktop.retina.model.document.Page;
import com.meritoki.app.desktop.retina.model.document.Position;

public class ShiftMargin extends Command {
	
	private static Logger logger = LogManager.getLogger(ShiftMargin.class.getName());

	public ShiftMargin(Document document) {
		super(document, "shiftMargin");
	}
	
	public void execute() {
		logger.info("execute()");
		Page pressedPage = this.document.cache.pressedPage;
		Image pressedImage = this.document.cache.pressedImage;
		pressedPage.setBufferedImage(null);
		Position position = pressedImage.position;
		double margin = position.margin;
		double shiftFactor = this.document.cache.shiftFactor;
		switch(this.document.cache.shiftOperator) {
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
