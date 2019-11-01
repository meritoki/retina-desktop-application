package com.meritoki.retina.application.desktop.model.document;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Document 
 * @author jorodriguez
 *
 */
public class Document {

	@JsonProperty
	public Project project;
	@JsonProperty
	public List<State> stateList = new ArrayList<>();
	@JsonProperty
	public List<User> userList = new ArrayList<>();
	public State state = new State();
	
	public Project getProject() {
		return this.project;
	}
	
	@JsonIgnore
	public State getState() {
		return this.state;
	}
	
	@JsonIgnore
	public Page getPage() {
		return (this.project != null) ? this.project.getPage() : null;
	}

	@JsonIgnore
	public List<Page> getPageList() {
		return (this.project != null) ? this.project.getPageList() : null;
	}

	@JsonIgnore
	public File getFile() {
		return (this.project != null) ? this.project.getFile() : null;
	}

	@JsonIgnore
	public File getFile(Point point) {
		return (this.project != null) ? this.project.getFile(point) : null;
	}

	@JsonIgnore
	public Shape getShape(Point point) {
		return (this.project != null) ? this.project.getShape(point) : null;
	}

	@JsonIgnore
	public Shape getShape() {
		return (this.project != null) ? this.project.getShape() : null;
	}

	@JsonIgnore
	public void setScale(double scale) {
		if (this.project != null) {
			this.project.setScale(scale);
		}
	}
	
	public void removeShape() {
		//TODO implement remove shape
//		this.model.shape = this.project.getShape();
//		this.model.project.getPage().getFile().removeShape(this.model.shape);
//		Command command = new Command();
//		Operation operation = new Operation();
//		operation.object = this.model.shape;
//		operation.sign = 0;
//		operation.id = UUID.randomUUID().toString();
//		operation.uuid = this.model.shape.uuid;
//		command.operationList.push(operation);
//		this.model.undoStack.push(command);
	}
	
	public void undo() {
//		if (this.model.undoStack.size() > 0) {
//			Command command = this.model.undoStack.pop();
//			Operation operation = null;
//			for (int i = 0; i < command.operationList.size(); i++) {
//				operation = command.operationList.get(i);
//				if (operation.sign == 1) {
//					if (operation.object instanceof Shape) {
//						this.model.project.getPage().getFile().removeShape((Shape) operation.object);
//					}
//				} else if (operation.sign == 0) {
//					if (operation.object instanceof Shape) {
//						this.model.project.getPage().getFile().addShape((Shape) operation.object);
//					}
//				}
//			}
//			this.model.redoStack.push(command);
//			
//		}
	}
	
	public void redo() {
//		if (this.model.redoStack.size() > 0) {
//			Command command = this.model.redoStack.pop();
//			Operation operation = null;
//			for (int i = command.operationList.size() - 1; i >= 0; i--) {
//				operation = command.operationList.get(i);
//				if (operation.sign == 1) {
//					if (operation.object instanceof Shape) {
//						this.model.project.getPage().getFile().addShape((Shape) operation.object);
//					}
//				} else if (operation.sign == 0) {
//					if (operation.object instanceof Shape) {
//						this.model.project.getPage().getFile().removeShape((Shape) operation.object);
//					}
//				}
//			}
//			this.model.undoStack.push(command);
//
//		}
	}
}
