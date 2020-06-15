package com.meritoki.app.desktop.retina.model.document;

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
public class DocumentAddShapeMoveShapeShiftImageTest {

	static Logger logger = LogManager.getLogger(DocumentAddShapeMoveShapeShiftImageTest.class.getName());
	static Document document = null;
	static String pageZeroUUID = null;
	static String pageOneUUID = null;
	static String pageTwoUUID = null;

	static int dimension = 4;

	@BeforeAll
	public static void initialize() {
		document = new Document();
		Page page = new Page();
		page = new Page(new Image(new File("./data/image/01.jpg")));
		pageZeroUUID = page.uuid;
		document.addPage(page);
		page = new Page();
		page.addImage(new Image(new File("./data/image/01.jpg")));
		page.addImage(new Image(new File("./data/image/02.jpg")));
		pageOneUUID = page.uuid;
		document.addPage(page);
		page = new Page();
		page.addImage(new Image(new File("./data/image/01.jpg")));
		page.addImage(new Image(new File("./data/image/02.jpg")));
		pageTwoUUID = page.uuid;
		document.addPage(page);
		assertEquals(document.pageList.size(), 3);
	}

	@Test
	@Order(1)
	public void addShape() {
		//Page Zero
		assertEquals(document.setIndex(0), true);
		assertEquals(document.getPage().setIndex(0), true);
		document.cache.pressedImage = document.getImage();
		double x = (document.cache.pressedImage.position.dimension.width / 2 - dimension / 2);
		double y = (document.cache.pressedImage.position.dimension.height / 2 - dimension / 2);
		int width = dimension;
		int height = dimension;
		document.cache.pressedPoint = new Point(x, y);
		document.cache.releasedPoint = new Point(x + width, y + height);
		try {
			document.pattern.execute("addShape");
		} catch (Exception e) {
			logger.error("Exception " + e.getMessage());
		}
		x = (document.cache.pressedImage.position.center.x);
		y = (document.cache.pressedImage.position.center.y);
		assertNotNull(document.getShape(new Point(x,y)));
		//Page One
		assertEquals(document.setIndex(1), true);
		assertEquals(document.getPage().setIndex(0),true);
		document.cache.pressedImage = document.getImage();
		x = (document.cache.pressedImage.position.center.x- dimension / 2);
		y = (document.cache.pressedImage.position.center.y - dimension / 2);
		width = dimension;
		height = dimension;
		document.cache.pressedPoint = new Point(x, y);
		document.cache.releasedPoint = new Point(x + width, y + height);
		try {
			document.pattern.execute("addShape");
		} catch (Exception e) {
			logger.error("Exception " + e.getMessage());
		}
		x = (document.cache.pressedImage.position.center.x);
		y = (document.cache.pressedImage.position.center.y);
		assertNotNull(document.getShape(new Point(x,y)));
		//Page Two
		assertEquals(document.setIndex(2), true);
		assertEquals(document.getPage().setIndex(1),true);
		document.cache.pressedImage = document.getPage().getImage();
		x = (document.cache.pressedImage.position.center.x - dimension / 2);
		y = (document.cache.pressedImage.position.center.y - dimension / 2);
		width = dimension;
		height = dimension;
		document.cache.pressedPoint = new Point(x, y);
		document.cache.releasedPoint = new Point(x + width, y + height);
		try {
			document.pattern.execute("addShape");
		} catch (Exception e) {
			logger.error("Exception " + e.getMessage());
		}
		document.cache.pressedImage = document.getPage().getImage();
		x = (document.cache.pressedImage.position.center.x);
		y = (document.cache.pressedImage.position.center.y);
		assertNotNull(document.getShape(new Point(x,y)));
	}
	
//	@Test
//	@Order(2)
//	public void save() {
//		DocumentController.save(new java.io.File("./test/document-move-shape-test-a.json"), document);
//	}

	@Test
	@Order(2)
	public void moveShape() {
		logger.info("single page");
		assertEquals(document.setIndex(0), true);
		assertEquals(document.getPage().setIndex(0), true);
		document.cache.pressedImage = document.getPage().getImage();
		double x = (document.cache.pressedImage.position.center.x);
		double y = (document.cache.pressedImage.position.center.y);
		document.cache.pressedPoint = new Point(x, y);
		document.cache.pressedShape = document.getPage().getShape(document.cache.pressedPoint);
		x = (document.cache.pressedImage.position.dimension.width / 2);
		y = (document.cache.pressedImage.position.dimension.height / 4);
		document.cache.releasedPoint = new Point(x, y);
		try {
			document.pattern.execute("moveShape");
		} catch (Exception e) {
			logger.error("Exception " + e.getMessage());
		}
		assertNotNull(document.getPage().getShape(new Point(x,y)));
		logger.info("left to right");
		assertEquals(document.setIndex(1), true);
		assertEquals(document.getPage().setIndex(0), true);
		document.cache.pressedImage = document.getPage().getImage();
		assertEquals(document.getPage().setIndex(1), true);
		document.cache.releasedImage = document.getPage().getImage();
		x = (document.cache.pressedImage.position.center.x);
		y = (document.cache.pressedImage.position.center.y);
		document.cache.pressedPoint = new Point(x, y);
		document.cache.pressedShape = document.getPage().getShape(document.cache.pressedPoint);
		x = (document.cache.releasedImage.position.center.x); 
		y = (document.cache.releasedImage.position.center.y);
		document.cache.releasedPoint = new Point(x, y);
		try {
			document.pattern.execute("moveShape");
		} catch (Exception e) {
			logger.error("Exception " + e.getMessage());
		}
		assertNotNull(document.getPage().getShape(new Point(x,y)));
		logger.info("right to left");
		assertEquals(document.setIndex(2), true);
		assertEquals(document.getPage().setIndex(1), true);
		document.cache.pressedImage = document.getPage().getImage();
		assertEquals(document.getPage().setIndex(0), true);
		document.cache.releasedImage = document.getPage().getImage();
		x = (document.cache.pressedImage.position.center.x);
		y = (document.cache.pressedImage.position.center.y);
		document.cache.pressedPoint = new Point(x, y);
		document.cache.pressedShape = document.getPage().getShape(document.cache.pressedPoint);
		x = (document.cache.releasedImage.position.center.x);
		y = (document.cache.releasedImage.position.center.y);
		document.cache.releasedPoint = new Point(x, y);
		try {
			document.pattern.execute("moveShape");
		} catch (Exception e) {
			logger.error("Exception " + e.getMessage());
		}
		assertNotNull(document.getPage().getShape(new Point(x,y)));
	}
	
//	@Test
//	@Order(4)
//	public void saveMove() {
//		DocumentController.save(new java.io.File("./test/document-move-shape-test-b.json"), document);
//	}
	
