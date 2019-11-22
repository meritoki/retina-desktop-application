package com.meritoki.retina.application.desktop.controller.client;

import java.util.Properties;

public class ClientController {
	
	public ClientController(Properties properties) {
		
	}
	
	public static ModelClient modelClient = new ModelClient();
	public static FileClient fileClient = new FileClient();
	public static UserClient userClient = new UserClient();
}
