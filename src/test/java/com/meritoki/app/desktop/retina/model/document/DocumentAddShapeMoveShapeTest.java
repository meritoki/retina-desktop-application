package com.meritoki.app.desktop.retina.model.document;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.File;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.meritoki.app.desktop.retina.model.Model;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DocumentAddShapeMoveShapeTest {

	static Logger logger = LogManager.getLogger(DocumentAddShapeMoveShapeShiftImageTest.class.getName());
	static Model model = new Model();
	static int dimension = 2;
	static int limit =   16384;

	@BeforeAll
	public static void initialize() {
		model.document = new Document();
		Page page = new Page();
		page.addImage(new Image(new File("./test/img/test.png")));
		page.addImage(new Image(new File("./test/img/test.png")));
		page.addImage(new Image(new File("./test/img/test.png")));
		model.document.addPage(page);
		assertEquals(model.document.pageList.size(), 1);
	}
	
	@Test
	@Order(1)
	public void addShape() {
		assertEquals(model.document.setIndex(0), true);
		model.document.getPage().getBufferedImage(model);
		assertEquals(model.document.getPage().setIndex(0), true);
		model.system.pressedImage = model.document.getImage();
		double x = (int) (model.system.pressedImage.position.center.x - dimension / 2);
		double y = (int) (model.system.pressedImage.position.center.y - dimension / 2);
		int width = dimension;
		int height = dimension;
		model.cache.pressedPoint = new Point(x, y);
		model.cache.releasedPoint = new Point(x + width, y + height);
		try {
			model.cache.pressedPageUUID = model.document.getPage().uuid;
			model.cache.pressedImageUUID = model.system.pressedImage.uuid;
			model.pattern.execute("addShape");
		} catch (Exception e) {
			logger.error("Exception " + e.getMessage());
		}
		assertNotNull(model.document.getShape(new Point(model.system.pressedImage.position.center.x, model.system.pressedImage.position.center.y)));
	}
	
	@Test
	@Order(2)
	public void moveShape() {
		assertEquals(model.document.setIndex(0), true);
		model.document.getPage().getBufferedImage(model);
		Page page = model.document.getPage();
		Position position = page.position;
		Point startPoint = new Point(model.system.pressedImage.position.center.x, model.system.pressedImage.position.center.y);
		Point previousPoint = startPoint;
		Point newPoint;
		int count = limit;
		while(count > 0) {
			newPoint = getRandomPoint(position);
			model.cache.pressedPoint = previousPoint;
			model.system.pressedShape = model.document.getPage().getShape(model.cache.pressedPoint);
			model.cache.releasedPoint = newPoint;
			try {
				model.cache.pressedPageUUID = model.document.getPage().uuid;
				model.cache.pressedShapeUUID = model.system.pressedShape.uuid;
				model.pattern.execute("moveShape");
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("Exception " + e.getMessage());
			}
			assertNotNull(model.document.getPage().getShape(newPoint));
			previousPoint = newPoint;
			count--;
		}
	}
	
	@Test
	@Order(3)
	public void undo() {
		int count = limit;
		while(count > 0) {
			try {
			model.pattern.undo();
			count--;
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		assertNotNull(model.document.getShape(new Point(model.system.pressedImage.position.center.x, model.system.pressedImage.position.center.y)));
	}
	
	public static Point getRandomPoint(Position position) {
		Point point = new Point();
		point.x = ThreadLocalRandom.current().nextInt(0, (int)(position.dimension.width));
		point.y = ThreadLocalRandom.current().nextInt(0, (int)(position.dimension.height));
		return point;
	}
}
