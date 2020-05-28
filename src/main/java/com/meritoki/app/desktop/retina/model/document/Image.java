package com.meritoki.app.desktop.retina.model.document;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import com.meritoki.app.desktop.retina.controller.node.NodeController;

/**
 * Class is used to manage reference to file in filesystem
 * 
 * @author jorodriguez
 *
 */
public class Image {
	@JsonIgnore
	static Logger logger = LogManager.getLogger(Image.class.getName());
	@JsonProperty
	public String uuid;
	@JsonIgnore 
	public File file;
	@JsonProperty
	public String filePath;
	@JsonProperty
	public String fileName;
	@JsonProperty
	public List<Shape> shapeList = new ArrayList<>();
	@JsonProperty
	public Position position = new Position();
	@JsonIgnore
	public BufferedImage bufferedImage;
	@JsonIgnore
	public int index = 0;
	@JsonProperty
	public double scale = 1;

	/**
	 * Default constructor
	 */
	public Image() {
		if(this.file == null) {
			this.file = new File(this.filePath+getSeperator()+this.fileName);
		}
//		this.initBufferedImage();
	}
	
	public static String getSeperator() {
		return FileSystems.getDefault().getSeparator();
	}

	/**
	 * Copy constructor
	 * 
	 * @param image
	 */
	public Image(Image image) {
		this.uuid = image.uuid;
		this.file = image.file;
		this.filePath = image.filePath;
		this.fileName = image.fileName;
		this.bufferedImage = image.getBufferedImage();
		this.position = image.position;
		this.index = image.index;
		for (Shape shape : image.shapeList) {
			this.shapeList.add(new Shape(shape,true));
		}
	}

	public Image(File file) {
		this.uuid = UUID.randomUUID().toString();
		this.file = file;
		this.filePath = this.file.getParent();
		this.fileName = this.file.getName();
		this.initBufferedImage();
	}

	@JsonIgnore
	public String getUUID() {
		return this.uuid;
	}

	@JsonIgnore
	public String getExtension() {
		return this.fileName.substring(this.fileName.lastIndexOf(".") + 1);
	}

	/**
	 * Function returns true if files have the same uuid
	 * 
	 * @param file
	 * @return flag
	 */
	@JsonIgnore
	public boolean equals(Image file) {
		boolean flag = false;
		if (this.uuid.equals(file.uuid)) {
			flag = true;
		}
		return flag;
	}

	/**
	 * Function gets the current index selected by user.
	 * 
	 * @return
	 */
	@JsonIgnore
	public int getIndex() {
		logger.debug("getIndex() this.index=" + this.index);
		return this.index;
	}

	/**
	 * Functions gets shape for index.
	 * 
	 * @return
	 */
	@JsonIgnore
	public Shape getShape() {
		Shape shape = null;
		if (this.index >= 0 && this.index < this.shapeList.size()) {
			shape = this.shapeList.get(this.index);
		}
		return shape;
	}

	/**
	 * DIMENSION 1
	 * 
	 * @param point
	 * @return
	 */
	@JsonIgnore
	public Shape getShape(Point point) {
		for (Shape shape : this.shapeList) {
			if (shape.position.containsPoint((point))) {
				logger.info("getShape(" + point + ") shape=" + shape);
				return shape;
			}
		}
		return null;
	}

	/**
	 * Function transforms the
	 * 
	 * @return
	 */
	@JsonIgnore
	public List<Shape> getShapeList() {
		return this.shapeList;
	}

	@JsonIgnore
	public BufferedImage getBufferedImage() {
		if(this.bufferedImage == null) {
			this.initBufferedImage();
			BufferedImage before = this.bufferedImage;
			int w = before.getWidth();
			int h = before.getHeight();
			BufferedImage after = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
			AffineTransform at = new AffineTransform();
			logger.info("getBufferedImage() this.scale="+this.scale);
			at.scale(this.scale, this.scale);
			AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
			after = scaleOp.filter(before, after);
			this.bufferedImage = after;
		}
		return this.bufferedImage;
	}

	@JsonIgnore
	public void initBufferedImage() {
		this.bufferedImage = NodeController.openBufferedImage(NodeController.getImageCache(),
				this.uuid + "." + this.getExtension());
		if (bufferedImage == null) {
			if (this.file != null) {
				bufferedImage = NodeController.openBufferedImage(this.file);
				if (bufferedImage != null) {
					this.setBufferedImage(bufferedImage);
					if (this.getExtension().equals("jpg") || this.getExtension().equals("jpeg")) {
						NodeController.saveJpg(NodeController.getImageCache(), this.uuid + "." + this.getExtension(),
								bufferedImage);
					}
				}
			}
		}

		if (this.bufferedImage != null) {
			this.position.absoluteDimension.width = this.bufferedImage.getWidth();
			this.position.absoluteDimension.height = this.bufferedImage.getHeight();
		}
	}

