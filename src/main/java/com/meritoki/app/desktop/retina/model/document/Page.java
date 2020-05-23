/*
 * Copyright 2019 Joaquin Osvaldo Rodriguez
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

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.meritoki.app.desktop.retina.controller.node.NodeController;

/**
 * The Page class is used to hold a list of shapes. The list of shapes can be
 * loaded from a Layout.
 *
 *
 * Displacement of shapeLists is maitained in the Page object. This is simply a
 * mechanism that converts the points in a Shape to the coordinate system of the
 * page. The page is the size of all the Files loaded into a bufferedImage and
 * displayed.
 *
 * @author jorodriguez
 *
 */
public class Page {

	/**
	 * Logger for Page class.
	 */
	@JsonIgnore
	static Logger logger = LogManager.getLogger(Page.class.getName());

	/**
	 * Identifier for Page class
	 */
	@JsonProperty
	public String uuid;
	/**
	 * List of Images loaded by user.
	 */
	@JsonProperty
	public List<Image> imageList = new ArrayList<Image>();
	/**
	 * Current file index selected by user. Default is zero for a fileList of size
	 * 1.
	 */
	@JsonIgnore
	public int index = 0;
	/**
	 * Cached copy of joined bufferedImage from one or more files.
	 */
	@JsonIgnore
	public BufferedImage bufferedImage = null;
	/**
	 * Scale of the entire page, applied to all files.
	 */
	@JsonProperty
	public Dimension dimension = new Dimension();

	@JsonProperty
	public Script script;

	/**
	 * Page class retains references to one or more files
	 */
	public Page() {
		this.uuid = UUID.randomUUID().toString();
	}

	/**
	 * Copy constructor INCOMPLETE AND CAUSING A BUG
	 *
	 * @param page
	 */
	public Page(Page page) {
		this.uuid = page.uuid;
		for (Image file : page.imageList) {
			this.imageList.add(new Image(file));
		}
		this.index = page.index;
		this.dimension = page.dimension;
	}

	/**
	 * Function gets the current index selected by user.
	 *
	 * @return this.index
	 */
	@JsonIgnore
	public int getIndex() {
		return this.index;
	}

	/**
	 * Function returns file using index
	 *
	 * @return file
	 */
	@JsonIgnore
	public Image getImage() {
		Image image = null;
		List<Image> imageList = this.getImageList();
		if (this.index >= 0 && this.index < imageList.size()) {
			image = imageList.get(this.index);
		}
		return image;
	}

	/**
	 * Function returns file that contains the point parameter
	 *
	 * @param Point
	 * @return File
	 */
	@JsonIgnore
	public Image getImage(Point point) {
		Image image = null;
		for (Image i : this.getImageList()) {
			image = i;
			if (image.containsPoint(point)) {
				break;
			} else {
				image = null;
			}
		}
		if (image != null) {
			logger.info("getImage(" + point + ") image=" + image);
		}
		return image;
	}

	/**
	 * Function returns shape that contains the point parameter
	 *
	 * @param point
	 * @return
	 */
	@JsonIgnore
	public Shape getShape(Point point) {
		Shape s = null;
		for (Image file : this.getImageList()) {
			s = file.getShape(point);
			if (s != null) {
				break;
			}
		}
		return s;
	}

	/**
	 * Function returns the Shape at the current index in File
	 *
	 * @return shape
	 */
	@JsonIgnore
	public Shape getShape() {
		Image file = this.getImage();
		Shape shape = (file != null) ? file.getShape() : null;
		return shape;
	}

	/**
	 * DIMENSION 1 C Converts the dimension of Shape back to the global coordinates.
	 * This method converts the shape back to the coordinates of the page.
	 *
	 * @return
	 */
	@JsonIgnore
	public List<Shape> getShapeList() {
		List<Shape> shapeList = new ArrayList<>();
		for (Image image : this.getImageList()) {
			for (Shape shape : image.getShapeList()) {
//				shape.dimension.setScale(image.dimension.scale);
//				shape.dimension.setOffset(image.dimension.offset);
//				shape.dimension.setMargin(image.dimension.margin);
//				shape.setDimension(null);
//				dimension = shape.getDimension();
//				dimension.x += (image.getOffset() * image.scale);
//				dimension.y += (image.getMargin() * image.scale);
				shape.bufferedImage = this.getShapeBufferedImage(shape);
				shapeList.add(shape);
			}
		}
		return shapeList;
	}

