package com.meritoki.app.desktop.retina.model.document;

public class Dimension {
	public double x;
	public double y;
	public double w;
	public double h;
	
	public Dimension() {
		
	}
	
	public Dimension(Dimension dimension) {
		this.x = dimension.x;
		this.y = dimension.y;
		this.w = dimension.w;
		this.h = dimension.h;
	}
}
