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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import com.meritoki.app.desktop.retina.model.document.command.Pattern;
import com.meritoki.app.desktop.retina.model.document.user.User;

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
	public String uuid = null;
	@JsonIgnore
	public int index = 0;
	@JsonProperty
	public List<Page> pageList = new ArrayList<>();
	@JsonProperty
	public List<User> userList = new LinkedList<>();
	@JsonIgnore
	public Pattern pattern;
	@JsonIgnore
	public Cache cache = new Cache();

	public Document() {
		this.uuid = UUID.randomUUID().toString();
		this.pattern = new Pattern(this);
//		this.test();
	}

	@JsonIgnore
	public void test() {
		Page page = new Page();
		page.addImage(new Image(new File("./data/image/01.jpg")));
		page.addImage(new Image(new File("./data/image/02.jpg")));
		this.addPage(page);
		page = new Page(new Image(new File("./data/image/03.jpg")));
		this.addPage(page);
		page = new Page(new Image(new File("./data/image/04.jpg")));
		this.addPage(page);
	}

	@JsonIgnore
	public Image getImage() {
		Image image = null;
		if (this.getPage() != null) {
			image = this.getPage().getImage();
		}
		logger.info("getImage() image="+image);
		return image;
	}

	@JsonIgnore
	public Image getImage(Point point) {
		logger.info("getImage("+point+")");
		Image image = null;
		if (this.getPage() != null) {
			image = this.getPage().getImage(point);
		}
		return image;
	}

	@JsonIgnore
	public void setImage(String uuid) {
		if (this.getPage() != null) {
			this.getPage().setImage(uuid);
		}
	}

	@JsonIgnore
	public Shape getShape(Point point) {
		Shape shape = null;
		if (this.getPage() != null) {
			shape = this.getPage().getShape(point);
		}
		logger.info("getShape("+point+") shape="+shape);
		return shape;
	}

	@JsonIgnore
	public void addShape(Shape shape) {
		if (this.getPage() != null) {
			this.getPage().addShape(shape);
		}
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

	/**
	 * Functions gets Page object at current index from Page List
	 * 
	 * @return Page
	 */
	@JsonIgnore
	public Page getPage() {
		int size = this.pageList.size();
		return (this.index < size && size > 0) ? this.pageList.get(this.index) : null;
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
					;
					break;
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
	public void addPage(Page page) {
		logger.info("addPage(" + page + ")");
		this.pageList.add(page);
	}

	@JsonIgnore
	public List<Shape> getShapeList() {
		List<Shape> shapeList = new ArrayList<>();
		for (Page page : this.pageList) {
			shapeList.addAll(page.getShapeList());
		}
		return shapeList;
	}

	@JsonIgnore
	public boolean importText(List<String[]> stringArrayList) {
		logger.info("importText(...)");
		boolean flag = false;
		for (int i = 0; i < stringArrayList.size(); i++) {
			String[] stringArray = stringArrayList.get(i);
			String value = null;
			for (String string : stringArray) {
				if (string.contains("value")) {
					value = string.split(":")[1].replace("\"", "");
				}
			}
			String uuid = null;
			for (String string : stringArray) {
				if (string.contains("the_image")) {
					uuid = string.split(":")[1].replace(".jpg", "").replace("}", "").replace("\"", "");
				}
			}
			System.out.println(value + " " + uuid);
			if (uuid != null && value != null) {
				for (Page page : this.getPageList()) {
					for (Shape shape : page.getShapeList()) {
						if (uuid.equals(shape.uuid)) {
							Text text = new Text();
							text.value = value;
							shape.addText(text);
							flag = true;
						}
					}
				}
			}
		}
		return flag;
	}

	@JsonIgnore
	public void addUser(User user) {
		this.userList.add(user);
	}

	@JsonIgnore
	public boolean containsUser(User user) {
		boolean flag = false;
		for (User u : this.userList) {
			if (u.uuid.equals(user.uuid)) {
				flag = true;
				break;
			}
		}
		return flag;
	}

	@JsonIgnore
	public boolean removeUser(User user) {
		boolean flag = false;
		for (int i = 0; i < this.userList.size(); i++) {
			User u = this.userList.get(i);
			if (u.uuid.equals(user.uuid)) {
				flag = true;
				this.userList.remove(i);
				break;
			}
		}
		return flag;
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
	@Override
	public String toString() {
		String string = "";
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		try {
			string = ow.writeValueAsString(this);
		} catch (IOException ex) {
			logger.error("IOException " + ex.getMessage());
		}
		return string;
	}
}
