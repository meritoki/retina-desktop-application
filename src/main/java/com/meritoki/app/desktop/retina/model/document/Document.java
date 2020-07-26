/*
 * Copyright 2020 Joaquin Osvaldo Rodriguez
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
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meritoki.app.desktop.retina.controller.node.NodeController;
import com.meritoki.app.desktop.retina.model.command.Command;
import com.meritoki.app.desktop.retina.model.provider.meritoki.Output;
import com.meritoki.app.desktop.retina.model.provider.zooniverse.Annotation;
import com.meritoki.app.desktop.retina.model.provider.zooniverse.Data;
import com.meritoki.app.desktop.retina.model.provider.zooniverse.Subject;

/**
 * Document
 *
 * @author jorodriguez
 *
 */
public class Document {
	@JsonIgnore
	static Logger logger = LogManager.getLogger(Document.class.getName());
	@JsonProperty
	public String uuid;
	@JsonIgnore
	public int index = 0;
	@JsonProperty
	public List<Page> pageList = new ArrayList<>();
	@JsonProperty
	public LinkedList<Command> logStack = new LinkedList<>();

	public Document() {
		this.uuid = UUID.randomUUID().toString();
	}

	@JsonIgnore
	public Image getImage() {
		Image image = null;
		if (this.getPage() != null) {
			image = this.getPage().getImage();
		}
		logger.info("getImage() image=" + image);
		return image;
	}

	@JsonIgnore
	public Image getImage(Point point) {
		Image image = null;
		if (this.getPage() != null) {
			image = this.getPage().getImage(point);
		}
		logger.info("getImage(" + point + ") image=" + image);
		return image;
	}

	@JsonIgnore
	public Image getImage(String uuid) {
		Image image = null;
		for (Page page : this.pageList) {
			for (Image i : page.imageList) {
				if (i.uuid.equals(uuid)) {
					image = i;
					break;
				}
			}
		}
		return image;
	}

	@JsonIgnore
	public Shape getShape() {
		return getPage().getShape();
	}

	@JsonIgnore
	public Shape getShape(Point point) {
		Shape shape = null;
		if (this.getPage() != null) {
			shape = this.getPage().getShape(point);
		}
		logger.info("getShape(" + point + ") shape=" + shape);
		return shape;
	}

	@JsonIgnore
	public Shape getShape(String uuid) {
		Shape shape = null;
		for (Page p : this.pageList) {
			for (Image i : p.imageList) {
				for (Shape s : i.shapeList) {
					if (s.uuid.equals(uuid)) {
						shape = s;
						break;
					}
				}
			}
		}
		return shape;
	}

	/**
	 * Get the index of the current Page, used by Dialogs
	 * 
	 * @return
	 */
	@JsonIgnore
	public int getIndex() {
		return this.index;
	}

	@JsonIgnore
	public int getIndex(String uuid) {
		int index = 0;
		Page page;
		for (int i = 0; i < this.pageList.size(); i++) {
			page = this.pageList.get(i);
			if (page.uuid.equals(uuid)) {
				index = i;
				break;
			}
		}
		return index;
	}

	/**
	 * Functions gets Page object at current index from Page List
	 * 
	 * @return Page
	 */
	@JsonIgnore
	public Page getPage() {
		int size = this.pageList.size();
		Page page = (this.index < size && size > 0) ? this.pageList.get(this.index) : null;
//		this.setBufferedImage(page);
		return page;
	}

	@JsonIgnore
	public Page getPage(int index) {
		int size = this.pageList.size();
		Page page = (index < size && size > 0) ? this.pageList.get(index) : null;
		logger.debug("getPage(" + index + ") page=" + page);
		return page;
	}

	@JsonIgnore
	public Page getPage(String uuid) {
		Page page = null;
		for (Page p : this.pageList) {
			if (p.uuid.equals(uuid)) {
				page = p;
				break;
			}
		}
		return page;
	}

	/**
	 * Function get reference to Page List
	 * 
	 * @return List<Page>
	 */
	@JsonIgnore
	public List<Page> getPageList() {
		return this.pageList;
	}

	@JsonIgnore
	public boolean setIndex(int index) {
		boolean flag = false;
		if (index >= 0 && index < this.pageList.size()) {
			this.index = index;
			flag = true;
		}
		return flag;
	}

