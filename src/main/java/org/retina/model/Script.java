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

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author osvaldo.rodriguez
 */
public class Script {
    
    public List<Image> imageList = new ArrayList<>();
//    public List<LinkedList<Data>> data = new ArrayList<>();
    
    public void setImageList(List<Image> imageList){
        this.imageList = imageList;
    }
    
    public List<Image> getImageList(){
        return this.imageList;
    }
    
//    public void setData(List<LinkedList<Data>> data) {
//        this.data = data;
//    }
//    
//    public List<LinkedList<Data>> getData() {
//        return this.data;
//    }
    
    public void image(String value) throws Exception{
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
                this.swap(a, b);
            } else if(i.contains("INTERLACE")){
                instruction=i.replaceFirst("INTERLACE", "");
                parameters=instruction.split(":");
                a=parameters[0].trim();
                b=parameters[1].trim();
                this.interlace(a, b);
            } else if(i.contains("INSERT")){
                instruction=i.replaceFirst("INSERT", "");
                parameters=instruction.split(":");
                a=parameters[0].trim();
                b=parameters[1].trim();
                this.insert(a,b);
            }
        }
    }
    
    public void page(String value) throws Exception{
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
                parameters=instruction.split(":");
                a=parameters[0].trim();
                b=parameters[1].trim();
                this.swap(a, b);
            } else if(i.contains("JOIN")){//0:0 0:0
                instruction=i.replaceFirst("JOIN", "");
                parameters=instruction.split(":");
                a=parameters[0].trim();
                b=parameters[1].trim();
                this.interlace(a, b);
            } else if(i.contains("INSERT")){//0:0 0:0
                instruction=i.replaceFirst("INSERT", "");
                parameters=instruction.split(":");
                a=parameters[0].trim();
                b=parameters[1].trim();
                this.insert(a,b);
            } else if(i.contains("MERGE")){
                instruction=i.replaceFirst("MERGE", "");
                parameters=instruction.split(":");
                a=parameters[0].trim();
                b=parameters[1].trim();
                this.insert(a,b);
            }
        }
    }
    
    public void swap(String a, String b) throws Exception {
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
            this.swap(x, y);
        }
    }
    
    public void swap(int a, int b){
       System.out.println("swap("+a+","+b+")");
       Collections.swap(this.imageList, a, b);
    }
    
    
    
    public void interlace(String a, String b) throws Exception {
        System.out.println("interlace("+a+","+b+")");
        String[] A;
        String[] B;
        if(a.contains("-") && b.contains("-")){
            A = a.split("-");
            B = b.split("-");
        }
    }
    
    public void insert(String a, String b) throws Exception {
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
            this.insert(x, y);
        }
    }
    
    public void insert(int a, int b){
        Image image = this.imageList.remove(a);
        this.imageList.add(b, image);
    }
    
    public void insert(int index, int a, int b, int x, int y){
        Image image = this.imageList.get(index);
        Page page = image.getPage();
        Data data = page.getDataMatrix().get(a).remove(b);
        page.getDataMatrix().get(x).add(y, data);
    }
    
    public void join(int a, int b) {
        Image imageA = this.imageList.get(a);
        Image imageB = this.imageList.get(b);
        List<LinkedList<Data>> dataA = imageA.getPage().getDataMatrix();
        List<LinkedList<Data>> dataB = imageB.getPage().getDataMatrix();
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
        imageA.getPage().setDataMatrix(data);
        imageB.getPage().setDataMatrix(null);
    }
}
