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
package com.meritoki.app.desktop.retina.model.document;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.meritoki.app.desktop.retina.controller.node.NodeController;
import com.meritoki.app.desktop.retina.model.document.command.Pattern;
import com.meritoki.app.desktop.retina.model.provider.meritoki.Output;

/**
 * Document
 *
 * @author jorodriguez
 *
 */
public class Document {
	@JsonIgnore
	static Logger logger = LogManager.getLogger(Document.class.getName());
	@JsonProperty
	public String uuid;
	@JsonIgnore
	public int index = 0;
	@JsonProperty
	public List<Page> pageList = new ArrayList<>();
	@JsonProperty
	public Pattern pattern = new Pattern();
	@JsonIgnore
	public Cache cache = new Cache();

	public Document() {
		this.uuid = UUID.randomUUID().toString();
		this.init();
		new File(NodeController.getDocumentCache(this.uuid)).mkdirs();
//		this.test();
	}

	public void save() {
		this.pattern.save();
	}

	public void init() {
		this.pattern.setDocument(this);
	}

	@JsonIgnore
	public void test() {
		Page page = new Page();
		page.addImage(new Image(new File("./data/image/01.jpg")));
		page.addImage(new Image(new File("./data/image/02.jpg")));
		page.addImage(new Image(new File("./data/image/03.jpg")));
		this.addPage(page);
		page = new Page(new Image(new File("./data/image/03.jpg")));
		this.addPage(page);
		page = new Page(new Image(new File("./data/image/04.jpg")));
		this.addPage(page);
	}

	@JsonIgnore
	public Image getImage() {
		Image image = null;
		if (this.getPage() != null) {
			image = this.getPage().getImage();
		}
		logger.info("getImage() image=" + image);
		return image;
	}

	@JsonIgnore
	public Image getImage(Point point) {
		Image image = null;
		if (this.getPage() != null) {
			image = this.getPage().getImage(point);
		}
		logger.info("getImage(" + point + ") image="+image);
		return image;
	}

	@JsonIgnore
	public void setImage(String uuid) {
		logger.info("setImage(" + uuid + ")");
		if (this.getPage() != null) {
			this.getPage().setImage(uuid);
		}
	}

	@JsonIgnore
	public void setImage(int index) {
		logger.debug("setImage(" + index + ")");
		if (this.getPage() != null) {
			this.getPage().setIndex(index);
		}
	}

	@JsonIgnore
	public Shape getShape(Point point) {
		Shape shape = null;
		if (this.getPage() != null) {
			shape = this.getPage().getShape(point);
		}
		logger.info("getShape(" + point + ") shape=" + shape);
		return shape;
	}

	@JsonIgnore
	public void addShape(Shape shape) {
		logger.info("addShape(" + shape + ")");
		Page page = this.getPage();
		if (page != null) {
			page.addShape(shape);
		}
	}

	/**
	 * Get the index of the current Page, used by Dialogs
	 * 
	 * @return
	 */
	@JsonIgnore
	public int getIndex() {
		return this.index;
	}

	@JsonIgnore
	public int getIndex(String uuid) {
		int index = 0;
		Page page;
		for (int i = 0; i < this.pageList.size(); i++) {
			page = this.pageList.get(i);
			if (page.uuid.equals(uuid)) {
				index = i;
				break;
			}
		}
		return index;
	}

	/**
	 * Functions gets Page object at current index from Page List
	 * 
	 * @return Page
	 */
	@JsonIgnore
	public Page getPage() {
		int size = this.pageList.size();
		Page page = (this.index < size && size > 0) ? this.pageList.get(this.index) : null;
		this.setBufferedImage(page);
		return page;
	}

	@JsonIgnore
	public Page getPage(int index) {
		int size = this.pageList.size();
		Page page = (index < size && size > 0) ? this.pageList.get(index) : null;
		logger.debug("getPage(" + index + ") page=" + page);
		return page;
	}

	/**
	 * Function get reference to Page List
	 * 
	 * @return List<Page>
	 */
	@JsonIgnore
	public List<Page> getPageList() {
		return this.pageList;
	}

	@JsonIgnore
	public boolean setIndex(int index) {
		boolean flag = false;
		if (index >= 0 && index < this.pageList.size()) {
			this.index = index;
			flag = true;
		}
		return flag;
	}

	@JsonIgnore
	public void setPage(String uuid) {
		logger.info("setPage(" + uuid + ")");
		if (uuid != null) {
			Page page = null;
			for (int i = 0; i < this.pageList.size(); i++) {
				page = this.pageList.get(i);
				if (page.uuid.equals(uuid)) {
					this.setIndex(i);
				} else {
					page.setBufferedImageNull();
				}
			}
		}
	}

