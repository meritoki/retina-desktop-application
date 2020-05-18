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
package com.meritoki.app.desktop.retina.model.document;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;

public class Shape {
    
	@JsonIgnore
    private static Logger logger = LogManager.getLogger(Shape.class.getName());
	@JsonProperty
	public Type type;
    @JsonProperty
    public List<Point> pointList = new ArrayList<>(2);
    @JsonProperty
    public double addScale = 1;
    @JsonProperty
    public double scale = 1;
    @JsonProperty
    public String uuid = null;
    @JsonIgnore
    public BufferedImage bufferedImage = null;
    @JsonProperty
    public Data data = new Data();
    @JsonIgnore
    public boolean removed = false;
    @JsonIgnore
    public Dimension dimension = null;
    @JsonProperty
    public List<Text> textList = new ArrayList<>();
    
    public Shape(){
         this.uuid = UUID.randomUUID().toString();
    }
    
    /**
     * Copy constructor
     * @param shape
     */
    public Shape(Shape shape) {
    	this.uuid = shape.uuid;
    	this.type = shape.type;
    	this.scale = shape.scale;
    	this.addScale = shape.addScale;
    	this.dimension = new Dimension(shape.getDimension());
    	for(Point p : shape.pointList) {
    		this.pointList.add(new Point(p));
    	}
    	this.data = new Data(shape.data);
    }
    
    /**
     * DIMENSION 1
     * @return
     */
    @JsonIgnore
    public Dimension getDimension() {
    	if(this.dimension == null) {
	    	Dimension dimension = new Dimension();
	    	dimension.x = Math.min(this.pointList.get(0).x, this.pointList.get(1).x);
	    	dimension.y = Math.min(this.pointList.get(0).y, this.pointList.get(1).y);
	    	dimension.w = Math.abs(this.pointList.get(0).x - this.pointList.get(1).x);
	    	dimension.h = Math.abs(this.pointList.get(0).y - this.pointList.get(1).y);
	    	dimension.x *= this.scale;
	    	dimension.y *= this.scale;
	    	dimension.w *= this.scale;
	    	dimension.h *= this.scale;
	    	this.dimension = dimension;
    	}
    	return dimension;
    }
    
    public void setDimension(Dimension dimension) {
    	this.dimension = dimension;
    }
    
    @JsonProperty
    public void addText(Text text){
        logger.debug("addText("+text.value+")");
        this.textList.add(text);
    }
    
    /**
     * Function creates a Map from the textList that shows the frequency that a text value
     * has been input by a user
     * @return
     */
    @JsonIgnore
    public Map<String, Integer> getTextMap() {
    	int count = 0;
    	Map<String,Integer> textMap = new HashMap<>();
    	for(Text text: this.textList) {
          count = (textMap.get(text.value)!=null)?textMap.get(text.value):0;
          ++count;
          textMap.put(text.value, count);
    	}
    	return textMap;
    }
    
    /**
     * Function returns the text value with the highest frequency of input
     * @return
     */
    @JsonIgnore
    public Text getDefaultText(){
        int max = 0;
        Text text = new Text();
        int value = 0;
        for(Map.Entry<String, Integer> entry : this.getTextMap().entrySet()){
        	value = entry.getValue().intValue();
            if(value > max){
            	max = value;
                text = new Text();
                text.value = entry.getKey();
            }
        }
        return text;
    }
    
    @JsonProperty
    public List<Text> getTextList(){
        List<Text> textList = new ArrayList<>();
        Text text = null;
        for(Map.Entry<String, Integer> entry : this.getTextMap().entrySet()){
            text = new Text();
            text.value = entry.getKey();
            textList.add(text);
        }
        return textList;
    }
    
