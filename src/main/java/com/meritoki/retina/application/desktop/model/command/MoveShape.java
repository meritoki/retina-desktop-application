package com.meritoki.retina.application.desktop.model.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.meritoki.retina.application.desktop.model.Model;


public class MoveShape extends Command {
	private static Logger logger = LogManager.getLogger(MoveShape.class.getName());
	public MoveShape(Model project) {
		this.model = project;
		this.name = "addPage";
	}
	
    @Override // Command
    public void execute() {
    	logger.info("execute()");
    	this.user = this.model.user;
    	this.model.variable.shape.move(this.model.variable.movedPoint);
    }
}
