package com.meritoki.retina.application.desktop.model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

import com.meritoki.retina.application.desktop.controller.client.FileClient;
import com.meritoki.retina.application.desktop.controller.client.ModelClient;
import com.meritoki.retina.application.desktop.controller.client.Status;
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
 * This allows support for Do/Undo/Redo operations
 * @author jorodriguez
 *
 */
public class Model {
	
	private static Logger logger = LogManager.getLogger(Model.class.getName());
    public boolean test = true;
    public boolean rectangle = true;
    public boolean ellipse = true;
	public Properties properties = null;
    public List<Provider> providerList = new ArrayList<>();
    public List<Vendor> vendorList = new ArrayList<>();
	public ModelClient modelClient = new ModelClient();
    public FileClient fileClient = new FileClient();
    public LinkedList<Command> undoStack = new LinkedList<>();
    public LinkedList<Command> redoStack = new LinkedList<>();
    public File file = null;
	public Project project;
	public Script script = new Script();
	//Project helpers
	public Page page = null;
	public Shape shape = null;
	public Text text = null;
	public List<Page> pageList = null;
	public List<Shape> shapeList = null;
	public List<Text> textList = null;
	public Point pressedPoint = new Point();
	public Point releasedPoint = new Point();
	public double scale = 1;
	
	public Model() {
		this.properties = Configuration.open("./retina-desktop.properties");
		if (this.test) {
			this.project = new Project();
			this.project.initTest();
        }
		this.providerList.add(new ZooniverseProvider());
		this.vendorList.add(new MicrosoftVendor());
		this.vendorList.add(new EphesoftVendor());
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
		            mapper.writeValue(this.file, this.project);
		            logger.info("saved...");
		        } catch (IOException ex) {
		           logger.error(ex);
		        }
        	}
        } else {
        	try {
				this.modelClient.importProject(mapper.writeValueAsString(this.project));
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
    public void saveAs(File file) {
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
				this.modelClient.importProject(mapper.writeValueAsString(this.project));
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
    public void open(File file) {
        logger.info("open("+file+")");
        ObjectMapper mapper = new ObjectMapper();
        if(!this.modelClient.checkHealth()) {
	        mapper.disable(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);
	        try {
	            this.project = mapper.readValue(file, Project.class);
	            this.file = file;
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
}
