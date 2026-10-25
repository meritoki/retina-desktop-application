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
package com.meritoki.app.desktop.retina.model.provider.zooniverse;

import java.util.TreeMap;

import com.fasterxml.jackson.annotation.JsonAnySetter;

public class Subject {

	    private String id;
	    public TreeMap<String, Data> dataMap = new TreeMap<>();

	    // Getters and setters (except for unknownFields)

	    @JsonAnySetter
	    public void setUnknownField(String name, Data value) {
	        dataMap.put(name, value);
	    }
}
