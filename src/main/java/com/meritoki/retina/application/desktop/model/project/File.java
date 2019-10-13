package com.meritoki.retina.application.desktop.model.project;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

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
    @JsonProperty
    public int width = 0;
    @JsonProperty
    public int height = 0;
    @JsonProperty 
    public int scale = 1;
    @JsonProperty
    public int margin = 0;
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
//    @JsonIgnore
//    public void setShape(String uuid) {
//        logger.debug("setShape(" + uuid + ")");
//        Shape shape = null;
//        for (int i = 0; i < this.shapeList.size(); i++) {
//            shape = this.shapeList.get(i);
//            if (shape.uuid.equals(uuid)) {
//                this.index = i;
//                break;
//            }
//        }
//    }
    
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
    
//  @JsonIgnore
//  public BufferedImage getShapeImage(Shape shape) {
//      BufferedImage bufferedImage = null;
//      if (this.getBufferedImage() != null) {
////          bufferedImage = this.getBufferedImage().getSubimage(shape.getX(), shape.getX(), (shape.getI() - shape.getX()),
////                  (shape.getJ() - shape.getY()));
//      }
//      return bufferedImage;
//  }

//  @JsonIgnore
//  public List<BufferedImage> getShapeListImageList(
//          List<Shape> rList) {
//      List<BufferedImage> imageList = new ArrayList<>();
//      for (Shape r : rList) {
//          imageList.add(this.getShapeImage(r));
//      }
//      return imageList;
//  }
    
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
    
    /**
     * Functions adds shape to shapeList.
     * @param shape
     */
    @JsonIgnore
    public void addShape(Shape shape) {
    	logger.info("addShape("+shape+")");
    	boolean flag = false;
        for(Shape s: this.shapeList) {
            if(s.uuid.equals(shape.uuid)) {
                s.removed =false;
                flag = this.setShape(s.uuid);
                break;
            } 
        }
        if(!flag) {
        	this.shapeList.add(shape);
        }
    }
    
    @JsonIgnore
    public void loadBufferedImage() {
        if (this.path != null && this.name != null) {
            try {
            	logger.info("loadBufferedImage() "+this.path + "/" + this.name);
                this.bufferedImage = ImageIO.read(new java.io.File(this.path + "/" + this.name));
                this.width = this.bufferedImage.getWidth();
                this.height = this.bufferedImage.getHeight();
            } catch (IOException ex) {
                logger.error(ex);
            }
        }
    }
}
