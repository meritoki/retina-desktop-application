package com.meritoki.app.desktop.retina.controller.client;

import com.meritoki.app.desktop.retina.controller.Controller;
import com.meritoki.app.desktop.retina.model.Model;
import com.meritoki.app.desktop.retina.model.system.System;

public class ClientController extends Controller {
	
	private Model model;
	public RetinaClient retinaClient;
	public FileClient fileClient;
	public UserClient userClient;
	
	public ClientController(Model model) {
		this.model = model;
		this.retinaClient = new RetinaClient(this.model);
		this.fileClient = new FileClient(this.model);
		this.userClient = new UserClient(this.model);
	}
}
