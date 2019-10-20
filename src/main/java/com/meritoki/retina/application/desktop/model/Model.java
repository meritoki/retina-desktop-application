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
import com.meritoki.retina.application.desktop.model.account.Account;
import com.meritoki.retina.application.desktop.model.account.Organization;
import com.meritoki.retina.application.desktop.model.account.User;
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
	public boolean test = false;
    @JsonIgnore
    public boolean rectangle = true;
    @JsonIgnore
    public boolean ellipse = true;
    
    @JsonIgnore
	public Properties properties = null;
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
    
    @JsonProperty
    public String uuid = null;
    @JsonProperty
    public List<Account> accountList = new ArrayList<>();
    @JsonProperty
    public LinkedList<Command> undoStack = new LinkedList<>();
    @JsonProperty
    public LinkedList<Command> redoStack = new LinkedList<>();
    
  //Project helpers
    @JsonIgnore
    public File file = null;
    @JsonIgnore
	public Project project;
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
		this.properties = this.open("./retina-desktop.properties");
                this.project = new Project();
		if (this.test) {
                    this.project.initTest();
                }
		Account account = new Account();
		User user = new User();
		user.name = "javainuse";
		user.password = "";
		account.user = user;
		Organization org = new Organization();
		account.organizationList.add(org);
		this.accountList.add(account);
		this.providerList.add(new ZooniverseProvider());
		this.vendorList.add(new MicrosoftVendor());
		this.vendorList.add(new EphesoftVendor());
	}
	
	@JsonIgnore
	public static void save(Properties properties) {
		
		
	}
	
	@JsonIgnore
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
    
    @JsonIgnore
    public Page getPage() {
    	return (this.project != null) ? this.project.getPage():null;
    }
    
    @JsonIgnore
    public List<Page> getPageList() {
    	return (this.project != null) ? this.project.getPageList():null;
    }
    
    @JsonIgnore
    public File getFile() {
    	return (this.project != null) ? this.project.getFile():null;
    }
    
    @JsonIgnore
    public File getFile(Point point) {
    	return (this.project != null) ? this.project.getFile(point):null;
    }
    
    @JsonIgnore
    public Shape getShape(Point point) {
    	return (this.project != null) ? this.project.getShape(point):null;
    }
    
    @JsonIgnore
    public Shape getShape() {
    	return (this.project != null) ? this.project.getShape():null;
    }
    
    @JsonIgnore
    public void setScale(double scale) {
    	Project project = this.project;
    	if(project != null) {
    		project.setScale(scale);
    	}
    }
    
//    @JsonIgnore
//    public Token login(String username, )
}
