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
package com.meritoki.app.desktop.retina.model.system;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.meritoki.app.desktop.retina.controller.node.NodeController;
import com.meritoki.app.desktop.retina.controller.user.UserController;
import com.meritoki.app.desktop.retina.model.document.Guide;
import com.meritoki.app.desktop.retina.model.document.Image;
import com.meritoki.app.desktop.retina.model.document.Matrix;
import com.meritoki.app.desktop.retina.model.document.Page;
import com.meritoki.app.desktop.retina.model.document.Point;
import com.meritoki.app.desktop.retina.model.document.Selection;
import com.meritoki.app.desktop.retina.model.document.Shape;
import com.meritoki.app.desktop.retina.model.document.user.User;
import com.meritoki.app.desktop.retina.model.provider.Provider;
import com.meritoki.app.desktop.retina.model.provider.meritoki.Meritoki;
import com.meritoki.app.desktop.retina.model.provider.zooniverse.Zooniverse;
import com.meritoki.app.desktop.retina.model.tool.Tool;
import com.meritoki.app.desktop.retina.model.vendor.Vendor;
import com.meritoki.app.desktop.retina.model.vendor.google.Google;

public class System {
	private static final Logger logger = LogManager.getLogger(System.class.getName());
	@JsonIgnore
	public Properties properties = null;
	@JsonProperty
	public Map<String,Provider> providerMap = new HashMap<>();
	@JsonProperty
	public Map<String,Vendor> vendorMap = new HashMap<>();
	@JsonProperty
	public Tool tool = null;
	@JsonIgnore
	public List<User> userList = new ArrayList<User>();
	@JsonIgnore
	public User user = null;//When a user is logged in the User is retained in this variable
	@JsonIgnore
	public File file = null;//The file that corresponds to the loaded document
	@JsonIgnore
	public String defaultFileName = "Untitled.json";
	@JsonIgnore
	public String defaultProjectFileName = "Untitled.ret";
	@JsonIgnore 
	public boolean machine = true;
	@JsonIgnore
	public boolean multiUser = false;
	@JsonIgnore
	public boolean newUser = false;
	@JsonIgnore
	public boolean loginUser = false;
	@JsonIgnore
	public boolean loggedIn = false;
	@JsonIgnore
	public boolean newDocument = true;
	@JsonIgnore
	public boolean isConnected = false;
	@JsonIgnore
	public int pageIndex = 0;
	@JsonIgnore
	public int imageIndex = 0;
	@JsonIgnore
	public int shapeIndex = 0;
	@JsonIgnore
	public Page page;
	@JsonIgnore
	public Image image;
	@JsonIgnore
	public Shape shape;
	@JsonIgnore
	public Guide guide;
	@JsonIgnore
	public Matrix matrix;
	@JsonIgnore
	public Page pressedPage;
	@JsonIgnore
	public Image pressedImage;
	@JsonIgnore
	public Image releasedImage;
	@JsonIgnore
	public Shape pressedShape;
	@JsonIgnore
	public Point pressedPoint;
	@JsonIgnore
	public Point releasedPoint;
	@JsonIgnore
	public Selection selection;

	public System() {
		this.init();
	}
	
	public void init() {
		logger.info("init()");
		this.initDirectories();
		this.initUsers();
		this.initProviders();
		this.initVendors();
		this.initProperties();
	}
	
	public void initDirectories() {
		if(!new File(NodeController.getSystemHome()).exists()) {
			new File(NodeController.getSystemHome()).mkdirs();
		}
		if(!new File(NodeController.getDocumentCache()).exists()) {
			new File(NodeController.getDocumentCache()).mkdirs();
		}
		if(!new File(NodeController.getResourceCache()).exists()) {
			new File(NodeController.getResourceCache()).mkdirs();
		}
	}
	
	public void initUsers() {
		if(UserController.exists()) {
			this.userList = UserController.open();
		}
		this.user = UserController.getAnonymousUser();
		if (this.userList.size() == 0) {
			this.newUser = true;
		} else {
			this.loginUser = true;
		}
	}
	
	public void initProperties() {
		if(multiUser)
			this.properties = NodeController.openPropertiesXML(".","retina.xml");
		else 
			this.properties = new Properties();
	}
	
	public void initProviders() {
		logger.info("initProviders()");
//		this.providerList.add(new Zooniverse());
		this.providerMap.put("zooniverse", new Zooniverse());
		if(this.machine)
			this.providerMap.put("meritoki", new Meritoki());
	}
	
	public void initVendors() {
		this.vendorMap.put("google", new Google());
	}
}
