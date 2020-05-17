package com.meritoki.app.desktop.retina.controller.thread;

import com.meritoki.app.desktop.retina.model.ModelPrototype;

/** 
 * Class iterates over shapes in Project to train the vision web service.
 * @author osvaldo.rodriguez
 *
 */
public class Shape implements Runnable {

	boolean flag = true;
	public ModelPrototype model = null;
	
	public Shape(ModelPrototype model) {
		this.model = model;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(flag) {
		}
	}

}
