package com.meritoki.app.desktop.retina.model.provider.zooniverse;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class Data {
	private static Logger logger = LogManager.getLogger(Data.class.getName());
	@JsonProperty
	public String retired;
	@JsonProperty("my_own_id")
	public String id;
	@JsonProperty("the_image") 
	public String image;
	
	@JsonIgnore
	public String getUUID() {
		String uuid = null;
		if(image != null) {
			uuid = image.replace(".jpg", "");
		}
		return uuid;
	}
	
	@JsonIgnore
	@Override
	public String toString() {
		String string = "";
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		try {
			string = ow.writeValueAsString(this);
		} catch (IOException ex) {
			logger.error("IOException " + ex.getMessage());
		}
		return string;
	}
}
