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

import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
    @JsonIgnore
    public static final int TOP = 0;
    @JsonIgnore
    public static final int BOTTOM = 1;
    @JsonIgnore
    public static final int LEFT = 2;
    @JsonIgnore
    public static final int RIGHT = 3;
    @JsonIgnore
    public static final int TOP_LEFT = 4;
    @JsonIgnore
    public static final int TOP_RIGHT = 5;
    @JsonIgnore
    public static final int BOTTOM_LEFT = 6;
    @JsonIgnore
    public static final int BOTTOM_RIGHT = 7;
    @JsonIgnore
    public static final String RECTANGLE = "rectangle";
    @JsonIgnore
    public static final String ELLIPSE = "ellipse";
    @JsonProperty
    public String classification = null;
    @JsonProperty
    List<Point> pointList = new ArrayList<>(2);
    @JsonProperty
    public Point startPoint = new Point();
    @JsonProperty
    public Point stopPoint = new Point();
    @JsonProperty
    public double addScale = 1;
    @JsonProperty
    public double scale = 1;
    @JsonProperty
    public String uuid = null;
//    private BufferedImage bufferedImage = null;
    //If null, then the data matrix will be populated with null.
    public Data data = new Data(); 
    public boolean removed = false;
    
    public Shape(){
         this.uuid = UUID.randomUUID().toString();
    }
    
    public Shape(Shape shape) {
    	this.uuid = shape.uuid;
    	this.pointList = shape.pointList;
    	this.startPoint = shape.startPoint;
    	this.stopPoint = shape.stopPoint;
    	this.data = shape.data;
    }
    
    @JsonIgnore
    public void draw(Graphics2D graphics2D) {
    	if(!removed) {
			double x = Math.min(startPoint.x, stopPoint.x);
			double y = Math.min(startPoint.y, stopPoint.y);
			double width = Math.abs(startPoint.x - stopPoint.x);
			double height = Math.abs(startPoint.y - stopPoint.y);
			//When addScale is 1 this scale multiplier works correctly.
			x *= this.scale;
			y *= this.scale;
			width *= this.scale;
			height *= this.scale;
			if(this.classification.equals(ELLIPSE)) {
				Ellipse2D.Double ellipse = new Ellipse2D.Double(x, y, width, height);
				graphics2D.draw(ellipse);
			} else if(this.classification.equals(RECTANGLE)) {
				Rectangle2D.Double rectangle = new Rectangle2D.Double(x, y, width, height);
				graphics2D.draw(rectangle);
			}
    	}
    }

    @JsonIgnore
    public void setBufferedImage(Page page){
       BufferedImage bufferedImage = null;
       if(page.getBufferedImage() != null){
           //bufferedImage = page.getBufferedImage().getSubimage(this.getX(), this.getX(), (this.getI()-this.getX()), (this.getJ()-this.getY()));
       }
//       this.bufferedImage = bufferedImage;
    }
    
    /**
     * Used by data Matrix algorithm
     * @param u
     * @return
     */
