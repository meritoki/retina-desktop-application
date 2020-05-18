package com.meritoki.app.desktop.retina.model.document;

import java.util.LinkedList;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

import com.meritoki.app.desktop.retina.model.document.command.Command;
import com.meritoki.app.desktop.retina.model.document.user.User;

/**
 * State is a class for...
 * @author jorodriguez
 *
 */
public class Event {
	@JsonProperty
	public User user;
	@JsonProperty
	public LinkedList<Command> undoStack = new LinkedList<>();
	@JsonIgnore
	public LinkedList<Command> redoStack = new LinkedList<>();
	@JsonProperty
	public int index = 0;
	
	public Event() {
		
	}
}
