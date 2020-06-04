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
package com.meritoki.app.desktop.retina.model.document;

import java.io.File;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.meritoki.app.desktop.retina.model.document.user.User;

public class Cache {
	@JsonIgnore
	public User user = null;
	@JsonIgnore
	public Selection selection = null;
	@JsonIgnore
	public ShapeType type = ShapeType.RECTANGLE;
	@JsonIgnore
	public File[] fileArray = null;
	@JsonIgnore
	public Page pressedPage = null;
	@JsonIgnore
	public Image pressedImage = null;
	@JsonIgnore
	public Image releasedImage = null;
	@JsonIgnore
	public Shape pressedShape = null;
	@JsonIgnore
	public List<Page> pageList = null;
	@JsonIgnore
	public List<Shape> shapeList = null;
	@JsonIgnore
	public Point pressedPoint = new Point();
	@JsonIgnore
	public Point releasedPoint = new Point();
//	@JsonIgnore
//	public double scale = 1;
	@JsonIgnore
	public String script;
	@JsonIgnore
	public int pageIndex = -1;
	@JsonIgnore
	public String pageUUID = null;
	@JsonIgnore
	public char scaleOperator = ' ';
	@JsonIgnore
	public char shiftOperator = ' ';
	@JsonIgnore
	public double scaleFactor = 1;
	@JsonIgnore
	public double shiftFactor = 1;
}
