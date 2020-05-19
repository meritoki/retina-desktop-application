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
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.meritoki.app.desktop.retina.controller.node.NodeController;
import com.meritoki.app.desktop.retina.controller.script.ScriptController;

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
	@JsonIgnore
	public double scale = 1;

	@JsonProperty
	public Script script = new Script();

	@JsonIgnore
	public double threshold = 16;

	@JsonIgnore
	public List<ArrayList<Shape>> shapeMatrix = null;

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
		this.bufferedImage = page.bufferedImage;
		this.scale = page.scale;
		if (page.shapeMatrix != null) {
			List<ArrayList<Shape>> shapeMatrix = new ArrayList<>();
			for (List<Shape> slist : page.shapeMatrix) {
				ArrayList<Shape> shapeList = new ArrayList<Shape>();
				for (Shape s : slist) {
					shapeList.add(new Shape(s));
				}
				shapeMatrix.add(shapeList);
			}
			this.shapeMatrix = shapeMatrix;
		}

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
				shape.dimension.setScale(image.scale);
				shape.dimension.setOffset(image.getOffset());
				shape.dimension.setMargin(image.getMargin());
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
			int x = (int) (shape.getDimension().x / this.scale);
			int y = (int) (shape.getDimension().y / this.scale);
			int w = (int) (shape.getDimension().width / this.scale);
			int h = (int) (shape.getDimension().height / this.scale);
//			bufferedImage = this.getBufferedImage().getSubimage(x, y, w, h);
		}
		return bufferedImage;
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
						image.uuid + "." + image.extension);
				if (bufferedImage == null) {
					bufferedImage = NodeController.openBufferedImage(image.getPath(), image.getNameAndExtension());
					if (bufferedImage != null) {
						image.setBufferedImage(bufferedImage);
						if (image.extension.equals("jpg") || image.extension.equals("jpeg")) {
							NodeController.saveJpg(NodeController.getImageCache(), image.uuid + "." + image.extension,
									bufferedImage);
						}
						// TODO Add support for PNG
//						if (ClientController.fileClient.checkHealth()) {
//							ClientController.fileClient.registerFile(image.uuid);
//							if (ClientController.fileClient.checkFile(image.uuid)) {
//								ClientController.fileClient.uploadFile(
//										NodeController.getImageCache() + NodeController.getSeperator(),
//										image.uuid + "." + image.extension);
//							}
//						}
					} else {
//						if (ClientController.fileClient.checkHealth()) {
//							if (ClientController.fileClient.checkFile(image.uuid)) {
//								ClientController.fileClient.downloadFile(
//										NodeController.getImageCache() + NodeController.getSeperator(),
//										image.getUUID() + "." + image.getExtension());
//								ClientController.fileClient.unmarkFile(image.uuid);
//							} else {
//								ClientController.fileClient.markFile(image.uuid);
//							}
//						}
					}
				} else {
					image.setBufferedImage(bufferedImage);
//					if (ClientController.fileClient.checkHealth()) {
//						ClientController.fileClient.registerFile(image.uuid);
//						if (ClientController.fileClient.checkFile(image.uuid)) {
//							ClientController.fileClient.uploadFile(
//									NodeController.getImageCache() + NodeController.getSeperator(),
//									image.uuid + "." + image.extension);
//						}
//					}
				}
			}
			image.setOffset(offset);
			image.setScale(this.scale);
			offset += image.getWidth();
		}
		return this.imageList;
	}

	/**
	 * Function returns bufferedImage with one or more File bufferedImages from the
	 * fileList
	 *
	 * @return this.bufferedImage;
	 */
	@JsonIgnore
	public BufferedImage getBufferedImage() {
		if (this.bufferedImage == null) {
			BufferedImage bufferedImage = null;
			Image file = null;
			for (Image f : this.getImageList()) {
				if (file == null) {
					file = new Image(f);
					bufferedImage = this.modifyImage(file);
				} else {
					bufferedImage = this.joinImages(file, f);
				}
			}
			this.bufferedImage = bufferedImage;
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
		this.scale = scale;
		for (Image file : this.imageList) {
			file.setScale(scale);
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
				this.setImage(image.uuid);
				this.setShape(shape.uuid);
				break;
			}
		}
	}

	@JsonIgnore
	public void addFile(Image file) {
		logger.info("addFile(" + file + ")");
		file.setScale(this.scale);
		this.imageList.add(file);
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
		Image file = this.getImage();
		if (file != null) {
			selection = file.intersectShape(point);
		}
		return selection;
	}

