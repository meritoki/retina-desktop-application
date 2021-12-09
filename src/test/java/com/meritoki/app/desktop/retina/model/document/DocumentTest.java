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
import com.meritoki.app.desktop.retina.model.Model;
import com.meritoki.app.desktop.retina.model.provider.zooniverse.Zooniverse;
import com.meritoki.app.desktop.retina.controller.node.NodeController;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DocumentTest {
	@JsonIgnore
	static Logger logger = LogManager.getLogger(DocumentTest.class.getName());
	static Model model = new Model();
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
		model.document = new Document();
		Page page = new Page();
		page = new Page(new Image(new File("./test/img/test.png")));
		pageZeroUUID = page.uuid;
		model.document.addPage(page);
		page = new Page();
		page.addImage(new Image(new File("./test/img/test.png")));
		pageOneUUID = page.uuid;
		model.document.addPage(page);
		page = new Page();
		page.addImage(new Image(new File("./test/img/test.png")));
		page.addImage(new Image(new File("./test/img/test.png")));
		pageTwoUUID = page.uuid;
		model.document.addPage(page);
	}

	@Test
	@Order(1)
	public void setIndex() {
		assertNotNull(model.document.getPage());
		assertEquals(model.document.setIndex(0), true);
		assertEquals(model.document.getPage().uuid, pageZeroUUID);
		assertEquals(model.document.setIndex(1), true);
		assertEquals(model.document.getPage().uuid, pageOneUUID);
		assertEquals(model.document.setIndex(2), true);
		assertEquals(model.document.getPage().uuid, pageTwoUUID);
	}

	@Test
	@Order(2)
	public void getImage() {
		assertEquals(model.document.setIndex(0), true);
		model.document.getPage().getBufferedImage(model);
		assertNotNull(model.document.getPage());
		for (Image image : model.document.getPage().imageList) {
			Point point = new Point(image.position.point);
			point.x += image.position.dimension.width / 2;
			point.y += image.position.dimension.height / 2;
			assertNotNull(model.document.getImage(point));
			assertEquals(model.document.getImage(point).uuid, image.uuid);
		}
		assertEquals(model.document.setIndex(1), true);
		model.document.getPage().getBufferedImage(model);
		assertNotNull(model.document.getPage());
		for (Image image : model.document.getPage().imageList) {
			Point point = new Point(image.position.point);
			point.x += image.position.dimension.width / 2;
			point.y += image.position.dimension.height / 2;
			assertNotNull(model.document.getImage(point));
			assertEquals(model.document.getImage(point).uuid, image.uuid);
		}
		assertEquals(model.document.setIndex(2), true);
		model.document.getPage().getBufferedImage(model);
		assertNotNull(model.document.getPage());
		for (Image image : model.document.getPage().imageList) {
			Point point = new Point(image.position.point);
			point.x += image.position.dimension.width / 2;
			point.y += image.position.dimension.height / 2;
			assertNotNull(model.document.getImage(point));
			assertEquals(model.document.getImage(point).uuid, image.uuid);
		}
	}

	@Test
	@Order(3)
	public void joinPages() {
		model.cache.script = "JOIN 0:1;";
		model.cache.pageList = model.document.getPageList();
		try {
			model.pattern.execute("executeScript");
		} catch (Exception e) {
			logger.error("Exception " + e.getMessage());
		}
		assertEquals(model.document.setIndex(0), true);
		assertNotNull(model.document.getPage());
		assertEquals(model.document.getPage().getImageList().size(), 2);
		for (Image image : model.document.getPage().imageList) {
			Point point = new Point(image.position.point);
			point.x += image.position.dimension.width / 2;
			point.y += image.position.dimension.height / 2;
			assertNotNull(model.document.getImage(point));
			assertEquals(model.document.getImage(point).uuid, image.uuid);
		}

		model.cache.script = "JOIN 0:1;";
		model.cache.pageList = model.document.getPageList();
		try {
			model.pattern.execute("executeScript");
		} catch (Exception e) {
			logger.error("Exception " + e.getMessage());
		}
		assertEquals(model.document.setIndex(0), true);
		assertNotNull(model.document.getPage());
		assertEquals(model.document.getPage().getImageList().size(), 4);
		for (Image image : model.document.getPage().imageList) {
			Point point = new Point(image.position.point);
			point.x += image.position.dimension.width / 2;
			point.y += image.position.dimension.height / 2;
			assertNotNull(model.document.getImage(point));
			assertEquals(model.document.getImage(point).uuid, image.uuid);
		}
	}

	@Test
	@Order(4)
	public void splitPages() {
		model.cache.script = "SPLIT 0;";
		model.cache.pageList = model.document.getPageList();
		try {
			model.pattern.execute("executeScript");
		} catch (Exception e) {
			logger.error("Exception " + e.getMessage());
		}
		assertEquals(model.document.getPageList().size(), 4);
		assertEquals(model.document.setIndex(0), true);
		assertNotNull(model.document.getPage());
		for (Image image : model.document.getPage().imageList) {
			Point point = new Point(image.position.point);
			point.x += image.position.dimension.width / 2;
			point.y += image.position.dimension.height / 2;
			assertNotNull(model.document.getImage(point));
			assertEquals(model.document.getImage(point).uuid, image.uuid);
		}
		assertEquals(model.document.setIndex(1), true);
		assertNotNull(model.document.getPage());
		for (Image image : model.document.getPage().imageList) {
			Point point = new Point(image.position.point);
			point.x += image.position.dimension.width / 2;
			point.y += image.position.dimension.height / 2;
			assertNotNull(model.document.getImage(point));
			assertEquals(model.document.getImage(point).uuid, image.uuid);
		}
		assertEquals(model.document.setIndex(2), true);
		assertNotNull(model.document.getPage());
		for (Image image : model.document.getPage().imageList) {
			Point point = new Point(image.position.point);
			point.x += image.position.dimension.width / 2;
			point.y += image.position.dimension.height / 2;
			assertNotNull(model.document.getImage(point));
			assertEquals(model.document.getImage(point).uuid, image.uuid);
		}
		assertEquals(model.document.setIndex(3), true);
		assertNotNull(model.document.getPage());
		for (Image image : model.document.getPage().imageList) {
			Point point = new Point(image.position.point);
			point.x += image.position.dimension.width / 2;
			point.y += image.position.dimension.height / 2;
			assertNotNull(model.document.getImage(point));
			assertEquals(model.document.getImage(point).uuid, image.uuid);
		}
	}

	@Test
	@Order(5)
	public void addShapes() {
		assertEquals(model.document.setIndex(0), true);
		assertEquals(model.document.getPage().setIndex(0), true);
		model.system.pressedImage = model.document.getImage();
		int x = (int) (model.system.pressedImage.position.dimension.width / 2 - dimension / 2);
		int y = (int) (model.system.pressedImage.position.dimension.height / 2 - dimension / 2);
		int width = dimension;
		int height = dimension;
		try {
			model.cache.pressedPoint = new Point(x, y);
			model.cache.releasedPoint = new Point(x + width, y + height);
			model.cache.pressedPageUUID = model.document.getPage().uuid;
			model.cache.pressedImageUUID = model.system.pressedImage.uuid;
			model.pattern.execute("addShape");
		} catch (Exception e) {
			logger.error("Exception " + e.getMessage());
		}
		assertEquals(model.document.setIndex(1), true);
		model.system.pressedImage = model.document.getImage();
		x = (int) (model.system.pressedImage.position.dimension.width / 2 - dimension / 2);
		y = (int) (model.system.pressedImage.position.dimension.height / 2 - dimension / 2);
		width = dimension;
		height = dimension;
		try {
			model.cache.pressedPoint = new Point(x, y);
			model.cache.releasedPoint = new Point(x + width, y + height);
			model.cache.pressedPageUUID = model.document.getPage().uuid;
			model.cache.pressedImageUUID = model.system.pressedImage.uuid;
			model.pattern.execute("addShape");
		} catch (Exception e) {
			logger.error("Exception " + e.getMessage());
		}

		assertEquals(model.document.setIndex(2), true);
		model.system.pressedImage = model.document.getImage();
		x = (int) (model.system.pressedImage.position.dimension.width / 2 - dimension / 2);
		y = (int) (model.system.pressedImage.position.dimension.height / 2 - dimension / 2);
		width = dimension;
		height = dimension;
		try {
			model.cache.pressedPoint = new Point(x, y);
			model.cache.releasedPoint = new Point(x + width, y + height);
			model.cache.pressedPageUUID = model.document.getPage().uuid;
			model.cache.pressedImageUUID = model.system.pressedImage.uuid;
			model.pattern.execute("addShape");
		} catch (Exception e) {
			logger.error("Exception " + e.getMessage());
		}

		assertEquals(model.document.setIndex(3), true);
		model.system.pressedImage = model.document.getImage();
		x = (int) (model.system.pressedImage.position.dimension.width / 2 - dimension / 2);
		y = (int) (model.system.pressedImage.position.dimension.height / 2 - dimension / 2);
		width = dimension;
		height = dimension;
		try {
			model.cache.pressedPoint = new Point(x, y);
			model.cache.releasedPoint = new Point(x + width, y + height);
			model.cache.pressedPageUUID = model.document.getPage().uuid;
			model.cache.pressedImageUUID = model.system.pressedImage.uuid;
			model.pattern.execute("addShape");
		} catch (Exception e) {
			logger.error("Exception " + e.getMessage());
		}
	}

	@Test
	@Order(6)
	public void getShapes() {
		assertEquals(model.document.setIndex(0), true);
		assertEquals(model.document.getPage().setIndex(0), true);
		int x = (int) (model.document.getPage().getImage().position.dimension.width / 2);
		int y = (int) (model.document.getPage().getImage().position.dimension.height / 2);
		assertNotNull(model.document.getPage().getImage().getShape(new Point(x, y)));

		assertEquals(model.document.setIndex(1), true);
		assertEquals(model.document.getPage().setIndex(0), true);
		x = (int) (model.document.getPage().getImage().position.dimension.width / 2);
		y = (int) (model.document.getPage().getImage().position.dimension.height / 2);
		assertNotNull(model.document.getPage().getImage().getShape(new Point(x, y)));

		assertEquals(model.document.setIndex(2), true);
		assertEquals(model.document.getPage().setIndex(0), true);
		x = (int) (model.document.getPage().getImage().position.dimension.width / 2);
		y = (int) (model.document.getPage().getImage().position.dimension.height / 2);
		assertNotNull(model.document.getPage().getImage().getShape(new Point(x, y)));

		assertEquals(model.document.setIndex(3), true);
		assertEquals(model.document.getPage().setIndex(0), true);
		x = (int) (model.document.getPage().getImage().position.dimension.width / 2);
		y = (int) (model.document.getPage().getImage().position.dimension.height / 2);
		assertNotNull(model.document.getPage().getImage().getShape(new Point(x, y)));
	}

	@Test
	@Order(7)
	public void joinWithShapes() {
		model.cache.script = "JOIN 0:1;";
		model.cache.pageList = model.document.getPageList();
		try {
			model.pattern.execute("executeScript");
		} catch (Exception e) {
			logger.error("Exception " + e.getMessage());
		}
		assertEquals(model.document.setIndex(0), true);
		assertNotNull(model.document.getPage());
		assertEquals(model.document.getPage().getImageList().size(), 2);
		for (Image image : model.document.getPage().imageList) {
			Point point = new Point(image.position.point);
			point.x += image.position.dimension.width / 2;
			point.y += image.position.dimension.height / 2;
			assertNotNull(model.document.getImage(point));
			assertEquals(model.document.getImage(point).uuid, image.uuid);
		}

		model.cache.script = "JOIN 0:1;";
		model.cache.pageList = model.document.getPageList();
		try {
			model.pattern.execute("executeScript");
		} catch (Exception e) {
			logger.error("Exception " + e.getMessage());
		}
		assertEquals(model.document.setIndex(0), true);
		assertNotNull(model.document.getPage());
		assertEquals(model.document.getPage().getImageList().size(), 3);
		for (Image image : model.document.getPage().imageList) {
			Point point = new Point(image.position.point);
			point.x += image.position.dimension.width / 2;
			point.y += image.position.dimension.height / 2;
			assertNotNull(model.document.getImage(point));
			assertEquals(model.document.getImage(point).uuid, image.uuid);
		}

		model.cache.script = "JOIN 0:1;";
		model.cache.pageList = model.document.getPageList();
		try {
			model.pattern.execute("executeScript");
		} catch (Exception e) {
			logger.error("Exception " + e.getMessage());
		}
		assertEquals(model.document.setIndex(0), true);
		assertNotNull(model.document.getPage());
		assertEquals(model.document.getPage().getImageList().size(), 4);
		for (Image image : model.document.getPage().imageList) {
			Point point = new Point(image.position.point);
			point.x += image.position.dimension.width / 2;
			point.y += image.position.dimension.height / 2;
			assertNotNull(model.document.getImage(point));
			assertEquals(model.document.getImage(point).uuid, image.uuid);
		}
	}

	@Test
	@Order(7)
	public void getJoinShapes() {
		assertEquals(model.document.setIndex(0), true);
		assertEquals(model.document.getPage().setIndex(0), true);
		for (Image image : model.document.getPage().getImageList()) {
			model.document.getPage().setImage(image.uuid);
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
		model.cache.script = "SPLIT 0;";
		model.cache.pageList = model.document.getPageList();
		try {
			model.pattern.execute("executeScript");
		} catch (Exception e) {
			logger.error("Exception " + e.getMessage());
		}
		assertEquals(model.document.getPageList().size(), 4);
		assertEquals(model.document.setIndex(0), true);
		assertNotNull(model.document.getPage());
		for (Image image : model.document.getPage().imageList) {
			Point point = new Point(image.position.point);
			point.x += image.position.dimension.width / 2;
			point.y += image.position.dimension.height / 2;
			assertNotNull(model.document.getImage(point));
			assertEquals(model.document.getImage(point).uuid, image.uuid);
		}
		assertEquals(model.document.setIndex(1), true);
		assertNotNull(model.document.getPage());
		for (Image image : model.document.getPage().imageList) {
			Point point = new Point(image.position.absolutePoint);
			point.x += image.position.dimension.width / 2;
			point.y += image.position.dimension.height / 2;
			assertNotNull(model.document.getImage(point));
			assertEquals(model.document.getImage(point).uuid, image.uuid);
		}
		assertEquals(model.document.setIndex(2), true);
		assertNotNull(model.document.getPage());
		for (Image image : model.document.getPage().imageList) {
			Point point = new Point(image.position.absolutePoint);
			point.x += image.position.dimension.width / 2;
			point.y += image.position.dimension.height / 2;
			assertNotNull(model.document.getImage(point));
			assertEquals(model.document.getImage(point).uuid, image.uuid);
		}
		assertEquals(model.document.setIndex(3), true);
		assertNotNull(model.document.getPage());
		for (Image image : model.document.getPage().imageList) {
			Point point = new Point(image.position.absolutePoint);
			point.x += image.position.dimension.width / 2;
			point.y += image.position.dimension.height / 2;
			assertNotNull(model.document.getImage(point));
			assertEquals(model.document.getImage(point).uuid, image.uuid);
		}

		assertEquals(model.document.setIndex(0), true);
		assertEquals(model.document.getPage().setIndex(0), true);
		for (Image image : model.document.getPage().getImageList()) {
			model.document.getPage().setImage(image.uuid);
			int x = (int) (image.position.dimension.width / 2);
			int y = (int) (image.position.dimension.height / 2);
			assertNotNull(image.getShape(new Point(x, y)));
		}

		assertEquals(model.document.setIndex(1), true);
		assertEquals(model.document.getPage().setIndex(0), true);
		for (Image image : model.document.getPage().getImageList()) {
			model.document.getPage().setImage(image.uuid);
			int x = (int) (image.position.dimension.width / 2);
			int y = (int) (image.position.dimension.height / 2);
			assertNotNull(image.getShape(new Point(x, y)));
		}

		assertEquals(model.document.setIndex(2), true);
		assertEquals(model.document.getPage().setIndex(0), true);
		for (Image image : model.document.getPage().getImageList()) {
			model.document.getPage().setImage(image.uuid);
			int x = (int) (image.position.dimension.width / 2);
			int y = (int) (image.position.absoluteDimension.height / 2);
			assertNotNull(image.getShape(new Point(x, y)));
		}

		assertEquals(model.document.setIndex(3), true);
		assertEquals(model.document.getPage().setIndex(0), true);
		for (Image image : model.document.getPage().getImageList()) {
			model.document.getPage().setImage(image.uuid);
			int x = (int) (image.position.dimension.width / 2);
			int y = (int) (image.position.dimension.height / 2);
			assertNotNull(image.getShape(new Point(x, y)));
		}
	}

	@Test
	@Order(8)
	public void joinWithShapesAgain() {
		model.cache.script = "JOIN 0:1;";
		model.cache.pageList = model.document.getPageList();
		try {
			model.pattern.execute("executeScript");
		} catch (Exception e) {
			logger.error("Exception " + e.getMessage());
		}
		assertEquals(model.document.setIndex(0), true);
		assertNotNull(model.document.getPage());
		assertEquals(model.document.getPage().getImageList().size(), 2);
		for (Image image : model.document.getPage().imageList) {
			Point point = new Point(image.position.point);
			point.x += image.position.dimension.width / 2;
			point.y += image.position.dimension.height / 2;
			assertNotNull(model.document.getImage(point));
			assertEquals(model.document.getImage(point).uuid, image.uuid);
		}

		model.cache.script = "JOIN 0:1;";
		model.cache.pageList = model.document.getPageList();
		try {
			model.pattern.execute("executeScript");
		} catch (Exception e) {
			logger.error("Exception " + e.getMessage());
		}
		assertEquals(model.document.setIndex(0), true);
		assertNotNull(model.document.getPage());
		assertEquals(model.document.getPage().getImageList().size(), 3);
		for (Image image : model.document.getPage().imageList) {
			Point point = new Point(image.position.point);
			point.x += image.position.dimension.width / 2;
			point.y += image.position.dimension.height / 2;
			assertNotNull(model.document.getImage(point));
			assertEquals(model.document.getImage(point).uuid, image.uuid);
		}

		model.cache.script = "JOIN 0:1;";
		model.cache.pageList = model.document.getPageList();
		try {
			model.pattern.execute("executeScript");
		} catch (Exception e) {
			logger.error("Exception " + e.getMessage());
		}
		assertEquals(model.document.setIndex(0), true);
		assertNotNull(model.document.getPage());
		assertEquals(model.document.getPage().getImageList().size(), 4);
		for (Image image : model.document.getPage().imageList) {
			Point point = new Point(image.position.point);
			point.x += image.position.dimension.width / 2;
			point.y += image.position.dimension.height / 2;
			assertNotNull(model.document.getImage(point));
			assertEquals(model.document.getImage(point).uuid, image.uuid);
		}
	}

	@Test
	@Order(9)
	public void getShapeBufferedImageAfterJoin() {
		assertEquals(model.document.setIndex(0), true);
		model.document.getPage().getBufferedImage(model);
		List<Shape> shapeList = model.document.getPage().getShapeList();
		for (Shape s : shapeList) {
			s.bufferedImage = model.document.getShapeBufferedImage(model.document.getPage().getScaledBufferedImage(model), s);
			assertNotNull(s.bufferedImage);
		}
//		Zooniverse zooniverse = new Zooniverse();
//		NodeController.deleteDirectory(new File("./test/manifest-test-A"));
//		try {
//			zooniverse.generateManifest("./test/manifest-test-A", shapeList);
//		} catch(Exception e) {
//			logger.error("Exception "+e.getMessage());
//		}
	}

	@Test
	@Order(10)
	public void splitWithShapesAgain() {
		model.cache.script = "SPLIT 0;";
		model.cache.pageList = model.document.getPageList();
		try {
			model.pattern.execute("executeScript");
		} catch (Exception e) {
			logger.error("Exception " + e.getMessage());
		}
		assertEquals(model.document.getPageList().size(), 4);
		assertEquals(model.document.setIndex(0), true);
		assertNotNull(model.document.getPage());
		for (Image image : model.document.getPage().imageList) {
			Point point = new Point(image.position.point);
			point.x += image.position.dimension.width / 2;
			point.y += image.position.dimension.height / 2;
			assertNotNull(model.document.getImage(point));
			assertEquals(model.document.getImage(point).uuid, image.uuid);
		}
		assertEquals(model.document.setIndex(1), true);
		assertNotNull(model.document.getPage());
		for (Image image : model.document.getPage().imageList) {
			Point point = new Point(image.position.point);
			point.x += image.position.dimension.width / 2;
			point.y += image.position.dimension.height / 2;
			assertNotNull(model.document.getImage(point));
			assertEquals(model.document.getImage(point).uuid, image.uuid);
		}
		assertEquals(model.document.setIndex(2), true);
		assertNotNull(model.document.getPage());
		for (Image image : model.document.getPage().imageList) {
			Point point = new Point(image.position.point);
			point.x += image.position.dimension.width / 2;
			point.y += image.position.dimension.height / 2;
			assertNotNull(model.document.getImage(point));
			assertEquals(model.document.getImage(point).uuid, image.uuid);
		}
		assertEquals(model.document.setIndex(3), true);
		assertNotNull(model.document.getPage());
		for (Image image : model.document.getPage().imageList) {
			Point point = new Point(image.position.point);
			point.x += image.position.dimension.width / 2;
			point.y += image.position.dimension.height / 2;
			assertNotNull(model.document.getImage(point));
			assertEquals(model.document.getImage(point).uuid, image.uuid);
		}

		assertEquals(model.document.setIndex(0), true);
		assertEquals(model.document.getPage().setIndex(0), true);
		for (Image image : model.document.getPage().getImageList()) {
			model.document.getPage().setImage(image.uuid);
			int x = (int) (image.position.dimension.width / 2);
			int y = (int) (image.position.dimension.height / 2);
			assertNotNull(image.getShape(new Point(x, y)));
		}

		assertEquals(model.document.setIndex(1), true);
		assertEquals(model.document.getPage().setIndex(0), true);
		for (Image image : model.document.getPage().getImageList()) {
			model.document.getPage().setImage(image.uuid);
			int x = (int) (image.position.dimension.width / 2);
			int y = (int) (image.position.dimension.height / 2);
			assertNotNull(image.getShape(new Point(x, y)));
		}

		assertEquals(model.document.setIndex(2), true);
		assertEquals(model.document.getPage().setIndex(0), true);
		for (Image image : model.document.getPage().getImageList()) {
			model.document.getPage().setImage(image.uuid);
			int x = (int) (image.position.dimension.width / 2);
			int y = (int) (image.position.dimension.height / 2);
			assertNotNull(image.getShape(new Point(x, y)));
		}

		assertEquals(model.document.setIndex(3), true);
		assertEquals(model.document.getPage().setIndex(0), true);
		for (Image image : model.document.getPage().getImageList()) {
			model.document.getPage().setImage(image.uuid);
			int x = (int) (image.position.dimension.width / 2);
			int y = (int) (image.position.dimension.height / 2);
			assertNotNull(image.getShape(new Point(x, y)));
		}
	}

	@Test
	@Order(11)
	public void save() {
		NodeController.saveDocument(new java.io.File("./test/document-test-a.json"), model.document);
	}

	@Test
	@Order(12)
	public void open() {
		model.document = NodeController.openDocument(new java.io.File("./test/document-test-a.json"));
		assertNotNull(model.document);
	}
	
	@Test
	@Order(13)
	public void test() {
		assertEquals(model.document.setIndex(0), true);
		assertEquals(model.document.getPage().setIndex(0),true);
		for(Image image: model.document.getPage().getImageList()) {
			model.document.getPage().setImage(image.uuid);
			int x = (int)(image.position.dimension.width/2);
			int y = (int)(image.position.dimension.height/2);
//			assertNotNull(image.file);
			assertNotNull(image.getShape(new Point(x,y)));
		}
		
		assertEquals(model.document.setIndex(1), true);
		assertEquals(model.document.getPage().setIndex(0),true);
		for(Image image: model.document.getPage().getImageList()) {
			model.document.getPage().setImage(image.uuid);
			int x = (int)(image.position.dimension.width/2);
			int y = (int)(image.position.dimension.height/2);
//			assertNotNull(image.file);
			assertNotNull(image.getShape(new Point(x,y)));
		}
		
		assertEquals(model.document.setIndex(2), true);
		assertEquals(model.document.getPage().setIndex(0),true);
		for(Image image: model.document.getPage().getImageList()) {
			model.document.getPage().setImage(image.uuid);
			int x = (int)(image.position.dimension.width/2);
			int y = (int)(image.position.dimension.height/2);
//			assertNotNull(image.file);
			assertNotNull(image.getShape(new Point(x,y)));
		}
		
		assertEquals(model.document.setIndex(3), true);
		assertEquals(model.document.getPage().setIndex(0),true);
		for(Image image: model.document.getPage().getImageList()) {
			model.document.getPage().setImage(image.uuid);
			int x = (int)(image.position.dimension.width/2);
			int y = (int)(image.position.dimension.height/2);
//			assertNotNull(image.file);
			assertNotNull(image.getShape(new Point(x,y)));
		}
	}
	
	@Test
	@Order(13) 
	public void setShapeData() {
		assertEquals(model.document.setIndex(0), true);
		assertEquals(model.document.getPage().setIndex(0),true);
		for(Image image: model.document.getPage().getImageList()) {
			model.document.getPage().setImage(image.uuid);
			int x = (int)(image.position.dimension.width/2);
			int y = (int)(image.position.dimension.height/2);
			Shape shape = image.getShape(new Point(x,y));
			assertNotNull(shape);
			Data data = new Data();
			data.text = new Text("Hello World 0");
			shape.setData(data);
		}
		
		assertEquals(model.document.setIndex(1), true);
		assertEquals(model.document.getPage().setIndex(0),true);
		for(Image image: model.document.getPage().getImageList()) {
			model.document.getPage().setImage(image.uuid);
			int x = (int)(image.position.dimension.width/2);
			int y = (int)(image.position.dimension.height/2);
			Shape shape = image.getShape(new Point(x,y));
			assertNotNull(shape);
			Data data = new Data();
			data.text = new Text("Hello World 1");
			shape.setData(data);
		}
		
		assertEquals(model.document.setIndex(2), true);
		assertEquals(model.document.getPage().setIndex(0),true);
		for(Image image: model.document.getPage().getImageList()) {
			model.document.getPage().setImage(image.uuid);
			int x = (int)(image.position.dimension.width/2);
			int y = (int)(image.position.dimension.height/2);
			Shape shape = image.getShape(new Point(x,y));
			assertNotNull(shape);
			Data data = new Data();
			data.text = new Text("Hello World 2");
			shape.setData(data);
		}
		
		assertEquals(model.document.setIndex(3), true);
		assertEquals(model.document.getPage().setIndex(0),true);
		for(Image image: model.document.getPage().getImageList()) {
			model.document.getPage().setImage(image.uuid);
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
		NodeController.saveDocument(new java.io.File("./test/document-test-b.json"), model.document);
	}
	
	@Test
	@Order(15)
	public void openShapeData() {
		model.document = NodeController.openDocument(new java.io.File("./test/document-test-b.json"));
		assertEquals(model.document.setIndex(0), true);
		assertEquals(model.document.getPage().setIndex(0),true);
		for(Image image: model.document.getPage().getImageList()) {
			model.document.getPage().setImage(image.uuid);
			int x = (int)(image.position.dimension.width/2);
			int y = (int)(image.position.dimension.height/2);
			Shape shape = image.getShape(new Point(x,y));
			assertNotNull(shape);
			assertEquals(shape.data.text.value, "Hello World 0");
		}
		
		assertEquals(model.document.setIndex(1), true);
		assertEquals(model.document.getPage().setIndex(0),true);
		for(Image image: model.document.getPage().getImageList()) {
			model.document.getPage().setImage(image.uuid);
			int x = (int)(image.position.dimension.width/2);
			int y = (int)(image.position.dimension.height/2);
			Shape shape = image.getShape(new Point(x,y));
			assertNotNull(shape);
			assertEquals(shape.data.text.value, "Hello World 1");
		}
		
		assertEquals(model.document.setIndex(2), true);
		assertEquals(model.document.getPage().setIndex(0),true);
		for(Image image: model.document.getPage().getImageList()) {
			model.document.getPage().setImage(image.uuid);
			int x = (int)(image.position.dimension.width/2);
			int y = (int)(image.position.dimension.height/2);
			Shape shape = image.getShape(new Point(x,y));
			assertNotNull(shape);
			assertEquals(shape.data.text.value, "Hello World 2");
		}
		
		assertEquals(model.document.setIndex(3), true);
		assertEquals(model.document.getPage().setIndex(0),true);
		for(Image image: model.document.getPage().getImageList()) {
			model.document.getPage().setImage(image.uuid);
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
		assertEquals(model.document.setIndex(0), true);
		List<Shape> shapeList = model.document.getPage().getGridShapeList();
		for(Shape s: shapeList) {
			s.bufferedImage = model.document.getShapeBufferedImage(model.document.getPage().getScaledBufferedImage(model), s);
			assertNotNull(s.bufferedImage);
		}
//		Zooniverse zooniverse = new Zooniverse();
//		NodeController.deleteDirectory(new File("./test/manifest-test-B"));
//		try {
//		zooniverse.generateManifest("./test/manifest-test-B", shapeList);
//		} catch(Exception e) {
//			logger.error("Exception "+e.getMessage());
//		}
	}
}
