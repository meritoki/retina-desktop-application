package com.meritoki.app.desktop.retina.model.document;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.DefaultTableModel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Table {
	private static Logger logger = LogManager.getLogger(Table.class.getName());
	public Matrix matrix;

	public Table(Matrix matrix) {
		this.matrix = matrix;
	}
	
	public DefaultTableModel getDefaultTableMode() {
		Object[] objectArray = this.getObjectArray();
		return new javax.swing.table.DefaultTableModel((Object[][])objectArray[1], (Object[])objectArray[0]);
	}
	
	public Object[][] getExcelObjectArray() {
		Object[][] dataMatrix = null;
		if (matrix != null) {
			List<ArrayList<Shape>> rowList = matrix.getTableRowList();
			List<Shape> shapeList;
			Shape shape;
			Data data;
			String value;
			if (rowList.size() > 0) {
				dataMatrix = new Object[rowList.size()][matrix.getShapeListMax()];
				for (int i = 0; i < rowList.size(); i++) {
					shapeList = rowList.get(i);
					for (int j = 0; j < shapeList.size(); j++) {
						shape = shapeList.get(j);
						data = shape.data;
						value = data.text.value;
						dataMatrix[i][j] = value;
					}
				}
			}
		}
		return dataMatrix;
	}

	public Object[] getObjectArray() {
		Object[] objectArray = new Object[2];
		Object[] columnArray = new Object[0];
		Object[][] dataMatrix = null;
		if (matrix != null) {
			List<ArrayList<Shape>> rowList = matrix.getTableRowList();
			List<Shape> shapeList;
			Shape shape;
			Data data;
			String value;
			if (rowList.size() > 0) {
				shapeList = rowList.get(0);
				columnArray = new Object[shapeList.size()];
				for (int j = 0; j < shapeList.size(); j++) {
					shape = shapeList.get(j);
					data = shape.data;
					value = null;
					switch (data.unit.type) {
					case TIME: {
						value = "time";
						break;
					}
					case SPACE: {
						value = "space";
						break;
					}
					case ENERGY: {
						if (data.unit.value != null && data.unit.value.equals("label")) {
							value = data.text.value;
						} else {
							value = data.unit.value;
						}
						break;
					}
					}
					columnArray[j] = value;
				}
				dataMatrix = new Object[rowList.size()-1][matrix.getShapeListMax()];
				for (int i = 1; i < rowList.size(); i++) {
					shapeList = rowList.get(i);
					for (int j = 0; j < shapeList.size(); j++) {
						shape = shapeList.get(j);
						data = shape.data;
						value = data.text.value;
						dataMatrix[i-1][j] = value;
					}
				}
			}
			objectArray[0] = columnArray;
			objectArray[1] = dataMatrix;
		}
		return objectArray;
	}
}
