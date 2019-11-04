package com.meritoki.retina.application.desktop.model.document;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;

import com.meritoki.retina.application.desktop.controller.node.NodeController;

public class File {
	@JsonIgnore
	static Logger logger = LogManager.getLogger(File.class.getName());
	@JsonProperty
	public String uuid = null;
	@JsonProperty
	public String name = null;
	@JsonProperty
	public String path = null;
	@JsonProperty
	public String cachePath = null;
	@JsonIgnore
	public BufferedImage bufferedImage = null;
	@JsonIgnore
	public Dimension dimension = null;
	@JsonProperty
	public double width = 0;
	@JsonProperty
	public double height = 0;
	@JsonProperty
	public double offset = 0;
	@JsonProperty
	public double margin = 0;
	@JsonProperty
	public double scale = 1;
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
	 * Instantiate new instance of File
	 */
	public File() {
		this.uuid = UUID.randomUUID().toString();
	}

	public File(File file) {
		this.uuid = file.uuid;
		this.name = file.name;
		this.path = file.path;
		this.cachePath = file.cachePath;
		this.bufferedImage = file.bufferedImage;
		this.width = file.width;
		this.height = file.height;
		this.offset = file.offset;
		this.margin = file.margin;
		this.scale = file.scale;
		this.index = file.index;
		for (Shape shape : file.shapeList) {
			this.shapeList.add(new Shape(shape));
		}
	}

	public File(String path, String name) {
		this.uuid = UUID.randomUUID().toString();
		this.path = path;
		this.name = name;
		this.setBufferedImage(NodeController.openBufferedImage(this.path, this.name));
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

	public Shape getShape(Point point) {
		Shape s = null;
		Point copyPoint = null;
		for (Shape shape : this.shapeList) {
			copyPoint = new Point(point);
			copyPoint.x -= this.offset * this.scale;// Required
			if (shape.contains(copyPoint)) {
				logger.info("getShape(" + copyPoint + ") s.uuid="+shape.uuid);
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
		logger.debug("setScale(" + scale + ")");
		this.scale = scale;
		for (Shape shape : this.shapeList) {
			shape.setScale(this.scale);
		}
	}

	@JsonIgnore
	public Dimension getDimension() {
		logger.debug("getDimension()");
		Dimension dimension = new Dimension();
		dimension.x = this.offset * this.scale;
		dimension.y = this.margin * this.scale;
		dimension.w = this.width * this.scale;
		dimension.h = this.height * this.scale;
		return dimension;
	}

	@JsonIgnore
	public void setBufferedImage(BufferedImage bufferedImage) {
		this.bufferedImage = bufferedImage;
		this.setWidth(this.bufferedImage.getWidth());
		this.setHeight(this.bufferedImage.getHeight());
	}

	@JsonIgnore
	public void setWidth(double width) {
		logger.debug("setWidth(" + width + ")");
		this.width = width;
	}

	@JsonIgnore
	public void setHeight(double height) {
		logger.debug("setHeight(" + height + ")");
		this.height = height;
	}

	public double getWidth() {
		return this.width;
	}

	public double getHeight() {
		return this.height;
	}

	public String getPath() {
		return this.path;
	}

	public String getName() {
		return this.name;
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

	public void setOffset(double offset) {
		logger.debug("setOffset(" + offset + ")");
		this.offset = offset;
	}

	public void setMargin(double margin) {
		logger.debug("setMargin(" + margin + ")");
		this.margin = margin;
	}

	/**
	 * B
	 * Functions adds shape to shapeList. The only place a shape's
	 * point list is modified to fit within the File
	 * @param shape
	 */
	@JsonIgnore
	public void addShape(Shape shape) {
		shape.pointList.get(0).x -= (this.offset * this.scale);
		shape.pointList.get(1).x -= (this.offset * this.scale);
		shape.pointList.get(0).y -= (this.margin * this.scale);
		shape.pointList.get(1).y -= (this.margin * this.scale);
		this.shapeList.add(shape);
	}

	public int intersectShape(Point point) {
			int selection = -1;
			double factor;
			Point copyPoint = null;
			Shape shape = this.getShape();
			if (shape != null) {
				copyPoint = new Point(point);
				factor = this.offset * shape.scale;
				copyPoint.x -= factor;
				selection = shape.intersect(copyPoint);
			}
			return selection;
		}

	@JsonIgnore
	public boolean containsShape(Shape shape) {
		boolean flag = false;
		if(this.containsPoint(new Point(shape.pointList.get(0)))){
			flag = true;
		}
		return flag;
	}

	/**
	 * Function sets removed variable for Shape equal to true;
	 * 
	 * @param shape
	 */
	@JsonIgnore
	public Shape removeShape(Shape shape) {
		Shape s = null;
		for (int i = 0; i < this.shapeList.size(); i++) {
			s = this.shapeList.get(i);
			if (s.uuid.equals(shape.uuid)) {
				this.shapeList.remove(i);
				s.pointList.get(0).x += this.offset * s.scale;
				s.pointList.get(1).x += this.offset * s.scale;
				break;
			} else {
				s = null;
			}
		}
		return s;
	}

	public boolean containsPoint(Point point) {
		boolean flag = false;
		if (point.x > (this.offset * this.scale) && point.x < (this.offset + this.width) * this.scale) {
			flag = true;
		}
		return flag;
	}

	@JsonIgnore
	@Override
	public String toString() {
		String string = "";
		if (logger.isTraceEnabled()) {
			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			try {
				string = ow.writeValueAsString(this);
			} catch (IOException ex) {
				java.util.logging.Logger.getLogger(Shape.class.getName()).log(Level.SEVERE, null, ex);
			}
		} else {
			string = this.uuid;
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
