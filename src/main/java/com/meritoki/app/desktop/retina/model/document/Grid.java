package com.meritoki.app.desktop.retina.model.document;

import java.util.ArrayList;
import java.util.List;

public class Grid extends Shape {
	
	public Dimension dimension;
	public int row = 8;
	public int column = 8;
	public Shape[][] matrix = new Shape[row][column];
	
	public Grid() {
		super();
		initMatrix();
	}
	
	public Grid(Shape shape) {
		super(shape,true);
		initMatrix();
	}
	
	public List<Shape> getShapeList() {
		List<Shape> shapeList = new ArrayList<Shape>();
		for(int i=0;i<matrix.length;i++) {
			for(int j=0;j<matrix[i].length;j++) {
				shapeList.add(matrix[i][j]);
			}
		}
		return shapeList;
	}
	
	public void initMatrix() {
		matrix = new Shape[row][column];
		for(int i=0;i<matrix.length;i++) {
			for(int j=0;j<matrix[i].length;j++) {
				matrix[i][j] = new Shape();
			}
		}
	}
	
	public void updateMatrix() {
		Point point = this.position.point;
		Dimension dimension = this.position.dimension;
		double width = dimension.width/column;
		double height = dimension.height/row;
		for(int i=0;i<matrix.length;i++) {
			for(int j=0;j<matrix[i].length;j++) {
				
				matrix[i][j].position.point.x = point.x+(j*width);
				matrix[i][j].position.point.y = point.y+(i*height);
				matrix[i][j].position.dimension.width = width;
				matrix[i][j].position.dimension.height = height;
			}
		}
	}
	
	public void setRow(int row) {
		this.row = row;
	}
	
	public void setColumn(int column) {
		this.column = column;
	}
	
	public void setHeight(double height) {
		this.dimension.height = height;
	}
	
	public void setWidth(double width) {
		this.dimension.width = width;
	}
}
