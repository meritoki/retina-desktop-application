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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 *
 * @author osvaldo.rodriguez
 */
public class Project {
    
    static Logger logger = LogManager.getLogger(Project.class.getName());
    public Credential credential;
    public String title;
    public String description;
    public String uuid;
    public String id;
    public String name;
    public List<Workflow> workflowList = new ArrayList<>();

    public Credential getCredential() {
        return credential;
    }

    public void setCredential(Credential credential) {
        this.credential = credential;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Project() {
        UUID uuid = UUID.randomUUID();
        this.uuid = uuid.toString();
    }
    
    public Project(String title, String description){
        logger.info("Project("+title+", "+description+")");
        UUID uuid = UUID.randomUUID();
        this.uuid = uuid.toString();
        this.title = title;
        this.description = description;
    }
    
    public Project(String id, String name, String title) {
        logger.info("Project("+id+","+name+","+title+")");
        UUID uuid = UUID.randomUUID();
        this.uuid = uuid.toString();
        this.id = id;
        this.name = name;
        this.title = title;
    }
    
    public void setResponse(String response) {
        String[] array = response.split(" ");
        this.setId(array[0].replace("*", ""));
        this.setName(array[1]);
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

    public List<Workflow> getWorkflowList() {
        return workflowList;
    }

    public void setWorkflowList(List<Workflow> workflowList) {
        this.workflowList = workflowList;
    }
}
