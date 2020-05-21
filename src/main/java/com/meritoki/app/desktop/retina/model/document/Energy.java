package com.meritoki.app.desktop.retina.model.document;

public class Energy {
	
	public String label;

	public void input(Shape shape) {
		Data data = shape.data;
		String unit = data.unit.value;
		String text = data.text.value;
		if (unit != null && text != null) {
			switch (unit) {
			case "label": {
				this.label = text;
			}
			}
		}
	}
}
