package com.meritoki.app.desktop.retina.model.command;

import java.util.List;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.meritoki.app.desktop.retina.model.Model;
import com.meritoki.app.desktop.retina.model.document.Image;
import com.meritoki.app.desktop.retina.model.document.Page;
import com.meritoki.app.desktop.retina.model.document.Point;
import com.meritoki.app.desktop.retina.model.document.Position;
import com.meritoki.app.desktop.retina.model.document.Selector;
import com.meritoki.app.desktop.retina.model.document.Shape;
import com.meritoki.app.desktop.retina.model.document.ShapeType;

public class AddSelector extends Command {
	private static Logger logger = LogManager.getLogger(AddSelector.class.getName());

	public AddSelector(Model document) {
		super(document, "addSelector");
	}
	
	@Override
	public void execute() throws Exception {
		logger.info("execute()");
		// variable
		String pressedPageUUID = this.model.cache.pressedPageUUID;
		String pressedImageUUID = this.model.cache.pressedImageUUID;
		Page page = this.model.document.getPage(pressedPageUUID);
		Image pressedImage = this.model.document.getImage(pressedImageUUID);
		ShapeType type = this.model.cache.type;
		Point pressedPoint = this.model.cache.pressedPoint;
		Point releasedPoint = this.model.cache.releasedPoint;
		Operation operation = new Operation();
		operation.object = this.model.cache.selector;
		operation.sign = 0;
		operation.id = UUID.randomUUID().toString();
		this.operationList.push(operation);
		double scale = page.position.scale;
		Selector selector = new Selector();
		selector.type = type;
		selector.position = new Position(new Point(pressedPoint), new Point(releasedPoint),
				pressedImage.position.relativeScale, scale, pressedImage.position.offset, pressedImage.position.margin);
		List<Shape> shapeList = page.getShapeList();
		for(Shape shape:shapeList) {
			selector.addShape(shape);
		}
		this.model.cache.selector = selector;
		selector.bufferedImage = this.model.document.getShapeBufferedImage(page.getScaledBufferedImage(this.model), selector);
		operation = new Operation();
		operation.object = new Selector(selector, true);
		operation.sign = 1;
		operation.id = UUID.randomUUID().toString();
		this.operationList.push(operation);
	}
	
	@Override
	public void undo() {
		for (int i = 0; i < this.operationList.size(); i++) {
			Operation operation = this.operationList.get(i);
			if (operation.sign == 0) {
				if (operation.object instanceof Selector) {
					this.model.cache.selector = (Selector)operation.object;
				} else {
					this.model.cache.selector = null;
				}
			} 
		}
	}
	
	@Override
	public void redo() {
		for (int i = 0; i < this.operationList.size(); i++) {
			Operation operation = this.operationList.get(i);
			if (operation.sign == 1) {
				if (operation.object instanceof Selector) {
					this.model.cache.selector = (Selector)operation.object;
				}
			} 
		}
	}
}
