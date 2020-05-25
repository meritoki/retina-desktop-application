package com.meritoki.app.desktop.retina.model.document.command;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.meritoki.app.desktop.retina.model.document.Position;
import com.meritoki.app.desktop.retina.model.document.Document;
import com.meritoki.app.desktop.retina.model.document.Point;
import com.meritoki.app.desktop.retina.model.document.Shape;

public class AddShape extends Command {

	private static Logger logger = LogManager.getLogger(AddShape.class.getName());

	public AddShape(Document document) {
		super(document, "addShape");
	}

	@Override
	public void execute() throws Exception {
		logger.info("execute()");
		this.user = this.document.cache.user;
//		if (this.minimumSize(this.document.cache.pressedPoint, this.document.cache.releasedPoint,
//				this.document.cache.scale)) {
			this.document.cache.pressedShape = new Shape();
			this.document.cache.pressedShape.type = this.document.cache.type;
			this.document.cache.pressedShape.position = new Position(new Point(this.document.cache.pressedPoint),
					new Point(this.document.cache.releasedPoint), this.document.cache.scale,
					this.document.cache.pressedImage.position.offset, this.document.cache.pressedImage.position.margin);
			this.document.addShape(this.document.cache.pressedShape);
			Operation operation = new Operation();
			operation.object = new Shape(this.document.cache.pressedShape);
			operation.sign = 1;
			operation.id = UUID.randomUUID().toString();
			operation.uuid = this.document.cache.pressedShape.uuid;
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
