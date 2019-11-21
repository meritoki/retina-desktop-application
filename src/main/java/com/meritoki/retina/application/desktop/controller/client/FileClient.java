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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
//import com.meritoki.retina.application.desktop.model.document.File;
import com.meritoki.retina.application.desktop.controller.client.json.File;

public class FileClient {

	private static final Logger log = LoggerFactory.getLogger(FileClient.class);
	private String url = "http://localhost:8302";
	private Properties properties = null;

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public boolean checkHealth() {
		boolean flag = false;
		RestTemplate restTemplate = new RestTemplate();
		String uri = new String(url + "/actuator/health");
		String responseJson = restTemplate.getForObject(uri, String.class);
		ObjectMapper mapper = new ObjectMapper();
		Status status = null;
		try {
			status = mapper.readValue(responseJson, Status.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (status.status.equals("UP")) {
			flag = true;
		}
		return flag;
	}

//	public void registerFile(String uuid) {
		////////////////////////////////////
//		Map<String, String> vars = new HashMap<String, String>();
//		vars.put("uuid", file.uuid);
//		RestTemplate restTemplate = new RestTemplate();
//		String uri = new String("http://localhost:8302/register");
//		String returns = restTemplate.postForObject(uri, file, String.class, vars);
		////////////////////
//		RestTemplate restTemplate = new RestTemplate();
//		String url = new String("http://localhost:8302/register");
//		HttpHeaders headers = new HttpHeaders();
//		headers.setContentType(MediaType.APPLICATION_JSON);
//		MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
//		map.add("uuid", uuid);
//		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
//		ResponseEntity<String> response = restTemplate.postForEntity( url, request , String.class );

//	}
	
//	public void registerFile(File file) {
//		Map<String, String> vars = new HashMap<String, String>();
//		vars.put("uuid", file.uuid);
//		RestTemplate restTemplate = new RestTemplate();
//		String uri = new String("http://localhost:8302/register");
//		String returns = restTemplate.postForObject(uri, file, String.class, vars);
//	}
	
	public void registerFile(String uuid) {
//		Map<String, String> vars = new HashMap<String, String>();
		File file = new File();
		file.uuid = uuid;
		RestTemplate restTemplate = new RestTemplate();
		String uri = new String("http://localhost:8302/register");
		String returns = restTemplate.postForObject(uri, file, String.class);
		System.out.println(returns);
	}
	
	public boolean checkFile(String uuid) {
		File file = new File();
		file.uuid = uuid;
		RestTemplate restTemplate = new RestTemplate();
		String uri = new String("http://localhost:8302/check");
		String returns = restTemplate.postForObject(uri, file, String.class);
		boolean flag = Boolean.parseBoolean(returns);
		return flag;
	}
	
	public void markFile(String uuid) {
		File file = new File();
		file.uuid = uuid;
		RestTemplate restTemplate = new RestTemplate();
		String uri = new String("http://localhost:8302/mark");
		String returns = restTemplate.postForObject(uri, file, String.class);
		System.out.println(returns);
	}
	
	public void unmarkFile(String uuid) {
		File file = new File();
		file.uuid = uuid;
		RestTemplate restTemplate = new RestTemplate();
		String uri = new String("http://localhost:8302/unmark");
		String returns = restTemplate.postForObject(uri, file, String.class);
		System.out.println(returns);
	}

	public void uploadFile(java.io.File file) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
		body.add("file", new FileSystemResource(file));
		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
		String serverUrl = "http://localhost:8302/upload";
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response = restTemplate.postForEntity(serverUrl, requestEntity, String.class);
	}

	public void downloadFile(String fileName) { // This method will download file using RestTemplate
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().add(new ByteArrayHttpMessageConverter());
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		ResponseEntity<byte[]> response = restTemplate.exchange("http://localhost:8302/download/" + fileName,
				HttpMethod.GET, entity, byte[].class, "1");
		if (response.getStatusCode() == HttpStatus.OK) {
			try {
				Files.write(Paths.get(fileName), response.getBody());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String args[]) {
		FileClient fileClient = new FileClient();
		fileClient.registerFile("123");
		fileClient.markFile("123");
		System.out.println(fileClient.checkFile("123"));
		fileClient.unmarkFile("123");
		System.out.println(fileClient.checkFile("123"));
//		fileClient.uploadFile(new java.io.File("./data/image/01.jpg"));
//		fileClient.downloadFile("./01.jpg");
	}
}