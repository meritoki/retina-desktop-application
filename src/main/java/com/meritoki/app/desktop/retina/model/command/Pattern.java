package com.meritoki.app.desktop.retina.model.command;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.meritoki.app.desktop.retina.model.Model;
import com.meritoki.app.desktop.retina.model.document.Document;
import com.meritoki.app.desktop.retina.model.document.Grid;
import com.meritoki.app.desktop.retina.model.document.Image;
import com.meritoki.app.desktop.retina.model.document.Page;
import com.meritoki.app.desktop.retina.model.document.Shape;
import com.meritoki.app.desktop.retina.model.document.user.User;

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
	private final HashMap<String, Command> commandMap = new HashMap<>();
	
	public Pattern() {
		
	}
	
	public void save() {
		this.model.document.logStack.addAll(0, this.undoStack);
		this.undoStack = new LinkedList<>();
	}
	
	public void setModel(Model model) {
		logger.info("setModel("+model+")");
		this.model = model;
		this.register();
	}
	
	@JsonIgnore
	public void register() {
		Command addPage = new AddPage(this.model);
		Command setPage = new SetPage(this.model);
		Command addShape = new AddShape(this.model);
		Command setShape = new SetShape(this.model);
		Command moveShape = new MoveShape(this.model);
		Command removeShape = new RemoveShape(this.model);
		Command resizeShape = new ResizeShape(this.model);
		Command executeScript = new ExecuteScript(this.model);
		Command removePage = new RemovePage(this.model);
		Command resizeImage = new ResizeImage(this.model);
		Command scalePage = new ScalePage(this.model);
		Command shiftImage = new ShiftImage(this.model);
		Command setImage = new SetImage(this.model);
		Command removeImage = new RemoveImage(this.model);
		Command setGrid = new SetGrid(this.model);
		Command setGridShape = new SetGridShape(this.model);
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
		Command newCommand = new Command(this.model, command.name);
		newCommand.user = this.model.system.user;
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
							this.model.document.pageList = (List<Page>)operation.object;
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
							this.model.document.getPage().setShape((String) operation.object);
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
							this.model.document.getPage().removeShape((Shape) operation.object);
						}
					} else if (operation.sign == 0) {
						if (operation.object instanceof Shape) {
							this.model.document.getPage().getImage().addShape((Shape) operation.object);
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
							Shape shape = (Shape)operation.object;
							this.model.document.getPage().removeShape(shape);
						}
					} else 
					if (operation.sign == 0) {
						if (operation.object instanceof Shape) {
							Shape shape = (Shape)operation.object;
							if(shape instanceof Grid) {
								Grid grid = (Grid) shape;
								grid.updateMatrix();
							}
							this.model.document.getPage().getImage().addShape(shape);
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
							this.model.document.getPage().removeShape((Shape) operation.object);
						}
					} else if (operation.sign == 0) {
						if (operation.object instanceof Shape) {
							this.model.document.getPage().getImage().addShape((Shape) operation.object);
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
							this.model.document.getPage().removeShape((Shape) operation.object);
						}
					} else if (operation.sign == 0) {
						if (operation.object instanceof Shape) {
							this.model.document.getPage().getImage().addShape((Shape) operation.object);
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
							this.model.document.pageList = (List<Page>)o.object;
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
							this.model.document.pageList.add(index, image);
						}
					}
				}
				break;
			}
			case "setPage":{
				for(Operation o: command.operationList) {
					if(o.sign == 0) {
						if(o.object instanceof String) {
							this.model.document.setPage((String)o.object);
						}
					}
				}
				break;
			}
			case "scalePage":{
				for(Operation o: command.operationList) {
					if(o.sign == 0) {
						if(o.object instanceof Double) {
							this.model.document.getPage().setScale((double)o.object);
						}
					}
				}
				break;
			}
			case "setImage": {
				for(Operation o: command.operationList) {
					if(o.sign == 0) {
						if(o.object instanceof String) {
							this.model.document.setImage((String)o.object);
						}
					}
				}
				break;
			}
			case "resizeImage":{
				for(Operation o: command.operationList) {
					if(o.sign == 0) {
						if(o.object instanceof Double) {
//							this.model.document.getPage().setBufferedImage(null);
//							this.model.document.getImage().setBufferedImage(null);
							this.model.document.getImage().setRelativeScale((double)o.object);
							this.model.document.setBufferedImage(this.model.document.getPage());
							
						}
					}
				}
				break;
			}
			case "shiftImage": {
				for(Operation o: command.operationList) {
					if(o.sign == 0) {
						if(o.object instanceof Double) {
							this.model.document.getPage().setBufferedImage(null);
							this.model.document.getPage().getImage().setMargin((double)o.object);
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
							this.model.document.getPage().setBufferedImage(null);
							this.model.document.getPage().imageList.add(index, image);
						}
					}
				}
			}
			case "setGrid": {
				for(Operation o: command.operationList) {
					if(o.sign == 0) {
						if(o.object instanceof Shape) {
							Shape shape = (Shape)o.object;
							this.model.document.getPage().removeShape(shape);
							this.model.document.getPage().addShape(shape);
						}
					}
				}
				break;
			}
			case "setGridShape": {
				for(Operation o: command.operationList) {
					if(o.sign == 0) {
						if(o.object instanceof String) {
							String uuid = (String)o.object;
							Shape shape = this.model.document.getPage().getShape();
							if(shape instanceof Grid) {
								Grid grid = (Grid)shape;
								grid.setShape(uuid);
							}
						}
					}
				}
				break;
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
							this.model.document.pageList = (List<Page>)operation.object;
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
							this.model.document.getPage().setShape((String) operation.object);
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
							this.model.document.getPage().getImage().addShape((Shape) operation.object);
						}
					} else if (operation.sign == 0) {
						if (operation.object instanceof Shape) {
							this.model.document.getPage().getImage().removeShape((Shape) operation.object);
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
							Shape shape = (Shape)operation.object;
							if(shape instanceof Grid) {
								Grid grid = (Grid) shape;
								grid.updateMatrix();
							}
							this.model.document.getPage().getImage().addShape((Shape) operation.object);
						}
					} 
					else if (operation.sign == 0) {
						if (operation.object instanceof Shape) {
							Shape shape = (Shape)operation.object;
							this.model.document.getPage().getImage().removeShape(shape);
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
							this.model.document.removePage((String)o.object);
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
							this.model.document.getPage().getImage().addShape((Shape) operation.object);
						}
					} else if (operation.sign == 0) {
						if (operation.object instanceof Shape) {
							this.model.document.getPage().getImage().removeShape((Shape) operation.object);
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
							this.model.document.getPage().getImage().addShape((Shape) operation.object);
						}
					} else if (operation.sign == 0) {
						if (operation.object instanceof Shape) {
							this.model.document.getPage().getImage().removeShape((Shape) operation.object);
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
							this.model.document.pageList = (List<Page>)o.object;
						}
					} 
				}
				break;
			}
			case "setImage": {
				for(Operation o: command.operationList) {
					if(o.sign == 1) {
						if(o.object instanceof String) {
							this.model.document.setImage((String)o.object);
						}
					}
				}
				break;
			}
			case "resizeImage":{
				for(Operation o: command.operationList) {
					if(o.sign == 1) {
						if(o.object instanceof Double) {
							this.model.document.getPage().setBufferedImage(null);
							this.model.document.getImage().setBufferedImage(null);
							this.model.document.getImage().setRelativeScale((double)o.object);
						}
					}
				}
				break;
			}
			case "scalePage":{
				for(Operation o: command.operationList) {
					if(o.sign == 1) {
						if(o.object instanceof Double) {
							this.model.document.getPage().setScale((double)o.object);
						}
					}
				}
				break;
			}
			case "setPage":{
				for(Operation o: command.operationList) {
					if(o.sign == 1) {
						if(o.object instanceof String) {
							this.model.document.setPage((String)o.object);
						}
					}
				}
				break;
			}
			case "shiftImage": {
				for(Operation o: command.operationList) {
					if(o.sign == 1) {
						if(o.object instanceof Double) {
							this.model.document.getPage().setBufferedImage(null);
							this.model.document.getPage().getImage().setMargin((double)o.object);
						}
					}
				}
				break;
			}
			case "removeImage": {
				for(Operation o: command.operationList) {
					if(o.sign == 1) {
						if(o.object instanceof String) {
							this.model.document.getPage().setBufferedImage(null);
							this.model.document.getPage().removeImage((String)o.object);
						}
					}
				}
				break;
			}
			case "setGrid": {
				for(Operation o: command.operationList) {
					if(o.sign == 1) {
						if(o.object instanceof Grid) {
							Grid grid = (Grid)o.object;
							this.model.document.getPage().removeShape(grid);
							this.model.document.getPage().addShape(grid);
						}
					}
				}
				break;
			}
			case "setGridShape": {
				for(Operation o: command.operationList) {
					if(o.sign == 1) {
						if(o.object instanceof String) {
							String uuid = (String)o.object;
							Shape shape = this.model.document.getPage().getShape();
							if(shape instanceof Grid) {
								Grid grid = (Grid)shape;
								grid.setShape(uuid);
							}
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
