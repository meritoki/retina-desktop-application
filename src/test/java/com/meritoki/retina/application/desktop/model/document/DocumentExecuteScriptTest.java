package com.meritoki.retina.application.desktop.model.document;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
public class DocumentExecuteScriptTest {

	static Logger logger = LogManager.getLogger(DocumentExecuteScriptTest.class.getName());
	static Document document = null;
	static String pageZeroUUID = null;
	static String pageOneUUID = null;
	static String pageTwoUUID = null;
	static String pageThreeUUID = null;
	static String pageFourUUID = null;
	
	static int dimension = 256;
	
	@BeforeAll
	public static void initialize() {
		document = new Document();
		Page page = new Page();
		page = new Page(new Image(new File("./data/image/01.jpg")));
		pageZeroUUID = page.uuid;
		document.addPage(page);
		page = new Page();
		page.addImage(new Image(new File("./data/image/02.jpg")));
		pageOneUUID = page.uuid;
		document.addPage(page);
		page = new Page();
		page.addImage(new Image(new File("./data/image/03.jpg")));
		page.addImage(new Image(new File("./data/image/04.jpg")));
		pageTwoUUID = page.uuid;
		document.addPage(page);
		page = new Page();
		page.addImage(new Image(new File("./data/image/05.jpg")));
		page.addImage(new Image(new File("./data/image/06.jpg")));
		page.addImage(new Image(new File("./data/image/07.jpg")));
		pageThreeUUID = page.uuid;
		document.addPage(page);
		assertEquals(document.pageList.size(),4);
	}
	
	@Test
	@Order(1)
	public void addShapes() {
		assertEquals(document.setIndex(0), true);
		assertEquals(document.getPage().setIndex(0),true);
		document.cache.pressedImage = document.getImage();
		int x = (int)(document.cache.pressedImage.position.absoluteDimension.width/2 - dimension/2);
		int y = (int)(document.cache.pressedImage.position.absoluteDimension.height/2 - dimension/2);
		int width = dimension;
		int height = dimension;
		document.cache.pressedPoint = new Point(x,y);
		document.cache.releasedPoint = new Point(x+width, y+height);
    	try {
			document.pattern.execute("addShape");
		} catch (Exception e) {
			logger.error("Exception "+e.getMessage());
		}
		assertEquals(document.setIndex(1), true);
		document.cache.pressedImage = document.getImage();
		x = (int)(document.cache.pressedImage.position.absoluteDimension.width/2 - dimension/2);
		y = (int)(document.cache.pressedImage.position.absoluteDimension.height/2 - dimension/2);
		width = dimension;
		height = dimension;
		document.cache.pressedPoint = new Point(x,y);
		document.cache.releasedPoint = new Point(x+width, y+height);
    	try {
			document.pattern.execute("addShape");
		} catch (Exception e) {
			logger.error("Exception "+e.getMessage());
		}
    	
		assertEquals(document.setIndex(2), true);
		document.cache.pressedImage = document.getImage();
		x = (int)(document.cache.pressedImage.position.absoluteDimension.width/2 - dimension/2);
		y = (int)(document.cache.pressedImage.position.absoluteDimension.height/2 - dimension/2);
		width = dimension;
		height = dimension;
		document.cache.pressedPoint = new Point(x,y);
		document.cache.releasedPoint = new Point(x+width, y+height);
    	try {
			document.pattern.execute("addShape");
		} catch (Exception e) {
			logger.error("Exception "+e.getMessage());
		}
    	
		assertEquals(document.setIndex(3), true);
		document.cache.pressedImage = document.getImage();
		x = (int)(document.cache.pressedImage.position.absoluteDimension.width/2 - dimension/2);
		y = (int)(document.cache.pressedImage.position.absoluteDimension.height/2 - dimension/2);
		width = dimension;
		height = dimension;
		document.cache.pressedPoint = new Point(x,y);
		document.cache.releasedPoint = new Point(x+width, y+height);
    	try {
			document.pattern.execute("addShape");
		} catch (Exception e) {
			logger.error("Exception "+e.getMessage());
		}
	}
	
	@Test
	@Order(2) 
	public void executeScript() {
		document.cache.script = "JOIN 0:1; SPLIT 1;";
    	document.cache.pageList = document.getPageList();
    	try {
			document.pattern.execute("executeScript");
		} catch (Exception e) {
			logger.error("Exception "+e.getMessage());
		}
	}
	
	@Test
	@Order(3)
	public void verify() {
		assertEquals(document.pageList.size(),4);
	}
	
	@Test
	@Order(4)
	public void undo() {
		document.pattern.undo();
		assertEquals(document.pageList.size(),4);
		assertEquals(document.setIndex(0), true);
		System.out.println(document.getPage().uuid);
		assertEquals(document.getPage().uuid,pageZeroUUID);
		
		assertEquals(document.setIndex(1), true);
		System.out.println(document.getPage().uuid);
		assertEquals(document.getPage().uuid,pageOneUUID);
		
		assertEquals(document.setIndex(2), true);
		System.out.println(document.getPage().uuid);
		assertEquals(document.getPage().uuid,pageTwoUUID);
		
		assertEquals(document.setIndex(3), true);
		System.out.println(document.getPage().uuid);
		assertEquals(document.getPage().uuid,pageThreeUUID);
	}
}
