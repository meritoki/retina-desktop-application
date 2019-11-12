package com.meritoki.retina.application.desktop.model;

import java.util.List;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.annotate.JsonIgnore;

import com.meritoki.retina.application.desktop.controller.node.NodeController;
import com.meritoki.retina.application.desktop.controller.security.BCryptController;
import com.meritoki.retina.application.desktop.controller.user.UserController;
import com.meritoki.retina.application.desktop.model.command.AddPage;
import com.meritoki.retina.application.desktop.model.command.AddShape;
import com.meritoki.retina.application.desktop.model.command.Command;
import com.meritoki.retina.application.desktop.model.command.MoveShape;
import com.meritoki.retina.application.desktop.model.command.RemoveShape;
import com.meritoki.retina.application.desktop.model.command.ResizeShape;
import com.meritoki.retina.application.desktop.model.command.SetPage;
import com.meritoki.retina.application.desktop.model.command.SetShape;
import com.meritoki.retina.application.desktop.model.document.Document;
import com.meritoki.retina.application.desktop.model.provider.Provider;
import com.meritoki.retina.application.desktop.model.provider.zooniverse.ZooniverseProvider;
import com.meritoki.retina.application.desktop.model.vendor.Vendor;
import java.util.ArrayList;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Model is a class that is used to maintain the state of the Project for the
 * desktop application locally and remotely The class wraps method calls to set
 * the Project and its attribute. These calls are intercepted by the class to
 * create a command stack. This allows support for Do/Undo/Redo operations. This
 * class periodically, writes itself in json to file.
 * 
 * @author jorodriguez
 *
 */
public class Model {

	private static final Logger logger = LogManager.getLogger(Model.class.getName());
	@JsonIgnore
	public Properties properties = null;
	@JsonIgnore
	public List<User> userList = null;
	@JsonIgnore
	public User user = null;
	@JsonIgnore
	public Document document = null;
	@JsonIgnore
	public Variable variable = null;
	@JsonProperty
	public List<Provider> providerList = new ArrayList<>();
	@JsonProperty
	public List<Vendor> vendorList = new ArrayList<>();

	public Model() {
		this.properties = NodeController.openProperties("./retina-desktop.properties");
		this.userList = UserController.open();
		this.providerList.add(new ZooniverseProvider());
		this.setDocument(new Document());
		this.variable = new Variable();
		if (this.userList.size() == 0) {
			this.variable.newUser = true;
			User user = new User();
			user.name = "anonymous";
			user.fullName = "anonymous";
			user.hash = BCryptController.hash("anonymous", 11);
			user.email = "null";
			this.userList.add(user);
		} else {
			this.variable.loginUser = false;//true;
		}
	}

	public Document getDocument() {
		return this.document;
	}

	public void setDocument(Document document) {
		this.document = document;
		Command addPage = new AddPage(this);
		Command setPage = new SetPage(this);
		Command addShape = new AddShape(this);
		Command setShape = new SetShape(this);
		Command moveShape = new MoveShape(this);
		Command removeShape = new RemoveShape(this);
		Command resizeShape = new ResizeShape(this);
		this.document.register("addPage", addPage);
		this.document.register("setPage", setPage);
		this.document.register("addShape", addShape);
		this.document.register("setShape", setShape);
		this.document.register("moveShape", moveShape);
		this.document.register("removeShape", removeShape);
		this.document.register("resizeShape", resizeShape);
	}

	public boolean loginUser(String userName, String password) {
		boolean flag = false;
		for (User u : userList) {
			if (u.name.equals(userName)) {
				if (BCryptController.verifyHash(password, u.hash)) {
					flag = true;
					this.user = u;
				}
			}
		}
		return flag;
	}

	public void registerUser(User user) {
		this.userList.add(user);
		UserController.save(userList);
	}
}
