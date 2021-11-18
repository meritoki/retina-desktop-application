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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.meritoki.app.desktop.retina.model.Model;
import com.meritoki.app.desktop.retina.model.document.Image;
import com.meritoki.app.desktop.retina.model.document.Page;

public class ScaleImage extends Command {
	private static Logger logger = LogManager.getLogger(ScaleImage.class.getName());
	
	public ScaleImage(Model document) {
		super(document, "scaleImage");
	}
	
    @Override // Command
    public void execute() {
    	logger.info("execute()");
		//variable
    	String pressedPageUUID = this.model.cache.pressedPageUUID;
    	String pressedImageUUID = this.model.cache.pressedImageUUID;
    	double scaleFactor = this.model.cache.scaleFactor;
    	char scaleOperator = this.model.cache.scaleOperator;
    	
    	Page page = this.model.document.getPage(pressedPageUUID);
    	Image pressedImage = this.model.document.getImage(pressedImageUUID);
    	double scale = page.position.scale;
    	//undo
    	Object[] objectArray = new Object[3];
    	objectArray[0] = pressedImage.position.relativeScale;
    	objectArray[1] = pressedImageUUID;
    	objectArray[2] = pressedPageUUID;
    	Operation operation = new Operation();
		operation.object = objectArray;//pressedImage.position.relativeScale;
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
		page.getBufferedImage(model);
		
//		this.model.document.setBufferedImage(page);

		//Redo
		operation = new Operation();
		operation.object = pressedImage.position.relativeScale;
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
					this.model.document.setImage((String)objectArray[1]);
					this.model.document.setPage((String)objectArray[2]);
					this.model.document.getPage().setBufferedImage(null);
					this.model.document.getImage().setBufferedImage(null);
					this.model.document.getImage().setRelativeScale((double)objectArray[0]);							
				}
			}
		}
		
	}

	@Override
	public void redo() throws Exception {
		for(Operation o: this.operationList) {
			if(o.sign == 1) {
				if(o.object instanceof Double) {
					this.model.document.getPage().setBufferedImage(null);
					this.model.document.getImage().setBufferedImage(null);
					this.model.document.getImage().setRelativeScale((double)o.object);
				}
			}
		}
	}
}
