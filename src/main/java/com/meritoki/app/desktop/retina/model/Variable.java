package com.meritoki.app.desktop.retina.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.meritoki.app.desktop.retina.model.document.File;
import com.meritoki.app.desktop.retina.model.document.Page;
import com.meritoki.app.desktop.retina.model.document.Point;
import com.meritoki.app.desktop.retina.model.document.Script;
import com.meritoki.app.desktop.retina.model.document.Shape;
import com.meritoki.app.desktop.retina.model.document.Text;
import com.meritoki.app.desktop.retina.model.provider.zooniverse.Project;
import com.meritoki.app.desktop.retina.model.provider.zooniverse.Zooniverse;

public class Variable {
	@JsonIgnore
	public boolean newUser = false;
	@JsonIgnore
	public boolean loginUser = false;
	@JsonIgnore
	public boolean rectangle = true;
	@JsonIgnore
	public boolean ellipse = true;
	@JsonIgnore
	public Zooniverse zooniverse = null;
	@JsonIgnore
	public List<Project> projectList = null;
	@JsonIgnore
	public File pressedFile = null;
	@JsonIgnore
	public File releasedFile = null;
	@JsonIgnore
	public java.io.File[] files = null;
	@JsonIgnore
	public java.io.File file = null;
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
	public int selection = -1;
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
	public boolean newDocument = true;
	@JsonIgnore
	public String cachePath = null;
}
