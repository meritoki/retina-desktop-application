package com.meritoki.app.desktop.retina.model.module;

import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import com.meritoki.app.desktop.retina.model.Model;
import com.meritoki.app.desktop.retina.model.provider.Provider;
import com.meritoki.app.desktop.retina.model.provider.meritoki.Meritoki;
import com.meritoki.module.library.model.Module;
import com.meritoki.module.library.model.Node;

public class Recognition extends Node {
	public String name = "Recognition";
	public String version = "0.1.202006";
	public String vendor = "Meritoki";
	public String year = "2020";
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
        for (Provider provider : this.model.system.providerList) {
            if (provider instanceof Meritoki) {
                this.meritoki = (Meritoki) provider;
                this.meritoki.open(this.model.document.uuid);
            }
        }
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
	
	@Override
	public void destroy() {
		this.meritoki.save();
	}
}