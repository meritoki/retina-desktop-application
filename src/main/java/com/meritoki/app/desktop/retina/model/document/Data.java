/*
 * Copyright 2019 Joaquin Osvaldo Rodriguez
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.meritoki.app.desktop.retina.model.document;

import java.io.IOException;
import java.util.UUID;
import java.util.logging.Level;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;

public class Data {
    private static Logger logger = LogManager.getLogger(Data.class.getName());
    @JsonProperty
    public String uuid;
    @JsonProperty
    public Unit unit = new Unit();
    @JsonProperty
    public Text text = new Text();
    
    public Data(){
        this.uuid = UUID.randomUUID().toString();
    }
    
    public Data(Data data) {
    	this.uuid = data.uuid;
    	this.unit = new Unit(data.unit);
    	this.text = new Text(data.text);
    }
    
    @JsonIgnore
    public void setUUID(String uuid){
        this.uuid = uuid;
    }
    
    @JsonIgnore
    public void setText(Text text) {
    	logger.info("setText("+text+")");
    	this.text = text;
    }
    
    @JsonProperty
    public Text getText(){
        return this.text;
    }
    
    @JsonIgnore
    @Override
    public String toString(){
        String string = "";
        if(logger.isTraceEnabled()){
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            try {
                string = ow.writeValueAsString(this);
            } catch (IOException ex) {
                java.util.logging.Logger.getLogger(Shape.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            string = this.uuid;
        }
        return string;
    }
}
