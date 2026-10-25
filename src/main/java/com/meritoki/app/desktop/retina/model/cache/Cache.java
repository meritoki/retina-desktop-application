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
package com.meritoki.app.desktop.retina.model.cache;

import java.io.File;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.meritoki.app.desktop.retina.model.document.Image;
import com.meritoki.app.desktop.retina.model.document.Page;
import com.meritoki.app.desktop.retina.model.document.Point;
import com.meritoki.app.desktop.retina.model.document.Selection;
import com.meritoki.app.desktop.retina.model.document.Selector;
import com.meritoki.app.desktop.retina.model.document.Shape;
import com.meritoki.app.desktop.retina.model.document.ShapeType;
import com.meritoki.app.desktop.retina.model.document.user.User;
import com.meritoki.library.cortex.model.Belief;
import com.meritoki.library.cortex.model.Concept;
import com.meritoki.library.cortex.model.cortex.Cortex;

public class Cache {
	@JsonProperty
	public User user = null;
	@JsonProperty
	public Selector selector = null;
	@JsonProperty
	public Selection selection = null;
	@JsonProperty
	public ShapeType type = ShapeType.RECTANGLE;
	@JsonProperty
	public String[] fileArray = null;
	@JsonProperty
	public String pressedPageUUID = null;
	@JsonProperty
	public String pressedImageUUID = null;
	@JsonProperty
	public String releasedImageUUID = null;
	@JsonProperty
	public String pressedShapeUUID = null;
	@JsonProperty
	public List<Page> pageList = null;
	@JsonProperty
	public List<Shape> shapeList = null;
	@JsonProperty
	public Point pressedPoint = null;
	@JsonProperty
	public Point releasedPoint = null;
	@JsonProperty
	public String script;
	@JsonProperty
	public int pageIndex = -1;
	@JsonProperty
	public int imageIndex = -1;
	@JsonProperty
	public int shapeIndex = -1;
	@JsonProperty
	public String pageUUID = null;
	@JsonProperty
	public String imageUUID = null;
	@JsonProperty
	public String shapeUUID = null;
	@JsonProperty
	public char scaleOperator = ' ';
	@JsonProperty
	public char shiftOperator = ' ';
	@JsonProperty
	public double scaleFactor = 1;
	@JsonProperty
	public double shiftFactor = 1;
	@JsonProperty
	public int row = 1;
	@JsonProperty
	public int column = 1;
	@JsonProperty
	public double defaultScale = 1;
	@JsonIgnore
	public Concept concept = null;
	public List<Concept> conceptList = null;
	public String conceptScript;
	public Belief belief;
	
	public Cortex cortex;
}
