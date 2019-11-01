package com.meritoki.retina.application.desktop.model.document;

import java.util.UUID;

import org.codehaus.jackson.annotate.JsonProperty;

public class User {
	@JsonProperty
	public String uuid;
	@JsonProperty
	public String name;
	public String password;
	@JsonProperty
	public String encryptedPassword;
	
	public User(){
        this.uuid = UUID.randomUUID().toString();
    }
}
