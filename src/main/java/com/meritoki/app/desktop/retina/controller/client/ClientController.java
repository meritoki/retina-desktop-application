package com.meritoki.app.desktop.retina.controller.client;

import com.meritoki.app.desktop.retina.controller.Controller;
import com.meritoki.app.desktop.retina.model.Model;
import com.meritoki.app.desktop.retina.model.system.System;

public class ClientController extends Controller {
	
	public ModelClient modelClient;
	public FileClient fileClient;
	public UserClient userClient;
	public VisionClient visionClient;
	
	public ClientController(Model system) {
		super(system);
		this.modelClient = new ModelClient(this.model);
		this.fileClient = new FileClient(this.model);
		this.userClient = new UserClient(this.model);
		this.visionClient = new VisionClient(this.model);
	}
}
