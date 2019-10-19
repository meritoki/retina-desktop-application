/*
 * Copyright 2019 jorodriguez.
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

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meritoki.retina.application.desktop.model.Model;
import com.meritoki.retina.application.desktop.model.account.User;

/**
 *
 * @author jorodriguez
 */
public class UserClient {
	
	private static final Logger logger = LoggerFactory.getLogger(UserClient.class);
	private String url = "http://localhost:8300";
	private Model model = null;
	private Token token = null;
	
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
}
