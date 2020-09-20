/*
 * Copyright 2020 Joaquin Osvaldo Rodriguez
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
package com.meritoki.app.desktop.retina.model.provider.meritoki;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.meritoki.app.desktop.retina.controller.node.NodeController;
import com.meritoki.app.desktop.retina.model.Model;
import com.meritoki.app.desktop.retina.model.document.Page;
import com.meritoki.app.desktop.retina.model.document.Shape;
import com.meritoki.app.desktop.retina.model.provider.Provider;
import com.meritoki.library.cortex.model.Belief;
import com.meritoki.library.cortex.model.Concept;
import com.meritoki.library.cortex.model.Node;
import com.meritoki.library.cortex.model.Point;
import com.meritoki.library.cortex.model.retina.Retina;

/**
 * As a provider there are several things that need to be stored in the provider
 * folder for a particular document The document UUID serves as a identifier for
 * a file or folder, likely folder where everything can be stored
 * 
 * 
 * @author jorodriguez
 *
 */
public class Meritoki extends Provider {

	public Document document;
	private Model model;
	public File defaultFile;
	private boolean visible = true;
	public Retina retina = new Retina();
	private Recognition recognition;

	public Meritoki() {
		super("meritoki");
		this.init();
	}

	public void setModel(Model model) {
		this.model = model;
		this.recognition = new Recognition(0, this.model);
	}

	public void init() {
		File directory = new File(getMeritokiHome());
		if (!directory.exists()) {
			directory.mkdirs();
		}
		this.document = new Document();
	}

	public void update() {
		this.initInputList();
	}

	public void setVisible(boolean visible) {
		System.out.println("setVisible(" + visible + ")");
		this.visible = visible;
		if (!visible) {
			if (this.retina.loop) {
				if (recognition != null)
					recognition.start();
			} else {
				if (recognition != null && recognition.getRun())
					recognition.destroy();
			}
		}
	}

	public void open(String documentUUID) {
		File directory = new File(getMeritokiHome() + NodeController.getSeperator() + documentUUID);
		this.defaultFile = new File(directory + NodeController.getSeperator() + "cortex.json");
		if (this.defaultFile.exists()) {
			// not functional
			this.document = (Document) NodeController.openJson(this.defaultFile, Document.class);
		} else {
			this.document = new Document();
			directory.mkdirs();
			NodeController.saveJson(this.defaultFile, this.document);
		}
		document.cortex.load();
	}

	public void exportDocument(File destination) {

	}

	public void importDocument(File file) {
		this.defaultFile = file;
		if (this.defaultFile.exists()) {
			// not functional
			this.document = (Document) NodeController.openJson(this.defaultFile, Document.class);
		}
		document.cortex.load();
	}

	public void save() {
		NodeController.saveJson(this.defaultFile, this.document);
	}

	public void paint(Graphics graphics) {
//        System.out.println("paint("+String.valueOf(graphics != null)+")");
		Graphics2D graphics2D = null;
		if (graphics != null) {
			graphics2D = (Graphics2D) graphics.create();
		}
		Concept concept = null;
		if (this.retina.manual) {
			concept = this.model.cache.concept;
			if (concept != null && concept.value.isEmpty()) {
				System.out.println("A concept="+concept);
				concept = new Concept();
			}
		} else {
			concept = new Concept();
		}
		this.retina.iterate(graphics2D, this.getBufferedImage(), this.document.cortex, concept);
	}

	// The model gives the image which has priority,
	// if page has no shapes, then the whole page is loaded;
	// if there are shapes then the currently selected shape is visualized.
	public BufferedImage getBufferedImage() {
		Input input = this.document.getInput();
		BufferedImage bufferedImage = (input != null) ? input.getBufferedImage() : null;
//		System.out.println("getBufferedImage() bufferedImage="+bufferedImage);
		return bufferedImage;
	}

	// Need function that returns a list of training candidates, they do not
	// necessarily require a concept
	// Every time this function is called a clean list is returned with a Page
	// followed by all of its Shapes
	// This is easy to visualize Pages or Shapes can be chosen using the Dialogs
	public List<Input> getInputList() {
		List<Input> inputList = new ArrayList<>();
		if (this.model != null && this.model.document != null) {
			List<Page> pageList = this.model.document.pageList;
			for (Page page : pageList) {
				Input input = new Input();
				input.uuid = page.uuid;
				input.page = page;
				input.page.bufferedImage = page.getBufferedImage(model);
				inputList.add(input);
				List<Shape> shapeList = page.getGridShapeList();
				for (Shape shape : shapeList) {
					input = new Input();
					input.uuid = shape.uuid;
					input.shape = shape;
					input.shape.bufferedImage = this.model.document
							.getShapeBufferedImage(page.getScaledBufferedImage(this.model), shape);
					inputList.add(input);
				}
			}
		}
		return inputList;
	}

	public void setInputList(List<Input> inputList) {
		this.document.inputList = new ArrayList<>();
		this.document.inputList.addAll(inputList);
	}

	public void initInputList() {
		this.setInputList(this.getInputList());
	}

	public static String getMeritokiHome() {
		return NodeController.getProviderHome() + NodeController.getSeperator() + "meritoki";
	}

	// Need fucntion that returns list of inference candidates, these are shapes
	// without text;

}

