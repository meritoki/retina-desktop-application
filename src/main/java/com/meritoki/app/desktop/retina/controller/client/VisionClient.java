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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Properties;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
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
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meritoki.app.desktop.retina.controller.client.json.File;
import com.meritoki.app.desktop.retina.controller.client.json.Network;
import com.meritoki.app.desktop.retina.controller.node.NodeController;
import com.meritoki.app.desktop.retina.model.document.user.User;

public class VisionClient {

	private static Logger logger = LogManager.getLogger(VisionClient.class.getName());
	private String url = null;
	private Properties properties = null;

	public VisionClient() {
		this.properties = NodeController.openProperties("./retina-desktop.properties");
		boolean gateway = Boolean.parseBoolean((String) this.properties.get("gateway"));
		if (gateway) {
			this.url = this.properties.getProperty("service.web.gateway.url") + "/vision";
		} else {
			this.url = this.properties.getProperty("service.web.vision.url");
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

	public String newNetwork(String uuid) {
		logger.info("newNetwork(" + uuid + ")");
		ObjectMapper mapper = new ObjectMapper();
		Network file = new Network();
		file.uuid = uuid;
		String fileJson;
		uuid = null;
		boolean flag = false;
		try {
			fileJson = mapper.writeValueAsString(file);
			RestTemplate restTemplate = new RestTemplate();
			String uri = new String(url + "/network/new");
			this.properties = NodeController.openProperties("./retina-desktop.properties");
			String token = this.properties.getProperty("token");
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("Authorization", "Bearer " + token);
			HttpEntity<String> entity = new HttpEntity<String>(fileJson, headers);
			String returns = restTemplate.postForObject(uri, entity, String.class);
			uuid = returns;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (ResourceAccessException e) {
			logger.error("ResourceAccessException");
		}
		return uuid;
	}

	public void saveNetwork(String uuid) {
		logger.info("saveNetwork(" + uuid + ")");
		ObjectMapper mapper = new ObjectMapper();
		Network network = new Network();
		network.uuid = uuid;
		String networkJson;
		try {
			networkJson = mapper.writeValueAsString(network);
			RestTemplate restTemplate = new RestTemplate();
			String uri = new String(url + "/network/save");
			this.properties = NodeController.openProperties("./retina-desktop.properties");
			String token = this.properties.getProperty("token");
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("Authorization", "Bearer " + token);
			HttpEntity<String> entity = new HttpEntity<String>(networkJson, headers);
			String returns = restTemplate.postForObject(uri, entity, String.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (ResourceAccessException e) {
			logger.error("ResourceAccessException");
		}
	}

	public void loadNetwork(String uuid) {
		logger.info("loadNetwork(" + uuid + ")");
		ObjectMapper mapper = new ObjectMapper();
		Network network = new Network();
		network.uuid = uuid;
		String networkJson;
		try {
			networkJson = mapper.writeValueAsString(network);
			RestTemplate restTemplate = new RestTemplate();
			String uri = new String(url + "/network/load");
			this.properties = NodeController.openProperties("./retina-desktop.properties");
			String token = this.properties.getProperty("token");
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("Authorization", "Bearer " + token);
			HttpEntity<String> entity = new HttpEntity<String>(networkJson, headers);
			String returns = restTemplate.postForObject(uri, entity, String.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (ResourceAccessException e) {
			logger.error("ResourceAccessException");
		}
	}

	public void trainNetwork(String name, double scale, int x, int y, String concept) {
		logger.info("trainNetwork(" + name + ","+scale+","+x+","+y+","+concept+")");
		ObjectMapper mapper = new ObjectMapper();
		File file = new File();
		file.name = name;
		file.scale = scale;
		file.x = x;
		file.y = y;
		file.concept = concept;
		String fileJson;
		try {
			fileJson = mapper.writeValueAsString(file);
			RestTemplate restTemplate = new RestTemplate();
			String uri = new String(url + "/network/train");
			this.properties = NodeController.openProperties("./retina-desktop.properties");
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

	public String inferNetwork(String name, double scale, int x, int y) {
		logger.info("inferNetwork(" + name+ ","+scale+","+x+","+y+")");
		ObjectMapper mapper = new ObjectMapper();
		File file = new File();
		file.name = name;
		file.scale = scale;
		file.x = x;
		file.y = y;
		String fileJson;
		String string = null;
		try {
			fileJson = mapper.writeValueAsString(file);
			RestTemplate restTemplate = new RestTemplate();
			String uri = new String(url + "/network/infer");
			this.properties = NodeController.openProperties("./retina-desktop.properties");
			String token = this.properties.getProperty("token");
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("Authorization", "Bearer " + token);
			HttpEntity<String> entity = new HttpEntity<String>(fileJson, headers);
			String returns = restTemplate.postForObject(uri, entity, String.class);
			string = returns;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (ResourceAccessException e) {
			logger.error("ResourceAccessException");
		}
		return string;
	}

	public void uploadFile(String filePath, String fileName) {
		logger.info("uploadFile(" + filePath + "," + fileName + ")");
		java.io.File file = new java.io.File(filePath + fileName);
		this.properties = NodeController.openProperties("./retina-desktop.properties");
		String token = this.properties.getProperty("token");
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		headers.set("Authorization", "Bearer " + token);
		MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
		body.add("file", new FileSystemResource(file));
		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
		String serverUrl = url + "/file/upload";
		RestTemplate restTemplate = new RestTemplate();
		try {
			ResponseEntity<String> response = restTemplate.postForEntity(serverUrl, requestEntity, String.class);
		} catch (ResourceAccessException e) {
			logger.error("ResourceAccessException");
		} catch(HttpServerErrorException e) {
			logger.error("HttpServerErrorException");
		}
	}

	public void downloadFile(String path, String fileName) { // This method will download file using RestTemplate
		logger.info("downloadFile(" + fileName + ")");
		try {
			RestTemplate restTemplate = new RestTemplate();
			restTemplate.getMessageConverters().add(new ByteArrayHttpMessageConverter());
			String token = this.properties.getProperty("token");
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + token);
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
			HttpEntity<String> entity = new HttpEntity<String>(headers);
			ResponseEntity<byte[]> response = restTemplate.exchange(url + "/file/download/" + fileName, HttpMethod.GET,
					entity, byte[].class, "1");
			if (response.getStatusCode() == HttpStatus.OK) {
				try {
					Files.write(Paths.get(path + fileName), response.getBody());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (ResourceAccessException e) {
			logger.error("ResourceAccessException");
		}
	}

	public static void main(String args[]) {
		VisionClient visionClient = new VisionClient();
		UserClient userClient = new UserClient();
		User user = new User();
		user.name = "javainuse";
		user.password = "password";
		if (userClient.checkHealth()) {
			if (userClient.login(user)) {
				String uuid = UUID.randomUUID().toString();
				visionClient.newNetwork(uuid);
				visionClient.saveNetwork(uuid);
				visionClient.loadNetwork(uuid);
				
				visionClient.uploadFile("./data/image/", "01.jpg");
//				visionClient.downloadFile(NodeController.getImageCache() + NodeController.getSeperator(), "01.jpg");
				visionClient.trainNetwork("01.jpg", 1, 0, 0, "test");
				visionClient.inferNetwork("01.jpg", 1, 0, 0);
			}
		}
	}
}

//public void registerFile(String uuid) {
////////////////////////////////////
//	Map<String, String> vars = new HashMap<String, String>();
//	vars.put("uuid", file.uuid);
//	RestTemplate restTemplate = new RestTemplate();
//	String uri = new String("http://localhost:8302/register");
//	String returns = restTemplate.postForObject(uri, file, String.class, vars);
////////////////////
//	RestTemplate restTemplate = new RestTemplate();
//	String url = new String("http://localhost:8302/register");
//	HttpHeaders headers = new HttpHeaders();
//	headers.setContentType(MediaType.APPLICATION_JSON);
//	MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
//	map.add("uuid", uuid);
//	HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
//	ResponseEntity<String> response = restTemplate.postForEntity( url, request , String.class );

//}

//public void registerFile(File file) {
//	Map<String, String> vars = new HashMap<String, String>();
//	vars.put("uuid", file.uuid);
//	RestTemplate restTemplate = new RestTemplate();
//	String uri = new String("http://localhost:8302/register");
//	String returns = restTemplate.postForObject(uri, file, String.class, vars);
//}