package com.meritoki.app.desktop.retina.model.document.bak;

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
import com.meritoki.app.desktop.retina.model.document.Document;
import com.meritoki.app.desktop.retina.model.document.DocumentShiftImageMoveShapeTest;
import com.meritoki.app.desktop.retina.model.document.Image;
import com.meritoki.app.desktop.retina.model.document.Page;
import com.meritoki.app.desktop.retina.model.document.Point;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DocumentResizeImageTest2 {

	static Logger logger = LogManager.getLogger(DocumentShiftImageMoveShapeTest.class.getName());
	static Model model = new Model();
	static int dimension = 4;
	
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
	public void addShapeZero() {
		
		assertEquals(model.document.setIndex(0), true);
		//left
		assertEquals(model.document.getPage().setIndex(0), true);
		model.system.pressedImage = model.document.getImage();
		int x = (int) (model.system.pressedImage.position.point.x+model.system.pressedImage.position.dimension.width / 2 - dimension / 2);
		int y = (int) (model.system.pressedImage.position.point.y+model.system.pressedImage.position.dimension.height / 2 - dimension / 2);
		int width = dimension;
		int height = dimension;
		model.cache.pressedPoint = new Point(x, y);
		model.cache.releasedPoint = new Point(x + width, y + height);
		try {
			model.pattern.execute("addShape");
		} catch (Exception e) {
			logger.error("Exception " + e.getMessage());
		}
		x = (int) (model.system.pressedImage.position.point.x+model.system.pressedImage.position.dimension.width / 2);
		y = (int) (model.system.pressedImage.position.point.y+model.system.pressedImage.position.dimension.height / 2);
		assertNotNull(model.document.getShape(new Point(x,y)));
		//right
		assertEquals(model.document.getPage().setIndex(1), true);
		model.system.pressedImage = model.document.getImage();
		x = (int) (model.system.pressedImage.position.point.x+model.system.pressedImage.position.dimension.width / 2 - dimension / 2);
		y = (int) (model.system.pressedImage.position.point.y+model.system.pressedImage.position.dimension.height / 2 - dimension / 2);
		width = dimension;
		height = dimension;
		model.cache.pressedPoint = new Point(x, y);
		model.cache.releasedPoint = new Point(x + width, y + height);
		try {
			model.pattern.execute("addShape");
		} catch (Exception e) {
			logger.error("Exception " + e.getMessage());
		}
		x = (int) (model.system.pressedImage.position.point.x+model.system.pressedImage.position.dimension.width / 2);
		y = (int) (model.system.pressedImage.position.point.y+model.system.pressedImage.position.dimension.height / 2);
		assertNotNull(model.document.getShape(new Point(x,y)));
	}
	
	@Test
	@Order(2)
	public void shrinkImageZero() {
		assertEquals(model.document.setIndex(0), true);
		model.system.pressedPage = model.document.getPage();
		
		//left
		assertEquals(model.document.getPage().setIndex(0), true);
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
		} catch (Exception e) {
			logger.error("Exception "+e.getMessage());
		}
		int x = (int) (model.system.pressedImage.position.point.x+model.system.pressedImage.position.dimension.width / 2);
		int y = (int) (model.system.pressedImage.position.point.y+model.system.pressedImage.position.dimension.height / 2);
		assertNotNull(model.document.getShape(new Point(x,y)));
		//right
		assertEquals(model.document.getPage().setIndex(1), true);
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
		} catch (Exception e) {
			logger.error("Exception "+e.getMessage());
		}
		x = (int) (model.system.pressedImage.position.point.x+model.system.pressedImage.position.dimension.width / 2);
		y = (int) (model.system.pressedImage.position.point.y+model.system.pressedImage.position.dimension.height / 2);
		assertNotNull(model.document.getShape(new Point(x,y)));
	}
	
	@Test
	@Order(3)
	public void addShapeOne() {
		assertEquals(model.document.setIndex(0), true);
		//left
		assertEquals(model.document.getPage().setIndex(0), true);
		model.system.pressedImage = model.document.getImage();
		int x = (int) (model.system.pressedImage.position.point.x+model.system.pressedImage.position.dimension.width / 2 - dimension / 2);
		int y = (int) (model.system.pressedImage.position.point.y+model.system.pressedImage.position.dimension.height / 4 - dimension / 2);
		int width = dimension;
		int height = dimension;
		model.cache.pressedPoint = new Point(x, y);
		model.cache.releasedPoint = new Point(x + width, y + height);
		try {
			model.pattern.execute("addShape");
		} catch (Exception e) {
			logger.error("Exception " + e.getMessage());
		}
		x = (int) (model.system.pressedImage.position.point.x+model.system.pressedImage.position.dimension.width / 2);
		y = (int) (model.system.pressedImage.position.point.y+model.system.pressedImage.position.dimension.height / 4);
		assertNotNull(model.document.getShape(new Point(x,y)));
		//right
		assertEquals(model.document.getPage().setIndex(1), true);
		model.system.pressedImage = model.document.getImage();
		x = (int) (model.system.pressedImage.position.point.x+model.system.pressedImage.position.dimension.width / 2 - dimension / 2);
		y = (int) (model.system.pressedImage.position.point.y+model.system.pressedImage.position.dimension.height / 4 - dimension / 2);
		width = dimension;
		height = dimension;
		model.cache.pressedPoint = new Point(x, y);
		model.cache.releasedPoint = new Point(x + width, y + height);
		try {
			model.pattern.execute("addShape");
		} catch (Exception e) {
			logger.error("Exception " + e.getMessage());
		}
		x = (int) (model.system.pressedImage.position.point.x+model.system.pressedImage.position.dimension.width / 2);
		y = (int) (model.system.pressedImage.position.point.y+model.system.pressedImage.position.dimension.height / 4);
		assertNotNull(model.document.getShape(new Point(x,y)));
	}
	
	@Test
	@Order(4)
	public void shrinkImageOne() {
		assertEquals(model.document.setIndex(0), true);
		//left
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
		int x = (int) (model.system.pressedImage.position.point.x+model.system.pressedImage.position.dimension.width / 2);
		int y = (int) (model.system.pressedImage.position.point.y+model.system.pressedImage.position.dimension.height / 2);
		assertNotNull(model.document.getShape(new Point(x,y)));
		x = (int) (model.system.pressedImage.position.point.x+model.system.pressedImage.position.dimension.width / 2);
		y = (int) (model.system.pressedImage.position.point.y+model.system.pressedImage.position.dimension.height / 4);
		assertNotNull(model.document.getShape(new Point(x,y)));
		//right
		assertEquals(model.document.getPage().setIndex(1), true);
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
		x = (int) (model.system.pressedImage.position.point.x+model.system.pressedImage.position.dimension.width / 2);
		y = (int) (model.system.pressedImage.position.point.y+model.system.pressedImage.position.dimension.height / 2);
		assertNotNull(model.document.getShape(new Point(x,y)));
		x = (int) (model.system.pressedImage.position.point.x+model.system.pressedImage.position.dimension.width / 2);
		y = (int) (model.system.pressedImage.position.point.y+model.system.pressedImage.position.dimension.height / 4);
		assertNotNull(model.document.getShape(new Point(x,y)));
	}
