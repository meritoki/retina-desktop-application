package com.meritoki.app.desktop.retina.model.document;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;

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
	@JsonProperty
	public Pattern pattern;
	@JsonProperty
	public Cache cache = new Cache();
	@JsonProperty
	public List<Layout> layoutList = new ArrayList<>();

	public Document() {
		this.uuid = UUID.randomUUID().toString();
		this.pattern = new Pattern(this);
		this.test();
	}
	
	@JsonIgnore
	public void test() {
		Page page = new Page();
		Image image = new Image(new File("./data/image/01.jpg"));
		page.imageList.add(image);
		image = new Image(new File("./data/image/02.jpg"));
		page.imageList.add(image);
		pageList.add(page);
//		page = new Page();
//		image = new Image("./data/image","02.jpg");
//		page.imageList.add(image);
//		pageList.add(page);
//		page = new Page();
//		image = new Image("./data/image","03.jpg");
//		page.imageList.add(image);
//		pageList.add(page);
//		page = new Page();
//		image = new Image("./data/image","04.jpg");
//		page.imageList.add(image);
//		pageList.add(page);
//		page = new Page();
//		image = new Image("./data/image","05.jpg");
//		page.imageList.add(image);
//		pageList.add(page);
//		page = new Page();
//		image = new Image("./data/image","06.jpg");
//		page.imageList.add(image);
//		pageList.add(page);
//		page = new Page();
//		image = new Image("./data/image","07.jpg");
//		page.imageList.add(image);
//		pageList.add(page);
//		page = new Page();
//		image = new Image("./data/image","08.jpg");
//		page.imageList.add(image);
//		pageList.add(page);
		this.setIndex(0);
	}
	
	public Image getImage() {
		Image image = null;
		if(this.getPage() != null) {
			image = this.getPage().getImage();
		}
		return image;
	}
	
	public Image getImage(Point point) {
		Image image = null;
		if(this.getPage() != null) {
			image = this.getPage().getImage(point);
		}
		return image;
	}
	
	public void setImage(String uuid) {
		if(this.getPage() != null) {
			this.getPage().setImage(uuid);
		}
	}
	
	public Shape getShape(Point point) {
		Shape shape = null;
		if(this.getPage() != null) {
			shape = this.getPage().getShape(point);
		}
		return shape;
	}
	
	public void addShape(Shape shape) {
		if(this.getPage() != null) {
			this.getPage().addShape(shape);
		}
	}
	
//	public Selection 
	

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
		return (this.pageList.size() > 0) ? this.pageList.get(this.index) : new Page();
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
		for (Page page : this.pageList) {
			shapeList.addAll(page.getShapeList());
		}
		return shapeList;
	}

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

//@JsonIgnore
//public void test() {
//	Page page = new Page();
//	File file = new File("./data/image", "01.jpg");
//	page.fileList.add(file);
//	file = new File("./data/image","02.jpg");
//	page.fileList.add(file);
//	pageList.add(page);
//	page = new Page();
//	file = new File("./data/image","02.jpg");
//	page.fileList.add(file);
//	pageList.add(page);
//	page = new Page();
//	file = new File("./data/image","03.jpg");
//	page.fileList.add(file);
//	pageList.add(page);
//	page = new Page();
//	file = new File("./data/image","04.jpg");
//	page.fileList.add(file);
//	pageList.add(page);
//	page = new Page();
//	file = new File("./data/image","05.jpg");
//	page.fileList.add(file);
//	pageList.add(page);
//	page = new Page();
//	file = new File("./data/image","06.jpg");
//	page.fileList.add(file);
//	pageList.add(page);
//	page = new Page();
//	file = new File("./data/image","07.jpg");
//	page.fileList.add(file);
//	pageList.add(page);
//	page = new Page();
//	file = new File("./data/image","08.jpg");
//	page.fileList.add(file);
//	pageList.add(page);
//	this.setIndex(0);
//}
//public static void main(String[] args) throws IOException{
//    Project project = new Project();
////    File file = new File("~/test.json");
//    project.initTest();
//    ObjectMapper mapper = new ObjectMapper();
//    Model model = new Model();
//    model.getDocument().project = project;
////    model.open(new java.io.File("/home/jorodriguez/test.json"));
////    mapper.setVisibility(JsonMethod.FIELD, Visibility.ANY);
////    mapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
////    mapper.writeValue(file, project);
////    project = mapper.readValue(file, Project.class);
//    String jsonInString = mapper.writeValueAsString(model.getDocument());
//    ModelClient modelClient = new ModelClient();
//    User user = new User();
//    user.name = "javainuse";
//    user.password = "password";
//    modelClient.login(user);
//    modelClient.uploadProject(jsonInString);
//    System.out.println(project);
//}
