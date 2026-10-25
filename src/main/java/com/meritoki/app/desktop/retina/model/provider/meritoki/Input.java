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
package com.meritoki.app.desktop.retina.model.provider.meritoki;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.meritoki.library.controller.node.NodeController;
import com.meritoki.library.cortex.model.*;

public class Input {
	@JsonProperty
	public String uuid;
	@JsonIgnore
	public BufferedImage bufferedImage;
	@JsonIgnore
	public File file;
	@JsonProperty
	public String filePath;
	@JsonProperty
	public String fileName;
	@JsonProperty
	public Concept concept;
	@JsonProperty
	public boolean scan = true;
	
	public Input() {
		this.uuid = UUID.randomUUID().toString();
	}
	
	@JsonIgnore
	public boolean equals(Object input) {
		boolean flag = false;
		if(input instanceof Input && this.uuid != null && ((Input)input).uuid != null && this.uuid.equals(((Input)input).uuid)) {
			flag = true;
		}
		return flag;
	}
	
	public BufferedImage getBufferedImage() {
		if (this.bufferedImage == null) {
			System.out.println("getBufferedImage() filePath="+filePath+" fileName="+fileName);
			this.file = new File(filePath + NodeController.getSeperator() + fileName);
			if (this.file.exists()) {
				this.bufferedImage = NodeController.openBufferedImage(this.file);
			}
		}
		return this.bufferedImage;
	}
}

//Page page = this.page;
//Shape shape = this.shape;
//BufferedImage bufferedImage = (page != null) ? page.bufferedImage : null;
//bufferedImage = (shape != null && bufferedImage == null)? shape.bufferedImage: bufferedImage;
