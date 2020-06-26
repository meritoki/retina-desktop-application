package com.meritoki.app.desktop.retina.model.provider.meritoki;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.meritoki.cortex.library.model.Group;

public class Document {

	@JsonProperty
	public Group group = null;
	
	public Document() {
		this.group = new Group(Group.HEXAGONAL);
	}
	
	@JsonIgnore
	public Group getGroup() {
		return this.group;
	}
}
