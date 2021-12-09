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
public class DocumentShiftImageMoveShapeTest {

	static Logger logger = LogManager.getLogger(DocumentShiftImageMoveShapeTest.class.getName());
	static Model model = new Model();
	static int dimension = 2;
	
	@BeforeAll
	public static void initialize() {
		model.document = new Document();
		Page page = new Page();
		page.addImage(new Image(new File("./test/img/test.png")));
		page.addImage(new Image(new File("./test/img/test.png")));
		model.document.addPage(page);
		assertEquals(model.document.pageList.size(), 1);
	}
	
	@Test
	@Order(1) 
	public void addShape() {
		assertEquals(model.document.setIndex(0), true);
		model.document.getPage().getBufferedImage(model);
		//add right
		assertEquals(model.document.getPage().setIndex(1), true);
		model.system.pressedImage = model.document.getImage();
		double x = (model.system.pressedImage.position.center.x - dimension / 2);
		double y = (model.system.pressedImage.position.center.y - dimension / 2);
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
	}
	
	@Test
	@Order(2)
	public void marginShift() {
		assertEquals(model.document.setIndex(0), true);
		model.document.getPage().getBufferedImage(model);
		assertEquals(model.document.getPage().setIndex(1), true);
		model.system.pressedPage = model.document.getPage();
		model.system.pressedImage = model.document.getImage();
		model.cache.shiftOperator = '+';
		model.cache.shiftFactor = 10;
		try {
			model.cache.pressedPageUUID = model.system.pressedPage.uuid;
			model.cache.pressedImageUUID = model.system.pressedImage.uuid;
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
		double x = (model.system.pressedImage.position.center.x);
		double y = (model.system.pressedImage.position.center.y);
		assertNotNull(model.document.getShape(new Point(x,y)));
	}
	
	@Test
	@Order(3)
	public void moveShape() {
		assertEquals(model.document.setIndex(0), true);
		model.document.getPage().getBufferedImage(model);
		assertEquals(model.document.getPage().setIndex(1), true);
		model.system.pressedImage = model.document.getPage().getImage();
		assertEquals(model.document.getPage().setIndex(0), true);
		model.system.releasedImage = model.document.getPage().getImage();
		double x = (model.system.pressedImage.position.center.x);
		double y = (model.system.pressedImage.position.center.y);
		model.cache.pressedPoint = new Point(x, y);
		model.system.pressedShape = model.document.getPage().getShape(model.cache.pressedPoint);
		x = (int) (model.system.releasedImage.position.center.x);
		y = (int) (model.system.releasedImage.position.center.y);
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
}
