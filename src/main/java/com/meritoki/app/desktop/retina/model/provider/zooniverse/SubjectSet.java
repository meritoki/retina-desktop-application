/*
 * Copyright 2021 Joaquin Osvaldo Rodriguez
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
package com.meritoki.app.desktop.retina.model.provider.zooniverse;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.meritoki.app.desktop.retina.model.document.Shape;

/**
 *
 * @author osvaldo.rodriguez
 */
public class SubjectSet {
    
	@JsonProperty
    public String title;
	@JsonProperty
    public String uuid;
	@JsonProperty
    public String id;
	@JsonProperty
    public String name;
	@JsonProperty
    public String path;
	@JsonIgnore
    public List<Shape> shapeList = new ArrayList<>();

    
    public SubjectSet(){
        UUID uuid = UUID.randomUUID();
        this.uuid = uuid.toString();
    }
    
    public SubjectSet(String name){
        UUID uuid = UUID.randomUUID();
        this.uuid = uuid.toString();
        this.name = name;
    }

    @JsonIgnore
    public String getName() {
        return name;
    }

    @JsonIgnore
    public void setName(String name) {
        this.name = name;
    }
    
    @JsonIgnore
    public String getTitle() {
        return title;
    }

    @JsonIgnore
    public void setTitle(String title) {
        this.title = title;
    }

    @JsonIgnore
    public String getUuid() {
        return uuid;
    }

    @JsonIgnore
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @JsonIgnore
    public String getId() {
        return id;
    }
    
    @JsonIgnore
    public void setId(String id) {
        this.id = id;
    }
    
    @JsonIgnore
    public List<Shape> getShapeList() {
        return shapeList;
    }

    @JsonIgnore
    public void setShapeList(List<Shape> shapeList) {
        this.shapeList = shapeList;
    }
}
