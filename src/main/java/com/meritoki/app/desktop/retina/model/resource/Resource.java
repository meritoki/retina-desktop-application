package com.meritoki.app.desktop.retina.model.resource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Resource {
	@JsonIgnore
	public List<String> emptyList = new ArrayList<>();
	@JsonIgnore
	public List<String> timeList = Arrays.asList("year", "month", "week", "day", "hour", "minute", "second");
	@JsonIgnore
	public List<String> spaceList = Arrays.asList("latitude", "longitude", "locale", "location");
	@JsonIgnore
	public List<String> energyList = Arrays.asList("temperature", "pressure");
	@JsonIgnore
	public List<String> languageList = Arrays.asList("letter", "word", "sentance", "paragraph");
	@JsonIgnore
	public List<String> tableList = Arrays.asList("label");
}
