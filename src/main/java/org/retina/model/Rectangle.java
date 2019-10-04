/*
 * Copyright 2019 osvaldo.rodriguez.
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
package org.retina.model;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.logging.log4j.LogManager;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;

/**
 *
 * @author osvaldo.rodriguez
 */
public class Rectangle {
    
    static org.apache.logging.log4j.Logger logger = LogManager.getLogger(Rectangle.class.getName());
    
    private int x = 0;
    private int y = 0;
    private int i = 0;
    private int j = 0;
    public String uuid = "";
    private BufferedImage bufferedImage = null;
    public Data data = new Data();
    
    
    public Rectangle(){
         UUID uuid = UUID.randomUUID();
         this.uuid = uuid.toString();
         this.data = new Data();
    }
    
    @JsonIgnore
    public void setBufferedImage(org.retina.model.Page image){
       BufferedImage bufferedImage = null;
       if(image.getBufferedImage() != null){
           bufferedImage = image.getBufferedImage().getSubimage(this.getX(), this.getX(), (this.getI()-this.getX()), (this.getJ()-this.getY()));
       }
       this.bufferedImage = bufferedImage;
    }
    
    public int compareTo(Object u) {
      if (x == 0 || ((Rectangle)u).x == 0) {
        return 0;
      }
      return ((Integer)x).compareTo(((Rectangle)u).getX());
    }
    
    @JsonIgnore
    public boolean isValid(){
        boolean flag = true;
        if(x==i && y==j){
            flag = false;
        }
        return flag;
    }
    
    @JsonProperty
    public String getUUID(){
        return this.uuid;
    }
    

    @JsonProperty
    public int getX() {
        return x;
    }
    @JsonProperty
    public void setX(int x) {
        this.x = x;
    }
    @JsonProperty
    public int getY() {
        return y;
    }
    @JsonProperty
    public void setY(int y) {
        this.y = y;
    }
    @JsonProperty
    public int getI() {
        return i;
    }
    @JsonProperty
    public void setI(int i) {
        this.i = i;
    }
    @JsonProperty
    public int getJ() {
        return j;
    }
    @JsonProperty
    public void setJ(int j) {
        this.j = j;
    }
    @JsonProperty
    public void setData(Data data){
        this.data = data;
    }
    @JsonProperty
    public Data getData(){
        return this.data;
    }
    
    @JsonIgnore
    public int getCenterX(){
        return (this.x + this.i)/2;
    }
    
    @JsonIgnore
    public int getCenterY(){
        return (this.y+this.j)/2;
    }
    
    @JsonIgnore
    public boolean contains(int x, int y){
        boolean flag = false;
        if(this.x < this.i && this.y < this.j) {
            if(this.x < x && x < this.i && this.y < y && y < this.j){
                flag = true;
            }
        } else if (this.i < this.x && this.j < this.y) {
            if(this.i < x && x < this.x && this.j < y && y < this.y){
                flag = true;
            }
        }
        return flag;
    }
    
    @JsonIgnore
    public boolean intersect(int x, int y){
        boolean flag = false;
        if(this.contains(x, y)){
            if(x == this.x || x == this.i || y == this.y || y == this.j){
                flag = true;
            }
        }
        return flag;
    }
  
    @JsonIgnore
    public void move(int x, int y){
        this.x = this.x + x;
        this.y = this.y + y;
        this.i = this.i + x;
        this.j = this.j + y;
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
                Logger.getLogger(Rectangle.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            string = this.uuid;
        }
        return string;
    }
    
}
