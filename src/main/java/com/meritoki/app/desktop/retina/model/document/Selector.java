package com.meritoki.app.desktop.retina.model.document;

import java.util.ArrayList;
import java.util.List;

public class Selector extends Shape {
	
	public List<Shape> shapeList = new ArrayList<>();
	
	public Selector() {}
	
	public Selector(Selector selector,boolean flag) {
		super(selector,flag);
		shapeList = selector.shapeList;
	}
	
	public void addShape(Shape shape) {
		if(this.position.containsPosition(shape.position)) {
			shapeList.add(shape);
		}
	}
}
