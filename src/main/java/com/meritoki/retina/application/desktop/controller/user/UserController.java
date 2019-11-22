package com.meritoki.retina.application.desktop.controller.user;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.type.TypeReference;

import com.meritoki.retina.application.desktop.controller.node.NodeController;
import com.meritoki.retina.application.desktop.model.User;

/**
 * User controller is responsible for loading the users 
 * registered with the desktop application.
 * At a minimum there is a default anonymous user.
 * 
 * @author jorodriguez
 *
 */
public class UserController {
	
	public static void main(String[] args) {
		List<User> userList = new ArrayList<>();
		userList.add(new User());
		UserController.save(userList);
		userList = UserController.open();
		for(User u: userList) {
			System.out.println(u);
		}
	}
	
	private static Logger logger = LogManager.getLogger(UserController.class.getName());
	public static String filePath = NodeController.getRetinaHome()+NodeController.getSeperator();
	public static String fileName = "users.json";
	
	public static void save(Object object) {
		logger.info("save()");
		NodeController.saveJson(filePath, fileName, object);
	}
	
	public static boolean exists() {
		return new File(filePath+fileName).exists();
	}
	
	@JsonIgnore
	public static List<User> open() {
		logger.info("open()");
		List<User> userList = null;
		userList = (List<User>)NodeController.openJson(new java.io.File(filePath+"/"+fileName), new TypeReference<List<User>>(){});
		return userList;
	}
}
