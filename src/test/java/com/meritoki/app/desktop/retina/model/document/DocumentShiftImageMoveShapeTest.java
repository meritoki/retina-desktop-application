package com.meritoki.app.desktop.retina.model.document;

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
public class DocumentShiftImageMoveShapeTest {

	static Logger logger = LogManager.getLogger(DocumentShiftImageMoveShapeTest.class.getName());
	static Document document = null;
	static int dimension = 2;
	
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
		//add right
		assertEquals(document.getPage().setIndex(1), true);
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
		assertNotNull(document.getShape(new Point(x,y)));
	}
	
	@Test
	@Order(2)
	public void marginShift() {
		assertEquals(document.setIndex(0), true);
		assertEquals(document.getPage().setIndex(1), true);
		document.cache.pressedImage = document.getImage();
		document.cache.shiftOperator = '+';
		document.cache.shiftFactor = 10;
		try {
			document.pattern.execute("shiftImage");
			document.pattern.execute("shiftImage");
			document.pattern.execute("shiftImage");
			document.pattern.execute("shiftImage");
			document.pattern.execute("shiftImage");
			document.pattern.execute("shiftImage");
			document.pattern.execute("shiftImage");
		} catch (Exception e) {
			logger.error("Exception "+e.getMessage());
		}
		double x = (document.cache.pressedImage.position.center.x);
		double y = (document.cache.pressedImage.position.center.y);
		assertNotNull(document.getShape(new Point(x,y)));
	}
	
	@Test
	@Order(3)
	public void moveShape() {
		assertEquals(document.setIndex(0), true);
		assertEquals(document.getPage().setIndex(1), true);
		document.cache.pressedImage = document.getPage().getImage();
		assertEquals(document.getPage().setIndex(0), true);
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
		assertNotNull(document.getPage().getShape(new Point(x,y)));
	}
}
