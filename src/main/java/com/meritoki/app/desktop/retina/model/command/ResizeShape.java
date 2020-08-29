package com.meritoki.app.desktop.retina.model.command;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.meritoki.app.desktop.retina.model.Model;
import com.meritoki.app.desktop.retina.model.document.Grid;
import com.meritoki.app.desktop.retina.model.document.Point;
import com.meritoki.app.desktop.retina.model.document.Selection;
import com.meritoki.app.desktop.retina.model.document.Shape;

public class ResizeShape extends Command {
	
	private static Logger logger = LogManager.getLogger(ResizeShape.class.getName());
	
	public ResizeShape(Model document) {
		super(document, "resizeShape");
	}
	
    @Override // Command
    public void execute() {
    	logger.info("execute()");
    	//variable
    	//cache
    	String pressedShapeUUID = this.model.cache.pressedShapeUUID;
    	Selection selection = this.model.cache.selection;
		Point releasedPoint = this.model.cache.releasedPoint;
		
		//load
		Shape pressedShape = this.model.document.getShape(pressedShapeUUID);
		
    	//Undo
		Operation operation = new Operation();
		operation.object = new Shape(pressedShape,true);
		operation.sign = 0;
		operation.id = UUID.randomUUID().toString();
		this.operationList.push(operation);
		//Logic
		pressedShape.position.resize(new Point(releasedPoint), selection);
		if(pressedShape instanceof Grid) {
			((Grid)pressedShape).updateMatrix();
		}
		//Redo
		operation = new Operation();
		operation.object = new Shape(pressedShape,true);
		operation.sign = 1;
		operation.id = UUID.randomUUID().toString();
		this.operationList.push(operation);
    }
}