//	@JsonIgnore
//	public int intersectShape(Point point) {
//		logger.trace("intersectShape(" + point + ")");
//		int selection = -1;
//		File file = this.getFile();
//		if (file != null) {
//			selection = file.intersectShape(point);
//		}
//		return selection;
//	}

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

	public void setShapeMatrix(List<ArrayList<Shape>> matrix) {
		this.shapeMatrix = matrix;
	}

	public List<ArrayList<Shape>> getShapeMatrix() {
		this.shapeMatrix = this.initShapeMatrix();
		if (!this.script.value.equals("")) {
			try {
				ScriptController.sortShapeMatrix(this.shapeMatrix, this.script.value);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return this.shapeMatrix;
	}

	@JsonIgnore
	public List<Shape> getShapeMatrixShapeList() {
		logger.debug("getShapeMatrixShapeList()");
		List<Shape> shapeList = null;
		List<ArrayList<Shape>> shapeMatrix = this.getShapeMatrix();// this.initMatrix();
		if (shapeMatrix != null) {
			shapeList = new LinkedList<>();
			for (int i = 0; i < shapeMatrix.size(); i++) {
				for (int j = 0; j < shapeMatrix.get(i).size(); j++) {
					shapeList.add(shapeMatrix.get(i).get(j));
				}
			}
		}
		return shapeList;
	}

	@JsonIgnore
	public List<ArrayList<Shape>> initShapeMatrix() {
		logger.debug("initShapeMatrix()");
		List<ArrayList<Shape>> shapeMatrix = new ArrayList<>();
		List<Shape> shapeList = this.getShapeList();
		if (shapeList != null && shapeList.size() > 0) {
//			logger.info("initShapeMatrix() shapeList = " + shapeList);
			Shape shape = null;
			boolean flag = true;
			for (int i = 0; i < shapeList.size(); i++) {
				// Set shape
				shape = shapeList.get(i);
				// Init new columnList, has shapes added to it
//				logger.info("initShapeMatrix() shape=" + shape);
				for (List<Shape> rowList : shapeMatrix) {
//					logger.info("initShapeMatrix() rowList=" + rowList);
					if (this.isShapeListYInThreshold(rowList, shape)) {
						rowList.add(shape);
						flag = false;
					}
				}
				if (flag) {
					ArrayList<Shape> rowList = new ArrayList<>();
					rowList.add(shape);
					shapeMatrix.add(rowList);
				} else {
					flag = true;
				}
				this.sortShapeMatrix(shapeMatrix);
			}
			this.printMatrix(shapeMatrix);
		}
		return shapeMatrix;
	}
	
//	columnList = new ArrayList<>();
//	for (Shape p : cList) {
//		columnList.add(new Shape(p));
//	}
//	columnList.add(shape);
//	this.sortColumnList(columnList);

	@JsonIgnore
	public void printMatrix(List<ArrayList<Shape>> matrix) {
		String string = null;
		if (matrix != null && matrix.size() > 0) {
			string = "\n";
			for (int i = 0; i < matrix.size(); i++) {
				for (int j = 0; j < matrix.get(i).size(); j++) {
					if (matrix.get(i).get(j) != null) {
						string += "*";
					}
				}
				if (i < (matrix.size() - 1)) {
					string += "\n";
				}
			}
		}
		if (string != null) {
			logger.debug(string);
		}
	}

	@JsonIgnore
	public double getShapeListYAverage(List<Shape> shapeList, Shape shape) {
		double average = 0;
		int count = 0;
		double sum = 0;
		for (Shape s : shapeList) {
			// d = //s.dimension;
			sum += s.dimension.getCenterY();// d.y+(d.h/2);
			count += 1;
		}
		// d = shape.dimension;s
		sum += shape.dimension.getCenterY();
		count +=1;
		average = sum / count;
//		logger.info("getShapeListYAverage(" + shapeList + "," + shape+ ") average=" + average);
		return average;
	}

	@JsonIgnore
	public boolean isShapeListYInThreshold(List<Shape> shapeList, Shape shape) {
//		logger.info("isShapeListYInThreshold(" + shapeList + ", " + averageY + ", " + threshold + ")");
		boolean flag = true;
		double a = 0;
		double average = this.getShapeListYAverage(shapeList, shape);
//		for (Shape s : shapeList) {
		a = shape.dimension.getCenterY();
		a = Math.abs(average - a);
//		logger.info("isShapeListYInThreshold(" + shapeList + ", " + shape+ ") a=" + a);
		if (a > (this.threshold*this.scale)) {
//			logger.info("isShapeListYInThreshold(" + shapeList + ", " + shape+ ") (" + a
//					+ " > " + this.threshold + ")");
			flag = false;
		}
//			else {
//				logger.info("isShapeListYInThreshold(" + shapeList + ", " + averageY + ", " + threshold + ") (" + a
//						+ " <= " + threshold + ")");
//			}
//		}
		
		return flag;
	}
		
//		public boolean isShapeListYInThreshold(List<Shape> shapeList, Shape shape) {
////			logger.info("isShapeListYInThreshold(" + shapeList + ", " + averageY + ", " + threshold + ")");
//			boolean flag = true;
//			double a = 0;
//			double average = this.getShapeListYAverage(shapeList, shape);
//			for (Shape s : shapeList) {
//				a = s.getCenterY();
//				a = Math.abs(average - a);
//				logger.info("isShapeListYInThreshold(" + shapeList + ", " + average + ", " + this.threshold + ") a=" + a);
//				if (a > this.threshold) {
//					logger.info("isShapeListYInThreshold(" + shapeList + ", " + average + ", " + this.threshold + ") (" + a
//							+ " > " + this.threshold + ")");
//					flag = false;
//					break;
//				}
////				else {
////					logger.info("isShapeListYInThreshold(" + shapeList + ", " + averageY + ", " + threshold + ") (" + a
////							+ " <= " + threshold + ")");
////				}
////			}
//			
//			return flag;
//		}

//	@JsonIgnore
//	public double getShapeListYAverage(List<Shape> shapeList, Shape shape) {
//		double average = 0;
//		int count = 0;
//		double sum = 0;
//		Dimension d = null;
//		for (Shape s : shapeList) {
////			d = //s.dimension;
//			sum += s.getCenterY();//d.y+(d.h/2);
//			count += 1;
//		}
////		d = shape.dimension;
//		sum +=  shape.getCenterY();//d.y+(d.h/2);//shape.getCenterY();
//		count += 1;
//		average = sum/count;
//		logger.info("getShapeListYAverage(" + shapeList + ", " + shape + ") average="+average);
//		return average;
//	}

	@JsonIgnore
	public double getFileListMinMargin() {
		double min = 65536;
		for (Image file : this.getImageList()) {
			if (file.margin < min) {
				min = file.margin;
			}
		}
		return min;
	}

	@JsonIgnore
	public double getFileListMaxMargin() {
		double max = -65536;
		for (Image file : this.getImageList()) {
			if (file.margin > max) {
				max = file.margin;
			}
		}
		return max;
	}

	@JsonIgnore
	public void sortShapeMatrix(List<ArrayList<Shape>> shapeMatrix) {
		for (List<Shape> shapeList : shapeMatrix) {
			this.sortRowList(shapeList);
		}
		Collections.sort(shapeMatrix, new Comparator<List<Shape>>() {
			public int compare(List<Shape> ideaVal1, List<Shape> ideaVal2) {
				Double idea1 = ideaVal1.get(0).dimension.getCenterY();
				Double idea2 = ideaVal2.get(0).dimension.getCenterY();
				return idea1.compareTo(idea2);
			}
		});
	}


	@JsonIgnore
	public void sortRowList(List<Shape> shapeList) {
		Collections.sort(shapeList, new Comparator<Shape>() {
			public int compare(Shape ideaVal1, Shape ideaVal2) {
				Double idea1 = ideaVal1.dimension.getCenterX();// pointList.get(0).x;
				Double idea2 = ideaVal2.dimension.getCenterX();// pointList.get(0).x;
				return idea1.compareTo(idea2);
			}
		});
//		for (int i = 0; i < shapeList.size(); i++) {
//			for (int j = shapeList.size() - 1; j > i; j--) {
//				if (shapeList.get(i).pointList.get(0).x > shapeList.get(j).pointList.get(0).x) {
//					Shape tmp = shapeList.get(i);
//					Collections.swap(shapeList, i, j);
////					shapeList.set(i, shapeList.get(j));
////					shapeList.set(j, tmp);
//				}
//			}
//		}
//		return shapeList;
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
	public BufferedImage modifyImage(Image a) { // BufferedImage img1,BufferedImage img2) {
		logger.debug("modifyImage(" + a + ")");
		BufferedImage bufferedImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
		if (a.bufferedImage != null) {
			int width = a.bufferedImage.getWidth();
			int height = a.bufferedImage.getHeight();
			bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			Graphics2D graphics2D = bufferedImage.createGraphics();
			Color oldColor = graphics2D.getColor();
			graphics2D.setPaint(Color.BLACK);
			graphics2D.fillRect(0, 0, width, height);
			graphics2D.setColor(oldColor);
			graphics2D.drawImage(a.bufferedImage, null, 0, (int) a.margin);
			graphics2D.dispose();
		}
		return bufferedImage;
	}

	@JsonIgnore
	public BufferedImage joinImages(Image a, Image b) {
		logger.debug("joinImages(" + a + "," + b + ")");
		BufferedImage bufferedImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
		if (a.bufferedImage != null && b.bufferedImage != null) {
			int width = a.bufferedImage.getWidth() + b.bufferedImage.getWidth();
			int height = Math.max(a.bufferedImage.getHeight() + (int) a.margin,
					b.bufferedImage.getHeight() + (int) b.margin);// +offset;
			bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			Graphics2D graphics2D = bufferedImage.createGraphics();
			Color oldColor = graphics2D.getColor();
			graphics2D.setPaint(Color.BLACK);
			graphics2D.fillRect(0, 0, width, height);
			graphics2D.setColor(oldColor);
			graphics2D.drawImage(a.bufferedImage, null, 0, (int) a.margin);
			graphics2D.drawImage(b.bufferedImage, null, a.bufferedImage.getWidth(), (int) b.margin);
			graphics2D.dispose();
		}
		return bufferedImage;
	}
//=======
//        String string = null;
//        if (data != null && data.size() > 0) {
//            string = "\n";
//            for (int i = 0; i < data.size(); i++) {
//                for (int j = 0; j < data.get(i).size(); j++) {
//                    if (data.get(i).get(j) != null) {
//                        string += "*";
//                    }
//                }
//                if (i < (data.size() - 1)) {
//                    string += "\n";
//                }
//            }
//        }
//        if (string != null) {
//            logger.info(string);
//        }
//    }
//
//    @JsonIgnore
//    public boolean isShapeListYInThreshold(List<Shape> shapeList, int averageY, int threshold) {
//        logger.debug("isShapeListYInThreshold(" + shapeList + ", " + averageY + ", " + threshold + ")");
//        boolean flag = true;
//        double a = 0;
//        for (Shape shape : shapeList) {
//            a = shape.getCenterY();
//            a = Math.abs(averageY - a);
//            if (a > threshold) {
//                flag = false;
//                break;
//            }
//        }
//        return flag;
//    }
//
//    @JsonIgnore
//    public double getFileListMinMargin() {
//        double min = 65536;
//        for (File file : this.getFileList()) {
//            if (file.margin < min) {
//                min = file.margin;
//            }
//        }
//        return min;
//    }
//
//    @JsonIgnore
//    public double getFileListMaxMargin() {
//        double max = -65536;
//        for (File file : this.getFileList()) {
//            if (file.margin > max) {
//                max = file.margin;
//            }
//        }
//        return max;
//    }
//
//    @JsonIgnore
//    public int getShapeListYAverage(List<Shape> shapeList, Shape rectangle) {
//        logger.debug("getShapeListYAverage(" + shapeList + ", " + rectangle + ")");
//        int count = 0;
//        int sum = 0;
//        for (Shape r : shapeList) {
//            sum += r.getCenterY();
//            count += 1;
//        }
//        count += 1;
//        sum += rectangle.getCenterY();
//        return sum / count;
//    }
//
//    @JsonIgnore
//    public List<List<Shape>> sortRowList(List<List<Shape>> rowList) {
//        for (int i = 0; i < rowList.size(); i++) {
//            this.sortColumnList(rowList.get(i));
//            for (int j = rowList.size() - 1; j > i; j--) {
//                if (rowList.get(i).get(0).getCenterY() > rowList.get(j).get(0).getCenterY()) {
//                    List<Shape> tmp = rowList.get(i);
//                    rowList.set(i, rowList.get(j));
//                    rowList.set(j, tmp);
//                }
//            }
//        }
//        return rowList;
//    }
//
//    @JsonIgnore
//    public List<Shape> sortColumnList(List<Shape> shapeList) {
//        for (int i = 0; i < shapeList.size(); i++) {
//            for (int j = shapeList.size() - 1; j > i; j--) {
//                if (shapeList.get(i).pointList.get(0).x > shapeList.get(j).pointList.get(0).x) {
//                    Shape tmp = shapeList.get(i);
//                    shapeList.set(i, shapeList.get(j));
//                    shapeList.set(j, tmp);
//                }
//            }
//        }
//        return shapeList;
//    }
//
//    @JsonIgnore
//    public boolean columnListContains(List<Shape> shapeList, Shape shape) {
//        boolean flag = false;
//        for (Shape s : shapeList) {
//            if (s.uuid.equals(shape.uuid)) {
//                flag = true;
//            }
//        }
//        return flag;
//    }
//
//    @JsonIgnore
//    public boolean rowListContains(List<List<Shape>> shapeList, Shape shape) {
//        boolean flag = false;
//        for (List<Shape> s : shapeList) {
//            flag = this.columnListContains(s, shape);
//            if (flag) {
//                break;
//            }
//        }
//        return flag;
//    }
//
//    @JsonIgnore
//    public BufferedImage modFile(File a) { // BufferedImage img1,BufferedImage img2) {
//        logger.debug("modFile(" + a + ")");
//        BufferedImage bufferedImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
//        if (a.bufferedImage != null) {
//            int width = a.bufferedImage.getWidth();
//            int height = a.bufferedImage.getHeight();
//            bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
//            Graphics2D graphics2D = bufferedImage.createGraphics();
//            Color oldColor = graphics2D.getColor();
//            graphics2D.setPaint(Color.BLACK);
//            graphics2D.fillRect(0, 0, width, height);
//            graphics2D.setColor(oldColor);
//            graphics2D.drawImage(a.bufferedImage, null, 0, (int) a.margin);
//            graphics2D.dispose();
//        }
//        return bufferedImage;
//    }
//
//    @JsonIgnore
//    public BufferedImage joinFile(File a, File b) {
//        logger.debug("joinFiles(" + a + "," + b + ")");
//        BufferedImage bufferedImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
//        if (a.bufferedImage != null && b.bufferedImage != null) {
//            int width = a.bufferedImage.getWidth() + b.bufferedImage.getWidth();
//            int height = Math.max(a.bufferedImage.getHeight() + (int) a.margin,
//                    b.bufferedImage.getHeight() + (int) b.margin);// +offset;
//            bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
//            Graphics2D graphics2D = bufferedImage.createGraphics();
//            Color oldColor = graphics2D.getColor();
//            graphics2D.setPaint(Color.BLACK);
//            graphics2D.fillRect(0, 0, width, height);
//            graphics2D.setColor(oldColor);
//            graphics2D.drawImage(a.bufferedImage, null, 0, (int) a.margin);
//            graphics2D.drawImage(b.bufferedImage, null, a.bufferedImage.getWidth(), (int) b.margin);
//            graphics2D.dispose();
//        }
//        return bufferedImage;
//    }
//>>>>>>> feature/vision

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

//@JsonIgnore
//public void setBufferedImage() {
//	File bufferedImageFile = null;
//	for(File file: this.getFileList()) {	
//		if(bufferedImageFile == null) {
//			bufferedImageFile = file;
//		} else {
//			bufferedImageFile.setBufferedImage(this.joinFile(bufferedImageFile, file));
//		}
//	}
//	this.bufferedImage = bufferedImageFile.getBufferedImage();
//}
//@JsonIgnore
//public BufferedImage getBufferedImage() {
//	if(this.bufferedImage == null)
//		this.setBufferedImage();
//	return this.bufferedImage;
//}
//
//@JsonIgnore
//public void setBufferedImage() {
//	File bufferedImageFile = null;
//	for(File file: this.getFileList()) {	
//		if(bufferedImageFile == null) {
//			bufferedImageFile = file;
//		} else {
//			bufferedImageFile.setBufferedImage(this.joinFile(bufferedImageFile, file));
//		}
//	}
//	this.bufferedImage = bufferedImageFile.getBufferedImage();
//}
//@JsonIgnore
//public void addShape(Shape shape) {
//	logger.debug("addShape(" + shape + ")");
//	File file = this.getFile();
//	if (file != null) {
//		file.addShape(shape);
//		this.setShape(shape.uuid);
//	}
//}
//@JsonIgnore
//public BufferedImage getShapeImage(Shape shape) {
//  BufferedImage bufferedImage = null;
//  if (this.getBufferedImage() != null) {
////        bufferedImage = this.getBufferedImage().getSubimage(shape.getX(), shape.getX(), (shape.getI() - shape.getX()),
////                (shape.getJ() - shape.getY()));
//  }
//  return bufferedImage;
//}
//bufferedImage = new BufferedImage((int) shape.getDimension().w, (int) shape.getDimension().h,
//BufferedImage.TYPE_INT_RGB);
//Graphics2D g2 = bufferedImage.createGraphics();
//if (shape.classification.equals(Shape.ELLIPSE)) {
//Ellipse2D.Double ellipse = new Ellipse2D.Double(shape.getDimension().x, shape.getDimension().y,
//	shape.getDimension().w, shape.getDimension().h);
//g2.setClip(ellipse);
//} else if (shape.classification.equals(Shape.RECTANGLE)) {
//Rectangle2D.Double rectangle = new Rectangle2D.Double(shape.getDimension().x, shape.getDimension().y,
//	shape.getDimension().w, shape.getDimension().h);
//g2.setClip(rectangle);
//}
//g2.drawImage(this.getBufferedImage(), 0, 0, null);
//g2.dispose();
//    @JsonIgnore
//    public List<LinkedList<Data>> initMatrix() {
//        logger.debug("initMatrix()");
//        List<LinkedList<Data>> dataMatrix = null;
//        List<Shape> shapeList = this.getShapeList();
//        if (shapeList != null && shapeList.size() > 0) {
//            logger.debug("getDataMatrix() shapeList = " + shapeList);
//            Shape shape = null;
//            int averageY;
//            int threshold = 64;
//            List<Shape> columnList;
//            List<List<Shape>> rowList = new ArrayList<>();
//            boolean flag = true;
//            for (int i = 0; i < shapeList.size(); i++) {
//                shape = shapeList.get(i);
//                columnList = new ArrayList<>();
//                if (!rowList.isEmpty()) {
//                    for (List<Shape> cList : rowList) {
//                        averageY = this.getShapeListYAverage(cList, shape);
//                        columnList = new ArrayList<>(cList);
//                        columnList.add(shape);
//                        if (this.isShapeListYInThreshold(columnList, averageY, threshold)) {
//                            cList.add(shape);
//                            flag = false;
//                        }
//                    }
//                    if (flag) {
//                        columnList = new ArrayList<>();
//                        columnList.add(shape);
//                        rowList.add(columnList);
//                    } else {
//                        flag = true;
//                    }
//                } else {
//                    columnList.add(shape);
//                    rowList.add(columnList);
//                }
//            }
//            int row = rowList.size();
//            int columnMax = 0;
//            int size = 0;
//            for (List<Shape> cList : rowList) {
//                size = cList.size();
//                if (size > columnMax) {
//                    columnMax = size;
//                }
//            }
//            rowList = this.sortRowList(rowList);
//            dataMatrix = new LinkedList<>();
//            for (int i = 0; i < row; i++) {
//                dataMatrix.add(i, new LinkedList<Data>());
//                for (int j = 0; j < rowList.get(i).size(); j++) {
//                    shape = rowList.get(i).get(j);
//                    if (shape.data != null) {
//                        dataMatrix.get(i).add(j, shape.data);
//                    }
//                }
//            }
//            this.printDataMatrix(dataMatrix);
//        }
//        return dataMatrix;
//    }

//@JsonIgnore
//public List<ArrayList<Shape>> initShapeMatrix() {
//  logger.info("initShapeMatrix()");
//  List<ArrayList<Shape>> dataMatrix = null;
//  List<Shape> shapeList = this.getShapeList();
//  if (shapeList != null && shapeList.size() > 0) {
//      logger.debug("initMatrix() shapeList = " + shapeList);
//      Shape shape = null;
//      int averageY;
//      int threshold = 16;
//      List<Shape> columnList;
//      List<List<Shape>> rowList = new ArrayList<>();
//      boolean flag = true;
//      for (int i = 0; i < shapeList.size(); i++) {
//      	//Set shape
//          shape = shapeList.get(i);
//          //Init new columnList, has shapes added to it
//          columnList = new ArrayList<>();
//          if (!rowList.isEmpty()) {
//              for (List<Shape> cList : rowList) {
//                  averageY = this.getShapeListYAverage(cList, shape);
//                  columnList = new ArrayList<>(cList);
//                  columnList.add(shape);
//                  if (this.isShapeListYInThreshold(columnList, averageY, threshold)) {
//                      cList.add(shape);
//                      flag = false;
//                  }
//              }
//              if (flag) {
//                  columnList = new ArrayList<>();
//                  columnList.add(shape);
//                  rowList.add(columnList);
//              } else {
//                  flag = true;
//              }
//          } else {
//          	logger.info("initShapeMatrix() first");
//              columnList.add(shape);
//              rowList.add(columnList);
//          }
//      }
//      int row = rowList.size();
//      int columnMax = 0;
//      int size = 0;
//      for (List<Shape> cList : rowList) {
//          size = cList.size();
//          if (size > columnMax) {
//              columnMax = size;
//          }
//      }
//      rowList = this.sortRowList(rowList);
//      dataMatrix = new LinkedList<>();
//      for (int i = 0; i < row; i++) {
//          dataMatrix.add(i, new ArrayList<Shape>());
//          for (int j = 0; j < rowList.get(i).size(); j++) {
//              shape = rowList.get(i).get(j);
//              if (shape.data != null) {
//                  dataMatrix.get(i).add(j, shape);
//              }
//          }
//      }
//      this.printMatrix(dataMatrix);
//  }
//  return dataMatrix;
//}


//@JsonIgnore
//private void sortAscendingList(List<Shape> list) {
//    Collections.sort(list, new Comparator<Shape>() {
//        public int compare(Shape ideaVal1, Shape ideaVal2) {
//        	Double idea1 = ideaVal1.rank;
//        	Double idea2 = ideaVal2.rank;
//            return idea1.compareTo(idea2);
//        }
//    });
//}
//
//@JsonIgnore
//private void sortDescendingList(List<Concept> list) {
//    Collections.sort(list, new Comparator<Concept>() {
//        public int compare(Concept ideaVal1, Concept ideaVal2) {
//        	Double idea1 = ideaVal1.rank;
//        	Double idea2 = ideaVal2.rank;
//            return idea2.compareTo(idea1);
//        }
//    });
//}
