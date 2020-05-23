package com.meritoki.app.desktop.retina.model.document;

import java.util.List;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class Dimension {

	public static void main(String[] args) {
		Dimension d = new Dimension();
		System.out.println(d);
	}

	@JsonIgnore
	private static Logger logger = LogManager.getLogger(Dimension.class.getName());
	@JsonProperty
	public List<Point> pointList = new ArrayList<>();
	@JsonProperty
	public double addScale = 1;
	@JsonProperty
	public double scale = 1;
	@JsonProperty
	public double i = 0;
	@JsonProperty
	public double j = 0;
	@JsonProperty
	public double w = 0;
	@JsonProperty
	public double h = 0;
	@JsonProperty
	public double x = 0;
	@JsonProperty
	public double y = 0;
	@JsonProperty
	public double width = 0;
	@JsonProperty
	public double height = 0;
	@JsonProperty
	public double offset = 0;
	@JsonProperty
	public double margin = 0;

	public Dimension() {
		this.scale();
	}
	
	public Dimension(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public Dimension(Point a, Point b, double scale, double addScale) {
		this.pointList.add(a);
		this.pointList.add(b);
		this.addScale = addScale;
		this.scale = scale * (1 / this.addScale);
		this.scale();
	}

	public Dimension(Dimension dimension) {
		this.x = dimension.x;
		this.y = dimension.y;
		this.width = dimension.width;
		this.height = dimension.height;
		this.scale = dimension.scale;
		this.addScale = dimension.addScale;
		for (Point p : dimension.pointList) {
			this.pointList.add(new Point(p));
		}
	}

	@JsonIgnore
	public void setAddScale(double addScale) {
		this.addScale = addScale;
	}

	@JsonIgnore
	public void setScale(double scale) {
		this.scale = (this.addScale > 0)? scale * (1 / this.addScale):scale;
		this.scale();
	}

	@JsonIgnore
	public void setPointList(List<Point> pointList) {
		this.pointList = pointList;
	}

	@JsonIgnore
	public void setOffset(double offset) {
		this.offset = offset;
	}

	@JsonIgnore
	public void setMargin(double margin) {
		this.margin = margin;
	}

	@JsonIgnore
	public double getCenterX() {
		return this.x + (this.width / 2);
	}

	@JsonIgnore
	public double getCenterY() {
		return this.y + (this.height / 2);
	}

	@JsonIgnore
	public Point getStartPoint() {
		this.scale();
		return new Point(this.x, this.y);
	}

	@JsonIgnore
	public Point getStopPoint() {
		this.scale();
		return new Point((this.x + this.width), (this.y + this.height));
	}

	@JsonIgnore
	public void scale() {
		if (this.pointList != null && this.pointList.size() == 2) {
			this.normalize();
			Point a = this.pointList.get(0);
			Point b = this.pointList.get(1);
			this.x = Math.min(a.x, b.x);
			this.y = Math.min(a.y, b.y);
			this.width = Math.abs(a.x - b.x);
			this.height = Math.abs(a.y - b.y);
		} else {
			this.x = this.i;
			this.y = this.j;
			this.width = this.w;
			this.height = this.h;
			this.x += this.offset*1/this.scale;
			this.y += this.margin;
		}
		this.x *= this.scale;
		this.y *= this.scale;
		this.width *= this.scale;
		this.height *= this.scale;
	}

	@JsonIgnore
	public boolean isValid() {
		boolean flag = true;
		if (this.pointList.get(0).x == this.pointList.get(1).x && this.pointList.get(0).y == this.pointList.get(1).y) {
			flag = false;
		}
		return flag;
	}

	@JsonIgnore
	public void normalize() {
		Point pointZero = this.pointList.get(0);
		Point pointOne = this.pointList.get(1);
		// Case B
		if (pointZero.x > pointOne.x && pointZero.y < pointOne.y) {
			// logger.info("sortPointList() Case B");
			this.pointList.set(0, new Point(pointOne.x, pointZero.y));
			this.pointList.set(1, new Point(pointZero.x, pointOne.y));
			// Case C
		} else if (pointZero.x < pointOne.x && pointZero.y > pointOne.y) {
			// logger.info("sortPointList() Case C");
			this.pointList.set(0, new Point(pointZero.x, pointOne.y));
			this.pointList.set(1, new Point(pointOne.x, pointZero.y));
			// Case D
		} else if (pointZero.x > pointOne.x && pointZero.y > pointOne.y) {
			// logger.info("sortPointList() Case D");
			this.pointList.set(0, pointOne);
			this.pointList.set(1, pointZero);
		}
	}

	@JsonIgnore
	public void addPoint(Point point) {
		this.pointList.add(point);
	}

	@JsonIgnore
	public boolean containsPoint(Point point) {

		boolean flag = false;
		Point startPoint = this.getStartPoint();
		Point stopPoint = this.getStopPoint();

		if (startPoint.x < stopPoint.x && startPoint.y < stopPoint.y) {
			if (startPoint.x <= point.x && point.x <= stopPoint.x && startPoint.y <= point.y
					&& point.y <= stopPoint.y) {
				flag = true;
			}
		}

		if (flag) {
			logger.info("containsPoint(" + point + ") dimension=" + this);
		}
//		else if (stopPoint.x < startPoint.x && stopPoint.y < startPoint.y) {
//			if (stopPoint.x <= point.x && point.x <= startPoint.x && stopPoint.y <= point.y
//					&& point.y <= startPoint.y) {
//				flag = true;
//			}
//		}
		return flag;
	}

	@JsonIgnore
	public Selection selectionPoint(Point point) {
		logger.info("selectionPoint(" + point + ")");
		Selection selection = null;
		Point startPoint = this.getStartPoint();
		Point stopPoint = this.getStopPoint();
		double margin = 20 * this.scale;
		if (point.x == startPoint.x && point.y == startPoint.y) {
			selection = Selection.TOP_LEFT;
		} else if (point.x > (stopPoint.x - margin) && point.x < (stopPoint.x + margin)
				&& point.y > (stopPoint.y - margin) && point.y < (stopPoint.y + margin)) {
			selection = Selection.BOTTOM_RIGHT;
		} else if (point.x > (stopPoint.x - margin) && point.x < (stopPoint.x + margin)
				&& point.y > (startPoint.y - margin) && point.y < (startPoint.y + margin)) {
			selection = Selection.TOP_RIGHT;
		} else if (point.x > (startPoint.x - margin) && point.x < (startPoint.x + margin)
				&& point.y > (stopPoint.y - margin) && point.y < (stopPoint.y + margin)) {
			selection = Selection.BOTTOM_LEFT;
		} else if (point.y >= (startPoint.y) && point.y < (startPoint.y + margin) && point.x > startPoint.x
				&& point.x < stopPoint.x) {
			selection = Selection.TOP;
		} else if (point.y > (stopPoint.y - margin) && point.y <= (stopPoint.y) && point.x > startPoint.x
				&& point.x < stopPoint.x) {
			selection = Selection.BOTTOM;
		} else if (point.x >= (startPoint.x) && point.x < (startPoint.x + margin) && point.y > startPoint.y
				&& point.y < stopPoint.y) {
			selection = Selection.LEFT;
		} else if (point.x > (stopPoint.x - margin) && point.x <= (stopPoint.x) && point.y > startPoint.y
				&& point.y < stopPoint.y) {
			selection = Selection.RIGHT;
		}
		return selection;
	}

	@JsonIgnore
	public boolean intersectPoint(Point point) {
		logger.info("intersectPoint(" + point + ")");
		boolean flag = false;
		if (this.selectionPoint(point) != null) {
			flag = true;
		}
		return flag;
	}

	@JsonIgnore
	public void resizePoint(Point point, Selection selection) {
		logger.info("resizePoint(" + point + ", " + selection + ")");
		switch (selection) {
		case TOP: {
			this.pointList.get(0).y = point.y;
			break;
		}
		case BOTTOM: {
			this.pointList.get(1).y = point.y;
			break;
		}
		case LEFT: {
			this.pointList.get(0).x = point.x;
			break;
		}
		case RIGHT: {
			this.pointList.get(1).x = point.x;
			break;
		}
		case TOP_LEFT: {
			this.pointList.set(0, point);
			break;
		}
		case TOP_RIGHT: {
			this.pointList.get(0).y = point.y;
			this.pointList.get(1).x = point.x;
			break;
		}
		case BOTTOM_LEFT: {
			this.pointList.get(0).x = point.x;
			this.pointList.get(1).y = point.y;
			break;
		}
		case BOTTOM_RIGHT: {
			this.pointList.set(1, point);
			break;
		}
		}
	}

	@JsonIgnore
	public void movePoint(Point point) {
		logger.info("movePoint(" + point + ")");
		this.pointList.get(0).x = this.pointList.get(0).x + point.x * (1 / this.scale);
		this.pointList.get(0).y = this.pointList.get(0).y + point.y * (1 / this.scale);
		this.pointList.get(1).x = this.pointList.get(1).x + point.x * (1 / this.scale);
		this.pointList.get(1).y = this.pointList.get(1).y + point.y * (1 / this.scale);
	}

	@JsonIgnore
	@Override
	public String toString() {
		String string = "";
		ObjectWriter ow = new ObjectMapper().writer();// .withDefaultPrettyPrinter();
		try {
			string = ow.writeValueAsString(this);
		} catch (IOException ex) {
			logger.error("IOException " + ex.getMessage());
		}
		return string;
	}
}
