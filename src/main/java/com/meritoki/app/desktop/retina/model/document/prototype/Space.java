package com.meritoki.app.desktop.retina.model.document.prototype;

import com.meritoki.app.desktop.retina.model.document.Data;
import com.meritoki.app.desktop.retina.model.document.Shape;

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
