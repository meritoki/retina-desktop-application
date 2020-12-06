package com.meritoki.app.desktop.retina.model.document;

import java.util.ArrayList;
import java.util.List;

public class Selector extends Shape {
	
	public List<Shape> shapeList = new ArrayList<>();
	
	public void addShape(Shape shape) {
		if(this.position.containsPosition(shape.position)) {
			shape.selected = true;
			shapeList.add(shape);
		}
	}
}
