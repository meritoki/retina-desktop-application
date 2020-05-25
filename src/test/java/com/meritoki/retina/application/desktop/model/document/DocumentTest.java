package com.meritoki.retina.application.desktop.model.document;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.File;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.meritoki.app.desktop.retina.model.document.Document;
import com.meritoki.app.desktop.retina.model.document.Image;
import com.meritoki.app.desktop.retina.model.document.Page;
import com.meritoki.app.desktop.retina.model.document.Point;

class DocumentTest {

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

	@BeforeAll
	public static void initialize() {
		document = new Document();
		Page page = new Page();
		page.addImage(new Image(new File("./data/image/01.jpg")));
		page.addImage(new Image(new File("./data/image/02.jpg")));
		page.addImage(new Image(new File("./data/image/03.jpg")));
		pageZeroUUID = page.uuid;
		document.addPage(page);
		page = new Page(new Image(new File("./data/image/02.jpg")));
		pageOneUUID = page.uuid;
		document.addPage(page);
		page = new Page(new Image(new File("./data/image/03.jpg")));
		pageTwoUUID = page.uuid;
		document.addPage(page);
	}

	@Test
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
	public void getImage() {
		assertEquals(document.setIndex(0), true);
		assertNotNull(document.getPage());
		for(Image image: document.getPage().imageList) {
			Point point = new Point(image.position.point);
			point.x += image.position.dimension.width/2;
			point.y += image.position.dimension.height/2;
			assertNotNull(document.getImage(point));
			assertEquals(document.getImage(point).uuid, image.uuid);
		}
		assertEquals(document.setIndex(1), true);
		assertNotNull(document.getPage());
		for(Image image: document.getPage().imageList) {
			Point point = new Point(image.position.absolutePoint);
			point.x += image.position.dimension.width/2;
			point.y += image.position.dimension.height/2;
			assertNotNull(document.getImage(point));
			assertEquals(document.getImage(point).uuid, image.uuid);
		}
		assertEquals(document.setIndex(2), true);
		assertNotNull(document.getPage());
		for(Image image: document.getPage().imageList) {
			Point point = new Point(image.position.absolutePoint);
			point.x += image.position.dimension.width/2;
			point.y += image.position.dimension.height/2;
			assertNotNull(document.getImage(point));
			assertEquals(document.getImage(point).uuid, image.uuid);
		}
	}
	
	@Test
	public void addShape() {
		
	}

}
