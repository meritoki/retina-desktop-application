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

public class Shape {
    
    static org.apache.logging.log4j.Logger logger = LogManager.getLogger(Shape.class.getName());
    
    public static final int TOP = 0;
    public static final int BOTTOM = 1;
    public static final int LEFT = 2;
    public static final int RIGHT = 3;
    public static final int TOP_LEFT = 4;
    public static final int TOP_RIGHT = 5;
    public static final int BOTTOM_LEFT = 6;
    public static final int BOTTOM_RIGHT = 7;
    public static final int RECTANGLE = 0;
    public static final int CIRCLE = 1;
    public static final int OVAL = 2;
    public int CLASSIFICATION = RECTANGLE;
    public Point startPoint = new Point();
    public Point stopPoint = new Point();
    public double addScale = 1;
    public double scale = 1;
    public String uuid = "";
    private BufferedImage bufferedImage = null;
    //If null, then the data matrix will be populated with null.
    public Data data = new Data(); 
    public boolean removed = false;
    
    public Shape(){
         UUID uuid = UUID.randomUUID();
         this.uuid = uuid.toString();
    }
    
    public Shape(Shape shape) {
    	this.uuid = shape.uuid;
    	this.startPoint = shape.startPoint;
    	this.stopPoint = shape.stopPoint;
    	this.data = shape.data;
    }

    @JsonIgnore
    public void setBufferedImage(Page page){
       BufferedImage bufferedImage = null;
       if(page.getBufferedImage() != null){
           //bufferedImage = page.getBufferedImage().getSubimage(this.getX(), this.getX(), (this.getI()-this.getX()), (this.getJ()-this.getY()));
       }
       this.bufferedImage = bufferedImage;
    }
    
    /**
     * Used by data Matrix algorithm
     * @param u
     * @return
     */
    public int compareTo(Object u) {
      if (this.startPoint.x == 0 || ((Shape)u).startPoint.x == 0) {
        return 0;
      }
      return ((Integer)this.startPoint.x).compareTo(((Shape)u).startPoint.x);
    }
    
    @JsonIgnore
    public boolean isValid(){
        boolean flag = true;
        if(this.startPoint.x==this.stopPoint.x && this.startPoint.y==this.stopPoint.y){
            flag = false;
        }
        return flag;
    }
    
    @JsonProperty
    public String getUUID(){
        return this.uuid;
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
        return (this.startPoint.x + this.stopPoint.x)/2;
    }
    
    @JsonIgnore
    public int getCenterY(){
        return (this.startPoint.y+this.stopPoint.y)/2;
    }
    
    @JsonIgnore
    public void scale(double scale) {
    	this.scale = this.round((scale - this.addScale)+1,6);
    }
    
	public double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    long factor = (long) Math.pow(10, places);
	    value = value * factor;
	    long tmp = Math.round(value);
	    return (double) tmp / factor;
	}
    
    @JsonIgnore
    public boolean contains(Point point){
        boolean flag = false;
        if(this.startPoint.x < this.stopPoint.x && this.startPoint.y < this.stopPoint.y) {
            if(this.startPoint.x <= point.x && point.x <= this.stopPoint.x && this.startPoint.y <= point.y && point.y <= this.stopPoint.y){
                flag = true;
            }
        } else if (this.stopPoint.x < this.startPoint.x && this.stopPoint.y < this.startPoint.y) {
            if(this.stopPoint.x <= point.x && point.x <= this.startPoint.x && this.stopPoint.y <= point.y && point.y <= this.startPoint.y){
                flag = true;
            }
        }
        return flag;
    }
    
    public boolean intersects(Point point) {
    	boolean flag  = false;
    	if(this.intersect(point)>-1) {
    		flag = true;
    	}
    	return flag;
    }
    
    @JsonIgnore
    public int intersect(Point point){
        int flag = -1;
        //introduce the idea of a buffer where a user does not have to press exactly on line
        //TOP
        if(point.y == this.startPoint.y && point.x > this.startPoint.x && point.x < this.stopPoint.x) {
        	flag = TOP;
        } else if(point.y == this.stopPoint.y && point.x > this.startPoint.x && point.x < this.stopPoint.x) {
        	flag = BOTTOM;
        } else if(point.x == this.startPoint.x && point.y > this.startPoint.y && point.y < this.stopPoint.y) {
        	flag = LEFT;
        } else if(point.x == this.stopPoint.x && point.y > this.startPoint.y && point.y < this.stopPoint.y) {
        	flag = RIGHT;
        } else if(point.x == this.startPoint.x && point.y == this.startPoint.y) {
        	flag = TOP_LEFT;
        } else if(point.x == this.stopPoint.x && point.y == this.stopPoint.y) {
        	flag = BOTTOM_RIGHT;
        } else if(point.x == this.stopPoint.x && point.y == this.startPoint.y) {
        	flag = TOP_RIGHT;
        } else if(point.x == this.startPoint.x && point.y == this.stopPoint.y) {
        	flag = BOTTOM_LEFT;
        }
        return flag;
    }
    
    public void resize(Point point, int selection) {
    	logger.info("resize("+point+", "+selection+")");
    	switch(selection) {
	    	case TOP:{
	    		this.startPoint.y = point.y;
	    		break;
	    	}
	    	case BOTTOM:{
	    		this.stopPoint.y = point.y;
	    		break;
	    	}
	    	case LEFT:{
	    		this.startPoint.x = point.x;
	    		break;
	    	}
	    	case RIGHT:{
	    		this.stopPoint.x = point.x;
	    		break;
	    	}
	    	case TOP_LEFT:{
	    		this.startPoint = point;
	    		break;
	    	}
	    	case TOP_RIGHT:{
	    		this.startPoint.y = point.y;
	    		this.stopPoint.x = point.x;
	    		break;
	    	}
	    	case BOTTOM_LEFT:{
	    		this.startPoint.x = point.x;
	    		this.stopPoint.y = point.y;
	    		break;
	    	}
	    	case BOTTOM_RIGHT:{
	    		this.stopPoint = point;
	    		break;
	    	}
    	}
    }
  
    @JsonIgnore
    public void move(Point point){
    	logger.info("move("+point+")");
        this.startPoint.x = this.startPoint.x + point.x;
        this.startPoint.y = this.startPoint.y + point.y;
        this.stopPoint.x = this.stopPoint.x + point.x;
        this.stopPoint.y = this.stopPoint.y + point.y;
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
                Logger.getLogger(Shape.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            string = "uuid="+this.uuid+", scale="+this.scale;
        }
        return string;
    }
    
}
