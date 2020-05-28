package com.meritoki.app.desktop.retina.model.document;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;


public class Dimension {

	@JsonIgnore
	static Logger logger = LogManager.getLogger(Dimension.class.getName());
	@JsonProperty
	public double width;
	@JsonProperty
	public double height;
	
	public Dimension() {
		
	}
	
	public Dimension(Dimension dimension) {
		this.width = dimension.width;
		this.height = dimension.height;
	}
	
	public Dimension(double width, double height) {
		this.width = width;
		this.height = height;
	}
	
	@JsonIgnore
	@Override
	public String toString() {
		String string = "";
		ObjectWriter ow = new ObjectMapper().writer();// .withDefaultPrettyPrinter();
		try {
			string = ow.writeValueAsString(this);
		} catch (IOException ex) {
			logger.error("IOException " + ex.getMessage());
		}
		return string;
	}
}
