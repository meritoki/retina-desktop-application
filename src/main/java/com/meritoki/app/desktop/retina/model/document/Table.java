package com.meritoki.app.desktop.retina.model.document;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Table {
	private static Logger logger = LogManager.getLogger(Table.class.getName());

	public Time time = new Time();
	public Space space = new Space();
	public Energy energy = new Energy();
	public boolean timeFlag = false;
	public boolean spaceFlag = false;
	public boolean energyFlag = false;
	public Shape previousShape = null;
	public Matrix matrix;

	public Table(Matrix matrix) {
		this.matrix = matrix;
	}

	public Object[] getObjectArray() {
		Object[] objectArray = new Object[2];
		Object[] columnArray = new Object[0];
		Object[][] dataMatrix = null;
		if (matrix != null) {
			List<ArrayList<Shape>> rowList = matrix.getRowList();
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
					case "time": {
						value = "time";
						break;
					}
					case "space": {
						value = "space";
						break;
					}
					case "energy": {
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

//objectArray[0] = new Object[] { "Id", "Name", "Hourly Rate", "Part Time" };
//objectArray[1] = new Object[][] { { 1, "John", 40.0, false }, { 2, "Rambo", 70.0, false },
//	{ 3, "Zorro", 60.0, true }, };
