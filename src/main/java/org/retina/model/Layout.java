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
import java.util.List;

/**
 *
 * @author osvaldo.rodriguez
 */
public class Layout {
    
    List<org.retina.model.Rectangle> rectangleList = new ArrayList<org.retina.model.Rectangle>();
    
    public void setRectangleList(List<org.retina.model.Rectangle> rectangleList){
        this.rectangleList = rectangleList;
    }
    
    public List<org.retina.model.Rectangle> getRectangleList(){
        return this.rectangleList;
    }
}
