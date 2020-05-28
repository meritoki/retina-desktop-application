package com.meritoki.retina.application.desktop.model.document;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.awt.Graphics;
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

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DocumentScaleTest {
	static Logger logger = LogManager.getLogger(DocumentExecuteScriptTest.class.getName());
	static Document document = null;
	static String pageZeroUUID = null;
	
	static int dimension = 256;
	
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
	public void scaleGrow() {
		assertEquals(document.setIndex(0), true);
		assertEquals(document.getPage().setIndex(0), true);
		document.cache.pressedImage = document.getImage();
		double scale = document.getPage().position.scale;
		scale *= 1.5;
		document.getPage().setScale(scale);
		int x = (int) (document.cache.pressedImage.position.dimension.width / 2);
		int y = (int) (document.cache.pressedImage.position.dimension.height / 2);
		assertNotNull(document.getPage().getShape(new Point(x,y)));
		double shapeScale = 256*1.5;
		x = (int) (document.cache.pressedImage.position.dimension.width / 2 - shapeScale/2);
		y = (int) (document.cache.pressedImage.position.dimension.height / 2 - shapeScale/2);
		assertNotNull(document.getPage().getShape(new Point(x,y)));
		assertNull(document.getPage().getShape(new Point(x-1,y-1)));
		x = (int) (document.cache.pressedImage.position.dimension.width / 2 + shapeScale/2);
		y = (int) (document.cache.pressedImage.position.dimension.height / 2 + shapeScale/2);
		assertNotNull(document.getPage().getShape(new Point(x,y)));
		assertNull(document.getPage().getShape(new Point(x+1,y+1)));
	}
	
	@Test
	@Order(3) 
	public void scaleShrink() {
		assertEquals(document.setIndex(0), true);
		assertEquals(document.getPage().setIndex(0), true);
		document.cache.pressedImage = document.getImage();
		double scale = document.getPage().position.scale;
		scale /= 1.5;
		document.getPage().setScale(scale);
		int x = (int) (document.cache.pressedImage.position.dimension.width / 2);
		int y = (int) (document.cache.pressedImage.position.dimension.height / 2);
		assertNotNull(document.getPage().getShape(new Point(x,y)));
		double shapeScale = 256;
		x = (int) (document.cache.pressedImage.position.dimension.width / 2 - shapeScale/2);
		y = (int) (document.cache.pressedImage.position.dimension.height / 2 - shapeScale/2);
		assertNotNull(document.getPage().getShape(new Point(x,y)));
		assertNull(document.getPage().getShape(new Point(x-1,y-1)));
		x = (int) (document.cache.pressedImage.position.dimension.width / 2 + shapeScale/2);
		y = (int) (document.cache.pressedImage.position.dimension.height / 2 + shapeScale/2);
		assertNotNull(document.getPage().getShape(new Point(x,y)));
		assertNull(document.getPage().getShape(new Point(x+1,y+1)));
	}
}
