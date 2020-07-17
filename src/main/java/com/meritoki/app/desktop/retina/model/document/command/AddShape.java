package com.meritoki.app.desktop.retina.model.document.command;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.meritoki.app.desktop.retina.model.document.Position;
import com.meritoki.app.desktop.retina.model.document.Document;
import com.meritoki.app.desktop.retina.model.document.Grid;
import com.meritoki.app.desktop.retina.model.document.Image;
import com.meritoki.app.desktop.retina.model.document.Page;
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
		Page page = this.document.getPage();
		ShapeType type = this.document.cache.type;
		Point pressedPoint = this.document.cache.pressedPoint;
		Point releasedPoint = this.document.cache.releasedPoint;
		Image pressedImage = this.document.cache.pressedImage;
		double scale = page.position.scale;
		Shape shape = new Shape();
//		Grid shape = new Grid();
		
//		if (this.minimumSize(pressedPoint, releasedPoint, scale)) {
			shape.type = type;
			shape.position = new Position(new Point(pressedPoint), new Point(releasedPoint),
					pressedImage.position.relativeScale, scale, pressedImage.position.offset,
					pressedImage.position.margin);
//			shape.updateMatrix();
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
