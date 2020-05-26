package com.meritoki.app.desktop.retina.controller.client;

import com.meritoki.app.desktop.retina.controller.Controller;
import com.meritoki.app.desktop.retina.model.system.System;

public class ClientController extends Controller {
	
	public ModelClient modelClient;
	public FileClient fileClient;
	public UserClient userClient;
	public VisionClient visionClient;
	
	public ClientController(System system) {
		super(system);
		this.modelClient = new ModelClient(this.system);
		this.fileClient = new FileClient(this.system);
		this.userClient = new UserClient(this.system);
		this.visionClient = new VisionClient(this.system);
	}
}
