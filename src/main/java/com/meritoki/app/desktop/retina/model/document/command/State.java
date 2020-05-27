package com.meritoki.app.desktop.retina.model.document.command;

import java.util.LinkedList;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;


/**
 * State is a class for...
 * @author jorodriguez
 *
 */
public class State {
	@JsonProperty
	public LinkedList<Command> undoStack = new LinkedList<>();
	@JsonIgnore
	public LinkedList<Command> redoStack = new LinkedList<>();
	@JsonProperty
	public int index = 0;
	
	public State() {
		
	}
}