	@JsonIgnore
	public void setPageList(List<Page> pageList) {
		logger.info("setPageList(" + pageList + ")");
		this.pageList = pageList;
	}

	@JsonIgnore
	public void addPage(Page page) {
		logger.info("addPage(" + page + ")");
		this.setBufferedImage(page);
		this.pageList.add(page);
	}

	@JsonIgnore
	public List<Shape> getShapeList() {
		List<Shape> shapeList = new ArrayList<>();
		for (Page page : this.pageList) {
			shapeList.addAll(page.getShapeList());
		}
		return shapeList;
	}

	@JsonIgnore
	public boolean importText(List<String[]> stringArrayList) {
		logger.info("importText(...)");
		boolean flag = false;
		for (int i = 0; i < stringArrayList.size(); i++) {
			String[] stringArray = stringArrayList.get(i);
			String value = null;
			for (String string : stringArray) {
				if (string.contains("value")) {
					value = string.split(":")[1].replace("\"", "");
				}
			}
			String uuid = null;
			for (String string : stringArray) {
				if (string.contains("the_image")) {
					uuid = string.split(":")[1].replace(".jpg", "").replace("}", "").replace("\"", "");
				}
			}
			System.out.println(value + " " + uuid);
			if (uuid != null && value != null) {
				for (Page page : this.getPageList()) {
					for (Shape shape : page.getShapeList()) {
						if (uuid.equals(shape.uuid)) {
							Text text = new Text();
							text.value = value;
							shape.addText(text);
							flag = true;
						}
					}
				}
			}
		}
		return flag;
	}

	@JsonIgnore
	public void importOutputList(List<Output> outputList) {
		for (Shape s : this.getShapeList()) {
			for (Output o : outputList) {
				if (s.uuid.equals(o.shape.uuid)) {
					s.textList.add(new Text(o.concept));
				}
			}
		}
	}

	@JsonIgnore
	public Page removePage(String uuid) {
		ListIterator<Page> pageListIterator = this.pageList.listIterator();
		while (pageListIterator.hasNext()) {
			Page page = pageListIterator.next();
			if (page.uuid.equals(uuid)) {
				pageListIterator.remove();
				return page;
			}
		}
		return null;
	}

	@JsonIgnore
	public void setBufferedImage(Page page) {
		if (page != null) {
			List<Image> imageList = page.getImageList();
			for (Image image : imageList) {
				this.setBufferedImage(image);
			}
			page.getBufferedImage();
		}
	}
	
	@JsonIgnore
	public void setBufferedImage(Image image) {
		if (image.getBufferedImage() == null)
			image.setBufferedImage(this.getBufferedImage(image));
	}

	@JsonIgnore
	public BufferedImage getBufferedImage(Image image) {
		BufferedImage bufferedImage = NodeController.openBufferedImage(NodeController.getDocumentCache(this.uuid), image.uuid + "." + image.getExtension());
		if (bufferedImage == null) {
			if (image.file != null) {
				bufferedImage = NodeController.openBufferedImage(image.file);
				if (bufferedImage != null) {
					if (image.getExtension().equals("jpg") || image.getExtension().equals("jpeg")) {
						NodeController.saveJpg(NodeController.getDocumentCache(this.uuid),
								image.uuid + "." + image.getExtension(), bufferedImage);
					}
				}
			}
		}
		BufferedImage before = bufferedImage;
		int w = before.getWidth();
		int h = before.getHeight();
		BufferedImage after = new BufferedImage((int) (w * image.position.relativeScale),
				(int) (h * image.position.relativeScale), BufferedImage.TYPE_INT_ARGB);
		AffineTransform at = new AffineTransform();
		at.scale(image.position.relativeScale, image.position.relativeScale);
		AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
		after = scaleOp.filter(before, after);
		bufferedImage = after;
		image.position.setAbsoluteDimension(new Dimension(bufferedImage.getWidth(), bufferedImage.getHeight()));
		return bufferedImage;
	}

//	@JsonIgnore
//	@Override
//	public String toString() {
//		String string = "";
//		ObjectWriter ow = new ObjectMapper().writer();//.withDefaultPrettyPrinter();
//		try {
//			string = ow.writeValueAsString(this);
//		} catch (IOException ex) {
//			logger.error("IOException " + ex.getMessage());
//		}
//		return string;
//	}
}
