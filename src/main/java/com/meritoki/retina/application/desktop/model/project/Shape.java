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
    
	@JsonIgnore
    private static org.apache.logging.log4j.Logger logger = LogManager.getLogger(Shape.class.getName());
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
    public List<Point> pointList = new ArrayList<>(2);
    @JsonProperty
    public double addScale = 1;
    @JsonProperty
    public double scale = 1;
    @JsonProperty
    public String uuid = null;
    @JsonIgnore
    private BufferedImage bufferedImage = null;
    @JsonProperty
    public Data data = new Data();
    @JsonIgnore
    public boolean removed = false;
    @JsonIgnore
    public Dimension dimension = null;
    
    public Shape(){
         this.uuid = UUID.randomUUID().toString();
    }
    
    public Shape(Shape shape) {
    	this.classification = shape.classification;
    	this.uuid = shape.uuid;
    	this.pointList = shape.pointList;
    	this.data = shape.data;
    }
    
    @JsonIgnore
    public void initDimension() {
    	Dimension dimension = new Dimension();
//    	if(this.dimension == null) {
    	dimension.x = Math.min(this.pointList.get(0).x, this.pointList.get(1).x);
//		this.x += offset;
    	dimension.y = Math.min(this.pointList.get(0).y, this.pointList.get(1).y);
//		this.y += margin;
    	dimension.w = Math.abs(this.pointList.get(0).x - this.pointList.get(1).x);
    	dimension.h = Math.abs(this.pointList.get(0).y - this.pointList.get(1).y);
    	dimension.x *= this.scale;
    	dimension.y *= this.scale;
    	dimension.w *= this.scale;
    	dimension.h *= this.scale;
    	this.dimension = dimension;
//    	}
//    	return dimension;
    }
    
    public void getObject() {
    	
    }
    
    @JsonIgnore
    public void sortPointList() {
    	Point pointZero = this.pointList.get(0);
    	Point pointOne = this.pointList.get(1);
    	//Case B
    	if(pointZero.x > pointOne.x && pointZero.y < pointOne.y) {
    		this.pointList.set(0, new Point(pointOne.x,pointZero.y));
    		this.pointList.set(1, new Point(pointZero.x,pointOne.y));
    	//Case C
    	} else if(pointZero.x < pointOne.x && pointZero.y > pointOne.y) {
    		this.pointList.set(0, new Point(pointZero.x,pointOne.y));
    		this.pointList.set(1, new Point(pointOne.x,pointZero.y));
    	//Case D
    	} else if(pointZero.x < pointOne.x && pointZero.y < pointOne.y) {
    		this.pointList.set(0, pointOne);
    		this.pointList.set(1, pointZero);
    	}
    }

    /**
     * Used by data Matrix algorithm
     * @param u
     * @return
     */
//    public int compareTo(Object u) {
//      if (this.pointList.get(0).x == 0 || ((Shape)u).startPoint.x == 0) {
//        return 0;
//      }
//      return ((Integer)this.pointList.get(0).x).compareTo(((Shape)u).startPoint.x);
//    }
    
    @JsonIgnore
    public boolean isValid(){
        boolean flag = true;
        if(this.pointList.get(0).x==this.pointList.get(1).x && this.pointList.get(0).y==this.pointList.get(1).y){
            flag = false;
        }
        return flag;
    }
    
    @JsonProperty
	public Data getData(){
	    return this.data;
	}

	@JsonIgnore
	public double getCenterX(){
	    return (this.pointList.get(0).x + this.pointList.get(1).x)/2;
	}

	@JsonIgnore
	public double getCenterY(){
	    return (this.pointList.get(0).y+this.pointList.get(1).y)/2;
	}
	
