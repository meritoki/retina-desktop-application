package com.meritoki.app.desktop.retina.model.provider.meritoki;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.meritoki.library.cortex.model.Cortex;
import com.meritoki.library.cortex.model.hexagon.Hexagonal;

public class Document {

	@JsonProperty
	public Cortex cortex;
	
	public Document() {
		this.cortex = new Hexagonal(Hexagonal.BRIGHTNESS, 0, 0, 27, 1, 0);
	}
}
