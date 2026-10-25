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

public class RemovePage extends Command {

	private static Logger logger = LogManager.getLogger(RemovePage.class.getName());
	
	public RemovePage(Model document) {
		super(document, "removePage");
	}
	
	@Override
	public void execute() {
    	logger.info("execute()");
    	String pressedPageUUID = this.model.cache.pressedPageUUID;
    	Page pressedPage = this.model.document.getPage(pressedPageUUID);
    	int pageIndex = this.model.document.getIndex(pressedPage.uuid);
    	Object[] objectArray = new Object[2];
    	objectArray[0] = pageIndex;
    	objectArray[1] = new Page(pressedPage);
    	//undo
		Operation operation = new Operation();
		operation.object = objectArray;
		operation.sign = 0;
		operation.id = UUID.randomUUID().toString();
		this.operationList.push(operation);
		//logic
		this.model.document.removePage(pressedPage.uuid);
		//redo
		operation = new Operation();
		operation.object = pressedPage.uuid;
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
					Page image = (Page)objectArray[1];
					this.model.document.pageList.add(index, image);
				}
			}
		}
		
	}

	@Override
	public void redo() throws Exception {
		for(Operation o: this.operationList) {
			if(o.sign == 1) {
				if(o.object instanceof String) {
					this.model.document.removePage((String)o.object);
				}
			}
		}
		
	}
}
