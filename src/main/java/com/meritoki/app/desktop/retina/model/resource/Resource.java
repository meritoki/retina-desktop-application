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
	public List<String> timeList = new ArrayList<>();//Arrays.asList("year", "month", "week", "day", "hour", "minute", "second");
	@JsonIgnore
	public List<String> spaceList = new ArrayList<>();//Arrays.asList("latitude", "longitude", "locale", "location");
	@JsonIgnore
	public List<String> energyList = new ArrayList<>();//Arrays.asList("temperature", "pressure");
	@JsonIgnore
	public List<String> languageList = new ArrayList<>();//Arrays.asList("letter", "word", "sentance", "paragraph");
	@JsonIgnore
	public List<String> recentList = new ArrayList<>();
	
	public Resource() {
		this.initEnergyList();
		this.initTimeList();
		this.initSpaceList();
		this.initLanguageList();
		this.initRecentList();
	}
	
	public void addRecent(String recent) {
		File file = new File(NodeController.getResourceCache()+NodeController.getSeperator()+"recent.csv");
		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(!this.recentList.contains(recent)) {
			this.recentList.add(recent);
			NodeController.saveCsv(NodeController.getResourceCache(), "recent.csv", this.recentList);
		}
	}
	
	public void removeRecent(String recent) {
		if(this.recentList.contains(recent)) {
			this.recentList.remove(recent);
			NodeController.saveCsv(NodeController.getResourceCache(), "recent.csv", this.recentList);
		}
	}
	
	public void initRecentList() {
		List<String[]> list = NodeController.openCsv(NodeController.getResourceCache()+NodeController.getSeperator()+"recent.csv");
		for(String[] stringArray:list) {
			for(String string: stringArray) {
				this.recentList.add(string);
			}
		}
	}
	
	public void initEnergyList() {
		List<String[]> energyList = NodeController.openCsv(NodeController.getResourceCache()+NodeController.getSeperator()+"energy.csv");
		for(String[] stringArray:energyList) {
			for(String string: stringArray) {
				this.energyList.add(string);
			}
		}
	}
	
	public void initSpaceList() {
		List<String[]> spaceList = NodeController.openCsv(NodeController.getResourceCache()+NodeController.getSeperator()+"space.csv");
		for(String[] stringArray:spaceList) {
			for(String string: stringArray) {
				this.spaceList.add(string);
			}
		}
	}
	
	public void initTimeList() {
		List<String[]> timeList = NodeController.openCsv(NodeController.getResourceCache()+NodeController.getSeperator()+"time.csv");
		for(String[] stringArray:timeList) {
			for(String string: stringArray) {
				this.timeList.add(string);
			}
		}
	}
	
	public void initLanguageList() {
		List<String[]> laguageList = NodeController.openCsv(NodeController.getResourceCache()+NodeController.getSeperator()+"language.csv");
		for(String[] stringArray:laguageList) {
			for(String string: stringArray) {
				this.languageList.add(string);
			}
		}
	}
}
