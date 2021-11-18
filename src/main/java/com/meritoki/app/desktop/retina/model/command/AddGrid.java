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

public class AddGrid extends Command {
	private static Logger logger = LogManager.getLogger(AddGrid.class.getName());

	public AddGrid(Model document) {
		super(document, "addGrid");
	}

	@Override
	public void execute() throws Exception {
		logger.info("execute()");
		int row = model.cache.row;
		int column = model.cache.column;
		String shapeUUID = model.cache.shapeUUID;
		Shape shape = this.model.document.getPage().getShape();
		Grid grid = null;
		// undo
		if (row > 1 || column > 1) {
			if (shapeUUID != null) {
				Operation operation = new Operation();
				operation.object = new Shape(shape, true);// (shape != null)?shape.uuid:null;
				operation.sign = 0;
				operation.id = UUID.randomUUID().toString();
				this.operationList.add(operation);
				// logic
				this.model.document.getPage().setShape(shapeUUID);
				shape = this.model.document.getPage().getShape();
				if (!(shape instanceof Grid)) {
					grid = new Grid(shape, row, column);
				} else {
					grid = (Grid) shape;
				}
				this.model.document.getPage().removeShape(shape);
				this.model.document.getPage().addShape(grid);
				// redo
				operation = new Operation();
				operation.object = new Grid(grid, true);// shapeUUID;
				operation.sign = 1;
				operation.id = UUID.randomUUID().toString();
				this.operationList.add(operation);
			} else {
				throw new Exception("shape uuid is null");
			}
		} else {
			throw new Exception("row and column less than or equal to 1");
		}
	}
	
	@Override
	public void undo() throws Exception {
		for(Operation o: this.operationList) {
			if(o.sign == 0) {
				if(o.object instanceof Shape) {
					Shape shape = (Shape)o.object;
					this.model.document.getPage().removeShape(shape);
					this.model.document.getPage().addShape(shape);
				}
			}
		}
		
	}

	@Override
	public void redo() throws Exception {
		for(Operation o: this.operationList) {
			if(o.sign == 1) {
				if(o.object instanceof Grid) {
					Grid grid = (Grid)o.object;
					this.model.document.getPage().removeShape(grid);
					this.model.document.getPage().addShape(grid);
				}
			}
		}
	}
}
