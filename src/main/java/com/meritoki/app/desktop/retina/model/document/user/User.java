package com.meritoki.app.desktop.retina.model.document.user;

import java.util.UUID;

import org.codehaus.jackson.annotate.JsonProperty;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.meritoki.app.desktop.retina.model.document.Image;

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
	
	/**
	 * Function returns true if Users have the same uuid
	 * 
	 * @param user
	 * @return flag
	 */
	@JsonIgnore
	public boolean equals(User user) {
		boolean flag = false;
		if (this.uuid.equals(user.uuid)) {
			flag = true;
		}
		return flag;
	}
}
