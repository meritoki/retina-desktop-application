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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meritoki.app.desktop.retina.model.Model;
import com.meritoki.library.controller.json.JsonController;
import com.meritoki.library.controller.node.NodeController;

public class FileClient {

	private static Logger logger = LogManager.getLogger(FileClient.class.getName());
	private String url = null;
	private Properties properties = null;

	public FileClient(Model model) {
		this.properties = model.system.properties;
		boolean gateway = Boolean.parseBoolean((String) this.properties.get("gateway"));
		if(gateway) {
			this.url = this.properties.getProperty("service.web.gateway.url")+"/file";
		} else {
			this.url = this.properties.getProperty("service.web.file.url");
		}
	}

	public boolean checkHealth() {
		logger.info("checkHealth()");
		boolean flag = false;
		try {
			RestTemplate restTemplate = new RestTemplate();
			String uri = new String(url + "/actuator/health");
			String response = restTemplate.getForObject(uri, String.class);
			Status status = (Status)JsonController.getObject(response, Status.class);
			if (status != null && status.status.equals("UP")) {
				flag = true;
			}
		} catch (ResourceAccessException e) {
			logger.error("ResourceAccessException");
		}
		return flag;
	}
	
	public void registerFile(String uuid) {
		logger.info("registerFile("+uuid+")");
		ObjectMapper mapper = new ObjectMapper();
		File file = new File();
		file.uuid = uuid;
		String fileJson;
		try {
			fileJson = mapper.writeValueAsString(file);
			RestTemplate restTemplate = new RestTemplate();
			String uri = new String(url+"/register");
			String token = this.properties.getProperty("token");
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("Authorization", "Bearer " + token);
			HttpEntity<String> entity = new HttpEntity<String>(fileJson, headers);
			String returns = restTemplate.postForObject(uri, entity, String.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (ResourceAccessException e) {
			logger.error("ResourceAccessException");
		}
	}
	
	public boolean checkFile(String uuid) {
		logger.info("checkFile("+uuid+")");
		ObjectMapper mapper = new ObjectMapper();
		File file = new File();
		file.uuid = uuid;
		String fileJson;
		boolean flag = false;
		try {
			fileJson = mapper.writeValueAsString(file);
			RestTemplate restTemplate = new RestTemplate();
			String uri = new String(url+"/check");
			String token = this.properties.getProperty("token");
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("Authorization", "Bearer " + token);
			HttpEntity<String> entity = new HttpEntity<String>(fileJson, headers);
			String returns = restTemplate.postForObject(uri, entity, String.class);
			flag = Boolean.parseBoolean(returns);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (ResourceAccessException e) {
			logger.error("ResourceAccessException");
		}
		return flag;
	}
	
	public void markFile(String uuid) {
		logger.info("markFile("+uuid+")");
		ObjectMapper mapper = new ObjectMapper();
		File file = new File();
		file.uuid = uuid;
		String fileJson;
		try {
			fileJson = mapper.writeValueAsString(file);
			RestTemplate restTemplate = new RestTemplate();
			String uri = new String(url+"/mark");
			String token = this.properties.getProperty("token");
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("Authorization", "Bearer " + token);
			HttpEntity<String> entity = new HttpEntity<String>(fileJson, headers);
			String returns = restTemplate.postForObject(uri, entity, String.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (ResourceAccessException e) {
			logger.error("ResourceAccessException");
		}
	}
	
	public void unmarkFile(String uuid) {
		logger.info("unmarkFile("+uuid+")");
		ObjectMapper mapper = new ObjectMapper();
		File file = new File();
		file.uuid = uuid;
		String fileJson;
		try {
			fileJson = mapper.writeValueAsString(file);
			RestTemplate restTemplate = new RestTemplate();
			String uri = new String(url+"/unmark");
			String token = this.properties.getProperty("token");
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("Authorization", "Bearer " + token);
			HttpEntity<String> entity = new HttpEntity<String>(fileJson, headers);
			String returns = restTemplate.postForObject(uri, entity, String.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (ResourceAccessException e) {
			logger.error("ResourceAccessException");
		}
	}

	public void uploadFile(String filePath, String fileName) {
		logger.info("uploadFile("+filePath+","+fileName+")");
		java.io.File file = new java.io.File(filePath+fileName);
		String token = this.properties.getProperty("token");
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		headers.set("Authorization", "Bearer " + token);
		MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
		body.add("file", new FileSystemResource(file));
		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
		String serverUrl = url+"/upload";
		RestTemplate restTemplate = new RestTemplate();
		try {
			ResponseEntity<String> response = restTemplate.postForEntity(serverUrl, requestEntity, String.class);
		} catch (ResourceAccessException e) {
			logger.error("ResourceAccessException");
		}
	}

	public void downloadFile(String path, String fileName) { // This method will download file using RestTemplate
		logger.info("downloadFile("+fileName+")");
		try {
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().add(new ByteArrayHttpMessageConverter());
		String token = this.properties.getProperty("token");
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + token);
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		ResponseEntity<byte[]> response = restTemplate.exchange(url+"/download/" + fileName,
				HttpMethod.GET, entity, byte[].class, "1");
		if (response.getStatusCode() == HttpStatus.OK) {
			try {
				Files.write(Paths.get(path+fileName), response.getBody());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		} catch (ResourceAccessException e) {
			logger.error("ResourceAccessException");
		}
	}

	public static void main(String args[]) {
		FileClient fileClient = new FileClient(new Model());
		fileClient.checkHealth();
		fileClient.registerFile("123");
		fileClient.checkFile("123");
		fileClient.markFile("123");
		fileClient.unmarkFile("123");
		fileClient.uploadFile("./data/image/","01.jpg");
		fileClient.downloadFile("."+NodeController.getSeperator(),"01.jpg");
	}
}