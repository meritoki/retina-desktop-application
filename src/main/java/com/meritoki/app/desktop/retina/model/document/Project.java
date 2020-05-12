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

import java.io.IOException;
import java.io.Serializable;
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

public class Project implements Serializable {

	private static final long serialVersionUID = 1L;

	static Logger logger = LogManager.getLogger(Project.class.getName());
	@JsonProperty
	public String uuid = "";
	@JsonProperty
	public List<Page> pageList = new ArrayList<>();
	@JsonIgnore
	public int index = 0;
	@JsonIgnore
	public File file = new File();
	@JsonProperty
	public List<Layout> layoutList = new ArrayList<>();
	@JsonIgnore
	public Document document = null;

	public Project() {
		this.uuid = UUID.randomUUID().toString();
	}
	
	public Project(Document document) {
		this.uuid = UUID.randomUUID().toString();
		this.document = document;
	}

	public Project(Project project) {
		this.uuid = project.uuid;
		for (Page p : project.pageList) {
			this.pageList.add(new Page(p));
		}
	}

//	@JsonIgnore
//	public void test() {
//		Page page = new Page();
//		File file = new File("./data/image", "01.jpg");
//		page.fileList.add(file);
//		file = new File("./data/image","02.jpg");
//		page.fileList.add(file);
//		pageList.add(page);
//		page = new Page();
//		file = new File("./data/image","02.jpg");
//		page.fileList.add(file);
//		pageList.add(page);
//		page = new Page();
//		file = new File("./data/image","03.jpg");
//		page.fileList.add(file);
//		pageList.add(page);
//		page = new Page();
//		file = new File("./data/image","04.jpg");
//		page.fileList.add(file);
//		pageList.add(page);
//		page = new Page();
//		file = new File("./data/image","05.jpg");
//		page.fileList.add(file);
//		pageList.add(page);
//		page = new Page();
//		file = new File("./data/image","06.jpg");
//		page.fileList.add(file);
//		pageList.add(page);
//		page = new Page();
//		file = new File("./data/image","07.jpg");
//		page.fileList.add(file);
//		pageList.add(page);
//		page = new Page();
//		file = new File("./data/image","08.jpg");
//		page.fileList.add(file);
//		pageList.add(page);
//		this.setIndex(0);
//	}

	/**
	 * Get the index of the current Page, used by Dialogs
	 * 
	 * @return
	 */
	@JsonIgnore
	public int getIndex() {
		logger.debug("getIndex() this.index=" + this.index);
		return this.index;
	}

	/**
	 * Functions gets Page object at current index from Page List
	 * 
	 * @return Page
	 */
	@JsonIgnore
	public Page getPage() {
		return (this.pageList.size() > 0) ? this.pageList.get(index) : new Page();
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
	public void setIndex(int index) {
		logger.debug("setIndex(" + index + ")");
		if (index >= 0 && index < this.pageList.size()) {
			this.index = index;
		}
	}

	@JsonIgnore
	public void setPage(String uuid) {
		logger.info("setPage(" + uuid + ")");
		Page page = null;
		for (int i = 0; i < this.pageList.size(); i++) {
			page = this.pageList.get(i);
			if (page.uuid.equals(uuid)) {
				this.setIndex(i);
				;
				break;
			}
		}
	}

	@JsonIgnore
	public void setPageList(List<Page> pageList) {
		logger.info("setPageList(" + pageList + ")");
		this.pageList = pageList;
	}

	@JsonIgnore
	public void addPage(Page page) {
		logger.info("addPage(" + page + ")");
		this.pageList.add(page);
	}
	
	public List<Shape> getShapeList() {
		List<Shape> shapeList = new ArrayList<>();
		for(Page page: this.pageList) {
			shapeList.addAll(page.getShapeList());
		}
		return shapeList;
	}

	@JsonIgnore
	@Override
	public String toString() {
		String string = "";
//        if(logger.isTraceEnabled()){
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		try {
			string = ow.writeValueAsString(this);
		} catch (IOException ex) {
			java.util.logging.Logger.getLogger(Shape.class.getName()).log(Level.SEVERE, null, ex);
		}
//        } else {
//            string = this.uuid;
//        }
		return string;
	}
}

///**
// * Function gets current File for current Page.
// * @return
// */
//@JsonIgnore
//public File getFile() {
//	Page page = this.getPage();
//	File file = (page != null) ? page.getFile(): null;
//	return file;
//}

///**
// * Function gets File for a Point with x and y value.
// * @param point
// * @return
// */
//@JsonIgnore
//public File getFile(Point point) {
//	Page page = this.getPage();
//	File shape = (page != null) ? page.getFile(point) : null;
//	return shape;
//}

///**
// * Function gets Shape from File of current Page for a Point with an x and y value.
// * @param point
// * @return
// */
//@JsonIgnore
//public Shape getShape(Point point) {
//	logger.trace("getShape("+point+")");
//	Page page = this.getPage();
//	Shape shape = (page != null) ? page.getShape(point) : null;
//	return shape;
//}
//
///**
// * Funtion gets Shape from File of current Page based on index in current File.
// * @return
// */
//@JsonIgnore
//public Shape getShape() {
//	Page page = this.getPage();
//	File file = (page != null) ? page.getFile() : null;
//	Shape shape = (file != null) ? file.getShape() : null;
//	return shape;
//}

//@JsonIgnore
//public List<Shape> getShapeList() {
//	Page page = this.getPage();
//	List<Shape> shapeList = (page != null) ? page.getShapeList():null;
//	return shapeList;
//}
//
//@JsonIgnore
//public BufferedImage getBufferedImage() {
//	Page page = this.getPage();
//	if(page != null && page.getBufferedImage() == null) 
//		page.setBufferedImage();
//	BufferedImage bufferedImage = (page != null) ? page.getBufferedImage() : null;
//	return bufferedImage;
//}
//    
//    @JsonIgnore
//    public void setScale(double scale) {
//    	logger.debug("setScale("+scale+")");
//    	Page page = this.getPage();
//    	if(page != null) {
//    		page.setScale(scale);
//    	}
//    }
//    
//
//@JsonIgnore
//public void addShape(Shape shape) {
//	logger.debug("addShape("+shape+")");
//	Page page = this.getPage();
//	if(page != null) {
//		page.addShape(shape);
//	}
//}
//
//@JsonIgnore
//public void addFile(File file) {
//	logger.info("addFile("+file+")");
//	Page page = this.getPage();
//	if(page != null) {
//		page.addFile(file);
//	}
//}
//
//@JsonIgnore
//public boolean containsShape(Shape shape) {
//	Page page = this.getPage();
//	return true;
//}
//
//@JsonIgnore
//public int intersectShape(Point point) {
//	logger.trace("intersectShape("+point+")");
//	Page page = this.getPage();
//	int selection = page.intersectShape(point);
//	return selection;
//}
//	@JsonIgnore
//public List<File> getFileList() {
//	Page page = this.getPage();
//	List<File> fileList = (page != null) ? page.getFileList(): null;
//	return fileList;
//}
//
//@JsonIgnore
//public void setShape(String uuid) {
//	logger.trace("setShape("+uuid+")");
//	Page page = this.getPage();
//	page.setShape(uuid);
//}
//
//@JsonIgnore
//public void setFile(String uuid) {
//	logger.trace("setFile("+uuid+")");
//	Page page = this.getPage();
//	page.setFile(uuid);
//}
//