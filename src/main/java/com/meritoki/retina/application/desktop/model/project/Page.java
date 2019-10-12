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
package com.meritoki.retina.application.desktop.model.project;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
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

/**
 * The Page class is used to hold a list of shapes.
 * The list of shapes can be loaded from a Layout.
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
	 * List of Files loaded by user.
	 */
	@JsonProperty
    public List<File> fileList = new ArrayList<File>();
	/**
	 * List of Shapes drawn by user.
	 */
	@JsonProperty
    public List<Shape> shapeList = new ArrayList<>();
	/**
	 * Currently index selected by user.
	 */
    @JsonIgnore
    public int index = 0;
    
    @JsonIgnore
    public BufferedImage bufferedImage = null;

    /**
     * Page class retains references to one or more files
     */
    public Page() {
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
    public void setShape(String uuid) {
        logger.debug("setShape(" + uuid + ")");
        Shape shape = null;
        for (int i = 0; i < this.shapeList.size(); i++) {
            shape = this.shapeList.get(i);
            if (shape.uuid.equals(uuid)) {
                this.index = i;
                break;
            }
        }
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
    
    /**
     * Functions adds shape to shapeList.
     * @param shape
     */
    @JsonIgnore
    public void addShape(Shape shape) {
        for(Shape s: this.shapeList) {
            if(s.uuid.equals(shape.uuid)) {
                s.removed =false;
                break;
            }
        }
    }
    
    public void addShapeList(List<Shape> shapeList) {
    	logger.info("addShapeList("+shapeList+")");
    	this.shapeList.addAll(shapeList);
    }

//    @JsonIgnore
//    public BufferedImage getShapeImage(Shape shape) {
//        BufferedImage bufferedImage = null;
//        if (this.getBufferedImage() != null) {
////            bufferedImage = this.getBufferedImage().getSubimage(shape.getX(), shape.getX(), (shape.getI() - shape.getX()),
////                    (shape.getJ() - shape.getY()));
//        }
//        return bufferedImage;
//    }

//    @JsonIgnore
//    public List<BufferedImage> getShapeListImageList(
//            List<Shape> rList) {
//        List<BufferedImage> imageList = new ArrayList<>();
//        for (Shape r : rList) {
//            imageList.add(this.getShapeImage(r));
//        }
//        return imageList;
//    }
    
    /**
     * Function loads the bufferedImage instantiated using the fileList.
     */
    public void loadBufferedImage() {
    	if(this.bufferedImage == null) {
    		logger.info("loadBufferedImage() bufferedImage=null");
    		this.bufferedImage = this.initBufferedImage();
    	}
    }
    
    /**
     * Function gets a bufferedImage with all the files from fileList as one.
     * @return
     */
    @JsonIgnore
    public BufferedImage initBufferedImage() {
    	logger.debug("initBufferedImage()");
    	BufferedImage bufferedImage = null;
    	for(File file: this.fileList) {
    		file.loadBufferedImage();
    		if(bufferedImage == null) {
    			bufferedImage = file.bufferedImage;
    		} else {
    			bufferedImage = this.joinBufferedImage(bufferedImage, file.bufferedImage);
    		}
    	}
        return bufferedImage;
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

    @JsonIgnore
    public List<LinkedList<Data>> getDataMatrix() {
    	logger.info("getDataMatrix()");
        List<LinkedList<Data>> dataMatrix = null;
        if (this.shapeList != null && this.shapeList.size() > 0) {
        	logger.info("getDataMatrix() this.shapeList = "+this.shapeList);
            ArrayList<Shape> shapeList = new ArrayList<>(this.shapeList);
            Shape shape = null;
            int averageY;
            int threshold = 64;
            List<Shape> columnList;
            List<List<Shape>> rowList = new ArrayList<>();
            boolean flag = true;
            for (int i = 0; i < shapeList.size(); i++) {
                shape = shapeList.get(i);
                columnList = new ArrayList<>();
                if (!rowList.isEmpty()) {
                    for (List<Shape> cList : rowList) {
                        averageY = this.getShapeListYAverage(cList, shape);
                        columnList = new ArrayList<>(cList);
                        columnList.add(shape);
                        if (this.isShapeListYInThreshold(columnList, averageY, threshold)) {
                            cList.add(shape);
                            flag = false;
                        }
                    }
                    if (flag) {
                        columnList = new ArrayList<>();
                        columnList.add(shape);
                        rowList.add(columnList);
                    } else {
                        flag = true;
                    }
                } else {
                    columnList.add(shape);
                    rowList.add(columnList);
                }
            }
            int row = rowList.size();
            int columnMax = 0;
            int size = 0;
            for (List<Shape> cList : rowList) {
                size = cList.size();
                if (size > columnMax) {
                    columnMax = size;
                }
            }
            rowList = this.sortRowList(rowList);
            dataMatrix = new LinkedList<>();
            for (int i = 0; i < row; i++) {
                dataMatrix.add(i, new LinkedList<Data>());
                for (int j = 0; j < rowList.get(i).size(); j++) {
                    shape = rowList.get(i).get(j);
                    if (shape.data != null) {
                        dataMatrix.get(i).add(j, shape.data);
                    }
                }
            }
            this.printDataMatrix(dataMatrix);
        }
        return dataMatrix;
    }

    @JsonIgnore
    public List<Data> getDataList() {
        logger.debug("getDataList()");
        List<Data> dataList = null;
        List<LinkedList<Data>> dataMatrix = this.getDataMatrix();
        if (dataMatrix != null) {
            dataList = new LinkedList<>();
            for (int i = 0; i < dataMatrix.size(); i++) {
                for (int j = 0; j < dataMatrix.get(i).size(); j++) {
                    dataList.add(dataMatrix.get(i).get(j));
                }
            }
        }
        return dataList;
    }

    public void printDataMatrix(List<LinkedList<Data>> data) {
    	logger.info("printDataMatrix(...)");
        String string = null;
        if (data != null && data.size() > 0) {
            string = "\n";
            for (int i = 0; i < data.size(); i++) {
                for (int j = 0; j < data.get(i).size(); j++) {
                    if (data.get(i).get(j) != null) {
                        string += "*";
                    }
                }
                if (i < (data.size() - 1)) {
                    string += "\n";
                }
            }
        }
        if (string != null) {
            logger.info(string);
        }
    }

    public boolean isShapeListYInThreshold(List<Shape> shapeList, int averageY, int threshold) {
        logger.debug("isShapeListYInThreshold(" + shapeList + ", " + averageY + ", " + threshold + ")");
        boolean flag = true;
        double a = 0;
        for (Shape shape : shapeList) {
            a = shape.getCenterY();
            a = Math.abs(averageY - a);
            if (a > threshold) {
                flag = false;
                break;
            }
        }
        return flag;
    }

    public int getShapeListYAverage(List<Shape> shapeList, Shape rectangle) {
        logger.debug("getShapeListYAverage(" + shapeList + ", " + rectangle + ")");
        int count = 0;
        int sum = 0;
        for (Shape r : shapeList) {
            sum += r.getCenterY();
            count += 1;
        }
        count += 1;
        sum += rectangle.getCenterY();
        return sum / count;
    }

    public List<List<Shape>> sortRowList(List<List<Shape>> rowList) {
        for (int i = 0; i < rowList.size(); i++) {
            this.sortColumnList(rowList.get(i));
            for (int j = rowList.size() - 1; j > i; j--) {
                if (rowList.get(i).get(0).getCenterY() > rowList.get(j).get(0).getCenterY()) {
                    List<Shape> tmp = rowList.get(i);
                    rowList.set(i, rowList.get(j));
                    rowList.set(j, tmp);
                }
            }
        }
        return rowList;
    }

    public List<Shape> sortColumnList(List<Shape> shapeList) {
        for (int i = 0; i < shapeList.size(); i++) {
            for (int j = shapeList.size() - 1; j > i; j--) {
                if (shapeList.get(i).pointList.get(0).x > shapeList.get(j).pointList.get(0).x) {
                    Shape tmp = shapeList.get(i);
                    shapeList.set(i, shapeList.get(j));
                    shapeList.set(j, tmp);
                }
            }
        }
        return shapeList;
    }

    public boolean columnListContains(List<Shape> shapeList, Shape shape) {
        boolean flag = false;
        for (Shape s : shapeList) {
            if (s.uuid.equals(shape.uuid)) {
                flag = true;
            }
        }
        return flag;
    }

    public boolean rowListContains(List<List<Shape>> shapeList, Shape shape) {
        boolean flag = false;
        for (List<Shape> s : shapeList) {
            flag = this.columnListContains(s, shape);
            if (flag) {
                break;
            }
        }
        return flag;
    }

    public BufferedImage joinBufferedImage(BufferedImage img1,BufferedImage img2) {
    	logger.info("joinBufferedImage(...,...)");
        //do some calculate first
        int offset  = 5;
        int wid = img1.getWidth()+img2.getWidth()+offset;
        int height = Math.max(img1.getHeight(),img2.getHeight())+offset;
        //create a new buffer and draw two image into the new image
        BufferedImage newImage = new BufferedImage(wid,height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = newImage.createGraphics();
        Color oldColor = g2.getColor();
        //fill background
        g2.setPaint(Color.WHITE);
        g2.fillRect(0, 0, wid, height);
        //draw image
        g2.setColor(oldColor);
        g2.drawImage(img1, null, 0, 0);
        g2.drawImage(img2, null, img1.getWidth()+offset, 0);
        g2.dispose();
        return newImage;
    }
}
