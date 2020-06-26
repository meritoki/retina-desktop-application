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
package com.meritoki.app.desktop.retina.model.provider.zooniverse;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *
 * @author osvaldo.rodriguez
 */
public class Project {
    
    static Logger logger = LogManager.getLogger(Project.class.getName());
    public String id;
    public String title;
    public String description;
    public String name;
    public List<Workflow> workflowList = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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
    
    public void setResponse(String response) {
        String[] array = response.split(" ");
        this.setId(array[0].replace("*", ""));
        this.setName(array[1]);
    }
    
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
    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Workflow> getWorkflowList() {
        return workflowList;
    }

    public void setWorkflowList(List<Workflow> workflowList) {
        this.workflowList = workflowList;
    }
}
