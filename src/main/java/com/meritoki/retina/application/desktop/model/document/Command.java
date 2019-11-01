package com.meritoki.retina.application.desktop.model.document;

import java.util.LinkedList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class Command {
	@JsonProperty
	public String name;
	@JsonProperty
	public User user;
	@JsonProperty
    public LinkedList<Operation> operationList = new LinkedList<>();
}
