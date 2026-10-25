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
public class DocumentSetPageTest {

	
	static Logger logger = LogManager.getLogger(DocumentSetImageTest.class.getName());
	static Model model = new Model();
	static Page pageZero;
	static Page pageOne;
	static Page pageTwo;
	
	@BeforeAll
	public static void initialize() {
		model.document = new Document();
		Page page = new Page(new Image(new File("./test/img/test.png")));
		model.document.addPage(page);
		page = new Page(new Image(new File("./test/img/test.png")));
		model.document.addPage(page);
		page = new Page(new Image(new File("./test/img/test.png")));
		model.document.addPage(page);
		pageZero = model.document.getPage(0);
		pageOne = model.document.getPage(1);
		pageTwo = model.document.getPage(2);
		assertEquals(model.document.pageList.size(),3);
	}
	
	@Test
	@Order(1)
	public void setPage() {
		model.cache.pageIndex = 0;
		try {
			model.pattern.execute("setPage");
		} catch(Exception e) {
			logger.error("Exception "+e.getMessage());
		}
		assertEquals(model.document.getPage().uuid,pageZero.uuid);
		
		model.cache.pageIndex = 1;
		try {
			model.pattern.execute("setPage");
		} catch(Exception e) {
			logger.error("Exception "+e.getMessage());
		}
		assertEquals(model.document.getPage().uuid,pageOne.uuid);
		
		model.cache.pageIndex = 2;
		try {
			model.pattern.execute("setPage");
		} catch(Exception e) {
			logger.error("Exception "+e.getMessage());
		}
		assertEquals(model.document.getPage().uuid,pageTwo.uuid);
		
		model.cache.pageIndex = -1;
		model.cache.pageUUID = pageZero.uuid;
		try {
			model.pattern.execute("setPage");
		} catch(Exception e) {
			logger.error("Exception "+e.getMessage());
		}
		assertEquals(model.document.getPage().uuid,pageZero.uuid);
		
		model.cache.pageIndex = -1;
		model.cache.pageUUID = pageOne.uuid;
		try {
			model.pattern.execute("setPage");
		} catch(Exception e) {
			logger.error("Exception "+e.getMessage());
		}
		assertEquals(model.document.getPage().uuid,pageOne.uuid);
		
		model.cache.pageIndex = -1;
		model.cache.pageUUID = pageTwo.uuid;
		try {
			model.pattern.execute("setPage");
		} catch(Exception e) {
			logger.error("Exception "+e.getMessage());
		}
		assertEquals(model.document.getPage().uuid,pageTwo.uuid);
	}

	@Test
	@Order(2)
	public void undo() {
		try {
		assertEquals(model.document.getPage().uuid,pageTwo.uuid);
		model.pattern.undo();
		assertEquals(model.document.getPage().uuid,pageOne.uuid);
		model.pattern.undo();
		assertEquals(model.document.getPage().uuid,pageZero.uuid);
		model.pattern.undo();
		assertEquals(model.document.getPage().uuid,pageTwo.uuid);
		model.pattern.undo();
		assertEquals(model.document.getPage().uuid,pageOne.uuid);
		model.pattern.undo();
		assertEquals(model.document.getPage().uuid,pageZero.uuid);
		model.pattern.undo();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	@Order(3)
	public void redo() {
		try {
		model.pattern.redo();
		assertEquals(model.document.getPage().uuid,pageZero.uuid);
		model.pattern.redo();
		assertEquals(model.document.getPage().uuid,pageOne.uuid);
		model.pattern.redo();
		assertEquals(model.document.getPage().uuid,pageTwo.uuid);
		model.pattern.redo();
		assertEquals(model.document.getPage().uuid,pageZero.uuid);
		model.pattern.redo();
		assertEquals(model.document.getPage().uuid,pageOne.uuid);
		model.pattern.redo();
		assertEquals(model.document.getPage().uuid,pageTwo.uuid);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
