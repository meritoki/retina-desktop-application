package com.meritoki.app.desktop.retina.model.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.meritoki.app.desktop.retina.model.Model;

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
    	this.model.getDocument().getProject().getPage().setShape(this.model.variable.pressedShape.uuid);
    }
}
