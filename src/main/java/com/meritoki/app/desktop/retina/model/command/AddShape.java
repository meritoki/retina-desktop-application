package com.meritoki.app.desktop.retina.model.command;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.meritoki.app.desktop.retina.model.Model;
import com.meritoki.app.desktop.retina.model.document.Operation;
import com.meritoki.app.desktop.retina.model.document.Point;
import com.meritoki.app.desktop.retina.model.document.Shape;

public class AddShape extends Command {
	private static Logger logger = LogManager.getLogger(AddShape.class.getName());
	public AddShape(Model project) {
		this.model = project;
		this.name = "addShape";
	}
	
    @Override // Command
    public void execute() {
    	logger.info("execute()");
    	this.user = this.model.user;
    	if(this.minimumSize(this.model.variable.pressedPoint, this.model.variable.releasedPoint, this.model.variable.scale)) {
			this.model.variable.pressedShape = new Shape();
			if (this.model.variable.rectangle) {
				this.model.variable.pressedShape.classification = Shape.RECTANGLE;
			} else if (this.model.variable.ellipse) {
				this.model.variable.pressedShape.classification = Shape.ELLIPSE;
			}
			this.model.variable.pressedShape.setAddScale(this.model.variable.scale);
			this.model.variable.pressedShape.setScale(this.model.variable.scale);
			this.model.variable.pressedShape.pointList.add(new Point(this.model.variable.pressedPoint));
			this.model.variable.pressedShape.pointList.add(new Point(this.model.variable.releasedPoint));
			this.model.variable.pressedShape.sortPointList();
			
	    	this.model.getDocument().getProject().getPage().addShape(this.model.variable.pressedShape);
			Operation operation = new Operation();
			operation.object = new Shape(this.model.variable.pressedShape);
			operation.sign = 1;
			operation.id = UUID.randomUUID().toString();
			operation.uuid = this.model.variable.pressedShape.uuid;
			this.operationList.push(operation);
    	} else {
    		logger.error("execute() shape size");
    	}
    }
    
    public boolean minimumSize(Point a, Point b, double scale) {
    	boolean flag = false;
    	double width = Math.abs(b.x-a.x) * scale;
    	double height = Math.abs(b.y-a.y) * scale;
    	double size = 512 * scale;
    	if((width * height) > size) {
    		flag = true;
    	}
    	return flag;
    }
}
