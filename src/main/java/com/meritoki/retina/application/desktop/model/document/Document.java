package com.meritoki.retina.application.desktop.model.document;

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

import com.meritoki.retina.application.desktop.controller.client.ModelClient;
import com.meritoki.retina.application.desktop.controller.json.JsonController;
import com.meritoki.retina.application.desktop.model.Model;
import com.meritoki.retina.application.desktop.model.User;
import com.meritoki.retina.application.desktop.model.command.Command;
import com.meritoki.retina.application.desktop.model.provider.Provider;
import com.meritoki.retina.application.desktop.model.provider.zooniverse.Reference;
import com.meritoki.retina.application.desktop.model.provider.zooniverse.Task;
import com.meritoki.retina.application.desktop.model.provider.zooniverse.ZooniverseProvider;
import com.meritoki.retina.application.desktop.model.vendor.Vendor;

/**
 * Document
 *
 * @author jorodriguez
 *
 */
public class Document {

    public static void main(String[] args) throws IOException {
        Project project = new Project();
        project.initTest();
        ObjectMapper mapper = new ObjectMapper();
        Model model = new Model();
        model.getDocument().project = project;
        String jsonInString = mapper.writeValueAsString(model.getDocument());
        ModelClient modelClient = new ModelClient();
        User user = new User();
        user.name = "javainuse";
        user.password = "password";
        modelClient.login(user);
        modelClient.uploadProject(jsonInString);
        System.out.println(project);
    }
    @JsonIgnore
    static Logger logger = LogManager.getLogger(Document.class.getName());
    @JsonProperty
    public String uuid = null;
    @JsonProperty
    public Project project = new Project();
    @JsonProperty
    public LinkedList<State> stateStack = new LinkedList<>();
    @JsonProperty
    public List<User> userList = new LinkedList<>();
    @JsonProperty
    public State state = new State();
    @JsonIgnore
    private final HashMap<String, Command> commandMap = new HashMap<>();

    public Document() {
        this.uuid = UUID.randomUUID().toString();
//        this.project.initTest();
    }

    public Project getProject() {
        return this.project;
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
        Command newCommand = new Command();
        newCommand.name = command.name;
        newCommand.model = command.model;
        newCommand.user = command.user;
        newCommand.operationList = command.operationList;
        this.state.undoStack.push(newCommand);
        command.reset();
    }
    
    public boolean importText(List<String[]> stringArrayList) {
    	logger.info("importText(...)");
    	boolean flag = false;
    	for(int i = 0; i< stringArrayList.size(); i++) {
    		String[] stringArray = stringArrayList.get(i);
    		String value = null;
    		for(String string: stringArray) {
    			if(string.contains("value")) {
    				value = string.split(":")[1].replace("\"", "");
    			}
    		}
    		String uuid = null;
    		for(String string: stringArray) {
    			if(string.contains("the_image")) {
    				uuid = string.split(":")[1].replace(".jpg", "").replace("}", "").replace("\"", "");
    			}
    		}
    		System.out.println(value+" "+uuid);
    		if(uuid != null && value != null) {
	    		for(Page page: this.getProject().getPageList()) {
	    			for(Shape shape: page.getShapeList()) {
	    				if(uuid.equals(shape.uuid)) {
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
                                this.project.getPage().getFile().setShape(((Shape) operation.object).uuid);
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
                                this.project.getPage().removeShape((Shape) operation.object);
                            }
                        } else if (operation.sign == 0) {
                            if (operation.object instanceof Shape) {
                                this.project.getPage().getFile().addShape((Shape) operation.object);
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
                                this.project.getPage().removeShape((Shape) operation.object);
                            }
                        } else if (operation.sign == 0) {
                            if (operation.object instanceof Shape) {
                                this.project.getPage().getFile().addShape((Shape) operation.object);
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
                                this.project.getPage().removeShape((Shape) operation.object);
                            }
                        } else if (operation.sign == 0) {
                            if (operation.object instanceof Shape) {
                                this.project.getPage().getFile().addShape((Shape) operation.object);
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
                                this.project.getPage().removeShape((Shape) operation.object);
                            }
                        } else if (operation.sign == 0) {
                            if (operation.object instanceof Shape) {
                                this.project.getPage().getFile().addShape((Shape) operation.object);
                            }
                        }
                    }
                    break;
                }
                default: {

                }
            }
            this.state.redoStack.push(command);
        }
    }

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
                                this.project.getPage().getFile().setShape(((Shape) operation.object).uuid);
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
                                this.project.getPage().getFile().addShape((Shape) operation.object);
                            }
                        } else if (operation.sign == 0) {
                            if (operation.object instanceof Shape) {
                                this.project.getPage().getFile().removeShape((Shape) operation.object);
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
                                this.project.getPage().getFile().addShape((Shape) operation.object);
                            }
                        } else if (operation.sign == 0) {
                            if (operation.object instanceof Shape) {
                                this.project.getPage().getFile().removeShape((Shape) operation.object);
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
                                this.project.getPage().getFile().addShape((Shape) operation.object);
                            }
                        } else if (operation.sign == 0) {
                            if (operation.object instanceof Shape) {
                                this.project.getPage().getFile().removeShape((Shape) operation.object);
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
                                this.project.getPage().getFile().addShape((Shape) operation.object);
                            }
                        } else if (operation.sign == 0) {
                            if (operation.object instanceof Shape) {
                                this.project.getPage().getFile().removeShape((Shape) operation.object);
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
