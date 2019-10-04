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

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
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
public class Page {
    
    static Logger logger = LogManager.getLogger(Page.class.getName());
    public String fileName = null;
    public String filePath = null;
    public String localURL;
    public String remoteURL;
    public String uuid;
    public Dimension dimension;
    private BufferedImage bufferedImage = null;
    private List<org.retina.model.Rectangle> rectangleList = new ArrayList<>();
    private Data data;
    private List<Data> dataList = new LinkedList<>();
    private List<LinkedList<Data>> dataMatrix = new ArrayList<>();
    public int index = 0;
    
    public Page() {
        UUID uuid = UUID.randomUUID();
        this.uuid = uuid.toString();
    }
    
//methods imported from Image/////////////////////////////
    @JsonProperty
    public List<org.retina.model.Rectangle> getRectangleList() {
        return this.rectangleList;
    }
    
    @JsonIgnore
    public void setRectangle(String uuid) {
        logger.debug("setRectangle("+uuid+")");
        Rectangle rectangle = null;
        for(int i = 0; i < this.rectangleList.size(); i++){
            rectangle = this.rectangleList.get(i);
            if(rectangle.uuid.equals(uuid)){
                this.index = i;
                break;
            }
        }
    }
    
    @JsonIgnore
    public Rectangle getRectangle() {
        Rectangle rectangle = null;
        if(this.index >= 0 && this.index < this.rectangleList.size()) {
            rectangle = this.rectangleList.get(this.index);
        }
        return rectangle;
    }
    
//    @JsonProperty
//    @JsonIgnore
//    public void setRectangleList(List<org.retina.model.Rectangle> rectangleList){
//        if(rectangleList!=null&&rectangleList.size()>0)
//            logger.info("setRectangleList("+rectangleList+")");
//        this.rectangleList = rectangleList;
//        this.page.setRectangleList(this.rectangleList);
//    }
    
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
//////////////////////////////////////////////////////////////////////////////
    
    @JsonIgnore
    public void setIndex(int index) {
        logger.debug("setIndex("+index+")");
        if(index >= 0 && index < this.dataList.size()) {
            this.index = index;
            this.setData(this.dataList.get(this.index));
        }
    }
    
    @JsonIgnore
    public int getIndex(){
        logger.debug("getIndex() this.index="+this.index);
        return this.index;
    }
    
    @JsonIgnore
    public void setData(Data data) {
        logger.info("setData("+data+")");
        this.data = data;
    }
    
    @JsonIgnore
    public Data getData() {
        return this.data;
    }
    
    public void setDataMatrix(List<LinkedList<Data>> dataMatrix) {
        if(dataMatrix !=null&&dataMatrix.size()>0)
            logger.info("setDataMatrix("+dataMatrix+")");
        this.dataMatrix = dataMatrix;
    }
    
    //Algorithm that converts rectangles into data matrix
    @JsonIgnore
    public void setRectangleList(List<Rectangle> rList) {
        if(rList!=null&&rList.size()>0)
            logger.info("setRectangleList("+rList+")");
        this.rectangleList = rList;
        ArrayList<Rectangle> rectangleList = new ArrayList<>(rList);
        List<LinkedList<Data>> dataMatrix = null;
        Rectangle a = null;
        int averageY;
        int threshold = 32;
        List<Rectangle> columnList;
        List<List<Rectangle>> rowList = new ArrayList<>();
        boolean flag = true;
        for(int i=0;i<rectangleList.size();i++){
            a = rectangleList.get(i);
            columnList = new ArrayList<>();
            if(!rowList.isEmpty()){
                for(List<Rectangle> cList: rowList){
                    averageY = this.getRectangleListYAverage(cList, a);
                    columnList = new ArrayList<>(cList);
                    columnList.add(a);
                    if(this.isRectangleListYInThreshold(columnList, averageY, threshold)){
                        cList.add(a);
                        flag = false;
                    }
                }
                if(flag){
                    columnList = new ArrayList<>();
                    columnList.add(a);
                    rowList.add(columnList);
                } else {
                    flag = true;
                }
            }else{
                columnList.add(a);
                rowList.add(columnList);
            }
        }
        int row = rowList.size();
        int columnMax = 0;
        int size = 0;
        for(List<Rectangle> cList:rowList){
            size = cList.size();
            if(size > columnMax) {
                columnMax = size;
            }
        }
        rowList = this.sortRowList(rowList);
        dataMatrix = new LinkedList<>();
        for(int i = 0; i<row;i++){
            dataMatrix.add(i, new LinkedList<Data>());
            for(int j = 0; j < rowList.get(i).size();j++){
                a = rowList.get(i).get(j);
                if(a != null){
                    dataMatrix.get(i).add(j, a.data);
                }
            }
        }
        this.printDataMatrix(dataMatrix);
        this.setDataMatrix(dataMatrix);
        this.initDataList();
    }
    
