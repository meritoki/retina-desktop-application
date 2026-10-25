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
import com.meritoki.app.desktop.retina.model.provider.meritoki.Meritoki;

public class SetShape extends Command {
	private static Logger logger = LogManager.getLogger(SetShape.class.getName());

	public SetShape(Model document) {
		super(document, "setShape");
	}

	@Override // Command
	public void execute() {
		logger.info("execute()");
		// variables
		String pressedShapeUUID = model.cache.pressedShapeUUID;
		String shapeUUID = model.cache.shapeUUID;
		//undo
		Operation operation = new Operation();
		operation.object = shapeUUID;
		operation.sign = 0;
		operation.id = UUID.randomUUID().toString();
		this.operationList.add(operation);
		//logic
		if(pressedShapeUUID != null) {
			this.model.document.getPage().setShape(pressedShapeUUID);
			Meritoki meritoki = (Meritoki)this.model.system.providerMap.get("meritoki");
    		if(meritoki != null) {
    			meritoki.setIndex(pressedShapeUUID);
    		}
		}
		//redo
		operation = new Operation();
		operation.object = pressedShapeUUID;
		operation.sign = 1;
		operation.id = UUID.randomUUID().toString();
		this.operationList.add(operation);
	}
	
	@Override
	public void undo() throws Exception {
		for (int i = 0; i < this.operationList.size(); i++) {
			Operation operation = this.operationList.get(i);
			if (operation.sign == 0) {
				if (operation.object instanceof String) {
					this.model.document.getPage().setShape((String) operation.object);
				}
			}
		}
	}

	@Override
	public void redo() throws Exception {
		for (int i = 0; i < this.operationList.size(); i++) {
			Operation operation = this.operationList.get(i);
			if (operation.sign == 1) {
				if (operation.object instanceof String) {
					this.model.document.getPage().setShape((String) operation.object);
				}
			}
		}
	}
}
