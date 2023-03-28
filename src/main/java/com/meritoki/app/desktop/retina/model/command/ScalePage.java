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
import com.meritoki.app.desktop.retina.model.document.Page;

public class ScalePage extends Command {

	private static Logger logger = LogManager.getLogger(ScalePage.class.getName());
	
	public ScalePage(Model document) {
		super(document, "scalePage");
	}
	
    @Override // Command
    public void execute() {
    	logger.debug("execute()");
    	//Variables
    	String pressedPageUUID = this.model.cache.pressedPageUUID;
		double scaleFactor = this.model.cache.scaleFactor;
		char scaleOperator = this.model.cache.scaleOperator;
    	
    	Page page = this.model.document.getPage(pressedPageUUID);
		double scale = page.position.scale;

    	//Undo
    	Operation operation = new Operation();
		operation.object = scale;
		operation.sign = 0;
		operation.id = UUID.randomUUID().toString();
		this.operationList.add(operation);
		//logic
		switch(scaleOperator) {
		case '*':{
			scale *= scaleFactor;
			break;
		}
		case '/':{
			scale /= scaleFactor;
			break;
		}
		}
		page.setScale(scale);
		//redo
    	operation = new Operation();
		operation.object = scale;
		operation.sign = 1;
		operation.id = UUID.randomUUID().toString();
		this.operationList.add(operation);
    }
    
	@Override
	public void undo() throws Exception {
		for(Operation o: this.operationList) {
			if(o.sign == 0) {
				if(o.object instanceof Double) {
					this.model.document.getPage().setScale((double)o.object);
				}
			}
		}
		
	}

	@Override
	public void redo() throws Exception {
		for(Operation o: this.operationList) {
			if(o.sign == 1) {
				if(o.object instanceof Double) {
					this.model.document.getPage().setScale((double)o.object);
				}
			}
		}
	}
}
