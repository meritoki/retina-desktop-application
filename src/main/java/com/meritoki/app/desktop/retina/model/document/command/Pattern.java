package com.meritoki.app.desktop.retina.model.document.command;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.meritoki.app.desktop.retina.model.document.Document;
import com.meritoki.app.desktop.retina.model.document.Image;
import com.meritoki.app.desktop.retina.model.document.Page;
import com.meritoki.app.desktop.retina.model.document.Shape;
import com.meritoki.app.desktop.retina.model.document.user.User;

public class Pattern {

	@JsonIgnore
	static Logger logger = LogManager.getLogger(Pattern.class.getName());
	@JsonIgnore
	public Document document;
	@JsonIgnore
	public User user;
	@JsonProperty
	public LinkedList<Command> logStack = new LinkedList<>();
	@JsonIgnore
	public LinkedList<Command> undoStack = new LinkedList<>();
	@JsonIgnore
	public LinkedList<Command> redoStack = new LinkedList<>();
	@JsonIgnore
	private final HashMap<String, Command> commandMap = new HashMap<>();
	
	public Pattern() {
		
	}
	
	public void save() {
		this.logStack.addAll(0, this.undoStack);
		this.undoStack = new LinkedList<>();
	}
	
	public void setDocument(Document document) {
		logger.info("setDocument("+document+")");
		this.document = document;
		this.register();
	}
	
	@JsonIgnore
	public void register() {
		Command addPage = new AddPage(this.document);
		Command setPage = new SetPage(this.document);
		Command addShape = new AddShape(this.document);
		Command setShape = new SetShape(this.document);
		Command moveShape = new MoveShape(this.document);
		Command removeShape = new RemoveShape(this.document);
		Command resizeShape = new ResizeShape(this.document);
		Command executeScript = new ExecuteScript(this.document);
		Command removePage = new RemovePage(this.document);
		Command resizeImage = new ResizeImage(this.document);
		Command scalePage = new ScalePage(this.document);
		Command shiftImage = new ShiftImage(this.document);
		Command setImage = new SetImage(this.document);
		Command removeImage = new RemoveImage(this.document);
		Command setGrid = new SetGrid(this.document);
		Command setGridShape = new SetGridShape(this.document);
		this.register("addPage", addPage);
		this.register("setPage", setPage);
		this.register("addShape", addShape);
		this.register("setShape", setShape);
		this.register("moveShape", moveShape);
		this.register("removeShape", removeShape);
		this.register("resizeShape", resizeShape);
		this.register("executeScript", executeScript);
		this.register("removePage", removePage);
		this.register("resizeImage", resizeImage);
		this.register("scalePage", scalePage);
		this.register("shiftImage", shiftImage);
		this.register("setImage", setImage);
		this.register("removeImage", removeImage);
		this.register("setGrid", setGrid);
		this.register("setGridShape", setGridShape);
	}

	@JsonIgnore
	public void register(String commandName, Command command) {
		this.commandMap.put(commandName, command);
	}

	@JsonIgnore
	public void execute(String commandName) throws Exception {
		Command command = commandMap.get(commandName);
		if (command == null) {
			throw new IllegalStateException("no command registered for " + commandName);
		}
		command.execute();
		Command newCommand = new Command(this.document, command.name);
		newCommand.user = this.user;
		newCommand.operationList = command.operationList;
		this.undoStack.push(newCommand);
		this.redoStack = new LinkedList<>();
		command.reset();
	}
	
