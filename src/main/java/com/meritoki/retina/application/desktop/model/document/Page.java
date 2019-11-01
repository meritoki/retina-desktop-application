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
package com.meritoki.retina.application.desktop.model.document;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
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

import com.meritoki.retina.application.desktop.model.Model;

/**
 * The Page class is used to hold a list of shapes.
 * The list of shapes can be loaded from a Layout.
 * 
 * 
 * Displacement of shapeLists is maitained in the Page object.
 * This is simply a mechanism that converts the points in a Shape to 
 * the coordinate system of the page. 
 * The page is the size of all the Files loaded into a bufferedImage and displayed.
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
	 * List of Files loaded by user.
	 */
	@JsonProperty
    public List<File> fileList = new ArrayList<File>();
	/**
	 * Currently index selected by user.
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
    @JsonIgnore
    public double scale = 1;

    /**
     * Page class retains references to one or more files
     */
    public Page() {
        this.uuid = UUID.randomUUID().toString();
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

	@JsonIgnore
	public File getFile() {
	    File file = null;
	    List<File> fileList = this.getFileList();
	    if (this.index >= 0 && this.index < fileList.size()) {
	        file = fileList.get(this.index);
	        logger.debug("getFile() file="+file);
	    }
	    
	    return file;
	}

	/**
	 * Crucial method that is failing. How to troubleshoot?
	 * 
	 * @param point
	 * @return
	 */
	@JsonIgnore
	public File getFile(Point point) {
		List<File> fileList = this.getFileList();
		File file = null;
		for(int i = 0;i < fileList.size();i++) {
			File f = fileList.get(i);
			logger.debug("getFile(point) f.offset="+f.offset);
			logger.debug("getFile(point) f.scale="+f.scale);
			logger.debug("getFile(point) f.offset*f.scale="+f.offset*f.scale);
			if(point.x > (f.offset*f.scale) && point.x < (f.offset+f.width)*f.scale) {
				file = f;
			}
		}
		logger.info("getFile("+point+") file="+file);
		return file;
	}
	
	@JsonIgnore
	public List<File> getFileList() {
    	double offset = 0;
    	for(File file: this.fileList) {
    		if(file.getBufferedImage() == null)
    			file.setBufferedImage();
    		file.setOffset(offset);
    		file.setMargin(0);
    		file.setDimension();
    		offset+=file.width;
    	}
		return this.fileList;
	}

	@JsonIgnore
	public Shape getShape(Point point) {
		logger.trace("getShape("+point+")");
		Shape s = null;
		for(File file:this.fileList) {
			if(file.getShape(point) != null) {
				s = file.getShape(point);
				break;
			}
		}
		return s;
	}
	
	@JsonIgnore
	public Shape getShape() {
		File file = this.getFile();
		return (file != null) ? file.getShape() : null;
	}

	@JsonIgnore
	public int intersectShape(Point point) {
		logger.trace("intersectShape("+point+")");
		int selection = -1;
		File file = this.getFile();
		if(file != null) {
			selection = file.intersectShape(point);
		}
		return selection;
	}

	@JsonIgnore
	public List<Shape> getShapeList() {
		List<Shape> shapeList = new ArrayList<>();
		Dimension dimension = null;
		double offset = 0;
		double minMargin = this.getFileListMinMargin();
    	double maxMargin = this.getFileListMaxMargin();
		for(File file: this.getFileList()) {
			for(Shape shape : file.getShapeList()) {
				shape.initDimension();
				dimension = shape.dimension;
				dimension.x += (offset * shape.scale);
//				dimension.y += margin;
				shapeList.add(shape);
			}
			offset+=file.width;
		}
		return shapeList;
	}

	/**
	 * Function gets a bufferedImage with all the files from fileList as one.
	 * @return
	 */
	@JsonIgnore
	public void setBufferedImage() {
    	File bufferedImageFile = null;
    	for(File file: this.fileList) {
    		if(bufferedImageFile == null) {
    			bufferedImageFile = file;
    			bufferedImageFile.bufferedImage = null;
    			bufferedImageFile.setBufferedImage();
    		} else {
    			file.getBufferedImage();
    			bufferedImageFile.bufferedImage = this.joinFile(bufferedImageFile, file);
    		}
    	}
    	this.bufferedImage = bufferedImageFile.bufferedImage;
	}
	
	@JsonIgnore
	public BufferedImage getBufferedImage() {
		return this.bufferedImage;
	}
	
	@JsonIgnore
	public void resetBufferedImage() {
		this.bufferedImage = null;
		for(File file: this.fileList) {
			file.bufferedImage = null;
		}
	}

	/**
     * Function sets the current index selected by user.
     * @param index
     */
    @JsonIgnore
    public void setIndex(int index) {
        logger.info("setIndex(" + index + ")");
        if (index >= 0 && index < this.fileList.size()) {
            this.index = index;
        }
    }
    
    @JsonIgnore
    public void setScale(double scale) {
        logger.debug("setScale(" + scale + ")");
        this.scale = scale;
        for(File file:this.fileList) {
        	file.setScale(scale);
        }
    }
    
    @JsonIgnore
    public void setShape(String uuid) {
		for(File file : this.fileList){
			if(file.setShape(uuid)) {
				file.setShape(uuid);
				if(!file.uuid.equals(this.getFile().uuid)) {
					this.setFile(file.uuid);
				}
			}
		}
	}
    
    @JsonIgnore
    public void setFile(String uuid) {
		logger.debug("setFile("+uuid+")");
		File file = null;
		for(int i=0;i<this.fileList.size();i++) {
			file = this.fileList.get(i);
			if(file.uuid.equals(uuid)) {
				this.setIndex(i);
				break;
			}
		}
	}

    @JsonIgnore
	public void addShape(Shape shape) {
    	logger.debug("addShape("+shape+")");
    	File file = this.getFile();
    	if(file != null) {
    		file.addShape(shape);
    	}
    }
    
    @JsonIgnore
    public void addFile(File file) {
    	logger.info("addFile("+file+")");
    	file.setScale(this.scale);
    	this.fileList.add(file);
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
    	logger.debug("getDataMatrix()");
        List<LinkedList<Data>> dataMatrix = null;
        List<Shape> shapeList = this.getShapeList();
        if (shapeList != null && shapeList.size() > 0) {
        	logger.debug("getDataMatrix() shapeList = "+shapeList);
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

    @JsonIgnore
    public void printDataMatrix(List<LinkedList<Data>> data) {
//    	logger.info("printDataMatrix(...)");
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
//            logger.info(string);
        }
    }

    @JsonIgnore
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
    
    @JsonIgnore
    public double getFileListMinMargin() {
    	double min = 65536;
    	for(File file: this.fileList) {
    		if(file.margin < min) {
    			min = file.margin;
    		}
    	}
    	return min;
    }
    
    @JsonIgnore
    public double getFileListMaxMargin() {
    	double max = -65536;
    	for(File file: this.fileList) {
    		if(file.margin > max) {
    			max = file.margin;
    		}
    	}
    	return max;
    }

    @JsonIgnore
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

    @JsonIgnore
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

    @JsonIgnore
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

    @JsonIgnore
    public boolean columnListContains(List<Shape> shapeList, Shape shape) {
        boolean flag = false;
        for (Shape s : shapeList) {
            if (s.uuid.equals(shape.uuid)) {
                flag = true;
            }
        }
        return flag;
    }

    @JsonIgnore
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
    
    @JsonIgnore
    public BufferedImage joinFile(File file1, File file2) { //BufferedImage img1,BufferedImage img2) {
    	logger.info("joinBufferedImage("+file1+","+file2+")");
        //do some calculate first
//        int offset  = 5;
    	int wid = 1;
    	int height = 1;
    	BufferedImage newImage = new BufferedImage(wid,height, BufferedImage.TYPE_INT_ARGB);
    	if(file1.bufferedImage != null && file2.bufferedImage != null) {
        wid = file1.bufferedImage.getWidth()+file2.bufferedImage.getWidth();//+offset;
        height = Math.max(file1.bufferedImage.getHeight()+(int)file1.margin,file2.bufferedImage.getHeight()+(int)file2.margin);//+offset;
        //create a new buffer and draw two image into the new image
        newImage = new BufferedImage(wid,height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = newImage.createGraphics();
        Color oldColor = g2.getColor();
        //fill background
        g2.setPaint(Color.WHITE);
        g2.fillRect(0, 0, wid, height);
        //draw image
        g2.setColor(oldColor);
        g2.drawImage(file1.bufferedImage, null, 0, (int)file1.margin);
        g2.drawImage(file2.bufferedImage, null, file1.bufferedImage.getWidth(), (int)file2.margin);
//        g2.drawImage(file2.bufferedImage, null, file1.bufferedImage.getWidth()+offset, (int)file2.margin);
        g2.dispose();
    	}
        return newImage;
    }

//    public BufferedImage joinBufferedImage(BufferedImage img1,BufferedImage img2) {
//    	logger.info("joinBufferedImage("+img1+","+img2+")");
//        //do some calculate first
//        int offset  = 5;
//        int wid = img1.getWidth()+img2.getWidth()+offset;
//        int height = Math.max(img1.getHeight(),img2.getHeight())+offset;
//        //create a new buffer and draw two image into the new image
//        BufferedImage newImage = new BufferedImage(wid,height, BufferedImage.TYPE_INT_ARGB);
//        Graphics2D g2 = newImage.createGraphics();
//        Color oldColor = g2.getColor();
//        //fill background
//        g2.setPaint(Color.WHITE);
//        g2.fillRect(0, 0, wid, height);
//        //draw image
//        g2.setColor(oldColor);
//        g2.drawImage(img1, null, 0, 0);
//        g2.drawImage(img2, null, img1.getWidth()+offset, 0);
//        g2.dispose();
//        return newImage;
//    }
}
