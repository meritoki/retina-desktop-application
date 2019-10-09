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
package com.meritoki.retina.application.desktop.model.project;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;

public class Data {
    static Logger logger = LogManager.getLogger(Data.class.getName());
    public String uuid;
    public Unit unit = new Unit();
    public Text text = new Text();
    public List<Text> textList = new ArrayList<>();
    
    public Data(){
        this.uuid = UUID.randomUUID().toString();
    }
    
    @JsonIgnore
    public void setUUID(String uuid){
        this.uuid = uuid;
    }
    
    @JsonProperty
    public void addText(Text text){
        logger.debug("addText("+text.value+")");
        this.textList.add(text);
    }
    
    /**
     * Function creates a Map from the textList that shows the frequency that a text value
     * has been input by a user
     * @return
     */
    @JsonIgnore
    public Map<String, Integer> getTextMap() {
    	int count = 0;
    	Map<String,Integer> textMap = new HashMap<>();
    	for(Text text: this.textList) {
          count = (textMap.get(text.value)!=null)?textMap.get(text.value):0;
          ++count;
          textMap.put(text.value, count);
    	}
    	return textMap;
    }
    
    /**
     * Function returns the text value with the highest frequency of input
     * @return
     */
    @JsonIgnore
    public Text getDefaultText(){
        int max = 0;
        Text text = new Text();
        int value = 0;
        for(Map.Entry<String, Integer> entry : this.getTextMap().entrySet()){
        	value = entry.getValue().intValue();
            if(value > max){
            	max = value;
                text = new Text();
                text.value = entry.getKey();
            }
        }
        return text;
    }
    
    @JsonProperty
    public List<Text> getTextList(){
        List<Text> textList = new ArrayList<>();
        Text text = null;
        for(Map.Entry<String, Integer> entry : this.getTextMap().entrySet()){
            text = new Text();
            text.value = entry.getKey();
            textList.add(text);
        }
        return textList;
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
