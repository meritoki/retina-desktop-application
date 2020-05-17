package com.meritoki.app.desktop.retina.model.document.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.meritoki.app.desktop.retina.model.document.Document;

public class SetPage extends Command {
	
	private static Logger logger = LogManager.getLogger(SetPage.class.getName());
	
	public SetPage(Document document) {
		super(document, "setShape");
	}
	
    @Override // Command
    public void execute() {
    	logger.info("execute()");
    	this.user = this.document.state.user;
    	this.document.getPage().setShape(this.document.state.pressedShape.uuid);
    }
}
