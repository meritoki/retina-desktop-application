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
package com.meritoki.retina.application.desktop.model.project;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

import javax.swing.JPanel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.codehaus.jackson.map.SerializationConfig;

import com.meritoki.retina.application.desktop.controller.client.ModelClient;
import com.meritoki.retina.application.desktop.model.Model;
import com.meritoki.retina.application.desktop.model.provider.Provider;
import com.meritoki.retina.application.desktop.model.vendor.Vendor;

public class Project implements Serializable {
    
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) throws IOException{
        Project project = new Project();
//        File file = new File("~/test.json");
//        project.initTest();
        ObjectMapper mapper = new ObjectMapper();
        Model model = new Model();
        model.open(new java.io.File("/home/jorodriguez/test.json"));
//        mapper.setVisibility(JsonMethod.FIELD, Visibility.ANY);
//        mapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
//        mapper.writeValue(file, project);
//        project = mapper.readValue(file, Project.class);
        String jsonInString = mapper.writeValueAsString(model.project);
        System.out.println(jsonInString);
        ModelClient modelClient = new ModelClient();
        modelClient.importProject(jsonInString);
        System.out.println(project);
    }
    
    static Logger logger = LogManager.getLogger(Project.class.getName());
    @JsonProperty
    public String uuid = "";
    @JsonProperty
    public List<Page> pageList = new ArrayList<>();
    @JsonIgnore
    public int index = 0;
    @JsonProperty
    public List<Layout> layoutList = new ArrayList<>();

    
    public Project(){ 
        this.uuid = UUID.randomUUID().toString();
    }
    
    @JsonIgnore
    public void initTest(){
        Page page = new Page();
        File file = new File();
        file.name = "01.jpg";
        file.path = "./data/page";
        page.fileList.add(file);
        file = new File();
        file.name = "02.jpg";
        file.path = "./data/page";
        page.fileList.add(file);
        pageList.add(page);
        page = new Page();
        file = new File();
        file.name = "02.jpg";
        file.path = "./data/page";
        page.fileList.add(file);
        pageList.add(page);
        page = new Page();
        file = new File();
        file.name = "03.jpg";
        file.path = "./data/page";
        page.fileList.add(file);
        pageList.add(page);
        page = new Page();
        file = new File();
        file.name = "04.jpg";
        file.path = "./data/page";
        page.fileList.add(file);
        pageList.add(page);
        page = new Page();
        file = new File();
        file.name = "05.jpg";
        file.path = "./data/page";
        page.fileList.add(file);
        pageList.add(page);
        page = new Page();
        file = new File();
        file.name = "06.jpg";
        file.path = "./data/page";
        page.fileList.add(file);
        pageList.add(page);
        page = new Page();
        file = new File();
        file.name = "07.jpg";
        file.path = "./data/page";
        page.fileList.add(file);
        pageList.add(page);
        page = new Page();
        file = new File();
        file.name = "08.jpg";
        file.path = "./data/page";
        page.fileList.add(file);
        pageList.add(page);
        this.setIndex(0);
    }
    
    @JsonIgnore
	public int getIndex(){
	    logger.debug("getIndex() this.index="+this.index);
	    return this.index;
	}

	@JsonIgnore
	public Page getPage() {
	    return (this.pageList.size() > 0) ? this.pageList.get(index) : null;
	}
	
	@JsonIgnore
	public List<Page> getPageList(){
	    return this.pageList;
	}

	@JsonIgnore
	public File getFile() {
		Page page = this.getPage();
		File file = (page != null) ? page.getFile(): null;
		return file;
	}

	public File getFile(Point point) {
		Page page = this.getPage();
		File shape = (page != null) ? page.getFile(point) : null;
		return shape;
	}

	/**
	 * A mousePressed begins an Add, Move, or Resize. Call each one a Command that
	 * affects the model. A command can have one or more Operations.
	 * 
	 * @param point
	 * @return
	 */
	public Shape getShape(Point point) {
		Page page = this.getPage();
		Shape shape = (page != null) ? page.getShape(point) : null;
		return shape;
	}
	
	public Shape getShape() {
		Page page = this.getPage();
		File file = (page != null) ? page.getFile() : null;
		Shape shape = (file != null) ? file.getShape() : null;
		return shape;
	}
	
	public List<Shape> getShapeList() {
		Page page = this.getPage();
		List<Shape> shapeList = (page != null) ? page.getShapeList():null;
		return shapeList;
	}

	public void setShape(String uuid) {
    	logger.info("setShape("+uuid+")");
    	Page page = this.getPage();
    	page.setShape(uuid);
    }
    
    public void setFile(String uuid) {
    	logger.info("setFile("+uuid+")");
    	Page page = this.getPage();
    	page.setFile(uuid);
    }
    
    @JsonIgnore
    public void setPageList(List<Page> pageList){
        logger.info("setPageList("+pageList+")");
        this.pageList = pageList;
    }
    
    @JsonIgnore
    public void setIndex(int index) {
        logger.debug("setIndex("+index+")");
        if(index >= 0 && index < this.pageList.size()) {
            this.index = index;
        }
    }
    
    @JsonIgnore
    public void setScale(double scale) {
    	logger.debug("setScale("+scale+")");
    	Page page = this.getPage();
    	if(page != null) {
    		page.setScale(scale);
    	}
    }
    
    @JsonIgnore
    public void setPage(String uuid) {
        logger.info("setPage("+uuid+")");
        Page page = null;
        for(int i = 0; i < this.pageList.size(); i++){
            page = this.pageList.get(i);
            if(page.uuid.equals(uuid)){
                this.setIndex(i);;
                break;
            }
        }
    }
    
    public void addShape(Shape shape) {
		logger.info("addShape("+shape+")");
		Page page = this.getPage();
		if(page != null) {
			page.addShape(shape);
		}
	}


	public void addFile(File file) {
		logger.info("addFile("+file+")");
		Page page = this.getPage();
		if(page != null) {
			page.addFile(file);
		}
	}

	@JsonIgnore
    @Override
    public String toString(){
        String string = "";
        if(logger.isTraceEnabled()){
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
}
