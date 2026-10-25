package com.meritoki.app.desktop.retina.model.document;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
public class DocumentExecuteScriptTest {

	static Logger logger = LogManager.getLogger(DocumentExecuteScriptTest.class.getName());
	static Model model = new Model();
	static String pageZeroUUID = null;
	static String pageOneUUID = null;
	static String pageTwoUUID = null;
	static String pageThreeUUID = null;
	static String pageFourUUID = null;
	
	static int dimension = 256;
	
	@BeforeAll
	public static void initialize() {
		model.document = new Document();
		Page page = new Page();
		page = new Page(new Image(new File("./test/img/test.png")));
		pageZeroUUID = page.uuid;
		model.document.addPage(page);
		page = new Page();
		page.addImage(new Image(new File("./test/img/test.png")));
		pageOneUUID = page.uuid;
		model.document.addPage(page);
		page = new Page();
		page.addImage(new Image(new File("./test/img/test.png")));
		page.addImage(new Image(new File("./test/img/test.png")));
		pageTwoUUID = page.uuid;
		model.document.addPage(page);
		page = new Page();
		page.addImage(new Image(new File("./test/img/test.png")));
		page.addImage(new Image(new File("./test/img/test.png")));
		page.addImage(new Image(new File("./test/img/test.png")));
		pageThreeUUID = page.uuid;
		model.document.addPage(page);
		assertEquals(model.document.pageList.size(),4);
	}
	
	@Test
	@Order(1)
	public void addShapes() {
		assertEquals(model.document.setIndex(0), true);
		model.document.getPage().getBufferedImage(model);
		assertEquals(model.document.getPage().setIndex(0),true);
		model.system.pressedImage = model.document.getImage();
		int x = (int)(model.system.pressedImage.position.absoluteDimension.width/2 - dimension/2);
		int y = (int)(model.system.pressedImage.position.absoluteDimension.height/2 - dimension/2);
		int width = dimension;
		int height = dimension;
		model.cache.pressedPoint = new Point(x,y);
		model.cache.releasedPoint = new Point(x+width, y+height);
    	try {
			model.cache.pressedPageUUID = model.document.getPage().uuid;
			model.cache.pressedImageUUID = model.system.pressedImage.uuid;
			model.pattern.execute("addShape");
		} catch (Exception e) {
			logger.error("Exception "+e.getMessage());
		}
		assertEquals(model.document.setIndex(1), true);
		model.document.getPage().getBufferedImage(model);
		model.system.pressedImage = model.document.getImage();
		x = (int)(model.system.pressedImage.position.absoluteDimension.width/2 - dimension/2);
		y = (int)(model.system.pressedImage.position.absoluteDimension.height/2 - dimension/2);
		width = dimension;
		height = dimension;
		model.cache.pressedPoint = new Point(x,y);
		model.cache.releasedPoint = new Point(x+width, y+height);
    	try {
			model.cache.pressedPageUUID = model.document.getPage().uuid;
			model.cache.pressedImageUUID = model.system.pressedImage.uuid;
			model.pattern.execute("addShape");
		} catch (Exception e) {
			logger.error("Exception "+e.getMessage());
		}
    	
		assertEquals(model.document.setIndex(2), true);
		model.document.getPage().getBufferedImage(model);
		model.system.pressedImage = model.document.getImage();
		x = (int)(model.system.pressedImage.position.absoluteDimension.width/2 - dimension/2);
		y = (int)(model.system.pressedImage.position.absoluteDimension.height/2 - dimension/2);
		width = dimension;
		height = dimension;
		model.cache.pressedPoint = new Point(x,y);
		model.cache.releasedPoint = new Point(x+width, y+height);
    	try {
			model.cache.pressedPageUUID = model.document.getPage().uuid;
			model.cache.pressedImageUUID = model.system.pressedImage.uuid;
			model.pattern.execute("addShape");
		} catch (Exception e) {
			logger.error("Exception "+e.getMessage());
		}
    	
		assertEquals(model.document.setIndex(3), true);
		model.document.getPage().getBufferedImage(model);
		model.system.pressedImage = model.document.getImage();
		x = (int)(model.system.pressedImage.position.absoluteDimension.width/2 - dimension/2);
		y = (int)(model.system.pressedImage.position.absoluteDimension.height/2 - dimension/2);
		width = dimension;
		height = dimension;
		model.cache.pressedPoint = new Point(x,y);
		model.cache.releasedPoint = new Point(x+width, y+height);
    	try {
			model.cache.pressedPageUUID = model.document.getPage().uuid;
			model.cache.pressedImageUUID = model.system.pressedImage.uuid;
			model.pattern.execute("addShape");
		} catch (Exception e) {
			logger.error("Exception "+e.getMessage());
		}
	}
	
	@Test
	@Order(2) 
	public void executeScript() {
		model.cache.script = "JOIN 0:1; SPLIT 1; SWAP 0:1; INSERT 2:1;";
    	model.cache.pageList = model.document.getPageList();
    	try {
			model.pattern.execute("executeScript");
		} catch (Exception e) {
			logger.error("Exception "+e.getMessage());
		}
	}
	
	@Test
	@Order(3)
	public void verify() {
		assertEquals(model.document.pageList.size(),4);
	}
	
	@Test
	@Order(4)
	public void undo() {
		try {
		model.pattern.undo();
		assertEquals(model.document.pageList.size(),4);
		assertEquals(model.document.setIndex(0), true);
		System.out.println(model.document.getPage().uuid);
		assertEquals(model.document.getPage().uuid,pageZeroUUID);
		
		assertEquals(model.document.setIndex(1), true);
		System.out.println(model.document.getPage().uuid);
		assertEquals(model.document.getPage().uuid,pageOneUUID);
		
		assertEquals(model.document.setIndex(2), true);
		System.out.println(model.document.getPage().uuid);
		assertEquals(model.document.getPage().uuid,pageTwoUUID);
		
		assertEquals(model.document.setIndex(3), true);
		System.out.println(model.document.getPage().uuid);
		assertEquals(model.document.getPage().uuid,pageThreeUUID);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
