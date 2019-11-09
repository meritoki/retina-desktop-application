/*
 * Copyright 2019 osvaldo.rodriguez.
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
package com.meritoki.retina.application.desktop.model.provider.zooniverse;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.meritoki.retina.application.desktop.model.document.Shape;

/**
 *
 * @author osvaldo.rodriguez
 */
public class SubjectSet {
    
    public String title;
    public String uuid;
    public String id;
    public String name;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public List<Shape> getShapeList() {
        return shapeList;
    }

    public void setShapeList(List<Shape> shapeList) {
        this.shapeList = shapeList;
    }
}
