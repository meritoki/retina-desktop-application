package com.meritoki.app.desktop.retina.model.provider.meritoki;

import java.awt.image.BufferedImage;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Input {
	@JsonProperty
	public String uuid;
	@JsonProperty
	public String shapeUUID;
	@JsonProperty
	public String concept;
	@JsonIgnore
	public BufferedImage bufferedImage;
}
