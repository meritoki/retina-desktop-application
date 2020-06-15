package com.meritoki.app.desktop.retina.model.document;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
public class DocumentSetImageTest {

	static Logger logger = LogManager.getLogger(DocumentSetImageTest.class.getName());
	static Document document = null;
	static Image imageZero;
	static Image imageOne;
	static Image imageTwo;
	
	@BeforeAll
	public static void initialize() {
		document = new Document();
		Page page = new Page();
		page.addImage(new Image(new File("./data/image/01.jpg")));
		page.addImage(new Image(new File("./data/image/02.jpg")));
		page.addImage(new Image(new File("./data/image/03.jpg")));
		document.addPage(page);
		imageZero = page.getImage(0);
		imageOne = page.getImage(1);
		imageTwo = page.getImage(2);
		assertEquals(document.pageList.size(),1);
	}
	
	@Test
	@Order(1)
	public void setImage() {
		document.cache.imageUUID = imageZero.uuid;
		try {
			document.pattern.execute("setImage");
		} catch (Exception e) {
			logger.error("Exception "+e.getMessage());
		}
		assertEquals(document.getImage().uuid, imageZero.uuid);
		document.cache.imageUUID = imageOne.uuid;
		try {
			document.pattern.execute("setImage");
		} catch (Exception e) {
			logger.error("Exception "+e.getMessage());
		}
		assertEquals(document.getImage().uuid, imageOne.uuid);
		document.cache.imageUUID = imageTwo.uuid;
		try {
			document.pattern.execute("setImage");
		} catch (Exception e) {
			logger.error("Exception "+e.getMessage());
		}
		assertEquals(document.getImage().uuid, imageTwo.uuid);
	}
	
	@Test
	@Order(2)
	public void undo() {
		document.pattern.undo();
		assertEquals(document.getImage().uuid, imageOne.uuid);
		document.pattern.undo();
		assertEquals(document.getImage().uuid, imageZero.uuid);
		document.pattern.undo();
		assertEquals(document.getImage().uuid, imageZero.uuid);
	}
	
	@Test
	@Order(3)
	public void redo() {
		document.pattern.redo();
		assertEquals(document.getImage().uuid, imageZero.uuid);
		document.pattern.redo();
		assertEquals(document.getImage().uuid, imageOne.uuid);
		document.pattern.redo();
		assertEquals(document.getImage().uuid, imageTwo.uuid);
	}
}
