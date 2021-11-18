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
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meritoki.app.desktop.retina.model.Model;
import com.meritoki.app.desktop.retina.model.document.user.User;
import com.meritoki.library.controller.node.NodeController;

/**
 *
 * @author jorodriguez
 */
public class UserClient {
	
	private static Logger logger = LogManager.getLogger(UserClient.class.getName());
	private String url = null;
//	private Token token = null;
	public Properties properties = null;
	public String token = null;
	
	public UserClient(Model model) {
		this.properties = model.system.properties;
		boolean gateway = Boolean.parseBoolean((String) this.properties.get("gateway"));
		if(gateway) {
			this.url = this.properties.getProperty("service.web.gateway.url")+"/user";
		} else {
			this.url = this.properties.getProperty("service.web.user.url");
		}
	}
	
	public boolean checkHealth() {
		logger.info("checkHealth()");
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
	
	public boolean login(User user) {
		logger.info("login("+user+")");
		boolean flag = false;
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
				Token token = mapper.readValue(responseJson, Token.class);
				this.properties.setProperty("token", token.token);
				NodeController.saveProperties(".", "retina.xml", this.properties);
				flag = true;
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
		return flag;
	}
	
}
