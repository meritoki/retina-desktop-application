package com.meritoki.retina.application.desktop.controller.thread;

import com.meritoki.retina.application.desktop.model.Model;

/** 
 * Class iterates over shapes in Project to train the vision web service.
 * @author osvaldo.rodriguez
 *
 */
public class Shape implements Runnable {

	boolean flag = true;
	public Model model = null;
	
	public Shape(Model model) {
		this.model = model;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(flag) {
		}
	}

}
