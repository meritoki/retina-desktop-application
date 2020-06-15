package com.meritoki.app.desktop.retina.model.module;

import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import com.meritoki.app.desktop.retina.model.Model;
import com.meritoki.module.library.model.Module;
import com.meritoki.module.library.model.Node;

public class Recognition extends Node {
	public String name = "Recognition";
	public String version = "0.1.202005";
	public String vendor = "Meritoki";
	public String copyright = "(c)2020";
	public Model model;
	
	public Recognition(int id) {
		super(id);
		this.about();
	}

	@Override
	public void initialize() {
		super.initialize();
	}

	public String about() {
		String about = this.name + " Version " + this.version + " Copyright " + this.getVendor() + " " + this.copyright;
		System.out.println(about);
		return about;
	}

	public Object load(Integer id, Object object) {
		logger.info("load("+id+", "+object+")");
		String string = null;
		if (object instanceof String) {
			string = (String) object;
			if (string.equals(Train.class.getCanonicalName())) {
				object = new Train(id.intValue(), this);
			} 
			else if (string.equals(Inference.class.getCanonicalName())) {
				object = new Inference(id.intValue(), this);
			}
			else {
				logger.warn("load(" + id + ", " + object + ")");
				object = null;
			}

		}
		return object;
	}
	
	public void setModel(Model model) {
		
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
				moduleMapStart(this.moduleMap);
				try {
					if (logger.isDebugEnabled())
						logger.debug("defaultState(" + object + ") (countDownLatch.await())");
					countDownLatch.await();
				} catch (InterruptedException ie) {
					logger.error("defaultState(" + object + ") InterruptedException");
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
}