	@JsonIgnore
	public void undo() {
		if (this.undoStack.size() > 0) {
			Command command = this.undoStack.pop();
			logger.info("undo() command.name=" + command.name);
			Operation operation = null;
			switch (command.name) {
			case "addPage": {
				for (int i = 0; i < command.operationList.size(); i++) {
					operation = command.operationList.get(i);
					if (operation.sign == 0) {
						if (operation.object instanceof List) {
							this.document.pageList = (List<Page>)operation.object;
						}
					}
				}
				break;
			}
			case "setShape": {
				for (int i = 0; i < command.operationList.size(); i++) {
					operation = command.operationList.get(i);
					if (operation.sign == 0) {
						if (operation.object instanceof String) {
							this.document.getPage().setShape((String) operation.object);
						}
					}
				}
				break;
			}
			case "addShape": {
				for (int i = 0; i < command.operationList.size(); i++) {
					operation = command.operationList.get(i);
					if (operation.sign == 1) {
						if (operation.object instanceof Shape) {
							this.document.getPage().removeShape((Shape) operation.object);
						}
					} else if (operation.sign == 0) {
						if (operation.object instanceof Shape) {
							this.document.getPage().getImage().addShape((Shape) operation.object);
						}
					}
				}
				break;
			}
			case "moveShape": {
				for (int i = 0; i < command.operationList.size(); i++) {
					operation = command.operationList.get(i);
					if (operation.sign == 1) {
						if (operation.object instanceof Shape) {
							this.document.getPage().removeShape((Shape) operation.object);
						}
					} else if (operation.sign == 0) {
						if (operation.object instanceof Shape) {
							this.document.getPage().getImage().addShape((Shape) operation.object);
						}
					}
				}
				break;
			}
			case "resizeShape": {
				for (int i = 0; i < command.operationList.size(); i++) {
					operation = command.operationList.get(i);
					if (operation.sign == 1) {
						if (operation.object instanceof Shape) {
							this.document.getPage().removeShape((Shape) operation.object);
						}
					} else if (operation.sign == 0) {
						if (operation.object instanceof Shape) {
							this.document.getPage().getImage().addShape((Shape) operation.object);
						}
					}
				}
				break;
			}
			case "removeShape": {
				for (int i = 0; i < command.operationList.size(); i++) {
					operation = command.operationList.get(i);
					if (operation.sign == 1) {
						if (operation.object instanceof Shape) {
							this.document.getPage().removeShape((Shape) operation.object);
						}
					} else if (operation.sign == 0) {
						if (operation.object instanceof Shape) {
							this.document.getPage().getImage().addShape((Shape) operation.object);
						}
					}
				}
				break;
			}
			case "executeScript" : {
				logger.info("undo() executeScript command.operationList.size()="+command.operationList.size());
				Collections.reverse(command.operationList);
				for(Operation o: command.operationList) {
					if(o.sign == 0) {
						if(o.object instanceof List) {
							this.document.pageList = (List<Page>)o.object;
						}
					}
				}
				break;
			}
			case "removePage": {
				for(Operation o: command.operationList) {
					if(o.sign == 0) {
						if(o.object instanceof Object[]) {
							Object[] objectArray = (Object[])o.object;
							int index = (int)objectArray[0];
							Page image = (Page)objectArray[1];
							this.document.pageList.add(index, image);
						}
					}
				}
				break;
			}
			case "setPage":{
				for(Operation o: command.operationList) {
					if(o.sign == 0) {
						if(o.object instanceof String) {
							this.document.setPage((String)o.object);
						}
					}
				}
				break;
			}
			case "scalePage":{
				for(Operation o: command.operationList) {
					if(o.sign == 0) {
						if(o.object instanceof Double) {
							this.document.getPage().setScale((double)o.object);
						}
					}
				}
				break;
			}
			case "setImage": {
				for(Operation o: command.operationList) {
					if(o.sign == 0) {
						if(o.object instanceof String) {
							this.document.setImage((String)o.object);
						}
					}
				}
				break;
			}
			case "resizeImage":{
				for(Operation o: command.operationList) {
					if(o.sign == 0) {
						if(o.object instanceof Double) {
							this.document.getPage().setBufferedImage(null);
							this.document.getImage().setBufferedImage(null);
							this.document.getImage().setRelativeScale((double)o.object);
						}
					}
				}
				break;
			}
			case "shiftImage": {
				for(Operation o: command.operationList) {
					if(o.sign == 0) {
						if(o.object instanceof Double) {
							this.document.getPage().setBufferedImage(null);
							this.document.getPage().getImage().setMargin((double)o.object);
						}
					}
				}
				break;
			}
			case "removeImage": {
				for(Operation o: command.operationList) {
					if(o.sign == 0) {
						if(o.object instanceof Object[]) {
							Object[] objectArray = (Object[])o.object;
							int index = (int)objectArray[0];
							Image image = (Image)objectArray[1];
							this.document.getPage().setBufferedImage(null);
							this.document.getPage().imageList.add(index, image);
						}
					}
				}
			}
			default: {
				logger.error("undo() default");
			}
			}
			this.redoStack.push(command);
		}
	}

