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

import com.meritoki.app.desktop.retina.model.Model;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DocumentAddShapeMoveShapeShiftImageTest {

	static Logger logger = LogManager.getLogger(DocumentAddShapeMoveShapeShiftImageTest.class.getName());
	static Model model = new Model();
	static String pageZeroUUID = null;
	static String pageOneUUID = null;
	static String pageTwoUUID = null;

	static int dimension = 4;

	@BeforeAll
	public static void initialize() {
		model.document = new Document();
		Page page = new Page();
		page = new Page(new Image(new File("./test/img/test.png")));
		pageZeroUUID = page.uuid;
		model.document.addPage(page);
		page = new Page();
		page.addImage(new Image(new File("./test/img/test.png")));
		page.addImage(new Image(new File("./test/img/test.png")));
		pageOneUUID = page.uuid;
		model.document.addPage(page);
		page = new Page();
		page.addImage(new Image(new File("./test/img/test.png")));
		page.addImage(new Image(new File("./test/img/test.png")));
		pageTwoUUID = page.uuid;
		model.document.addPage(page);
		assertEquals(model.document.pageList.size(), 3);
	}

	@Test
	@Order(1)
	public void addShape() {
		//Page Zero
		assertEquals(model.document.setIndex(0), true);
		model.document.getPage().getBufferedImage(model);
		assertEquals(model.document.getPage().setIndex(0), true);
		model.system.pressedImage = model.document.getImage();
		double x = (model.system.pressedImage.position.dimension.width / 2 - dimension / 2);
		double y = (model.system.pressedImage.position.dimension.height / 2 - dimension / 2);
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
		x = (model.system.pressedImage.position.center.x);
		y = (model.system.pressedImage.position.center.y);
		assertNotNull(model.document.getShape(new Point(x,y)));
		//Page One
		assertEquals(model.document.setIndex(1), true);
		model.document.getPage().getBufferedImage(model);
		assertEquals(model.document.getPage().setIndex(0),true);
		model.system.pressedImage = model.document.getImage();
		x = (model.system.pressedImage.position.center.x- dimension / 2);
		y = (model.system.pressedImage.position.center.y - dimension / 2);
		width = dimension;
		height = dimension;
		model.cache.pressedPoint = new Point(x, y);
		model.cache.releasedPoint = new Point(x + width, y + height);
		try {
			model.cache.pressedPageUUID = model.document.getPage().uuid;
			model.cache.pressedImageUUID = model.system.pressedImage.uuid;
			model.pattern.execute("addShape");
		} catch (Exception e) {
			logger.error("Exception " + e.getMessage());
		}
		x = (model.system.pressedImage.position.center.x);
		y = (model.system.pressedImage.position.center.y);
		assertNotNull(model.document.getShape(new Point(x,y)));
		//Page Two
		assertEquals(model.document.setIndex(2), true);
		model.document.getPage().getBufferedImage(model);
		assertEquals(model.document.getPage().setIndex(1),true);
		model.system.pressedImage = model.document.getPage().getImage();
		x = (model.system.pressedImage.position.center.x - dimension / 2);
		y = (model.system.pressedImage.position.center.y - dimension / 2);
		width = dimension;
		height = dimension;
		model.cache.pressedPoint = new Point(x, y);
		model.cache.releasedPoint = new Point(x + width, y + height);
		try {
			model.cache.pressedPageUUID = model.document.getPage().uuid;
			model.cache.pressedImageUUID = model.system.pressedImage.uuid;
			model.pattern.execute("addShape");
		} catch (Exception e) {
			logger.error("Exception " + e.getMessage());
		}
		model.system.pressedImage = model.document.getPage().getImage();
		x = (model.system.pressedImage.position.center.x);
		y = (model.system.pressedImage.position.center.y);
		assertNotNull(model.document.getShape(new Point(x,y)));
	}
//	
////	@Test
////	@Order(2)
////	public void save() {
////		DocumentController.save(new java.io.File("./test/document-move-shape-test-a.json"), document);
////	}
//
	@Test
	@Order(2)
	public void moveShape() {
		logger.info("single page");
		assertEquals(model.document.setIndex(0), true);
		model.document.getPage().getBufferedImage(model);
		assertEquals(model.document.getPage().setIndex(0), true);
		model.system.pressedImage = model.document.getPage().getImage();
		model.system.releasedImage = model.document.getPage().getImage();
		double x = (model.system.pressedImage.position.center.x);
		double y = (model.system.pressedImage.position.center.y);
		model.cache.pressedPoint = new Point(x, y);
		model.system.pressedShape = model.document.getPage().getShape(model.cache.pressedPoint);
		x = (model.system.pressedImage.position.dimension.width / 2);
		y = (model.system.pressedImage.position.dimension.height / 4);
		model.cache.releasedPoint = new Point(x, y);
		try {
			model.cache.pressedPageUUID = model.document.getPage().uuid;
			model.cache.pressedShapeUUID = model.system.pressedShape.uuid;
			model.cache.pressedImageUUID = model.system.pressedImage.uuid;
			model.cache.releasedImageUUID = model.system.releasedImage.uuid;
			model.pattern.execute("moveShape");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception " + e.getMessage());
		}
		assertNotNull(model.document.getPage().getShape(new Point(x,y)));
		logger.info("left to right");
		assertEquals(model.document.setIndex(1), true);
		model.document.getPage().getBufferedImage(model);
		assertEquals(model.document.getPage().setIndex(0), true);
		model.system.pressedImage = model.document.getPage().getImage();
		assertEquals(model.document.getPage().setIndex(1), true);
		model.system.releasedImage = model.document.getPage().getImage();
		x = (model.system.pressedImage.position.center.x);
		y = (model.system.pressedImage.position.center.y);
		model.cache.pressedPoint = new Point(x, y);
		model.system.pressedShape = model.document.getPage().getShape(model.cache.pressedPoint);
		x = (model.system.releasedImage.position.center.x); 
		y = (model.system.releasedImage.position.center.y);
		model.cache.releasedPoint = new Point(x, y);
		try {
			model.cache.pressedPageUUID = model.document.getPage().uuid;
			model.cache.pressedShapeUUID = model.system.pressedShape.uuid;
			model.cache.pressedImageUUID = model.system.pressedImage.uuid;
			model.cache.releasedImageUUID = model.system.releasedImage.uuid;
			model.pattern.execute("moveShape");
		} catch (Exception e) {
			logger.error("Exception " + e.getMessage());
		}
		assertNotNull(model.document.getPage().getShape(new Point(x,y)));
		logger.info("right to left");
		assertEquals(model.document.setIndex(2), true);
		model.document.getPage().getBufferedImage(model);
		assertEquals(model.document.getPage().setIndex(1), true);
		model.system.pressedImage = model.document.getPage().getImage();
		assertEquals(model.document.getPage().setIndex(0), true);
		model.system.releasedImage = model.document.getPage().getImage();
		x = (model.system.pressedImage.position.center.x);
		y = (model.system.pressedImage.position.center.y);
		model.cache.pressedPoint = new Point(x, y);
		model.system.pressedShape = model.document.getPage().getShape(model.cache.pressedPoint);
		x = (model.system.releasedImage.position.center.x);
		y = (model.system.releasedImage.position.center.y);
		model.cache.releasedPoint = new Point(x, y);
		try {
			model.cache.pressedPageUUID = model.document.getPage().uuid;
			model.cache.pressedShapeUUID = model.system.pressedShape.uuid;
			model.cache.pressedImageUUID = model.system.pressedImage.uuid;
			model.cache.releasedImageUUID = model.system.releasedImage.uuid;
			model.pattern.execute("moveShape");
		} catch (Exception e) {
			logger.error("Exception " + e.getMessage());
		}
		assertNotNull(model.document.getPage().getShape(new Point(x,y)));
	}
//	
////	@Test
////	@Order(4)
////	public void saveMove() {
////		DocumentController.save(new java.io.File("./test/document-move-shape-test-b.json"), document);
////	}
//	
	@Test
	@Order(3)
	public void shiftImage() {
		logger.info("single page");
		assertEquals(model.document.setIndex(0), true);
		assertEquals(model.document.getPage().setIndex(0), true);
		model.system.pressedImage = model.document.getImage();
		model.cache.shiftOperator = '+';
		model.cache.shiftFactor = 10;
		try {
			model.cache.pressedPageUUID = model.document.getPage().uuid;
			model.cache.pressedImageUUID = model.system.pressedImage.uuid;
			model.pattern.execute("shiftImage");
			model.pattern.execute("shiftImage");
			model.pattern.execute("shiftImage");
			model.pattern.execute("shiftImage");
			model.pattern.execute("shiftImage");
			model.pattern.execute("shiftImage");
			model.pattern.execute("shiftImage");
			model.pattern.execute("shiftImage");
		} catch (Exception e) {
			logger.error("Exception "+e.getMessage());
		}
		double x = (int)(model.system.pressedImage.position.dimension.width/2);
		double y = (int)(model.system.pressedImage.position.point.y+model.system.pressedImage.position.dimension.height/4);
		assertNotNull(model.document.getShape(new Point(x,y)));
		logger.info("right");
		assertEquals(model.document.setIndex(1), true);
		assertEquals(model.document.getPage().setIndex(1), true);
		model.system.pressedImage = model.document.getImage();
		model.cache.shiftOperator = '+';
		model.cache.shiftFactor = 10;
		try {
			model.cache.pressedPageUUID = model.document.getPage().uuid;
			model.cache.pressedImageUUID = model.system.pressedImage.uuid;
			model.pattern.execute("shiftImage");
			model.pattern.execute("shiftImage");
			model.pattern.execute("shiftImage");
			model.pattern.execute("shiftImage");
			model.pattern.execute("shiftImage");
			model.pattern.execute("shiftImage");
			model.pattern.execute("shiftImage");
			model.pattern.execute("shiftImage");
		} catch (Exception e) {
			logger.error("Exception "+e.getMessage());
		}
		x = model.system.pressedImage.position.center.x;
		y = model.system.pressedImage.position.center.y;
		assertNotNull(model.system.pressedImage.getShape(new Point(x,y)));
		logger.info("left");
		assertEquals(model.document.setIndex(2), true);
		assertEquals(model.document.getPage().setIndex(0), true);
		model.system.pressedImage = model.document.getImage();
		model.cache.shiftOperator = '+';
		model.cache.shiftFactor = 10;
		try {
			model.cache.pressedPageUUID = model.document.getPage().uuid;
			model.cache.pressedImageUUID = model.system.pressedImage.uuid;
			model.pattern.execute("shiftImage");
			model.pattern.execute("shiftImage");
			model.pattern.execute("shiftImage");
			model.pattern.execute("shiftImage");
			model.pattern.execute("shiftImage");
			model.pattern.execute("shiftImage");
			model.pattern.execute("shiftImage");
			model.pattern.execute("shiftImage");
		} catch (Exception e) {
			logger.error("Exception "+e.getMessage());
		}
		x = model.system.pressedImage.position.center.x;
		y = model.system.pressedImage.position.center.y;
		assertNotNull(model.system.pressedImage.getShape(new Point(x,y)));
	}
//	
//	@Test
//	@Order(4)
//	public void undo() {
//		
//	}
//	
//	@Test
//	@Order(5)
//	public void redo() {
//		
//	}
	
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
//		model.pattern.undo();
//		model.pattern.undo();
//		model.pattern.undo();
//	}
//	
//	@Test
//	@Order(5)
//	public void verifyShapePosition() {
//		assertEquals(model.document.setIndex(0), true);
//		assertEquals(model.document.getPage().setIndex(0), true);
//		model.system.pressedImage = model.document.getPage().getImage();
//		int x = (int) (model.system.pressedImage.position.absoluteDimension.width / 2);
//		int y = (int) (model.system.pressedImage.position.absoluteDimension.height / 2);
//		assertNotNull(model.document.getPage().getShape(new Point(x,y)));
//	}
//	
//	@Test
//	@Order(3)
//	public void save() {
//		DocumentController.save(new java.io.File("./document-shape-move-test.json"), document);
//	}
}
