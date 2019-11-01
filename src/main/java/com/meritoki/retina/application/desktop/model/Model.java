package com.meritoki.retina.application.desktop.model;

import java.io.FileInputStream;
//import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

import com.meritoki.retina.application.desktop.controller.client.FileClient;
import com.meritoki.retina.application.desktop.controller.client.ModelClient;
import com.meritoki.retina.application.desktop.controller.client.Status;
import com.meritoki.retina.application.desktop.controller.client.UserClient;
import com.meritoki.retina.application.desktop.controller.system.PropertiesController;
import com.meritoki.retina.application.desktop.controller.user.UserController;
import com.meritoki.retina.application.desktop.model.document.Command;
import com.meritoki.retina.application.desktop.model.document.Document;
import com.meritoki.retina.application.desktop.model.document.File;
import com.meritoki.retina.application.desktop.model.document.Page;
import com.meritoki.retina.application.desktop.model.document.Point;
import com.meritoki.retina.application.desktop.model.document.Project;
import com.meritoki.retina.application.desktop.model.document.Script;
import com.meritoki.retina.application.desktop.model.document.Shape;
import com.meritoki.retina.application.desktop.model.document.Text;
import com.meritoki.retina.application.desktop.model.document.User;
import com.meritoki.retina.application.desktop.model.provider.Provider;
import com.meritoki.retina.application.desktop.model.vendor.Vendor;

/**
 * Model is a class that is used to maintain the state of the Project for the
 * desktop application locally and remotely The class wraps method calls to set
 * the Project and its attribute. These calls are intercepted by the class to
 * create a command stack. This allows support for Do/Undo/Redo operations. This
 * class periodically, writes itself in json to file.
 * 
 * @author jorodriguez
 *
 */
public class Model {

	private static Logger logger = LogManager.getLogger(Model.class.getName());
	@JsonIgnore
	public boolean test = true;
	@JsonIgnore
	public boolean rectangle = true;
	@JsonIgnore
	public boolean ellipse = true;

	
	@JsonIgnore
	public List<Provider> providerList = new ArrayList<>();
	@JsonIgnore
	public List<Vendor> vendorList = new ArrayList<>();
	@JsonIgnore
	public ModelClient modelClient = new ModelClient();
	@JsonIgnore
	public FileClient fileClient = new FileClient();
	@JsonIgnore
	public UserClient userClient = new UserClient();
	
	@JsonIgnore
	public Properties properties = null;
	
	@JsonIgnore
	public List<User> userList = null;

	@JsonIgnore
	public Document document = null;

	@JsonIgnore
	public File file = null;
//	@JsonIgnore
//	public Project project;
	@JsonIgnore
	public Script script = new Script();
	@JsonIgnore
	public Page page = null;
	@JsonIgnore
	public Shape shape = null;
	@JsonIgnore
	public Text text = null;
	@JsonIgnore
	public List<Page> pageList = null;
	@JsonIgnore
	public List<Shape> shapeList = null;
	@JsonIgnore
	public List<Text> textList = null;
	@JsonIgnore
	public Point pressedPoint = new Point();
	@JsonIgnore
	public Point releasedPoint = new Point();
	@JsonIgnore
	public double scale = 1;
	@JsonIgnore
	public List<String> emptyList = new ArrayList<>();
	@JsonIgnore
	public List<String> timeList = Arrays.asList("year", "month", "week", "day", "hour", "minute", "second");
	@JsonIgnore
	public List<String> spaceList = Arrays.asList("latitude", "longitude", "locale", "location");
	@JsonIgnore
	public List<String> energyList = Arrays.asList("letter", "word", "sentance", "temperature", "pressure");
	
	public Model() {
		this.properties = PropertiesController.open("./retina-desktop.properties");
		this.userList = UserController.open();
		this.document = new Document();
	}
	
	public Document getDocument() {
		return this.document;
	}
}
