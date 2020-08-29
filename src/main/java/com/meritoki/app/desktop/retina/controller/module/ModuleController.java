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
package com.meritoki.app.desktop.retina.controller.module;

import com.meritoki.app.desktop.retina.model.Model;
import com.meritoki.app.desktop.retina.model.module.Recognition;
import com.meritoki.library.controller.Controller;

public class ModuleController extends Controller {

	private Model model;
	private Recognition recognition;

	public ModuleController(Model model) {
		this.model = model;
		this.recognition = new Recognition(0,this.model);
	}

	public void start() {
		recognition.start();
	}

	public void stop() {
		recognition.destroy();
	}
}
