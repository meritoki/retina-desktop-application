package com.meritoki.app.desktop.retina.model.document;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.meritoki.app.desktop.retina.model.document.user.User;

public class State {
	@JsonIgnore
	public Type type = Type.RECTANGLE;
	@JsonIgnore
	public Image pressedImage = null;
	@JsonIgnore
	public Image releasedImage = null;
	@JsonIgnore
	public File[] fileArray = null;
	@JsonIgnore
	public Script script = null;
	@JsonIgnore
	public Page page = null;
	@JsonIgnore
	public Shape pressedShape = null;
	@JsonIgnore
	public Text text = null;
	@JsonIgnore
	public List<Page> pageList = null;
	@JsonIgnore
	public List<Shape> shapeList = null;
	@JsonIgnore
	public List<Text> textList = null;
	@JsonIgnore
	public Point pressedPoint = new Point();
	@JsonIgnore
	public Point releasedPoint = new Point();
	@JsonIgnore
	public Point movedPoint = new Point();
	@JsonIgnore
	public double scale = 1;
	@JsonIgnore
	public Selection selection = null;
	@JsonIgnore
	public String defaultFileName = "untitled.json";
	@JsonIgnore
	public String documentFileName = null;
	@JsonIgnore
	public String documentFilePath = null;
	@JsonIgnore
	public List<String> emptyList = new ArrayList<>();
	@JsonIgnore
	public List<String> timeList = Arrays.asList("year", "month", "week", "day", "hour", "minute", "second");
	@JsonIgnore
	public List<String> spaceList = Arrays.asList("latitude", "longitude", "locale", "location");
	@JsonIgnore
	public List<String> energyList = Arrays.asList("label", "letter", "word", "sentance", "temperature", "pressure");
	@JsonIgnore
	public String cachePath = null;
	@JsonIgnore
	public User user = null;
}
