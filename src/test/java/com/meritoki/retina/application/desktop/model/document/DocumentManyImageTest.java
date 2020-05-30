package com.meritoki.retina.application.desktop.model.document;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.meritoki.app.desktop.retina.controller.document.DocumentController;
import com.meritoki.app.desktop.retina.model.document.Document;
import com.meritoki.app.desktop.retina.model.document.Image;
import com.meritoki.app.desktop.retina.model.document.Page;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DocumentManyImageTest {

	static Logger logger = LogManager.getLogger(DocumentResizeShapeTest.class.getName());
	static Document document = null;
	int limit = 16;
	
	@BeforeAll
	public static void initialize() {
		document = new Document();
	}
	
	@Test
	@Order(1)
	public void addPages() {
		Page page = new Page();
		for(int i=0;i<limit;i++) {
			page.addImage(new Image(new File("./data/image/01.jpg")));
		}
		document.addPage(page);
	}
	
	@Test
	@Order(2)
	public void savePages() {
		DocumentController.save(new java.io.File("./test/document-many-image-test.json"), document);
	}
}