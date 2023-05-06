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
package com.meritoki.app.desktop.retina.model.provider.meritoki;

import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import com.meritoki.app.desktop.retina.model.Model;
import com.meritoki.app.desktop.retina.model.provider.Provider;
import com.meritoki.module.library.model.Module;
import com.meritoki.module.library.model.Node;
import com.meritoki.module.library.model.State;

public class Recognition extends Node {
	public String name = "Recognition";
	public String version = "0.2.202305";
	public String vendor = "Meritoki";
	public String year = "2020-2023";
	public Model model;
	private Meritoki meritoki;
	
	public Recognition(int id, Model model) {
		super(id);
		this.about();
		this.model = model;
	}

	@Override
	public void initialize() {
		super.initialize();
		this.meritoki = (Meritoki)this.model.system.providerMap.get("meritoki");
        this.filter = false;
        this.setState(State.DEFAULT);
	}

	public String about() {
		String about = this.name + " Version " + this.version + " Copyright " + this.getVendor() + " " + this.year;
		logger.info(about);
		return about;
	}

	public Object load(Integer id, Object object) {
		logger.info("load("+id+", "+object+")");
		String string = null;
		if (object instanceof String) {
			string = (String) object;
			if (string.equals(Train.class.getCanonicalName())) {
				object = new Train(id.intValue(), this, this.model);
			} 
			else if (string.equals(Inference.class.getCanonicalName())) {
				object = new Inference(id.intValue(), this, this.model);
			}
			else {
				logger.warn("load(" + id + ", " + object + ")");
				object = null;
			}

		}
		return object;
	}

	@Override
	public void defaultState(Object object) {
		if (object != null)
			moduleMapAdd(this.moduleMap, object);
		if (delayExpired()) {
			if (this.moduleMapSize != this.moduleMap.size())
				this.moduleMapSize = this.moduleMap.size();
			Set<Integer> destroyIDSet = moduleMapGetDestroy(this.moduleMap, this.idSet);
			if (!destroyIDSet.isEmpty()) {
				Iterator<Integer> loadIDSetIterator = destroyIDSet.iterator();
				CountDownLatch countDownLatch = new CountDownLatch(destroyIDSet.size());
				while (loadIDSetIterator.hasNext()) {
					Integer id;
					if ((id = loadIDSetIterator.next()) != this.id) {
						object = load(id, this.idProperties.getProperty(id.toString()));
						if(object instanceof Module) {
							Module module = (Module)object;
							module.setCountDownLatch(countDownLatch);
						}
					}
				}
				this.moduleMapStart(this.moduleMap);
				try {
					
					logger.debug("defaultState(" + object + ") (countDownLatch.await())");
					countDownLatch.await();
				} catch (InterruptedException ie) {
					logger.warn("defaultState(" + object + ") InterruptedException");
				}
			} else {
				moduleMapStart(this.moduleMap);
			}
			setDelay(newDelay(this.inputDelay));
		}
	}

	public String getVersion() {
		return this.version;
	}

	public String getVendor() {
		return this.vendor;
	}
	
	@Override
	public void destroy() {
		super.destroy();
//		this.meritoki.saveCortex();
	}
}
//for (Provider provider : this.model.system.providerList) {
//if (provider instanceof Meritoki) {
//  this.meritoki = (Meritoki) provider;
//  this.meritoki.init();
//}
//}