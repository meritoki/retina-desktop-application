package com.meritoki.retina.application.desktop.model;

import java.util.UUID;

import org.codehaus.jackson.annotate.JsonProperty;

public class User {
	@JsonProperty
	public String uuid;
	@JsonProperty
	public String name;
	public String password;
	@JsonProperty
	public String hash;
	public String email;
	public String fullName;
	
	public User(){
        this.uuid = UUID.randomUUID().toString();
    }
}
