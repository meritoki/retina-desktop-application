package com.meritoki.retina.application.desktop.model.document;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.File;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.meritoki.app.desktop.retina.model.document.Document;
import com.meritoki.app.desktop.retina.model.document.Image;
import com.meritoki.app.desktop.retina.model.document.Page;
import com.meritoki.app.desktop.retina.model.document.Point;
import com.meritoki.app.desktop.retina.model.document.Shape;

class ProjectTest {

	static Document project = null;
	static String pageZeroUUID = null;
	static String pageOneUUID = null;
	static String pageTwoUUID = null;
	
	static String fileZeroUUID = null;
	static String fileOneUUID = null;
	static double fileZeroWidth = 0;
	static double fileZeroHeight = 0;
	static double fileOneWidth = 0;
	static double fileOneHeight = 0;
	
	static String shapeZeroUUID = null;

	@BeforeAll
	public static void initialize() {
		project = new Document();
		Page page = new Page();
		Image file = new Image(new File("./data/image/01.jpg"));
		page.addImage(file);
		file = new Image(new File("./data/image/02.jpg"));
		page.addImage(file);
		Shape shape = new Shape();
		Point pointA = new Point(0,0);
		Point pointB = new Point(100,100);
		shape.position.addPoint(pointA);
		shape.position.addPoint(pointB);
		shape.position.normalize();
		shapeZeroUUID = shape.getUUID();
		page.addShape(shape);
		pageZeroUUID = page.uuid;
		project.addPage(page);
		page = new Page();
		file = new Image(new File("./data/image/02.jpg"));
		page.imageList.add(file);
		pageOneUUID = page.uuid;
		project.addPage(page);
		page = new Page();
		file = new Image(new File("./data/image/03.jpg"));
		page.imageList.add(file);
		pageTwoUUID = page.uuid;
		project.addPage(page);
//		page = new Page();
//		file = new File("./data/image", "04.jpg");
//		page.fileList.add(file);
//		project.addPage(page);
//		page = new Page();
//		file = new File("./data/image", "05.jpg");
//		page.fileList.add(file);
//		project.addPage(page);
//		page = new Page();
//		file = new File("./data/image", "06.jpg");
//		page.fileList.add(file);
//		project.addPage(page);
//		page = new Page();
//		file = new File("./data/image", "07.jpg");
//		page.fileList.add(file);
//		project.addPage(page);
//		page = new Page();
//		file = new File("./data/image", "08.jpg");
//		page.fileList.add(file);
//		project.addPage(page);
	}

	@Test
	public void setPage() {
		assertNotNull(project.getPage());
		project.setIndex(0);
		assertEquals(project.getPage().uuid, pageZeroUUID);
		project.setIndex(1);
		assertEquals(project.getPage().uuid, pageOneUUID);
		project.setIndex(2);
		assertEquals(project.getPage().uuid, pageTwoUUID);
	}
	
	@Test
	public void setPageFile() {
		project.setIndex(0);
		List<Image> fileList = project.getPage().getImageList();
		for(Image file: fileList) {
			Point point = new Point(file.position.offset+(file.position.width/2),file.position.margin+(file.position.height/2));
			assertEquals(project.getPage().getImage(point).getUUID(),file.getUUID());
		}
	}
	
	@Test
	public void setPageFileShape() {
		Point point = new Point(50,50);
		project.setIndex(0);
		Image file = project.getPage().getImage(point);
		project.getPage().setImage(file.getUUID());
		for(Shape s:project.getPage().getShapeList()) {
			System.out.println(s);
		}
		
		Shape shape = project.getPage().getImage().getShape(point);
		System.out.println(shape);
		assertEquals(shapeZeroUUID,shape.getUUID());
	}

}
