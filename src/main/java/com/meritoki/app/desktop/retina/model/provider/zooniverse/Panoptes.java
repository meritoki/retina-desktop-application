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

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.meritoki.app.desktop.retina.controller.node.NodeController;
import com.meritoki.library.controller.node.Exit;

public class Panoptes {
	
	static org.apache.logging.log4j.Logger logger = LogManager.getLogger(Panoptes.class.getName());
	
	@JsonIgnore
	public boolean isAvailable()  {
		logger.info("isAvailable()");
		String command = null;
		if (NodeController.isLinux() || NodeController.isMac()) {
			command = "panoptes";
		} else if (NodeController.isWindows()) {
			command = "set PYTHONIOENCODING=utf-8 && panoptes";
		}
		boolean flag = true;
		Exit exit;
		try {
			exit = NodeController.executeCommand(command, 1440);
			if (exit.value != 0) {
				flag = false;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return flag;
	}
	
	public static List<Project> listProject(String query) throws Exception {
		logger.info("listProject(" + query + ")");
		List<Project> projectList = new ArrayList<>();
		String command = null;
		if (NodeController.isLinux() || NodeController.isMac()) {
			command = "panoptes project ls";
		} else if (NodeController.isWindows()) {
			command = "set PYTHONIOENCODING=utf-8 && panoptes project ls";
		}
		Exit exit = NodeController.executeCommand(command, 1440);
		if (exit.value != 0) {
			throw new Exception("Non-Zero Exit Value: " + exit.value);
		} else {
			List<String> stringList = exit.list;
			if (stringList.size() > 0 && !stringList.get(0).equals("error")) {
				for (String s : stringList) {
					if(s.contains(query)) {
						String[] result = s.split(" ");
						String id = result[0].replace("*", "");
						String name = result[1];
						String title = convertArrayToStringMethod(result, 2);
						projectList.add(new Project(id, name, title));
					}
				}
			}
		}
		return projectList;
	}
	
	@JsonIgnore
	public static List<Workflow> listWorkflow(Project project) throws Exception {
		String panoptesCommand = "panoptes workflow ls -p " + project.getId();
		Exit exit = NodeController.executeCommand(panoptesCommand, 60);
		List<Workflow> workflowList = new ArrayList<>();
		if (exit.value != 0) {
			throw new Exception("Failed Command: " + panoptesCommand);
		} else {
			if (exit.list.size() > 0) {
				for (String s : exit.list) {
					s = s.trim();
					String[] stringArray = s.split(" ");
					Workflow workflow = new Workflow(stringArray[0], convertArrayToStringMethod(stringArray, 1));
					workflowList.add(workflow);
				}
			}
		}
		return workflowList;
	}
	
	@JsonIgnore
	public static void setConfig(Credential credential) throws Exception {
		if (credential != null) {
			Map<Object, Object> data = new HashMap<>();
			data.put("username", credential.userName);
			data.put("password", credential.password);
			data.put("endpoint", "https://www.zooniverse.org");
			File configFile = new File(ZooniverseController.getConfigPath());
			if(!configFile.exists()) {
				configFile.mkdirs();
			}
			NodeController.saveYaml(ZooniverseController.getConfigPath(), "config.yml", data);
			File panoptesHome = new File(NodeController.getPanoptesHome());
			if(!panoptesHome.exists()) {
				panoptesHome.mkdir();
			}
			String cpCommand = "";
			if(NodeController.isWindows()) {
				cpCommand += "copy ";
			} else {
				cpCommand = "cp ";
			}
			cpCommand += ZooniverseController.getConfigPath() + NodeController.getSeperator() + "config.yml "
					+ NodeController.getPanoptesHome() + NodeController.getSeperator();
			Exit exit = NodeController.executeCommand(cpCommand);
			if (exit.value != 0) {
				logger.error("setConfig("+credential+") exit.output="+exit.getOutput());
				throw new Exception("Failed Command: " + cpCommand);
			}
		}
	}
	
	@JsonIgnore
	public static Project createProject(Project project) throws Exception {
		String panoptesCommand = "panoptes project create " + project.getTitle() + " "
				+ project.getDescription();
		Exit exit = NodeController.executeCommand(panoptesCommand);
		if (exit.value != 0) {
			throw new Exception("Failed Command: " + panoptesCommand);
		} else {
			List<String> stringList = exit.list;
			if (stringList.size() > 0 && !stringList.get(0).equals("error")) {
				String string = stringList.get(0);
				String[] stringArray = string.split(" ");
				project.setId(stringArray[0]);
				project.setName(stringArray[1]);
//				this.projectList.add(project);
//				this.project = project;
			}
		}
		return project;
	}
	
	@JsonIgnore
	public static void uploadSubjectSet(SubjectSet subjectSet, String filePath, String fileName) throws Exception {
		String panoptesCommand = "panoptes subject-set upload-subjects " + subjectSet.getId() + " " + filePath + "/"
				+ fileName;
		Exit exit = NodeController.executeCommand(panoptesCommand, 2880);
		if (exit.value != 0) {
			throw new Exception("Failed Command: " + panoptesCommand);
		}
	}

	@JsonIgnore
	public static void workflowUploadSubjectSet(Workflow workflow, SubjectSet subjectSet) throws Exception {
		String panoptesCommand = "panoptes workflow add-subject-sets " + workflow.getId() + " " + subjectSet.getId();
		Exit exit = NodeController.executeCommand(panoptesCommand);
		if (exit.value != 0) {
			throw new Exception("Failed Command: " + panoptesCommand);
		}
	}

	public static void downloadClassification(Project project, String fileName) throws Exception {
		String panoptesCommand = "panoptes project download --generate " + project.id + " " + fileName;
		Exit exit = NodeController.executeCommand(panoptesCommand, 1440);
		if (exit.value != 0) {
			throw new Exception("Failed Command: " + panoptesCommand);
		}
	}
	
	@JsonIgnore
	public static void createSubjectSet(String projectId, SubjectSet subjectSet) throws Exception {
		logger.info("createSubjectSet("+projectId+", "+subjectSet+")");
		String panoptesCommand = "panoptes subject-set create " + projectId + " " + subjectSet.getTitle();
		Exit exit = NodeController.executeCommand(panoptesCommand);
		if (exit.value != 0) {
			throw new Exception("Non-Zero Exit Value: " + exit.value);
		} else {
			List<String> stringList = exit.list;
			if (stringList.size() > 0 && !stringList.get(0).equals("error")) {
				String string = stringList.get(0);
				for (String s : stringList) {
					String[] stringArray = string.split(" ");
					subjectSet.setId(stringArray[0]);
				}
			} else {
				throw new Exception("Name already taken");
			}
		}
	}

	
	@JsonIgnore
	public static String convertArrayToStringMethod(String[] strArray, int start) {
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = start; i < strArray.length; i++) {
			stringBuilder.append(" ");
			stringBuilder.append(strArray[i]);
		}
		return stringBuilder.toString().trim();
	}
}
