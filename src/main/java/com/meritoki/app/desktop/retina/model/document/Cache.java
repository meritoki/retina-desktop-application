package com.meritoki.app.desktop.retina.model.document;

import java.io.File;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;

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
	@JsonIgnore
	public Point movedPoint = new Point();
	@JsonIgnore
	public double scale = 1;
	@JsonIgnore
	public String script;
	
}
