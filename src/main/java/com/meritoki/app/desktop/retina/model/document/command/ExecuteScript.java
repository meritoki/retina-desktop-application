package com.meritoki.app.desktop.retina.model.document.command;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.meritoki.app.desktop.retina.model.document.Document;
import com.meritoki.app.desktop.retina.model.document.Image;
import com.meritoki.app.desktop.retina.model.document.Page;

public class ExecuteScript extends Command {
	
	private  Logger logger = LogManager.getLogger(ExecuteScript.class.getName());
	
	public ExecuteScript(Document document) {
		super(document, "executeScript");
	}

	public void execute() throws Exception {
		this.user = this.document.cache.user;
		this.operationList.addAll(this.getOperationList(this.document.cache.pageList, this.document.cache.script));
	}
	
	/**
	 * SWAP 1-2:3-4 INTERLACE 1-2:3-4 INSERT 1-2:3-4 SHEET even:odd | SHEET odd:even
	 * | SHEET 1:2 | SHEET 2:3
	 * 
	 * @param value
	 * @throws Exception
	 */
	public List<Operation> getOperationList(List<Page> pageList, String value) throws Exception {
		List<Operation> operationList = new ArrayList<>();
		value = value.replace("\n", "").replace("\r", "");
		String[] instructions = value.split(";");
		String instruction;
		String[] parameters;
		String a;
		String b;
		for (String i : instructions) {
//			if (i.contains("SWAP")) {
//				instruction = i.replaceFirst("SWAP", "");
//				parameters = instruction.split(":");
//				a = parameters[0].trim();
//				b = parameters[1].trim();
//				operationList.add(swapPage(pageList, a, b));
//			} else if (i.contains("INTERLACE")) {
//				instruction = i.replaceFirst("INTERLACE", "");
//				parameters = instruction.split(":");
//				a = parameters[0].trim();
//				b = parameters[1].trim();
//				operationList.add(interlacePage(pageList, a, b));
//			} else if (i.contains("INSERT")) {
//				instruction = i.replaceFirst("INSERT", "");
//				parameters = instruction.split(":");
//				a = parameters[0].trim();
//				b = parameters[1].trim();
//				operationList.add(insertPage(pageList, a, b));
//			} else 
				if (i.contains("JOIN")) {
				instruction = i.replaceFirst("JOIN", "");
				parameters = instruction.split(":");
				a = parameters[0].trim();
				b = parameters[1].trim();
				operationList.add(joinPage(pageList, a, b));
			}
		}
		return operationList;
	}

	public Operation joinPage(List<Page> pageList, String a, String b) {
		Operation operation = new Operation();
		operation.object = pageList;
		operation.sign = 1;
		operation.id = UUID.randomUUID().toString();
		int x = Integer.parseInt(a);
		int y = Integer.parseInt(b);
		Page pageA = pageList.get(x);
		Page pageB = pageList.get(y);
		for (Image image : pageB.imageList) {
			pageA.addImage(image);
		}
		pageA.setBufferedImage(null);
		pageList.remove(y);
		return operation;
	}

//	public  void swapPage(List<Page> pageList, String a, String b) throws Exception {
//		Operation operation = new Operation();
//		logger.info("swap(" + a + "," + b + ")");
//		String[] A;
//		String[] B;
//		String i;
//		String j;
//		int x;
//		int y;
//		int diff = 0;
//		if (a.contains("-") && b.contains("-")) {
//			A = a.split("-");
//			B = b.split("-");
//			i = A[0];
//			j = A[1];
//			x = Integer.parseInt(i);
//			y = Integer.parseInt(j);
//			diff = y - x;
//			i = B[0];
//			j = B[1];
//			x = Integer.parseInt(i);
//			y = Integer.parseInt(j);
//			if (diff == (y - x)) {
//				logger.info("valid");
//			}
//		} else {
//			i = a;
//			j = b;
//			x = Integer.parseInt(i);
//			y = Integer.parseInt(j);
//			swapPage(pageList, x, y);
//		}
//	}
//
//	public  void swapPage(List<Page> pageList, int a, int b) {
//		logger.info("swap(" + a + "," + b + ")");
//		Collections.swap(pageList, a, b);
//	}
//
//	public  void interlacePage(List<Page> pageList, String a, String b) throws Exception {
//		logger.info("interlace(" + a + "," + b + ")");
//		String[] A;
//		String[] B;
//		if (a.contains("-") && b.contains("-")) {
//			A = a.split("-");
//			B = b.split("-");
//		}
//	}
//
//	public  void insertPage(List<Page> pageList, String a, String b) throws Exception {
//		logger.info("insert(" + a + "," + b + ")");
//		String[] A;
//		String[] B;
//		String i;
//		String j;
//		int x;
//		int y;
//		int diff = 0;
//		if (a.contains("-") && b.contains("-")) {
//			A = a.split("-");
//			B = b.split("-");
//			i = A[0];
//			j = A[1];
//			x = Integer.parseInt(i);
//			y = Integer.parseInt(j);
//			diff = y - x;
//			i = B[0];
//			j = B[1];
//			x = Integer.parseInt(i);
//			y = Integer.parseInt(j);
//			if (diff == (y - x)) {
//				logger.info("valid");
//			}
//		} else {
//			i = a;
//			j = b;
//			x = Integer.parseInt(i);
//			y = Integer.parseInt(j);
//			insertPage(pageList, x, y);
//		}
//	}
//
//	public  void insertPage(List<Page> pageList, int a, int b) {
//		Page page = pageList.remove(a);
//		pageList.add(b, page);
//	}
//
//	public  void insert(List<Page> pageList, int index, int a, int b, int x, int y) {
//		Page page = pageList.get(index);
//		Shape data = page.getMatrix().init().get(a).remove(b);
//		page.getMatrix().init().get(x).add(y, data);
//	}
}
