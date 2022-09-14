/*
 * Copyright 2021 Joaquin Osvaldo Rodriguez
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.meritoki.app.desktop.retina.model.pattern;

import java.util.LinkedList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.meritoki.app.desktop.retina.controller.client.ClientController;
import com.meritoki.app.desktop.retina.model.Model;
import com.meritoki.app.desktop.retina.model.command.AddGrid;
import com.meritoki.app.desktop.retina.model.command.AddGuide;
import com.meritoki.app.desktop.retina.model.command.AddPage;
import com.meritoki.app.desktop.retina.model.command.AddSelector;
import com.meritoki.app.desktop.retina.model.command.AddShape;
import com.meritoki.app.desktop.retina.model.command.Command;
import com.meritoki.app.desktop.retina.model.command.CreateProject;
import com.meritoki.app.desktop.retina.model.command.MoveSelector;
import com.meritoki.app.desktop.retina.model.command.MoveShape;
import com.meritoki.app.desktop.retina.model.command.OpenProject;
import com.meritoki.app.desktop.retina.model.command.RemoveImage;
import com.meritoki.app.desktop.retina.model.command.RemovePage;
import com.meritoki.app.desktop.retina.model.command.RemoveShape;
import com.meritoki.app.desktop.retina.model.command.ResizeShape;
import com.meritoki.app.desktop.retina.model.command.ScaleImage;
import com.meritoki.app.desktop.retina.model.command.ScalePage;
import com.meritoki.app.desktop.retina.model.command.ScriptPage;
import com.meritoki.app.desktop.retina.model.command.SetGrid;
import com.meritoki.app.desktop.retina.model.command.SetImage;
import com.meritoki.app.desktop.retina.model.command.SetPage;
import com.meritoki.app.desktop.retina.model.command.SetShape;
import com.meritoki.app.desktop.retina.model.command.ShiftImage;

public class Pattern {

	@JsonIgnore
	static Logger logger = LogManager.getLogger(Pattern.class.getName());
	@JsonIgnore
	public Model model;
	@JsonIgnore
	public LinkedList<Command> undoStack = new LinkedList<>();
	@JsonIgnore
	public LinkedList<Command> redoStack = new LinkedList<>();
	@JsonIgnore
	public ClientController clientController;
	
	public Pattern() {}
	
	public void save() {
		this.model.document.logStack.addAll(0, this.undoStack);
		this.undoStack = new LinkedList<>();
	}
	
	public void setModel(Model model) {
		logger.info("setModel("+model+")");
		this.model = model;

		this.clientController = new ClientController(this.model);
	}
	
	public Command getCommand(String name) throws Exception {
		Command command = null;
		switch(name) {
		case "createProject": {
			command = new CreateProject(this.model);
			break;
		}
		case "openProject": {
			command = new OpenProject(this.model);
			break;
		}
		case "addPage": {
			command = new AddPage(this.model);
			break;
		}
		case "addGuide" : {
			command = new AddGuide(this.model);
			break;
		}
		case "addShape": {
			command = new AddShape(this.model);
			break;
		}
		case "addGrid": {
			command = new AddGrid(this.model);
			break;
		}
		case "addSelector": {
			command = new AddSelector(this.model);
			break;
		}
		case "setPage": {
			command = new SetPage(this.model);
			break;
		}
		case "setImage": {
			command = new SetImage(this.model);
			break;
		}
		case "setShape": {
			command = new SetShape(this.model);
			break;
		}
		case "setGrid": {
			command = new SetGrid(this.model);
			break;
		}
		case "scalePage": {
			command = new ScalePage(this.model);
			break;
		}
		case "scaleImage": {
			command = new ScaleImage(this.model);
			break;
		}
		case "resizeShape": {
			command = new ResizeShape(this.model);
			break;
		}
		case "shiftImage": {
			command = new ShiftImage(this.model);
			break;
		}
		case "moveShape": {
			command = new MoveShape(this.model);
			break;
		}
		case "moveSelector": {
			command = new MoveSelector(this.model);
			break;
		}
		case "removePage": {
			command = new RemovePage(this.model);
			break;
		}
		case "removeImage": {
			command = new RemoveImage(this.model);
			break;
		}
		case "removeShape": {
			command = new RemoveShape(this.model);
			break;
		}
		case "executeScript": {
			command = new ScriptPage(this.model);
			break;
		}
		default: {
			 throw new UnsupportedOperationException("Unsupported Operation");
		}
		}
		return command;
	}

	@JsonIgnore
	public void execute(String name) throws Exception {
		Command command = this.getCommand(name);
		if (command == null) {
			throw new IllegalStateException("no command registered for " + name);
		}
		if(this.model.system.multiUser && this.model.system.isConnected) {
			switch(name) {
			case "setPage": {
				command.execute();
				break;
			}
			case "setImage": {
				command.execute();
				break;
			}
			case "setShape": {
				command.execute();
				break;
			}
			case "setGrid": {
				command.execute();
				break;
			}
			default: {
				this.model.system.page = this.model.document.getPage();
				this.model.system.image = this.model.document.getImage();
				this.model.system.shape = this.model.document.getShape();
				this.model.document = this.clientController.retinaClient.postDocumentCommand(this.model.document, command, this.model.cache);
				if(this.model.document.setPage(this.model.system.page.uuid)) {
					if(this.model.document.setImage(this.model.system.image.uuid)) {
						
					} else {
						this.model.document.getPage().setIndex(0);
					}
				} else {
					this.model.document.setIndex(0);
				}
			}
			}
		} else {
			command.execute();
			command.user = this.model.system.user;
			this.undoStack.push(command);
		}
	}
	
	@JsonIgnore
	public void undo() throws Exception {
		if (this.undoStack.size() > 0) {
			Command command = this.undoStack.pop();
			logger.info("undo() command.name=" + command.name);
			command.undo();
			this.redoStack.push(command);
		}
	}

	@JsonIgnore
	public void redo() throws Exception {
		if (this.redoStack.size() > 0) {
			Command command = this.redoStack.pop();
			logger.info("redo() command.name=" + command.name);
			command.redo();
			this.undoStack.push(command);
		}
	}
}
//@JsonIgnore
//private final HashMap<String, Command> commandMap = new HashMap<>();
//this.register();
//Command newCommand = new Command(this.model, command.name);
//newCommand.operationList = command.operationList;
//@JsonIgnore
//public void register() {
//	//Add
//	Command addPage = new AddPage(this.model);
//	AddShape addShape = new AddShape(this.model);
//	Command addGrid = new AddGrid(this.model);
//	//Set
//	Command setPage = new SetPage(this.model);
//	Command setImage = new SetImage(this.model);
//	Command setShape = new SetShape(this.model);
//	Command setGrid = new SetGrid(this.model);
//	//Scale/Resize
//	Command scalePage = new ScalePage(this.model);
//	Command scaleImage = new ScaleImage(this.model);
//	Command resizeShape = new ResizeShape(this.model);
//	//Move/Shift
//	Command shiftImage = new ShiftImage(this.model);
//	Command moveShape = new MoveShape(this.model);
//	//Remove
//	Command removePage = new RemovePage(this.model);
//	Command removeImage = new RemoveImage(this.model);
//	Command removeShape = new RemoveShape(this.model);
//	//Execute
//	Command executeScript = new ScriptPage(this.model);
//	//Add
//	this.register("addPage", addPage);
//	this.register("addShape", addShape);
//	this.register("addGrid", addGrid);
//	//Set
//	this.register("setPage", setPage);
//	this.register("setImage", setImage);
//	this.register("setShape", setShape);
//	this.register("setGrid", setGrid);
//	//Scale/Resize
//	this.register("scalePage", scalePage);
//	this.register("scaleImage", scaleImage);
//	this.register("resizeShape", resizeShape);
//	//Move/Shift
//	this.register("shiftImage", shiftImage);
//	this.register("moveShape", moveShape);
//	//Remove
//	this.register("removePage", removePage);
//	this.register("removeImage", removeImage);
//	this.register("removeShape", removeShape);
//	//Excute
//	this.register("executeScript", executeScript);
//}
//
//@JsonIgnore
//public void register(String commandName, Command command) {
//	this.commandMap.put(commandName, command);
//}
//
