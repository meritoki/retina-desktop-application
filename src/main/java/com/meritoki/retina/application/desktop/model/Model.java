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
import com.meritoki.retina.application.desktop.model.account.Account;
import com.meritoki.retina.application.desktop.model.command.Command;
import com.meritoki.retina.application.desktop.model.project.File;
import com.meritoki.retina.application.desktop.model.project.Page;
import com.meritoki.retina.application.desktop.model.project.Point;
import com.meritoki.retina.application.desktop.model.project.Project;
import com.meritoki.retina.application.desktop.model.project.Shape;
import com.meritoki.retina.application.desktop.model.project.Text;
import com.meritoki.retina.application.desktop.model.provider.Provider;
import com.meritoki.retina.application.desktop.model.provider.zooniverse.Zooniverse;
import com.meritoki.retina.application.desktop.model.provider.zooniverse.ZooniverseProvider;
import com.meritoki.retina.application.desktop.model.vendor.Vendor;
import com.meritoki.retina.application.desktop.model.vendor.ephesoft.EphesoftVendor;
import com.meritoki.retina.application.desktop.model.vendor.microsoft.MicrosoftVendor;

/**
 * Model is a class that is used to maintain the state of the 
 * Project for the desktop application locally and remotely
 * The class wraps method calls to set the Project and its attribute.
 * These calls are intercepted by the class to create a command stack.
 * This allows support for Do/Undo/Redo operations. This class periodically,
 * writes itself in json to file. 
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
	public Properties properties = null;
    public List<Provider> providerList = new ArrayList<>();
    public List<Vendor> vendorList = new ArrayList<>();
	public ModelClient modelClient = new ModelClient();
    public FileClient fileClient = new FileClient();
    
    
    @JsonProperty
    public List<Account> accountList;
    @JsonProperty
    public LinkedList<Command> undoStack = new LinkedList<>();
    @JsonProperty
    public LinkedList<Command> redoStack = new LinkedList<>();
    
  //Project helpers
    public File file = null;
	public Project project;
	public Script script = new Script();
	public Page page = null;
	public Shape shape = null;
	public Text text = null;
	public List<Page> pageList = null;
	public List<Shape> shapeList = null;
	public List<Text> textList = null;
	public Point pressedPoint = new Point();
	public Point releasedPoint = new Point();
	public double scale = 1;
	
	public List<String> emptyList = new ArrayList<>();
    public List<String> timeList = Arrays.asList("year", "month", "week", "day", "hour", "minute", "second");
    public List<String> spaceList = Arrays.asList("latitude", "longitude", "locale", "location");
    public List<String> energyList = Arrays.asList("letter", "word", "sentance", "temperature", "pressure");
	
	public Model() {
		this.properties = this.open("./retina-desktop.properties");
		if (this.test) {
			this.project = new Project();
			this.project.initTest();
        }
		this.providerList.add(new ZooniverseProvider());
		this.vendorList.add(new MicrosoftVendor());
		this.vendorList.add(new EphesoftVendor());
	}
	
	public static void save(Properties properties) {
		
		
	}
	
	public static Properties open(String fileName) {
        Properties properties = new Properties();
		try (InputStream input = new FileInputStream(fileName)) {
            properties.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
		return properties;
	}
	
    @JsonIgnore
    public void save() {
        logger.info("save()");
        ObjectMapper mapper = new ObjectMapper();
        if(!this.modelClient.checkHealth()) {
        	if(this.file != null) {
		        mapper.setVisibility(JsonMethod.FIELD, JsonAutoDetect.Visibility.ANY);
		        mapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
		        try {
		            mapper.writeValue(new java.io.File(this.file.path+"/"+this.file.name), this.project);
		            logger.info("saved...");
		        } catch (IOException ex) {
		           logger.error(ex);
		        }
        	}
        } else {
        	try {
				this.modelClient.uploadProject(mapper.writeValueAsString(this.project));
			} catch (JsonGenerationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    }
    
    @JsonIgnore
    public void saveAs(java.io.File file) {
        logger.info("saveAs("+file+")");
        ObjectMapper mapper = new ObjectMapper();
        if(!this.modelClient.checkHealth()) {
	        mapper.setVisibility(JsonMethod.FIELD, JsonAutoDetect.Visibility.ANY);
	        mapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
	        try {
	            mapper.writeValue(file, this.project);
	            logger.info("saved...");
	        } catch (IOException ex) {
	           logger.error(ex);
	        }
        } else {
        	try {
				this.modelClient.uploadProject(mapper.writeValueAsString(this.project));
			} catch (JsonGenerationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    }
    
    @JsonIgnore
    public void open(java.io.File file) {
        logger.info("open("+file+")");
        ObjectMapper mapper = new ObjectMapper();
        if(!this.modelClient.checkHealth()) {
	        mapper.disable(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);
	        try {
	            this.project = mapper.readValue(file, Project.class);
	            this.file = new File();
	            this.file.path = file.getAbsolutePath();
	            this.file.name = file.getName();
	            logger.info("opened...");
	        } catch (JsonGenerationException e) {
	            logger.error(e);
	        } catch (JsonMappingException e) {
	            logger.error(e);
	        } catch (IOException e) {
	            logger.error(e);
	        }
        } else {
//        	this.modelClient();
        }
    }
    
    public Page getPage() {
    	return (this.project != null) ? this.project.getPage():null;
    }
    
    public List<Page> getPageList() {
    	return (this.project != null) ? this.project.getPageList():null;
    }
    
    public File getFile() {
    	return (this.project != null) ? this.project.getFile():null;
    }
    
    public File getFile(Point point) {
    	return (this.project != null) ? this.project.getFile(point):null;
    }
    
    public Shape getShape(Point point) {
    	return (this.project != null) ? this.project.getShape(point):null;
    }
    
    public Shape getShape() {
    	return (this.project != null) ? this.project.getShape():null;
    }
}
