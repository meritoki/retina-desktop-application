package com.meritoki.retina.application.desktop.model.document;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.File;

import javax.swing.JOptionPane;

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
import com.meritoki.app.desktop.retina.model.document.Selection;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DocumentResizeShapeTest {
	
	static Logger logger = LogManager.getLogger(DocumentResizeShapeTest.class.getName());
	static Document document = null;
	static String pageZeroUUID = null;
	static String pageOneUUID = null;
	static String pageTwoUUID = null;

	static int dimension = 100;

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
		page.addImage(new Image(new File("./data/image/03.jpg")));
		pageTwoUUID = page.uuid;
		document.addPage(page);
		assertEquals(document.pageList.size(), 3);
	}
	
	@Test
	@Order(1)
	public void addShapes() {
		//Page Zero
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
		//Page One
		assertEquals(document.setIndex(1), true);
		assertEquals(document.getPage().setIndex(1),true);
		document.cache.pressedImage = document.getImage();
		x = (int) (document.cache.pressedImage.position.point.x+document.cache.pressedImage.position.dimension.width / 2 - dimension / 2);
		y = (int) (document.cache.pressedImage.position.point.y+document.cache.pressedImage.position.dimension.height / 2 - dimension / 2);
		width = dimension;
		height = dimension;
		document.cache.pressedPoint = new Point(x, y);
		document.cache.releasedPoint = new Point(x + width, y + height);
		try {
			document.pattern.execute("addShape");
		} catch (Exception e) {
			logger.error("Exception " + e.getMessage());
		}
		x = (int) (document.cache.pressedImage.position.point.x+document.cache.pressedImage.position.dimension.width / 2);
		y = (int) (document.cache.pressedImage.position.point.y+document.cache.pressedImage.position.dimension.height / 2);
		assertNotNull(document.getPage().getShape(new Point(x,y)));
		//Page Two
		assertEquals(document.setIndex(2), true);
		assertEquals(document.getPage().setIndex(2),true);
		document.cache.pressedImage = document.getPage().getImage();
		x = (int) (document.cache.pressedImage.position.point.x+document.cache.pressedImage.position.dimension.width / 2 - dimension / 2);
		y = (int) (document.cache.pressedImage.position.point.y+document.cache.pressedImage.position.dimension.height / 2 - dimension / 2);
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
		x = (int) (document.cache.pressedImage.position.point.x+document.cache.pressedImage.position.dimension.width / 2);
		y = (int) (document.cache.pressedImage.position.point.y+document.cache.pressedImage.position.dimension.height / 2);
		assertNotNull(document.getPage().getShape(new Point(x,y)));
	}
	
	@Test
	@Order(2)
	public void saveShapes() {
		DocumentController.save(new java.io.File("./test/document-resize-shape-test-a.json"), document);
	}
	
	@Test
	@Order(3)
	public void resizeShapes() {
		assertEquals(document.setIndex(0), true);
		assertEquals(document.getPage().setIndex(0), true);
		document.cache.selection = Selection.TOP;
		document.cache.pressedImage = document.getImage();
		int centerX = (int) (document.cache.pressedImage.position.dimension.width / 2);
		int centerY = (int) (document.cache.pressedImage.position.dimension.height / 2);
		document.cache.pressedShape = document.cache.pressedImage.getShape(new Point(centerX,centerY));
		assertNotNull(document.cache.pressedShape);
		document.cache.releasedPoint = new Point(centerX, centerY - dimension/2 - 10);
		try {
			document.pattern.execute("resizeShape");
		} catch (Exception e) {
			logger.error("Exception "+e.getMessage());
		}
		logger.info(document.cache.pressedShape);
		assertEquals(document.cache.pressedShape.position.dimension.height, 110);
		
		assertEquals(document.setIndex(1), true);
		assertEquals(document.getPage().setIndex(1), true);
		document.cache.selection = Selection.TOP;
		document.cache.pressedImage = document.getImage();
		centerX = (int) (document.cache.pressedImage.position.point.x + document.cache.pressedImage.position.dimension.width / 2);
		centerY = (int) (document.cache.pressedImage.position.point.y + document.cache.pressedImage.position.dimension.height / 2);
		document.cache.pressedShape = document.cache.pressedImage.getShape(new Point(centerX,centerY));
		assertNotNull(document.cache.pressedShape);
		document.cache.releasedPoint = new Point(centerX, centerY - dimension/2 - 10);
		logger.info(document.cache.releasedPoint);
		try {
			document.pattern.execute("resizeShape");
		} catch (Exception e) {
			logger.error("Exception "+e.getMessage());
		}
		logger.info(document.cache.pressedShape);
		assertEquals(document.cache.pressedShape.position.dimension.height, 110);
		
		assertEquals(document.setIndex(2), true);
		assertEquals(document.getPage().setIndex(2), true);
		document.cache.selection = Selection.TOP;
		document.cache.pressedImage = document.getImage();
		centerX = (int) (document.cache.pressedImage.position.point.x + document.cache.pressedImage.position.dimension.width / 2);
		centerY = (int) (document.cache.pressedImage.position.point.y + document.cache.pressedImage.position.dimension.height / 2);
		document.cache.pressedShape = document.cache.pressedImage.getShape(new Point(centerX,centerY));
		document.cache.releasedPoint = new Point(centerX, centerY - dimension/2 - 10);
		try {
			document.pattern.execute("resizeShape");
		} catch (Exception e) {
			logger.error("Exception "+e.getMessage());
		}
		logger.info(document.cache.pressedShape);
		assertEquals(document.cache.pressedShape.position.dimension.height, 110);
		
	}
	
	

}
