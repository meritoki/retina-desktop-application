package com.meritoki.app.desktop.retina.model.command;

import java.util.UUID;

import com.meritoki.app.desktop.retina.model.Model;
import com.meritoki.app.desktop.retina.model.document.Image;

public class RemoveImage extends Command {
	public RemoveImage(Model document) {
		super(document, "removeImage");
	}
	
	public void execute() {
    	Image pressedImage = this.model.cache.pressedImage;
    	int imageIndex = this.model.document.getPage().getIndex(pressedImage.uuid);
    	Object[] objectArray = new Object[2];
    	objectArray[0] = imageIndex;
    	objectArray[1] = new Image(pressedImage);
    	//undo
		Operation operation = new Operation();
		operation.object = objectArray;
		operation.sign = 0;
		operation.id = UUID.randomUUID().toString();
		this.operationList.push(operation);
		//logic
		this.model.document.getPage().removeImage(pressedImage.uuid);
		this.model.document.getPage().setBufferedImage(null);
		//logic
		operation = new Operation();
		operation.object = pressedImage.uuid;
		operation.sign = 1;
		operation.id = UUID.randomUUID().toString();
		this.operationList.push(operation);
	}
}
