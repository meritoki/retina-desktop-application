package com.meritoki.app.desktop.retina.model.document.command;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.meritoki.app.desktop.retina.model.document.Document;
import com.meritoki.app.desktop.retina.model.document.Image;
import com.meritoki.app.desktop.retina.model.document.Page;

public class JoinPage extends Command {

	private static Logger logger = LogManager.getLogger(AddShape.class.getName());
	
	public JoinPage(Document document) {
		super(document, "joinPage");
	}
	
	public void execute() {
//		this.user = this.document.cache.user;
//		
//		String a = this.document.cache.a;
//		String b = this.document.cache.b;
//		int x = Integer.parseInt(a);
//		int y = Integer.parseInt(b);
//		Page pageA = this.document.pageList.get(x);
//		Page pageB = this.document.pageList.get(y);
//		for (Image file : pageB.imageList) {
//			pageA.addImage(file);
//		}
//		pageA.setBufferedImage(null);
//		this.document.pageList.remove(y);
//		
//		Operation operation = new Operation();
//		operation.object = this.document.pageList;
//		operation.sign = 1;
//		operation.id = UUID.randomUUID().toString();
//		this.operationList.push(operation);
	}

}
