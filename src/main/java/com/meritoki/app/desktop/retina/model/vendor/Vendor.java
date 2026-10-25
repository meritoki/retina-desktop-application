/*
 * Copyright 2021 Joaquin Osvaldo Rodriguez
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
package com.meritoki.app.desktop.retina.model.vendor;

import java.util.Map;
import java.util.TreeMap;

import com.meritoki.app.desktop.retina.model.Model;
import com.meritoki.app.desktop.retina.view.frame.MainFrame;

public class Vendor {

	public String name;
	public MainFrame mainFrame;
	public Model model;
	public Map<String,Product> productMap = new TreeMap<>();
	
	public Vendor(String name) {
		this.name = name;
	}
	
	public void setMainFrame(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
	}
	
	public void setModel(Model model) {
		this.model = model;
	}
	
	public boolean isAvailable() throws Exception{
		return true;
	}
	
	public void initialize() {
		
	}
	
	public void execute() throws Exception {
		
	}
	
	public void save() {
		
	}
	

}
