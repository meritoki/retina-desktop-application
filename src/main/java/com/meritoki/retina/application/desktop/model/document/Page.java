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
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;

import com.meritoki.retina.application.desktop.controller.client.ClientController;
import com.meritoki.retina.application.desktop.controller.node.NodeController;
import com.meritoki.retina.application.desktop.controller.script.ScriptController;

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
	 * List of Files loaded by user.
	 */
	@JsonProperty
	public List<File> fileList = new ArrayList<File>();
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
		for (File file : page.fileList) {
			this.fileList.add(new File(file));
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
	public File getFile() {
		File file = null;
		List<File> fileList = this.getFileList();
		if (this.index >= 0 && this.index < fileList.size()) {
			file = fileList.get(this.index);
		}
		return file;
	}

	/**
	 * Function returns file that contains the point parameter
	 *
	 * @param Point
	 * @return File
	 */
	@JsonIgnore
	public File getFile(Point point) {
		logger.info("getFile(" + point + ")");
		File f = null;
		for (File file : this.getFileList()) {
			f = file;
			if (f.containsPoint(point)) {
				break;
			} else {
				f = null;
			}
		}
		if (f != null) {
			logger.info("getFile(" + point + ") file.uuid=" + f.uuid);
		}
		return f;
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
		for (File file : this.getFileList()) {
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
		File file = this.getFile();
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
		Dimension dimension = null;
		for (File file : this.getFileList()) {
			for (Shape shape : file.getShapeList()) {
				shape.setScale(file.scale);
				shape.setDimension(null);
				dimension = shape.getDimension();
				dimension.x += (file.getOffset() * file.scale);
				dimension.y += (file.getMargin() * file.scale);
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
			int w = (int) (shape.getDimension().w / this.scale);
			int h = (int) (shape.getDimension().h / this.scale);
			bufferedImage = this.getBufferedImage().getSubimage(x, y, w, h);
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
	public List<File> getFileList() {
		double offset = 0;
		for (File file : this.fileList) {
			if (file.getBufferedImage() == null) {
				BufferedImage bufferedImage = NodeController.openBufferedImage(NodeController.getImageCache(),
						file.uuid + "." + file.extension);
				if (bufferedImage == null) {
					bufferedImage = NodeController.openBufferedImage(file.getPath(), file.getNameAndExtension());
					if (bufferedImage != null) {
						file.setBufferedImage(bufferedImage);
						if (file.extension.equals("jpg") || file.extension.equals("jpeg")) {
							NodeController.saveJpg(NodeController.getImageCache(), file.uuid + "." + file.extension,
									bufferedImage);
						}
						// TODO Add support for PNG
						if (ClientController.fileClient.checkHealth()) {
							ClientController.fileClient.registerFile(file.uuid);
							if (ClientController.fileClient.checkFile(file.uuid)) {
								ClientController.fileClient.uploadFile(
										NodeController.getImageCache() + NodeController.getSeperator(),
										file.uuid + "." + file.extension);
							}
						}
					} else {
						if (ClientController.fileClient.checkHealth()) {
							if (ClientController.fileClient.checkFile(file.uuid)) {
								ClientController.fileClient.downloadFile(
										NodeController.getImageCache() + NodeController.getSeperator(),
										file.getUUID() + "." + file.getExtension());
								ClientController.fileClient.unmarkFile(file.uuid);
							} else {
								ClientController.fileClient.markFile(file.uuid);
							}
						}
					}
				} else {
					file.setBufferedImage(bufferedImage);
					if (ClientController.fileClient.checkHealth()) {
						ClientController.fileClient.registerFile(file.uuid);
						if (ClientController.fileClient.checkFile(file.uuid)) {
							ClientController.fileClient.uploadFile(
									NodeController.getImageCache() + NodeController.getSeperator(),
									file.uuid + "." + file.extension);
						}
					}
				}
			}
			file.setOffset(offset);
			file.setScale(this.scale);
			offset += file.getWidth();
		}
		return this.fileList;
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
			File file = null;
			for (File f : this.getFileList()) {
				if (file == null) {
					file = new File(f);
					bufferedImage = this.modFile(file);
				} else {
					bufferedImage = this.joinFile(file, f);
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
		if (index >= 0 && index < this.fileList.size()) {
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
		for (File file : this.fileList) {
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
		for (File file : this.getFileList()) {
			if (file.setShape(uuid)) {
				this.setFile(file.uuid);
				break;
			}
		}
	}

	@JsonIgnore
	public void setFile(String uuid) {
		logger.info("setFile(" + uuid + ")");
		File file = null;
		List<File> fileList = this.getFileList();
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
		logger.info("addShape(" + shape + ")");
		for (File f : this.getFileList()) {
			if (f.containsShape(shape)) {
				f.addShape(shape);
				this.setFile(f.uuid);
				this.setShape(shape.uuid);
				break;
			}
		}
	}

	@JsonIgnore
	public void addFile(File file) {
		logger.info("addFile(" + file + ")");
		file.setScale(this.scale);
		this.fileList.add(file);
	}

	public Shape removeShape(Shape shape) {
		logger.info("removeShape(" + shape + ")");
		Shape s = null;
		for (File file : this.getFileList()) {
			s = file.removeShape(shape.uuid);
			if (s != null) {
				break;
			}
		}
		return s;
	}

	@JsonIgnore
	public int intersectShape(Point point) {
		logger.trace("intersectShape(" + point + ")");
		int selection = -1;
		File file = this.getFile();
		if (file != null) {
			selection = file.intersectShape(point);
		}
		return selection;
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
		logger.info("initShapeMatrix()");
		List<ArrayList<Shape>> shapeMatrix = new ArrayList<>();
		List<Shape> shapeList = this.getShapeList();
		if (shapeList != null && shapeList.size() > 0) {
			logger.info("initShapeMatrix() shapeList = " + shapeList);
			Shape shape = null;
			boolean flag = true;
			for (int i = 0; i < shapeList.size(); i++) {
				// Set shape
				shape = shapeList.get(i);
				// Init new columnList, has shapes added to it
				logger.info("initShapeMatrix() shape=" + shape);
				for (List<Shape> rowList : shapeMatrix) {
					logger.info("initShapeMatrix() rowList=" + rowList);
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
//    	logger.info("printDataMatrix(...)");
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
			logger.info(string);
		}
	}

	@JsonIgnore
	public double getShapeListYAverage(List<Shape> shapeList, Shape shape) {
		double average = 0;
		int count = 0;
		double sum = 0;
		Dimension d = null;
		for (Shape s : shapeList) {
			// d = //s.dimension;
			sum += s.getCenterY();// d.y+(d.h/2);
			count += 1;
		}
		// d = shape.dimension;s
		sum += shape.getCenterY();
		count +=1;
		average = sum / count;
		logger.info("getShapeListYAverage(" + shapeList + "," + shape+ ") average=" + average);
		return average;
	}

	@JsonIgnore
	public boolean isShapeListYInThreshold(List<Shape> shapeList, Shape shape) {
//		logger.info("isShapeListYInThreshold(" + shapeList + ", " + averageY + ", " + threshold + ")");
		boolean flag = true;
		double a = 0;
		double average = this.getShapeListYAverage(shapeList, shape);
//		for (Shape s : shapeList) {
		a = shape.getCenterY();
		a = Math.abs(average - a);
		logger.info("isShapeListYInThreshold(" + shapeList + ", " + shape+ ") a=" + a);
		if (a > this.threshold) {
			logger.info("isShapeListYInThreshold(" + shapeList + ", " + shape+ ") (" + a
					+ " > " + this.threshold + ")");
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
		for (File file : this.getFileList()) {
			if (file.margin < min) {
				min = file.margin;
			}
		}
		return min;
	}

	@JsonIgnore
	public double getFileListMaxMargin() {
		double max = -65536;
		for (File file : this.getFileList()) {
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
				Double idea1 = ideaVal1.get(0).getCenterY();
				Double idea2 = ideaVal2.get(0).getCenterY();
				return idea1.compareTo(idea2);
			}
		});
	}


	@JsonIgnore
	public void sortRowList(List<Shape> shapeList) {
		Collections.sort(shapeList, new Comparator<Shape>() {
			public int compare(Shape ideaVal1, Shape ideaVal2) {
				Double idea1 = ideaVal1.getCenterX();// pointList.get(0).x;
				Double idea2 = ideaVal2.getCenterX();// pointList.get(0).x;
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
	public BufferedImage modFile(File a) { // BufferedImage img1,BufferedImage img2) {
		logger.debug("modFile(" + a + ")");
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
	public BufferedImage joinFile(File a, File b) {
		logger.debug("joinFiles(" + a + "," + b + ")");
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
