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
public class DocumentAddShapeResizeImageMoveShapeTest {

	static Logger logger = LogManager.getLogger(DocumentAddShapeMoveShapeResizeImageTest.class.getName());
	static Model model = new Model();
	static int dimension = 16;

	@BeforeAll
	public static void initialize() {
		model.document = new Document();
		Page page = new Page();
		page.addImage(new Image(new File("./data/image/01.jpg")));
		page.addImage(new Image(new File("./data/image/02.jpg")));
		model.document.addPage(page);
		assertEquals(model.document.pageList.size(), 1);
	}
	
	@Test
	@Order(1)
	public void addShape() {
		assertEquals(model.document.setIndex(0), true);
		assertEquals(model.document.getPage().setIndex(0), true);
		model.system.pressedImage = model.document.getImage();
		double x = (model.system.pressedImage.position.center.x - dimension / 2);
		double y = (model.system.pressedImage.position.center.y - dimension / 2);
		int width = dimension;
		int height = dimension;
		model.cache.pressedPoint = new Point(x, y);
		model.cache.releasedPoint = new Point(x + width, y + height);
		try {
			model.pattern.execute("addShape");
		} catch (Exception e) {
			logger.error("Exception " + e.getMessage());
		}
		x = (model.system.pressedImage.position.center.x);
		y = (model.system.pressedImage.position.center.y);
		Shape shape =model.document.getShape(new Point(x, y));
		logger.info("addShape() pressedImage="+model.system.pressedImage);
		logger.info("addShape() shape="+shape);
		assertNotNull(shape);
	}
	
	@Test
	@Order(2)
	public void resizeImage() {
		assertEquals(model.document.setIndex(0), true);
		assertEquals(model.document.getPage().setIndex(0), true);
		model.system.pressedPage = model.document.getPage();
		model.system.pressedImage = model.document.getImage();
		model.cache.scaleOperator = '/';
		model.cache.scaleFactor = 1.01;
		try {
			model.pattern.execute("resizeImage");
			model.pattern.execute("resizeImage");
			model.pattern.execute("resizeImage");
			model.pattern.execute("resizeImage");
			model.pattern.execute("resizeImage");
			model.pattern.execute("resizeImage");
			model.pattern.execute("resizeImage");
		} catch (Exception e) {
			logger.error("Exception "+e.getMessage());
		}
		double x = (model.system.pressedImage.position.center.x);
		double y = (model.system.pressedImage.position.center.y);
		logger.info("addShape() pressedImage="+model.system.pressedImage);
		logger.info("shapeList="+model.document.getShapeList());
		Shape shape = model.document.getShape(new Point(x, y));
		logger.info("shapeList="+model.document.getShapeList());
		logger.info("shape="+shape);
		assertNotNull(shape);
	}
	
	@Test
	@Order(3)
	public void moveShape() {
		assertEquals(model.document.setIndex(0), true);
		assertEquals(model.document.getPage().setIndex(0), true);
		model.system.pressedImage = model.document.getPage().getImage();
		assertEquals(model.document.getPage().setIndex(1), true);
		model.system.releasedImage = model.document.getPage().getImage();
		double x = (model.system.pressedImage.position.center.x);
		double y = (model.system.pressedImage.position.center.y);
		model.cache.pressedPoint = new Point(x, y);
		logger.info("pressedImage="+model.system.pressedImage);
		logger.info("pressedPoint="+model.cache.pressedPoint);
		model.system.pressedShape = model.document.getPage().getShape(model.cache.pressedPoint);
		logger.info("pressedShape="+model.system.pressedShape);
		assertNotNull(model.system.pressedShape);
		x = (model.system.releasedImage.position.center.x);
		y = (model.system.releasedImage.position.center.y);
		model.cache.releasedPoint = new Point(x, y);
		logger.info("releasedImage="+model.system.releasedImage);
		logger.info("releasedPoint="+model.cache.releasedPoint);
		try {
			model.pattern.execute("moveShape");
		} catch (Exception e) {
			logger.error("Exception " + e.getMessage());
		}
		Shape shape =model.document.getShape(new Point(x, y));
		logger.info("shapeList="+model.document.getShapeList());
		logger.info("shape="+shape);
		assertNotNull(shape);
		assertEquals(model.system.pressedShape.position.dimension.width, shape.position.dimension.width);
		assertEquals(model.system.pressedShape.position.dimension.height, shape.position.dimension.height);
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
//		assertEquals(model.document.setIndex(0), true);
//		assertEquals(model.document.getPage().setIndex(1), true);
//		model.cache.pressedPage = model.document.getPage();
//		model.system.pressedImage = model.document.getImage();
//		model.cache.scaleOperator = '/';
//		model.cache.scaleFactor = 1.01;
//		try {
//			model.pattern.execute("resizeImage");
//			model.pattern.execute("resizeImage");
//			model.pattern.execute("resizeImage");
//			model.pattern.execute("resizeImage");
//			model.pattern.execute("resizeImage");
//			model.pattern.execute("resizeImage");
//			model.pattern.execute("resizeImage");
//		} catch (Exception e) {
//			logger.error("Exception "+e.getMessage());
//		}
//		int x = (int) (model.system.pressedImage.position.point.x+model.system.pressedImage.position.dimension.width / 2);
//		int y = (int) (model.system.pressedImage.position.point.y+model.system.pressedImage.position.dimension.height / 2);
//		logger.info("shapeList="+model.document.getShapeList());
//		Shape shape =model.document.getShape(new Point(x, y));
//		logger.info("shapeList="+model.document.getShapeList());
//		logger.info("shape="+shape);
//		assertNotNull(shape);
//	}
//	
//	@Test
//	@Order(5)
//	public void moveShapeOne() {
//		assertEquals(model.document.setIndex(0), true);
//		assertEquals(model.document.getPage().setIndex(1), true);
//		model.system.pressedImage = model.document.getPage().getImage();
//		assertEquals(model.document.getPage().setIndex(0), true);
//		model.system.releasedImage = model.document.getPage().getImage();
//		int x = (int) (model.system.pressedImage.position.point.x+model.system.pressedImage.position.dimension.width / 2);
//		int y = (int) (model.system.pressedImage.position.point.y+model.system.pressedImage.position.dimension.height / 2);
//		model.cache.pressedPoint = new Point(x, y);
//		logger.info("pressedImage="+model.system.pressedImage);
//		logger.info("pressedPoint="+model.cache.pressedPoint);
//		model.system.pressedShape = model.document.getPage().getShape(model.cache.pressedPoint);
//		logger.info("pressedShape="+model.system.pressedShape);
//		assertNotNull(model.system.pressedShape);
//		x = (int) (model.system.releasedImage.position.point.x+model.system.releasedImage.position.dimension.width / 2);
//		y = (int) (model.system.releasedImage.position.point.y+model.system.releasedImage.position.dimension.height / 2);
//		model.cache.releasedPoint = new Point(x, y);
//		logger.info("releasedImage="+model.system.releasedImage);
//		logger.info("releasedPoint="+model.cache.releasedPoint);
//		try {
//			model.pattern.execute("moveShape");
//		} catch (Exception e) {
//			logger.error("Exception " + e.getMessage());
//		}
//		Shape shape =model.document.getShape(new Point(x, y));
//		logger.info("shapeList="+model.document.getShapeList());
//		logger.info("shape="+shape);
//		assertNotNull(shape);
//		assertEquals(model.system.pressedShape.position.dimension.width, shape.position.dimension.width);
//		assertEquals(model.system.pressedShape.position.dimension.height, shape.position.dimension.height);
//	}
}
