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
import com.meritoki.app.desktop.retina.model.document.Shape;

public class RemoveShape extends Command {
	
	private static Logger logger = LogManager.getLogger(RemoveShape.class.getName());
	
	public RemoveShape(Model document) {
		super(document, "removeShape");
	}
	
    @Override // Command
    public void execute() {
    	logger.info("execute()");
    	//variables
    	String pressedShapeUUID = this.model.cache.pressedShapeUUID;
    	Shape pressedShape = (pressedShapeUUID != null)?this.model.document.getShape(pressedShapeUUID):null;
		//undo
		Operation operation = new Operation();
		operation.object = (pressedShape != null)?new Shape(pressedShape,true):null;
		operation.sign = 0;
		operation.id = UUID.randomUUID().toString();
		this.operationList.push(operation);
		//logic
		this.model.document.getPage().removeShape(pressedShape);
    }
    
	@Override
	public void undo() throws Exception {
		for (int i = 0; i < this.operationList.size(); i++) {
			Operation operation = this.operationList.get(i);
			if (operation.sign == 1) {
				if (operation.object instanceof Shape) {
					this.model.document.getPage().removeShape((Shape) operation.object);
				}
			} else if (operation.sign == 0) {
				if (operation.object instanceof Shape) {
					this.model.document.getPage().getImage().addShape((Shape) operation.object);
				}
			}
		}
		
	}

	@Override
	public void redo() throws Exception {
		for (int i = 0; i < this.operationList.size(); i++) {
			Operation operation = this.operationList.get(i);
			if (operation.sign == 1) {
				if (operation.object instanceof Shape) {
					this.model.document.getPage().getImage().addShape((Shape) operation.object);
				}
			} else if (operation.sign == 0) {
				if (operation.object instanceof Shape) {
					this.model.document.getPage().getImage().removeShape((Shape) operation.object);
				}
			}
		}
		
	}
}
