package com.meritoki.app.desktop.retina.model.provider.meritoki;

import java.awt.image.BufferedImage;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.meritoki.app.desktop.retina.model.document.Shape;

public class Input {
	@JsonProperty
	public String uuid;
	@JsonProperty
	public Shape shape;
	@JsonProperty
	public String concept;
	@JsonProperty
	public boolean flag;
	
	public Input() {
		this.uuid = UUID.randomUUID().toString();
	}
}
