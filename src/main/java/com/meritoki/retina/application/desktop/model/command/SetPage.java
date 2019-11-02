package com.meritoki.retina.application.desktop.model.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.meritoki.retina.application.desktop.model.Model;

public class SetPage extends Command {
	private static Logger logger = LogManager.getLogger(SetPage.class.getName());
	public SetPage(Model project) {
		this.model = project;
		this.name = "setShape";
	}
	
    @Override // Command
    public void execute() {
    	logger.info("execute()");
    	this.user = this.model.user;
    	this.model.getDocument().getProject().setShape(this.model.variable.shape.uuid);
    }
}
