package com.meritoki.retina.application.desktop.controller.parser;

import com.meritoki.retina.application.desktop.model.document.Data;
import com.meritoki.retina.application.desktop.model.document.Shape;

public class Space {

	public String latitude = null;
	public String longitude = null;
	
	public void input(Shape shape) {
		Data data = shape.data;
		String unit = data.unit.value;
		String text = data.text.value;
		if (unit != null && text != null) {
			switch (unit) {
			case "latitude": {
				this.latitude = text;
			}
			case "longitude": {
				this.longitude = text;
			}
			}
		}
	}
}