//	
	@Test
	@Order(3)
	public void addShapeTwo() {
		assertEquals(model.document.setIndex(0), true);
		assertEquals(model.document.getPage().setIndex(0), true);
		model.system.pressedImage = model.document.getImage();
		int x = (int) (model.system.pressedImage.position.point.x+model.system.pressedImage.position.dimension.width / 2 - dimension / 2);
		int y = (int) (model.system.pressedImage.position.point.y+model.system.pressedImage.position.dimension.height / 3/4 - dimension / 2);
		int width = dimension;
		int height = dimension;
		model.cache.pressedPoint = new Point(x, y);
		model.cache.releasedPoint = new Point(x + width, y + height);
		try {
			model.pattern.execute("addShape");
		} catch (Exception e) {
			logger.error("Exception " + e.getMessage());
		}
		x = (int) (model.system.pressedImage.position.point.x+model.system.pressedImage.position.dimension.width / 2);
		y = (int) (model.system.pressedImage.position.point.y+model.system.pressedImage.position.dimension.height / 3/4);
		assertNotNull(model.document.getShape(new Point(x,y)));
		
		assertEquals(model.document.getPage().setIndex(1), true);
		model.system.pressedImage = model.document.getImage();
		x = (int) (model.system.pressedImage.position.point.x+model.system.pressedImage.position.dimension.width / 2 - dimension / 2);
		y = (int) (model.system.pressedImage.position.point.y+model.system.pressedImage.position.dimension.height / 3/4 - dimension / 2);
		width = dimension;
		height = dimension;
		model.cache.pressedPoint = new Point(x, y);
		model.cache.releasedPoint = new Point(x + width, y + height);
		try {
			model.pattern.execute("addShape");
		} catch (Exception e) {
			logger.error("Exception " + e.getMessage());
		}
		x = (int) (model.system.pressedImage.position.point.x+model.system.pressedImage.position.dimension.width / 2);
		y = (int) (model.system.pressedImage.position.point.y+model.system.pressedImage.position.dimension.height / 3/4);
		assertNotNull(model.document.getShape(new Point(x,y)));
	}
