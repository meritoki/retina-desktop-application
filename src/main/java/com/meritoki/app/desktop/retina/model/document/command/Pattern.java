package com.meritoki.app.desktop.retina.model.document.command;

import java.util.HashMap;
import java.util.LinkedList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

import com.meritoki.app.desktop.retina.model.document.Document;
import com.meritoki.app.desktop.retina.model.document.Event;
import com.meritoki.app.desktop.retina.model.document.Shape;

public class Pattern {

	@JsonIgnore
	static Logger logger = LogManager.getLogger(Pattern.class.getName());
	@JsonIgnore
	public Document document;
	@JsonProperty
	public Event event = new Event();
	@JsonProperty
	public LinkedList<Event> eventStack = new LinkedList<>();
	@JsonIgnore
	private final HashMap<String, Command> commandMap = new HashMap<>();
	
	public Pattern(Document document) {
		this.document = document;
		this.register();
	}
	
	public void register() {
		Command addPage = new AddPage(this.document);
		Command setPage = new SetPage(this.document);
		Command addShape = new AddShape(this.document);
		Command setShape = new SetShape(this.document);
		Command moveShape = new MoveShape(this.document);
		Command removeShape = new RemoveShape(this.document);
		Command resizeShape = new ResizeShape(this.document);
		this.register("addPage", addPage);
		this.register("setPage", setPage);
		this.register("addShape", addShape);
		this.register("setShape", setShape);
		this.register("moveShape", moveShape);
		this.register("removeShape", removeShape);
		this.register("resizeShape", resizeShape);
	}

	@JsonIgnore
	public void register(String commandName, Command command) {
		commandMap.put(commandName, command);
	}

	@JsonIgnore
	public void execute(String commandName) {
		Command command = commandMap.get(commandName);
		if (command == null) {
			throw new IllegalStateException("no command registered for " + commandName);
		}
		command.execute();
		Command newCommand = new Command(this.document, command.name);
		newCommand.user = command.user;
		newCommand.operationList = command.operationList;
		this.event.undoStack.push(newCommand);
		command.reset();
	}
	
	public void undo() {
		if (this.event.undoStack.size() > 0) {
			Command command = this.event.undoStack.pop();
			logger.info("undo() command.name=" + command.name);
			Operation operation = null;
			switch (command.name) {
			case "setShape": {
				for (int i = 0; i < command.operationList.size(); i++) {
					operation = command.operationList.get(i);
					if (operation.sign == 0) {
						if (operation.object instanceof Shape) {
							this.document.getPage().getFile().setShape(((Shape) operation.object).uuid);
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
							this.document.getPage().getFile().addShape((Shape) operation.object);
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
							this.document.getPage().getFile().addShape((Shape) operation.object);
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
							this.document.getPage().getFile().addShape((Shape) operation.object);
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
							this.document.getPage().getFile().addShape((Shape) operation.object);
						}
					}
				}
				break;
			}
			default: {

			}
			}
			this.event.redoStack.push(command);
		}
	}

	public void redo() {
		if (this.event.redoStack.size() > 0) {
			Command command = this.event.redoStack.pop();
			Operation operation = null;
			switch (command.name) {
			case "setShape": {
				for (int i = 0; i < command.operationList.size(); i++) {
					operation = command.operationList.get(i);
					if (operation.sign == 1) {
						if (operation.object instanceof Shape) {
							this.document.getPage().getFile().setShape(((Shape) operation.object).uuid);
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
							this.document.getPage().getFile().addShape((Shape) operation.object);
						}
					} else if (operation.sign == 0) {
						if (operation.object instanceof Shape) {
							this.document.getPage().getFile().removeShape((Shape) operation.object);
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
							this.document.getPage().getFile().addShape((Shape) operation.object);
						}
					} else if (operation.sign == 0) {
						if (operation.object instanceof Shape) {
							this.document.getPage().getFile().removeShape((Shape) operation.object);
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
							this.document.getPage().getFile().addShape((Shape) operation.object);
						}
					} else if (operation.sign == 0) {
						if (operation.object instanceof Shape) {
							this.document.getPage().getFile().removeShape((Shape) operation.object);
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
							this.document.getPage().getFile().addShape((Shape) operation.object);
						}
					} else if (operation.sign == 0) {
						if (operation.object instanceof Shape) {
							this.document.getPage().getFile().removeShape((Shape) operation.object);
						}
					}
				}
				break;
			}
			default: {

			}
			}
			this.event.undoStack.push(command);
		}
	}
}
