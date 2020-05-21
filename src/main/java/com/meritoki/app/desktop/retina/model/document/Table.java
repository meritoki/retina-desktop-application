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
	public List<Shape> shapeList;
	
	public Table(List<Shape> shapeList) {
		this.shapeList = shapeList;
	}
	
	public Object[] getObjectArray() {
		Object[] objectArray = new Object[2];
		Object[] titleArray = new Object[0];
		Object[][] dataMatrix = new Object[0][0];
		if (shapeList != null) {
			// Variables
			Row row = new Row();
			Data data = null;
			String buffer = null;
			// first build the title, then build the dataArray
			for (Shape shape : shapeList) {
				data = shape.data;
				switch (data.unit.type) {
				case "time": {
					if(buffer != null && !buffer.equals("time")) {
						row.objectList.add(buffer);
					}
					buffer = "time";
					break;
				}
				case "space": {
					if(buffer != null && !buffer.equals("space")) {
						row.objectList.add(buffer);
					}
					buffer = "space";
					break;
				}
				case "energy": {
					String value = null;
					if(data.unit.value != null && data.unit.value.equals("label")) {
						value = data.text.value;
					} else {
						value = data.unit.value;
					}
					if(buffer != null && !buffer.equals(value)) {
						row.objectList.add(buffer);
					}
					buffer = value;
					break;
				}
				}
			}
			titleArray = row.getObjectArray();
			buffer = null;
//			row = new Row();
//			for (Shape shape : shapeList) {
//				data = shape.data;
//				switch (data.unit.type) {
//				case "time": {
//					if(buffer != null && !buffer.equals("time")) {
////						row.objectList.add(buffer);
//						switch(buffer) {
//						case "space": {
//							
//							break;
//						}
//						}
//					}
//					this.time.input(shape);
//					break;
//				}
//				case "space": {
//					if(buffer != null && !buffer.equals("space")) {
////						row.objectList.add(buffer);
//						switch(buffer) {
//						case "time": {
////							row.objectList
//							break;
//						}
//						}
//					}
//					this.space.input(shape);
//					break;
//				}
//				case "energy": {
//					if(buffer != null && !buffer.equals("space")) {
////						row.objectList.add(buffer);
//					}
//					this.energy.input(shape);
//					break;
//				}
//				}
//			}
//			objectArray[0] = new Object[] { "Id", "Name", "Hourly Rate", "Part Time" };
//			objectArray[1] = new Object[][] { { 1, "John", 40.0, false }, { 2, "Rambo", 70.0, false },
//				{ 3, "Zorro", 60.0, true }, };
			objectArray[0] = titleArray;
			objectArray[1] = new Object[][] { { 1, "John", 40.0, false }, { 2, "Rambo", 70.0, false },
				{ 3, "Zorro", 60.0, true }, };
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