	@JsonIgnore
	public void setPage(String uuid) {
		logger.info("setPage(" + uuid + ")");
		if (uuid != null) {
			Page page = null;
			for (int i = 0; i < this.pageList.size(); i++) {
				page = this.pageList.get(i);
				if (page.uuid.equals(uuid)) {
					this.setIndex(i);
				} else {
					page.setBufferedImageNull();
				}
			}
		}
	}

	@JsonIgnore
	public void setPageList(List<Page> pageList) {
		logger.info("setPageList(" + pageList + ")");
		this.pageList = pageList;
	}

	@JsonIgnore
	public void setImage(int index) {
		logger.debug("setImage(" + index + ")");
		if (this.getPage() != null) {
			this.getPage().setIndex(index);
		}
	}

	@JsonIgnore
	public void setImage(String uuid) {
		logger.info("setImage(" + uuid + ")");
		if (this.getPage() != null) {
			this.getPage().setImage(uuid);
		}
	}

	@JsonIgnore
	public void addPage(Page page) {
		logger.info("addPage(" + page + ")");
//		this.setBufferedImage(page);
		this.pageList.add(page);
	}

	@JsonIgnore
	public void addShape(Shape shape) {
		logger.info("addShape(" + shape + ")");
		Page page = this.getPage();
		if (page != null) {
			page.addShape(shape);
		}
	}

	@JsonIgnore
	public List<Shape> getShapeList(boolean flag) {
		List<Shape> shapeList = new ArrayList<>();
		for (Page page : this.pageList) {
			shapeList.addAll(page.getShapeList());
		}
		return shapeList;
	}

	@JsonIgnore
	public List<Shape> getGridShapeList(boolean flag) {
		List<Shape> shapeList = new ArrayList<>();
		for (Page page : this.pageList) {
//			if(flag)
//				this.setBufferedImage(page);
			shapeList.addAll(page.getGridShapeList());
			page.setBufferedImageNull();
		}
		return shapeList;
	}

//	@JsonIgnore
//	public List<Shape> getCompleteShapeList() {
//		List<Shape> shapeList = new ArrayList<>();
//		for (Page page : this.pageList) {
//			shapeList.addAll(page.getGridShapeList());
//		}
//		return shapeList;
//	}