	public BufferedImage getShapeBufferedImage(Shape shape) {
		BufferedImage bufferedImage = null;
		if (this.getBufferedImage() != null) {
			int x = (int) (shape.dimension.x);// / this.scale);
			int y = (int) (shape.dimension.y);// / this.scale);
			int w = (int) (shape.dimension.width);// / this.scale);
			int h = (int) (shape.dimension.height);// / this.scale);
//			bufferedImage = this.getBufferedImage().getSubimage(x, y, w, h);
		}
		return bufferedImage;
	}

	public Matrix getMatrix() {
		return new Matrix(this.getShapeList(), this.script);
	}
	
	public Table getTable() {
		return new Table(this.getMatrix());
	}
	
	public Archive getArchive() {
		return new Archive(this.getMatrix());
	}

	/**
	 * DIMENSION 2 A Function is the only place where the File objects receive the
	 * metadata necessary to correctly process shapes.
	 *
	 * @return
	 */
	@JsonIgnore
	public List<Image> getImageList() {
		double offset = 0;
		for (Image image : this.imageList) {
			if (image.getBufferedImage() == null) {
				BufferedImage bufferedImage = NodeController.openBufferedImage(NodeController.getImageCache(),
						image.uuid + "." + image.getExtension());
				if (bufferedImage == null) {
					bufferedImage = NodeController.openBufferedImage(image.file.getParent(), image.file.getName());
					if (bufferedImage != null) {
						image.setBufferedImage(bufferedImage);
						if (image.getExtension().equals("jpg") || image.getExtension().equals("jpeg")) {
							NodeController.saveJpg(NodeController.getImageCache(),
									image.uuid + "." + image.getExtension(), bufferedImage);
						}
					}

				} else {
					image.setBufferedImage(bufferedImage);
				}
			}
			image.setOffset(offset);
			image.setScale(this.dimension.scale);
			this.dimension.width += image.dimension.width;
			offset += image.dimension.width;
		}
		return this.imageList;
	}

//	/**
//	 * DIMENSION 2 A Function is the only place where the File objects receive the
//	 * metadata necessary to correctly process shapes.
//	 *
//	 * @return
//	 */
//	@JsonIgnore
//	public List<Image> getImageList() {
//		double offset = 0;
//		for (Image image : this.imageList) {
//			if (image.getBufferedImage() == null) {
//				BufferedImage bufferedImage = NodeController.openBufferedImage(NodeController.getImageCache(),
//						image.uuid + "." + image.extension);
//				if (bufferedImage == null) {
//					bufferedImage = NodeController.openBufferedImage(image.getPath(), image.getNameAndExtension());
//					if (bufferedImage != null) {
//						image.setBufferedImage(bufferedImage);
//						if (image.extension.equals("jpg") || image.extension.equals("jpeg")) {
//							NodeController.saveJpg(NodeController.getImageCache(), image.uuid + "." + image.extension,
//									bufferedImage);
//						}
//						// TODO Add support for PNG
////						if (ClientController.fileClient.checkHealth()) {
////							ClientController.fileClient.registerFile(image.uuid);
////							if (ClientController.fileClient.checkFile(image.uuid)) {
////								ClientController.fileClient.uploadFile(
////										NodeController.getImageCache() + NodeController.getSeperator(),
////										image.uuid + "." + image.extension);
////							}
////						}
//					} else {
////						if (ClientController.fileClient.checkHealth()) {
////							if (ClientController.fileClient.checkFile(image.uuid)) {
////								ClientController.fileClient.downloadFile(
////										NodeController.getImageCache() + NodeController.getSeperator(),
////										image.getUUID() + "." + image.getExtension());
////								ClientController.fileClient.unmarkFile(image.uuid);
////							} else {
////								ClientController.fileClient.markFile(image.uuid);
////							}
////						}
//					}
//				} else {
//					image.setBufferedImage(bufferedImage);
////					if (ClientController.fileClient.checkHealth()) {
////						ClientController.fileClient.registerFile(image.uuid);
////						if (ClientController.fileClient.checkFile(image.uuid)) {
////							ClientController.fileClient.uploadFile(
////									NodeController.getImageCache() + NodeController.getSeperator(),
////									image.uuid + "." + image.extension);
////						}
////					}
//				}
//			}
//			image.setOffset(offset);
//			image.setScale(this.scale);
//			offset += image.dimension.offset;
//		}
//		return this.imageList;
//	}

