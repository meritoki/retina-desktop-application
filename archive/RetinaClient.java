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

import java.util.List;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.meritoki.app.desktop.retina.model.Model;
import com.meritoki.app.desktop.retina.model.cache.Cache;
import com.meritoki.app.desktop.retina.model.command.Command;
import com.meritoki.app.desktop.retina.model.document.Document;
import com.meritoki.app.desktop.retina.model.document.user.User;
import com.meritoki.library.controller.json.JsonController;

public class RetinaClient {

	private static Logger logger = LogManager.getLogger(RetinaClient.class.getName());
	private String url = null;
	private Properties properties;

	public RetinaClient(Model model) {
		this.properties = model.system.properties;
		boolean gateway = Boolean.parseBoolean((String) this.properties.get("gateway"));
		if (gateway) {
			this.url = this.properties.getProperty("service.web.gateway.url") + "/retina";
		} else {
			this.url = this.properties.getProperty("service.web.retina.url");
		}
	}

	public HttpHeaders getApplicationJsonAuthorizationBearerHttpHeaders() {
		String token = this.properties.getProperty("token");
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		httpHeaders.set("Authorization", "Bearer " + token);
		return httpHeaders;
	}

	public boolean checkHealth() {
		logger.info("checkHealth()");
		boolean flag = false;
		try {
			RestTemplate restTemplate = new RestTemplate();
			String uri = new String(url + "/actuator/health");
			String response = restTemplate.getForObject(uri, String.class);
			Status status = (Status) JsonController.getObject(response, Status.class);
			if (status != null && status.status.equals("UP")) {
				flag = true;
			}
		} catch (ResourceAccessException e) {
			logger.error("ResourceAccessException " + e.getMessage());
		}
		return flag;
	}

	public boolean postDocument(Document document) {
		logger.info("postDocument(" + document + ")");
		boolean flag = false;
		try {
			String json = JsonController.getJson(document);
			RestTemplate restTemplate = new RestTemplate();
			String uri = new String(url + "/document");
			HttpHeaders httpHeaders = this.getApplicationJsonAuthorizationBearerHttpHeaders();
			HttpEntity<String> httpEntity = new HttpEntity<String>(json, httpHeaders);
			String response = restTemplate.postForObject(uri, httpEntity, String.class);
			Status status = (Status) JsonController.getObject(response, Status.class);
			if (status.message.equals("imported")) {
				flag = true;
			}
		} catch (ResourceAccessException e) {
			logger.error("ResourceAccessException " + e.getMessage());
		}
		return flag;
	}
	
	public Document postDocumentCommand(Document document, Command command, Cache cache) {
		logger.info("postDocumentCommand(" + document + ", "+command+")");
		Document d = null;
		try {
			String json = JsonController.getJson(cache);
			RestTemplate restTemplate = new RestTemplate();
			String uri = new String(url + "/document/"+document.uuid+"/command/"+command.name);
			HttpHeaders httpHeaders = this.getApplicationJsonAuthorizationBearerHttpHeaders();
			HttpEntity<String> httpEntity = new HttpEntity<String>(json, httpHeaders);
			String response = restTemplate.postForObject(uri, httpEntity, String.class);
			Status status = (Status) JsonController.getObject(response, Status.class);
			if (status.message.equals("success")) {
				d = (Document) JsonController.getObject(status.data.toString(), Document.class);
			}
		} catch (ResourceAccessException e) {
			logger.error("ResourceAccessException " + e.getMessage());
		}
		return d;
	}

	public boolean postDocumentShare(Document document, User user) {
		boolean flag = false;
		try {
			String json = JsonController.getJson(user);
			RestTemplate restTemplate = new RestTemplate();
			String uri = new String(url + "/document/"+document.uuid+"/share");
			HttpHeaders httpHeaders = this.getApplicationJsonAuthorizationBearerHttpHeaders();
			HttpEntity<String> httpEntity = new HttpEntity<String>(json, httpHeaders);
			String response = restTemplate.postForObject(uri, httpEntity, String.class);
			Status status = (Status) JsonController.getObject(response, Status.class);
			if (status.message.equals("shared")) {
				flag = true;
			} else if (status.message.equals("already shared")) {
				flag = true;
			}
		} catch (ResourceAccessException e) {
			logger.error("ResourceAccessException " + e.getMessage());
		}
		return flag;
	}
	
	public boolean deleteDocument(Document document) {
		boolean flag = false;
		try {
			RestTemplate restTemplate = new RestTemplate();
			String uri = new String(url + "/document/"+document.uuid);
			HttpHeaders httpHeaders = this.getApplicationJsonAuthorizationBearerHttpHeaders();
			HttpEntity httpEntity = new HttpEntity(httpHeaders);
			ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.DELETE, httpEntity, String.class);
			Status status = (Status) JsonController.getObject(response.getBody(), Status.class);
			if (status.message.equals("success")) {
				flag = true;
			}
		} catch (ResourceAccessException e) {
			logger.error("ResourceAccessException");
		}
		return flag;
	}

	public List<String> getDocumentList() {
		List<String> stringList = null;
		try {
			RestTemplate restTemplate = new RestTemplate();
			String uri = new String(url + "/document/");
			HttpHeaders httpHeaders = this.getApplicationJsonAuthorizationBearerHttpHeaders();
			HttpEntity httpEntity = new HttpEntity(httpHeaders);
			ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, httpEntity, String.class);
			Status status = (Status) JsonController.getObject(response.getBody(), Status.class);
			if (status.message.equals("success")) {
				stringList = (List<String>) JsonController.getObject(status.data.toString(),
						new TypeReference<List<String>>() {
						});
			}
		} catch (ResourceAccessException e) {
			logger.error("ResourceAccessException");
		}
		return stringList;
	}

	public Document getDocument(String uuid) {
		Document document = null;
		try {
			RestTemplate restTemplate = new RestTemplate();
			String uri = new String(url + "/document/" + uuid);
			HttpHeaders httpHeaders = this.getApplicationJsonAuthorizationBearerHttpHeaders();
			HttpEntity httpEntity = new HttpEntity(httpHeaders);
			ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, httpEntity, String.class);
			Status status = (Status) JsonController.getObject(response.getBody(), Status.class);
			if (status.message.equals("success")) {
				document = (Document) JsonController.getObject(status.data.toString(), Document.class);
			}
		} catch (ResourceAccessException e) {
			logger.error("ResourceAccessException");
		}
		return document;
	}

	public static void main(String args[]) {
		RetinaClient rc = new RetinaClient(new Model());
		List<String> stringList = rc.getDocumentList();
		if(stringList != null) {
		for (String string : stringList) {
			System.out.println(string);
			Document document = rc.getDocument(string);
			System.out.println(document);
			
		}
		}
		Document d = rc.getDocument("b2327d35-3940-47b4-aa10-1d8e820399b8");
		if(d != null) {
		System.out.println(d);
		User user = new User();
		user.name = "jorodriguez1988@yahoo.com";
		System.out.println(rc.postDocumentShare(d, user));
//		System.out.println(rc.deleteDocument(d));
		}
	}
}
