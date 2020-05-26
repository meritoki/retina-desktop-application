package com.meritoki.retina.application.desktop.model.document;

import java.io.File;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.meritoki.app.desktop.retina.model.document.Document;
import com.meritoki.app.desktop.retina.model.document.Image;
import com.meritoki.app.desktop.retina.model.document.Page;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ZooniverseTest {

	static Document document = null;
	@BeforeAll
	public static void initialize() {
		document = new Document();
		Page page = new Page();
		page = new Page(new Image(new File("./data/image/01.jpg")));
		document.addPage(page);
		page = new Page();
		page.addImage(new Image(new File("./data/image/02.jpg")));
		document.addPage(page);
		page = new Page();
		page.addImage(new Image(new File("./data/image/01.jpg")));
		page.addImage(new Image(new File("./data/image/02.jpg")));
		document.addPage(page);
	}
	
	@Test
	@Order(1)
	public void export() {
		
	}
}
