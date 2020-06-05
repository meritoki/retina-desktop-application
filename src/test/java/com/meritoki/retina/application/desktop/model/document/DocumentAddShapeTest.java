package com.meritoki.retina.application.desktop.model.document;

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
import com.meritoki.app.desktop.retina.model.document.Document;
import com.meritoki.app.desktop.retina.model.document.Image;
import com.meritoki.app.desktop.retina.model.document.Page;
import com.meritoki.app.desktop.retina.model.document.Point;
import com.meritoki.app.desktop.retina.model.document.Position;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DocumentAddShapeTest {
	static Logger logger = LogManager.getLogger(DocumentResizeShapeTest.class.getName());
	static Document document = null;
	static String pageZeroUUID = null;
	static String pageOneUUID = null;
	static String pageTwoUUID = null;

	static int divisor = 100;

	@BeforeAll
	public static void initialize() {
		document = new Document();
		Page page = new Page();
		page = new Page(new Image(new File("./data/image/01.jpg")));
		pageZeroUUID = page.uuid;
		document.addPage(page);
		page = new Page();
		page.addImage(new Image(new File("./data/image/01.jpg")));
		page.addImage(new Image(new File("./data/image/02.jpg")));
		pageOneUUID = page.uuid;
		document.addPage(page);
		page = new Page();
		page.addImage(new Image(new File("./data/image/01.jpg")));
		page.addImage(new Image(new File("./data/image/02.jpg")));
		page.addImage(new Image(new File("./data/image/03.jpg")));
		pageTwoUUID = page.uuid;
		document.addPage(page);
		assertEquals(document.pageList.size(), 3);
	}
	
	@Test
	@Order(1)
	public void addShapes() {
		assertEquals(document.setIndex(0), true);
		assertEquals(document.getPage().setIndex(0), true);
		document.cache.pressedImage = document.getImage();
		Position position = document.cache.pressedImage.position;
		int width = (int)position.dimension.width;
		int height = (int)position.dimension.height;
		int widthQuotient = width/divisor;
		int heightQuotient = height/divisor;
		for(int i=0;i<widthQuotient;i++) {
			for(int j=0;j<heightQuotient;j++) {
				int x = (int)(position.point.x+(i*divisor));
				int y = (int)(position.point.y+(j*divisor));
				document.cache.pressedPoint = new Point(x, y);
				document.cache.releasedPoint = new Point(x + divisor, y + divisor);
				try {
					document.pattern.execute("addShape");
				} catch (Exception e) {
					logger.error("Exception " + e.getMessage());
				}
			}
		}
		assertEquals(document.setIndex(1), true);
		assertEquals(document.getPage().setIndex(0), true);
		document.cache.pressedImage = document.getImage();
		position = document.cache.pressedImage.position;
		width = (int)position.dimension.width;
		height = (int)position.dimension.height;
		widthQuotient = width/divisor;
		heightQuotient = height/divisor;
		for(int i=0;i<widthQuotient;i++) {
			for(int j=0;j<heightQuotient;j++) {
				int x = (int)(position.point.x+(i*divisor));
				int y = (int)(position.point.y+(j*divisor));
				document.cache.pressedPoint = new Point(x, y);
				document.cache.releasedPoint = new Point(x + divisor, y + divisor);
				try {
					document.pattern.execute("addShape");
				} catch (Exception e) {
					logger.error("Exception " + e.getMessage());
				}
			}
		}
		assertEquals(document.setIndex(1), true);
		assertEquals(document.getPage().setIndex(1), true);
		document.cache.pressedImage = document.getImage();
		position = document.cache.pressedImage.position;
		width = (int)position.dimension.width;
		height = (int)position.dimension.height;
		widthQuotient = width/divisor;
		heightQuotient = height/divisor;
		for(int i=0;i<widthQuotient;i++) {
			for(int j=0;j<heightQuotient;j++) {
				int x = (int)(position.point.x+(i*divisor));
				int y = (int)(position.point.y+(j*divisor));
				document.cache.pressedPoint = new Point(x, y);
				document.cache.releasedPoint = new Point(x + divisor, y + divisor);
				try {
					document.pattern.execute("addShape");
				} catch (Exception e) {
					logger.error("Exception " + e.getMessage());
				}
			}
		}
		assertEquals(document.setIndex(2), true);
		assertEquals(document.getPage().setIndex(0), true);
		document.cache.pressedImage = document.getImage();
		position = document.cache.pressedImage.position;
		width = (int)position.dimension.width;
		height = (int)position.dimension.height;
		widthQuotient = width/divisor;
		heightQuotient = height/divisor;
		for(int i=0;i<widthQuotient;i++) {
			for(int j=0;j<heightQuotient;j++) {
				int x = (int)(position.point.x+(i*divisor));
				int y = (int)(position.point.y+(j*divisor));
				document.cache.pressedPoint = new Point(x, y);
				document.cache.releasedPoint = new Point(x + divisor, y + divisor);
				try {
					document.pattern.execute("addShape");
				} catch (Exception e) {
					logger.error("Exception " + e.getMessage());
				}
			}
		}
		assertEquals(document.setIndex(2), true);
		assertEquals(document.getPage().setIndex(1), true);
		document.cache.pressedImage = document.getImage();
		position = document.cache.pressedImage.position;
		width = (int)position.dimension.width;
		height = (int)position.dimension.height;
		widthQuotient = width/divisor;
		heightQuotient = height/divisor;
		for(int i=0;i<widthQuotient;i++) {
			for(int j=0;j<heightQuotient;j++) {
				int x = (int)(position.point.x+(i*divisor));
				int y = (int)(position.point.y+(j*divisor));
				document.cache.pressedPoint = new Point(x, y);
				document.cache.releasedPoint = new Point(x + divisor, y + divisor);
				try {
					document.pattern.execute("addShape");
				} catch (Exception e) {
					logger.error("Exception " + e.getMessage());
				}
			}
		}
		assertEquals(document.setIndex(2), true);
		assertEquals(document.getPage().setIndex(2), true);
		document.cache.pressedImage = document.getImage();
		position = document.cache.pressedImage.position;
		width = (int)position.dimension.width;
		height = (int)position.dimension.height;
		widthQuotient = width/divisor;
		heightQuotient = height/divisor;
		for(int i=0;i<widthQuotient;i++) {
			for(int j=0;j<heightQuotient;j++) {
				int x = (int)(position.point.x+(i*divisor));
				int y = (int)(position.point.y+(j*divisor));
				document.cache.pressedPoint = new Point(x, y);
				document.cache.releasedPoint = new Point(x + divisor, y + divisor);
				try {
					document.pattern.execute("addShape");
				} catch (Exception e) {
					logger.error("Exception " + e.getMessage());
				}
			}
		}
	}
	
	@Test
	@Order(2)
	public void saveShapes() {
		DocumentController.save(new java.io.File("./test/document-many-shape-test-a.json"), document);
	}
}
