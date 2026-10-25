/*
 * Copyright 2021 Joaquin Osvaldo Rodriguez
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.meritoki.app.desktop.retina.model.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.meritoki.app.desktop.retina.model.Model;
import com.meritoki.app.desktop.retina.model.document.Grid;
import com.meritoki.app.desktop.retina.model.document.Image;
import com.meritoki.app.desktop.retina.model.document.Page;
import com.meritoki.app.desktop.retina.model.document.Shape;

public class ScriptPage extends Command {

	private Logger logger = LogManager.getLogger(ScriptPage.class.getName());

	public ScriptPage(Model document) {
		super(document, "executeScript");
	}

	public void execute() throws Exception {
		logger.info("execute()");
		this.user = this.model.cache.user;
		this.operationList = (LinkedList<Operation>) this.getOperationList(this.model.cache.pageList,
				this.model.cache.script);
	}
	
	@Override
	public void undo() throws Exception {
		logger.info("undo() executeScript command.operationList.size()="+this.operationList.size());
		Collections.reverse(this.operationList);
		for(Operation o: this.operationList) {
			if(o.sign == 0) {
				if(o.object instanceof List) {
					this.model.document.pageList = (List<Page>)o.object;
				}
			}
		}
		
	}

	@Override
	public void redo() throws Exception {
		Collections.reverse(this.operationList);
		for(Operation o: this.operationList) {
			if(o.sign == 1) {
				if(o.object instanceof List) {
					this.model.document.pageList = (List<Page>)o.object;
				}
			} 
		}
		
	}

	/**
	 * SWAP 1-2:3-4 INTERLACE 1-2:3-4 INSERT 1-2:3-4 SHEET even:odd | SHEET odd:even
	 * | SHEET 1:2 | SHEET 2:3
	 * 
	 * @param value
	 * @throws Exception
	 */
	public List<Operation> getOperationList(List<Page> pageList, String value) throws Exception {
		LinkedList<Operation> operationList = new LinkedList<>();
		value = value.replace("\n", "").replace("\r", "");
		String[] instructions = value.split(";");
		String instruction;
		String[] parameters;
		String a;
		String b;
		for (String i : instructions) {
			if (i.contains("SWAP")) {
				instruction = i.replaceFirst("SWAP", "");
				parameters = instruction.split(":");
				a = parameters[0].trim();
				b = parameters[1].trim();
				operationList.addAll(swapPage(pageList, a, b));
			} else if (i.contains("INSERT")) {
				instruction = i.replaceFirst("INSERT", "");
				parameters = instruction.split(":");
				a = parameters[0].trim();
				b = parameters[1].trim();
				operationList.addAll(insertPage(pageList, a, b));
			} else if (i.contains("JOIN")) {
				instruction = i.replaceFirst("JOIN", "");
				parameters = instruction.split(":");
				a = parameters[0].trim();
				b = parameters[1].trim();
				operationList.addAll(joinPage(pageList, a, b));
			} else if (i.contains("SPLIT")) {
				instruction = i.replaceFirst("SPLIT", "");
				a = instruction.trim();
				operationList.addAll(splitPage(pageList, a));
			} else if (i.contains("LAYOUT")) {
				instruction = i.replaceFirst("LAYOUT", "");
				parameters = instruction.split(":");
				a = parameters[0].trim();
				b = parameters[1].trim();
				operationList.addAll(layoutPage(pageList, a, b));
			} else if (i.contains("CLEAR")) {
				instruction = i.replaceFirst("CLEAR", "");
				a = instruction.trim();
				operationList.addAll(clearPage(pageList, a));
			}
		}
		logger.info("getOperationList(...) operationList.size()=" + operationList.size());
		return operationList;
	}

	public List<Operation> swapPage(List<Page> pageList, String a, String b) throws Exception {
		List<Operation> operationList = new ArrayList<>();
		// Operation operation = new Operation();
		logger.info("swap(" + a + "," + b + ")");
		String[] A;
		String[] B;
		String i;
		String j;
		int x;
		int y;
		int diff = 0;
		if (a.contains("-") && b.contains("-")) {
			A = a.split("-");
			B = b.split("-");
			i = A[0];
			j = A[1];
			x = Integer.parseInt(i);
			y = Integer.parseInt(j);
			diff = y - x;
			i = B[0];
			j = B[1];
			x = Integer.parseInt(i);
			y = Integer.parseInt(j);
			if (diff == (y - x)) {
				logger.info("valid");
			}
		} else {
			i = a;
			j = b;
			x = Integer.parseInt(i);
			y = Integer.parseInt(j);
			operationList.addAll(swapPage(pageList, x, y));
		}
		return operationList;
	}

	public List<Operation> swapPage(List<Page> pageList, int a, int b) {
		logger.info("swap(" + a + "," + b + ")");
		List<Operation> operationList = new ArrayList<>();
		Operation operation = new Operation();
		operation.object = this.copyPageList(pageList);
		operation.sign = 0;
		operation.id = UUID.randomUUID().toString();
		operationList.add(operation);

		Collections.swap(pageList, a, b);

		operation = new Operation();
		operation.object = this.copyPageList(pageList);
		operation.sign = 1;
		operation.id = UUID.randomUUID().toString();
		operationList.add(operation);
		return operationList;

	}

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
	public List<Operation> insertPage(List<Page> pageList, String a, String b) throws Exception {
		logger.info("insert(" + a + "," + b + ")");
		List<Operation> operationList = new ArrayList<>();
		String[] A;
		String[] B;
		String i;
		String j;
		int x;
		int y;
		int diff = 0;
		if (a.contains("-") && b.contains("-")) {
			A = a.split("-");
			B = b.split("-");
			i = A[0];
			j = A[1];
			x = Integer.parseInt(i);
			y = Integer.parseInt(j);
			diff = y - x;
			i = B[0];
			j = B[1];
			x = Integer.parseInt(i);
			y = Integer.parseInt(j);
			if (diff == (y - x)) {
				logger.info("valid");
			}
		} else {
			i = a;
			j = b;
			x = Integer.parseInt(i);
			y = Integer.parseInt(j);
			operationList.addAll(insertPage(pageList, x, y));
		}
		return operationList;
	}

	public List<Operation> insertPage(List<Page> pageList, int a, int b) {
		List<Operation> operationList = new ArrayList<>();
		Operation operation = new Operation();
		List<Page> pList = new ArrayList<>();
		for (Page page : pageList) {
			pList.add(new Page(page));
		}
		operation.object = pList;
		operation.sign = 1;
		operation.id = UUID.randomUUID().toString();
		operationList.add(operation);

		// Logic
		Page page = pageList.remove(a);
		pageList.add(b, page);

		operation = new Operation();
		operation.object = pageList;
		operation.sign = 0;
		operation.id = UUID.randomUUID().toString();
		operationList.add(operation);
		return operationList;
	}

	public List<Operation> joinPage(List<Page> pageList, String a, String b) {
		logger.info("joinPage(" + a + "," + b + ")");
		List<Operation> operationList = new ArrayList<>();
		// Operation Undo
		Operation operation = new Operation();
		operation.object = this.copyPageList(pageList);
		operation.sign = 0;
		operation.id = UUID.randomUUID().toString();
		operationList.add(operation);

		// Logic
		int x = Integer.parseInt(a);
		int y = Integer.parseInt(b);
		Page pageA = pageList.get(x);
		Page pageB = pageList.get(y);
		for (Image image : pageB.imageList) {
			pageA.addImage(image);
		}
		pageA.setBufferedImage(null);
		pageList.remove(y);
		// End Logic

		// Operation Redo
		operation = new Operation();
		operation.object = this.copyPageList(pageList);
		operation.sign = 1;
		operation.id = UUID.randomUUID().toString();
		operationList.add(operation);
		return operationList;
	}

	public List<Operation> splitPage(List<Page> pageList, String a) {
		logger.info("splitPage(" + a + ")");
		Operation operation = new Operation();
		operation.object = this.copyPageList(pageList);
		operation.sign = 0;
		operation.id = UUID.randomUUID().toString();
		operationList.add(operation);

		// Logic
		int x = Integer.parseInt(a);
//		int y = Integer.parseInt(b);
		Page page = pageList.get(x);
		List<Image> imageList = page.getImageList();
		Collections.reverse(imageList);
		pageList.remove(x);
		for (Image image : imageList) {
			image.setOffset(0);
			image.setMargin(0);
			pageList.add(x, new Page(image));
		}
		// End Logic

		// Operation Redo
		operation = new Operation();
		operation.object = this.copyPageList(pageList);
		operation.sign = 1;
		operation.id = UUID.randomUUID().toString();
		operationList.add(operation);
		return operationList;
	}

	public List<Operation> layoutPage(List<Page> pageList, String a, String b) {
		logger.info("joinPage(" + a + "," + b + ")");
		List<Operation> operationList = new ArrayList<>();
		// Operation Undo
		Operation operation = new Operation();
		operation.object = this.copyPageList(pageList);
		operation.sign = 0;
		operation.id = UUID.randomUUID().toString();
		operationList.add(operation);

		// Logic
		int x = Integer.parseInt(a);
		int y = Integer.parseInt(b);
		Page pageA = pageList.get(x);
		Page pageB = pageList.get(y);
		// Pending
		for (Image image : pageA.imageList) {
			for (Shape shape : image.shapeList) {
				for (Image i : pageB.imageList) {
					if (shape instanceof Grid) {
						Grid grid = new Grid((Grid) shape, false);
						grid.setScale(pageB.position.scale);
						if (i.containsShape(grid)) {
							pageB.addShape(grid);
						}
					} else {
						Shape s = new Shape(shape, false);
						s.setScale(pageB.position.scale);
						if (i.containsShape(s)) {
							pageB.addShape(s);
						}
					}
				}
			}
		}

		// End Logic

		// Operation Redo
		operation = new Operation();
		operation.object = this.copyPageList(pageList);
		operation.sign = 1;
		operation.id = UUID.randomUUID().toString();
		operationList.add(operation);
		return operationList;
	}

	public List<Operation> clearPage(List<Page> pageList, String a) {
		Operation operation = new Operation();
		operation.object = this.copyPageList(pageList);
		operation.sign = 0;
		operation.id = UUID.randomUUID().toString();
		operationList.add(operation);

		// Logic
		int x = Integer.parseInt(a);
//		int y = Integer.parseInt(b);
		Page page = pageList.get(x);
		for (Image image : page.imageList) {
			image.removeAllShapes();
		}
		// End Logic

		// Operation Redo
		operation = new Operation();
		operation.object = this.copyPageList(pageList);
		operation.sign = 1;
		operation.id = UUID.randomUUID().toString();
		operationList.add(operation);
		return operationList;
	}

	public List<Page> copyPageList(List<Page> pageList) {
		List<Page> pList = new ArrayList<>();
		for (Page page : pageList) {
			pList.add(new Page(page));
		}
		return pList;
	}
}

//if (i.contains("INTERLACE")) {
//instruction = i.replaceFirst("INTERLACE", "");
//parameters = instruction.split(":");
//a = parameters[0].trim();
//b = parameters[1].trim();
//operationList.add(interlacePage(pageList, a, b));
//} else 
