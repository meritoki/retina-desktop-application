package com.meritoki.app.desktop.retina.model.command;

import java.util.LinkedList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

import com.meritoki.app.desktop.retina.model.Model;
import com.meritoki.app.desktop.retina.model.User;
import com.meritoki.app.desktop.retina.model.document.Operation;

public class Command implements CommandInterface {
	private static Logger logger = LogManager.getLogger(Command.class.getName());
	@JsonIgnore
	public Model model;
	@JsonProperty
	public String name;
	@JsonProperty
	public User user;
	@JsonProperty
    public LinkedList<Operation> operationList = new LinkedList<>();
	
	@Override
	public void execute() {
		
	}
	
	public void reset() {
		this.operationList = new LinkedList<>();
	}
}
