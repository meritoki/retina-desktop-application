/*
 * Copyright 2021 Joaquin Osvaldo Rodriguez
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
	
	@Override
	public void undo() throws Exception {
		for(Operation o: this.operationList) {
			if(o.sign == 0) {
				if(o.object instanceof Object[]) {
					Object[] objectArray = (Object[])o.object;
					int index = (int)objectArray[0];
					Image image = (Image)objectArray[1];
					this.model.document.getPage().setBufferedImage(null);
					this.model.document.getPage().imageList.add(index, image);
				}
			}
		}
		
	}

	@Override
	public void redo() throws Exception {
		for(Operation o: this.operationList) {
			if(o.sign == 1) {
				if(o.object instanceof String) {
					this.model.document.getPage().setBufferedImage(null);
					this.model.document.getPage().removeImage((String)o.object);
				}
			}
		}
	}
}
