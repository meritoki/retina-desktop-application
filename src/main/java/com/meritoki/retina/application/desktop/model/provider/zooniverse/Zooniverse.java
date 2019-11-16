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

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.codehaus.jackson.annotate.JsonIgnore;
import com.meritoki.retina.application.desktop.controller.node.NodeController;
import com.meritoki.retina.application.desktop.model.document.Shape;

/**
 *
 * @author osvaldo.rodriguez
 */
public class Zooniverse {

    static org.apache.logging.log4j.Logger logger = LogManager.getLogger(Zooniverse.class.getName());
    public Credential credential = null;
    public Project project;
    public List<Project> projectList = new ArrayList<Project>();
    public int index = 0;

    public Credential getCredential() {
        return credential;
    }

    public List<Project> getProjectList() {
        return projectList;
    }

    @JsonIgnore
    public List<Project> getProjectList(String query) {
        logger.info("searchProject(" + query + ")");
        List<String> stringList = NodeController.executeCommand("panoptes project ls | grep " + query, 60);
        List<Project> projectList = new ArrayList<>();
        if (stringList.size() > 0 && !stringList.get(0).equals("error")) {
            for (String s : stringList) {
                String[] result = s.split(" ");
                String id = result[0].replace("*", "");
                String name = result[1];
                String title = this.convertArrayToStringMethod(result, 2);
                projectList.add(new Project(id, name, title));
            }
        }
        return projectList;
    }

    /**
     * Functions gets Page object at current index from Page List
     *
     * @return Page
     */
    @JsonIgnore
    public Project getProject() {
        return (this.projectList.size() > 0) ? this.projectList.get(index) : new Project();
    }
    
    @JsonIgnore
    public Project getProject(String name) {
        Project project = null;
        for(Project p: this.projectList) {
            if(p.name.equals(name)) {
                project = p;
            }
        }
        return project;
    }

    public void setCredential(Credential credential) {
        this.credential = credential;
    }

    public void setProjectList(List<Project> projectList) {
        this.projectList = projectList;
    }

    @JsonIgnore
    public void setProject(String id) {
        logger.info("setProject(" + id + ")");
        for (int i = 0; i < this.projectList.size(); i++) {
            Project project = this.projectList.get(i);
            if (project.id.equals(id)) {
                this.setIndex(i);;
                break;
            }
        }
    }

    @JsonIgnore
    public void setIndex(int index) {
        logger.debug("setIndex(" + index + ")");
        if (index >= 0 && index < this.projectList.size()) {
            this.index = index;
        }
    }

    @JsonIgnore
    public void addProject(Project project) {
        this.projectList.add(project);
    }

    @JsonIgnore
    public void setConfig() {
        this.setConfig(this.credential);
    }

    @JsonIgnore
    public void setConfig(Credential credential) {
        if (credential != null) {
            Map<Object, Object> data = new HashMap<>();
            data.put("username", credential.userName);
            data.put("password", credential.password);
            data.put("endpoint", "https://www.zooniverse.org");
            NodeController.saveYaml("./", "config.yml", data);
            NodeController.executeCommand("cp ./config.yml ~/.panoptes/");
//            NodeController.executeCommand("panoptes configure");
        }
    }

    @JsonIgnore
    public void createProject(Project project) {
        List<String> stringList = NodeController.executeCommand("panoptes project create " + project.getTitle() + " " + project.getDescription());
        if (stringList.size() > 0 && !stringList.get(0).equals("error")) {
            String string = stringList.get(0);
            String[] stringArray = string.split(" ");
            project.setId(stringArray[0]);
            project.setName(stringArray[1]);
            this.projectList.add(project);
            this.project = project;
        }
    }

    @JsonIgnore
    public void updateProjectWorkflowList(Project project) {
        List<String> stringList = NodeController.executeCommand("panoptes workflow ls -p " + project.getId(), 60);
        if (stringList.size() > 0 && !stringList.get(0).equals("error")) {
            String string = stringList.get(0);
            for (String s : stringList) {
                String[] stringArray = string.split(" ");
                Workflow workflow = new Workflow(stringArray[0], this.convertArrayToStringMethod(stringArray, 1));
                project.getWorkflowList().add(workflow);
            }
        }
    }

    @JsonIgnore
    public void createSubjectSet(String projectId, SubjectSet subjectSet) {
        List<String> stringList = NodeController.executeCommand("panoptes subject-set create " + projectId + " " + subjectSet.getTitle());
        if (stringList.size() > 0 && !stringList.get(0).equals("error")) {
            String string = stringList.get(0);
            for (String s : stringList) {
                String[] stringArray = string.split(" ");
                subjectSet.setId(stringArray[0]);
            }
        }
    }
    
    @JsonIgnore
    public void uploadSubjectSet(SubjectSet subjectSet, String filePath, String fileName) {
        List<String> stringList = NodeController.executeCommand("panoptes subject-set upload-subjects " + subjectSet.getId() + " " + filePath+"/"+fileName);
    }
    
    @JsonIgnore
    public void workflowUploadSubjectSet(Workflow workflow, SubjectSet subjectSet) {
        List<String> stringList = NodeController.executeCommand("panoptes workflow add-subject-sets " + workflow.getId() + " " + subjectSet.getId());
    }
    
    public void downloadClassification(Project project, String fileName) {
    	List<String> stringList = NodeController.executeCommand("panoptes project download --generate "+project.id+" "+fileName, 60*10);
    }
    

    @JsonIgnore
    public String convertArrayToStringMethod(String[] strArray, int start) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = start; i < strArray.length; i++) {
            stringBuilder.append(" ");
            stringBuilder.append(strArray[i]);
        }
        return stringBuilder.toString().trim();
    }
    
    public void generateManifest(String manifestPath, List<Shape> shapeList) {
    	logger.info("generateManifest("+manifestPath+", shapeList)");
    	StringBuilder stringBuilder = new StringBuilder();
    	stringBuilder.append("my_own_id");
    	stringBuilder.append(",");
    	stringBuilder.append("the_image");
    	stringBuilder.append("\n");
    	new File(manifestPath).mkdirs();
    	Shape shape = null;
    	for(int i = 0;i<shapeList.size();i++) {
    		shape = shapeList.get(i);
    		stringBuilder.append(i);
    		stringBuilder.append(",");
    		NodeController.saveJpg(manifestPath, shape.uuid+".jpg", shape.bufferedImage);
    		stringBuilder.append(shape.uuid+".jpg");
    		stringBuilder.append("\n");
    	}
    	NodeController.saveCsv(manifestPath, "manifest.csv", stringBuilder);
    }
}

//@JsonIgnore
//public List<Project> searchProjectList = new ArrayList<Project>();
//public List<Project> getSearchProjectList() {
//  return searchProjectList;
//}
//public void setSearchProjectList(List<Project> searchProjectList) {
//  this.searchProjectList = searchProjectList;
//}
