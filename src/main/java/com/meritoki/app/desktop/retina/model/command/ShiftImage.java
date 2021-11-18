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
		//redo
		operation = new Operation();
		operation.object = margin;
		operation.sign = 1;
		operation.id = UUID.randomUUID().toString();
		this.operationList.add(operation);
	}
	
	@Override
	public void undo() throws Exception {
		for(Operation o: this.operationList) {
			if(o.sign == 0) {
				if(o.object instanceof Double) {
					this.model.document.getPage().setBufferedImage(null);
					this.model.document.getPage().getImage().setMargin((double)o.object);
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
					this.model.document.getPage().getImage().setMargin((double)o.object);
				}
			}
		}
	}
}
