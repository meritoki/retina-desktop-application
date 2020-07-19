package com.meritoki.app.desktop.retina.model.provider.zooniverse;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Annotation {
	@JsonProperty
	public String task;
	@JsonProperty
	public String value;
	@JsonProperty("task_label")
	public String taskLabel;
}
