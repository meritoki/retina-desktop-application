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
package com.meritoki.retina.application.desktop.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.meritoki.retina.application.desktop.model.project.Data;
import com.meritoki.retina.application.desktop.model.project.Page;

/**
 *
 * @author osvaldo.rodriguez
 */
public class Script {
    
    public List<Page> pageList = new ArrayList<>();
    public List<LinkedList<Data>> dataMatrix = new ArrayList<>();
    
    public void setPageList(List<Page> pageList){
        this.pageList = pageList;
    }
    
    public List<Page> getPageList(){
        return this.pageList;
    }
    
//    public void setDataMatrix(List<LinkedList<Data>> dataMatrix) {
//        this.dataMatrix = dataMatrix;
//    }
    
//    public List<LinkedList<Data>> getDataMatrix() {
//        return this.dataMatrix;
//    }
    
//Page Sort
    
    /**
     * SWAP 1-2:3-4
     * INTERLACE 1-2:3-4
     * INSERT 1-2:3-4
     * @param value
     * @throws Exception
     */
    public void sortPageList(String value) throws Exception{
        value = value.replace("\n", "").replace("\r", "");
        String[] instructions = value.split(";");
        String instruction;
        String[] parameters;
        String a;
        String b;
        for(String i: instructions){
            System.out.println(i);
            if(i.contains("SWAP")){
                instruction=i.replaceFirst("SWAP", "");
                parameters=instruction.split(":");
                a=parameters[0].trim();
                b=parameters[1].trim();
                this.swapPage(a, b);
            } else if(i.contains("INTERLACE")){
                instruction=i.replaceFirst("INTERLACE", "");
                parameters=instruction.split(":");
                a=parameters[0].trim();
                b=parameters[1].trim();
                this.interlacePage(a, b);
            } else if(i.contains("INSERT")){
                instruction=i.replaceFirst("INSERT", "");
                parameters=instruction.split(":");
                a=parameters[0].trim();
                b=parameters[1].trim();
                this.insertPage(a,b);
            }
        }
    }
    
    public void swapPage(String a, String b) throws Exception {
        System.out.println("swap("+a+","+b+")");
        String[] A;
        String[] B;
        String i;
        String j;
        int x;
        int y;
        int diff = 0;
        if(a.contains("-") && b.contains("-")){
            A = a.split("-");
            B = b.split("-");
            i = A[0];
            j = A[1];
            x = Integer.parseInt(i);
            y = Integer.parseInt(j);
            diff = y-x;
            i = B[0];
            j = B[1];
            x = Integer.parseInt(i);
            y = Integer.parseInt(j);
            if(diff == (y-x)){
                System.out.println("valid");
            }
        }else{
            i = a;
            j = b;
            x = Integer.parseInt(i);
            y = Integer.parseInt(j);
            this.swapPage(x, y);
        }
    }
    
    public void swapPage(int a, int b){
       System.out.println("swap("+a+","+b+")");
       Collections.swap(this.pageList, a, b);
    }
    
    
    
    public void interlacePage(String a, String b) throws Exception {
        System.out.println("interlace("+a+","+b+")");
        String[] A;
        String[] B;
        if(a.contains("-") && b.contains("-")){
            A = a.split("-");
            B = b.split("-");
        }
    }
    
    public void insertPage(String a, String b) throws Exception {
        System.out.println("insert("+a+","+b+")");
        String[] A;
        String[] B;
        String i;
        String j;
        int x;
        int y;
        int diff = 0;
        if(a.contains("-") && b.contains("-")){
            A = a.split("-");
            B = b.split("-");
            i = A[0];
            j = A[1];
            x = Integer.parseInt(i);
            y = Integer.parseInt(j);
            diff = y-x;
            i = B[0];
            j = B[1];
            x = Integer.parseInt(i);
            y = Integer.parseInt(j);
            if(diff == (y-x)){
                System.out.println("valid");
            }
        }else{
            i = a;
            j = b;
            x = Integer.parseInt(i);
            y = Integer.parseInt(j);
            this.insertPage(x, y);
        }
    }
    
    public void insertPage(int a, int b){
        Page page = this.pageList.remove(a);
        this.pageList.add(b, page);
    }
    