    @JsonIgnore
    public void sortPointList() {
    	Point pointZero = this.pointList.get(0);
    	Point pointOne = this.pointList.get(1);
    	//Case B
    	if(pointZero.x > pointOne.x && pointZero.y < pointOne.y) {
    		logger.info("sortPointList() Case B");
    		this.pointList.set(0, new Point(pointOne.x,pointZero.y));
    		this.pointList.set(1, new Point(pointZero.x,pointOne.y));
    	//Case C
    	} else if(pointZero.x < pointOne.x && pointZero.y > pointOne.y) {
    		logger.info("sortPointList() Case C");
    		this.pointList.set(0, new Point(pointZero.x,pointOne.y));
    		this.pointList.set(1, new Point(pointOne.x,pointZero.y));
    	//Case D
    	} else if(pointZero.x > pointOne.x && pointZero.y > pointOne.y) {
    		logger.info("sortPointList() Case D");
    		this.pointList.set(0, pointOne);
    		this.pointList.set(1, pointZero);
    	} else {
    		logger.info("sortPointList() sorted");
    	}
    }


    
    @JsonIgnore
    public boolean isValid(){
        boolean flag = true;
        if(this.pointList.get(0).x==this.pointList.get(1).x && this.pointList.get(0).y==this.pointList.get(1).y){
            flag = false;
        }
        return flag;
    }
    
    @JsonProperty
    public String getUUID() {
    	return this.uuid;
    }
    
    @JsonProperty
	public Data getData(){
	    return this.data;
	}

	@JsonIgnore
	public double getCenterX(){
		Dimension dimension = this.getDimension();
		double x = dimension.x+(dimension.w/2);
		return x;
//	    return (this.pointList.get(0).x + this.pointList.get(1).x)/2;
	}

	@JsonIgnore
	public double getCenterY(){
		Dimension dimension = this.getDimension();
		double y = dimension.y+(dimension.h/2);
		return y;
//	    return (this.pointList.get(0).y+this.pointList.get(1).y)/2;
	}
	
	public List<Point> getPointList() {
		return this.pointList;
	}

	@JsonProperty
    public void setData(Data data){
        this.data = data;
    }
	
	@JsonIgnore
	public void setAddScale(double addScale) {
		this.addScale = addScale;
	}
    @JsonIgnore
    public void setScale(double scale) {
    	this.scale = scale*(1/this.addScale);
    }
    
    public void setPointList(List<Point> pointList) {
    	this.pointList = pointList;
    }
    
    @JsonIgnore
    public boolean containsPoint(Point point){
    	logger.info("containsPoint("+point+")");
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
        return flag;
    }
    
    public boolean intersects(Point point) {
    	boolean flag  = false;
    	if(this.intersect(point) != null) {
    		flag = true;
    	}
    	return flag;
    }
    
