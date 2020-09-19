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
package com.meritoki.app.desktop.retina.model.provider.meritoki;

import java.awt.image.BufferedImage;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.meritoki.app.desktop.retina.model.document.Shape;
import com.meritoki.app.desktop.retina.model.document.Page;

public class Input {
	@JsonProperty
	public String uuid;
	@JsonProperty
	public Page page;
	@JsonProperty
	public Shape shape;
	@JsonProperty
	public String concept;
	@JsonProperty
	public boolean flag;
	
	public Input() {
		this.uuid = UUID.randomUUID().toString();
	}
	
	@JsonIgnore
	public boolean equals(Shape shape) {
		boolean flag = false;
		if(this.uuid.equals(shape.uuid)) {
			flag = true;
		}
		return flag;
	}
	
	public boolean hasPage(String uuid) {
		return (page != null)? page.uuid.equals(uuid): false;
	}
	
	public boolean hasShape(String uuid) {
		return (shape != null)? shape.uuid.equals(uuid): false;
	}
	
	public BufferedImage getBufferedImage() {
		Page page = this.page;
		Shape shape = this.shape;
		BufferedImage bufferedImage = (page != null) ? page.bufferedImage : null;
		bufferedImage = (bufferedImage == null)? shape.bufferedImage: bufferedImage;
		return bufferedImage;
	}
}
