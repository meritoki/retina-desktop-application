package com.meritoki.retina.application.desktop.model.project;

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
	
    @JsonIgnore
    @Override
    public String toString(){
        String string = "";
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        try {
            string = ow.writeValueAsString(this);
        } catch (IOException ex) {
            Logger.getLogger(Shape.class.getName()).log(Level.SEVERE, null, ex);
        }
        return string;
    }
}
