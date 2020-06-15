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

import com.meritoki.app.desktop.retina.model.document.Dimension;
import com.meritoki.app.desktop.retina.model.document.Document;
import com.meritoki.app.desktop.retina.model.document.Image;
import com.meritoki.app.desktop.retina.model.document.Page;
import com.meritoki.app.desktop.retina.model.document.Point;
import com.meritoki.app.desktop.retina.model.document.Shape;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DocumentAddShapeScalePageTest {
	static Logger logger = LogManager.getLogger(DocumentAddShapeScalePageTest.class.getName());
	static Document document = null;
	static String pageZeroUUID = null;
	static Dimension origin = null;
	static int dimension = 4;
	
	@BeforeAll
	public static void initialize() {
		document = new Document();
		Page page = new Page();
		page = new Page(new Image(new File("./data/image/01.jpg")));
		pageZeroUUID = page.uuid;
		document.addPage(page);
		page = new Page();
		assertEquals(document.pageList.size(),1);
	}
	
	@Test
	@Order(1)
	public void addShape() {
		assertEquals(document.setIndex(0), true);
		assertEquals(document.getPage().setIndex(0), true);
		document.cache.pressedImage = document.getImage();
		int x = (int) (document.cache.pressedImage.position.absoluteDimension.width / 2 - dimension / 2);
		int y = (int) (document.cache.pressedImage.position.absoluteDimension.height / 2 - dimension / 2);
		int width = dimension;
		int height = dimension;
		document.cache.pressedPoint = new Point(x, y);
		document.cache.releasedPoint = new Point(x + width, y + height);
		try {
			document.pattern.execute("addShape");
		} catch (Exception e) {
			logger.error("Exception " + e.getMessage());
		}
	}
	
	@Test
	@Order(2)
	public void scaleUp() {
		assertEquals(document.setIndex(0), true);
		assertEquals(document.getPage().setIndex(0), true);
		document.cache.pressedImage = document.getImage();
		origin = new Dimension(document.cache.pressedImage.position.dimension);
		document.cache.scaleOperator = '*';
		document.cache.scaleFactor = 1.5;
		try {
			document.pattern.execute("scalePage");
			document.pattern.execute("scalePage");
			document.pattern.execute("scalePage");
			document.pattern.execute("scalePage");
		} catch (Exception e) {
			logger.error("Exception " + e.getMessage());
		} 
		double x = document.cache.pressedImage.position.center.x;
		double y = document.cache.pressedImage.position.center.y;
		assertNotNull(document.getShape(new Point(x,y)));
	}
	
	@Test
	@Order(3)
	public void scaleDown() {
		assertEquals(document.setIndex(0), true);
		assertEquals(document.getPage().setIndex(0), true);
		document.cache.pressedImage = document.getImage();
		document.cache.scaleOperator = '/';
		document.cache.scaleFactor = 1.5;
		try {
			document.pattern.execute("scalePage");
			document.pattern.execute("scalePage");
			document.pattern.execute("scalePage");
			document.pattern.execute("scalePage");
		} catch (Exception e) {
			logger.error("Exception " + e.getMessage());
		} 
		double x = document.cache.pressedImage.position.center.x;
		double y = document.cache.pressedImage.position.center.y;
		Shape shape = document.getShape(new Point(x,y));
		assertNotNull(shape);
		assertEquals(shape.position.dimension.width,dimension);
		assertEquals(shape.position.dimension.height,dimension);
	}
	
	@Test
	@Order(4)
	public void undo() {
		//scale down
		document.pattern.undo();
		document.pattern.undo();
		document.pattern.undo();
		document.pattern.undo();
		//scale up
		document.pattern.undo();
		document.pattern.undo();
		document.pattern.undo();
		document.pattern.undo();
		assertEquals(origin.width,document.cache.pressedImage.position.dimension.width);
		assertEquals(origin.height,document.cache.pressedImage.position.dimension.height);
		//undo add shape
		document.pattern.undo();
		assertEquals(document.getShapeList().size(),0);
	}
	
	@Test
	@Order(5)
	public void redo() {
		
	}
}
