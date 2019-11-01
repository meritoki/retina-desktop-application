package com.meritoki.retina.application.desktop.model.document;

import java.util.LinkedList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * State is a class for...
 * @author jorodriguez
 *
 */
public class State {
	@JsonProperty
	public Project project;
	@JsonProperty
	public LinkedList<Command> undoStack = new LinkedList<>();
	@JsonProperty
	public LinkedList<Command> redoStack = new LinkedList<>();
}
