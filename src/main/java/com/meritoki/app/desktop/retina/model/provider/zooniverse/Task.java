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
package com.meritoki.app.desktop.retina.model.provider.zooniverse;

public class Task {
	 private String task;
	 private String value;
	 private String task_label;


	 // Getter Methods 

	 public String getTask() {
	  return task;
	 }

	 public String getValue() {
	  return value;
	 }

	 public String getTask_label() {
	  return task_label;
	 }

	 // Setter Methods 

	 public void setTask(String task) {
	  this.task = task;
	 }

	 public void setValue(String value) {
	  this.value = value;
	 }

	 public void setTask_label(String task_label) {
	  this.task_label = task_label;
	 }
	}