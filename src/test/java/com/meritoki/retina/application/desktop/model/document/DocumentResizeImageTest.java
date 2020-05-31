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

import com.meritoki.app.desktop.retina.model.document.Document;
import com.meritoki.app.desktop.retina.model.document.Image;
import com.meritoki.app.desktop.retina.model.document.Page;
import com.meritoki.app.desktop.retina.model.document.Point;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DocumentResizeImageTest {

	static Logger logger = LogManager.getLogger(DocumentMarginShiftMoveShapeTest.class.getName());
	static Document document = null;
	static int dimension = 2;
	
	@BeforeAll
	public static void initialize() {
		document = new Document();
		Page page = new Page();
		page.addImage(new Image(new File("./data/image/01.jpg")));
		document.addPage(page);
		assertEquals(document.pageList.size(), 1);
	}
	
	@Test
	@Order(1) 
	public void addShapeZero() {
		assertEquals(document.setIndex(0), true);
		assertEquals(document.getPage().setIndex(0), true);
		document.cache.pressedImage = document.getImage();
		int x = (int) (document.cache.pressedImage.position.point.x+document.cache.pressedImage.position.dimension.width / 2 - dimension / 2);
		int y = (int) (document.cache.pressedImage.position.point.y+document.cache.pressedImage.position.dimension.height / 2 - dimension / 2);
		int width = dimension;
		int height = dimension;
		document.cache.pressedPoint = new Point(x, y);
		document.cache.releasedPoint = new Point(x + width, y + height);
		try {
			document.pattern.execute("addShape");
		} catch (Exception e) {
			logger.error("Exception " + e.getMessage());
		}
		x = (int) (document.cache.pressedImage.position.point.x+document.cache.pressedImage.position.dimension.width / 2);
		y = (int) (document.cache.pressedImage.position.point.y+document.cache.pressedImage.position.dimension.height / 2);
		assertNotNull(document.getShape(new Point(x,y)));
	}
	
	@Test
	@Order(2)
	public void shrinkImageZero() {
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
		} catch (Exception e) {
			logger.error("Exception "+e.getMessage());
		}
		int x = (int) (document.cache.pressedImage.position.point.x+document.cache.pressedImage.position.dimension.width / 2);
		int y = (int) (document.cache.pressedImage.position.point.y+document.cache.pressedImage.position.dimension.height / 2);
		assertNotNull(document.getShape(new Point(x,y)));
	}
	
	@Test
	@Order(3)
	public void addShapeOne() {
		assertEquals(document.setIndex(0), true);
		assertEquals(document.getPage().setIndex(0), true);
		document.cache.pressedImage = document.getImage();
		int x = (int) (document.cache.pressedImage.position.point.x+document.cache.pressedImage.position.dimension.width / 2 - dimension / 2);
		int y = (int) (document.cache.pressedImage.position.point.y+document.cache.pressedImage.position.dimension.height / 4 - dimension / 2);
		int width = dimension;
		int height = dimension;
		document.cache.pressedPoint = new Point(x, y);
		document.cache.releasedPoint = new Point(x + width, y + height);
		try {
			document.pattern.execute("addShape");
		} catch (Exception e) {
			logger.error("Exception " + e.getMessage());
		}
		x = (int) (document.cache.pressedImage.position.point.x+document.cache.pressedImage.position.dimension.width / 2);
		y = (int) (document.cache.pressedImage.position.point.y+document.cache.pressedImage.position.dimension.height / 4);
		assertNotNull(document.getShape(new Point(x,y)));
	}
	
	@Test
	@Order(4)
	public void shrinkImageOne() {
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
		assertNotNull(document.getShape(new Point(x,y)));
		x = (int) (document.cache.pressedImage.position.point.x+document.cache.pressedImage.position.dimension.width / 2);
		y = (int) (document.cache.pressedImage.position.point.y+document.cache.pressedImage.position.dimension.height / 4);
		assertNotNull(document.getShape(new Point(x,y)));
	}
	
	@Test
	@Order(3)
	public void addShapeTwo() {
		assertEquals(document.setIndex(0), true);
		assertEquals(document.getPage().setIndex(0), true);
		document.cache.pressedImage = document.getImage();
		int x = (int) (document.cache.pressedImage.position.point.x+document.cache.pressedImage.position.dimension.width / 2 - dimension / 2);
		int y = (int) (document.cache.pressedImage.position.point.y+document.cache.pressedImage.position.dimension.height / 3/4 - dimension / 2);
		int width = dimension;
		int height = dimension;
		document.cache.pressedPoint = new Point(x, y);
		document.cache.releasedPoint = new Point(x + width, y + height);
		try {
			document.pattern.execute("addShape");
		} catch (Exception e) {
			logger.error("Exception " + e.getMessage());
		}
		x = (int) (document.cache.pressedImage.position.point.x+document.cache.pressedImage.position.dimension.width / 2);
		y = (int) (document.cache.pressedImage.position.point.y+document.cache.pressedImage.position.dimension.height / 3/4);
		assertNotNull(document.getShape(new Point(x,y)));
	}
	
	@Test
	@Order(4)
	public void growImageTwo() {
		assertEquals(document.setIndex(0), true);
		assertEquals(document.getPage().setIndex(0), true);
		document.cache.pressedPage = document.getPage();
		document.cache.pressedImage = document.getImage();
		document.cache.scaleOperator = '*';
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
		assertNotNull(document.getShape(new Point(x,y)));
		x = (int) (document.cache.pressedImage.position.point.x+document.cache.pressedImage.position.dimension.width / 2);
		y = (int) (document.cache.pressedImage.position.point.y+document.cache.pressedImage.position.dimension.height / 4);
		assertNotNull(document.getShape(new Point(x,y)));
		x = (int) (document.cache.pressedImage.position.point.x+document.cache.pressedImage.position.dimension.width / 2);
		y = (int) (document.cache.pressedImage.position.point.y+document.cache.pressedImage.position.dimension.height / 3/4);
		assertNotNull(document.getShape(new Point(x,y)));
	}
	
	@Test
	@Order(5)
	public void addShapeThree() {
		assertEquals(document.setIndex(0), true);
		assertEquals(document.getPage().setIndex(0), true);
		document.cache.pressedImage = document.getImage();
		int x = (int) (document.cache.pressedImage.position.point.x+document.cache.pressedImage.position.dimension.width / 4 - dimension / 2);
		int y = (int) (document.cache.pressedImage.position.point.y+document.cache.pressedImage.position.dimension.height / 2 - dimension / 2);
		int width = dimension;
		int height = dimension;
		document.cache.pressedPoint = new Point(x, y);
		document.cache.releasedPoint = new Point(x + width, y + height);
		try {
			document.pattern.execute("addShape");
		} catch (Exception e) {
			logger.error("Exception " + e.getMessage());
		}
		x = (int) (document.cache.pressedImage.position.point.x+document.cache.pressedImage.position.dimension.width / 4);
		y = (int) (document.cache.pressedImage.position.point.y+document.cache.pressedImage.position.dimension.height / 2);
		assertNotNull(document.getShape(new Point(x,y)));
	}
	
	@Test
	@Order(6)
	public void growImageThree() {
		assertEquals(document.setIndex(0), true);
		assertEquals(document.getPage().setIndex(0), true);
		document.cache.pressedPage = document.getPage();
		document.cache.pressedImage = document.getImage();
		document.cache.scaleOperator = '*';
		document.cache.scaleFactor = 1.05;
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
		assertNotNull(document.getShape(new Point(x,y)));
		x = (int) (document.cache.pressedImage.position.point.x+document.cache.pressedImage.position.dimension.width / 2);
		y = (int) (document.cache.pressedImage.position.point.y+document.cache.pressedImage.position.dimension.height / 4);
		assertNotNull(document.getShape(new Point(x,y)));
		x = (int) (document.cache.pressedImage.position.point.x+document.cache.pressedImage.position.dimension.width / 2);
		y = (int) (document.cache.pressedImage.position.point.y+document.cache.pressedImage.position.dimension.height / 3/4);
		assertNotNull(document.getShape(new Point(x,y)));
		x = (int) (document.cache.pressedImage.position.point.x+document.cache.pressedImage.position.dimension.width / 4);
		y = (int) (document.cache.pressedImage.position.point.y+document.cache.pressedImage.position.dimension.height /2);
		assertNotNull(document.getShape(new Point(x,y)));
	}
	
}