	/**
	 * Function sets the current index selected by user.
	 * 
	 * @param index
	 */
	@JsonIgnore
	public void setIndex(int index) {
		if (index >= 0 && index < this.shapeList.size()) {
			this.index = index;
		}
	}

	@JsonIgnore
	public void setScale(double scale) {
		logger.info("setScale("+scale+")");
		this.position.setScale(scale);
		for (Shape shape : this.shapeList) {
			shape.setScale(scale);
		}
	}

	@JsonIgnore
	public void setBufferedImage(BufferedImage bufferedImage) {
		this.bufferedImage = bufferedImage;
		this.position.absoluteDimension.width = this.bufferedImage.getWidth();
		this.position.absoluteDimension.height = this.bufferedImage.getHeight();
	}

	@JsonIgnore
	public void setOffset(double offset) {
		this.position.offset = offset;
		for (Shape shape : this.shapeList) {
			shape.position.setOffset(offset);
		}
	}

	/**
	 * Function necessary for a shape to move at all with the margin shift
	 * 
	 * @param margin
	 */
	@JsonIgnore
	public void setMargin(double margin) {
		logger.info("setMargin(" + margin + ")");
		this.position.margin = margin;
		for (Shape shape : this.shapeList) {
			shape.setMargin(margin);
		}
	}

	/**
	 * Functions sets current index by uuid.
	 * 
	 * @param uuid
	 */
	@JsonIgnore
	public boolean setShape(String uuid) {
		logger.debug("setShape(" + uuid + ")");
		boolean flag = false;
		Shape shape = null;
		for (int i = 0; i < this.shapeList.size(); i++) {
			shape = this.shapeList.get(i);
			if (shape.uuid.equals(uuid)) {
				this.index = i;
				flag = true;
				break;
			}
		}
		return flag;
	}

	/**
	 * DIMENSION 3 B Functions adds shape to shapeList. The only place a shape's
	 * point list is modified to fit within the File
	 * 
	 * @param shape
	 */
	@JsonIgnore
	public void addShape(Shape shape) {
		logger.info("addShape(" + shape + ")");
		this.shapeList.add(shape);
	}

	/**
	 * Need To Refactor
	 * 
	 * @param point
	 * @return
	 */
	@JsonIgnore
	public Selection intersectShape(Point point) {
		Selection selection = null;
		Shape shape = this.getShape();
		if (shape != null) {
			selection = shape.position.selectionPoint(point);
		}
		return selection;
	}

	@JsonIgnore
	public boolean containsShape(Shape shape) {
		return this.containsPoint(shape.position.getStartPoint());
	}

	@JsonIgnore
	public Shape removeShape(Shape shape) {
		return this.removeShape(shape.uuid);
	}

	@JsonIgnore
	public Shape removeShape(String uuid) {
		ListIterator<Shape> shapeListIterator = this.shapeList.listIterator();
		while (shapeListIterator.hasNext()) {
			Shape shape = shapeListIterator.next();
			if (shape.uuid.equals(uuid)) {
				shapeListIterator.remove();
				return shape;
			}
		}
		return null;
	}
	
	@JsonIgnore
	public List<Shape> removeAllShapes() {
		List<Shape> shapeList = this.shapeList;
		this.shapeList = new ArrayList<>();
		return shapeList;
	}

	/**
	 * Function returns true if point is contained within file
	 * 
	 * @param point
	 * @return
	 */
	@JsonIgnore
	public boolean containsPoint(Point point) {
		return this.position.containsPoint(point);
	}

	@JsonIgnore
	@Override
	public String toString() {
		String string = "";
		ObjectWriter ow = new ObjectMapper().writer();
		try {
			string = ow.writeValueAsString(this);
		} catch (IOException e) {
			logger.error("IOException " + e.getMessage());
		}

		return string;
	}
}

/**
 * DIMENSION 4 Function removes Shape from shapeList using uuid.
 * 
 * @param shape
 */
//@JsonIgnore
//public Shape removeShape(String uuid) {
//	Shape s = null;
//	for (int i = 0; i < this.shapeList.size(); i++) {
//		s = this.shapeList.get(i);
//		if (s.uuid.equals(uuid)) {
//			this.shapeList.remove(i);
//			break;
//		} else {
//			s = null;
//		}
//	}
//	return s;
//}