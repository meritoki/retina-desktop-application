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

import java.util.List;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class Position {
	@JsonIgnore
	private static Logger logger = LogManager.getLogger(Position.class.getName());
	@JsonProperty
	public double addScale = 1;
	@JsonProperty
	public double scale = 1;
	@JsonProperty
	public double relativeScale = 1;
	@JsonProperty
	public Point point = new Point();
	@JsonProperty
	public Point absolutePoint = new Point();
	@JsonProperty
	public Point relativePoint = null;
	@JsonProperty
	public Dimension dimension = new Dimension();
	@JsonProperty
	public Dimension absoluteDimension = new Dimension();
	@JsonProperty
	public double offset = 0;
	@JsonProperty
	public double margin = 0;

	public Position() {
	}

	public Position(int x, int y, int width, int height) {
		this.absolutePoint.x = x;
		this.absolutePoint.y = y;
		this.absoluteDimension.width = width;
		this.absoluteDimension.height = height;
		this.scale();
	}

	public Position(Point a, Point b, double addScale, double offset, double margin) {
		logger.info("Position(" + a + ", " + b + ", " + addScale + ", " + offset + ", " + margin + ")");
		List<Point> pointList = new ArrayList<>();
		pointList.add(a);
		pointList.add(b);
		this.normalize(pointList);
		this.addScale = addScale;
		this.offset = offset;
		this.margin = margin;
		this.absolutePoint = new Point(pointList.get(0));
		this.point = new Point(this.absolutePoint);// possible bug cause
		Point stopPoint = new Point(pointList.get(1));
		this.absoluteDimension.width = Math.abs(stopPoint.x - this.absolutePoint.x);
		this.absoluteDimension.height = Math.abs(stopPoint.y - this.absolutePoint.y);
		this.relativePoint = this.getRelativePoint();
		this.scale();
	}

	public Position(Point absolutePoint, Dimension absoluteDimension, double addScale, double offset, double margin) {
		logger.debug("Position(" + absolutePoint + ", " + absoluteDimension + ", " + addScale + ", " + offset + ", "
				+ margin + ")");
		this.addScale = addScale;
		this.offset = offset;
		this.margin = margin;
		this.absolutePoint = absolutePoint;
		this.absoluteDimension = absoluteDimension;
		this.relativePoint = this.getRelativePoint();
		this.scale();
	}

	public Position(Position position) {
		logger.debug("Position(" + position + ")");
		this.absolutePoint = new Point(position.absolutePoint);
		this.absoluteDimension = new Dimension(position.absoluteDimension);
		this.scale = position.scale;
		this.addScale = position.addScale;
		this.margin = position.margin;
		this.offset = position.offset;
		if (position.relativePoint != null)
			this.relativePoint = new Point(position.relativePoint);
		this.scale();
	}

	@JsonIgnore
	public void normalize(List<Point> pointList) {
		Point pointZero = pointList.get(0);
		Point pointOne = pointList.get(1);
		if (pointZero.x > pointOne.x && pointZero.y < pointOne.y) {
			pointList.set(0, new Point(pointOne.x, pointZero.y));
			pointList.set(1, new Point(pointZero.x, pointOne.y));
		} else if (pointZero.x < pointOne.x && pointZero.y > pointOne.y) {
			pointList.set(0, new Point(pointZero.x, pointOne.y));
			pointList.set(1, new Point(pointOne.x, pointZero.y));
		} else if (pointZero.x > pointOne.x && pointZero.y > pointOne.y) {
			pointList.set(0, pointOne);
			pointList.set(1, pointZero);
		}
	}

	@JsonIgnore
	public Point getRelativePoint() {
		Point point = new Point();
		point.x = this.absolutePoint.x - offset * this.addScale;
		point.y = this.absolutePoint.y - margin * this.addScale;
		return point;
	}

	@JsonIgnore
	public void setAbsolutePoint(Point point) {
		logger.debug("setAbsolutePoint(" + point + ")");
		this.absolutePoint = point;
		this.scale();
	}

	@JsonIgnore
	public void setAbsolutePoint(double x, double y) {
		logger.debug("setAbsolutePoint(" + point + ")");
		this.absolutePoint.x = x;
		this.absolutePoint.y = y;
		this.scale();
	}

	@JsonIgnore
	public void setAbsoluteDimension(Dimension dimension) {
		logger.debug("setAbsoluteDimension(" + dimension + ")");
		this.absoluteDimension = dimension;
		this.scale();
	}

	@JsonIgnore
	public void addAbsoluteDimension(Dimension dimension) {
		this.absoluteDimension.width += dimension.width;
		this.absoluteDimension.height += dimension.height;
		logger.debug("addAbsoluteDimension(" + dimension + ") this.absoluteDimension=" + this.absoluteDimension);
		this.scale();
	}

	@JsonIgnore
	public void scale() {
		if (this.relativePoint != null) {
			this.point.x = this.relativePoint.x + this.offset * this.addScale;// this was apparent fix for shift margin
			this.point.y = this.relativePoint.y + this.margin * this.addScale;
			this.dimension.width = this.absoluteDimension.width;
			this.dimension.height = this.absoluteDimension.height;
			this.point.x *= this.relativeScale;
			this.point.y *= this.relativeScale;
			this.dimension.width *= this.relativeScale;
			this.dimension.height *= this.relativeScale;
		} else {
			this.point.x = this.absolutePoint.x + this.offset;
			this.point.y = this.absolutePoint.y + this.margin;
			this.dimension.width = this.absoluteDimension.width;
			this.dimension.height = this.absoluteDimension.height;
		}
		this.point.x *= this.scale;
		this.point.y *= this.scale;
		this.dimension.width *= this.scale;
		this.dimension.height *= this.scale;
	}

	@JsonIgnore
	public void setScale(double scale) {
		this.scale = (this.addScale > 0) ? scale / this.addScale : scale;
		logger.debug("setScale(" + scale + ") this.scale=" + this.scale);
		this.scale();
	}

	@JsonIgnore
	public void setRelativeScale(double scale) {
		this.relativeScale = (this.addScale > 0) ? scale / this.addScale : scale;
		logger.info("setRelativeScale(" + scale + ") this.relativeScale=" + this.relativeScale);
		this.scale();
	}

	@JsonIgnore
	public void setAddScale(double addScale) {
		logger.info("setAddScale(" + addScale + ")");
		this.addScale = addScale;
	}

	@JsonIgnore
	public void setOffset(double offset) {
		logger.debug("setOffset(" + offset + ")");
		this.offset = offset;
		this.scale();
	}

	@JsonIgnore
	public void setMargin(double margin) {
		this.margin = margin;
		this.scale();
	}

	@JsonIgnore
	public double getCenterX() {
		return this.absolutePoint.x + (this.dimension.width / 2);
	}

	@JsonIgnore
	public double getCenterY() {
		return this.absolutePoint.y + (this.dimension.height / 2);
	}

	@JsonIgnore
	public Point getStartPoint() {
		this.scale();
		return this.point;
	}

	@JsonIgnore
	public Point getStopPoint() {
		this.scale();
		return new Point((this.point.x + this.dimension.width), (this.point.y + this.dimension.height));
	}

	@JsonIgnore
	public boolean contains(Point point) {
		boolean flag = false;
		Point startPoint = this.getStartPoint();
		Point stopPoint = this.getStopPoint();
		if (startPoint.x < stopPoint.x && startPoint.y < stopPoint.y) {
			if (startPoint.x <= point.x && point.x <= stopPoint.x && startPoint.y <= point.y
					&& point.y <= stopPoint.y) {
				flag = true;
			}
		}
		logger.debug("contains(" + point + ") flag=" + flag);
		return flag;
	}

	@JsonIgnore
	public Selection selection(Point point) {

		Selection selection = null;
		Point startPoint = this.getStartPoint();
		Point stopPoint = this.getStopPoint();
		double margin = 20 * this.scale;
		if (point.x > (startPoint.x - margin) && point.x < (startPoint.x + margin) && point.y > (startPoint.y - margin)
				&& point.y < (startPoint.y + margin)) {
			selection = Selection.TOP_LEFT;
		} else if (point.x > (stopPoint.x - margin) && point.x < (stopPoint.x + margin)
				&& point.y > (startPoint.y - margin) && point.y < (startPoint.y + margin)) {
			selection = Selection.TOP_RIGHT;
		} else if (point.x > (startPoint.x - margin) && point.x < (startPoint.x + margin)
				&& point.y > (stopPoint.y - margin) && point.y < (stopPoint.y + margin)) {
			selection = Selection.BOTTOM_LEFT;
		} else if (point.x > (stopPoint.x - margin) && point.x < (stopPoint.x + margin)
				&& point.y > (stopPoint.y - margin) && point.y < (stopPoint.y + margin)) {
			selection = Selection.BOTTOM_RIGHT;
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
		logger.debug("selection(" + point + ") selection=" + selection);
		return selection;
	}



	@JsonIgnore
	public void resize(Point point, Selection selection) {
		logger.info("resize(" + point + ", " + selection + ")");
		Point startPoint = this.getStartPoint();
		Point stopPoint = this.getStopPoint();
		switch (selection) {
		case TOP: {
			startPoint.y = point.y;
			break;
		}
		case BOTTOM: {
			stopPoint.y = point.y;
			break;
		}
		case LEFT: {
			startPoint.x = point.x;
			break;
		}
		case RIGHT: {
			stopPoint.x = point.x;
			break;
		}
		case TOP_LEFT: {
			startPoint.x = point.x;
			startPoint.y = point.y;
			break;
		}
		case TOP_RIGHT: {
			startPoint.y = point.y;
			stopPoint.x = point.x;
			break;
		}
		case BOTTOM_LEFT: {
			startPoint.x = point.x;
			stopPoint.y = point.y;
			break;
		}
		case BOTTOM_RIGHT: {
			stopPoint.x = point.x;
			stopPoint.y = point.y;
			break;
		}
		}
		// changing the absolute point is required, this dictates the raw dimensions of
		// a shape
		// the problem appears to be with scaling in and out, when changing the absolute
		// point and dimension b/c this fundamentally changes the
		// original shape and how it is rendered on the page
		// there is a scaling issue that has to be accounted for in this process.
		startPoint = new Point(startPoint.x / this.scale - this.offset / this.addScale,startPoint.y / this.scale - this.margin / this.addScale);
		stopPoint = new Point(stopPoint.x / this.scale - this.offset / this.addScale,stopPoint.y / this.scale - this.margin / this.addScale);
		this.absolutePoint = new Point(startPoint);
		this.absoluteDimension = new Dimension(stopPoint.x - this.absolutePoint.x, stopPoint.y - this.absolutePoint.y);
		this.relativePoint = this.getRelativePoint();
		this.scale();
	}

	@JsonIgnore
	public void move(Point point) {
		logger.info("move(" + point + ")");
		Point origin = null;
		if (this.relativePoint != null) {
			origin = this.relativePoint;
		} else {
			origin = this.absolutePoint;
		}
		origin.x += point.x / scale;
		origin.y += point.y / scale;
		this.scale();
	}

	@JsonIgnore
	@Override
	public String toString() {
		String string = "";
		ObjectWriter ow = new ObjectMapper().writer();
		if (logger.isDebugEnabled()) {
			// .withDefaultPrettyPrinter();
			try {
				string = ow.writeValueAsString(this);
			} catch (IOException ex) {
				logger.error("IOException " + ex.getMessage());
			}
		} else if (logger.isInfoEnabled()) {
			string = "{\"point\":" + this.point + ", \"dimension\":" + this.dimension + "}";
		}
		return string;
	}
}

//@JsonIgnore
//public boolean intersectPoint(Point point) {
//	boolean flag = false;
//	if (this.selectionPoint(point) != null) {
//		logger.info("intersectPoint(" + point + ")");
//		flag = true;
//	}
//	return flag;
//}
