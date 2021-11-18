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
package com.meritoki.app.desktop.retina.model.resource;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.meritoki.app.desktop.retina.controller.node.NodeController;


public class Resource {
	@JsonIgnore
	public List<String> emptyList = new ArrayList<>();
	@JsonIgnore
	public List<String> timeList = new ArrayList<>();
	@JsonIgnore
	public List<String> spaceList = new ArrayList<>();
	@JsonIgnore
	public List<String> energyList = new ArrayList<>();
	@JsonIgnore
	public List<String> languageList = new ArrayList<>();
	@JsonIgnore
	public List<String> recentList = new ArrayList<>();

	public Resource() {
		this.initEnergyList();
		this.initTimeList();
		this.initSpaceList();
		this.initLanguageList();
		this.initRecentList();
	}

	public void initResourceCache() {
		File file = new File(NodeController.getResourceCache());
		if (!file.exists()) {
			file.mkdirs();
		}
	}

	public void addRecent(String recent) {
		File file = new File(NodeController.getResourceCache() + NodeController.getSeperator() + "recent.csv");
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (!this.recentList.contains(recent)) {
			this.recentList.add(recent);
			NodeController.saveText(NodeController.getResourceCache(), "recent.csv", this.recentList);
		}
	}

	public void removeRecent(String recent) {
		if (this.recentList.contains(recent)) {
			this.recentList.remove(recent);
			NodeController.saveText(NodeController.getResourceCache(), "recent.csv", this.recentList);
		}
	}
	
	public void save() {
		NodeController.saveText(NodeController.getResourceCache(), "recent.csv", this.recentList);
	}

	public void initRecentList() {
		File file = new File(NodeController.getResourceCache() + NodeController.getSeperator() + "recent.csv");
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		List<String[]> list = NodeController
				.openCsv(NodeController.getResourceCache() + NodeController.getSeperator() + "recent.csv");
		for (String[] stringArray : list) {
			for (String string : stringArray) {
				this.recentList.add(string);
			}
		}
	}

	public void initEnergyList() {
		File file = new File(NodeController.getResourceCache() + NodeController.getSeperator() + "energy.csv");
		if (!file.exists()) {
			NodeController.saveText(file, Arrays.asList("temperature", "pressure"));
		}
		List<String[]> energyList = NodeController
				.openCsv(NodeController.getResourceCache() + NodeController.getSeperator() + "energy.csv");
		for (String[] stringArray : energyList) {
			for (String string : stringArray) {
				this.energyList.add(string);
			}
		}
	}

	public void initSpaceList() {
		File file = new File(NodeController.getResourceCache() + NodeController.getSeperator() + "space.csv");
		if (!file.exists()) {
			NodeController.saveText(file, Arrays.asList("latitude", "longitude", "locale", "location"));
		}
		List<String[]> spaceList = NodeController
				.openCsv(NodeController.getResourceCache() + NodeController.getSeperator() + "space.csv");
		for (String[] stringArray : spaceList) {
			for (String string : stringArray) {
				this.spaceList.add(string);
			}
		}
	}

	public void initTimeList() {
		File file = new File(NodeController.getResourceCache() + NodeController.getSeperator() + "time.csv");
		if (!file.exists()) {
			NodeController.saveText(file,Arrays.asList("year", "month", "week", "day", "hour", "minute","second") );
		}
		List<String[]> timeList = NodeController
				.openCsv(NodeController.getResourceCache() + NodeController.getSeperator() + "time.csv");
		for (String[] stringArray : timeList) {
			for (String string : stringArray) {
				this.timeList.add(string);
			}
		}
	}

	public void initLanguageList() {
		File file = new File(NodeController.getResourceCache() + NodeController.getSeperator() + "language.csv");
		if (!file.exists()) {
			NodeController.saveText(file, Arrays.asList("letter", "word", "sentance", "paragraph"));
		}
		List<String[]> laguageList = NodeController
				.openCsv(NodeController.getResourceCache() + NodeController.getSeperator() + "language.csv");
		for (String[] stringArray : laguageList) {
			for (String string : stringArray) {
				this.languageList.add(string);
			}
		}
	}
}
