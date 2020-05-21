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
			String value = null;
			dataMatrix = new Object[rowList.size()][matrix.getShapeListMax()];
			for(int i = 0; i < rowList.size(); i++) {
				shapeList = rowList.get(i);
				if(i == 0) {
					columnArray = new Object[shapeList.size()];
					for(int j = 0; j < shapeList.size(); j++) {
						shape = shapeList.get(j);
						data = shape.data;
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
							if(data.unit.value != null && data.unit.value.equals("label")) {
								value = data.text.value;
							} else {
								value = data.unit.value;
							}
							break;
						}
						}
						columnArray[j] = value;
					}
				} else {
					for(int j = 0; j < shapeList.size(); j++) {
						shape = shapeList.get(j);
						data = shape.data;
						value = data.text.value;
						dataMatrix[i][j] = value;
					}
				}
			}
			objectArray[0] = columnArray;
			objectArray[1] = dataMatrix;
		}
		return objectArray;
	}

	public Object[] parseColumn(List<Shape> shapeList) {
		logger.info("parseColumn(shapeList)");
		List<Object> objectList = new ArrayList<>();
		if (shapeList != null) {

			Data data = null;
			String type;
			for (Shape shape : shapeList) {
				data = shape.data;
				type = data.unit.type.toLowerCase();
				if (this.previousShape != null) {
					if (!previousShape.data.unit.type.equals(type)) {

					}
				}
				switch (type) {
				case "time": {
					this.time.input(shape);
				}
				case "space": {
					this.space.input(shape);
					if (!previousShape.data.unit.type.equals(type)) {

					}
				}
				case "energy": {
					if (data.unit.value.equals("label")) {

					}

					this.energy.input(shape);
				}
				}
				this.previousShape = shape;
			}
		}

		Object[] columns = new Object[] { "Id", "Name", "Hourly Rate", "Part Time" };

		return columns;
	}

	public Object[][] parseData(List<Shape> shapeList) {
		logger.info("parseData(shapeList)");
		List<Object> objectList = new ArrayList<>();
		if (shapeList != null) {
			Data data = null;
			String type;
			for (Shape shape : shapeList) {
				data = shape.data;
				type = data.unit.type.toLowerCase();
				switch (type) {
				case "time": {
					this.time.input(shape);
					if (!previousShape.data.unit.type.equals(type)) {

					}
				}
				case "space": {
					this.space.input(shape);
					if (!previousShape.data.unit.type.equals(type)) {

					}
				}
				case "energy": {
					this.energy.input(shape);
				}
				}
				this.previousShape = shape;
			}
		}
		Object[][] array = new Object[][] { { 1, "John", 40.0, false }, { 2, "Rambo", 70.0, false },
				{ 3, "Zorro", 60.0, true }, };
		return array;
	}
}

//objectArray[0] = new Object[] { "Id", "Name", "Hourly Rate", "Part Time" };
//objectArray[1] = new Object[][] { { 1, "John", 40.0, false }, { 2, "Rambo", 70.0, false },
//	{ 3, "Zorro", 60.0, true }, };
