package com.meritoki.retina.application.desktop.model.document;

import java.util.UUID;

import org.codehaus.jackson.annotate.JsonProperty;

public class Network {

	@JsonProperty
	public String uuid = null;
	
	public Network() {
		this.uuid = UUID.randomUUID().toString();
	}
}
