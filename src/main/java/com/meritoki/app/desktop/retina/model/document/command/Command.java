package com.meritoki.app.desktop.retina.model.document.command;

import java.util.LinkedList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

import com.meritoki.app.desktop.retina.model.Model;
import com.meritoki.app.desktop.retina.model.document.Document;
import com.meritoki.app.desktop.retina.model.document.user.User;

public class Command implements CommandInterface {
	private static Logger logger = LogManager.getLogger(Command.class.getName());
	@JsonIgnore
	public Document document;
	@JsonProperty
	public String name;
	@JsonProperty
	public User user;
	@JsonProperty
    public LinkedList<Operation> operationList = new LinkedList<>();

	public Command(Document document, String name) {
		this.document = document;
		this.name = name;
	}
	
	@Override
	public void execute() {
		
	}
	
	public void reset() {
		this.operationList = new LinkedList<>();
	}
}
