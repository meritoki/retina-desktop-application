package com.meritoki.retina.application.desktop.model;

import java.util.LinkedList;

import com.meritoki.retina.application.desktop.model.project.Project;
import com.meritoki.retina.application.desktop.model.system.Command;

public class Model {
    public LinkedList<Command> undoStack = new LinkedList<>();
    public LinkedList<Command> redoStack = new LinkedList<>();
	public Project project;
}
