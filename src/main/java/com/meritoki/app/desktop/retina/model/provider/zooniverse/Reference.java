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

public class Reference {
	 private String retired = null;
	 private String my_own_id;
	 private String the_image;


	 // Getter Methods 

	 public String getRetired() {
	  return retired;
	 }

	 public String getMy_own_id() {
	  return my_own_id;
	 }

	 public String getThe_image() {
	  return the_image;
	 }

	 // Setter Methods 

	 public void setRetired(String retired) {
	  this.retired = retired;
	 }

	 public void setMy_own_id(String my_own_id) {
	  this.my_own_id = my_own_id;
	 }

	 public void setThe_image(String the_image) {
	  this.the_image = the_image;
	 }
	}
