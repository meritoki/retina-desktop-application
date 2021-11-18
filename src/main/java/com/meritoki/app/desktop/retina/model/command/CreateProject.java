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
package com.meritoki.app.desktop.retina.model.command;

import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.meritoki.app.desktop.retina.controller.node.NodeController;
import com.meritoki.app.desktop.retina.model.Model;
import com.meritoki.app.desktop.retina.model.document.Image;

public class CreateProject extends Command {

	public CreateProject(Model document) {
		super(document, "createProject");
	}
	
	public void execute() throws Exception {
		if(this.model.cache.fileArray.length > 0) {
		String file = this.model.cache.fileArray[0];
		List<File> fileList = new ArrayList<>();
		if(this.model.system.file != null) {
			fileList.add(this.model.system.file);
			for(Image image: this.model.document.getImageList()) {
				fileList.add(new File(NodeController.getDocumentCache(model.document.uuid)+File.separatorChar+image.uuid+"." + image.getExtension()));
			}
			NodeController.zip(fileList, file);
		} else {
			throw new Exception("Please Save Document First");
		}
		} else {
			throw new Exception("File Is Null");
		}
	}
}