//    public int compareTo(Object u) {
//      if (this.startPoint.x == 0 || ((Shape)u).startPoint.x == 0) {
//        return 0;
//      }
//      return ((Integer)this.startPoint.x).compareTo(((Shape)u).startPoint.x);
//    }
    
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
    public double getCenterX(){
        return (this.startPoint.x + this.stopPoint.x)/2;
    }
    
    @JsonIgnore
    public double getCenterY(){
        return (this.startPoint.y+this.stopPoint.y)/2;
    }
    
    @JsonIgnore
    public void scale(double scale) {
    	logger.debug("scale("+scale+")");
    	this.scale = scale*(1/this.addScale);
//    	this.startPoint.x*=this.scale;
//    	this.startPoint.y*=this.scale;
//    	this.stopPoint.y*=this.scale;
//    	this.stopPoint.x*=this.scale;
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
        Point startPoint = new Point();
        Point stopPoint = new Point();
        startPoint.x = this.startPoint.x*this.scale;
        startPoint.y = this.startPoint.y*this.scale;
        stopPoint.x = this.stopPoint.x*this.scale;
        stopPoint.y = this.stopPoint.y*this.scale;
        
        if(startPoint.x < stopPoint.x && startPoint.y < stopPoint.y) {
            if(startPoint.x <= point.x && point.x <= stopPoint.x && startPoint.y <= point.y && point.y <= stopPoint.y){
                flag = true;
            }
        } else if (stopPoint.x < startPoint.x && stopPoint.y < startPoint.y) {
            if(stopPoint.x <= point.x && point.x <= startPoint.x && stopPoint.y <= point.y && point.y <= startPoint.y){
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
    	logger.info("intersect("+point+")");
        int selection = -1;
        Point startPoint = new Point();
        Point stopPoint = new Point();
        startPoint.x = this.startPoint.x*this.scale;
        startPoint.y = this.startPoint.y*this.scale;
        stopPoint.x = this.stopPoint.x*this.scale;
        stopPoint.y = this.stopPoint.y*this.scale;
        //introduce the idea of a buffer where a user does not have to press exactly on line
        //TOP
        int margin = 20;
        if(point.x == startPoint.x && point.y == startPoint.y) {
        	//Works
        	selection = TOP_LEFT;
        } else if(point.x > (stopPoint.x-margin) && point.x<(stopPoint.x+margin) && point.y > (stopPoint.y-margin) && point.y < (stopPoint.y+margin)) {//(point.x == stopPoint.x && point.y == stopPoint.y) {
        	//Works
        	selection = BOTTOM_RIGHT;
        } else if(point.x > (stopPoint.x-margin) && point.x < (stopPoint.x+margin) && point.y > (startPoint.y-margin) && point.y < (startPoint.y + margin)) {
        	//Works
        	selection = TOP_RIGHT;
        } else if(point.x > (startPoint.x-margin) && point.x < (startPoint.x+margin) && point.y > (stopPoint.y - margin) && point.y < (stopPoint.y + margin)) {
        	//Works
        	selection = BOTTOM_LEFT;
        } else if(point.y > (startPoint.y-margin) && point.y < (startPoint.y+margin) && point.x > startPoint.x && point.x < stopPoint.x) {
        	//Works
        	selection = TOP;
        } else if(point.y > (startPoint.y-margin) && point.y < (startPoint.y+margin) && point.x > startPoint.x && point.x < stopPoint.x) {
        	//Not working
        	selection = BOTTOM;
        } else if(point.x > (startPoint.x-margin) && point.x < (startPoint.x+margin) && point.y > startPoint.y && point.y < stopPoint.y) {
        	//Works
        	selection = LEFT;
        } else if(point.x > (startPoint.x-margin) && point.x < (startPoint.x+margin) && point.y > startPoint.y && point.y < stopPoint.y) {
        	//Not working
        	selection = RIGHT;
        }
        
//        if(point.y == startPoint.y && point.x > startPoint.x && point.x < stopPoint.x) {
//        	selection = TOP;
//        } else if(point.y == stopPoint.y && point.x > startPoint.x && point.x < stopPoint.x) {
//        	selection = BOTTOM;
//        } else if(point.x == startPoint.x && point.y > startPoint.y && point.y < stopPoint.y) {
//        	selection = LEFT;
//        } else if(point.x == stopPoint.x && point.y > startPoint.y && point.y < stopPoint.y) {
//        	selection = RIGHT;
//        } else if(point.x == startPoint.x && point.y == startPoint.y) {
//        	selection = TOP_LEFT;
//        } else if(point.x == stopPoint.x && point.y == stopPoint.y) {
//        	selection = BOTTOM_RIGHT;
//        } else if(point.x == stopPoint.x && point.y == startPoint.y) {
//        	selection = TOP_RIGHT;
//        } else if(point.x == startPoint.x && point.y == stopPoint.y) {
//        	selection = BOTTOM_LEFT;
//        }
        return selection;
    }
    
    public void resize(Point point, int selection) {
    	logger.info("resize("+point+", "+selection+")");
    	Point startPoint = new Point();
        Point stopPoint = new Point();
        startPoint.x = this.startPoint.x*this.scale;
        startPoint.y = this.startPoint.y*this.scale;
        stopPoint.x = this.stopPoint.x*this.scale;
        stopPoint.y = this.stopPoint.y*this.scale;
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
