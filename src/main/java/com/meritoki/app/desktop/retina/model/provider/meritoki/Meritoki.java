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
package com.meritoki.app.desktop.retina.model.provider.meritoki;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.meritoki.app.desktop.retina.controller.node.NodeController;
import com.meritoki.app.desktop.retina.model.document.Page;
import com.meritoki.app.desktop.retina.model.document.Shape;
import com.meritoki.app.desktop.retina.model.provider.Provider;
import com.meritoki.library.cortex.model.Belief;
import com.meritoki.library.cortex.model.eye.retina.Retina;
import com.meritoki.library.cortex.model.eye.retina.State;
import com.meritoki.library.cortex.model.network.ColorType;
import com.meritoki.library.cortex.model.network.square.Squared;
import com.meritoki.library.cortex.model.unit.Concept;
import com.meritoki.library.cortex.model.unit.Point;

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

	private static final Logger logger = LogManager.getLogger(Meritoki.class.getName());
	public boolean flag = true;
	public Document document = new Document();
	public File file;
	private boolean visible = true;
	public Retina retina = new Retina(null, null);
//	private Recognition recognition;
	public Dimension dimension;
	public boolean loop = false;
	public List<Input> inputList;
	public int index = 0;
	public BufferedImage bufferedImage;

	public Meritoki() {
		super("meritoki");
		File directory = new File(getMeritokiHome());
		if (!directory.exists()) {
			directory.mkdirs();
		}
	}

	/**
	 * Meritoki can know the size of the JPanel
	 * @param dimension
	 */
	public void setDimension(Dimension dimension) {
		this.dimension = dimension;
		this.retina.setDimension(dimension);
	}

	public void zoom(double factor) {
		this.retina.deltaDistance(factor);
		Point origin = new Point(this.retina.getInputCenterX(), this.retina.getInputCenterY());
//		origin.center = true;
		this.retina.setOrigin(origin);
	}

	public void input(Object object) {// Point point) {
		logger.info("input(" + object + ")");
		if (object instanceof Point) {
//			logger.info("input(" + object + ") instanceof Point");
			Point point = (Point) object;
			this.retina.setOrigin(point);
		}
	}

	public void scale() {
		this.retina.setDistance(this.retina.minDistance);
		Point origin = new Point(this.retina.getInputCenterX(), this.retina.getInputCenterY());
//		origin.center = true;
		this.retina.setOrigin(origin);
	}

	@Override
	public void init() {// String documentUUID) {
		logger.info("init()");
		String uuid = this.model.document.uuid;
		File directory = new File(getCortexHome(uuid));
		this.file = new File(directory + NodeController.getSeperator() + "cortex.json");
		if (this.file.exists()) {
			logger.info("init() defaultFile=" + file);
			this.document = (Document) NodeController.openJson(this.file, Document.class);
		} else {
			this.document = new Document();
			this.document.cortex = new Squared(new ColorType[]{ ColorType.BRIGHTNESS, ColorType.RED, ColorType.GREEN, ColorType.BLUE }, 0, 0, 27, 10, 0);
		}
		this.document.cortex.load();// ?
		this.document.cortex.update();
		this.retina = new Retina(this.document.cortex);
		this.update();
	}

	public void update() {
		this.setInputList(this.getInputList());
		Input currentInput = this.getCurrentInput();
		if(!this.inputList.contains(currentInput)) {
			this.inputList.add(currentInput);
		}

	}

	public void start() {
		this.loop = true;
		this.retina.setDistance(0);
	}

	public void stop() {
		this.loop = false;
	}

	public void reset() {
		logger.info("reset()");
		this.document = new Document();
		this.retina = new Retina(this.getBufferedImage(), this.document.cortex);
		this.retina.setDimension(this.dimension);
		this.update();
	}

	public void paint(Graphics graphics) {
//		logger.info("paint(" + String.valueOf(graphics != null) + ")");
		Graphics2D graphics2D = null;
		if (graphics != null) {
			graphics2D = (Graphics2D) graphics.create();
		}
		// Has to work in and out of loop;
		Input input = this.getInput();
		if (input != null) {
			Concept concept = input.concept;
			Concept override = this.model.cache.concept;
			if (!loop) {
				if (override == null) {
					concept = null;
				} else if (override.uuid.isEmpty()) {
					concept = new Concept();
				} else {
					concept = override;
				}
			}
//		logger.info("paint(" + String.valueOf(graphics != null) + ") concept="+concept);
			// Difference is call iterate over input directly;
			// When loop is true, call iterate;
			this.bufferedImage = this.getBufferedImage();
			if (this.bufferedImage != this.retina.bufferedImage) {
//				this.retina = new Retina(this.bufferedImage, this.document.cortex);
				this.retina.setBufferedImage(this.bufferedImage);
				this.retina.setDimension(this.dimension);
			}
			if (this.loop) {
				// Problem - Image is processed over and over again, this adds time.
				this.retina.iterate(graphics2D, concept);// bufferedImage, this.document.cortex,
				// Passing bufferedImage and cortex.
				if (this.retina.state == State.COMPLETE) {
					logger.info("COMPLETE");
					input.scan = false;
					this.document.inputMap.put(input.uuid, input);
					this.update();
					boolean flag = this.setIndex(true);
					if (flag) {
						this.retina.setDistance(0);
					} else {
						this.loop = false;
					}
				}
			} else {
				this.retina.input(graphics2D, this.retina.getOrigin(), concept);// bufferedImage, this.document.cortex,
			}
		}
	}

	public void save() {
		logger.info("save()");
		String uuid = this.model.document.uuid;
		File directory = new File(getCortexHome(uuid));
		this.file = new File(directory + NodeController.getSeperator() + "cortex.json");
		logger.info("init() defaultFile=" + file);
		if (!this.file.exists()) {
			directory.mkdirs();
			NodeController.saveJson(this.file, this.document);
		}
		for (Belief b : this.document.cortex.beliefList) {
			if (b.file == null) {
				directory = new File(getBeliefHome() + NodeController.getSeperator());
				if (!directory.exists()) {
					directory.mkdirs();
				}
				b.file = new File(directory + NodeController.getSeperator() + b.uuid + ".jpg");
				if (!b.file.exists()) {
					try {
						NodeController.saveJpg(b.file, b.bufferedImage);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			b.filePath = b.file.getParent();
			b.fileName = b.file.getName();
			// logger.info("getBufferedImage() b.filePath="+b.filePath+"
			// b.fileName="+b.fileName);
		}
		NodeController.saveJson(this.file, this.document);
	}

	public void setVisible(boolean visible) {
		logger.info("setVisible(" + visible + ")");
		this.visible = visible;
		if (!visible) {
//			if (this.loop) {
//				if (recognition != null)
//					recognition.start();
//			} else {
//				if (recognition != null && recognition.getRun())
//					recognition.destroy();
//			}
		}
	}

	public void exportDocument(File destination) {
		NodeController.saveJson(destination, this.document);
	}

	public void importDocument(File file) {
		if (file.exists()) {
			// not functional
			this.document = (Document) NodeController.openJson(file, Document.class);
		}
		document.cortex.load();
	}

	public Dimension getDimension() {
		Dimension dimension = new Dimension(0, 0);
		if (this.retina != null) {
			dimension.setSize(this.retina.getObjectWidth(), this.retina.getObjectHeight());
		}
		return dimension;
	}

	@JsonIgnore
	public boolean setIndex(String uuid) {
		logger.info("setIndex(" + uuid + ")");
		boolean flag = false;
		for (int i = 0; i < this.inputList.size(); i++) {
			Input input = this.inputList.get(i);
			if (input.uuid.equals(uuid)) {
				flag = this.setIndex(i);
				break;
			}
		}
		return flag;
	}

	@JsonIgnore
	public boolean setIndex(int index) {
		logger.info("setIndex(" + index + ")");
		boolean flag = false;
		if (index >= 0 && index < this.inputList.size()) {
			this.index = index;
			flag = true;
		}
		return flag;
	}

	@JsonIgnore
	public boolean setIndex(boolean flag) {
		for (Input i : this.inputList) {
			if (i.scan == flag) {
				return this.setIndex(i.uuid);
			}
		}
		return false;
	}

	@JsonIgnore
	public Input getInput() {
		int size = this.inputList.size();
		Input page = (this.index < size && size > 0) ? this.inputList.get(this.index) : null;
		return page;
	}

	@JsonIgnore
	public Input getInput(int index) {
		int size = this.inputList.size();
		Input page = (index < size && size > 0) ? this.inputList.get(index) : null;
		return page;
	}

	// The model gives the image which has priority,
	// if page has no shapes, then the whole page is loaded;
	// if there are shapes then the currently selected shape is visualized.
	public BufferedImage getBufferedImage() {
		Input input = this.getInput();
		BufferedImage bufferedImage = (input != null) ? input.getBufferedImage() : null;
		return bufferedImage;
	}

	public Input getCurrentInput() {
		Input input = null;
		if (this.model != null && this.model.document != null) {
			Page page = this.model.document.getPage();
			input = (page != null)?this.document.inputMap.get(page.uuid):null;
			if (input == null) {
				input = new Input();
				input.scan = true;
				input.uuid = (page != null)?page.uuid:null;
				input.bufferedImage = (page != null)?page.getBufferedImage(model):null;
				input.concept = new Concept();
				inputList.add(input);// we want it in list if it has not been scanned;
			} else {
				input.scan = false;
				input.bufferedImage = page.getBufferedImage(model);
				
			}
		}
//		logger.info("getInputList() inputList.size()=" + inputList.size());
		return input;
	}

	public List<Input> getInputList() {
		List<Input> inputList = new ArrayList<>();
		if (this.model != null && this.model.document != null) {
			List<Page> pageList = this.model.document.pageList;
			for (Page page : pageList) {
				List<Shape> shapeList = page.getGridShapeList();
				for (Shape shape : shapeList) {
					Input input = this.document.inputMap.get(shape.uuid);
					if (input == null) {
						input = new Input();
						input.uuid = shape.uuid;
						input.bufferedImage = this.model.document
								.getShapeBufferedImage(page.getScaledBufferedImage(this.model), shape);
						String value = shape.data.text.value;
						Concept concept = (value != null) ? new Concept(value) : new Concept();
						input.concept = concept;
						inputList.add(input);
					} else {
						input.scan = false;
						String value = shape.data.text.value;
						Concept concept = (value != null) ? new Concept(value) : new Concept();
						input.concept = concept;
						input.bufferedImage = this.model.document
								.getShapeBufferedImage(page.getScaledBufferedImage(this.model), shape);
						inputList.add(input);
					}
				}
			}
		}
//		logger.info("getInputList() inputList.size()=" + inputList.size());
		return inputList;
	}

	public void setInputList(List<Input> inputList) {
//		logger.info("setInputList(" + inputList.size() + ")");
		this.inputList = inputList;
	}

	public static String getMeritokiHome() {
		return NodeController.getProviderHome() + NodeController.getSeperator() + "meritoki";
	}

	public static String getCortexHome(String uuid) {
		return getMeritokiHome() + NodeController.getSeperator() + "document" + NodeController.getSeperator() + uuid;
	}

	public static String getBeliefHome() {
		return getMeritokiHome() + NodeController.getSeperator() + "belief";
	}

	public static String getInputHome() {
		return getMeritokiHome() + NodeController.getSeperator() + "input";
	}

	// Need fucntion that returns list of inference candidates, these are shapes
	// without text;

}

//List<Input> inputList = new ArrayList<>();
//inputList.addAll(document.getInputList());
//inputList.addAll(this.getInputList());
//this.setInputList(inputList);

//public void paint(Graphics graphics) {
//Graphics2D graphics2D = null;
//if(graphics != null) {
//	graphics2D = (Graphics2D) graphics.create();
//}
//if (flag) {
//	if (pointStack.size() > 0) {
//		Point point = this.pointStack.pop();
//		logger.info(point);
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
//				logger.info("node point" + p);
//				pointStack.push(p);
//			}
////			random = min + (int)(Math.random() * ((max - min) + 1));
////			if(random == value) {
////				Point p = (Point) n;
////				logger.info("node point" + p);
////				pointStack.push(p);
////			}
//		}
//		logger.info("this.pointStack.size()=" + this.pointStack.size());
//
//	} else {
//		this.retina.setBufferedImage(this.getBufferedImage());
//		this.retina.setCortex(this.document.cortex);
//		logger.info("this.retina.getMagnification()=" + this.retina.getMagnification());
//		if (this.distance == 0) {
//			logger.info("this.distance == 0");
//			this.retina.maxDistance = this.retina.getMaxDistance();
//			this.distance = this.retina.maxDistance;
//			this.size = (int) (this.retina.getMaxDistance() - this.retina.focalLength);
//			logger.info("size=" + size);
//			this.index = 0;
//			this.retina.setDistance(this.distance);
//			this.retina.input(graphics2D);
////			this.pointStack = this.retina.getPointList();
//			logger.info("this.pointStack.size()=" + this.pointStack.size());
//		} else {
//			this.interval = this.size / 12;
//			if ((index * this.interval) < this.size) {
//				logger.info("index=" + index);
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
//				logger.info("this.pointStack.size()=" + this.pointStack.size());
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

//logger.info(concept);
//if (this.retina.cortex != null) {
//	Belief belief = this.retina.cortex.getBelief();
//	if (belief != null) {
//		List<Concept> conceptList = belief.getConceptList();
//		logger.info(conceptList);
//		if (conceptList != null && conceptList.size() > 0) {
//			concept = conceptList.get(0);
//		}
//	}
//}

//logger.info("B concept="+concept);
//Belief belief = this.retina.cortex.getBelief();
//logger.info(belief);
//if (belief != null) {
//	List<Concept> conceptList = belief.getConceptList();
//	logger.info(conceptList);
//	if (conceptList != null && conceptList.size() > 1) {
//		concept = conceptList.get(conceptList.size());
//	}
//}

//for (Input b : this.document.getInputList()) {
//if (b.file == null) {
//	File directory = new File(getInputHome() + NodeController.getSeperator());
//	if (!directory.exists()) {
//		directory.mkdirs();
//	}
//	b.file = new File(directory + NodeController.getSeperator() + b.uuid + ".jpg");
//	if (!b.file.exists()) {
//		try {
//			NodeController.saveJpg(b.file, b.bufferedImage);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//}
//b.filePath = b.file.getParent();
//b.fileName = b.file.getName();
//// logger.info("getBufferedImage() b.filePath="+b.filePath+"
//// b.fileName="+b.fileName);
//}

// Need function that returns a list of training candidates, they do not
// necessarily require a concept
// Every time this function is called a clean list is returned with a Page
// followed by all of its Shapes
// This is easy to visualize Pages or Shapes can be chosen using the Dialogs
//public void setInputMap() {
//	if (this.model != null && this.model.document != null) {
//		List<Page> pageList = this.model.document.pageList;
//		for (Page page : pageList) {
//			Input input = this.document.inputMap.get(page.uuid);
//			if(input == null) {
//				input = new Input();
//				input.uuid = page.uuid;
//				input.bufferedImage = page.getBufferedImage(model);
//				input.concept = new Concept();
//				this.document.inputMap.put(input.uuid,input);
//			} 
//			List<Shape> shapeList = page.getGridShapeList();
//			for (Shape shape : shapeList) {
//				input = this.document.inputMap.get(shape.uuid);
//				if(input == null) {
//					input = new Input();
//					input.uuid = shape.uuid;
//					input.bufferedImage = this.model.document
//							.getShapeBufferedImage(page.getScaledBufferedImage(this.model), shape);
//					input.concept = new Concept(shape.data.text.value);
//					this.document.inputMap.put(input.uuid,input);
//				}
//			}
//		}
//	}
//}

//if (concept != null && concept.value.isEmpty()) {
//	logger.info("A concept=" + concept);
//	concept = new Concept();
//}
//if (!this.loop) {
//	concept = this.model.cache.concept;
//	if (concept != null && concept.value.isEmpty()) {
//		logger.info("A concept=" + concept);
//		concept = new Concept();
//	}
//} else {
//	concept = new Concept();
//}

//Concept concept = null;
//if (!this.loop) {
//	concept = this.model.cache.concept;
//	if (concept != null && concept.value.isEmpty()) {
//		logger.info("A concept=" + concept);
//		concept = new Concept();
//	}
//} else {
//	concept = new Concept();
//}
//concept = (this.model.cache.concept == null)?:this.model.cache.concept;
//concept = (concept.value.isEmpty())?new Concept():concept;

//concept = (this.model.cache.concept != null)?this.model.cache.concept:concept;
//concept = (concept.value.isEmpty())?new Concept():concept;
