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
public class DocumentResizeImageTest {

	static Logger logger = LogManager.getLogger(DocumentResizeImageTest.class.getName());
	static Model model = new Model();
	static String pageZeroUUID = null;
	static Dimension origin = null;
	static int dimension = 4;
	
	@BeforeAll
	public static void initialize() {
		model.document = new Document();
		Page page = new Page();
		page = new Page(new Image(new File("./data/image/01.jpg")));
		pageZeroUUID = page.uuid;
		model.document.addPage(page);
		page = new Page();
		assertEquals(model.document.pageList.size(),1);
	}
	
	@Test
	@Order(1) 
	public void resize() {
		assertEquals(model.document.setIndex(0), true);
		assertEquals(model.document.getPage().setIndex(0), true);
		model.system.pressedImage = model.document.getImage();
		origin = new Dimension(model.system.pressedImage.position.dimension);
		model.cache.scaleOperator = '/';
		model.cache.scaleFactor = 1.01;
		try {
			model.pattern.execute("resizeImage");
			model.pattern.execute("resizeImage");
			model.pattern.execute("resizeImage");
			model.pattern.execute("resizeImage");
		} catch (Exception e) {
			logger.error("Exception "+e.getMessage());
		}
		assertEquals(model.document.getPage().setIndex(0), true);
		model.system.pressedPage = model.document.getPage();
		model.system.pressedImage = model.document.getImage();
		model.cache.scaleOperator = '*';
		model.cache.scaleFactor = 1.01;
		try {
			model.pattern.execute("resizeImage");
			model.pattern.execute("resizeImage");
			model.pattern.execute("resizeImage");
			model.pattern.execute("resizeImage");
		} catch (Exception e) {
			logger.error("Exception "+e.getMessage());
		}
	}
	
	@Test
	@Order(2)
	public void undo() {
		//scale down
		model.pattern.undo();
		model.pattern.undo();
		model.pattern.undo();
		model.pattern.undo();
		//scale up
		model.pattern.undo();
		model.pattern.undo();
		model.pattern.undo();
		model.pattern.undo();
		model.pattern.undo();
		logger.info(origin);
		logger.info(model.system.pressedImage);
		assertEquals(origin.width,model.system.pressedImage.position.dimension.width);
		assertEquals(origin.height,model.system.pressedImage.position.dimension.height);
	}
}
