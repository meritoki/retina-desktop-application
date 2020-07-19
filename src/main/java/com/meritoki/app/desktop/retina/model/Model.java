/*
 * Copyright 2020 Joaquin Osvaldo Rodriguez
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
package com.meritoki.app.desktop.retina.model;

import java.io.File;

import com.meritoki.app.desktop.retina.controller.document.DocumentController;
import com.meritoki.app.desktop.retina.model.document.Document;
import com.meritoki.app.desktop.retina.model.resource.Resource;
import com.meritoki.app.desktop.retina.model.system.System;

public class Model {
	/**
	 * System is an object that retains objects relative to the operation of Retina
	 */
	public System system = new System();
	/**
	 * Resource is an object that retains data that can be loaded for use in Retina
	 */
	public Resource resource = new Resource();
	/** 
	 * Document is an object that is manipulated by Retina
	 */
	public Document document = new Document();

	public Model() {
		this.document.pattern.user = this.system.user;
	}
	
	public void openDocument(File file) {
		this.system.file = file;
		this.document = (DocumentController.open(this.system.file));
		this.document.pattern.user = this.system.user;
		this.system.newDocument = false;
		this.resource.addRecent(this.system.file.getAbsolutePath());
	}
}
