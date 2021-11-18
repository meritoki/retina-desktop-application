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

import java.io.File;

import com.meritoki.app.desktop.retina.controller.node.NodeController;

public class ZooniverseController {
	public static File getProjectJSON() {
		return new File(getProjectJSONPath());
	}
	
	public static String getZooniverseHome() {
		return NodeController.getSystemHome() + NodeController.getSeperator() + "provider"
				+ NodeController.getSeperator() + "zooniverse";
	}
	
	public static String getProjectJSONPath() {
		return getZooniverseHome()+NodeController.getSeperator() + "project.json";
	}

	public static String getSubjectSetPath() {
		return getZooniverseHome() + NodeController.getSeperator() + "subject-set"
				+ NodeController.getSeperator();
	}
	
	public static String getConfigPath() {
		return NodeController.getSystemHome() + NodeController.getSeperator() + "provider"
				+ NodeController.getSeperator() + "zooniverse";
	}
}
