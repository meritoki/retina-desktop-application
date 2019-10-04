/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.retina.model;

import org.retina.model.zooniverse.Zooniverse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
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
import org.retina.view.frame.Main;
import static org.retina.model.Rectangle.logger;
import org.retina.model.microsoft.Microsoft;

/**
 *
 * @author osvaldo.rodriguez
 */
public class Model {
    
    public static void main(String[] args) throws IOException{
        Model model = new Model();
        File file = new File("./data/model.json");
        model.initTest();
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(JsonMethod.FIELD, Visibility.ANY);
        mapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
        mapper.writeValue(file, model);
        model = mapper.readValue(file, Model.class);
        System.out.println(model);
    }
    
    static Logger logger = LogManager.getLogger(Model.class.getName());
    public File file = null;
    public Script script = new Script();
    public List<Image> imageList = null;
    public Image image = null;
    public int index = 0;
    public String uuid = "";
    public Zooniverse zooniverse = new Zooniverse();
    public Microsoft microsoft = new Microsoft();

    public Zooniverse getZooniverse() {
        return zooniverse;
    }

    public void setZooniverse(Zooniverse zooniverse) {
        this.zooniverse = zooniverse;
    }
    
    public Model(){ 
        UUID uuid = UUID.randomUUID();
        this.uuid = uuid.toString();
    }
    
    @JsonIgnore
    public void initTest(){
        this.image = new Image();
        this.image.fileName = "01.jpg";
        this.image.filePath = "./data/image";
        Rectangle r = new Rectangle();
        r.setX(0);
        r.setY(0);
        r.setI(1);
        r.setJ(1);
        this.image.getRectangleList().add(r);
        this.image.setRectangle(r);
        this.imageList.add(this.image);
        this.image = new Image();
        this.image.fileName = "02.jpg";
        this.image.filePath = "./data/image";
        this.imageList.add(this.image);
        this.image = new Image();
        this.image.fileName = "03.jpg";
        this.image.filePath = "./data/image";
        this.imageList.add(this.image);
        this.image = new Image();
        this.image.fileName = "04.jpg";
        this.image.filePath = "./data/image";
        this.imageList.add(this.image);
        this.image = new Image();
        this.image.fileName = "05.jpg";
        this.image.filePath = "./data/image";
        this.imageList.add(this.image);
        this.image = new Image();
        this.image.fileName = "06.jpg";
        this.image.filePath = "./data/image";
        this.imageList.add(this.image);
        this.image = new Image();
        this.image.fileName = "07.jpg";
        this.image.filePath = "./data/image";
        this.imageList.add(this.image);
        this.image = new Image();
        this.image.fileName = "08.jpg";
        this.image.filePath = "./data/image";
        this.imageList.add(this.image);
        this.setIndex(0);
    }
    
    @JsonIgnore
    public void setFile(File file){
        logger.info("setFile("+file+")");
        this.file = file;
    }
    
    @JsonIgnore
    public void save(File file) {
        logger.info("save("+file+")");
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(JsonMethod.FIELD, JsonAutoDetect.Visibility.ANY);
        mapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
        try {
            mapper.writeValue(file, this);
            logger.info("saved...");
        } catch (IOException ex) {
           logger.error(ex);
        }
        
    }
    
    @JsonIgnore
    public Model open(File file) {
        logger.info("open("+file+")");
        Model model = null;
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);
        try {
            model = mapper.readValue(file, Model.class);
            logger.info("opened...");
        } catch (JsonGenerationException e) {
            logger.error(e);
        } catch (JsonMappingException e) {
            logger.error(e);
        } catch (IOException e) {
            logger.error(e);
        }
        return model;
    }
    
    @JsonIgnore
    public void load(){
        System.out.println("load()");
    }
    
    @JsonProperty
    public void setImage(Image image){
        if(image!=null)
            logger.info("setImage("+image.uuid+")");
        this.image = image;
        this.image.getPage().setRectangleList(this.image.getRectangleList());
    }
    
    @JsonIgnore
    public void setImage(String uuid) {
        logger.debug("setImage("+uuid+")");
        Image image = null;
        for(int i = 0; i < this.imageList.size(); i++){
            image = this.imageList.get(i);
            if(image.uuid.equals(uuid)){
                this.setImage(image);
                this.index = i;
                break;
            }
        }
    }
    
    @JsonProperty
    public void setImageList(List<Image> imageList){
        logger.info("setImageList("+imageList+")");
        this.imageList = imageList;
    }
    
    @JsonProperty
    public List<Image> getImageList(){
        return this.imageList;
    }
    
    @JsonProperty
    public Image getImage() {
        return this.image;
    }
    
    @JsonIgnore
    public void setIndex(int index) {
        logger.debug("setIndex("+index+")");
        if(index >= 0 && index < this.imageList.size()) {
            this.index = index;
            this.setImage(this.imageList.get(this.index));
        }
    }
    
    @JsonIgnore
    public int getIndex(){
        logger.debug("getIndex() this.index="+this.index);
        return this.index;
    }
    
    @JsonIgnore
    public Script getScript(){
        return this.script;
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
                java.util.logging.Logger.getLogger(Rectangle.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            string = this.uuid;
        }
        return string;
    }
}
