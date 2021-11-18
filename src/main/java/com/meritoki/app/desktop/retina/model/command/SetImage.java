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

public class SetImage extends Command {
	
	private static Logger logger = LogManager.getLogger(SetImage.class.getName());

	public SetImage(Model document) {
		super(document, "setImage");
	}
	
	public void execute() {
		logger.info("execute()");
		//variables
//		Point pressedPoint = document.cache.pressedPoint;
    	String pressedImageUUID = model.cache.pressedImageUUID;//for redo
    	String imageUUID = model.cache.imageUUID;//for undo
    	Image image = model.document.getImage(imageUUID);
    	//undo
		Operation operation = new Operation();
		operation.object = (image != null)?image.uuid:null;//(pressedImage != null)?pressedImage.uuid:null;
		operation.sign = 0;
		operation.id = UUID.randomUUID().toString();
		this.operationList.add(operation);
		//logic
		if(pressedImageUUID != null) {
			model.document.setImage(pressedImageUUID);
		}
		//redo
		operation = new Operation();
		operation.object = pressedImageUUID;
		operation.sign = 1;
		operation.id = UUID.randomUUID().toString();
		this.operationList.add(operation);
	}
	
	@Override
	public void undo() throws Exception {
		for(Operation o: this.operationList) {
			if(o.sign == 0) {
				if(o.object instanceof String) {
					this.model.document.setImage((String)o.object);
				}
			}
		}
	}

	@Override
	public void redo() throws Exception {
		for(Operation o: this.operationList) {
			if(o.sign == 1) {
				if(o.object instanceof String) {
					this.model.document.setImage((String)o.object);
				}
			}
		}
	}
}
