package com.meritoki.app.desktop.retina.model.command;

import org.codehaus.jackson.annotate.JsonProperty;

public class Operation {
	@JsonProperty
    public Object object;
	@JsonProperty
    public int sign;
	@JsonProperty
    public String id;
	@JsonProperty
    public String uuid;
}
