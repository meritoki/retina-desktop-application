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

import com.meritoki.app.desktop.retina.controller.document.DocumentController;
import com.meritoki.app.desktop.retina.model.document.Document;
import com.meritoki.app.desktop.retina.model.document.Image;
import com.meritoki.app.desktop.retina.model.document.Page;
import com.meritoki.app.desktop.retina.model.document.Point;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DocumentShapeMoveTest {

	static Logger logger = LogManager.getLogger(DocumentShapeMoveTest.class.getName());
	static Document document = null;
	static String pageZeroUUID = null;
	static String pageOneUUID = null;
	static String pageTwoUUID = null;

	static int dimension = 256;

	@BeforeAll
	public static void initialize() {
		document = new Document();
		Page page = new Page();
		page = new Page(new Image(new File("./data/image/01.jpg")));
		pageZeroUUID = page.uuid;
		document.addPage(page);
		page = new Page();
		page.addImage(new Image(new File("./data/image/02.jpg")));
		page.addImage(new Image(new File("./data/image/03.jpg")));
		pageOneUUID = page.uuid;
		document.addPage(page);
		page = new Page();
		page.addImage(new Image(new File("./data/image/04.jpg")));
		page.addImage(new Image(new File("./data/image/05.jpg")));
		page.addImage(new Image(new File("./data/image/06.jpg")));
		pageTwoUUID = page.uuid;
		document.addPage(page);
		assertEquals(document.pageList.size(), 3);
	}

	@Test
	@Order(1)
	public void addShapes() {
		assertEquals(document.setIndex(0), true);
		assertEquals(document.getPage().setIndex(0), true);
		document.cache.pressedImage = document.getImage();
		int x = (int) (document.cache.pressedImage.position.absoluteDimension.width / 2 - dimension / 2);
		int y = (int) (document.cache.pressedImage.position.absoluteDimension.height / 2 - dimension / 2);
		int width = dimension;
		int height = dimension;
		document.cache.pressedPoint = new Point(x, y);
		document.cache.releasedPoint = new Point(x + width, y + height);
		try {
			document.pattern.execute("addShape");
		} catch (Exception e) {
			logger.error("Exception " + e.getMessage());
		}
		assertEquals(document.setIndex(1), true);
		document.cache.pressedImage = document.getImage();
		x = (int) (document.cache.pressedImage.position.absoluteDimension.width / 2 - dimension / 2);
		y = (int) (document.cache.pressedImage.position.absoluteDimension.height / 2 - dimension / 2);
		width = dimension;
		height = dimension;
		document.cache.pressedPoint = new Point(x, y);
		document.cache.releasedPoint = new Point(x + width, y + height);
		try {
			document.pattern.execute("addShape");
		} catch (Exception e) {
			logger.error("Exception " + e.getMessage());
		}

		assertEquals(document.setIndex(2), true);
		document.cache.pressedImage = document.getImage();
		x = (int) (document.cache.pressedImage.position.absoluteDimension.width / 2 - dimension / 2);
		y = (int) (document.cache.pressedImage.position.absoluteDimension.height / 2 - dimension / 2);
		width = dimension;
		height = dimension;
		document.cache.pressedPoint = new Point(x, y);
		document.cache.releasedPoint = new Point(x + width, y + height);
		try {
			document.pattern.execute("addShape");
		} catch (Exception e) {
			logger.error("Exception " + e.getMessage());
		}
	}

	@Test
	@Order(2)
	public void moveShapes() {
		assertEquals(document.setIndex(0), true);
		assertEquals(document.getPage().setIndex(0), true);
		int x = (int) (document.cache.pressedImage.position.absoluteDimension.width / 2);
		int y = (int) (document.cache.pressedImage.position.absoluteDimension.height / 2);
		document.cache.pressedPoint = new Point(x, y);
		document.cache.pressedShape = document.getPage().getShape(document.cache.pressedPoint);
		x = (int) (document.cache.pressedImage.position.absoluteDimension.width / 2);
		y = (int) (document.cache.pressedImage.position.absoluteDimension.height / 4);
		document.cache.releasedPoint = new Point(x, y);
		try {
			document.pattern.execute("moveShape");
		} catch (Exception e) {
			logger.error("Exception " + e.getMessage());
		}
		assertNotNull(document.getPage().getShape(new Point(x,y)));
	}
	
	@Test
	@Order(3) 
	public void undoMoveShape() {
		document.pattern.undo();
	}
	
	@Test
	@Order(4)
	public void verifyShapePosition() {
		assertEquals(document.setIndex(0), true);
		assertEquals(document.getPage().setIndex(0), true);
		int x = (int) (document.cache.pressedImage.position.absoluteDimension.width / 2);
		int y = (int) (document.cache.pressedImage.position.absoluteDimension.height / 2);
		assertNotNull(document.getPage().getShape(new Point(x,y)));
	}
//	
//	@Test
//	@Order(3)
//	public void save() {
//		DocumentController.save(new java.io.File("./document-shape-move-test.json"), document);
//	}
}
