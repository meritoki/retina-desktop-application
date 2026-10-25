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
import com.meritoki.app.desktop.retina.model.document.Grid;
import com.meritoki.app.desktop.retina.model.document.Shape;

public class SetGrid extends Command {
	private static Logger logger = LogManager.getLogger(SetGrid.class.getName());
	
	public SetGrid(Model document) {
		super(document, "setGrid");
	}
	
	@Override // Command
	public void execute() {
		logger.info("execute()");
		// variables
		String shapeUUID = model.cache.shapeUUID;
		Shape shape = this.model.document.getPage().getShape();
		Grid grid = null;
		if(shape instanceof Grid) {
			grid = (Grid)shape;
			shape = grid.getShape();
		}
//		//undo
		Operation operation = new Operation();
		operation.object = (shape != null)?shape.uuid:null;
		operation.sign = 0;
		operation.id = UUID.randomUUID().toString();
		this.operationList.add(operation);
		//logic
		if(shapeUUID != null) {
			this.model.document.getPage().setGridShape(shapeUUID);
		}
		//redo
		operation = new Operation();
		operation.object = shapeUUID;
		operation.sign = 1;
		operation.id = UUID.randomUUID().toString();
		this.operationList.add(operation);
	}
	
	@Override
	public void undo() throws Exception {
		for(Operation o: this.operationList) {
			if(o.sign == 0) {
				if(o.object instanceof String) {
					String uuid = (String)o.object;
					Shape shape = this.model.document.getPage().getShape();
					if(shape instanceof Grid) {
						Grid grid = (Grid)shape;
						grid.setShape(uuid);
					}
				}
			}
		}
		
	}

	@Override
	public void redo() throws Exception {
		for(Operation o: this.operationList) {
			if(o.sign == 1) {
				if(o.object instanceof String) {
					String uuid = (String)o.object;
					Shape shape = this.model.document.getPage().getShape();
					if(shape instanceof Grid) {
						Grid grid = (Grid)shape;
						grid.setShape(uuid);
					}
				}
			}
		}
	}
}
