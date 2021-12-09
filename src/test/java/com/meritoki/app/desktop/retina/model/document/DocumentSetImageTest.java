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
public class DocumentSetImageTest {

	static Logger logger = LogManager.getLogger(DocumentSetImageTest.class.getName());
	static Model model = new Model();
	static Image imageZero;
	static Image imageOne;
	static Image imageTwo;
	
	@BeforeAll
	public static void initialize() {
		model.document = new Document();
		Page page = new Page();
		page.addImage(new Image(new File("./test/img/test.png")));
		page.addImage(new Image(new File("./test/img/test.png")));
		page.addImage(new Image(new File("./test/img/test.png")));
		model.document.addPage(page);
		imageZero = page.getImage(0);
		imageOne = page.getImage(1);
		imageTwo = page.getImage(2);
		assertEquals(model.document.pageList.size(),1);
	}
	
	@Test
	@Order(1)
	public void setImage() {
		
		try {
			model.cache.imageUUID = imageZero.uuid;
			model.cache.pressedImageUUID = imageZero.uuid;
			model.pattern.execute("setImage");
		} catch (Exception e) {
			logger.error("Exception "+e.getMessage());
		}
		assertEquals(model.document.getImage().uuid, imageZero.uuid);
		
		try {
			model.cache.pressedImageUUID = imageOne.uuid;
			model.cache.imageUUID = imageZero.uuid;
			model.pattern.execute("setImage");
		} catch (Exception e) {
			logger.error("Exception "+e.getMessage());
		}
		assertEquals(model.document.getImage().uuid, imageOne.uuid);
		
		try {
			model.cache.pressedImageUUID = imageTwo.uuid;
			model.cache.imageUUID = imageOne.uuid;
			model.pattern.execute("setImage");
		} catch (Exception e) {
			logger.error("Exception "+e.getMessage());
		}
		assertEquals(model.document.getImage().uuid, imageTwo.uuid);
	}
	
	@Test
	@Order(2)
	public void undo() {
		try {
		model.pattern.undo();
		assertEquals(model.document.getImage().uuid, imageOne.uuid);
		model.pattern.undo();
		assertEquals(model.document.getImage().uuid, imageZero.uuid);
		model.pattern.undo();
		assertEquals(model.document.getImage().uuid, imageZero.uuid);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	@Order(3)
	public void redo() {
		try {
		model.pattern.redo();
		assertEquals(model.document.getImage().uuid, imageZero.uuid);
		model.pattern.redo();
		assertEquals(model.document.getImage().uuid, imageOne.uuid);
		model.pattern.redo();
		assertEquals(model.document.getImage().uuid, imageTwo.uuid);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
