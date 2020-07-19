package com.meritoki.app.desktop.retina.model.provider.zooniverse;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import com.fasterxml.jackson.annotation.JsonAnySetter;

public class Subject {

	    private String id;
	    public TreeMap<String, Data> dataMap = new TreeMap<>();

	    // Getters and setters (except for unknownFields)

	    @JsonAnySetter
	    public void setUnknownField(String name, Data value) {
	        dataMap.put(name, value);
	    }
}
