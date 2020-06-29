/*
 * Copyright 2019 Meritoki All Rights Reserved.
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
package com.meritoki.app.desktop.retina.controller.client;

import java.io.IOException;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meritoki.app.desktop.retina.model.Model;
import com.meritoki.app.desktop.retina.model.document.Document;
import com.meritoki.app.desktop.retina.model.document.Point;
import com.meritoki.app.desktop.retina.model.document.Shape;
import com.meritoki.app.desktop.retina.model.document.user.User;

public class ModelClient {

	private static Logger logger = LogManager.getLogger(ModelClient.class.getName());
	private String url = null;
	private Token token = null;
	private Properties properties;
	
	public ModelClient(Model model) {
		this.properties = model.system.properties;
		boolean gateway = Boolean.parseBoolean((String) this.properties.get("gateway"));
		if(gateway) {
			this.url = this.properties.getProperty("service.web.gateway.url")+"/model";
		} else {
			this.url = this.properties.getProperty("service.web.model.url");
		}
	}

	public boolean checkHealth() {
		boolean flag = false;
		Status status = null;
		try {
			RestTemplate restTemplate = new RestTemplate();
			String uri = new String(url + "/actuator/health");
			String responseJson = restTemplate.getForObject(uri, String.class);
			ObjectMapper mapper = new ObjectMapper();
			try {
				status = mapper.readValue(responseJson, Status.class);
			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (ResourceAccessException e) {
			logger.error("ResourceAccessException");
		}
		if (status != null && status.status.equals("UP")) {
			flag = true;
		}
		return flag;
	}

	public void updateProject() {

	}

	public void login(User user) {
		RestTemplate restTemplate = new RestTemplate();
		String uri = new String(url + "/authenticate");
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		String body = "{\"username\":\"" + user.name + "\", \"password\":\"" + user.password + "\"}";
		logger.info(body);
		HttpEntity<String> entity = new HttpEntity<String>(body, headers);
		try {
			String responseJson = restTemplate.postForObject(uri, entity, String.class);
			ObjectMapper mapper = new ObjectMapper();
			try {
				this.token = mapper.readValue(responseJson, Token.class);
				logger.info("login(user) token.token = "+token.token);
			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (ResourceAccessException e) {
			logger.error("ResourceAccessException");
		}
	}

	public void uploadDocument(Document document) {
		logger.info("uploadDocument(" + document + ")");
		ObjectMapper mapper = new ObjectMapper();
		String documentJson;
		try {
			documentJson = mapper.writeValueAsString(document);
			RestTemplate restTemplate = new RestTemplate();
			String uri = new String(url + "/import");
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("Authorization", "Bearer " + this.token.token);
			HttpEntity<String> entity = new HttpEntity<String>(documentJson, headers);
			String string = restTemplate.postForObject(uri, entity, String.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}

	public String downloadProject(String uuid) {
		String string = null;
		RestTemplate restTemplate = new RestTemplate();
		String uri = new String(url + "/project/" + uuid);
		string = restTemplate.getForObject(uri, String.class);
		return string;
	}
	
	public Shape getShape(Point point) {
//		RestTemplate restTemplate = new RestTemplate();
//		String uri = new String(url + "/project/");
//		HttpHeaders headers = new HttpHeaders();
//		headers.setContentType(MediaType.APPLICATION_JSON);
//		headers.set("Authorization", "Bearer " + this.token.token);
//		HttpEntity<String> entity = new HttpEntity<String>(project, headers);
//		String string = restTemplate.postForObject(uri, entity, String.class);
//		System.out.println(string);
		
		return null;
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
