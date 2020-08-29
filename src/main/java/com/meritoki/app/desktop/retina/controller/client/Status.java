package com.meritoki.app.desktop.retina.controller.client;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Status {
	@JsonProperty
	public String status;
	@JsonProperty
	public String message;
	@JsonProperty
	public Object data;
}