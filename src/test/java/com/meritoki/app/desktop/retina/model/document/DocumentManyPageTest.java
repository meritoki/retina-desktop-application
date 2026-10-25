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

import com.meritoki.app.desktop.retina.controller.node.NodeController;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DocumentManyPageTest {

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
		for(int i=0;i<limit;i++) {
			Page page = new Page(new Image(new File("./test/img/test.png")));
			document.addPage(page);
		}
		assertEquals(document.pageList.size(),limit);
	}
	
	@Test
	@Order(2)
	public void savePages() {
		NodeController.saveDocument(new java.io.File("./test/document-many-page-test.json"), document);
	}
}
