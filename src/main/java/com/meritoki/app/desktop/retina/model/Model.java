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
package com.meritoki.app.desktop.retina.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.meritoki.app.desktop.retina.controller.client.ClientController;
import com.meritoki.app.desktop.retina.controller.node.NodeController;
import com.meritoki.app.desktop.retina.controller.user.UserController;
import com.meritoki.app.desktop.retina.model.cache.Cache;
import com.meritoki.app.desktop.retina.model.document.Document;
import com.meritoki.app.desktop.retina.model.document.Grid;
import com.meritoki.app.desktop.retina.model.document.Shape;
import com.meritoki.app.desktop.retina.model.document.user.User;
import com.meritoki.app.desktop.retina.model.pattern.Pattern;
import com.meritoki.app.desktop.retina.model.provider.Provider;
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
		this.newDocument();
	}
	
	public void setProviderModel() {
		for(Entry<String, Provider> entry:this.system.providerMap.entrySet()) {
			Provider provider = entry.getValue();
			provider.setModel(this);
			provider.init();
		}
	}
	
	public void newDocument() {
		this.document = new Document();
		this.system.newDocument = true;
		this.system.tool = null;
		this.system.file = null;
		this.setProviderModel();
	}
	
	public List<String> getDocumentList() {
		ClientController clientController = new ClientController(this);
		List<String> stringList = new ArrayList<>();//clientController.retinaClient.getDocumentList();
		return stringList;
	}
	
	public void openDocument(String uuid) {
		ClientController clientController = new ClientController(this);
		this.document = new Document();//clientController.retinaClient.getDocument(uuid);
		File directory = new File(NodeController.getDocumentCache(this.document.uuid));
		if(!directory.exists()) {
			directory.mkdirs();
		}
	}
	
	public void openDocument(File file) {
		logger.info("openDocument("+file+")");
		this.system.file = file;
		this.document = NodeController.openDocument(this.system.file);
		this.report(this.document);
		this.system.newDocument = false;
		this.resource.addRecent(this.system.file.getAbsolutePath());
		File directory = new File(NodeController.getDocumentCache(this.document.uuid));
		if(!directory.exists()) {
			directory.mkdirs();
		}
		for(Entry<String, Provider> entry:this.system.providerMap.entrySet()) {
			Provider provider = entry.getValue();
			provider.init();
		}
	}
	
	public void report(Document document) {
		int pageCount = document.getPageList().size();
		List<Shape> shapeList = document.getShapeList();
		int shapeCount = shapeList.size();
		int gridCount = 0;
		int dataTextCount = 0;
		int emptyTextCount = 0;
		int nullTextCount = 0;
		
		for(Shape shape: shapeList) {
			if(shape instanceof Grid) {
				gridCount++;
				shapeCount--;
				List<Shape> gridShapeList = ((Grid)shape).getShapeList();
				shapeCount += gridShapeList.size();
				for(Shape gridShape:gridShapeList) {
					if(gridShape.getData().getText().value != null) {
						dataTextCount++;
						if(gridShape.getData().getText().value.trim().isEmpty()) {
							emptyTextCount++;
						}
					} else {
						nullTextCount++;
					}
				}
			} else {
				if(shape.getData().getText().value != null) {
					dataTextCount++;
					if(shape.getData().getText().value.trim().isEmpty()) {
						emptyTextCount++;
					}
				}else {
					nullTextCount++;
				}
			}
		}
		logger.info("report("+document.uuid+") Report");
		logger.info("report("+document.uuid+") pageCount="+pageCount);
		logger.info("report("+document.uuid+") shapeCount="+shapeCount);
		logger.info("report("+document.uuid+") gridCount="+gridCount);
		logger.info("report("+document.uuid+") dataTextCount="+dataTextCount);
		logger.info("report("+document.uuid+") emptyTextCount="+emptyTextCount);
		logger.info("report("+document.uuid+") nullTextCount="+nullTextCount);
	}
	
	public void saveDocument(File file) {
		logger.info("saveDocument("+file+")");
		this.system.file = file;
		this.pattern.save();
		NodeController.saveDocument(this.system.file, this.document);
		this.resource.addRecent(this.system.file.getAbsolutePath());
		this.system.newDocument = false;
		for(Entry<String, Provider> entry:this.system.providerMap.entrySet()) {
			Provider provider = entry.getValue();
			provider.save();
		}
	}
	public void saveDocument() {
		logger.info("saveDocument()");
		this.pattern.save();
		NodeController.saveDocument(this.system.file, this.document);
		this.resource.addRecent(this.system.file.getAbsolutePath());
		this.system.newDocument = false;
		for(Entry<String, Provider> entry:this.system.providerMap.entrySet()) {
			Provider provider = entry.getValue();
			provider.save();
		}

	}
	
	public boolean loginUser(String name, String password) {
		UserController userController = new UserController(this.system);
		if(this.system.multiUser && this.system.isConnected) {
			ClientController clientController = new ClientController(this);
			User user = new User();
			user.name = name;
			user.password = password;
			this.system.loggedIn = false;//clientController.userClient.login(user);
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
//for (Provider provider : this.system.providerList) {
//if (provider instanceof Meritoki) {
//  Meritoki meritoki = (Meritoki) provider;
//  meritoki.open(this.document.uuid);
////  meritoki.init();
//}
//}
//Meritoki meritoki = (Meritoki) this.system.providerMap.get("meritoki");
//meritoki.setModel(this);
//meritoki.open(this.document.uuid);
//meritoki.init();
//
//for (Provider provider : this.system.providerList) {
//    if (provider instanceof Meritoki) {
//        Meritoki meritoki = (Meritoki) provider;
//        meritoki.setModel(this);
//        meritoki.open(this.document.uuid);
//        meritoki.init();
//    }
//}

//for (Provider provider : this.system.providerList) {
//if (provider instanceof Meritoki) {
//  Meritoki meritoki = (Meritoki) provider;
//  meritoki.save();
//}
//}

//for (Provider provider : this.system.providerList) {
//if (provider instanceof Meritoki) {
//  Meritoki meritoki = (Meritoki) provider;
//  meritoki.save();
//}
//}

