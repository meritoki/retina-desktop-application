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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import com.meritoki.retina.application.desktop.controller.system.Shell;

/**
 *
 * @author osvaldo.rodriguez
 */
public class Zooniverse {

    static org.apache.logging.log4j.Logger logger = LogManager.getLogger(Zooniverse.class.getName());
    public Credential credential = null;
    public Project project;
    public List<Project> projectList = new ArrayList<Project>();
    @JsonIgnore
    public List<Project> searchProjectList = new ArrayList<Project>();

    public List<Project> getSearchProjectList() {
        return searchProjectList;
    }

    public void setSearchProjectList(List<Project> searchProjectList) {
        this.searchProjectList = searchProjectList;
    }

    public Credential getCredential() {
        return credential;
    }

    public void setCredential(Credential credential) {
        this.credential = credential;
    }

    public List<Project> getProjectList() {
        return projectList;
    }

    public void setProjectList(List<Project> projectList) {
        this.projectList = projectList;
    }

    @JsonIgnore
    public void createConfig() {
        this.createConfig(this.credential);
    }

    @JsonIgnore
    public void createConfig(Credential credential) {
        if (credential != null) {
            Map<Object, Object> data = new HashMap<>();
            data.put("username", credential.userName);
            data.put("password", credential.password);
            data.put("endpoint", "https://www.zooniverse.org");

            DumperOptions options = new DumperOptions();
            options.setPrettyFlow(true);
            // Fix below - additional configuration
            options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
            Yaml yaml = new Yaml(options);
            FileWriter writer;
            try {
                writer = new FileWriter("./config.yml");
                yaml.dump(data, writer);
            } catch (IOException ex) {
                logger.error(ex);
            }
            Shell.executeCommand("cp ./config.yml ~/.panoptes/");
            Shell.executeCommand("panoptes configure");
        }
    }

    @JsonIgnore
    public void setupConfig() {

    }

    @JsonIgnore
    public void createProject(Project project) {
        List<String> stringList = Shell.executeCommand("panoptes project create " + project.getTitle() + " " + project.getDescription());
        if(stringList.size() > 0 && !stringList.get(0).equals("error")){
            String string = stringList.get(0);
            String[] stringArray = string.split(" ");
            project.setId(stringArray[0]);
            project.setName(stringArray[1]);
            this.projectList.add(project);
            this.project = project;
        }
    }

    @JsonIgnore
    public void searchProject(String query) {
        logger.info("searchProject(" + query + ")");
        List<String> stringList = Shell.executeCommand("panoptes project ls | grep " + query);
        Project project;
        List<Project> projectList = new ArrayList<>();
        if(stringList.size() > 0 && !stringList.get(0).equals("error")){
            for (String s : stringList) {
                String[] result = s.split(" ");
                String id = result[0].replace("*", "");
                String name = result[1];
                String title = this.convertArrayToStringMethod(result, 2);
                project = new Project(id, name, title);
                projectList.add(project);
            }
        }
        this.searchProjectList = projectList;
    }
    
    @JsonIgnore
    public void updateProjectWorkflowList(Project project) {
        List<String> stringList = Shell.executeCommand("panoptes workflow ls -p " + project.getId(),60);
        if(stringList.size() > 0 && !stringList.get(0).equals("error")){
            String string = stringList.get(0);
            for(String s: stringList){
                String[] stringArray = string.split(" ");
                Workflow workflow = new Workflow(stringArray[0],this.convertArrayToStringMethod(stringArray, 1));
                project.getWorkflowList().add(workflow);
            }
        }
    }

    @JsonIgnore
    public void createSubjectSet(String projectId, SubjectSet subjectSet) {
        List<String> stringList = Shell.executeCommand("panoptes subject-set create " + projectId + " " + subjectSet.getTitle());
        subjectSet.setId(stringList.get(0));
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
//    @JsonIgnore
//    public List<String> executeCommand(String command) {
//        logger.info("executeCommand(" + command + ")");
//        List<String> stringList = new ArrayList<>();
//        String s;
//        Process p;
//        try {
//            p = Runtime.getRuntime().exec(command);
//            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
//            while ((s = br.readLine()) != null) {
//                stringList.add(s);
//            }
//
//            if (!p.waitFor(1, TimeUnit.MINUTES)) {
//                System.out.println("exit: " + p.exitValue());
//                //timeout - kill the process. 
//                p.destroy(); // consider using destroyForcibly instead
//            }
//        } catch (Exception e) {
//        }
//        return stringList;
//    }
    
//    @JsonIgnore
//    public List<String> executeCommand(String command) throws IOException {
//        logger.info("executeCommand(" + command + ")");
//        Process process = Runtime.getRuntime().exec(command);
//
//        ExecutorService newFixedThreadPool = Executors.newFixedThreadPool(2);
//
//        Future<String> output = newFixedThreadPool.submit(() -> {
//            return IOUtils.toString(process.getInputStream());
//        });
//        Future<String> error = newFixedThreadPool.submit(() -> {
//            return IOUtils.toString(process.getErrorStream());
//        });
//
//        newFixedThreadPool.shutdown();
//
//// process.waitFor();
//        if (!process.waitFor(3, TimeUnit.SECONDS)) {
//            System.out.println("Destroy");
//            process.destroy();
//        }
//
//        System.out.println(output.get());
//        System.out.println(error.get());
//    }
//    public List<String> executeCommand(String command) {
//        logger.info("executeCommand(" + command + ")");
//        List<String> stringList = new ArrayList<>();
//        String s;
//        Process p;
//        int timeoutInSeconds = 60;
//        int exitValue;
//        try {
//            p = Runtime.getRuntime().exec(command);
//
//            if (timeoutInSeconds <= 0) {
//                exitValue = p.waitFor();
//            } else {
//                long now = System.currentTimeMillis();
//                long timeoutInMillis = 1000L * timeoutInSeconds;
//                long finish = now + timeoutInMillis;
//                while (isAlive(p) && (System.currentTimeMillis() < finish)) {
//                    BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
//                    while ((s = br.readLine()) != null) {
//                        stringList.add(s);
//                    }
//                    Thread.sleep(10);
//                }
//                if (isAlive(p)) {
//                    throw new InterruptedException("Process timeout out after " + timeoutInSeconds + " seconds");
//                }
//                exitValue = p.exitValue();
//                logger.info("executeCommand(" + command + ") exitValue = " + exitValue);
//            }
//        } catch (Exception e) {
//        }
//        return stringList;
//    }
//
//    public static boolean isAlive(Process p) {
//        try {
//            p.exitValue();
//            return false;
//        } catch (IllegalThreadStateException e) {
//            return true;
//        }
//    }
}