	@JsonIgnore
	public boolean importZooniverse(String fileName) {
		List<String[]> stringArrayList = NodeController.openCsv(fileName);
		logger.info("importZooniverse(" + fileName + ")");
		boolean flag = false;
		for (int i = 1; i < stringArrayList.size(); i++) {
			String[] stringArray = stringArrayList.get(i);
			logger.info("importZooniverse(" + fileName + ") stringArray.length=" + stringArray.length);
			List<String> valueList = new ArrayList<>();
			String uuid = null;
			String annotationString = stringArray[11];
			annotationString = annotationString.replace("\"\"", "\"");
			annotationString = annotationString.replaceFirst("\"", "");
			int length = annotationString.length();
			annotationString = annotationString.substring(0, length - 1);
			ObjectMapper objectMapper = new ObjectMapper();
			try {
				List<Annotation> annotationList = objectMapper.readValue(annotationString,
						new TypeReference<List<Annotation>>() {
						});
				for (Annotation a : annotationList) {
					valueList.add(a.value);
				}
			} catch (JsonParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String subjectString = stringArray[12];
			subjectString = subjectString.replace("\"\"", "\"");
			subjectString = subjectString.replaceFirst("\"", "");
			length = subjectString.length();
			subjectString = subjectString.substring(0, length - 1);
			try {
				Subject subject = objectMapper.readValue(subjectString, Subject.class);
				Data data = (Data) subject.dataMap.firstEntry().getValue();
				uuid = data.getUUID();
			} catch (JsonParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (uuid != null && valueList.size() > 0) {
				logger.info("importZooniverse(" + fileName + ") valueList=" + valueList + " uuid=" + uuid);

				for (Shape shape : this.getGridShapeList(false)) {
					if (uuid.equals(shape.uuid)) {
						for (String value : valueList) {
							Text text = new Text();
							text.value = value;
							shape.addText(text);
							flag = true;
						}
						shape.getData().setText(shape.getDefaultText());
					}
				}

			} else {
				logger.error("importZooniverse(" + fileName + ") valueList=" + valueList + " uuid=" + uuid);
			}
		}
		logger.error("importZooniverse(" + fileName + ") flag=" + flag);
		return flag;
	}

	@JsonIgnore
	public void importOutputList(List<Output> outputList) {
		for (Shape s : this.getShapeList(false)) {
			for (Output o : outputList) {
				if (s.uuid.equals(o.shape.uuid)) {
					s.textList.add(new Text(o.concept));
				}
			}
		}
	}

	@JsonIgnore
	public Page removePage(String uuid) {
		ListIterator<Page> pageListIterator = this.pageList.listIterator();
		while (pageListIterator.hasNext()) {
			Page page = pageListIterator.next();
			if (page.uuid.equals(uuid)) {
				pageListIterator.remove();
				return page;
			}
		}
		return null;
	}

	@JsonIgnore
	public void pagePaint(Graphics2D graphics2D) {
		AffineTransform affineTransform = new AffineTransform();
		Page page = this.getPage();
		if (page != null) {
			affineTransform.scale(page.position.scale, page.position.scale);// this handles scaling the bufferedImage
			BufferedImage bufferedImage = page.getBufferedImage(this.uuid);
			if (bufferedImage != null) {
				graphics2D.drawImage(bufferedImage, affineTransform, null);
			}
			List<Image> imageList = page.getImageList();
			Image image = this.getImage();
			if (imageList != null) {
				for (Image i : imageList) {
					Position p = i.position;
					if (image != null && i.uuid.equals(image.uuid)) {
						graphics2D.setColor(Color.RED);
					} else {
						graphics2D.setColor(Color.YELLOW);
					}
					Rectangle2D.Double rectangle = new Rectangle2D.Double(p.point.x, p.point.y, p.dimension.width,
							p.dimension.height);
					graphics2D.draw(rectangle);
				}

			}
			List<Shape> shapeList = page.getSortedShapeList();// this.getMatrix().getShapeList();
			Shape shape = page.getShape();
			Shape gridShape = page.getGridShape();
			Shape previousShape = null;
			if (shapeList != null) {
				for (Shape s : shapeList) {
					Position position = s.position;
					if (shape != null && s.uuid.equals(shape.uuid)) {
						graphics2D.setColor(Color.RED);
					} else {
						graphics2D.setColor(Color.BLUE);
					}
					switch (s.type) {
					case ELLIPSE: {
						Ellipse2D.Double ellipse = new Ellipse2D.Double(position.point.x, position.point.y,
								position.dimension.width, position.dimension.height);
						graphics2D.draw(ellipse);
						break;
					}
					case RECTANGLE: {
						Rectangle2D.Double rectangle = new Rectangle2D.Double(position.point.x, position.point.y,
								position.dimension.width, position.dimension.height);
						graphics2D.draw(rectangle);
						if (s instanceof Grid) {
							Shape[][] matrix = ((Grid) s).matrix;
							for (int i = 0; i < matrix.length; i++) {
								for (int j = 0; j < matrix[i].length; j++) {
									Shape gs = matrix[i][j];
									if (gridShape != null && gs.uuid.equals(gridShape.uuid)) {
										graphics2D.setColor(Color.YELLOW);
									} else {
										if (shape != null && s.uuid.equals(shape.uuid)) {
											graphics2D.setColor(Color.RED);
										} else {
											graphics2D.setColor(Color.BLUE);
										}
									}
									rectangle = new Rectangle2D.Double(gs.position.point.x, gs.position.point.y,
											gs.position.dimension.width, gs.position.dimension.height);
									graphics2D.draw(rectangle);
								}
							}
						}
						break;
					}
					}
					if (previousShape != null) {
						Position p = previousShape.position;
						graphics2D.drawLine((int) (p.center.x), (int) (p.center.y), (int) (position.center.x),
								(int) (position.center.y));
					}
					previousShape = s;
				}
			}
			graphics2D.setColor(Color.BLUE);
			Rectangle2D.Double frame = new Rectangle2D.Double(0, 0, page.position.dimension.width,
					page.position.dimension.height);
			graphics2D.draw(frame);
		}
	}
	
	@JsonIgnore
	public void shapePaint(Graphics2D graphics2D) {
		Page page = this.getPage();
		Shape shape = (page != null) ? page.getShape(): null;
		if (shape != null) {
			if(shape instanceof Grid) {
				Grid grid = (Grid)shape;
				shape = grid.getShape();
			}
			AffineTransform affineTransform = new AffineTransform();
			affineTransform.scale(1, 1);
			BufferedImage bufferedImage = this.getShapeBufferedImage(page.getScaledBufferedImage(this.uuid), shape);
			if (bufferedImage != null) {
				graphics2D.drawImage(bufferedImage, affineTransform, null);
			}
		}
	}
	
	@JsonIgnore
	public BufferedImage getShapeBufferedImage(BufferedImage bufferedImage, Shape shape) {
		logger.debug("getShapeBufferedImage("+shape+")");
		BufferedImage shapeBufferedImage = null;
		if (bufferedImage != null) {
			if(shape.position.point.x >= 0 && shape.position.point.y >= 0 && (int)shape.position.dimension.height > 0 && (int)shape.position.dimension.width > 0) {
				shapeBufferedImage = bufferedImage.getSubimage((int)shape.position.point.x, (int)shape.position.point.y, (int)shape.position.dimension.width, (int)shape.position.dimension.height);
			} else {
				logger.error("MAJOR ERROR THAT NEEDS TO BE FIXED shape.position="+shape.position);
			}
		}
		return shapeBufferedImage;
	}

//	@JsonIgnore
//	public void setBufferedImage(Page page) {
////		logger.info("setBufferedImage("+page+")");
//		if (page != null) {
//			List<Image> imageList = page.getImageList();
//			for (Image image : imageList) {
//				this.setBufferedImage(image);
//			}
//			if(page.getBufferedImage()== null) {
//				logger.error("setBufferedImage("+page+") bufferedImage==null");
//			}
//		}
//	}
//	
//	@JsonIgnore
//	public void setBufferedImage(Image image) {
//		if (image.getBufferedImage() == null) {
////			logger.info("setBufferedImage("+image+") image.getBufferedImage()==null");
//			image.setBufferedImage(this.getBufferedImage(image));
//		}
//	}
//
//	@JsonIgnore
//	public BufferedImage getBufferedImage(Image image) {
//		BufferedImage bufferedImage = NodeController.openBufferedImage(NodeController.getDocumentCache(this.uuid), image.uuid + "." + image.getExtension());
//		if (bufferedImage == null) {
//			logger.info("getBufferedImage("+image+") bufferedImage==null");
//			if(image.file == null) {
//				File file = new File(image.filePath+NodeController.getSeperator()+image.fileName);
//				image.file = file;
//			}
//			if (image.file != null) {
//				bufferedImage = NodeController.openBufferedImage(image.file);
//				if (bufferedImage != null) {
//					if (image.getExtension().equals("jpg") || image.getExtension().equals("jpeg")) {
//						File documentCache = new File(NodeController.getDocumentCache(this.uuid));
//						if(!documentCache.exists()) {
//							documentCache.mkdirs();
//						}
//						try {
//							NodeController.saveJpg(NodeController.getDocumentCache(this.uuid),
//									image.uuid + "." + image.getExtension(), bufferedImage);
//						} catch (Exception e) {
//							logger.error("Exception "+e.getMessage());
//						}
//					}
//				}
//			}
//		}
//		BufferedImage before = bufferedImage;
//		int w = before.getWidth();
//		int h = before.getHeight();
//		BufferedImage after = new BufferedImage((int) (w * image.position.relativeScale),
//				(int) (h * image.position.relativeScale), BufferedImage.TYPE_INT_ARGB);
//		AffineTransform at = new AffineTransform();
//		at.scale(image.position.relativeScale, image.position.relativeScale);
//		AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
//		after = scaleOp.filter(before, after);
//		bufferedImage = after;
//		image.position.setAbsoluteDimension(new Dimension(bufferedImage.getWidth(), bufferedImage.getHeight()));
//		return bufferedImage;
//	}

	@JsonIgnore
	@Override
	public String toString() {
		String string = "";
		string = "{\"uuid\":" + this.uuid + "}";
		return string;
	}

//	@JsonIgnore
//	@Override
//	public String toString() {
//		String string = "";
//		ObjectWriter ow = new ObjectMapper().writer();//.withDefaultPrettyPrinter();
//		try {
//			string = ow.writeValueAsString(this);
//		} catch (IOException ex) {
//			logger.error("IOException " + ex.getMessage());
//		}
//		return string;
//	}
}
