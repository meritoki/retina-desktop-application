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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.meritoki.library.cortex.model.group.Group;
import com.meritoki.library.cortex.model.network.Color;
import com.meritoki.library.cortex.model.network.Cortex;
import com.meritoki.library.cortex.model.network.hexagon.Hexagonal;
import com.meritoki.library.cortex.model.network.square.Squared;

public class Document {

	@JsonProperty
	public Cortex cortex;
	
	public Document() {
//		this.cortex = new Squared(Squared.BRIGHTNESS,0,0,32,1,0);
		this.cortex = new Hexagonal(Color.BRIGHTNESS, 0, 0, 27, 1, 0);
//		this.cortex = new Group(Group.HEXAGONAL);
	}
}
