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
import com.meritoki.app.desktop.retina.model.document.Shape;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DocumentAddShapeMoveShapeResizeImageTest {
	
	static Logger logger = LogManager.getLogger(DocumentAddShapeMoveShapeResizeImageTest.class.getName());
	static Document document = null;
	static int dimension = 4;

	@BeforeAll
	public static void initialize() {
		document = new Document();
		Page page = new Page();
		page.addImage(new Image(new File("./data/image/01.jpg")));
		page.addImage(new Image(new File("./data/image/02.jpg")));
		document.addPage(page);
		assertEquals(document.pageList.size(), 1);
	}
	
	@Test
	@Order(1)
	public void addShape() {
		assertEquals(document.setIndex(0), true);
		assertEquals(document.getPage().setIndex(0), true);
		document.cache.pressedImage = document.getImage();
		double x = (document.cache.pressedImage.position.center.x - dimension / 2);
		double y = (document.cache.pressedImage.position.center.y - dimension / 2);
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
		Shape shape =document.getShape(new Point(x, y));
		assertNotNull(shape);
	}
	
	@Test
	@Order(2)
	public void moveShape() {
		assertEquals(document.setIndex(0), true);
		assertEquals(document.getPage().setIndex(0), true);
		document.cache.pressedImage = document.getPage().getImage();
		assertEquals(document.getPage().setIndex(1), true);
		document.cache.releasedImage = document.getPage().getImage();
		double x = (document.cache.pressedImage.position.center.x);
		double y = (document.cache.pressedImage.position.center.y);
		document.cache.pressedPoint = new Point(x, y);
		document.cache.pressedShape = document.getPage().getShape(document.cache.pressedPoint);
		x = (int) (document.cache.releasedImage.position.center.x);
		y = (int) (document.cache.releasedImage.position.center.y);
		document.cache.releasedPoint = new Point(x, y);
		try {
			document.pattern.execute("moveShape");
		} catch (Exception e) {
			logger.error("Exception " + e.getMessage());
		}
		Shape shape =document.getShape(new Point(x, y));
		assertNotNull(shape);
	}
	
	@Test
	@Order(3)
	public void resizeImage() {
		assertEquals(document.setIndex(0), true);
		assertEquals(document.getPage().setIndex(1), true);
		document.cache.pressedPage = document.getPage();
		document.cache.pressedImage = document.getImage();
		document.cache.scaleOperator = '/';
		document.cache.scaleFactor = 1.01;
		try {
			document.pattern.execute("resizeImage");
			document.pattern.execute("resizeImage");
			document.pattern.execute("resizeImage");
			document.pattern.execute("resizeImage");
			document.pattern.execute("resizeImage");
			document.pattern.execute("resizeImage");
			document.pattern.execute("resizeImage");
			document.pattern.execute("resizeImage");
		} catch (Exception e) {
			logger.error("Exception "+e.getMessage());
		}
		int x = (int) (document.cache.pressedImage.position.center.x);
		int y = (int) (document.cache.pressedImage.position.center.y);
		assertNotNull(document.getShape(new Point(x,y)));
	}
	
	@Test
	@Order(4) 
	public void undo() {
		document.pattern.undo();
		document.pattern.undo();
		document.pattern.undo();
		document.pattern.undo();
		document.pattern.undo();
		document.pattern.undo();
		document.pattern.undo();
		document.pattern.undo();
		document.pattern.undo();
		document.pattern.undo();
	}
	
	@Test
	@Order(5) 
	public void redo() {
		document.pattern.redo();
		document.pattern.redo();
		document.pattern.redo();
		document.pattern.redo();
		document.pattern.redo();
		document.pattern.redo();
		document.pattern.redo();
		document.pattern.redo();
		document.pattern.redo();
		document.pattern.redo();
	}
	
//	@Test
//	@Order(4)
//	public void moveShapeOne() {
//		assertEquals(document.setIndex(0), true);
//		//right to left
//		assertEquals(document.getPage().setIndex(1), true);
//		document.cache.pressedImage = document.getPage().getImage();
//		assertEquals(document.getPage().setIndex(0), true);
//		document.cache.releasedImage = document.getPage().getImage();
//		double x = (document.cache.pressedImage.position.center.x);
//		double y = (document.cache.pressedImage.position.center.y);
//		document.cache.pressedPoint = new Point(x, y);
//		document.cache.pressedShape = document.getPage().getShape(document.cache.pressedPoint);
//		x = (document.cache.releasedImage.position.center.x);
//		y = (document.cache.releasedImage.position.center.y);
//		document.cache.releasedPoint = new Point(x, y);
//		try {
//			document.pattern.execute("moveShape");
//		} catch (Exception e) {
//			logger.error("Exception " + e.getMessage());
//		}
//		Shape shape =document.getShape(new Point(x, y));
//		assertNotNull(shape);
//	}
//	
//	@Test
//	@Order(5)
//	public void resizeImageOne() {
//		assertEquals(document.setIndex(0), true);
//		assertEquals(document.getPage().setIndex(0), true);
//		document.cache.pressedPage = document.getPage();
//		document.cache.pressedImage = document.getImage();
//		document.cache.scaleOperator = '/';
//		document.cache.scaleFactor = 1.01;
//		try {
//			document.pattern.execute("resizeImage");
//			document.pattern.execute("resizeImage");
//			document.pattern.execute("resizeImage");
//			document.pattern.execute("resizeImage");
//			document.pattern.execute("resizeImage");
//			document.pattern.execute("resizeImage");
//		} catch (Exception e) {
//			logger.error("Exception "+e.getMessage());
//		}
//		double x = (document.cache.pressedImage.position.center.x);
//		double y = (document.cache.pressedImage.position.center.y);
//		assertNotNull(document.getShape(new Point(x,y)));
//	}
//	
//	@Test
//	@Order(6)
//	public void moveShapeTwo() {
//		assertEquals(document.setIndex(0), true);
//		assertEquals(document.getPage().setIndex(0), true);
//		document.cache.pressedImage = document.getPage().getImage();
//		assertEquals(document.getPage().setIndex(1), true);
//		document.cache.releasedImage = document.getPage().getImage();
//		double x = (document.cache.pressedImage.position.center.x);
//		double y = (document.cache.pressedImage.position.center.y);
//		document.cache.pressedPoint = new Point(x, y);
//		document.cache.pressedShape = document.getPage().getShape(document.cache.pressedPoint);
//		x = (document.cache.releasedImage.position.center.x);
//		y = (document.cache.releasedImage.position.center.y);
//		document.cache.releasedPoint = new Point(x, y);
//		try {
//			document.pattern.execute("moveShape");
//		} catch (Exception e) {
//			logger.error("Exception " + e.getMessage());
//		}
//		Shape shape =document.getShape(new Point(x, y));
//		assertNotNull(shape);
//	}
//	
//	@Test
//	@Order(7)
//	public void resizeImageTwo() {
//		assertEquals(document.setIndex(0), true);
//		assertEquals(document.getPage().setIndex(1), true);
//		document.cache.pressedPage = document.getPage();
//		document.cache.pressedImage = document.getImage();
//		double x = (document.cache.pressedImage.position.center.x);
//		double y = (document.cache.pressedImage.position.center.y);
//		document.cache.scaleOperator = '/';
//		document.cache.scaleFactor = 1.01;
//		try {
//			document.pattern.execute("resizeImage");
//			document.pattern.execute("resizeImage");
//			document.pattern.execute("resizeImage");
//			document.pattern.execute("resizeImage");
//			document.pattern.execute("resizeImage");
//			document.pattern.execute("resizeImage");
//		} catch (Exception e) {
//			logger.error("Exception "+e.getMessage());
//		}
//		x = (document.cache.pressedImage.position.center.x);
//		y = (document.cache.pressedImage.position.center.y);
//		Shape shape =document.getShape(new Point(x, y));
//		assertNotNull(shape);
//	}
//	
//	@Test
//	@Order(8)
//	public void save() {
//		DocumentController.save(new java.io.File("./test/document-add-move-shape-resize-image-test.json"), document);
//	}

}