//	@JsonIgnore
//	public void setOffset(double offset) {
//		this.offset = offset;
//	}
//	
//	@JsonIgnore
//	public void setMargin(double offset) {
//		this.margin = offset;
//	}

	@JsonIgnore
	    public void setBufferedImage(Page page){
//	       BufferedImage bufferedImage = null;
	//       if(page.getBufferedImage() != null){
	//           //bufferedImage = page.getBufferedImage().getSubimage(this.getX(), this.getX(), (this.getI()-this.getX()), (this.getJ()-this.getY()));
	//       }
	//       this.bufferedImage = bufferedImage;
	    }

	@JsonProperty
    public void setData(Data data){
        this.data = data;
    }
    @JsonIgnore
    public void setScale(double scale) {
    	this.scale = scale*(1/this.addScale);
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
//    	logger.info("contains("+point+")");
        boolean flag = false;
        Point startPoint = new Point();
        Point stopPoint = new Point();
        startPoint.x = this.pointList.get(0).x*this.scale;
        startPoint.y = this.pointList.get(0).y*this.scale;
        stopPoint.x = this.pointList.get(1).x*this.scale;
        stopPoint.y = this.pointList.get(1).y*this.scale;
        
        if(startPoint.x < stopPoint.x && startPoint.y < stopPoint.y) {
            if(startPoint.x <= point.x && point.x <= stopPoint.x && startPoint.y <= point.y && point.y <= stopPoint.y){
                flag = true;
            }
        } else if (stopPoint.x < startPoint.x && stopPoint.y < startPoint.y) {
            if(stopPoint.x <= point.x && point.x <= startPoint.x && stopPoint.y <= point.y && point.y <= startPoint.y){
                flag = true;
            }
        }
        logger.info("contains("+point+") "+flag);
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
        startPoint.x = this.pointList.get(0).x*this.scale;
        startPoint.y = this.pointList.get(0).y*this.scale;
        stopPoint.x = this.pointList.get(1).x*this.scale;
        stopPoint.y = this.pointList.get(1).y*this.scale;
        logger.info("intersect(point) startPoint="+startPoint);
        logger.info("intersect(point) stopPoint="+stopPoint);
        //introduce the idea of a buffer where a user does not have to press exactly on line
        //TOP
        int margin = 20;
        if(point.x == startPoint.x && point.y == startPoint.y) {
        	logger.info("intersect("+point+") TOP_LEFT");
        	//Works
        	selection = TOP_LEFT;
        } else if(point.x > (stopPoint.x-margin) && point.x<(stopPoint.x+margin) && point.y > (stopPoint.y-margin) && point.y < (stopPoint.y+margin)) {//(point.x == stopPoint.x && point.y == stopPoint.y) {
        	logger.info("intersect("+point+") BOTTOM_RIGHT");
        	//Works
        	selection = BOTTOM_RIGHT;
        } else if(point.x > (stopPoint.x-margin) && point.x < (stopPoint.x+margin) && point.y > (startPoint.y-margin) && point.y < (startPoint.y + margin)) {
        	//Works
        	logger.info("intersect("+point+") TOP_RIGHT");
        	selection = TOP_RIGHT;
        } else if(point.x > (startPoint.x-margin) && point.x < (startPoint.x+margin) && point.y > (stopPoint.y - margin) && point.y < (stopPoint.y + margin)) {
        	//Works
        	logger.info("intersect("+point+") BOTTOM_LEFT");
        	selection = BOTTOM_LEFT;
        } else if(point.y > (startPoint.y-margin) && point.y < (startPoint.y+margin) && point.x > startPoint.x && point.x < stopPoint.x) {
        	//Works
        	logger.info("intersect("+point+") TOP");
        	selection = TOP;
        } else if(point.y > (startPoint.y-margin) && point.y < (startPoint.y+margin) && point.x > startPoint.x && point.x < stopPoint.x) {
        	//Not working
        	logger.info("intersect("+point+") BOTTOM");
        	selection = BOTTOM;
        } else if(point.x > (startPoint.x-margin) && point.x < (startPoint.x+margin) && point.y > startPoint.y && point.y < stopPoint.y) {
        	//Works
        	logger.info("intersect("+point+") LEFT");
        	selection = LEFT;
        } else if(point.x > (startPoint.x-margin) && point.x < (startPoint.x+margin) && point.y > startPoint.y && point.y < stopPoint.y) {
        	//Not working
        	logger.info("intersect("+point+") RIGHT");
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
        startPoint.x = this.pointList.get(0).x*this.scale;
        startPoint.y = this.pointList.get(0).y*this.scale;
        stopPoint.x = this.pointList.get(1).x*this.scale;
        stopPoint.y = this.pointList.get(1).y*this.scale;
    	switch(selection) {
	    	case TOP:{
	    		this.pointList.get(0).y = point.y;
	    		break;
	    	}
	    	case BOTTOM:{
	    		this.pointList.get(1).y = point.y;
	    		break;
	    	}
	    	case LEFT:{
	    		this.pointList.get(0).x = point.x;
	    		break;
	    	}
	    	case RIGHT:{
	    		this.pointList.get(1).x = point.x;
	    		break;
	    	}
	    	case TOP_LEFT:{
	    		this.pointList.set(0, point);
	    		break;
	    	}
	    	case TOP_RIGHT:{
	    		this.pointList.get(0).y = point.y;
	    		this.pointList.get(1).x = point.x;
	    		break;
	    	}
	    	case BOTTOM_LEFT:{
	    		this.pointList.get(0).x = point.x;
	    		this.pointList.get(1).y = point.y;
	    		break;
	    	}
	    	case BOTTOM_RIGHT:{
	    		this.pointList.set(1, point);
	    		break;
	    	}
    	}
    }
  
    @JsonIgnore
    public void move(Point point){
    	logger.info("move("+point+")");
        this.pointList.get(0).x = this.pointList.get(0).x + point.x;
        this.pointList.get(0).y = this.pointList.get(0).y + point.y;
        this.pointList.get(1).x = this.pointList.get(1).x + point.x;
        this.pointList.get(1).y = this.pointList.get(1).y + point.y;
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
            string = "uuid="+this.uuid;//+", scale="+this.scale;
        }
        return string;
    }
    
}
