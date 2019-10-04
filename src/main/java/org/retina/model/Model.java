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
    public Script script = new Script();
    public List<Page> pageList = new ArrayList<>();
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
        Page page = new Page();
        page.fileName = "01.jpg";
        page.filePath = "./data/page";
        Rectangle r = new Rectangle();
        r.setX(0);
        r.setY(0);
        r.setI(1);
        r.setJ(1);
        page.getRectangleList().add(r);
        pageList.add(page);
        page = new Page();
        page.fileName = "02.jpg";
        page.filePath = "./data/page";
        pageList.add(page);
        page = new Page();
        page.fileName = "03.jpg";
        page.filePath = "./data/page";
        pageList.add(page);
        page = new Page();
        page.fileName = "04.jpg";
        page.filePath = "./data/page";
        pageList.add(page);
        page = new Page();
        page.fileName = "05.jpg";
        page.filePath = "./data/page";
        pageList.add(page);
        page = new Page();
        page.fileName = "06.jpg";
        page.filePath = "./data/page";
        pageList.add(page);
        page = new Page();
        page.fileName = "07.jpg";
        page.filePath = "./data/page";
        pageList.add(page);
        page = new Page();
        page.fileName = "08.jpg";
        page.filePath = "./data/page";
        pageList.add(page);
        this.setIndex(0);
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
    
//    @JsonProperty
//    public void setPage(Page page){
//        if(page!=null)
//            logger.info("setPage("+page.uuid+")");
//        this.page.setRectangleList(this.page.getRectangleList());
//    }
//    
    @JsonIgnore
    public void setPage(String uuid) {
        logger.debug("setPage("+uuid+")");
        Page page = null;
        for(int i = 0; i < this.pageList.size(); i++){
            page = this.pageList.get(i);
            if(page.uuid.equals(uuid)){
                this.index = i;
                break;
            }
        }
    }
    
    @JsonProperty
    public void setPageList(List<Page> pageList){
        logger.info("setPageList("+pageList+")");
        this.pageList = pageList;
    }
    
    @JsonProperty
    public List<Page> getPageList(){
        return this.pageList;
    }
    
    @JsonProperty
    public Page getPage() {
        return this.pageList.get(index);
    }
    
    @JsonIgnore
    public void setIndex(int index) {
        logger.debug("setIndex("+index+")");
        if(index >= 0 && index < this.pageList.size()) {
            this.index = index;
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
