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

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.meritoki.app.desktop.retina.controller.node.NodeController;
import com.meritoki.app.desktop.retina.model.Model;
import com.meritoki.app.desktop.retina.model.document.Image;
import com.meritoki.app.desktop.retina.model.document.Page;
import com.meritoki.app.desktop.retina.model.provider.meritoki.Meritoki;
import com.meritoki.library.controller.memory.MemoryController;

public class AddPage extends Command {
	
	private static Logger logger = LogManager.getLogger(AddPage.class.getName());
	
	public AddPage(Model model) {
		super(model, "addPage");
	}
	
    @Override
    public void execute() {
    	logger.info("execute()");
//    	Meritoki meritoki = (Meritoki)this.model.system.providerMap.get("meritoki");
//		if(meritoki != null) {
//			meritoki.update();
//		}
    	//undo
    	Operation operation = new Operation();
    	operation.object = new ArrayList<Page>(this.model.document.pageList);
    	operation.id = UUID.randomUUID().toString();
    	operation.sign = 0;
    	this.operationList.push(operation);	
    	//logic
    	String[] fileArray = this.model.cache.fileArray;
    	for(String f: fileArray) {
    		File file = new File(f);
    		if(file.getName().contains(".pdf")) {
    			File[] pageArray = NodeController.openPDF(NodeController.getDocumentCache(this.model.document.uuid),file);
    			for(File p:pageArray) {
    				Page page = new Page();
    				page.imageList.add(new Image(p));
    	    		this.model.document.addPage(page);
//    	    		meritoki.input(page);
    	    		page.setBufferedImageNull();
    	    		MemoryController.log();
    			}
    		} else {
	    		Page page = new Page();
				page.imageList.add(new Image(file));
	    		this.model.document.addPage(page);
//	    		meritoki.input(page);
	    		page.getBufferedImage(this.model);
	    		page.setBufferedImageNull();
	    		MemoryController.log();
    		}
    	}
    	
    	Meritoki meritoki = (Meritoki)this.model.system.providerMap.get("meritoki");
		if(meritoki != null) {
			meritoki.update();
		}
    	
    	//redo
    	operation = new Operation();
    	operation.object = this.model.document.pageList;
    	operation.id = UUID.randomUUID().toString();
    	operation.sign = 1;
    	this.operationList.push(operation);
    }
    
	@Override
	public void undo() throws Exception {
		for (int i = 0; i < this.operationList.size(); i++) {
			Operation operation = this.operationList.get(i);
			if (operation.sign == 0) {
				if (operation.object instanceof List) {
					this.model.document.pageList = (List<Page>)operation.object;
				}
			}
		}
	}

	@Override
	public void redo() throws Exception {
		for (int i = 0; i < this.operationList.size(); i++) {
			Operation operation = this.operationList.get(i);
			if (operation.sign == 1) {
				if (operation.object instanceof List) {
					this.model.document.pageList = (List<Page>)operation.object;
				}
			}
		}
	}
}
