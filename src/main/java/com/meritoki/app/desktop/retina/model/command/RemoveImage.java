package com.meritoki.app.desktop.retina.model.command;

import java.util.UUID;

import com.meritoki.app.desktop.retina.model.Model;
import com.meritoki.app.desktop.retina.model.document.Image;
import com.meritoki.app.desktop.retina.model.document.Page;

public class RemoveImage extends Command {
	public RemoveImage(Model document) {
		super(document, "removeImage");
	}
	
	public void execute() {
		String pressedPageUUID = this.model.cache.pressedPageUUID;
		String pressedImageUUID = this.model.cache.pressedImageUUID;
		Page pressedPage = this.model.document.getPage(pressedPageUUID);
    	Image pressedImage = this.model.document.getImage(pressedImageUUID);
    	int imageIndex = pressedPage.getIndex(pressedImage.uuid);
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
		pressedPage.removeImage(pressedImage.uuid);
		pressedPage.setBufferedImage(null);
		//logic
		operation = new Operation();
		operation.object = pressedImage.uuid;
		operation.sign = 1;
		operation.id = UUID.randomUUID().toString();
		this.operationList.push(operation);
	}
}
