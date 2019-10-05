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
package com.meritoki.retina.application.desktop.controller.client;

import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import com.meritoki.retina.application.desktop.model.project.Project;

public class ModelClient {

    private static final Logger log = LoggerFactory.getLogger(ModelClient.class);
    private String url = "http://localhost:8301";
    
    public void configure(Properties properties) {
    	url = properties.getProperty("service.web.gateway.url");
    }
    
    public void importProject(String project) {
    	RestTemplate restTemplate = new RestTemplate();
    	String uri = new String(url+"/import");
    	HttpHeaders headers = new HttpHeaders();
    	headers.setContentType(MediaType.APPLICATION_JSON);
    	HttpEntity<String> entity = new HttpEntity<String>(project, headers);
    	String string = restTemplate.postForObject(uri, entity, String.class);
    	System.out.println(string);
    }

//    public static void main(String args[]) {
//        Map<String, String> vars = new HashMap<String, String>();
//        vars.put("uuid", "123");
//        RestTemplate rt = new RestTemplate();
//        String uri = new String("http://localhost:8080/machine/register");
//        FileTest u = new FileTest();
//        u.uuid = "123";
//        String returns = rt.postForObject(uri, u, String.class, vars);
//        System.out.println(returns);
//    }
}