//	
	@Test
	@Order(4)
	public void growImageTwo() {
		assertEquals(model.document.setIndex(0), true);
		assertEquals(model.document.getPage().setIndex(0), true);
		model.system.pressedPage = model.document.getPage();
		model.system.pressedImage = model.document.getImage();
		model.cache.scaleOperator = '*';
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
		int x = (int) (model.system.pressedImage.position.point.x+model.system.pressedImage.position.dimension.width / 2);
		int y = (int) (model.system.pressedImage.position.point.y+model.system.pressedImage.position.dimension.height / 2);
		assertNotNull(model.document.getShape(new Point(x,y)));
		x = (int) (model.system.pressedImage.position.point.x+model.system.pressedImage.position.dimension.width / 2);
		y = (int) (model.system.pressedImage.position.point.y+model.system.pressedImage.position.dimension.height / 4);
		assertNotNull(model.document.getShape(new Point(x,y)));
		x = (int) (model.system.pressedImage.position.point.x+model.system.pressedImage.position.dimension.width / 2);
		y = (int) (model.system.pressedImage.position.point.y+model.system.pressedImage.position.dimension.height / 3/4);
		assertNotNull(model.document.getShape(new Point(x,y)));
		
		assertEquals(model.document.getPage().setIndex(1), true);
		model.system.pressedPage = model.document.getPage();
		model.system.pressedImage = model.document.getImage();
		model.cache.scaleOperator = '*';
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
		x = (int) (model.system.pressedImage.position.point.x+model.system.pressedImage.position.dimension.width / 2);
		y = (int) (model.system.pressedImage.position.point.y+model.system.pressedImage.position.dimension.height / 2);
		assertNotNull(model.document.getShape(new Point(x,y)));
		x = (int) (model.system.pressedImage.position.point.x+model.system.pressedImage.position.dimension.width / 2);
		y = (int) (model.system.pressedImage.position.point.y+model.system.pressedImage.position.dimension.height / 4);
		assertNotNull(model.document.getShape(new Point(x,y)));
		x = (int) (model.system.pressedImage.position.point.x+model.system.pressedImage.position.dimension.width / 2);
		y = (int) (model.system.pressedImage.position.point.y+model.system.pressedImage.position.dimension.height / 3/4);
		assertNotNull(model.document.getShape(new Point(x,y)));
	}
