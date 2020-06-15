package com.meritoki.app.desktop.retina.model.document;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
import com.meritoki.app.desktop.retina.model.document.Point;
import com.meritoki.app.desktop.retina.model.document.Shape;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DocumentScalePageTest {
	static Logger logger = LogManager.getLogger(DocumentScalePageTest.class.getName());
	static Document document = null;
	static String pageZeroUUID = null;
	static Dimension source = null;
	static Dimension sink = null;
	
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
	public void scale() {
		assertEquals(document.setIndex(0), true);
		assertEquals(document.getPage().setIndex(0), true);
		document.cache.pressedImage = document.getImage();
		source = new Dimension(document.cache.pressedImage.position.dimension);
		document.cache.scaleOperator = '*';
		document.cache.scaleFactor = 1.5;
		try {
			document.pattern.execute("scalePage");
			document.pattern.execute("scalePage");
			document.pattern.execute("scalePage");
			document.pattern.execute("scalePage");
		} catch (Exception e) {
			logger.error("Exception " + e.getMessage());
		}
		document.cache.scaleOperator = '/';
		try {
			document.pattern.execute("scalePage");
			document.pattern.execute("scalePage");
			document.pattern.execute("scalePage");
			document.pattern.execute("scalePage");
		} catch (Exception e) {
			logger.error("Exception " + e.getMessage());
		} 
		sink = new Dimension(document.cache.pressedImage.position.dimension);
	}
	
	@Test
	@Order(2)
	public void undo() {
		document.pattern.undo();
		document.pattern.undo();
		document.pattern.undo();
		document.pattern.undo();
		document.pattern.undo();
		document.pattern.undo();
		document.pattern.undo();
		document.pattern.undo();
		assertEquals(source.width,document.cache.pressedImage.position.dimension.width);
		assertEquals(source.height,document.cache.pressedImage.position.dimension.height);
	}
	
	@Test
	@Order(3)
	public void redo() {
		document.pattern.redo();
		document.pattern.redo();
		document.pattern.redo();
		document.pattern.redo();
		document.pattern.redo();
		document.pattern.redo();
		document.pattern.redo();
		document.pattern.redo();
		assertEquals(sink.width,document.cache.pressedImage.position.dimension.width);
		assertEquals(sink.height,document.cache.pressedImage.position.dimension.height);
	}
}
