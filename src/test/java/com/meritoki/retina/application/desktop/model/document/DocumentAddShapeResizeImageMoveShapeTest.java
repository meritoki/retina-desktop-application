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

import com.meritoki.app.desktop.retina.model.document.Document;
import com.meritoki.app.desktop.retina.model.document.Image;
import com.meritoki.app.desktop.retina.model.document.Page;
import com.meritoki.app.desktop.retina.model.document.Point;
import com.meritoki.app.desktop.retina.model.document.Shape;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DocumentAddShapeResizeImageMoveShapeTest {

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
		x = (int) (document.cache.pressedImage.position.dimension.width / 2);
		y = (int) (document.cache.pressedImage.position.dimension.height / 2);
		Shape shape =document.getShape(new Point(x, y));
		logger.info("shape="+shape);
		assertNotNull(shape);
	}
	
	@Test
	@Order(2)
	public void resizeImage() {
		assertEquals(document.setIndex(0), true);
		assertEquals(document.getPage().setIndex(0), true);
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
		} catch (Exception e) {
			logger.error("Exception "+e.getMessage());
		}
		int x = (int) (document.cache.pressedImage.position.point.x+document.cache.pressedImage.position.dimension.width / 2);
		int y = (int) (document.cache.pressedImage.position.point.y+document.cache.pressedImage.position.dimension.height / 2);
		logger.info("shapeList="+document.getShapeList());
		Shape shape =document.getShape(new Point(x, y));
		logger.info("shapeList="+document.getShapeList());
		logger.info("shape="+shape);
		assertNotNull(shape);
	}
	
	@Test
	@Order(3)
	public void moveShape() {
		assertEquals(document.setIndex(0), true);
		assertEquals(document.getPage().setIndex(0), true);
		document.cache.pressedImage = document.getPage().getImage();
		assertEquals(document.getPage().setIndex(1), true);
		document.cache.releasedImage = document.getPage().getImage();
		int x = (int) (document.cache.pressedImage.position.dimension.width / 2);
		int y = (int) (document.cache.pressedImage.position.dimension.height / 2);
		document.cache.pressedPoint = new Point(x, y);
		logger.info("pressedImage="+document.cache.pressedImage);
		logger.info("pressedPoint="+document.cache.pressedPoint);
		document.cache.pressedShape = document.getPage().getShape(document.cache.pressedPoint);
		logger.info("pressedShape="+document.cache.pressedShape);
		assertNotNull(document.cache.pressedShape);
		x = (int) (document.cache.releasedImage.position.point.x+document.cache.releasedImage.position.dimension.width / 2);
		y = (int) (document.cache.releasedImage.position.point.y+document.cache.releasedImage.position.dimension.height / 2);
		document.cache.releasedPoint = new Point(x, y);
		logger.info("releasedImage="+document.cache.releasedImage);
		logger.info("releasedPoint="+document.cache.releasedPoint);
		try {
			document.pattern.execute("moveShape");
		} catch (Exception e) {
			logger.error("Exception " + e.getMessage());
		}
		Shape shape =document.getShape(new Point(x, y));
		logger.info("shapeList="+document.getShapeList());
		logger.info("shape="+shape);
		assertNotNull(shape);
		assertEquals(document.cache.pressedShape.position.dimension.width, shape.position.dimension.width);
		assertEquals(document.cache.pressedShape.position.dimension.height, shape.position.dimension.height);
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
//	@Order(4)
//	public void resizeImageOne() {
//		assertEquals(document.setIndex(0), true);
//		assertEquals(document.getPage().setIndex(1), true);
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
//			document.pattern.execute("resizeImage");
//		} catch (Exception e) {
//			logger.error("Exception "+e.getMessage());
//		}
//		int x = (int) (document.cache.pressedImage.position.point.x+document.cache.pressedImage.position.dimension.width / 2);
//		int y = (int) (document.cache.pressedImage.position.point.y+document.cache.pressedImage.position.dimension.height / 2);
//		logger.info("shapeList="+document.getShapeList());
//		Shape shape =document.getShape(new Point(x, y));
//		logger.info("shapeList="+document.getShapeList());
//		logger.info("shape="+shape);
//		assertNotNull(shape);
//	}
//	
//	@Test
//	@Order(5)
//	public void moveShapeOne() {
//		assertEquals(document.setIndex(0), true);
//		assertEquals(document.getPage().setIndex(1), true);
//		document.cache.pressedImage = document.getPage().getImage();
//		assertEquals(document.getPage().setIndex(0), true);
//		document.cache.releasedImage = document.getPage().getImage();
//		int x = (int) (document.cache.pressedImage.position.point.x+document.cache.pressedImage.position.dimension.width / 2);
//		int y = (int) (document.cache.pressedImage.position.point.y+document.cache.pressedImage.position.dimension.height / 2);
//		document.cache.pressedPoint = new Point(x, y);
//		logger.info("pressedImage="+document.cache.pressedImage);
//		logger.info("pressedPoint="+document.cache.pressedPoint);
//		document.cache.pressedShape = document.getPage().getShape(document.cache.pressedPoint);
//		logger.info("pressedShape="+document.cache.pressedShape);
//		assertNotNull(document.cache.pressedShape);
//		x = (int) (document.cache.releasedImage.position.point.x+document.cache.releasedImage.position.dimension.width / 2);
//		y = (int) (document.cache.releasedImage.position.point.y+document.cache.releasedImage.position.dimension.height / 2);
//		document.cache.releasedPoint = new Point(x, y);
//		logger.info("releasedImage="+document.cache.releasedImage);
//		logger.info("releasedPoint="+document.cache.releasedPoint);
//		try {
//			document.pattern.execute("moveShape");
//		} catch (Exception e) {
//			logger.error("Exception " + e.getMessage());
//		}
//		Shape shape =document.getShape(new Point(x, y));
//		logger.info("shapeList="+document.getShapeList());
//		logger.info("shape="+shape);
//		assertNotNull(shape);
//		assertEquals(document.cache.pressedShape.position.dimension.width, shape.position.dimension.width);
//		assertEquals(document.cache.pressedShape.position.dimension.height, shape.position.dimension.height);
//	}
}
