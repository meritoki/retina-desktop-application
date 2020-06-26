package com.meritoki.app.desktop.retina.model.module;

import java.awt.image.BufferedImage;

import com.meritoki.app.desktop.retina.model.Model;
import com.meritoki.app.desktop.retina.model.provider.Provider;
import com.meritoki.app.desktop.retina.model.provider.meritoki.Input;
import com.meritoki.app.desktop.retina.model.provider.meritoki.Meritoki;
import com.meritoki.module.library.model.Data;
import com.meritoki.module.library.model.Module;
import com.meritoki.module.library.model.Node;

public class Inference extends Node {
	
	public static final int WAIT = 1;
	public static final int SCAN = 2;
	private Model model;
	private Meritoki meritoki;

	public Inference(int intValue, Module module, Model model) {
		super(intValue, module);
		this.model = model;
	}
	
	public void initialize() {
		super.initialize();
        for (Provider provider : this.model.system.providerList) {
            if (provider instanceof Meritoki) {
                this.meritoki = (Meritoki) provider;
            }
        }
		this.stateMap.put(WAIT,"WAIT");
		this.stateMap.put(SCAN,"SCAN");
		this.setState(WAIT);
	}
	
	protected void machine(int state, Object object) {
		switch(state) {
		case WAIT: {
			this.wait(object);
			break;
		}
		case SCAN: {
			this.scan(object);
			break;
		}
		}
		super.machine(state, object);
	}
	
	private void wait(Object object) {
		if(object instanceof Data) {
			Data data = (Data)object;
			switch(data.getType()) {
			case Data.UNBLOCK:{
				this.setState(SCAN);
				this.setDelay(this.newDelay(this.inputDelay));
				break;
			}
			}
		}
	}
	
	private void scan(Object object) {
		if(object instanceof Data) {
			Data data = (Data)object;
		}
		if(this.delayExpired()) {
			logger.info("scan(...)");
			this.setState(WAIT);
		}
	}
	
	public void scan(BufferedImage bufferedImage, String concept) {
		logger.info("scan("+bufferedImage+", "+concept+")");
	}
}