    @JsonIgnore
    public Selection intersect(Point point){
        Selection selection = null;
        Point startPoint = new Point();
        Point stopPoint = new Point();
        startPoint.x = this.pointList.get(0).x*this.scale;
        startPoint.y = this.pointList.get(0).y*this.scale;
        stopPoint.x = this.pointList.get(1).x*this.scale;
        stopPoint.y = this.pointList.get(1).y*this.scale;
        //introduce the idea of a buffer where a user does not have to press exactly on line
        //TOP
        double margin = 20*this.scale;
        if(point.x == startPoint.x && point.y == startPoint.y) {
        	logger.info("intersect("+point+") TOP_LEFT");
        	selection = Selection.TOP_LEFT;
        } else if(point.x > (stopPoint.x-margin) && point.x<(stopPoint.x+margin) && point.y > (stopPoint.y-margin) && point.y < (stopPoint.y+margin)) {
        	logger.info("intersect("+point+") BOTTOM_RIGHT");
        	selection = Selection.BOTTOM_RIGHT;
        } else if(point.x > (stopPoint.x-margin) && point.x < (stopPoint.x+margin) && point.y > (startPoint.y-margin) && point.y < (startPoint.y + margin)) {
        	logger.info("intersect("+point+") TOP_RIGHT");
        	selection = Selection.TOP_RIGHT;
        } else if(point.x > (startPoint.x-margin) && point.x < (startPoint.x+margin) && point.y > (stopPoint.y - margin) && point.y < (stopPoint.y + margin)) {
        	logger.info("intersect("+point+") BOTTOM_LEFT");
        	selection = Selection.BOTTOM_LEFT;
        } else if(point.y >= (startPoint.y) && point.y < (startPoint.y+margin) && point.x > startPoint.x && point.x < stopPoint.x) {
        	logger.info("intersect("+point+") TOP");
        	selection = Selection.TOP;
        } else if(point.y > (stopPoint.y-margin) && point.y <= (stopPoint.y) && point.x > startPoint.x && point.x < stopPoint.x) {
        	logger.info("intersect("+point+") BOTTOM");
        	selection = Selection.BOTTOM;
        } else if(point.x >= (startPoint.x) && point.x < (startPoint.x+margin) && point.y > startPoint.y && point.y < stopPoint.y) {
        	logger.info("intersect("+point+") LEFT");
        	selection = Selection.LEFT;
        } else if(point.x > (stopPoint.x-margin) && point.x <= (stopPoint.x) && point.y > startPoint.y && point.y < stopPoint.y) {
        	logger.info("intersect("+point+") RIGHT");
        	selection = Selection.RIGHT;
        }
        return selection;
    }
    

    
    /**
     * DIMENSION 2
     * @param point
     * @param selection
     */
    public void resize(Point point, Selection selection) {
    	Point startPoint = new Point();
        Point stopPoint = new Point();
        startPoint.x = this.pointList.get(0).x*this.scale;
        startPoint.y = this.pointList.get(0).y*this.scale;
        stopPoint.x = this.pointList.get(1).x*this.scale;
        stopPoint.y = this.pointList.get(1).y*this.scale;
    	switch(selection) {
	    	case TOP:{
	    		logger.info("resize("+point+", "+selection+") TOP");
	    		this.pointList.get(0).y = point.y;
	    		break;
	    	}
	    	case BOTTOM:{
	    		logger.info("resize("+point+", "+selection+") BOTTOM");
	    		this.pointList.get(1).y = point.y;
	    		break;
	    	}
	    	case LEFT:{
	    		logger.info("resize("+point+", "+selection+") LEFT");
	    		this.pointList.get(0).x = point.x;
	    		break;
	    	}
	    	case RIGHT:{
	    		logger.info("resize("+point+", "+selection+") RIGHT");
	    		this.pointList.get(1).x = point.x;
	    		break;
	    	}
	    	case TOP_LEFT:{
	    		logger.info("resize("+point+", "+selection+") TOP_LEFT");
	    		this.pointList.set(0, point);
	    		break;
	    	}
	    	case TOP_RIGHT:{
	    		logger.info("resize("+point+", "+selection+") TOP_RIGHT");
	    		this.pointList.get(0).y = point.y;
	    		this.pointList.get(1).x = point.x;
	    		break;
	    	}
	    	case BOTTOM_LEFT:{
	    		logger.info("resize("+point+", "+selection+") BOTTOM_LEFT");
	    		this.pointList.get(0).x = point.x;
	    		this.pointList.get(1).y = point.y;
	    		break;
	    	}
	    	case BOTTOM_RIGHT:{
	    		logger.info("resize("+point+", "+selection+") BOTTOM_RIGHT");
	    		this.pointList.set(1, point);
	    		break;
	    	}
    	}
    }
  
