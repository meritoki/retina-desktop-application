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
import com.meritoki.app.desktop.retina.model.document.Guide;
import com.meritoki.app.desktop.retina.model.document.Image;
import com.meritoki.app.desktop.retina.model.document.Page;
import com.meritoki.app.desktop.retina.model.document.Point;
import com.meritoki.app.desktop.retina.model.document.Position;
import com.meritoki.app.desktop.retina.model.document.Shape;
import com.meritoki.app.desktop.retina.model.document.ShapeType;
import com.meritoki.app.desktop.retina.model.provider.meritoki.Meritoki;

public class AddGuide extends Command {

	private static Logger logger = LogManager.getLogger(AddGuide.class.getName());

	public AddGuide(Model document) {
		super(document, "addShape");
	}

	@Override
	public void execute() throws Exception {
		logger.info("execute()");
		// variable
		String pressedPageUUID = this.model.cache.pressedPageUUID;
		String pressedImageUUID = this.model.cache.pressedImageUUID;
		
		Page page = this.model.document.getPage(pressedPageUUID);
		Image pressedImage = this.model.document.getImage(pressedImageUUID);
		ShapeType type = this.model.cache.type;
		Point pressedPoint = this.model.cache.pressedPoint;
		Point releasedPoint = this.model.cache.releasedPoint;
		
		double scale = page.position.scale;
		Guide shape = new Guide();
//		if (this.minimumSize(pressedPoint, releasedPoint, scale)) {
		shape.type = type;
		shape.position = new Position(new Point(pressedPoint), new Point(releasedPoint),
				pressedImage.position.relativeScale, scale, pressedImage.position.offset, pressedImage.position.margin);
		this.model.document.addShape(shape);
		shape.bufferedImage = this.model.document.getShapeBufferedImage(page.getScaledBufferedImage(this.model), shape);
    	Meritoki meritoki = (Meritoki)this.model.system.providerMap.get("meritoki");
		if(meritoki != null) {
			meritoki.update();
		}
		Operation operation = new Operation();
		operation.object = new Shape(shape, true);
		operation.sign = 1;
		operation.id = UUID.randomUUID().toString();
		this.operationList.push(operation);
//		} else {
//			throw new Exception("Shape too small");
//		}
	}

	public boolean minimumSize(Point a, Point b, double scale) {
		boolean flag = false;
		double width = Math.abs(b.x - a.x) * scale;
		double height = Math.abs(b.y - a.y) * scale;
		double size = 512 * scale;
		if ((width * height) > size) {
			flag = true;
		}
		return flag;
	}
	
	@Override
	public void undo() throws Exception {
		logger.info("undo()");
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
		for (int i = this.operationList.size() - 1; i >= 0; i--) {
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
