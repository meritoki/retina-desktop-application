package com.meritoki.app.desktop.retina.controller.module;

import com.meritoki.app.desktop.retina.controller.Controller;
import com.meritoki.app.desktop.retina.model.Model;
import com.meritoki.app.desktop.retina.model.module.Recognition;

public class ModuleController extends Controller {
	
	public static void main(String[] args) {
		ModuleController moduleController = new ModuleController(new Model());
		moduleController.start();
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		moduleController.stop();
	}
	
	Recognition recognition = new Recognition(0);
	

	public ModuleController(Model model) {
		super(model);
	}
	
	public void start() {
		recognition.start();
	}
	
	public void stop() {
		recognition.destroy();
	}
}
