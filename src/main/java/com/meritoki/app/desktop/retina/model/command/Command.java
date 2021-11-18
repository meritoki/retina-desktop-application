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

import java.util.Date;
import java.util.LinkedList;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.meritoki.app.desktop.retina.model.Model;
import com.meritoki.app.desktop.retina.model.document.user.User;

public class Command implements CommandInterface {
	@JsonIgnore
	public Model model;
	@JsonProperty
	public Date date;
	@JsonProperty
	public String name;
	@JsonProperty
	public User user;
	@JsonIgnore
    public LinkedList<Operation> operationList = new LinkedList<>();
	
	public Command() {
	}

	public Command(Model model, String name) {
		this.model = model;
		this.date = new Date();
		this.name = name;
	}
	
	@Override
	public void execute() throws Exception {
	}
	
	public void reset() {
		this.operationList = new LinkedList<>();
	}

	@Override
	public void undo() throws Exception {
		  throw new UnsupportedOperationException("Unsupported Operation");
	}

	@Override
	public void redo() throws Exception {
		throw new UnsupportedOperationException("Unsupported Operation");
	}
}
