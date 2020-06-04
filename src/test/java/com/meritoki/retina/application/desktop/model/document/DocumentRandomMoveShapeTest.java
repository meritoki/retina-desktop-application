package com.meritoki.retina.application.desktop.model.document;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import java.util.concurrent.ThreadLocalRandom;

import com.meritoki.app.desktop.retina.model.document.Document;
import com.meritoki.app.desktop.retina.model.document.Image;
import com.meritoki.app.desktop.retina.model.document.Page;
import com.meritoki.app.desktop.retina.model.document.Point;
import com.meritoki.app.desktop.retina.model.document.Position;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DocumentRandomMoveShapeTest {

	static Logger logger = LogManager.getLogger(DocumentMoveShapeShiftImageTest.class.getName());
	static Document document = null;
	static int dimension = 2;
	static int limit = 10000;

	@BeforeAll
	public static void initialize() {
		document = new Document();
		Page page = new Page();
		page.addImage(new Image(new File("./data/image/01.jpg")));
		page.addImage(new Image(new File("./data/image/02.jpg")));
		page.addImage(new Image(new File("./data/image/03.jpg")));
		document.addPage(page);
		assertEquals(document.pageList.size(), 1);
	}
	
	@Test
	@Order(1)
	public void addShape() {
		assertEquals(document.setIndex(0), true);
		assertEquals(document.getPage().setIndex(0), true);
		document.cache.pressedImage = document.getImage();
		int x = (int) (document.cache.pressedImage.position.dimension.width / 2 - dimension / 2);
		int y = (int) (document.cache.pressedImage.position.dimension.height / 2 - dimension / 2);
		int width = dimension;
		int height = dimension;
		document.cache.pressedPoint = new Point(x, y);
		document.cache.releasedPoint = new Point(x + width, y + height);
		try {
			document.pattern.execute("addShape");
		} catch (Exception e) {
			logger.error("Exception " + e.getMessage());
		}
		assertNotNull(document.getShape(new Point(document.cache.pressedImage.position.dimension.width / 2, document.cache.pressedImage.position.dimension.height / 2)));
	}
	
	@Test
	@Order(2)
	public void randomMove() {
		assertEquals(document.setIndex(0), true);
		Page page = document.getPage();
		Position position = page.position;
		Point startPoint = new Point(document.cache.pressedImage.position.dimension.width / 2, document.cache.pressedImage.position.dimension.height / 2);
		Point previousPoint = startPoint;
		Point newPoint;
		int count = limit;
		while(count > 0) {
			newPoint = getRandomPoint(position);
			document.cache.pressedPoint = previousPoint;
			document.cache.pressedShape = document.getPage().getShape(document.cache.pressedPoint);
			document.cache.releasedPoint = newPoint;
			try {
				document.pattern.execute("moveShape");
			} catch (Exception e) {
				logger.error("Exception " + e.getMessage());
			}
			assertNotNull(document.getPage().getShape(newPoint));
			previousPoint = newPoint;
			count--;
		}
	}
	
	public static Point getRandomPoint(Position position) {
		Point point = new Point();
		point.x = ThreadLocalRandom.current().nextInt(0, (int)(position.dimension.width) + 1);
		point.y = ThreadLocalRandom.current().nextInt(0, (int)(position.dimension.height) + 1);
		return point;
	}
}
