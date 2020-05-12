package com.meritoki.app.desktop.retina.model.document;

import java.util.LinkedList;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

import com.meritoki.app.desktop.retina.model.User;
import com.meritoki.app.desktop.retina.model.command.Command;

/**
 * State is a class for...
 * @author jorodriguez
 *
 */
public class State {
	@JsonProperty
	public User user;
	@JsonProperty
	public Project project;
	@JsonProperty
	public LinkedList<Command> undoStack = new LinkedList<>();
	@JsonIgnore
	public LinkedList<Command> redoStack = new LinkedList<>();
	@JsonProperty
	public int index = 0;
}
