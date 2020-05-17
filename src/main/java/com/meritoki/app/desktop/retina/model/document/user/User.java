package com.meritoki.app.desktop.retina.model.document.user;

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
	
	public User(String name, String password){
        this.uuid = UUID.randomUUID().toString();
        this.name = name;
        this.password = password;
    }
}
