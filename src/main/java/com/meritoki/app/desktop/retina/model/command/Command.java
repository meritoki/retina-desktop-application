package com.meritoki.app.desktop.retina.model.command;

import java.util.Date;
import java.util.LinkedList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.meritoki.app.desktop.retina.controller.client.ClientController;
import com.meritoki.app.desktop.retina.model.Model;
import com.meritoki.app.desktop.retina.model.document.Document;
import com.meritoki.app.desktop.retina.model.document.user.User;

public class Command implements CommandInterface {
	private static Logger logger = LogManager.getLogger(Command.class.getName());
	@JsonIgnore
	public Model model;
	@JsonProperty
	public Date date;
	@JsonProperty
	public String name;
	@JsonProperty
	public User user;
	@JsonIgnore
    public LinkedList<Operation> operationList = new LinkedList<>();
	
	public Command() {
	}

	public Command(Model model, String name) {
		this.model = model;
		this.date = new Date();
		this.name = name;
	}
	
	@Override
	public void execute() throws Exception {
	}
	
	public void reset() {
		this.operationList = new LinkedList<>();
	}
	
	
}