    public void insert(int index, int a, int b, int x, int y){
        Page page = this.pageList.get(index);
        Data data = page.getDataMatrix().get(a).remove(b);
        page.getDataMatrix().get(x).add(y, data);
    }
    
//Data Matrix Sort
    
    /**
     * COMMAND PAGE(S) ROW,COLUMN:ROW,COLUMN
     * SWAP 0-n 1,2:3,4 | SWAP 0 1,2:3,4
     * INSERT 0-n 1,2:3,4 | INSERT 0 1,2:3,4
     * MERGE 0-n 1,1:1,2 | MERGE 0 1,1:1,2
     * COMMAND PAGE(S) RANGE:RANGE
     * JOIN 0-n 1:1 | JOIN 0-1 1-6:2-7 | JOIN even-odd RANGE:RANGE
     * COMMAND PAGE(S) COL/ROW RANGE:RANGE 
     * JOIN 0-n ROW 1:1 | JOIN 0-1 ROW 1-6:2-7
     * 
     * @param value
     * @throws Exception
     */
    public void sortDataMatrix(String value) throws Exception{
        value = value.replace("\n", "").replace("\r", "");
        String[] instructions = value.split(";");
        String instruction;
        String[] parameters;
        String a;
        String b;
        for(String i: instructions){
            System.out.println(i);
            if(i.contains("SWAP")){//0:0 0:0;
                instruction=i.replaceFirst("SWAP", "");
                instruction = instruction.trim();
                parameters=instruction.split(" ");
                a=parameters[0].trim();
                b=parameters[1].trim();
                this.swapData(a, b);
            }else if(i.contains("INSERT")){//0:0 0:0
                instruction=i.replaceFirst("INSERT", "");
                parameters=instruction.split(":");
                a=parameters[0].trim();
                b=parameters[1].trim();
                this.insertData(a,b);
            } else if(i.contains("MERGE")){
                instruction=i.replaceFirst("MERGE", "");
                parameters=instruction.split(":");
                a=parameters[0].trim();
                b=parameters[1].trim();
                this.mergeData(a,b);
            } else if(i.contains("JOIN")){//0:0 0:0
                instruction=i.replaceFirst("JOIN", "");
                parameters=instruction.split(":");
                a=parameters[0].trim();
                b=parameters[1].trim();
                this.joinData(a, b);
            } 
        }
    }
    
    public void swapData(String a, String b) {
    	String[] pageArray = new String[1];
    	int[] pageIntArray = new int [0];
    	if(a.contains("-")) {
    		pageArray = a.split("-");
    		int startInt = Integer.parseInt(pageArray[0]);
    		int stopInt = Integer.parseInt(pageArray[1]);
    		int count = stopInt - startInt;
    		pageIntArray = new int[count];
    		for(int i=0;i<count;i++) {
    			pageIntArray[i] = startInt + i;
    		}
    	} else if(a.contains(",")) {
    		pageArray = a.split(",");
    	} else {
    		pageArray[0] = a;
    	}
    	String[] coordinateArray = b.split(":");
    	String[] coordinateZero = coordinateArray[0].split(",");
    	String[] coordinateOne = coordinateArray[1].split(",");
    	Page page = null;
    	for(int i=0;i<pageIntArray.length;i++) {
    		page = this.pageList.get(pageIntArray[i]);
    		
    	}
    }
    
    public void insertData(String a, String b) {
    	
    }
    
    public void mergeData(String a, String b) {
    	
    }
    
   public void joinData(String a, String b) {
    	
    }
    
    public void joinData(int a, int b) {
        Page pageA = this.pageList.get(a);
        Page pageB = this.pageList.get(b);
        List<LinkedList<Data>> dataA = pageA.getDataMatrix();
        List<LinkedList<Data>> dataB = pageB.getDataMatrix();
        List<LinkedList<Data>> data = new ArrayList<>((dataA.size()>dataB.size())?dataA.size():dataB.size());
        for(int i = 0; i < dataA.size(); i++){
            for(int j=0;j<dataA.get(i).size();j++){
                data.get(i).add(dataA.get(i).get(j));
            }
        }
        for(int i = 0; i < dataB.size(); i++){
            for(int j=0;j<dataB.get(i).size();j++){
                data.get(i).add(dataB.get(i).get(j));
            }
        }
//        pageA.setDataMatrix(data);
//        pageB.setDataMatrix(null);
    }
}