	/**
	 * Function returns bufferedImage with one or more File bufferedImages from the
	 * fileList
	 *
	 * @return this.bufferedImage;
	 */
	@JsonIgnore
	public BufferedImage getBufferedImage() {
		if (this.bufferedImage == null) {
			this.bufferedImage = this.joinImages(this.getImageList());
			this.dimension.w = this.bufferedImage.getWidth();
			this.dimension.h = this.bufferedImage.getHeight();
		}
		return this.bufferedImage;
	}

	@JsonIgnore
	public void setBufferedImage(BufferedImage bufferedImage) {
		this.bufferedImage = bufferedImage;
	}

	/**
	 * Function sets the current index selected by user.
	 *
	 * @param index
	 */
	@JsonIgnore
	public void setIndex(int index) {
		if (index >= 0 && index < this.imageList.size()) {
			this.index = index;
		}
	}

	/**
	 * Function sets scale for page and all files in fileList
	 *
	 * @param scale
	 */
	@JsonIgnore
	public void setScale(double scale) {
		this.dimension.scale = scale;
		for (Image image : this.imageList) {
			image.setScale(scale);
		}
	}

	/**
	 * Function sets current shape and file using uuid
	 *
	 * @param uuid
	 */
	@JsonIgnore
	public void setShape(String uuid) {
		for (Image file : this.getImageList()) {
			if (file.setShape(uuid)) {
				this.setImage(file.uuid);
				break;
			}
		}
	}

	@JsonIgnore
	public void setImage(String uuid) {
		logger.info("setImage(" + uuid + ")");
		Image file = null;
		List<Image> fileList = this.getImageList();
		for (int i = 0; i < fileList.size(); i++) {
			file = fileList.get(i);
			if (file.uuid.equals(uuid)) {
				this.setIndex(i);
				break;
			}
		}
	}

	/**
	 * Shape has global coordinates
	 *
	 * @param shape
	 */
	@JsonIgnore
	public void addShape(Shape shape) {
		for (Image image : this.getImageList()) {
			if (image.containsShape(shape)) {
				image.addShape(shape);
				this.setShape(shape.uuid);
				break;
			}
		}
	}

	@JsonIgnore
	public void addImage(Image image) {
		logger.info("addImage(" + image + ")");
		image.setScale(this.dimension.scale);
		this.imageList.add(image);
	}

	public Shape removeShape(Shape shape) {
		logger.info("removeShape(" + shape + ")");
		Shape s = null;
		for (Image file : this.getImageList()) {
			s = file.removeShape(shape.uuid);
			if (s != null) {
				break;
			}
		}
		return s;
	}

	@JsonIgnore
	public Selection intersectShape(Point point) {
		logger.trace("intersectShape(" + point + ")");
		Selection selection = null;
		Image image = this.getImage();
		if (image != null) {
			selection = image.intersectShape(point);
		}
		return selection;
	}

//
//	@JsonIgnore
//	public double getFileListMinMargin() {
//		double min = 65536;
//		for (Image image : this.getImageList()) {
//			if (image.dimension.margin < min) {
//				min = image.dimension.margin;
//			}
//		}
//		return min;
//	}
//
//	@JsonIgnore
//	public double getFileListMaxMargin() {
//		double max = -65536;
//		for (Image image : this.getImageList()) {
//			if (image.dimension.margin > max) {
//				max = image.dimension.margin;
//			}
//		}
//		return max;
//	}

//	@JsonIgnore
//	public BufferedImage modifyImage(Image image) { // BufferedImage img1,BufferedImage img2) {
//		logger.debug("modifyImage(" + image + ")");
//		BufferedImage bufferedImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
//		if (image.bufferedImage != null) {
//			int width = image.bufferedImage.getWidth();
//			int height = image.bufferedImage.getHeight();
//			bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
//			Graphics2D graphics2D = bufferedImage.createGraphics();
//			Color oldColor = graphics2D.getColor();
//			graphics2D.setPaint(Color.BLACK);
//			graphics2D.fillRect(0, 0, width, height);
//			graphics2D.setColor(oldColor);
//			graphics2D.drawImage(image.bufferedImage, null, 0, (int) image.dimension.margin);
//			graphics2D.dispose();
//		}
//		return bufferedImage;
//	}

