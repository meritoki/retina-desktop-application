package com.meritoki.app.desktop.retina.model.document.command;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

import com.meritoki.app.desktop.retina.model.document.Document;
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
	public State state = new State();
	@JsonIgnore
	private final HashMap<String, Command> commandMap = new HashMap<>();
	
	public Pattern() {
		//Default constructor for loading from JSON;
	}
	
	public Pattern(Document document) {
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
		this.register("addPage", addPage);
		this.register("setPage", setPage);
		this.register("addShape", addShape);
		this.register("setShape", setShape);
		this.register("moveShape", moveShape);
		this.register("removeShape", removeShape);
		this.register("resizeShape", resizeShape);
		this.register("executeScript", executeScript);
		this.register("removePage", removePage);
	}

	@JsonIgnore
	public void register(String commandName, Command command) {
		commandMap.put(commandName, command);
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
		logger.info(newCommand.operationList.size());
		this.state.undoStack.push(newCommand);
		command.reset();
	}
	
	@JsonIgnore
	public void undo() {
		if (this.state.undoStack.size() > 0) {
			Command command = this.state.undoStack.pop();
			logger.info("undo() command.name=" + command.name);
			Operation operation = null;
			switch (command.name) {
			case "setShape": {
				for (int i = 0; i < command.operationList.size(); i++) {
					operation = command.operationList.get(i);
					if (operation.sign == 0) {
						if (operation.object instanceof Shape) {
							this.document.getPage().getImage().setShape(((Shape) operation.object).uuid);
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
						if(o.object instanceof List) {
							this.document.addPage((Page) o.object);
						}
					}
				}
				break;
			}
			case "setPage":{
				for(Operation o: command.operationList) {
					if(o.sign == 0) {
						if(o.object instanceof Integer) {
							this.document.setIndex((int)o.object);
						}
					}
				}
			}
			default: {

			}
			}
			this.state.redoStack.push(command);
		}
	}

	@JsonIgnore
	public void redo() {
		if (this.state.redoStack.size() > 0) {
			Command command = this.state.redoStack.pop();
			Operation operation = null;
			switch (command.name) {
			case "setShape": {
				for (int i = 0; i < command.operationList.size(); i++) {
					operation = command.operationList.get(i);
					if (operation.sign == 1) {
						if (operation.object instanceof Shape) {
							this.document.getPage().getImage().setShape(((Shape) operation.object).uuid);
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
			default: {

			}
			}
			this.state.undoStack.push(command);
		}
	}
}
