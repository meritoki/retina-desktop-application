package com.meritoki.app.desktop.retina.model.document;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.File;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.meritoki.app.desktop.retina.controller.document.DocumentController;
import com.meritoki.app.desktop.retina.controller.node.NodeController;
import com.meritoki.app.desktop.retina.model.provider.zooniverse.Zooniverse;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DocumentTest {
	@JsonIgnore
	static Logger logger = LogManager.getLogger(DocumentTest.class.getName());
	static Document document = null;
	static String pageZeroUUID = null;
	static String pageOneUUID = null;
	static String pageTwoUUID = null;

	static String imageZeroUUID = null;
	static String imageOneUUID = null;
	static double imageZeroWidth = 0;
	static double imageZeroHeight = 0;
	static double imageOneWidth = 0;
	static double imageOneHeight = 0;

	static String shapeZeroUUID = null;

	static int dimension = 128;

	@BeforeAll
	public static void initialize() {
		document = new Document();
		Page page = new Page();
		page = new Page(new Image(new File("./data/image/01.jpg")));
		pageZeroUUID = page.uuid;
		document.addPage(page);
		page = new Page();
		page.addImage(new Image(new File("./data/image/02.jpg")));
		pageOneUUID = page.uuid;
		document.addPage(page);
		page = new Page();
		page.addImage(new Image(new File("./data/image/01.jpg")));
		page.addImage(new Image(new File("./data/image/02.jpg")));
		pageTwoUUID = page.uuid;
		document.addPage(page);
	}

	@Test
	@Order(1)
	public void setIndex() {
		assertNotNull(document.getPage());
		assertEquals(document.setIndex(0), true);
		assertEquals(document.getPage().uuid, pageZeroUUID);
		assertEquals(document.setIndex(1), true);
		assertEquals(document.getPage().uuid, pageOneUUID);
		assertEquals(document.setIndex(2), true);
		assertEquals(document.getPage().uuid, pageTwoUUID);
	}

	@Test
	@Order(2)
	public void getImage() {
		assertEquals(document.setIndex(0), true);
		assertNotNull(document.getPage());
		for (Image image : document.getPage().imageList) {
			Point point = new Point(image.position.point);
			point.x += image.position.dimension.width / 2;
			point.y += image.position.dimension.height / 2;
			assertNotNull(document.getImage(point));
			assertEquals(document.getImage(point).uuid, image.uuid);
		}
		assertEquals(document.setIndex(1), true);
		assertNotNull(document.getPage());
		for (Image image : document.getPage().imageList) {
			Point point = new Point(image.position.point);
			point.x += image.position.dimension.width / 2;
			point.y += image.position.dimension.height / 2;
			assertNotNull(document.getImage(point));
			assertEquals(document.getImage(point).uuid, image.uuid);
		}
		assertEquals(document.setIndex(2), true);
		assertNotNull(document.getPage());
		for (Image image : document.getPage().imageList) {
			Point point = new Point(image.position.point);
			point.x += image.position.dimension.width / 2;
			point.y += image.position.dimension.height / 2;
			assertNotNull(document.getImage(point));
			assertEquals(document.getImage(point).uuid, image.uuid);
		}
	}

	@Test
	@Order(3)
	public void joinPages() {
		document.cache.script = "JOIN 0:1;";
		document.cache.pageList = document.getPageList();
		try {
			document.pattern.execute("executeScript");
		} catch (Exception e) {
			logger.error("Exception " + e.getMessage());
		}
		assertEquals(document.setIndex(0), true);
		assertNotNull(document.getPage());
		assertEquals(document.getPage().getImageList().size(), 2);
		for (Image image : document.getPage().imageList) {
			Point point = new Point(image.position.point);
			point.x += image.position.dimension.width / 2;
			point.y += image.position.dimension.height / 2;
			assertNotNull(document.getImage(point));
			assertEquals(document.getImage(point).uuid, image.uuid);
		}

		document.cache.script = "JOIN 0:1;";
		document.cache.pageList = document.getPageList();
		try {
			document.pattern.execute("executeScript");
		} catch (Exception e) {
			logger.error("Exception " + e.getMessage());
		}
		assertEquals(document.setIndex(0), true);
		assertNotNull(document.getPage());
		assertEquals(document.getPage().getImageList().size(), 4);
		for (Image image : document.getPage().imageList) {
			Point point = new Point(image.position.point);
			point.x += image.position.dimension.width / 2;
			point.y += image.position.dimension.height / 2;
			assertNotNull(document.getImage(point));
			assertEquals(document.getImage(point).uuid, image.uuid);
		}
	}

	@Test
	@Order(4)
	public void splitPages() {
		document.cache.script = "SPLIT 0;";
		document.cache.pageList = document.getPageList();
		try {
			document.pattern.execute("executeScript");
		} catch (Exception e) {
			logger.error("Exception " + e.getMessage());
		}
		assertEquals(document.getPageList().size(), 4);
		assertEquals(document.setIndex(0), true);
		assertNotNull(document.getPage());
		for (Image image : document.getPage().imageList) {
			Point point = new Point(image.position.point);
			point.x += image.position.dimension.width / 2;
			point.y += image.position.dimension.height / 2;
			assertNotNull(document.getImage(point));
			assertEquals(document.getImage(point).uuid, image.uuid);
		}
		assertEquals(document.setIndex(1), true);
		assertNotNull(document.getPage());
		for (Image image : document.getPage().imageList) {
			Point point = new Point(image.position.point);
			point.x += image.position.dimension.width / 2;
			point.y += image.position.dimension.height / 2;
			assertNotNull(document.getImage(point));
			assertEquals(document.getImage(point).uuid, image.uuid);
		}
		assertEquals(document.setIndex(2), true);
		assertNotNull(document.getPage());
		for (Image image : document.getPage().imageList) {
			Point point = new Point(image.position.point);
			point.x += image.position.dimension.width / 2;
			point.y += image.position.dimension.height / 2;
			assertNotNull(document.getImage(point));
			assertEquals(document.getImage(point).uuid, image.uuid);
		}
		assertEquals(document.setIndex(3), true);
		assertNotNull(document.getPage());
		for (Image image : document.getPage().imageList) {
			Point point = new Point(image.position.point);
			point.x += image.position.dimension.width / 2;
			point.y += image.position.dimension.height / 2;
			assertNotNull(document.getImage(point));
			assertEquals(document.getImage(point).uuid, image.uuid);
		}
	}

	@Test
	@Order(5)
	public void addShapes() {
		assertEquals(document.setIndex(0), true);
		assertEquals(document.getPage().setIndex(0), true);
		document.cache.pressedImage = document.getImage();
		int x = (int) (document.cache.pressedImage.position.dimension.width / 2 - dimension / 2);
		int y = (int) (document.cache.pressedImage.position.dimension.height / 2 - dimension / 2);
		int width = dimension;
		int height = dimension;
		document.cache.pressedPoint = new Point(x, y);
		document.cache.releasedPoint = new Point(x + width, y + height);
		try {
			document.pattern.execute("addShape");
		} catch (Exception e) {
			logger.error("Exception " + e.getMessage());
		}
		assertEquals(document.setIndex(1), true);
		document.cache.pressedImage = document.getImage();
		x = (int) (document.cache.pressedImage.position.dimension.width / 2 - dimension / 2);
		y = (int) (document.cache.pressedImage.position.dimension.height / 2 - dimension / 2);
		width = dimension;
		height = dimension;
		document.cache.pressedPoint = new Point(x, y);
		document.cache.releasedPoint = new Point(x + width, y + height);
		try {
			document.pattern.execute("addShape");
		} catch (Exception e) {
			logger.error("Exception " + e.getMessage());
		}

		assertEquals(document.setIndex(2), true);
		document.cache.pressedImage = document.getImage();
		x = (int) (document.cache.pressedImage.position.dimension.width / 2 - dimension / 2);
		y = (int) (document.cache.pressedImage.position.dimension.height / 2 - dimension / 2);
		width = dimension;
		height = dimension;
		document.cache.pressedPoint = new Point(x, y);
		document.cache.releasedPoint = new Point(x + width, y + height);
		try {
			document.pattern.execute("addShape");
		} catch (Exception e) {
			logger.error("Exception " + e.getMessage());
		}

		assertEquals(document.setIndex(3), true);
		document.cache.pressedImage = document.getImage();
		x = (int) (document.cache.pressedImage.position.dimension.width / 2 - dimension / 2);
		y = (int) (document.cache.pressedImage.position.dimension.height / 2 - dimension / 2);
		width = dimension;
		height = dimension;
		document.cache.pressedPoint = new Point(x, y);
		document.cache.releasedPoint = new Point(x + width, y + height);
		try {
			document.pattern.execute("addShape");
		} catch (Exception e) {
			logger.error("Exception " + e.getMessage());
		}
	}

	@Test
	@Order(6)
	public void getShapes() {
		assertEquals(document.setIndex(0), true);
		assertEquals(document.getPage().setIndex(0), true);
		int x = (int) (document.getPage().getImage().position.dimension.width / 2);
		int y = (int) (document.getPage().getImage().position.dimension.height / 2);
		assertNotNull(document.getPage().getImage().getShape(new Point(x, y)));

		assertEquals(document.setIndex(1), true);
		assertEquals(document.getPage().setIndex(0), true);
		x = (int) (document.getPage().getImage().position.dimension.width / 2);
		y = (int) (document.getPage().getImage().position.dimension.height / 2);
		assertNotNull(document.getPage().getImage().getShape(new Point(x, y)));

		assertEquals(document.setIndex(2), true);
		assertEquals(document.getPage().setIndex(0), true);
		x = (int) (document.getPage().getImage().position.dimension.width / 2);
		y = (int) (document.getPage().getImage().position.dimension.height / 2);
		assertNotNull(document.getPage().getImage().getShape(new Point(x, y)));

		assertEquals(document.setIndex(3), true);
		assertEquals(document.getPage().setIndex(0), true);
		x = (int) (document.getPage().getImage().position.dimension.width / 2);
		y = (int) (document.getPage().getImage().position.dimension.height / 2);
		assertNotNull(document.getPage().getImage().getShape(new Point(x, y)));
	}

	@Test
	@Order(7)
	public void joinWithShapes() {
		document.cache.script = "JOIN 0:1;";
		document.cache.pageList = document.getPageList();
		try {
			document.pattern.execute("executeScript");
		} catch (Exception e) {
			logger.error("Exception " + e.getMessage());
		}
		assertEquals(document.setIndex(0), true);
		assertNotNull(document.getPage());
		assertEquals(document.getPage().getImageList().size(), 2);
		for (Image image : document.getPage().imageList) {
			Point point = new Point(image.position.point);
			point.x += image.position.dimension.width / 2;
			point.y += image.position.dimension.height / 2;
			assertNotNull(document.getImage(point));
			assertEquals(document.getImage(point).uuid, image.uuid);
		}

		document.cache.script = "JOIN 0:1;";
		document.cache.pageList = document.getPageList();
		try {
			document.pattern.execute("executeScript");
		} catch (Exception e) {
			logger.error("Exception " + e.getMessage());
		}
		assertEquals(document.setIndex(0), true);
		assertNotNull(document.getPage());
		assertEquals(document.getPage().getImageList().size(), 3);
		for (Image image : document.getPage().imageList) {
			Point point = new Point(image.position.point);
			point.x += image.position.dimension.width / 2;
			point.y += image.position.dimension.height / 2;
			assertNotNull(document.getImage(point));
			assertEquals(document.getImage(point).uuid, image.uuid);
		}

		document.cache.script = "JOIN 0:1;";
		document.cache.pageList = document.getPageList();
		try {
			document.pattern.execute("executeScript");
		} catch (Exception e) {
			logger.error("Exception " + e.getMessage());
		}
		assertEquals(document.setIndex(0), true);
		assertNotNull(document.getPage());
		assertEquals(document.getPage().getImageList().size(), 4);
		for (Image image : document.getPage().imageList) {
			Point point = new Point(image.position.point);
			point.x += image.position.dimension.width / 2;
			point.y += image.position.dimension.height / 2;
			assertNotNull(document.getImage(point));
			assertEquals(document.getImage(point).uuid, image.uuid);
		}
	}

	@Test
	@Order(7)
	public void getJoinShapes() {
		assertEquals(document.setIndex(0), true);
		assertEquals(document.getPage().setIndex(0), true);
		for (Image image : document.getPage().getImageList()) {
			document.getPage().setImage(image.uuid);
			assertNotNull(image.getShapeList());
			assertEquals(image.getShapeList().size(), 1);
			int x = (int) (image.position.point.x + image.position.dimension.width / 2);
			int y = (int) (image.position.point.y + image.position.dimension.height / 2);
			assertNotNull(image.getShape(new Point(x, y)));
		}
	}

//	@Test
//	@Order(8)
//	public void save() {
//		DocumentController.save(new java.io.File("./test/document-test-a.json"), document);
//	}
//
//	@Test
//	@Order(9)
//	public void open() {
//		document = DocumentController.open(new java.io.File("./test/document-test-a.json"));
//		assertNotNull(document);
//	}
//	
	@Test
	@Order(7)
	public void splitWithShapes() {
		document.cache.script = "SPLIT 0;";
		document.cache.pageList = document.getPageList();
		try {
			document.pattern.execute("executeScript");
		} catch (Exception e) {
			logger.error("Exception " + e.getMessage());
		}
		assertEquals(document.getPageList().size(), 4);
		assertEquals(document.setIndex(0), true);
		assertNotNull(document.getPage());
		for (Image image : document.getPage().imageList) {
			Point point = new Point(image.position.point);
			point.x += image.position.dimension.width / 2;
			point.y += image.position.dimension.height / 2;
			assertNotNull(document.getImage(point));
			assertEquals(document.getImage(point).uuid, image.uuid);
		}
		assertEquals(document.setIndex(1), true);
		assertNotNull(document.getPage());
		for (Image image : document.getPage().imageList) {
			Point point = new Point(image.position.absolutePoint);
			point.x += image.position.dimension.width / 2;
			point.y += image.position.dimension.height / 2;
			assertNotNull(document.getImage(point));
			assertEquals(document.getImage(point).uuid, image.uuid);
		}
		assertEquals(document.setIndex(2), true);
		assertNotNull(document.getPage());
		for (Image image : document.getPage().imageList) {
			Point point = new Point(image.position.absolutePoint);
			point.x += image.position.dimension.width / 2;
			point.y += image.position.dimension.height / 2;
			assertNotNull(document.getImage(point));
			assertEquals(document.getImage(point).uuid, image.uuid);
		}
		assertEquals(document.setIndex(3), true);
		assertNotNull(document.getPage());
		for (Image image : document.getPage().imageList) {
			Point point = new Point(image.position.absolutePoint);
			point.x += image.position.dimension.width / 2;
			point.y += image.position.dimension.height / 2;
			assertNotNull(document.getImage(point));
			assertEquals(document.getImage(point).uuid, image.uuid);
		}

		assertEquals(document.setIndex(0), true);
		assertEquals(document.getPage().setIndex(0), true);
		for (Image image : document.getPage().getImageList()) {
			document.getPage().setImage(image.uuid);
			int x = (int) (image.position.dimension.width / 2);
			int y = (int) (image.position.dimension.height / 2);
			assertNotNull(image.getShape(new Point(x, y)));
		}

		assertEquals(document.setIndex(1), true);
		assertEquals(document.getPage().setIndex(0), true);
		for (Image image : document.getPage().getImageList()) {
			document.getPage().setImage(image.uuid);
			int x = (int) (image.position.dimension.width / 2);
			int y = (int) (image.position.dimension.height / 2);
			assertNotNull(image.getShape(new Point(x, y)));
		}

		assertEquals(document.setIndex(2), true);
		assertEquals(document.getPage().setIndex(0), true);
		for (Image image : document.getPage().getImageList()) {
			document.getPage().setImage(image.uuid);
			int x = (int) (image.position.dimension.width / 2);
			int y = (int) (image.position.absoluteDimension.height / 2);
			assertNotNull(image.getShape(new Point(x, y)));
		}

		assertEquals(document.setIndex(3), true);
		assertEquals(document.getPage().setIndex(0), true);
		for (Image image : document.getPage().getImageList()) {
			document.getPage().setImage(image.uuid);
			int x = (int) (image.position.dimension.width / 2);
			int y = (int) (image.position.dimension.height / 2);
			assertNotNull(image.getShape(new Point(x, y)));
		}
	}

	@Test
	@Order(8)
	public void joinWithShapesAgain() {
		document.cache.script = "JOIN 0:1;";
		document.cache.pageList = document.getPageList();
		try {
			document.pattern.execute("executeScript");
		} catch (Exception e) {
			logger.error("Exception " + e.getMessage());
		}
		assertEquals(document.setIndex(0), true);
		assertNotNull(document.getPage());
		assertEquals(document.getPage().getImageList().size(), 2);
		for (Image image : document.getPage().imageList) {
			Point point = new Point(image.position.point);
			point.x += image.position.dimension.width / 2;
			point.y += image.position.dimension.height / 2;
			assertNotNull(document.getImage(point));
			assertEquals(document.getImage(point).uuid, image.uuid);
		}

		document.cache.script = "JOIN 0:1;";
		document.cache.pageList = document.getPageList();
		try {
			document.pattern.execute("executeScript");
		} catch (Exception e) {
			logger.error("Exception " + e.getMessage());
		}
		assertEquals(document.setIndex(0), true);
		assertNotNull(document.getPage());
		assertEquals(document.getPage().getImageList().size(), 3);
		for (Image image : document.getPage().imageList) {
			Point point = new Point(image.position.point);
			point.x += image.position.dimension.width / 2;
			point.y += image.position.dimension.height / 2;
			assertNotNull(document.getImage(point));
			assertEquals(document.getImage(point).uuid, image.uuid);
		}

		document.cache.script = "JOIN 0:1;";
		document.cache.pageList = document.getPageList();
		try {
			document.pattern.execute("executeScript");
		} catch (Exception e) {
			logger.error("Exception " + e.getMessage());
		}
		assertEquals(document.setIndex(0), true);
		assertNotNull(document.getPage());
		assertEquals(document.getPage().getImageList().size(), 4);
		for (Image image : document.getPage().imageList) {
			Point point = new Point(image.position.point);
			point.x += image.position.dimension.width / 2;
			point.y += image.position.dimension.height / 2;
			assertNotNull(document.getImage(point));
			assertEquals(document.getImage(point).uuid, image.uuid);
		}
	}

	@Test
	@Order(9)
	public void getShapeBufferedImageAfterJoin() {
		assertEquals(document.setIndex(0), true);
		List<Shape> shapeList = document.getPage().getShapeList();
		for (Shape s : shapeList) {
			assertNotNull(s.bufferedImage);
		}
		Zooniverse zooniverse = new Zooniverse();
		NodeController.deleteDirectory(new File("./test/manifest-test-A"));
		zooniverse.generateManifest("./test/manifest-test-A", shapeList);
	}

	@Test
	@Order(10)
	public void splitWithShapesAgain() {
		document.cache.script = "SPLIT 0;";
		document.cache.pageList = document.getPageList();
		try {
			document.pattern.execute("executeScript");
		} catch (Exception e) {
			logger.error("Exception " + e.getMessage());
		}
		assertEquals(document.getPageList().size(), 4);
		assertEquals(document.setIndex(0), true);
		assertNotNull(document.getPage());
		for (Image image : document.getPage().imageList) {
			Point point = new Point(image.position.point);
			point.x += image.position.dimension.width / 2;
			point.y += image.position.dimension.height / 2;
			assertNotNull(document.getImage(point));
			assertEquals(document.getImage(point).uuid, image.uuid);
		}
		assertEquals(document.setIndex(1), true);
		assertNotNull(document.getPage());
		for (Image image : document.getPage().imageList) {
			Point point = new Point(image.position.point);
			point.x += image.position.dimension.width / 2;
			point.y += image.position.dimension.height / 2;
			assertNotNull(document.getImage(point));
			assertEquals(document.getImage(point).uuid, image.uuid);
		}
		assertEquals(document.setIndex(2), true);
		assertNotNull(document.getPage());
		for (Image image : document.getPage().imageList) {
			Point point = new Point(image.position.point);
			point.x += image.position.dimension.width / 2;
			point.y += image.position.dimension.height / 2;
			assertNotNull(document.getImage(point));
			assertEquals(document.getImage(point).uuid, image.uuid);
		}
		assertEquals(document.setIndex(3), true);
		assertNotNull(document.getPage());
		for (Image image : document.getPage().imageList) {
			Point point = new Point(image.position.point);
			point.x += image.position.dimension.width / 2;
			point.y += image.position.dimension.height / 2;
			assertNotNull(document.getImage(point));
			assertEquals(document.getImage(point).uuid, image.uuid);
		}

		assertEquals(document.setIndex(0), true);
		assertEquals(document.getPage().setIndex(0), true);
		for (Image image : document.getPage().getImageList()) {
			document.getPage().setImage(image.uuid);
			int x = (int) (image.position.dimension.width / 2);
			int y = (int) (image.position.dimension.height / 2);
			assertNotNull(image.getShape(new Point(x, y)));
		}

		assertEquals(document.setIndex(1), true);
		assertEquals(document.getPage().setIndex(0), true);
		for (Image image : document.getPage().getImageList()) {
			document.getPage().setImage(image.uuid);
			int x = (int) (image.position.dimension.width / 2);
			int y = (int) (image.position.dimension.height / 2);
			assertNotNull(image.getShape(new Point(x, y)));
		}

		assertEquals(document.setIndex(2), true);
		assertEquals(document.getPage().setIndex(0), true);
		for (Image image : document.getPage().getImageList()) {
			document.getPage().setImage(image.uuid);
			int x = (int) (image.position.dimension.width / 2);
			int y = (int) (image.position.dimension.height / 2);
			assertNotNull(image.getShape(new Point(x, y)));
		}

		assertEquals(document.setIndex(3), true);
		assertEquals(document.getPage().setIndex(0), true);
		for (Image image : document.getPage().getImageList()) {
			document.getPage().setImage(image.uuid);
			int x = (int) (image.position.dimension.width / 2);
			int y = (int) (image.position.dimension.height / 2);
			assertNotNull(image.getShape(new Point(x, y)));
		}
	}

	@Test
	@Order(11)
	public void save() {
		DocumentController.save(new java.io.File("./test/document-test-a.json"), document);
	}

	@Test
	@Order(12)
	public void open() {
		document = DocumentController.open(new java.io.File("./test/document-test-a.json"));
		assertNotNull(document);
	}
	
	@Test
	@Order(13)
	public void test() {
		assertEquals(document.setIndex(0), true);
		assertEquals(document.getPage().setIndex(0),true);
		for(Image image: document.getPage().getImageList()) {
			document.getPage().setImage(image.uuid);
			int x = (int)(image.position.dimension.width/2);
			int y = (int)(image.position.dimension.height/2);
			assertNotNull(image.file);
			assertNotNull(image.getShape(new Point(x,y)));
		}
		
		assertEquals(document.setIndex(1), true);
		assertEquals(document.getPage().setIndex(0),true);
		for(Image image: document.getPage().getImageList()) {
			document.getPage().setImage(image.uuid);
			int x = (int)(image.position.dimension.width/2);
			int y = (int)(image.position.dimension.height/2);
			assertNotNull(image.file);
			assertNotNull(image.getShape(new Point(x,y)));
		}
		
		assertEquals(document.setIndex(2), true);
		assertEquals(document.getPage().setIndex(0),true);
		for(Image image: document.getPage().getImageList()) {
			document.getPage().setImage(image.uuid);
			int x = (int)(image.position.dimension.width/2);
			int y = (int)(image.position.dimension.height/2);
			assertNotNull(image.file);
			assertNotNull(image.getShape(new Point(x,y)));
		}
		
		assertEquals(document.setIndex(3), true);
		assertEquals(document.getPage().setIndex(0),true);
		for(Image image: document.getPage().getImageList()) {
			document.getPage().setImage(image.uuid);
			int x = (int)(image.position.dimension.width/2);
			int y = (int)(image.position.dimension.height/2);
			assertNotNull(image.file);
			assertNotNull(image.getShape(new Point(x,y)));
		}
	}
	
	@Test
	@Order(13) 
	public void setShapeData() {
		assertEquals(document.setIndex(0), true);
		assertEquals(document.getPage().setIndex(0),true);
		for(Image image: document.getPage().getImageList()) {
			document.getPage().setImage(image.uuid);
			int x = (int)(image.position.dimension.width/2);
			int y = (int)(image.position.dimension.height/2);
			Shape shape = image.getShape(new Point(x,y));
			assertNotNull(shape);
			Data data = new Data();
			data.text = new Text("Hello World 0");
			shape.setData(data);
		}
		
		assertEquals(document.setIndex(1), true);
		assertEquals(document.getPage().setIndex(0),true);
		for(Image image: document.getPage().getImageList()) {
			document.getPage().setImage(image.uuid);
			int x = (int)(image.position.dimension.width/2);
			int y = (int)(image.position.dimension.height/2);
			Shape shape = image.getShape(new Point(x,y));
			assertNotNull(shape);
			Data data = new Data();
			data.text = new Text("Hello World 1");
			shape.setData(data);
		}
		
		assertEquals(document.setIndex(2), true);
		assertEquals(document.getPage().setIndex(0),true);
		for(Image image: document.getPage().getImageList()) {
			document.getPage().setImage(image.uuid);
			int x = (int)(image.position.dimension.width/2);
			int y = (int)(image.position.dimension.height/2);
			Shape shape = image.getShape(new Point(x,y));
			assertNotNull(shape);
			Data data = new Data();
			data.text = new Text("Hello World 2");
			shape.setData(data);
		}
		
		assertEquals(document.setIndex(3), true);
		assertEquals(document.getPage().setIndex(0),true);
		for(Image image: document.getPage().getImageList()) {
			document.getPage().setImage(image.uuid);
			int x = (int)(image.position.dimension.width/2);
			int y = (int)(image.position.dimension.height/2);
			Shape shape = image.getShape(new Point(x,y));
			assertNotNull(shape);
			Data data = new Data();
			data.text = new Text("Hello World 3");
			shape.setData(data);
		}
	}
	
	@Test
	@Order(14)
	public void saveShapeData() {
		DocumentController.save(new java.io.File("./test/document-test-b.json"), document);
	}
	
	@Test
	@Order(15)
	public void openShapeData() {
		document = DocumentController.open(new java.io.File("./test/document-test-b.json"));
		assertEquals(document.setIndex(0), true);
		assertEquals(document.getPage().setIndex(0),true);
		for(Image image: document.getPage().getImageList()) {
			document.getPage().setImage(image.uuid);
			int x = (int)(image.position.dimension.width/2);
			int y = (int)(image.position.dimension.height/2);
			Shape shape = image.getShape(new Point(x,y));
			assertNotNull(shape);
			assertEquals(shape.data.text.value, "Hello World 0");
		}
		
		assertEquals(document.setIndex(1), true);
		assertEquals(document.getPage().setIndex(0),true);
		for(Image image: document.getPage().getImageList()) {
			document.getPage().setImage(image.uuid);
			int x = (int)(image.position.dimension.width/2);
			int y = (int)(image.position.dimension.height/2);
			Shape shape = image.getShape(new Point(x,y));
			assertNotNull(shape);
			assertEquals(shape.data.text.value, "Hello World 1");
		}
		
		assertEquals(document.setIndex(2), true);
		assertEquals(document.getPage().setIndex(0),true);
		for(Image image: document.getPage().getImageList()) {
			document.getPage().setImage(image.uuid);
			int x = (int)(image.position.dimension.width/2);
			int y = (int)(image.position.dimension.height/2);
			Shape shape = image.getShape(new Point(x,y));
			assertNotNull(shape);
			assertEquals(shape.data.text.value, "Hello World 2");
		}
		
		assertEquals(document.setIndex(3), true);
		assertEquals(document.getPage().setIndex(0),true);
		for(Image image: document.getPage().getImageList()) {
			document.getPage().setImage(image.uuid);
			int x = (int)(image.position.dimension.width/2);
			int y = (int)(image.position.dimension.height/2);
			Shape shape = image.getShape(new Point(x,y));
			assertNotNull(shape);
			assertEquals(shape.data.text.value, "Hello World 3");
		}
	}	
	
	@Test
	@Order(16)
	public void getShapeBufferedImage() {
		assertEquals(document.setIndex(0), true);
		List<Shape> shapeList = document.getPage().getShapeList();
		for(Shape s: shapeList) {
			assertNotNull(s.bufferedImage);
		}
		Zooniverse zooniverse = new Zooniverse();
		NodeController.deleteDirectory(new File("./test/manifest-test-B"));
		zooniverse.generateManifest("./test/manifest-test-B", shapeList);
	}
}
