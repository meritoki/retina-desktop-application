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
package com.meritoki.app.desktop.retina.model.document;

import java.util.ArrayList;
import java.util.List;

public class Selector extends Shape {
	
	public List<Shape> shapeList = new ArrayList<>();
	
	public Selector() {}
	
	public Selector(Selector selector,boolean flag) {
		super(selector,flag);
		this.shapeList = this.copyShapeList(selector.shapeList);
	}
	
	public void addShape(Shape shape) {
		if(this.position.containsPosition(shape.position)) {
			shapeList.add(shape);
		}
	}

	public List<Shape> copyShapeList(List<Shape> shapeList) {
		List<Shape> sList = new ArrayList<>();
		for(Shape shape: shapeList) {
			if(shape instanceof Grid) {
				Grid grid = (Grid)shape;
				shape = new Grid(grid,true);
			} else {
				shape = new Shape(shape,true);
			}
			sList.add(shape);
		}
		return sList;
	}
	
	//Consider putting global move function
}