	@Test
	@Order(3)
	public void shiftImage() {
		logger.info("single page");
		assertEquals(document.setIndex(0), true);
		assertEquals(document.getPage().setIndex(0), true);
		document.cache.pressedImage = document.getImage();
		document.cache.shiftOperator = '+';
		document.cache.shiftFactor = 10;
		try {
			document.pattern.execute("shiftImage");
			document.pattern.execute("shiftImage");
			document.pattern.execute("shiftImage");
			document.pattern.execute("shiftImage");
			document.pattern.execute("shiftImage");
			document.pattern.execute("shiftImage");
			document.pattern.execute("shiftImage");
			document.pattern.execute("shiftImage");
		} catch (Exception e) {
			logger.error("Exception "+e.getMessage());
		}
		double x = (int)(document.cache.pressedImage.position.dimension.width/2);
		double y = (int)(document.cache.pressedImage.position.point.y+document.cache.pressedImage.position.dimension.height/4);
		assertNotNull(document.getShape(new Point(x,y)));
		logger.info("right");
		assertEquals(document.setIndex(1), true);
		assertEquals(document.getPage().setIndex(1), true);
		document.cache.pressedImage = document.getImage();
		document.cache.shiftOperator = '+';
		document.cache.shiftFactor = 10;
		try {
			document.pattern.execute("shiftImage");
			document.pattern.execute("shiftImage");
			document.pattern.execute("shiftImage");
			document.pattern.execute("shiftImage");
			document.pattern.execute("shiftImage");
			document.pattern.execute("shiftImage");
			document.pattern.execute("shiftImage");
			document.pattern.execute("shiftImage");
		} catch (Exception e) {
			logger.error("Exception "+e.getMessage());
		}
		x = document.cache.pressedImage.position.center.x;
		y = document.cache.pressedImage.position.center.y;
		assertNotNull(document.cache.pressedImage.getShape(new Point(x,y)));
		logger.info("left");
		assertEquals(document.setIndex(2), true);
		assertEquals(document.getPage().setIndex(0), true);
		document.cache.pressedImage = document.getImage();
		document.cache.shiftOperator = '+';
		document.cache.shiftFactor = 10;
		try {
			document.pattern.execute("shiftImage");
			document.pattern.execute("shiftImage");
			document.pattern.execute("shiftImage");
			document.pattern.execute("shiftImage");
			document.pattern.execute("shiftImage");
			document.pattern.execute("shiftImage");
			document.pattern.execute("shiftImage");
			document.pattern.execute("shiftImage");
		} catch (Exception e) {
			logger.error("Exception "+e.getMessage());
		}
		x = document.cache.pressedImage.position.center.x;
		y = document.cache.pressedImage.position.center.y;
		assertNotNull(document.cache.pressedImage.getShape(new Point(x,y)));
	}
	
	@Test
	@Order(4)
	public void undo() {
		
	}
	
	@Test
	@Order(5)
	public void redo() {
		
	}
	
//	@Test
//	@Order(6)
//	public void saveShift() {
//		DocumentController.save(new java.io.File("./test/document-move-shape-test-c.json"), document);
//	}
	
	
	
//	@Test
//	@Order(3)
//	public void save() {
//		DocumentController.save(new java.io.File("./test/document-move-shape-test.json"), document);
//	}
//	
//	@Test
//	@Order(4) 
//	public void undoMoveShape() {
//		document.pattern.undo();
//		document.pattern.undo();
//		document.pattern.undo();
//	}
//	
//	@Test
//	@Order(5)
//	public void verifyShapePosition() {
//		assertEquals(document.setIndex(0), true);
//		assertEquals(document.getPage().setIndex(0), true);
//		document.cache.pressedImage = document.getPage().getImage();
//		int x = (int) (document.cache.pressedImage.position.absoluteDimension.width / 2);
//		int y = (int) (document.cache.pressedImage.position.absoluteDimension.height / 2);
//		assertNotNull(document.getPage().getShape(new Point(x,y)));
//	}
//	
//	@Test
//	@Order(3)
//	public void save() {
//		DocumentController.save(new java.io.File("./document-shape-move-test.json"), document);
//	}
}
