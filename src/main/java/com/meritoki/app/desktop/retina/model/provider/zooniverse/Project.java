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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author osvaldo.rodriguez
 */
public class Project {
    
    static Logger logger = LogManager.getLogger(Project.class.getName());
    @JsonProperty
    public String id;
    @JsonProperty
    public String title;
    @JsonProperty
    public String description;
    @JsonProperty
    public String name;
    @JsonIgnore
    public List<Workflow> workflowList = new ArrayList<>();

    public Project() {

    }
    
    public Project(String title, String description){
        logger.info("Project("+title+", "+description+")");
        this.title = title;
        this.description = description;
    }
    
    public Project(String id, String name, String title) {
        logger.info("Project("+id+","+name+","+title+")");
        this.id = id;
        this.name = name;
        this.title = title;
    }
    
    @Override
    public boolean equals(Object object) {
    	if(object instanceof Project) {
    		Project p = (Project)object;
    		return (this.id.equals(p.id)); 
    	}
    	return false;
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
    public void setResponse(String response) {
        String[] array = response.split(" ");
        this.setId(array[0].replace("*", ""));
        this.setName(array[1]);
    }
    
    @JsonIgnore
    public Workflow getWorkflow(String title) {
        Workflow workflow = null;
        for(Workflow w: this.workflowList) {
            if(w.title.equals(title)) {
                workflow = w;
            }
        }
        return workflow;
    }
    
    @JsonIgnore
    public void addWorkflow(Workflow workflow) {
        this.workflowList.add(workflow);
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
    public String getDescription() {
        return description;
    }

    @JsonIgnore
    public void setDescription(String description) {
        this.description = description;
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
    public List<Workflow> getWorkflowList() {
        return workflowList;
    }

    @JsonIgnore
    public void setWorkflowList(List<Workflow> workflowList) {
        this.workflowList = workflowList;
    }
}
