package com.meritoki.app.desktop.retina.model.system;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

import com.meritoki.app.desktop.retina.controller.node.NodeController;
import com.meritoki.app.desktop.retina.controller.security.BCryptController;
import com.meritoki.app.desktop.retina.controller.user.UserController;
import com.meritoki.app.desktop.retina.model.document.user.User;
import com.meritoki.app.desktop.retina.model.provider.Provider;
import com.meritoki.app.desktop.retina.model.provider.zooniverse.Zooniverse;
import com.meritoki.app.desktop.retina.model.vendor.Vendor;

public class System {
	@JsonProperty
	public List<Provider> providerList = new ArrayList<>();
	@JsonProperty
	public List<Vendor> vendorList = new ArrayList<>();
	@JsonIgnore
	public Properties properties = null;
	@JsonIgnore
	public List<User> userList = new ArrayList<User>();
	@JsonIgnore
	public User user = null;
	@JsonIgnore
	public File file = null;
	@JsonIgnore
	public boolean newUser = false;
	@JsonIgnore
	public boolean loginUser = false;
	@JsonIgnore
	public boolean newDocument = true;

	public System() {
		if(!new File(NodeController.getRetinaHome()).exists()) {
			new File(NodeController.getRetinaHome()).mkdirs();
		}
		if(!new File(NodeController.getImageCache()).exists()) {
			new File(NodeController.getImageCache()).mkdirs();
		}
		this.properties = NodeController.openProperties("./retina-desktop.properties");
		if(UserController.exists()) {
			this.userList = UserController.open();
		}
		this.providerList.add(new Zooniverse());
		if (this.userList.size() == 0) {
			this.newUser = true;
			User user = new User();
			user.name = "anonymous";
			user.fullName = "anonymous";
			user.hash = BCryptController.hash("anonymous", 11);
			user.email = "null";
			this.userList.add(user);
			UserController.save(this.userList);
		} else {
			this.loginUser = false;
		}
	}
	
	public void initProviders() {
		this.providerList.add(new Zooniverse());
	}
}
