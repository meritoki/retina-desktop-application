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
public class DocumentScalePageTest {
	static Logger logger = LogManager.getLogger(DocumentScalePageTest.class.getName());
	static Model model = new Model();
	static String pageZeroUUID = null;
	static Dimension source = null;
	static Dimension sink = null;
	
	@BeforeAll
	public static void initialize() {
		model.document = new Document();
		Page page = new Page();
		page = new Page(new Image(new File("./test/img/test.png")));
		pageZeroUUID = page.uuid;
		model.document.addPage(page);
		page = new Page();
		assertEquals(model.document.pageList.size(),1);
	}
	
	@Test
	@Order(1)
	public void scale() {
		assertEquals(model.document.setIndex(0), true);
		assertEquals(model.document.getPage().setIndex(0), true);
		model.system.pressedImage = model.document.getImage();
		source = new Dimension(model.system.pressedImage.position.dimension);
		model.cache.scaleOperator = '*';
		model.cache.scaleFactor = 1.5;
		try {
			model.pattern.execute("scalePage");
			model.pattern.execute("scalePage");
			model.pattern.execute("scalePage");
			model.pattern.execute("scalePage");
		} catch (Exception e) {
			logger.error("Exception " + e.getMessage());
		}
		model.cache.scaleOperator = '/';
		try {
			model.pattern.execute("scalePage");
			model.pattern.execute("scalePage");
			model.pattern.execute("scalePage");
			model.pattern.execute("scalePage");
		} catch (Exception e) {
			logger.error("Exception " + e.getMessage());
		} 
		sink = new Dimension(model.system.pressedImage.position.dimension);
	}
	
	@Test
	@Order(2)
	public void undo() {
		try {
		model.pattern.undo();
		model.pattern.undo();
		model.pattern.undo();
		model.pattern.undo();
		model.pattern.undo();
		model.pattern.undo();
		model.pattern.undo();
		model.pattern.undo();
		assertEquals(source.width,model.system.pressedImage.position.dimension.width);
		assertEquals(source.height,model.system.pressedImage.position.dimension.height);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	@Order(3)
	public void redo() {
		try {
		model.pattern.redo();
		model.pattern.redo();
		model.pattern.redo();
		model.pattern.redo();
		model.pattern.redo();
		model.pattern.redo();
		model.pattern.redo();
		model.pattern.redo();
		assertEquals(sink.width,model.system.pressedImage.position.dimension.width);
		assertEquals(sink.height,model.system.pressedImage.position.dimension.height);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
