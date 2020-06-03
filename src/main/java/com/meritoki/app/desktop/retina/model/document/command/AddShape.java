package com.meritoki.app.desktop.retina.model.document.command;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.meritoki.app.desktop.retina.model.document.Position;
import com.meritoki.app.desktop.retina.model.document.Document;
import com.meritoki.app.desktop.retina.model.document.Image;
import com.meritoki.app.desktop.retina.model.document.Point;
import com.meritoki.app.desktop.retina.model.document.Shape;
import com.meritoki.app.desktop.retina.model.document.ShapeType;

public class AddShape extends Command {

	private static Logger logger = LogManager.getLogger(AddShape.class.getName());

	public AddShape(Document document) {
		super(document, "addShape");
	}

	@Override
	public void execute() throws Exception {
		logger.info("execute()");
		// variable
		double scale = this.document.cache.scale;
		ShapeType type = this.document.cache.type;
		Point pressedPoint = this.document.cache.pressedPoint;
		Point releasedPoint = this.document.cache.releasedPoint;
		Image pressedImage = this.document.cache.pressedImage;

		Shape shape = new Shape();
//		if (this.minimumSize(this.document.cache.pressedPoint, this.document.cache.releasedPoint,
//				this.document.cache.scale)) {
		shape.type = type;
		shape.position = new Position(new Point(pressedPoint), new Point(releasedPoint),
				pressedImage.position.relativeScale, scale, pressedImage.position.offset,
				pressedImage.position.margin);
		this.document.addShape(shape);
		Operation operation = new Operation();
		operation.object = new Shape(shape, true);
		operation.sign = 1;
		operation.id = UUID.randomUUID().toString();
		this.operationList.push(operation);
//		} else {
//			throw new Exception("Shape too small");
//		}
	}

	public boolean minimumSize(Point a, Point b, double scale) {
		boolean flag = false;
		double width = Math.abs(b.x - a.x) * scale;
		double height = Math.abs(b.y - a.y) * scale;
		double size = 512 * scale;
		if ((width * height) > size) {
			flag = true;
		}
		return flag;
	}
}
