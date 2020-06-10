package com.meritoki.app.desktop.retina.model.document.command;

import java.util.UUID;

import com.meritoki.app.desktop.retina.model.document.Document;
import com.meritoki.app.desktop.retina.model.document.Image;
import com.meritoki.app.desktop.retina.model.document.Page;

public class RemoveImage extends Command {
	public RemoveImage(Document document) {
		super(document, "removeImage");
	}
	
	public void execute() {
    	Image pressedImage = this.document.cache.pressedImage;
    	//undo
		Operation operation = new Operation();
		operation.object = new Image(pressedImage);
		operation.sign = 0;
		operation.id = UUID.randomUUID().toString();
		this.operationList.push(operation);
		//logic
		this.document.getPage().removeImage(pressedImage.uuid);
		this.document.getPage().setBufferedImage(null);
	}
}