//	
	@Test
	@Order(5)
	public void addShapeThree() {
		assertEquals(model.document.setIndex(0), true);
		assertEquals(model.document.getPage().setIndex(0), true);
		model.system.pressedImage = model.document.getImage();
		int x = (int) (model.system.pressedImage.position.point.x+model.system.pressedImage.position.dimension.width / 4 - dimension / 2);
		int y = (int) (model.system.pressedImage.position.point.y+model.system.pressedImage.position.dimension.height / 2 - dimension / 2);
		int width = dimension;
		int height = dimension;
		model.cache.pressedPoint = new Point(x, y);
		model.cache.releasedPoint = new Point(x + width, y + height);
		try {
			model.pattern.execute("addShape");
		} catch (Exception e) {
			logger.error("Exception " + e.getMessage());
		}
		x = (int) (model.system.pressedImage.position.point.x+model.system.pressedImage.position.dimension.width / 4);
		y = (int) (model.system.pressedImage.position.point.y+model.system.pressedImage.position.dimension.height / 2);
		assertNotNull(model.document.getShape(new Point(x,y)));
		
		assertEquals(model.document.getPage().setIndex(1), true);
		model.system.pressedImage = model.document.getImage();
		x = (int) (model.system.pressedImage.position.point.x+model.system.pressedImage.position.dimension.width / 4 - dimension / 2);
		y = (int) (model.system.pressedImage.position.point.y+model.system.pressedImage.position.dimension.height / 2 - dimension / 2);
		width = dimension;
		height = dimension;
		model.cache.pressedPoint = new Point(x, y);
		model.cache.releasedPoint = new Point(x + width, y + height);
		try {
			model.pattern.execute("addShape");
		} catch (Exception e) {
			logger.error("Exception " + e.getMessage());
		}
		x = (int) (model.system.pressedImage.position.point.x+model.system.pressedImage.position.dimension.width / 4);
		y = (int) (model.system.pressedImage.position.point.y+model.system.pressedImage.position.dimension.height / 2);
		assertNotNull(model.document.getShape(new Point(x,y)));
	}
//	
	@Test
	@Order(6)
	public void growImageThree() {
		assertEquals(model.document.setIndex(0), true);
		assertEquals(model.document.getPage().setIndex(0), true);
		model.system.pressedPage = model.document.getPage();
		model.system.pressedImage = model.document.getImage();
		model.cache.scaleOperator = '*';
		model.cache.scaleFactor = 1.05;
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
		int x = (int) (model.system.pressedImage.position.point.x+model.system.pressedImage.position.dimension.width / 2);
		int y = (int) (model.system.pressedImage.position.point.y+model.system.pressedImage.position.dimension.height / 2);
		assertNotNull(model.document.getShape(new Point(x,y)));
		x = (int) (model.system.pressedImage.position.point.x+model.system.pressedImage.position.dimension.width / 2);
		y = (int) (model.system.pressedImage.position.point.y+model.system.pressedImage.position.dimension.height / 4);
		assertNotNull(model.document.getShape(new Point(x,y)));
		x = (int) (model.system.pressedImage.position.point.x+model.system.pressedImage.position.dimension.width / 2);
		y = (int) (model.system.pressedImage.position.point.y+model.system.pressedImage.position.dimension.height / 3/4);
		assertNotNull(model.document.getShape(new Point(x,y)));
		x = (int) (model.system.pressedImage.position.point.x+model.system.pressedImage.position.dimension.width / 4);
		y = (int) (model.system.pressedImage.position.point.y+model.system.pressedImage.position.dimension.height /2);
		assertNotNull(model.document.getShape(new Point(x,y)));
		
		assertEquals(model.document.getPage().setIndex(1), true);
		model.system.pressedPage = model.document.getPage();
		model.system.pressedImage = model.document.getImage();
		model.cache.scaleOperator = '*';
		model.cache.scaleFactor = 1.05;
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
		x = (int) (model.system.pressedImage.position.point.x+model.system.pressedImage.position.dimension.width / 2);
		y = (int) (model.system.pressedImage.position.point.y+model.system.pressedImage.position.dimension.height / 2);
		assertNotNull(model.document.getShape(new Point(x,y)));
		x = (int) (model.system.pressedImage.position.point.x+model.system.pressedImage.position.dimension.width / 2);
		y = (int) (model.system.pressedImage.position.point.y+model.system.pressedImage.position.dimension.height / 4);
		assertNotNull(model.document.getShape(new Point(x,y)));
		x = (int) (model.system.pressedImage.position.point.x+model.system.pressedImage.position.dimension.width / 2);
		y = (int) (model.system.pressedImage.position.point.y+model.system.pressedImage.position.dimension.height / 3/4);
		assertNotNull(model.document.getShape(new Point(x,y)));
		x = (int) (model.system.pressedImage.position.point.x+model.system.pressedImage.position.dimension.width / 4);
		y = (int) (model.system.pressedImage.position.point.y+model.system.pressedImage.position.dimension.height /2);
		assertNotNull(model.document.getShape(new Point(x,y)));
	}
	
}
