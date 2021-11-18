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

import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.type.TypeReference;
import com.meritoki.app.desktop.retina.controller.node.NodeController;
import com.meritoki.app.desktop.retina.model.document.Document;
import com.meritoki.app.desktop.retina.model.document.Page;
import com.meritoki.app.desktop.retina.model.document.Shape;
import com.meritoki.app.desktop.retina.model.provider.Provider;
import com.meritoki.library.controller.memory.MemoryController;

/**
 *
 * @author osvaldo.rodriguez
 */
public class Zooniverse extends Provider {

	static org.apache.logging.log4j.Logger logger = LogManager.getLogger(Zooniverse.class.getName());
	public Credential credential = null;
	public Project project;
	public List<Project> projectList = new ArrayList<Project>();
	public int index = 0;
	public boolean test = false;
	public StringBuilder report = new StringBuilder();

	public Zooniverse() {
		super("zooniverse");
		logger.info("Zooniverse()");
	}
	
	@Override
	@JsonIgnore
	public void init() {
		logger.info("init()");
		List<Project> cacheProjectList = this.getCacheProjectList(ZooniverseController.getProjectJSON());
		if(cacheProjectList != null && cacheProjectList.size() > 0) {
			for(Project p:cacheProjectList) {
				if(!this.projectList.contains(p)) {
					this.projectList.add(p);
				}
			}
		}
	}
	
	public void findProject(String query) throws Exception {
		this.setProjectList(Panoptes.listProject(query));
	}
	
	public void updateProjectWorkflowList(Project project) throws Exception {
		List<Workflow> workflowList = Panoptes.listWorkflow(project);
		if(workflowList.size() > 0) {
			project.setWorkflowList(new ArrayList<>());
			for(Workflow workflow: workflowList) {
//				if(!project.getWorkflowList().contains(workflow)) {
				project.getWorkflowList().add(workflow);
//				}
			}
			List<Project> cacheProjectList = getCacheProjectList(ZooniverseController.getProjectJSON());
			if(cacheProjectList != null && cacheProjectList.size() > 0) {
				if(!cacheProjectList.contains(project)) {
					cacheProjectList.add(project);
					NodeController.saveJson(ZooniverseController.getProjectJSON(), cacheProjectList);
				}
			} else {
				cacheProjectList = new ArrayList<>();
				cacheProjectList.add(project);
				NodeController.saveJson(ZooniverseController.getProjectJSON(), cacheProjectList);
			}
			if(project.getWorkflowList().size() == 0) {
				throw new Exception("Unable to Access any Workflows");
			}
		}
	}
	
	public List<Project> getCacheProjectList(File file) {
		List<Project> cacheProjectList = null;
		if(file.exists()) {
			cacheProjectList = (List<Project>)NodeController.openJson(file,new TypeReference<List<Project>>(){});
		}
		return cacheProjectList;
	}

	public Credential getCredential() {
		return credential;
	}

	public List<Project> getProjectList() {
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
		for (Project p : this.projectList) {
			if (p.name.equals(name)) {
				project = p;
			}
		}
		return project;
	}

	public void setCredential(Credential credential) {
		this.credential = credential;
	}

	@JsonIgnore
	public void setProjectList(List<Project> projectList) {
		this.projectList = projectList;
	}

	@JsonIgnore
	public void setProject(String id) {
		logger.info("setProject(" + id + ")");
		for (int i = 0; i < this.projectList.size(); i++) {
			Project project = this.projectList.get(i);
			if (project.id.equals(id)) {
				this.setIndex(i);
				;
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
	public void setConfig() throws Exception {
		Panoptes.setConfig(this.credential);
	}

	public void generateManifest(String manifestPath, Document document) throws Exception { //List<Shape> shapeList) throws Exception {
		logger.info("generateManifest(" + manifestPath + ", "+(document != null)+")");
		MemoryController.log();
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("my_own_id");
		stringBuilder.append(",");
		stringBuilder.append("the_image");
		stringBuilder.append("\n");
		new File(manifestPath).mkdirs();
		int index = 0;
		this.report = new StringBuilder();
		for(Page p:document.getPageList()) {
			for(Shape s:p.getGridShapeList()) {
				stringBuilder.append(index);
				stringBuilder.append(",");
				BufferedImage image = this.model.document.getShapeBufferedImage(p.getScaledBufferedImage(this.model), s);
				if(image != null) {
					NodeController.saveJpg(manifestPath, s.uuid + ".jpg", this.model.document.getShapeBufferedImage(p.getScaledBufferedImage(this.model), s));
				} else {
					this.report.append("Page "+p.uuid+" Null Shape "+s.uuid+"\n");
				}
				stringBuilder.append(s.uuid + ".jpg");
				stringBuilder.append("\n");
				index++;
			}
		}
		MemoryController.log();
		NodeController.saveText(manifestPath, "manifest.csv", stringBuilder);
	}

	public SubjectSet getSubjectSet(String title, Document document) throws Exception { //List<Shape> shapeList) throws Exception {
		logger.info("getSubjectSet("+title+","+(document != null)+")");
		String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		SubjectSet subjectSet = new SubjectSet();
		subjectSet.title = title;
		subjectSet.path = ZooniverseController.getSubjectSetPath() + timeStamp;
		this.generateManifest(subjectSet.path, document);
		return subjectSet;
	}

	public void upload(Project project, Workflow workflow, SubjectSet subjectSet) throws Exception {
		logger.info("upload("+project+","+workflow+","+subjectSet+")");
		if(!test) {
			if (project != null && workflow != null && subjectSet != null) {
				Panoptes.createSubjectSet(project.getId(), subjectSet);
				Panoptes.uploadSubjectSet(subjectSet, subjectSet.path, "manifest.csv");
				Panoptes.workflowUploadSubjectSet(workflow, subjectSet);
			} else {
				
				throw new Exception("project, workflow, and/or subject set is null");
			}
		} else {
			throw new Exception("test enabled");
		}
	}
}
