/*
 * Copyright 2020 Joaquin Osvaldo Rodriguez
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
package com.meritoki.app.desktop.retina.model;

import java.io.File;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.meritoki.app.desktop.retina.controller.client.ClientController;
import com.meritoki.app.desktop.retina.controller.document.DocumentController;
import com.meritoki.app.desktop.retina.controller.node.NodeController;
import com.meritoki.app.desktop.retina.controller.user.UserController;
import com.meritoki.app.desktop.retina.model.cache.Cache;
import com.meritoki.app.desktop.retina.model.command.Pattern;
import com.meritoki.app.desktop.retina.model.document.Document;
import com.meritoki.app.desktop.retina.model.document.Image;
import com.meritoki.app.desktop.retina.model.document.Page;
import com.meritoki.app.desktop.retina.model.document.user.User;
import com.meritoki.app.desktop.retina.model.resource.Resource;
import com.meritoki.app.desktop.retina.model.system.System;

public class Model {
	private static final Logger logger = LogManager.getLogger(Model.class.getName());
	/**
	 * System is an object that retains objects relative to the operation of Retina
	 */
	public System system = new System();
	/**
	 * Resource is an object that retains data that can be loaded for use in Retina
	 */
	public Resource resource = new Resource();
	/** 
	 * Document is an object that is manipulated by Retina
	 */
	public Document document = new Document();
	/**
	 * Pattern is used as the control interface with a document and web services
	 */
	public Pattern pattern = new Pattern();
	/**
	 * Cache retains variables for use with the Command Pattern
	 */
	public Cache cache = new Cache();

	public Model() {
		this.pattern.setModel(this);
	}
	
	public List<String> getDocumentList() {
		ClientController clientController = new ClientController(this);
		List<String> stringList = clientController.retinaClient.getDocumentList();
		return stringList;
	}
	
	public void openDocument(String uuid) {
		ClientController clientController = new ClientController(this);
		this.document = clientController.retinaClient.getDocument(uuid);
		File directory = new File(NodeController.getDocumentCache(this.document.uuid));
		if(!directory.exists()) {
			directory.mkdirs();
		}
	}
	
	public void openDocument(File file) {
		logger.info("openDocument("+file+")");
		this.system.file = file;
		this.document = (DocumentController.open(this.system.file));
		this.system.newDocument = false;
		this.resource.addRecent(this.system.file.getAbsolutePath());
		File directory = new File(NodeController.getDocumentCache(this.document.uuid));
		if(!directory.exists()) {
			directory.mkdirs();
		}
	}
	
	public void saveDocument(File file) {
		logger.info("saveDocument("+file+")");
		this.system.file = file;
		this.pattern.save();
		DocumentController.save(this.system.file, this.document);
		this.resource.addRecent(this.system.file.getAbsolutePath());
		this.system.newDocument = false;
	}
	public void saveDocument() {
		logger.info("saveDocument()");
		this.pattern.save();
		DocumentController.save(this.system.file, this.document);
		this.resource.addRecent(this.system.file.getAbsolutePath());
		this.system.newDocument = false;
	}
	
	public boolean loginUser(String name, String password) {
		UserController userController = new UserController(this.system);
		if(this.system.isConnected) {
			ClientController clientController = new ClientController(this);
			User user = new User();
			user.name = name;
			user.password = password;
			this.system.loggedIn = clientController.userClient.login(user);
		} else {
			this.system.loggedIn = userController.loginUser(name, password);	
		}
		return this.system.loggedIn;
	}
	
	public void logoutUser() {
		this.system.loggedIn = false;
		this.system.initUsers();
	}
}
