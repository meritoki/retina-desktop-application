package com.meritoki.app.desktop.retina.model.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.meritoki.app.desktop.retina.model.Model;

public class MoveSelector extends Command {
	private static Logger logger = LogManager.getLogger(MoveSelector.class.getName());

	public MoveSelector(Model document) {
		super(document, "moveSelector");
	}
	
	@Override
	public void execute() throws Exception {
		
	}
}
