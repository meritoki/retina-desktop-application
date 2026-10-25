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
package com.meritoki.app.desktop.retina.model.document.prototype;

import java.util.Calendar;
import java.util.GregorianCalendar;

import com.meritoki.app.desktop.retina.model.document.Data;
import com.meritoki.app.desktop.retina.model.document.Shape;

public class Time {

	public int year = 0;
	public int month = 0;
	public int day = 0;
	public int hour = 0;
	public int minute = 0;
	public int second = 0;
	public int millisecond = 0;

	public void input(Shape shape) {
		Data data = shape.data;
		String unit = data.unit.value;
		String text = data.text.value;
		if (unit != null && text != null) {
			switch (unit) {
			case "year": {
				this.year = Integer.parseInt(text);
			}
			case "month": {
				try {
					this.month = Integer.parseInt(text);
				} catch(NumberFormatException e) {
					text = text.toLowerCase();
					switch (text) {
					case "january": {
						this.month = 1;
					}
					case "february": {
						this.month = 2;
					}
					case "march": {
						this.month = 3;
					}
					case "april": {
						this.month = 4;
					}
					case "may": {
						this.month = 5;
					}
					case "june": {
						this.month = 6;
					}
					case "july": {
						this.month = 7;
					}
					case "august": {
						this.month = 8;
					}
					case "september": {
						this.month = 9;
					}
					case "october": {
						this.month = 10;
					}
					case "november": {
						this.month = 11;
					}
					case "december": {
						this.month = 12;
					}
					}
				}
			}
			case "day": {
				this.day = Integer.parseInt(text);
			}
			case "hour": {
				this.hour = Integer.parseInt(text);
			}
			case "minute": {
				this.minute = Integer.parseInt(text);
			}
			case "second": {
				this.second = Integer.parseInt(text);
			}
			case "millisecond": {
				this.millisecond = Integer.parseInt(text);
			}
			}
		}
	}

	public String toString() {
		Calendar date = new GregorianCalendar();
		date.set(Calendar.YEAR, this.year);
		date.set(Calendar.MONTH, this.month);
		date.set(Calendar.HOUR_OF_DAY, this.hour);
		date.add(Calendar.DAY_OF_MONTH, this.day);
		date.set(Calendar.MINUTE, this.minute);
		date.set(Calendar.SECOND, this.second);
		date.set(Calendar.MILLISECOND, this.millisecond);
		return date.toString();
	}
}
