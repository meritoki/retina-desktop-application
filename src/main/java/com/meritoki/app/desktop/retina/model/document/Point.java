package com.meritoki.app.desktop.retina.model.document;

import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class Point {
	@JsonIgnore
    static Logger logger = LogManager.getLogger(Point.class.getName());
	@JsonProperty
	public double x = 0;
	@JsonProperty
	public double y = 0;
	
	public Point() {
	}
	
	/** 
	 * Copy constructor
	 * @param p
	 */
	public Point(Point p) {
		this.x = p.x;
		this.y = p.y;
	}
	
	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	@JsonIgnore
	public boolean equals(Point point) {
		boolean flag = false;
		if(this.x == point.x && this.y == point.y) {
			flag = true;
		}
		return flag;
	}
	
    @JsonIgnore
    @Override
    public String toString(){
        String string = "";
        ObjectWriter ow = new ObjectMapper().writer();//.withDefaultPrettyPrinter();
        try {
            string = ow.writeValueAsString(this);
        } catch (IOException e) {
            logger.error("IOException "+e.getMessage());
        }
        return string;
    }
}
