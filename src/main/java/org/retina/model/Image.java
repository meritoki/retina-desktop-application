/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.retina.model;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import javax.imageio.ImageIO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;

/**
 *
 * @author osvaldo.rodriguez
 */
public class Image {
    
    static Logger logger = LogManager.getLogger(Image.class.getName());
    public String fileName = null;
    public String filePath = null;
    public String localURL;
    public String remoteURL;
    public String uuid;
    public Dimension dimension;
    private BufferedImage bufferedImage = null;
    private Page page = new Page();
    public org.retina.model.Rectangle rectangle = new org.retina.model.Rectangle();
    private List<org.retina.model.Rectangle> rectangleList = new ArrayList<>();
    private int index = 0;
    
     public Image(){
         UUID uuid = UUID.randomUUID();
         this.uuid = uuid.toString();
    }
     
    @JsonIgnore
    public void setIndex(int index) {
        logger.debug("setIndex("+index+")");
        if(index >= 0 && index < this.rectangleList.size()) {
            this.index = index;
            this.setRectangle(this.rectangleList.get(this.index));
        }
    }
    
    @JsonIgnore
    public int getIndex() {
        return this.index;
    }
    
    @JsonProperty
    public void setRectangle(org.retina.model.Rectangle rectangle){
        this.rectangle = rectangle;
    }
    
    @JsonIgnore
    public void setRectangle(String uuid) {
        logger.debug("setRectangle("+uuid+")");
        Rectangle rectangle = null;
        for(int i = 0; i < this.rectangleList.size(); i++){
            rectangle = this.rectangleList.get(i);
            if(rectangle.uuid.equals(uuid)){
                this.rectangle = rectangle;
                this.index = i;
                break;
            }
        }
    }
    
    @JsonProperty
    public List<org.retina.model.Rectangle> getRectangleList() {
        return this.rectangleList;
    }
    
    @JsonProperty
    public org.retina.model.Rectangle getRectangle(){
        return this.rectangle;
    }
    
//    @JsonProperty
    @JsonIgnore
    public void setRectangleList(List<org.retina.model.Rectangle> rectangleList){
        if(rectangleList!=null&&rectangleList.size()>0)
            logger.info("setRectangleList("+rectangleList+")");
        this.rectangleList = rectangleList;
        this.page.setRectangleList(this.rectangleList);
    }
    
    @JsonIgnore
    public BufferedImage getRectangleImage(org.retina.model.Rectangle r){
       BufferedImage bufferedImage = null;
       if(this.getBufferedImage() != null){
           bufferedImage = this.getBufferedImage().getSubimage(r.getX(), r.getX(), (r.getI()-r.getX()), (r.getJ()-r.getY()));
       }
       return bufferedImage;
    }
    
    @JsonIgnore
    public List<BufferedImage> getRectangleListImageList(List<org.retina.model.Rectangle> rList) {
        List<BufferedImage> imageList = new ArrayList<>();
        for(org.retina.model.Rectangle r:rList) {
            imageList.add(this.getRectangleImage(r));
        }
        return imageList;
    }
    
    @JsonIgnore
    public Page getPage(){
        return this.page;
    }
    
    @JsonIgnore
    public void loadBufferedImage(){
        logger.debug("loadBufferedImage() file="+this.filePath+"/"+this.fileName);
        if(this.filePath!=null&&this.fileName!=null){
            try {                
               this.bufferedImage = ImageIO.read(new File(this.filePath+"/"+this.fileName));
            } catch (IOException ex) {
                 logger.error(ex);
            } 
        }
    }
    
    @JsonIgnore
    public BufferedImage getBufferedImage(){
       if(this.bufferedImage == null){
           this.loadBufferedImage();
       }
       return this.bufferedImage;
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
