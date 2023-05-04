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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.meritoki.library.cortex.model.cortex.Cortex;

public class Document {

	@JsonProperty
	public Cortex cortex = null;//new Hexagonal(Color.BRIGHTNESS, 0, 0, 27, 1, 0);
	// Retains inputs that have been completed;
	@JsonProperty
	public Map<String, Input> inputMap = new HashMap<>();

	public Document() {
//		this.cortex.load();
	}

	public List<Input> getInputList() {
		List<Input> inputList = new ArrayList<>();
		for (Entry<String, Input> entry : this.inputMap.entrySet()) {
			inputList.add(entry.getValue());
		}
		return inputList;
	}
}

//this.document.inputList = new ArrayList<>();
//this.inputList = new ArrayList<>();
//this.inputList.addAll(inputList);
//public void setInputList(List<Input> inputList) {
//for (int i = 0; i < inputList.size(); i++) {
//	Input novel = inputList.get(i);
//	if (!this.inputList.contains(novel)) {
//		this.inputList.add(novel);
//	}
////	if (this.inputList.contains(novel)) {
////		int index = this.inputList.indexOf(novel);
////		Input input = this.inputList.get(index);
////		input.shape = novel.shape;
////		input.page = novel.page;
////	} else {
////		this.inputList.add(novel);
////	}
//}
//}
