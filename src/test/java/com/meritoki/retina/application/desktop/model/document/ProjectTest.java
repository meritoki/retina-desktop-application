package com.meritoki.retina.application.desktop.model.document;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
		Image file = new Image("./data/image", "01.jpg");
		page.addFile(file);
		file = new Image("./data/image", "02.jpg");
		page.addFile(file);
		Shape shape = new Shape();
		Point pointA = new Point(0,0);
		Point pointB = new Point(100,100);
		shape.dimension.addPoint(pointA);
		shape.dimension.addPoint(pointB);
		shape.dimension.normalize();
		shapeZeroUUID = shape.getUUID();
		page.addShape(shape);
		pageZeroUUID = page.uuid;
		project.addPage(page);
		page = new Page();
		file = new Image("./data/image", "02.jpg");
		page.imageList.add(file);
		pageOneUUID = page.uuid;
		project.addPage(page);
		page = new Page();
		file = new Image("./data/image", "03.jpg");
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
			Point point = new Point(file.offset+(file.getWidth()/2),file.margin+(file.getHeight()/2));
			assertEquals(project.getPage().getFile(point).getUUID(),file.getUUID());
		}
	}
	
	@Test
	public void setPageFileShape() {
		Point point = new Point(50,50);
		project.setIndex(0);
		Image file = project.getPage().getFile(point);
		project.getPage().setFile(file.getUUID());
		for(Shape s:project.getPage().getShapeList()) {
			System.out.println(s);
		}
		
		Shape shape = project.getPage().getFile().getShape(point);
		System.out.println(shape);
		assertEquals(shapeZeroUUID,shape.getUUID());
	}

}
