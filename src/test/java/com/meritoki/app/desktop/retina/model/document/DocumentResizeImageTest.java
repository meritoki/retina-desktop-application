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

import com.meritoki.app.desktop.retina.model.document.Dimension;
import com.meritoki.app.desktop.retina.model.document.Document;
import com.meritoki.app.desktop.retina.model.document.Image;
import com.meritoki.app.desktop.retina.model.document.Page;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DocumentResizeImageTest {

	static Logger logger = LogManager.getLogger(DocumentResizeImageTest.class.getName());
	static Document document = null;
	static String pageZeroUUID = null;
	static Dimension origin = null;
	static int dimension = 4;
	
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
	public void resize() {
		assertEquals(document.setIndex(0), true);
		assertEquals(document.getPage().setIndex(0), true);
		document.cache.pressedImage = document.getImage();
		origin = new Dimension(document.cache.pressedImage.position.dimension);
		document.cache.scaleOperator = '/';
		document.cache.scaleFactor = 1.01;
		try {
			document.pattern.execute("resizeImage");
			document.pattern.execute("resizeImage");
			document.pattern.execute("resizeImage");
			document.pattern.execute("resizeImage");
		} catch (Exception e) {
			logger.error("Exception "+e.getMessage());
		}
		assertEquals(document.getPage().setIndex(0), true);
		document.cache.pressedPage = document.getPage();
		document.cache.pressedImage = document.getImage();
		document.cache.scaleOperator = '*';
		document.cache.scaleFactor = 1.01;
		try {
			document.pattern.execute("resizeImage");
			document.pattern.execute("resizeImage");
			document.pattern.execute("resizeImage");
			document.pattern.execute("resizeImage");
		} catch (Exception e) {
			logger.error("Exception "+e.getMessage());
		}
	}
	
	@Test
	@Order(2)
	public void undo() {
		//scale down
		document.pattern.undo();
		document.pattern.undo();
		document.pattern.undo();
		document.pattern.undo();
		//scale up
		document.pattern.undo();
		document.pattern.undo();
		document.pattern.undo();
		document.pattern.undo();
		document.pattern.undo();
		logger.info(origin);
		logger.info(document.cache.pressedImage);
		assertEquals(origin.width,document.cache.pressedImage.position.dimension.width);
		assertEquals(origin.height,document.cache.pressedImage.position.dimension.height);
	}
}
