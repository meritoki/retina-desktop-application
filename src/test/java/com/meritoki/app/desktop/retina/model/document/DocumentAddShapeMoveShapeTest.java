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
	static int limit = 10000;

	@BeforeAll
	public static void initialize() {
		model.document = new Document();
		Page page = new Page();
		page.addImage(new Image(new File("./data/image/01.jpg")));
		page.addImage(new Image(new File("./data/image/02.jpg")));
		page.addImage(new Image(new File("./data/image/03.jpg")));
		model.document.addPage(page);
		assertEquals(model.document.pageList.size(), 1);
	}
	
	@Test
	@Order(1)
	public void addShape() {
		assertEquals(model.document.setIndex(0), true);
		assertEquals(model.document.getPage().setIndex(0), true);
		model.cache.pressedImage = model.document.getImage();
		double x = (int) (model.cache.pressedImage.position.center.x - dimension / 2);
		double y = (int) (model.cache.pressedImage.position.center.y - dimension / 2);
		int width = dimension;
		int height = dimension;
		model.cache.pressedPoint = new Point(x, y);
		model.cache.releasedPoint = new Point(x + width, y + height);
		try {
			model.pattern.execute("addShape");
		} catch (Exception e) {
			logger.error("Exception " + e.getMessage());
		}
		assertNotNull(model.document.getShape(new Point(model.cache.pressedImage.position.center.x, model.cache.pressedImage.position.center.y)));
	}
	
	@Test
	@Order(2)
	public void moveShape() {
		assertEquals(model.document.setIndex(0), true);
		Page page = model.document.getPage();
		Position position = page.position;
		Point startPoint = new Point(model.cache.pressedImage.position.center.x, model.cache.pressedImage.position.center.y);
		Point previousPoint = startPoint;
		Point newPoint;
		int count = limit;
		while(count > 0) {
			newPoint = getRandomPoint(position);
			model.cache.pressedPoint = previousPoint;
			model.cache.pressedShape = model.document.getPage().getShape(model.cache.pressedPoint);
			model.cache.releasedPoint = newPoint;
			try {
				model.pattern.execute("moveShape");
			} catch (Exception e) {
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
			model.pattern.undo();
			count--;
		}
		assertNotNull(model.document.getShape(new Point(model.cache.pressedImage.position.center.x, model.cache.pressedImage.position.center.y)));
	}
	
	public static Point getRandomPoint(Position position) {
		Point point = new Point();
		point.x = ThreadLocalRandom.current().nextInt(0, (int)(position.dimension.width) + 1);
		point.y = ThreadLocalRandom.current().nextInt(0, (int)(position.dimension.height) + 1);
		return point;
	}
}
