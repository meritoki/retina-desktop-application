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
public class DocumentAddShapeScalePageTest {
	static Logger logger = LogManager.getLogger(DocumentAddShapeScalePageTest.class.getName());
	static Model model = new Model();
	static String pageZeroUUID = null;
	static Dimension origin = null;
	static int dimension = 4;
	
	@BeforeAll
	public static void initialize() {
		model.document = new Document();
		Page page = new Page();
		page = new Page(new Image(new File("./test/img/test.png")));
		pageZeroUUID = page.uuid;
		model.document.addPage(page);
		page = new Page();
		assertEquals(model.document.pageList.size(),1);
	}
	
	@Test
	@Order(1)
	public void addShape() {
		assertEquals(model.document.setIndex(0), true);
		model.document.getPage().getBufferedImage(model);
		assertEquals(model.document.getPage().setIndex(0), true);
		model.system.pressedImage = model.document.getImage();
		int x = (int) (model.system.pressedImage.position.absoluteDimension.width / 2 - dimension / 2);
		int y = (int) (model.system.pressedImage.position.absoluteDimension.height / 2 - dimension / 2);
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
	}
	
	@Test
	@Order(2)
	public void scaleUp() {
		assertEquals(model.document.setIndex(0), true);
		
		assertEquals(model.document.getPage().setIndex(0), true);
		model.system.pressedImage = model.document.getImage();
		origin = new Dimension(model.system.pressedImage.position.dimension);
		model.cache.scaleOperator = '*';
		model.cache.scaleFactor = 1.5;
		try {
			model.cache.pressedPageUUID = model.document.getPage().uuid;
			model.pattern.execute("scalePage");
			model.pattern.execute("scalePage");
			model.pattern.execute("scalePage");
			model.pattern.execute("scalePage");
		} catch (Exception e) {
			logger.error("Exception " + e.getMessage());
		} 
		double x = model.system.pressedImage.position.center.x;
		double y = model.system.pressedImage.position.center.y;
		assertNotNull(model.document.getShape(new Point(x,y)));
	}
	
	@Test
	@Order(3)
	public void scaleDown() {
		assertEquals(model.document.setIndex(0), true);
		assertEquals(model.document.getPage().setIndex(0), true);
		model.system.pressedImage = model.document.getImage();
		model.cache.scaleOperator = '/';
		model.cache.scaleFactor = 1.5;
		try {
			model.cache.pressedPageUUID = model.document.getPage().uuid;
			model.pattern.execute("scalePage");
			model.pattern.execute("scalePage");
			model.pattern.execute("scalePage");
			model.pattern.execute("scalePage");
		} catch (Exception e) {
			logger.error("Exception " + e.getMessage());
		} 
		double x = model.system.pressedImage.position.center.x;
		double y = model.system.pressedImage.position.center.y;
		Shape shape = model.document.getShape(new Point(x,y));
		assertNotNull(shape);
		assertEquals(shape.position.dimension.width,dimension);
		assertEquals(shape.position.dimension.height,dimension);
	}
	
	@Test
	@Order(4)
	public void undo() {
		//scale down
		try {
		model.pattern.undo();
		model.pattern.undo();
		model.pattern.undo();
		model.pattern.undo();
		//scale up
		model.pattern.undo();
		model.pattern.undo();
		model.pattern.undo();
		model.pattern.undo();
		assertEquals(origin.width,model.system.pressedImage.position.dimension.width);
		assertEquals(origin.height,model.system.pressedImage.position.dimension.height);
		//undo add shape
		model.pattern.undo();
		assertEquals(model.document.getShapeList().size(),0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
