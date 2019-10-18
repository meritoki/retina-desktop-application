package com.meritoki.retina.application.desktop.model.project;

import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

import javax.imageio.ImageIO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;

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
    
    public Dimension dimension = null;
    @JsonProperty
    public double width = 0;
    @JsonProperty
    public double height = 0;
    @JsonProperty 
    public double scale = 1;
    @JsonProperty
    public double margin = 0;
    @JsonProperty
    public double offset = 0;
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
    public File(){ 
        this.uuid = UUID.randomUUID().toString();
    }
    
    public File(String path, String name){ 
        this.uuid = UUID.randomUUID().toString();
        this.path = path;
        this.name = name;
    }
    
//    Rectangle2D.Double rectangle = new Rectangle2D.Double(file.offset * model.scale,
//			file.margin * model.scale, file.width * model.scale, file.height * model.scale);
    
    /**
	 * Function gets the current index selected by user.
	 * @return
	 */
	@JsonIgnore
	public int getIndex() {
	    logger.debug("getIndex() this.index=" + this.index);
	    return this.index;
	}

	/**
	 * Functions gets shape for index.
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
		logger.info("getShape("+point+")");
		Shape s = null;
		double factor = this.offset*this.scale;
		logger.info("getShape(point) this.offset="+this.offset);
		logger.info("getShape(point) this.scale="+this.scale);
		logger.info("getShape(point) factor="+this.offset*this.scale);
//		point.x += factor;//Does not fix
		for(Shape shape: this.shapeList) {
			logger.info("getShape("+point+")");
			if(shape.contains(point)) {
				s = shape;
				break;
			}
		}
		return s;
	}

	/**
	 * Function transforms the 
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
        for(Shape shape:this.shapeList) {
        	shape.setScale(this.scale);
        }
    }
    
    //    Rectangle2D.Double rectangle = new Rectangle2D.Double(file.offset * model.scale,
	//			file.margin * model.scale, file.width * model.scale, file.height * model.scale);
	    
	    @JsonIgnore
	    public void setDimension() {
	    	logger.debug("getDimension()");
	    	Dimension dimension = new Dimension();
	    	dimension.x = this.offset * this.scale;
	    	dimension.y = this.margin * this.scale;
	    	dimension.w = this.width * this.scale;
	    	dimension.h = this.height* this.scale;
		    this.dimension = dimension;
	    }

	@JsonIgnore
	public void setBufferedImage() {
        if (this.path != null && this.name != null) {
            try {
            	logger.info("setBufferedImage() "+this.path + "/" + this.name);
                this.bufferedImage = ImageIO.read(new java.io.File(this.path + "/" + this.name));
                this.width = this.bufferedImage.getWidth();
                this.height = this.bufferedImage.getHeight();
                logger.info("getBufferedImage() width="+width);
                logger.info("getBufferedImage() height="+height);
            } catch (IOException ex) {
                logger.error(ex);
            }
        }
	}

	/**
     * Functions sets current index by uuid.
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
//    	logger.info("setOffset("+offset+")");
    	this.offset = offset;
    }
    
    public void setMargin(double margin) {
//    	logger.info("setMargin("+margin+")");
    	this.margin = margin;
    }
    
    
    /**
	 * Functions adds shape to shapeList.
	 * @param shape
	 */
	@JsonIgnore
	public void addShape(Shape shape) {
		logger.info("addShape("+shape+") shape.pointList="+shape.pointList);
		shape.pointList.get(0).x -= (this.offset*shape.scale);
		shape.pointList.get(1).x -= (this.offset*shape.scale);
		logger.info("addShape(shape) this.offset="+this.offset);
		logger.info("addShape(shape) this.scale="+this.scale);
		logger.info("addShape(shape) this.offset*this.scale="+this.offset*this.scale);
		logger.info("addShape(shape) shape.pointList="+shape.pointList);
		this.shapeList.add(shape);
	}

	/**
     * Function sets removed variable for Shape equal to true;
     * @param shape
     */
    @JsonIgnore
    public void removeShape(Shape shape) {
        for(Shape s: this.shapeList) {
            if(s.uuid.equals(shape.uuid)) {
                s.removed =true;
                break;
            }
        }
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
