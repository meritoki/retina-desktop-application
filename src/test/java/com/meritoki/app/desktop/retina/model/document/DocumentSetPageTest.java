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

import com.meritoki.app.desktop.retina.model.document.Document;
import com.meritoki.app.desktop.retina.model.document.Image;
import com.meritoki.app.desktop.retina.model.document.Page;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DocumentSetPageTest {

	
	static Logger logger = LogManager.getLogger(DocumentSetImageTest.class.getName());
	static Document document = null;
	static Page pageZero;
	static Page pageOne;
	static Page pageTwo;
	
	@BeforeAll
	public static void initialize() {
		document = new Document();
		Page page = new Page(new Image(new File("./data/image/01.jpg")));
		document.addPage(page);
		page = new Page(new Image(new File("./data/image/02.jpg")));
		document.addPage(page);
		page = new Page(new Image(new File("./data/image/03.jpg")));
		document.addPage(page);
		pageZero = document.getPage(0);
		pageOne = document.getPage(1);
		pageTwo = document.getPage(2);
		assertEquals(document.pageList.size(),3);
	}
	
	@Test
	@Order(1)
	public void setPage() {
		document.cache.pageIndex = 0;
		try {
			document.pattern.execute("setPage");
		} catch(Exception e) {
			logger.error("Exception "+e.getMessage());
		}
		assertEquals(document.getPage().uuid,pageZero.uuid);
		
		document.cache.pageIndex = 1;
		try {
			document.pattern.execute("setPage");
		} catch(Exception e) {
			logger.error("Exception "+e.getMessage());
		}
		assertEquals(document.getPage().uuid,pageOne.uuid);
		
		document.cache.pageIndex = 2;
		try {
			document.pattern.execute("setPage");
		} catch(Exception e) {
			logger.error("Exception "+e.getMessage());
		}
		assertEquals(document.getPage().uuid,pageTwo.uuid);
		
		document.cache.pageIndex = -1;
		document.cache.pageUUID = pageZero.uuid;
		try {
			document.pattern.execute("setPage");
		} catch(Exception e) {
			logger.error("Exception "+e.getMessage());
		}
		assertEquals(document.getPage().uuid,pageZero.uuid);
		
		document.cache.pageIndex = -1;
		document.cache.pageUUID = pageOne.uuid;
		try {
			document.pattern.execute("setPage");
		} catch(Exception e) {
			logger.error("Exception "+e.getMessage());
		}
		assertEquals(document.getPage().uuid,pageOne.uuid);
		
		document.cache.pageIndex = -1;
		document.cache.pageUUID = pageTwo.uuid;
		try {
			document.pattern.execute("setPage");
		} catch(Exception e) {
			logger.error("Exception "+e.getMessage());
		}
		assertEquals(document.getPage().uuid,pageTwo.uuid);
	}

	@Test
	@Order(2)
	public void undo() {
		assertEquals(document.getPage().uuid,pageTwo.uuid);
		document.pattern.undo();
		assertEquals(document.getPage().uuid,pageOne.uuid);
		document.pattern.undo();
		assertEquals(document.getPage().uuid,pageZero.uuid);
		document.pattern.undo();
		assertEquals(document.getPage().uuid,pageTwo.uuid);
		document.pattern.undo();
		assertEquals(document.getPage().uuid,pageOne.uuid);
		document.pattern.undo();
		assertEquals(document.getPage().uuid,pageZero.uuid);
		document.pattern.undo();
		
	}
	
	@Test
	@Order(3)
	public void redo() {
		document.pattern.redo();
		assertEquals(document.getPage().uuid,pageZero.uuid);
		document.pattern.redo();
		assertEquals(document.getPage().uuid,pageOne.uuid);
		document.pattern.redo();
		assertEquals(document.getPage().uuid,pageTwo.uuid);
		document.pattern.redo();
		assertEquals(document.getPage().uuid,pageZero.uuid);
		document.pattern.redo();
		assertEquals(document.getPage().uuid,pageOne.uuid);
		document.pattern.redo();
		assertEquals(document.getPage().uuid,pageTwo.uuid);
	}
}
