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

import com.meritoki.app.desktop.retina.controller.node.NodeController;
import com.meritoki.app.desktop.retina.model.Model;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DocumentResizeShapeTest {
	
	static Logger logger = LogManager.getLogger(DocumentResizeShapeTest.class.getName());
	static Model model = new Model();
	static String pageZeroUUID = null;
	static String pageOneUUID = null;
	static String pageTwoUUID = null;

	static int dimension = 100;

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
		page.addImage(new Image(new File("./test/img/test.png")));
		pageTwoUUID = page.uuid;
		model.document.addPage(page);
		assertEquals(model.document.pageList.size(), 3);
	}
	
	@Test
	@Order(1)
	public void addShapes() {
		//Page Zero
		assertEquals(model.document.setIndex(0), true);
		model.document.getPage().getBufferedImage(model);
		assertEquals(model.document.getPage().setIndex(0), true);
		model.system.pressedImage = model.document.getImage();
		int x = (int) (model.system.pressedImage.position.dimension.width / 2 - dimension / 2);
		int y = (int) (model.system.pressedImage.position.dimension.height / 2 - dimension / 2);
		int width = dimension;
		int height = dimension;
		try {
			model.cache.pressedPoint = new Point(x, y);
			model.cache.releasedPoint = new Point(x + width, y + height);
			model.cache.pressedPageUUID = model.document.getPage().uuid;
			model.cache.pressedImageUUID = model.system.pressedImage.uuid;
			model.pattern.execute("addShape");
		} catch (Exception e) {
			logger.error("Exception " + e.getMessage());
		}
		//Page One
		assertEquals(model.document.setIndex(1), true);
		model.document.getPage().getBufferedImage(model);
		assertEquals(model.document.getPage().setIndex(1),true);
		model.system.pressedImage = model.document.getImage();
		x = (int) (model.system.pressedImage.position.point.x+model.system.pressedImage.position.dimension.width / 2 - dimension / 2);
		y = (int) (model.system.pressedImage.position.point.y+model.system.pressedImage.position.dimension.height / 2 - dimension / 2);
		width = dimension;
		height = dimension;
		
		try {
			model.cache.pressedPoint = new Point(x, y);
			model.cache.releasedPoint = new Point(x + width, y + height);
			model.cache.pressedPageUUID = model.document.getPage().uuid;
			model.cache.pressedImageUUID = model.system.pressedImage.uuid;
			model.pattern.execute("addShape");
		} catch (Exception e) {
			logger.error("Exception " + e.getMessage());
		}
		x = (int) (model.system.pressedImage.position.point.x+model.system.pressedImage.position.dimension.width / 2);
		y = (int) (model.system.pressedImage.position.point.y+model.system.pressedImage.position.dimension.height / 2);
		assertNotNull(model.document.getPage().getShape(new Point(x,y)));
		//Page Two
		assertEquals(model.document.setIndex(2), true);
		model.document.getPage().getBufferedImage(model);
		assertEquals(model.document.getPage().setIndex(2),true);
		model.system.pressedImage = model.document.getPage().getImage();
		x = (int) (model.system.pressedImage.position.point.x+model.system.pressedImage.position.dimension.width / 2 - dimension / 2);
		y = (int) (model.system.pressedImage.position.point.y+model.system.pressedImage.position.dimension.height / 2 - dimension / 2);
		width = dimension;
		height = dimension;
		try {
			model.cache.pressedPoint = new Point(x, y);
			model.cache.releasedPoint = new Point(x + width, y + height);
			model.cache.pressedPageUUID = model.document.getPage().uuid;
			model.cache.pressedImageUUID = model.system.pressedImage.uuid;
			model.pattern.execute("addShape");
		} catch (Exception e) {
			logger.error("Exception " + e.getMessage());
		}
		model.system.pressedImage = model.document.getPage().getImage();
		x = (int) (model.system.pressedImage.position.point.x+model.system.pressedImage.position.dimension.width / 2);
		y = (int) (model.system.pressedImage.position.point.y+model.system.pressedImage.position.dimension.height / 2);
		assertNotNull(model.document.getPage().getShape(new Point(x,y)));
	}
	
	@Test
	@Order(2)
	public void saveShapes() {
		NodeController.saveDocument(new java.io.File("./test/document-resize-shape-test-a.json"), model.document);
	}
	
	@Test
	@Order(3)
	public void resizeShapes() {
		assertEquals(model.document.setIndex(0), true);
		model.document.getPage().getBufferedImage(model);
		assertEquals(model.document.getPage().setIndex(0), true);
		
		model.system.pressedImage = model.document.getImage();
		int centerX = (int) (model.system.pressedImage.position.dimension.width / 2);
		int centerY = (int) (model.system.pressedImage.position.dimension.height / 2);
		model.system.pressedShape = model.system.pressedImage.getShape(new Point(centerX,centerY));
		assertNotNull(model.system.pressedShape);
		
		try {
			model.cache.selection = Selection.TOP;
			model.cache.releasedPoint = new Point(centerX, centerY - dimension/2 - 10);
			model.cache.pressedShapeUUID = model.system.pressedShape.uuid;
			model.pattern.execute("resizeShape");
		} catch (Exception e) {
			logger.error("Exception "+e.getMessage());
		}
		logger.info(model.system.pressedShape);
		assertEquals(model.system.pressedShape.position.dimension.height, 110);
		
		assertEquals(model.document.setIndex(1), true);
		model.document.getPage().getBufferedImage(model);
		assertEquals(model.document.getPage().setIndex(1), true);
		
		model.system.pressedImage = model.document.getImage();
		centerX = (int) (model.system.pressedImage.position.point.x + model.system.pressedImage.position.dimension.width / 2);
		centerY = (int) (model.system.pressedImage.position.point.y + model.system.pressedImage.position.dimension.height / 2);
		model.system.pressedShape = model.system.pressedImage.getShape(new Point(centerX,centerY));
		assertNotNull(model.system.pressedShape);
		logger.info(model.cache.releasedPoint);
		try {
			model.cache.selection = Selection.TOP;
			model.cache.releasedPoint = new Point(centerX, centerY - dimension/2 - 10);
			model.cache.pressedShapeUUID = model.system.pressedShape.uuid;
			model.pattern.execute("resizeShape");
		} catch (Exception e) {
			logger.error("Exception "+e.getMessage());
		}
		logger.info(model.system.pressedShape);
		assertEquals(model.system.pressedShape.position.dimension.height, 110);
		
		assertEquals(model.document.setIndex(2), true);
		model.document.getPage().getBufferedImage(model);
		assertEquals(model.document.getPage().setIndex(2), true);
		
		model.system.pressedImage = model.document.getImage();
		centerX = (int) (model.system.pressedImage.position.point.x + model.system.pressedImage.position.dimension.width / 2);
		centerY = (int) (model.system.pressedImage.position.point.y + model.system.pressedImage.position.dimension.height / 2);
		model.system.pressedShape = model.system.pressedImage.getShape(new Point(centerX,centerY));
		
		try {
			model.cache.selection = Selection.TOP;
			model.cache.releasedPoint = new Point(centerX, centerY - dimension/2 - 10);
			model.cache.pressedShapeUUID = model.system.pressedShape.uuid;
			model.pattern.execute("resizeShape");
		} catch (Exception e) {
			logger.error("Exception "+e.getMessage());
		}
		logger.info(model.system.pressedShape);
		assertEquals(model.system.pressedShape.position.dimension.height, 110);	
	}
}