//public void paint(Graphics graphics) {
//Graphics2D graphics2D = null;
//if(graphics != null) {
//	graphics2D = (Graphics2D) graphics.create();
//}
//if (flag) {
//	if (pointStack.size() > 0) {
//		Point point = this.pointStack.pop();
//		System.out.println(point);
//		this.retina.setOrigin(point.x, point.y);
//		this.retina.input(graphics2D);
//		List<Node> nodeList = point.getChildren();
//		int max = 2;
//		int min = 0;
//		int value = 2;
//		int random;
//		double minDistance = 32;
//		for (Node n : nodeList) {
//			Point p = (Point) n;
//			if(this.retina.getDistance(point, p)>minDistance) {
//				System.out.println("node point" + p);
//				pointStack.push(p);
//			}
////			random = min + (int)(Math.random() * ((max - min) + 1));
////			if(random == value) {
////				Point p = (Point) n;
////				System.out.println("node point" + p);
////				pointStack.push(p);
////			}
//		}
//		System.out.println("this.pointStack.size()=" + this.pointStack.size());
//
//	} else {
//		this.retina.setBufferedImage(this.getBufferedImage());
//		this.retina.setCortex(this.document.cortex);
//		System.out.println("this.retina.getMagnification()=" + this.retina.getMagnification());
//		if (this.distance == 0) {
//			System.out.println("this.distance == 0");
//			this.retina.maxDistance = this.retina.getMaxDistance();
//			this.distance = this.retina.maxDistance;
//			this.size = (int) (this.retina.getMaxDistance() - this.retina.focalLength);
//			System.out.println("size=" + size);
//			this.index = 0;
//			this.retina.setDistance(this.distance);
//			this.retina.input(graphics2D);
////			this.pointStack = this.retina.getPointList();
//			System.out.println("this.pointStack.size()=" + this.pointStack.size());
//		} else {
//			this.interval = this.size / 12;
//			if ((index * this.interval) < this.size) {
//				System.out.println("index=" + index);
//				this.distance = size;
//				this.distance -= index * this.interval;
//				this.index++;
//				this.retina.setDistance(this.distance);
//				this.retina.input(graphics2D);
//				if ((this.index * this.interval) > this.size) {
//					this.pointStack.push(this.retina.root);// new LinkedList(this.retina.pointList);
//				} else {
////					this.pointStack = this.retina.getPointList();
//				}
//				System.out.println("this.pointStack.size()=" + this.pointStack.size());
//			} else {
//				flag = false;
//			}
//		}
//	}
//} else {
//	this.retina.setBufferedImage(this.getBufferedImage());
//	this.retina.setCortex(this.document.cortex);
//	if (this.distance == 0) {
//		this.retina.maxDistance = this.retina.getMaxDistance();
//		this.distance = this.retina.maxDistance;
//	}
//	this.retina.setDistance(this.distance);
//	this.retina.input(graphics2D);
//	this.retina.drawPointList(graphics2D);
//}
//}

//public File inputFile;
//public File outputFile;

//public List<Input> openInput(String documentUUID) {
//	File directory = new File(getMeritokiHome() + NodeController.getSeperator() + documentUUID);
//	this.inputFile = new File(
//			directory + NodeController.getSeperator() + NodeController.getSeperator() + "input.json");
//	if (this.inputFile.exists()) {
//		inputQueue = (List<Input>) NodeController.openJson(this.inputFile, new TypeReference<List<Input>>() {
//		});
//	} else {
//		this.inputQueue = new ArrayList<>();
//		directory.mkdirs();
//		NodeController.saveJson(this.inputFile, this.inputQueue);
//	}
//	return inputQueue;
//}
//
//public void saveInput() {
//	NodeController.saveJson(this.inputFile, this.inputQueue);
//}
//
//public List<Output> openOutput(String documentUUID) {
//	File directory = new File(getMeritokiHome() + NodeController.getSeperator() + documentUUID);
//	this.outputFile = new File(
//			directory + NodeController.getSeperator() + NodeController.getSeperator() + "output.json");
//	if (this.outputFile.exists()) {
//		this.outputList = (List<Output>) NodeController.openJson(this.outputFile,
//				new TypeReference<List<Output>>() {
//				});
//	} else {
//		this.outputList = new ArrayList<>();
//		directory.mkdirs();
//		NodeController.saveJson(this.outputFile, this.outputList);
//	}
//	return outputList;
//}

//if (s.data.text.value != null) {
//for (Input input : this.inputQueue) {
//	if (input.shape.uuid.equals(s.uuid)) {
//		add = false;
//		input.shape.bufferedImage = 
//		input.concept = s.data.text.value;
//	}
//}
//if (add) {
//	Input input = new Input();
//	input.shape = s;
//	input.shape.bufferedImage = document
//			.getShapeBufferedImage(page.getScaledBufferedImage(this.model), s);
//	input.concept = s.data.text.value;
//	input.flag = false;
//	this.inputQueue.add(input);
//}
//}

//public void saveOutput() {
//NodeController.saveJson(this.outputFile, this.outputList);
//}

//System.out.println(concept);
//if (this.retina.cortex != null) {
//	Belief belief = this.retina.cortex.getBelief();
//	if (belief != null) {
//		List<Concept> conceptList = belief.getConceptList();
//		System.out.println(conceptList);
//		if (conceptList != null && conceptList.size() > 0) {
//			concept = conceptList.get(0);
//		}
//	}
//}

//System.out.println("B concept="+concept);
//Belief belief = this.retina.cortex.getBelief();
//System.out.println(belief);
//if (belief != null) {
//	List<Concept> conceptList = belief.getConceptList();
//	System.out.println(conceptList);
//	if (conceptList != null && conceptList.size() > 1) {
//		concept = conceptList.get(conceptList.size());
//	}
//}
