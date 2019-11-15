package com.meritoki.retina.application.desktop.model.document;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;

public class Point {
	@JsonProperty
	public double x = 0;
	@JsonProperty
	public double y = 0;
	
	public Point() {
		
	}
	
	public Point(Point p) {
		this.x = p.x;
		this.y = p.y;
	}
	
	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
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
        } catch (IOException ex) {
            Logger.getLogger(Shape.class.getName()).log(Level.SEVERE, null, ex);
        }
        return string;
    }
}
