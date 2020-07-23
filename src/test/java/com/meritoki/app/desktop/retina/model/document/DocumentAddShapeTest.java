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

import com.meritoki.app.desktop.retina.controller.document.DocumentController;
import com.meritoki.app.desktop.retina.model.Model;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DocumentAddShapeTest {
	static Logger logger = LogManager.getLogger(DocumentAddShapeTest.class.getName());
	static Model model = new Model();
	static String pageZeroUUID = null;
	static String pageOneUUID = null;
	static String pageTwoUUID = null;

	static int divisor = 100;

	@BeforeAll
	public static void initialize() {
		model.document = new Document();
		Page page = new Page();
		page = new Page(new Image(new File("./data/image/01.jpg")));
		pageZeroUUID = page.uuid;
		model.document.addPage(page);
		page = new Page();
		page.addImage(new Image(new File("./data/image/01.jpg")));
		page.addImage(new Image(new File("./data/image/02.jpg")));
		pageOneUUID = page.uuid;
		model.document.addPage(page);
		page = new Page();
		page.addImage(new Image(new File("./data/image/01.jpg")));
		page.addImage(new Image(new File("./data/image/02.jpg")));
		page.addImage(new Image(new File("./data/image/03.jpg")));
		pageTwoUUID = page.uuid;
		model.document.addPage(page);
		assertEquals(model.document.pageList.size(), 3);
	}
	
	@Test
	@Order(1)
	public void addShapes() {
		assertEquals(model.document.setIndex(0), true);
		assertEquals(model.document.getPage().setIndex(0), true);
		model.cache.pressedImage = model.document.getImage();
		Position position = model.cache.pressedImage.position;
		int width = (int)position.dimension.width;
		int height = (int)position.dimension.height;
		int widthQuotient = width/divisor;
		int heightQuotient = height/divisor;
		for(int i=0;i<widthQuotient;i++) {
			for(int j=0;j<heightQuotient;j++) {
				int x = (int)(position.point.x+(i*divisor));
				int y = (int)(position.point.y+(j*divisor));
				model.cache.pressedPoint = new Point(x, y);
				model.cache.releasedPoint = new Point(x + divisor, y + divisor);
				try {
					model.pattern.execute("addShape");
				} catch (Exception e) {
					logger.error("Exception " + e.getMessage());
				}
			}
		}
		assertEquals(model.document.setIndex(1), true);
		assertEquals(model.document.getPage().setIndex(0), true);
		model.cache.pressedImage = model.document.getImage();
		position = model.cache.pressedImage.position;
		width = (int)position.dimension.width;
		height = (int)position.dimension.height;
		widthQuotient = width/divisor;
		heightQuotient = height/divisor;
		for(int i=0;i<widthQuotient;i++) {
			for(int j=0;j<heightQuotient;j++) {
				int x = (int)(position.point.x+(i*divisor));
				int y = (int)(position.point.y+(j*divisor));
				model.cache.pressedPoint = new Point(x, y);
				model.cache.releasedPoint = new Point(x + divisor, y + divisor);
				try {
					model.pattern.execute("addShape");
				} catch (Exception e) {
					logger.error("Exception " + e.getMessage());
				}
			}
		}
		assertEquals(model.document.setIndex(1), true);
		assertEquals(model.document.getPage().setIndex(1), true);
		model.cache.pressedImage = model.document.getImage();
		position = model.cache.pressedImage.position;
		width = (int)position.dimension.width;
		height = (int)position.dimension.height;
		widthQuotient = width/divisor;
		heightQuotient = height/divisor;
		for(int i=0;i<widthQuotient;i++) {
			for(int j=0;j<heightQuotient;j++) {
				int x = (int)(position.point.x+(i*divisor));
				int y = (int)(position.point.y+(j*divisor));
				model.cache.pressedPoint = new Point(x, y);
				model.cache.releasedPoint = new Point(x + divisor, y + divisor);
				try {
					model.pattern.execute("addShape");
				} catch (Exception e) {
					logger.error("Exception " + e.getMessage());
				}
			}
		}
		assertEquals(model.document.setIndex(2), true);
		assertEquals(model.document.getPage().setIndex(0), true);
		model.cache.pressedImage = model.document.getImage();
		position = model.cache.pressedImage.position;
		width = (int)position.dimension.width;
		height = (int)position.dimension.height;
		widthQuotient = width/divisor;
		heightQuotient = height/divisor;
		for(int i=0;i<widthQuotient;i++) {
			for(int j=0;j<heightQuotient;j++) {
				int x = (int)(position.point.x+(i*divisor));
				int y = (int)(position.point.y+(j*divisor));
				model.cache.pressedPoint = new Point(x, y);
				model.cache.releasedPoint = new Point(x + divisor, y + divisor);
				try {
					model.pattern.execute("addShape");
				} catch (Exception e) {
					logger.error("Exception " + e.getMessage());
				}
			}
		}
		assertEquals(model.document.setIndex(2), true);
		assertEquals(model.document.getPage().setIndex(1), true);
		model.cache.pressedImage = model.document.getImage();
		position = model.cache.pressedImage.position;
		width = (int)position.dimension.width;
		height = (int)position.dimension.height;
		widthQuotient = width/divisor;
		heightQuotient = height/divisor;
		for(int i=0;i<widthQuotient;i++) {
			for(int j=0;j<heightQuotient;j++) {
				int x = (int)(position.point.x+(i*divisor));
				int y = (int)(position.point.y+(j*divisor));
				model.cache.pressedPoint = new Point(x, y);
				model.cache.releasedPoint = new Point(x + divisor, y + divisor);
				try {
					model.pattern.execute("addShape");
				} catch (Exception e) {
					logger.error("Exception " + e.getMessage());
				}
			}
		}
		assertEquals(model.document.setIndex(2), true);
		assertEquals(model.document.getPage().setIndex(2), true);
		model.cache.pressedImage = model.document.getImage();
		position = model.cache.pressedImage.position;
		width = (int)position.dimension.width;
		height = (int)position.dimension.height;
		widthQuotient = width/divisor;
		heightQuotient = height/divisor;
		for(int i=0;i<widthQuotient;i++) {
			for(int j=0;j<heightQuotient;j++) {
				int x = (int)(position.point.x+(i*divisor));
				int y = (int)(position.point.y+(j*divisor));
				model.cache.pressedPoint = new Point(x, y);
				model.cache.releasedPoint = new Point(x + divisor, y + divisor);
				try {
					model.pattern.execute("addShape");
				} catch (Exception e) {
					logger.error("Exception " + e.getMessage());
				}
			}
		}
	}
	
	@Test
	@Order(2)
	public void saveShapes() {
		DocumentController.save(new java.io.File("./test/document-many-shape-test-a.json"), model.document);
	}
}
