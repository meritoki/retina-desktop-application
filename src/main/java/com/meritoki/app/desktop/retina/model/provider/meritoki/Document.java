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

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.meritoki.library.cortex.model.network.Color;
import com.meritoki.library.cortex.model.network.Cortex;
import com.meritoki.library.cortex.model.network.hexagon.Hexagonal;
import com.meritoki.library.cortex.model.retina.Retina;

public class Document {

	@JsonProperty
	public Cortex cortex;
	@JsonProperty
	public List<Input> inputList = new ArrayList<>();
	@JsonProperty
	public List<Output> outputList = new ArrayList<>();
	@JsonProperty
	public int index = 0;

	
	public Document() {
		this.cortex = new Hexagonal(Color.BRIGHTNESS, 0, 0, 27, 1, 0);
		this.cortex.load();
	}
	
	@JsonIgnore
	public boolean setIndex(String uuid) {
		boolean flag = false;
		for(int i=0;i<this.inputList.size();i++) {
			Input input = this.inputList.get(i);
			if(input.uuid.equals(uuid)) {
				this.index = i;
				flag = true;
				break;
			}
		}
		return flag;
	}
	
	@JsonIgnore
	public boolean setIndex(int index) {
		boolean flag = false;
		if (index >= 0 && index < this.inputList.size()) {
			this.index = index;
			flag = true;
		}
		return flag;
	}
	
	@JsonIgnore
	public Input getInput() {
		int size = this.inputList.size();
		Input page = (this.index < size && size > 0) ? this.inputList.get(this.index) : null;
		return page;
	}

	@JsonIgnore
	public Input getInput(int index) {
		int size = this.inputList.size();
		Input page = (index < size && size > 0) ? this.inputList.get(index) : null;
		return page;
	}
	
	@JsonIgnore
	public void addInput(Input input) {
		if(!inputList.contains(input)) {
			inputList.add(input);
		} else {
			for(Input i: inputList) {
				if(i.uuid.equals(input.uuid)) {
					if(i.shape != null) {
						i.shape.bufferedImage = input.shape.bufferedImage;
					} else if(i.page != null) {
						i.page.bufferedImage = input.shape.bufferedImage;
					}
				}
			}
		}
	}
}
