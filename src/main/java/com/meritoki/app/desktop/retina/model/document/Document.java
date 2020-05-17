package com.meritoki.app.desktop.retina.model.document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;

import com.meritoki.app.desktop.retina.model.document.command.AddPage;
import com.meritoki.app.desktop.retina.model.document.command.AddShape;
import com.meritoki.app.desktop.retina.model.document.command.Command;
import com.meritoki.app.desktop.retina.model.document.command.MoveShape;
import com.meritoki.app.desktop.retina.model.document.command.Operation;
import com.meritoki.app.desktop.retina.model.document.command.RemoveShape;
import com.meritoki.app.desktop.retina.model.document.command.ResizeShape;
import com.meritoki.app.desktop.retina.model.document.command.SetPage;
import com.meritoki.app.desktop.retina.model.document.command.SetShape;
import com.meritoki.app.desktop.retina.model.document.user.User;

/**
 * Document
 *
 * @author jorodriguez
 *
 */
public class Document {
	@JsonIgnore
	static Logger logger = LogManager.getLogger(Document.class.getName());
	@JsonProperty
	public String uuid = null;
	@JsonProperty
	public LinkedList<Event> eventStack = new LinkedList<>();
	@JsonProperty
	public List<User> userList = new LinkedList<>();
	@JsonProperty
	public Event event = new Event();
	@JsonProperty
	public State state = new State();
	@JsonIgnore
	private final HashMap<String, Command> commandMap = new HashMap<>();
	@JsonProperty
	public List<Page> pageList = new ArrayList<>();
	@JsonIgnore
	public int index = 0;
	@JsonIgnore
	public File file = new File();
	@JsonProperty
	public List<Layout> layoutList = new ArrayList<>();

	public Document() {
		this.uuid = UUID.randomUUID().toString();
		this.registerCommands();
	}
	
	public void registerCommands() {
		Command addPage = new AddPage(this);
		Command setPage = new SetPage(this);
		Command addShape = new AddShape(this);
		Command setShape = new SetShape(this);
		Command moveShape = new MoveShape(this);
		Command removeShape = new RemoveShape(this);
		Command resizeShape = new ResizeShape(this);
		this.register("addPage", addPage);
		this.register("setPage", setPage);
		this.register("addShape", addShape);
		this.register("setShape", setShape);
		this.register("moveShape", moveShape);
		this.register("removeShape", removeShape);
		this.register("resizeShape", resizeShape);
	}

//	@JsonIgnore
//	public void test() {
//		Page page = new Page();
//		File file = new File("./data/image", "01.jpg");
//		page.fileList.add(file);
//		file = new File("./data/image","02.jpg");
//		page.fileList.add(file);
//		pageList.add(page);
//		page = new Page();
//		file = new File("./data/image","02.jpg");
//		page.fileList.add(file);
//		pageList.add(page);
//		page = new Page();
//		file = new File("./data/image","03.jpg");
//		page.fileList.add(file);
//		pageList.add(page);
//		page = new Page();
//		file = new File("./data/image","04.jpg");
//		page.fileList.add(file);
//		pageList.add(page);
//		page = new Page();
//		file = new File("./data/image","05.jpg");
//		page.fileList.add(file);
//		pageList.add(page);
//		page = new Page();
//		file = new File("./data/image","06.jpg");
//		page.fileList.add(file);
//		pageList.add(page);
//		page = new Page();
//		file = new File("./data/image","07.jpg");
//		page.fileList.add(file);
//		pageList.add(page);
//		page = new Page();
//		file = new File("./data/image","08.jpg");
//		page.fileList.add(file);
//		pageList.add(page);
//		this.setIndex(0);
//	}

	/**
	 * Get the index of the current Page, used by Dialogs
	 * 
	 * @return
	 */
	@JsonIgnore
	public int getIndex() {
		logger.debug("getIndex() this.index=" + this.index);
		return this.index;
	}

	/**
	 * Functions gets Page object at current index from Page List
	 * 
	 * @return Page
	 */
	@JsonIgnore
	public Page getPage() {
		return (this.pageList.size() > 0) ? this.pageList.get(index) : new Page();
	}

	/**
	 * Function get reference to Page List
	 * 
	 * @return List<Page>
	 */
	@JsonIgnore
	public List<Page> getPageList() {
		return this.pageList;
	}

	@JsonIgnore
	public void setIndex(int index) {
		logger.debug("setIndex(" + index + ")");
		if (index >= 0 && index < this.pageList.size()) {
			this.index = index;
		}
	}

	@JsonIgnore
	public void setPage(String uuid) {
		logger.info("setPage(" + uuid + ")");
		Page page = null;
		for (int i = 0; i < this.pageList.size(); i++) {
			page = this.pageList.get(i);
			if (page.uuid.equals(uuid)) {
				this.setIndex(i);
				;
				break;
			}
		}
	}

	@JsonIgnore
	public void setPageList(List<Page> pageList) {
		logger.info("setPageList(" + pageList + ")");
		this.pageList = pageList;
	}

