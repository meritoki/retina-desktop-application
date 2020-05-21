package com.meritoki.app.desktop.retina.model.document;

import java.util.ArrayList;
import java.util.List;

public class Row {

	public List<Object> objectList = new ArrayList<>();
	
	public Object[] getObjectArray() {
		Object[] objectArray = new Object[objectList.size()];
        objectArray = objectList.toArray(objectArray);
        return objectArray;
	}
}
