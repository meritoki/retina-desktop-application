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
import com.meritoki.app.desktop.retina.model.provider.meritoki.Meritoki;
import com.meritoki.library.controller.memory.MemoryController;

public class SetPage extends Command {
	
	private static Logger logger = LogManager.getLogger(SetPage.class.getName());
	
	public SetPage(Model document) {
		super(document, "setPage");
	}
	
    @Override // Command
    public void execute() {
    	logger.info("execute()");
    	//variables
    	String pageUUID = model.cache.pageUUID;
    	int pageIndex = model.cache.pageIndex;
    	Page page = model.document.getPage();
    	//undo
    	Operation operation = new Operation();
		operation.object = (page != null)?page.uuid:null;
		operation.sign = 0;
		operation.id = UUID.randomUUID().toString();
		this.operationList.add(operation);
		//logic
    	if (pageUUID != null) {
    		this.model.document.setPage(pageUUID);
    		Meritoki meritoki = (Meritoki)this.model.system.providerMap.get("meritoki");
    		if(meritoki != null) {
    			meritoki.setIndex(pageUUID);
    		}
    	} else if(pageIndex >  -1) {
    		this.model.document.setIndex(pageIndex);
    		pageUUID = this.model.document.getPage().uuid;
    	}
    	//redo
    	operation = new Operation();
		operation.object = pageUUID;
		operation.sign = 1;
		operation.id = UUID.randomUUID().toString();
		this.operationList.add(operation);
		MemoryController.log();
    }
    
	@Override
	public void undo() throws Exception {
		for(Operation o: this.operationList) {
			if(o.sign == 0) {
				if(o.object instanceof String) {
					this.model.document.setPage((String)o.object);
				}
			}
		}
		
	}

	@Override
	public void redo() throws Exception {
		for(Operation o: this.operationList) {
			if(o.sign == 1) {
				if(o.object instanceof String) {
					this.model.document.setPage((String)o.object);
				}
			}
		}
	}
}
