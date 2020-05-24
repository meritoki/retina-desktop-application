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
	@JsonProperty
	public List<Shape> shapeList = new ArrayList<>();
	@JsonProperty
	public Position position = new Position();
	@JsonIgnore
	public BufferedImage bufferedImage = null;
	@JsonIgnore
	public int index = 0;
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
		this.position = image.position;
		this.index = image.index;
		for (Shape shape : image.shapeList) {
			this.shapeList.add(new Shape(shape));
		}
	}
	
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
			if (shape.position.containsPoint((point))) {
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
		this.position.setScale(scale);
		for (Shape shape : this.shapeList) {
			shape.setScale(scale);
		}
	}

	@JsonIgnore
	public void setBufferedImage(BufferedImage bufferedImage) {
		this.bufferedImage = bufferedImage;
		this.position.w = this.bufferedImage.getWidth();
		this.position.h = this.bufferedImage.getHeight();
	}

	public void setOffset(double offset) {
		this.position.offset = offset;
		for (Shape shape : this.shapeList) {
			shape.position.setOffset(offset);
		}
	}

	/** 
	 * Function necessary for a shape to move at all with the margin shift
	 * @param margin
	 */
	public void setMargin(double margin) {
		logger.info("setMargin("+margin+")");
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
		logger.info("addShape("+shape+")");
		shape.position.setOffset(this.position.offset);
		shape.position.setMargin(this.position.margin);
		shape.position.setAddScale(this.position.scale);
		this.shapeList.add(shape);
	}

	public Selection intersectShape(Point point) {
		Selection selection = null;
		double factor;
		Point copyPoint = null;
		Shape shape = this.getShape();
		if (shape != null) {
			copyPoint = new Point(point);
			factor = this.position.offset * this.position.scale;
			copyPoint.x -= factor;
			selection = shape.position.selectionPoint(copyPoint);
		}
		return selection;
	}

	@JsonIgnore
	public boolean containsShape(Shape shape) {
		boolean flag = false;
		if(this.containsPoint(shape.position.getStartPoint())) {
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