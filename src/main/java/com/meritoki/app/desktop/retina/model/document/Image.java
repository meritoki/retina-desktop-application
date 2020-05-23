package com.meritoki.app.desktop.retina.model.document;

import java.awt.image.BufferedImage;
import java.io.File;
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
	public String uuid = null;
	@JsonProperty
	public File file = null;
	@JsonIgnore
	public BufferedImage bufferedImage = null;
	@JsonIgnore
	public Dimension dimension = new Dimension();
	/**
	 * Current index of the shapeList.
	 */
	@JsonIgnore
	public int index = 0;
	/**
	 * List of Shapes drawn by user.
	 */
	@JsonProperty
	public List<Shape> shapeList = new ArrayList<>();

	/**
	 * Default constructor
	 */
	public Image() {
		this.uuid = UUID.randomUUID().toString();
	}

	/**
	 * Copy constructor
	 * 
	 * @param image
	 */
	public Image(Image image) {
		this.uuid = image.uuid;
		this.file = image.file;
		this.bufferedImage = image.bufferedImage;
		this.dimension = image.dimension;
		this.index = image.index;
		for (Shape shape : image.shapeList) {
			this.shapeList.add(new Shape(shape));
		}
	}

	/**
	 * Constructor accepts a file path and name parameters
	 * 
	 * @param path
	 * @param name
	 */
//	public Image(String path, String name) {
//		this.uuid = UUID.randomUUID().toString();
//		this.path = path;
//		this.name = name;
//		this.extension = this.getExtension(this.name);
//		this.name = this.name.replace("." + this.extension, "");
//	}
	
	public Image(File file) {
		this.uuid = UUID.randomUUID().toString();
		this.file = file;
	}

	public String getUUID() {
		return this.uuid;
	}

	public String getExtension() {
		return file.getName().substring(file.getName().lastIndexOf(".") + 1);
	}

	/**
	 * Function returns true if files have the same uuid
	 * 
	 * @param file
	 * @return flag
	 */
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
	public Shape getShape(Point point) {
		Shape s = null;
		for (Shape shape : this.shapeList) {
			if (shape.dimension.containsPoint((point))) {
//			if (shape.dimension.containsPoint(this.getFilePoint(point))) {
				logger.info("getShape(" + point + ") shape" + shape);
				s = shape;
				break;
			}
		}
		return s;
	}

	/**
	 * Function transforms the
	 * 
	 * @return
	 */
	public List<Shape> getShapeList() {
		return this.shapeList;
	}

	@JsonIgnore
	public BufferedImage getBufferedImage() {
		return this.bufferedImage;
	}
	
	/**
	 * Function sets the current index selected by user.
	 * 
	 * @param index
	 */
	@JsonIgnore
	public void setIndex(int index) {
		logger.debug("setIndex(" + index + ")");
		if (index >= 0 && index < this.shapeList.size()) {
			this.index = index;
		}
	}

	public void setScale(double scale) {
		this.dimension.setScale(scale);
		for (Shape shape : this.shapeList) {
			shape.setScale(scale);
		}
	}

	@JsonIgnore
	public void setBufferedImage(BufferedImage bufferedImage) {
		this.bufferedImage = bufferedImage;
		this.dimension.w = this.bufferedImage.getWidth();
		this.dimension.h = this.bufferedImage.getHeight();
	}

	public void setOffset(double offset) {
		this.dimension.offset = offset;
		for (Shape shape : this.shapeList) {
			shape.dimension.setOffset(offset);
		}
	}

	public void setMargin(double margin) {
		this.dimension.margin = margin;
		for (Shape shape : this.shapeList) {
			shape.dimension.setMargin(margin);
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
		shape.dimension.setOffset(this.dimension.offset);
		shape.dimension.setMargin(this.dimension.margin);
		this.shapeList.add(shape);
	}

	public Selection intersectShape(Point point) {
		Selection selection = null;
		double factor;
		Point copyPoint = null;
		Shape shape = this.getShape();
		if (shape != null) {
			copyPoint = new Point(point);
			factor = this.dimension.offset * this.dimension.scale;
			copyPoint.x -= factor;
			selection = shape.dimension.selectionPoint(copyPoint);
		}
		return selection;
	}

	@JsonIgnore
	public boolean containsShape(Shape shape) {
		boolean flag = false;
		if(this.containsPoint(shape.dimension.getStartPoint())) {
			flag = true;
		}
		return flag;
	}

	@JsonIgnore
	public Shape removeShape(Shape shape) {
		return this.removeShape(shape.uuid);
	}

	/**
	 * DIMENSION 4 Function removes Shape from shapeList using uuid.
	 * 
	 * @param shape
	 */
	@JsonIgnore
	public Shape removeShape(String uuid) {
		Shape s = null;
		for (int i = 0; i < this.shapeList.size(); i++) {
			s = this.shapeList.get(i);
			if (s.uuid.equals(uuid)) {
				this.shapeList.remove(i);
				break;
			} else {
				s = null;
			}
		}
		return s;
	}

	/**
	 * Function returns true if point is contained within file
	 * 
	 * @param point
	 * @return
	 */
	public boolean containsPoint(Point point) {
		return this.dimension.containsPoint(point);
	}

	@JsonIgnore
	@Override
	public String toString() {
		String string = "";
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		try {
			string = ow.writeValueAsString(this);
		} catch (IOException e) {
			logger.error("IOException " + e.getMessage());
		}

		return string;
	}
}
//@JsonIgnore
//public BufferedImage getShapeImage(Shape shape) {
//  BufferedImage bufferedImage = null;
//  if (this.getBufferedImage() != null) {
////      bufferedImage = this.getBufferedImage().getSubimage(shape.getX(), shape.getX(), (shape.getI() - shape.getX()),
////              (shape.getJ() - shape.getY()));
//  }
//  return bufferedImage;
//}

//@JsonIgnore
//public List<BufferedImage> getShapeListImageList(
//      List<Shape> rList) {
//  List<BufferedImage> imageList = new ArrayList<>();
//  for (Shape r : rList) {
//      imageList.add(this.getShapeImage(r));
//  }
//  return imageList;
//}

//	@JsonIgnore
//	public void addShape(Shape shape) {
//		shape.pointList.get(0).x -= (this.offset * this.scale);
//		shape.pointList.get(1).x -= (this.offset * this.scale);
//		shape.pointList.get(0).y -= (this.margin * this.scale);
//		shape.pointList.get(1).y -= (this.margin * this.scale);
//		this.shapeList.add(shape);
//	}
//@JsonIgnore
//public Shape removeShape(String uuid) {
//	Shape s = null;
//	for (int i = 0; i < this.shapeList.size(); i++) {
//		s = this.shapeList.get(i);
//		if (s.uuid.equals(uuid)) {
//			this.shapeList.remove(i);
//			s.pointList = this.getGlobalPointList(this.copyPointList(s.pointList));
//			//convert to global coordinates
////			s.pointList.get(0).x += this.offset * this.scale;
////			s.pointList.get(1).x += this.offset * this.scale;
//			break;
//		} else {
//			s = null;
//		}
//	}
//	return s;
//}

//@JsonIgnore
//public void addShape(Shape shape) {
//	shape.pointList.get(0).x -= (this.offset * this.scale);
//	shape.pointList.get(1).x -= (this.offset * this.scale);
//	shape.pointList.get(0).y -= (this.margin * this.scale);
//	shape.pointList.get(1).y -= (this.margin * this.scale);
//	this.shapeList.add(shape);
//}