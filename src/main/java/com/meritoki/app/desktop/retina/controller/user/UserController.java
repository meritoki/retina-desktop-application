package com.meritoki.app.desktop.retina.controller.user;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.type.TypeReference;
import com.meritoki.app.desktop.retina.controller.Controller;
import com.meritoki.app.desktop.retina.controller.client.ClientController;
import com.meritoki.app.desktop.retina.controller.node.NodeController;
import com.meritoki.app.desktop.retina.controller.security.SecurityController;
import com.meritoki.app.desktop.retina.model.system.System;
import com.meritoki.app.desktop.retina.model.Model;
import com.meritoki.app.desktop.retina.model.document.user.User;

/**
 * User controller is responsible for loading the users registered with the
 * desktop application. At a minimum there is a default anonymous user.
 * 
 * @author jorodriguez
 *
 */
public class UserController extends Controller {

//	public static void main(String[] args) {
//		List<User> userList = new ArrayList<>();
//		userList.add(new User());
//		UserController.save(userList);
//		userList = UserController.open();
//		for(User u: userList) {
//			System.out.println(u);
//		}
//	}

	private static Logger logger = LogManager.getLogger(UserController.class.getName());
	public static String filePath = NodeController.getRetinaHome() + NodeController.getSeperator();
	public static String fileName = "users.json";
	private System system;

	public UserController(System system) {
		this.system = system;
	}

	public static void save(Object object) {
		logger.info("save()");
		NodeController.saveJson(filePath, fileName, object);
	}

	public static boolean exists() {
		return new File(filePath + fileName).exists();
	}

	@JsonIgnore
	public static List<User> open() {
		logger.info("open()");
		List<User> userList = null;
		userList = (List<User>) NodeController.openJson(new java.io.File(filePath + "/" + fileName), new TypeReference<List<User>>() {});
		return userList;
	}

	public boolean loginUser(String userName, String password) {
		logger.info("loginUser(" + userName + ", " + password + ")");
		boolean flag = false;
		for (User user : this.system.userList) {
			if (user.name.equals(userName)) {
				if (SecurityController.verifyHash(password, user.hash)) {
					flag = true;
					this.system.user = user;
				}
			}
		}
		return flag;
	}

	public void registerUser(User user) {
		this.system.userList.add(user);
		UserController.save(this.system.userList);
	}
	
	public static User getAnonymousUser() {
		User user = new User();
		user.name = "anonymous";
		user.fullName = "anonymous";
		user.hash = SecurityController.hash("anonymous", 11);
		user.email = "null";
		return user;
	}
}

//public boolean loginUser(String userName, String password) {
//	logger.info("loginUser("+userName+", "+password+")");
//	boolean flag = false;
//	ClientController clientController = new ClientController(this.system);
//	if(clientController.userClient.checkHealth()) {
//		User user = new User(userName, password);
//		flag = clientController.userClient.login(user);
//	} else {
//		for (User u : system.userList) {
//			logger.info(u.name);
//			if (u.name.equals(userName)) {
//				if (SecurityController.verifyHash(password, u.hash)) {
//					flag = true;
//					this.system.user = u;
//				}
//			}
//		}
//	}
//	return flag;
//}