    @JsonIgnore
    public void move(Point point){
    	logger.info("move("+point+")");
    	logger.info("move("+point+") this.pointList="+this.pointList);
        this.pointList.get(0).x = this.pointList.get(0).x + point.x;
        this.pointList.get(0).y = this.pointList.get(0).y + point.y;
        this.pointList.get(1).x = this.pointList.get(1).x + point.x;
        this.pointList.get(1).y = this.pointList.get(1).y + point.y;
        logger.info("move("+point+") this.pointList="+this.pointList);
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
                logger.error("IOException "+ex.getMessage());
            }
        } else {
            string = "("+this.data.getText().value+","+this.getCenterX()+","+this.getCenterY()+")";
        }
        return string;
    }
}
//@JsonIgnore
//public static final String RECTANGLE = "rectangle";
//@JsonIgnore
//public static final String ELLIPSE = "ellipse";
//@JsonProperty
//public String classification = null;s
//@JsonIgnore
//public int intersect(Point point){
//  int selection = -1;
//  Point startPoint = new Point();
//  Point stopPoint = new Point();
//  startPoint.x = this.pointList.get(0).x*this.scale;
//  startPoint.y = this.pointList.get(0).y*this.scale;
//  stopPoint.x = this.pointList.get(1).x*this.scale;
//  stopPoint.y = this.pointList.get(1).y*this.scale;
//  //introduce the idea of a buffer where a user does not have to press exactly on line
//  //TOP
//  double margin = 20*this.scale;
////  if(point.x == startPoint.x && point.y == startPoint.y) {
////  	logger.info("intersect("+point+") TOP_LEFT");
////  	//Works
////  	selection = TOP_LEFT;
////  } else if(point.x > (stopPoint.x-margin) && point.x<(stopPoint.x+margin) && point.y > (stopPoint.y-margin) && point.y < (stopPoint.y+margin)) {
////  	logger.info("intersect("+point+") BOTTOM_RIGHT");
////  	//Works
////  	selection = BOTTOM_RIGHT;
////  } else if(point.x > (stopPoint.x-margin) && point.x < (stopPoint.x+margin) && point.y > (startPoint.y-margin) && point.y < (startPoint.y + margin)) {
////  	//Works
////  	logger.info("intersect("+point+") TOP_RIGHT");
////  	selection = TOP_RIGHT;
////  } else if(point.x > (startPoint.x-margin) && point.x < (startPoint.x+margin) && point.y > (stopPoint.y - margin) && point.y < (stopPoint.y + margin)) {
////  	//Works
////  	logger.info("intersect("+point+") BOTTOM_LEFT");
////  	selection = BOTTOM_LEFT;
//////  } else if(point.y > (startPoint.y-margin) && point.y < (startPoint.y+margin) && point.x > startPoint.x && point.x < stopPoint.x) {
////  } else 
//  if(point.y >= (startPoint.y) && point.y < (startPoint.y+margin) && point.x > startPoint.x && point.x < stopPoint.x) {
//  	//Works
//  	logger.info("intersect("+point+") TOP");
//  	selection = TOP;
////  } else if(point.y > (stopPoint.y-margin) && point.y < (stopPoint.y+margin) && point.x > startPoint.x && point.x < stopPoint.x) {
//  } else if(point.y > (stopPoint.y-margin) && point.y <= (stopPoint.y) && point.x > startPoint.x && point.x < stopPoint.x) {
//  	//Not working
//  	logger.info("intersect("+point+") BOTTOM");
//  	selection = BOTTOM;
////  } else if(point.x > (startPoint.x-margin) && point.x < (startPoint.x+margin) && point.y > startPoint.y && point.y < stopPoint.y) {
//  } else if(point.x >= (startPoint.x) && point.x < (startPoint.x+margin) && point.y > startPoint.y && point.y < stopPoint.y) {
//  	//Works
//  	logger.info("intersect("+point+") LEFT");
//  	selection = LEFT;
////  } else if(point.x > (stopPoint.x-margin) && point.x < (stopPoint.x+margin) && point.y > startPoint.y && point.y < stopPoint.y) {
//  } else if(point.x > (stopPoint.x-margin) && point.x <= (stopPoint.x) && point.y > startPoint.y && point.y < stopPoint.y) {
//  	//Not working
//  	logger.info("intersect("+point+") RIGHT");
//  	selection = RIGHT;
//  }
//  return selection;
//}
//@JsonIgnore
//public static final int TOP = 0;
//@JsonIgnore
//public static final int BOTTOM = 1;
//@JsonIgnore
//public static final int LEFT = 2;
//@JsonIgnore
//public static final int RIGHT = 3;
//@JsonIgnore
//public static final int TOP_LEFT = 4;
//@JsonIgnore
//public static final int TOP_RIGHT = 5;
//@JsonIgnore
//public static final int BOTTOM_LEFT = 6;
//@JsonIgnore
//public static final int BOTTOM_RIGHT = 7;
//public double round(double value, int places) {
//if (places < 0) throw new IllegalArgumentException();
//
//long factor = (long) Math.pow(10, places);
//value = value * factor;
//long tmp = Math.round(value);
//return (double) tmp / factor;
//}

/**
 * Used by data Matrix algorithm
 * @param u
 * @return
 */
//public int compareTo(Object u) {
//  if (this.pointList.get(0).x == 0 || ((Shape)u).startPoint.x == 0) {
//    return 0;
//  }
//  return ((Integer)this.pointList.get(0).x).compareTo(((Shape)u).startPoint.x);
//}
