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

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.meritoki.app.desktop.retina.model.Model;
import com.meritoki.app.desktop.retina.model.document.Shape;
import com.meritoki.app.desktop.retina.model.provider.Provider;
import com.meritoki.library.cortex.model.Concept;
import com.meritoki.module.library.model.Module;
import com.meritoki.module.library.model.Node;
import com.meritoki.module.library.model.State;
import com.meritoki.module.library.model.data.Data;
import com.meritoki.module.library.model.data.DataType;

public class Inference extends Node {
	
	public static final int WAIT = 1;
	public static final int SEARCH = 2;
	public static final int SCAN = 3;
	private Model model;
	private Meritoki meritoki;
	private boolean scan = false;

	public Inference(int intValue, Module module, Model model) {
		super(intValue, module);
		this.model = model;
	}
	
	public void initialize() {
		super.initialize();
		this.meritoki = (Meritoki)this.model.system.providerMap.get("meritoki");
		this.setState(State.WAIT);
	}
	
	protected void machine(State state, Object object) {
		switch(state) {
		case WAIT: {
			this.wait(object);
			break;
		}
		case SEARCH: {
			this.search(object);
			break;
		}
		case SCAN: {
			this.scan(object);
			break;
		}
		default: {
			super.machine(state, object);
		}
		}
		
	}
	
	private void wait(Object object) {
		if(object instanceof Data) {
			Data data = (Data)object;
			switch(data.getType()) {
			case UNBLOCK:{
				this.setState(State.SEARCH);
				this.setDelay(this.newDelay(this.inputDelay));
				break;
			}
			}
		}
	}
	
	private void search(Object object) {
		if (object instanceof Data) {
			Data data = (Data) object;
		}
		if(this.delayExpired()) {
			if(this.scan) {
				this.setState(State.SCAN);
			} else {
				this.rootAdd(new Data(1,this.id, DataType.UNBLOCK,0,null,this.objectList));
				this.setDelay(this.newDelay(this.inputDelay));
				this.setState(State.WAIT);
			}
			this.setDelay(this.newDelay(this.inputDelay));
		}
	}
	
	private void scan(Object object) {
		if(this.delayExpired()) {
			this.rootAdd(new Data(1,this.id, DataType.UNBLOCK,0,null,this.objectList));
			this.setDelay(this.newDelay(this.inputDelay));
			this.setState(State.WAIT);
		}
	}
	
	public String scan(BufferedImage bufferedImage) {
		logger.info("scan("+bufferedImage+")");
		List<Concept> conceptList = null;
		Concept concept = null;
		if (bufferedImage != null) {
//			bufferedImage = this.scaleBufferedImage(bufferedImage, 0.25);
//			logger.info("scan(...) bufferedImaged="+bufferedImage);
			double width = bufferedImage.getWidth();
			double height = bufferedImage.getHeight();
			int radius = (int) (this.meritoki.document.cortex.getRadius());
			int wInterval = (int)(width/(radius));
			int hInterval = (int)(height/(radius));
			logger.info("scan(...) hInterval="+hInterval);
			logger.info("scan(...) wInterval="+wInterval);
			Map<String, Integer> conceptMap = new HashMap<>();
			for (int w = 0; w < width; w++) {
				for (int h = 0; h < height; h++) {
					if ((w%radius) == 0 && (h % radius) == 0) {
					this.meritoki.document.cortex.setOrigin(w, h);
					this.meritoki.document.cortex.update();
					this.meritoki.document.cortex.process(null,bufferedImage, null);
					conceptList = ((com.meritoki.library.cortex.model.network.Network)this.meritoki.document.cortex).getRootLevel().getCoincidenceConceptList();
					for (Concept c : conceptList) {
						Integer integer = conceptMap.get(c.toString());
						integer = (integer == null) ? 0 : integer;
						conceptMap.put(c.toString(), integer + 1);
					}
					concept = new Concept(this.getMaxConcept(conceptMap, 0.90));
					}
				}
			}
		}
		logger.info("scan(...) concept.value="+concept);
		return (concept != null)?concept.value: null;
	}
	
	public String getMaxConcept(Map<String, Integer> conceptMap, double threshold) {
		String concept = null;
		double max = 0;
		double sum = 0;
		for (Map.Entry<String, Integer> entry : conceptMap.entrySet()) {
			String key = entry.getKey();
			Integer value = entry.getValue();
			sum += value;
			if (key != null && !key.equals("null") && value > max) {
				max = value;
				concept = key;
			}
		}
		double quotient = (max > 0 && sum > 0) ? max / sum : 0;
//		logger.info(quotient + " " + concept);
		if (quotient < threshold) {
			concept = null;
		}
		return concept;
	}
	
	public BufferedImage scaleBufferedImage(BufferedImage bufferedImage, double scale) {
		BufferedImage before = bufferedImage;
		int w = before.getWidth();
		int h = before.getHeight();
		BufferedImage after = new BufferedImage((int)(w*scale), (int)(h*scale), BufferedImage.TYPE_INT_ARGB);
		AffineTransform at = new AffineTransform();
		at.scale(scale, scale);
		AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
		after = scaleOp.filter(before, after);
		return after;
	}
}
