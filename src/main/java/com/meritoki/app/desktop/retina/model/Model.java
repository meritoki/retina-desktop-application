package com.meritoki.app.desktop.retina.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.annotate.JsonIgnore;

import com.meritoki.app.desktop.retina.controller.security.SecurityController;
import com.meritoki.app.desktop.retina.controller.user.UserController;
import com.meritoki.app.desktop.retina.model.document.Document;
import com.meritoki.app.desktop.retina.model.document.user.User;
import com.meritoki.app.desktop.retina.model.resource.Resource;
import com.meritoki.app.desktop.retina.model.system.System;

public class Model {
	
	private static final Logger logger = LogManager.getLogger(Model.class.getName());
	public Resource resource = new Resource();
	public System system = new System();
	public Document document = new Document();
	public List<User> userList = new ArrayList<User>();
	
	public Model() {
		this.initUsers();
	}
	
	public void initUsers() {
		logger.info("initUsers()");
		UserController userController = new UserController(this);
		if(userController.exists()) {
			this.userList = userController.open();
		}
		this.system.user = userController.getAnonymousUser();
		this.document.pattern.user = this.system.user;
		
		if (this.userList.size() == 0) {
			this.system.newUser = false;
		} else {
			this.system.loginUser = false;
		}
	}
}
