package com.meritoki.app.desktop.retina.model.document;

import java.util.ArrayList;
import java.util.List;

public class Archive {

	public List<String> stringList = new ArrayList<>();
	public Matrix matrix;

	public Archive(Matrix matrix) {
		this.matrix = matrix;
		initStringList();
	}

	public void initStringList() {
		if (matrix != null) {
			List<ArrayList<Shape>> rowList = matrix.getArchiveRowList();
			List<Shape> shapeList;
			Shape shape;
			Data data;
			if (rowList.size() > 0) {
				for (int i = 0; i < rowList.size(); i++) {
					shapeList = rowList.get(i);
					for (int j = 0; j < shapeList.size(); j++) {
						shape = shapeList.get(j);
						data = shape.data;
						if(data.text.value != null)
							stringList.add(data.text.value);
					}
				}
			}
		}
	}
}
