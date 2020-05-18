package com.meritoki.app.desktop.retina.model.document.command;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.meritoki.app.desktop.retina.model.Model;
import com.meritoki.app.desktop.retina.model.document.Document;
import com.meritoki.app.desktop.retina.model.document.Point;
import com.meritoki.app.desktop.retina.model.document.Shape;
import com.meritoki.app.desktop.retina.model.document.Type;

public class AddShape extends Command {
	
	private static Logger logger = LogManager.getLogger(AddShape.class.getName());
	
	public AddShape(Document document) {
		super(document, "addShape");
	}
	
    @Override // Command
    public void execute() {
    	logger.info("execute()");
    	this.user = this.document.state.user;
    	if(this.minimumSize(this.document.state.pressedPoint, this.document.state.releasedPoint, this.document.state.scale)) {
			this.document.state.pressedShape = new Shape();
			if (this.document.state.rectangle) {
				this.document.state.pressedShape.type = Type.RECTANGLE;
			} else if (this.document.state.ellipse) {
				this.document.state.pressedShape.type = Type.ELLIPSE;
			}
			this.document.state.pressedShape.setAddScale(this.document.state.scale);
			this.document.state.pressedShape.setScale(this.document.state.scale);
			this.document.state.pressedShape.pointList.add(new Point(this.document.state.pressedPoint));
			this.document.state.pressedShape.pointList.add(new Point(this.document.state.releasedPoint));
			this.document.state.pressedShape.sortPointList();
	    	this.document.getPage().addShape(this.document.state.pressedShape);
			Operation operation = new Operation();
			operation.object = new Shape(this.document.state.pressedShape);
			operation.sign = 1;
			operation.id = UUID.randomUUID().toString();
			operation.uuid = this.document.state.pressedShape.uuid;
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
