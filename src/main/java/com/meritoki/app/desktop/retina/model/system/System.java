package com.meritoki.app.desktop.retina.model.system;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

import com.meritoki.app.desktop.retina.controller.node.NodeController;
import com.meritoki.app.desktop.retina.controller.security.SecurityController;
import com.meritoki.app.desktop.retina.controller.user.UserController;
import com.meritoki.app.desktop.retina.model.Model;
import com.meritoki.app.desktop.retina.model.document.user.User;
import com.meritoki.app.desktop.retina.model.provider.Provider;
import com.meritoki.app.desktop.retina.model.provider.zooniverse.Zooniverse;
import com.meritoki.app.desktop.retina.model.vendor.Vendor;
import com.meritoki.app.desktop.retina.model.vendor.microsoft.Microsoft;

public class System {
	private static final Logger logger = LogManager.getLogger(System.class.getName());
	@JsonIgnore
	public Properties properties = null;
	@JsonProperty
	public List<Provider> providerList = new ArrayList<>();
	@JsonProperty
	public List<Vendor> vendorList = new ArrayList<>();
	@JsonIgnore
	public List<User> userList = new ArrayList<User>();
	@JsonIgnore
	public User user = null;//When a user is logged in the User is retained in this variable
	@JsonIgnore
	public File file = null;//The file that corresponds to the loaded document
	@JsonIgnore
	public String defaultFileName = "Untitled.json";
	@JsonIgnore
	public boolean newUser = false;
	@JsonIgnore
	public boolean loginUser = false;
	@JsonIgnore
	public boolean newDocument = true;

	public System() {
		this.initDirectories();
		this.initProviders();
		this.initVendors();
		this.initProperties();
		this.initUsers();
	}
	
	public void initDirectories() {
		if(!new File(NodeController.getRetinaHome()).exists()) {
			new File(NodeController.getRetinaHome()).mkdirs();
		}
		if(!new File(NodeController.getImageCache()).exists()) {
			new File(NodeController.getImageCache()).mkdirs();
		}
	}
	
	public void initProperties() {
		this.properties = NodeController.openProperties("./retina.properties");
	}
	
	public void initProviders() {
		this.providerList.add(new Zooniverse());
	}
	
	public void initVendors() {
		this.vendorList.add(new Microsoft());
	}
	
	public void initUsers() {
		logger.info("initUsers()");
		UserController userController = new UserController(this);
		if(userController.exists()) {
			this.userList = userController.open();
		}
		if (this.userList.size() == 0) {
			this.newUser = true;
			User user = new User();
			user.name = "anonymous";
			user.fullName = "anonymous";
			user.hash = SecurityController.hash("anonymous", 11);
			user.email = "null";
			this.userList.add(user);
			userController.save(this.userList);
		} else {
			this.loginUser = true;
		}
	}
}
