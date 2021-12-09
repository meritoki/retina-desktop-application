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
public class DocumentManyPageManyImageTest {

	static Logger logger = LogManager.getLogger(DocumentResizeShapeTest.class.getName());
	static Document document = null;
	int pageLimit = 8;
	int imageLimit = 3;
	
	@BeforeAll
	public static void initialize() {
		document = new Document();
	}
	
	@Test
	@Order(1)
	public void addPages() {
		for(int i=0;i<pageLimit;i++) {
			Page page = new Page();
			for(int j=0;j<imageLimit;j++) {
				page.addImage(new Image(new File("./test/img/test.png")));
			}
			document.addPage(page);
		}
		assertEquals(document.pageList.size(),pageLimit);
	}
	
	@Test
	@Order(2)
	public void savePages() {
		NodeController.saveDocument(new java.io.File("./test/document-many-page-many-image-test.json"), document);
	}
}