	@JsonIgnore
	public void addPage(Page page) {
		logger.info("addPage(" + page + ")");
		this.pageList.add(page);
	}

	public List<Shape> getShapeList() {
		List<Shape> shapeList = new ArrayList<>();
		for (Page page : this.pageList) {
			shapeList.addAll(page.getShapeList());
		}
		return shapeList;
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
		Command newCommand = new Command(this, command.name);
		newCommand.user = command.user;
		newCommand.operationList = command.operationList;
		this.event.undoStack.push(newCommand);
		command.reset();
	}

	public boolean importText(List<String[]> stringArrayList) {
		logger.info("importText(...)");
		boolean flag = false;
		for (int i = 0; i < stringArrayList.size(); i++) {
			String[] stringArray = stringArrayList.get(i);
			String value = null;
			for (String string : stringArray) {
				if (string.contains("value")) {
					value = string.split(":")[1].replace("\"", "");
				}
			}
			String uuid = null;
			for (String string : stringArray) {
				if (string.contains("the_image")) {
					uuid = string.split(":")[1].replace(".jpg", "").replace("}", "").replace("\"", "");
				}
			}
			System.out.println(value + " " + uuid);
			if (uuid != null && value != null) {
				for (Page page : this.getPageList()) {
					for (Shape shape : page.getShapeList()) {
						if (uuid.equals(shape.uuid)) {
							Text text = new Text();
							text.value = value;
							shape.addText(text);
							flag = true;
						}
					}
				}
			}
		}
		return flag;
	}

	@JsonIgnore
	public void addUser(User user) {
		this.userList.add(user);
	}

	@JsonIgnore
	public boolean containsUser(User user) {
		boolean flag = false;
		for (User u : this.userList) {
			if (u.uuid.equals(user.uuid)) {
				flag = true;
				break;
			}
		}
		return flag;
	}

	public boolean removeUser(User user) {
		boolean flag = false;
		for (int i = 0; i < this.userList.size(); i++) {
			User u = this.userList.get(i);
			if (u.uuid.equals(user.uuid)) {
				flag = true;
				this.userList.remove(i);
				break;
			}
		}
		return flag;
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
							this.getPage().getFile().setShape(((Shape) operation.object).uuid);
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
							this.getPage().removeShape((Shape) operation.object);
						}
					} else if (operation.sign == 0) {
						if (operation.object instanceof Shape) {
							this.getPage().getFile().addShape((Shape) operation.object);
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
							this.getPage().removeShape((Shape) operation.object);
						}
					} else if (operation.sign == 0) {
						if (operation.object instanceof Shape) {
							this.getPage().getFile().addShape((Shape) operation.object);
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
							this.getPage().removeShape((Shape) operation.object);
						}
					} else if (operation.sign == 0) {
						if (operation.object instanceof Shape) {
							this.getPage().getFile().addShape((Shape) operation.object);
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
							this.getPage().removeShape((Shape) operation.object);
						}
					} else if (operation.sign == 0) {
						if (operation.object instanceof Shape) {
							this.getPage().getFile().addShape((Shape) operation.object);
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
							this.getPage().getFile().setShape(((Shape) operation.object).uuid);
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
							this.getPage().getFile().addShape((Shape) operation.object);
						}
					} else if (operation.sign == 0) {
						if (operation.object instanceof Shape) {
							this.getPage().getFile().removeShape((Shape) operation.object);
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
							this.getPage().getFile().addShape((Shape) operation.object);
						}
					} else if (operation.sign == 0) {
						if (operation.object instanceof Shape) {
							this.getPage().getFile().removeShape((Shape) operation.object);
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
							this.getPage().getFile().addShape((Shape) operation.object);
						}
					} else if (operation.sign == 0) {
						if (operation.object instanceof Shape) {
							this.getPage().getFile().removeShape((Shape) operation.object);
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
							this.getPage().getFile().addShape((Shape) operation.object);
						}
					} else if (operation.sign == 0) {
						if (operation.object instanceof Shape) {
							this.getPage().getFile().removeShape((Shape) operation.object);
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

	@JsonIgnore
	@Override
	public String toString() {
		String string = "";
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		try {
			string = ow.writeValueAsString(this);
		} catch (IOException ex) {
			logger.error("IOException "+ex.getMessage());
		}
		return string;
	}
}
//public static void main(String[] args) throws IOException{
//    Project project = new Project();
////    File file = new File("~/test.json");
//    project.initTest();
//    ObjectMapper mapper = new ObjectMapper();
//    Model model = new Model();
//    model.getDocument().project = project;
////    model.open(new java.io.File("/home/jorodriguez/test.json"));
////    mapper.setVisibility(JsonMethod.FIELD, Visibility.ANY);
////    mapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
////    mapper.writeValue(file, project);
////    project = mapper.readValue(file, Project.class);
//    String jsonInString = mapper.writeValueAsString(model.getDocument());
//    ModelClient modelClient = new ModelClient();
//    User user = new User();
//    user.name = "javainuse";
//    user.password = "password";
//    modelClient.login(user);
//    modelClient.uploadProject(jsonInString);
//    System.out.println(project);
//}