    public List<LinkedList<Data>> getDataMatrix() {
        return this.dataMatrix;
    }
    
    public void printDataMatrix(List<LinkedList<Data>> data){
        String string = null;
        if(data != null && data.size()> 0) {
            string = "\n";
            for(int i = 0; i<data.size();i++){
                for(int j = 0; j < data.get(i).size();j++){
                    if(data.get(i).get(j) != null){
                        string += "*";
                    }
                }
                if(i<(data.size()-1))
                    string +="\n";
            }
        }
        if(string != null)
            logger.info(string);
    }
    
    public boolean isRectangleListYInThreshold(List<Rectangle> rectangleList, int averageY, int threshold) {
        logger.debug("isRectangleListYInThreshold("+rectangleList+", "+averageY+", "+threshold+")");
        boolean flag = true;
        int a = 0;
        for(Rectangle r:rectangleList){
            a = r.getCenterY();
            a = Math.abs(averageY-a);
            if(a > threshold){
                flag = false;
                break;
            }
        }
        return flag;
    }
    
    public int getRectangleListYAverage(List<Rectangle> rectangleList, Rectangle rectangle) {
        logger.debug("getRectangleListYAverage("+rectangleList+", "+rectangle+")");
        int count = 0;
        int sum = 0;
        for(Rectangle r: rectangleList){
            sum += r.getCenterY();
            count+=1;
        }
        count +=1;
        sum +=rectangle.getCenterY();
        return sum/count;
    }
    
    public List<List<Rectangle>> sortRowList(List<List<Rectangle>> rowList) {
        for (int i = 0; i < rowList.size(); i++) {
            this.sortColumnList(rowList.get(i));
            for (int j = rowList.size() - 1; j > i; j--) {
                if (rowList.get(i).get(0).getCenterY() > rowList.get(j).get(0).getCenterY()) {
                    List<Rectangle> tmp = rowList.get(i);
                    rowList.set(i,rowList.get(j)) ;
                    rowList.set(j,tmp);
                }
            }
        }
        return rowList;
    }
    
    public List<Rectangle> sortColumnList(List<Rectangle> rectangleList) {
        for (int i = 0; i < rectangleList.size(); i++) {
            for (int j = rectangleList.size() - 1; j > i; j--) {
                if (rectangleList.get(i).getX() > rectangleList.get(j).getX()) {
                    Rectangle tmp = rectangleList.get(i);
                    rectangleList.set(i,rectangleList.get(j)) ;
                    rectangleList.set(j,tmp);
                }
            }
        }
        return rectangleList;
    }
    
    public boolean columnListContains(List<Rectangle> rectangleList, Rectangle rectangle) {
        boolean flag = false;
        for(Rectangle r:rectangleList){
            if(r.uuid.equals(rectangle.uuid)){
                flag = true;
            }
        }
        return flag;
    }
    
    public boolean rowListContains(List<List<Rectangle>> rowList, Rectangle rectangle ){
        boolean flag = false;
        for(List<Rectangle> r:rowList){
            flag = this.columnListContains(r, rectangle);
            if(flag) {
                break;
            }
        }
        return flag;
    }
    
    public void setDataList(List<Data> dataList) {
        this.dataList = dataList;
    }
    
    public List<Data> getDataList() {
        return this.dataList;
    }
    
    public void initDataList(){
        logger.debug("initDataList()");
        this.dataList = new LinkedList<Data>();
        for(int i=0;i<this.dataMatrix.size();i++){
            for(int j=0;j<this.dataMatrix.get(i).size();j++){
                dataList.add(this.dataMatrix.get(i).get(j));
            }
        }
    }
}