	@JsonIgnore
	public void redo() {
		if (this.redoStack.size() > 0) {
			Command command = this.redoStack.pop();
			logger.info("redo() command.name=" + command.name);
			Operation operation = null;
			switch (command.name) {
			case "addPage": {
				for (int i = 0; i < command.operationList.size(); i++) {
					operation = command.operationList.get(i);
					if (operation.sign == 1) {
						if (operation.object instanceof List) {
							this.document.pageList = (List<Page>)operation.object;
						}
					}
				}
				break;
			}
			case "setShape": {
				for (int i = 0; i < command.operationList.size(); i++) {
					operation = command.operationList.get(i);
					if (operation.sign == 1) {
						if (operation.object instanceof String) {
							this.document.getPage().setShape((String) operation.object);
						}
					}
				}
				break;
			}
			case "addShape": {
				for (int i = command.operationList.size() - 1; i >= 0; i--) {
					operation = command.operationList.get(i);
					if (operation.sign == 1) {
						if (operation.object instanceof Shape) {
							this.document.getPage().getImage().addShape((Shape) operation.object);
						}
					} else if (operation.sign == 0) {
						if (operation.object instanceof Shape) {
							this.document.getPage().getImage().removeShape((Shape) operation.object);
						}
					}
				}
				break;
			}
			case "moveShape": {
				for (int i = 0; i < command.operationList.size(); i++) {
					operation = command.operationList.get(i);
					if (operation.sign == 1) {
						if (operation.object instanceof Shape) {
							this.document.getPage().getImage().addShape((Shape) operation.object);
						}
					} else if (operation.sign == 0) {
						if (operation.object instanceof Shape) {
							this.document.getPage().getImage().removeShape((Shape) operation.object);
						}
					}
				}
				break;
			}
			//Does not function
			case "removePage": {
				for(Operation o: command.operationList) {
					if(o.sign == 1) {
						if(o.object instanceof String) {
							this.document.removePage((String)o.object);
						}
					}
				}
				break;
			}
			case "resizeShape": {
				for (int i = 0; i < command.operationList.size(); i++) {
					operation = command.operationList.get(i);
					if (operation.sign == 1) {
						if (operation.object instanceof Shape) {
							this.document.getPage().getImage().addShape((Shape) operation.object);
						}
					} else if (operation.sign == 0) {
						if (operation.object instanceof Shape) {
							this.document.getPage().getImage().removeShape((Shape) operation.object);
						}
					}
				}
				break;
			}
			case "removeShape": {
				for (int i = 0; i < command.operationList.size(); i++) {
					operation = command.operationList.get(i);
					if (operation.sign == 1) {
						if (operation.object instanceof Shape) {
							this.document.getPage().getImage().addShape((Shape) operation.object);
						}
					} else if (operation.sign == 0) {
						if (operation.object instanceof Shape) {
							this.document.getPage().getImage().removeShape((Shape) operation.object);
						}
					}
				}
				break;
			}
			case "executeScript" : {
				Collections.reverse(command.operationList);
				for(Operation o: command.operationList) {
					if(o.sign == 1) {
						if(o.object instanceof List) {
							this.document.pageList = (List<Page>)o.object;
						}
					} 
				}
				break;
			}
			case "setImage": {
				for(Operation o: command.operationList) {
					if(o.sign == 1) {
						if(o.object instanceof String) {
							this.document.setImage((String)o.object);
						}
					}
				}
				break;
			}
			case "resizeImage":{
				for(Operation o: command.operationList) {
					if(o.sign == 1) {
						if(o.object instanceof Double) {
							this.document.getPage().setBufferedImage(null);
							this.document.getImage().setBufferedImage(null);
							this.document.getImage().setRelativeScale((double)o.object);
						}
					}
				}
				break;
			}
			case "scalePage":{
				for(Operation o: command.operationList) {
					if(o.sign == 1) {
						if(o.object instanceof Double) {
							this.document.getPage().setScale((double)o.object);
						}
					}
				}
				break;
			}
			case "setPage":{
				for(Operation o: command.operationList) {
					if(o.sign == 1) {
						if(o.object instanceof String) {
							this.document.setPage((String)o.object);
						}
					}
				}
				break;
			}
			case "shiftImage": {
				for(Operation o: command.operationList) {
					if(o.sign == 1) {
						if(o.object instanceof Double) {
							this.document.getPage().setBufferedImage(null);
							this.document.getPage().getImage().setMargin((double)o.object);
						}
					}
				}
				break;
			}
			case "removeImage": {
				for(Operation o: command.operationList) {
					if(o.sign == 1) {
						if(o.object instanceof String) {
							this.document.getPage().setBufferedImage(null);
							this.document.getPage().removeImage((String)o.object);
						}
					}
				}
				break;
			}
			default: {

			}
			}
			this.undoStack.push(command);
		}
	}
}