	@JsonIgnore
	public BufferedImage joinImages(List<Image> imageList) {
		logger.debug("joinImages(" + imageList + ")");
		BufferedImage bufferedImage = null;
		for (int i = 0; i < imageList.size(); i++) {
			Image a = imageList.get(i);
			if (bufferedImage == null) {
				int width = a.bufferedImage.getWidth();
				int height = (int) (a.bufferedImage.getHeight() + a.dimension.margin);
				bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
				Graphics2D graphics2D = bufferedImage.createGraphics();
				graphics2D.setPaint(Color.BLACK);
				graphics2D.fillRect(0, 0, width, height);
				graphics2D.setColor(Color.BLACK);
				graphics2D.drawImage(a.bufferedImage, null, 0, (int)(a.dimension.margin));
				graphics2D.dispose();
			}
			if (i + 1 < imageList.size()) {
				Image b = imageList.get(i + 1);
				int w = bufferedImage.getWidth();
				int h = bufferedImage.getHeight();
				int width = w + b.bufferedImage.getWidth();
				int height = Math.max(h, b.bufferedImage.getHeight() + (int) b.dimension.margin);// +offset;
//				int height = b.bufferedImage.getHeight() + (int) b.dimension.margin;// +offset;
				BufferedImage bI = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
				Graphics2D graphics2D = bI.createGraphics();
				Color oldColor = graphics2D.getColor();
				graphics2D.setPaint(Color.BLACK);
				graphics2D.fillRect(0, 0, width, height);
				graphics2D.setColor(oldColor);
				graphics2D.drawImage(bufferedImage, null, 0, 0);
//				graphics2D.drawImage(b.bufferedImage, null, w, (int) b.dimension.margin);
				graphics2D.drawImage(b.bufferedImage, null, w, (int) (b.dimension.margin));
				graphics2D.dispose();
				bufferedImage = bI;
			}
		}
		return bufferedImage;
	}

	public void paint(Graphics2D graphics2D) {
		AffineTransform affineTransform = new AffineTransform();
		affineTransform.scale(this.dimension.scale, this.dimension.scale);
		BufferedImage bufferedImage = this.getBufferedImage();
		if (bufferedImage != null) {
			graphics2D.drawImage(bufferedImage, affineTransform, null);
		}

		List<Image> imageList = this.getImageList();
		Image image = this.getImage();
		Dimension d = null;
		if (imageList != null) {
			for (Image i : imageList) {
				d = i.dimension;
				d.scale();
				if (image != null && i.uuid.equals(image.uuid)) {
					graphics2D.setColor(Color.RED);
				} else {
					graphics2D.setColor(Color.YELLOW);
				}
				Rectangle2D.Double rectangle = new Rectangle2D.Double(d.x, d.y, d.width, d.height);
				graphics2D.draw(rectangle);
			}

		}
		List<Shape> shapeList = this.getMatrix().getShapeList();
		Shape shape = this.getShape();
		Shape previousShape = null;
		if (shapeList != null) {
			for (Shape s : shapeList) {
				d = s.dimension;
				d.scale();
				if (shape != null && s.uuid.equals(shape.uuid)) {
					graphics2D.setColor(Color.RED);
				} else {
					graphics2D.setColor(Color.BLUE);
				}
				switch (s.type) {
				case ELLIPSE: {
					Ellipse2D.Double ellipse = new Ellipse2D.Double(d.x, d.y, d.width, d.height);
					graphics2D.draw(ellipse);
					break;
				}
				case RECTANGLE: {
					Rectangle2D.Double rectangle = new Rectangle2D.Double(d.x, d.y, d.width, d.height);
					graphics2D.draw(rectangle);
					break;
				}
				}
				if (previousShape != null) {
					Dimension dimension = previousShape.dimension;
					graphics2D.drawLine((int) (dimension.x + (dimension.width / 2)),
							(int) (dimension.y + (dimension.height / 2)), (int) (d.x + (d.width / 2)),
							(int) (d.y + (d.height / 2)));
				}
				previousShape = s;
			}
		}
		this.dimension.scale();
		graphics2D.setColor(Color.BLUE);
		Rectangle2D.Double frame = new Rectangle2D.Double(0, 0, this.dimension.width, this.dimension.height);
		graphics2D.draw(frame);

	}
	
	@JsonIgnore
	@Override
	public String toString() {
		String string = "";
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		try {
			string = ow.writeValueAsString(this);
		} catch (IOException ex) {
			logger.error("IOException " + ex.getMessage());
		}

		return string;
	}
}
