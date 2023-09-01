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
package com.meritoki.app.desktop.retina.view.panel.meritoki;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.meritoki.app.desktop.retina.model.Model;
import com.meritoki.app.desktop.retina.model.provider.Provider;
import com.meritoki.app.desktop.retina.model.provider.meritoki.Meritoki;
import com.meritoki.app.desktop.retina.view.frame.MainFrame;
import com.meritoki.library.cortex.model.Belief;
import com.meritoki.library.cortex.model.network.Level;
import com.meritoki.library.cortex.model.network.Network;
import com.meritoki.library.cortex.model.network.Shape;

/**
 *
 * @author jorodriguez
 */
public class RetinaPanel extends JPanel {

	private static Logger logger = LogManager.getLogger(RetinaPanel.class.getName());
	private MainFrame mainFrame = null;
	private Model model = null;
	private Meritoki meritoki;

	public void setMainFrame(MainFrame main) {
		this.mainFrame = main;
	}

	public void setModel(Model model) {
		this.model = model;
//		this.init();
		this.setPreferredSize(this.getPreferredSize());
		this.meritoki = (Meritoki)this.model.system.providerMap.get("meritoki");
	}
	public void init() {
		logger.debug("init()");
		this.repaint();
		this.revalidate();
	}

	@Override
	public void paint(Graphics graphics) {
		super.paint(graphics);
		if (this.model != null) {
			Graphics2D graphics2D = (Graphics2D) graphics.create();
//			this.meritoki.retina.drawCortexColor(graphics2D);
//			Belief belief = (this.meritoki.document != null)?this.meritoki.document.cortex.getBelief(): null;
//			if(belief != null) {
//				AffineTransform affineTransform = new AffineTransform();
//				affineTransform.scale(1, 1);// this handles scaling the
//				graphics2D.drawImage(belief.getBufferedImage(), affineTransform, null);
//			}
		}
	}
}

//Level level = ((Network) this.meritoki.document.cortex).getInputLevel();
//if (level != null) {
//	int dimension = (int)(this.meritoki.document.cortex.getSensorRadius()*2);
//	double scale = 1;
//	dimension *= scale;
//	BufferedImage bufferedImage = new BufferedImage(dimension, dimension, BufferedImage.TYPE_INT_RGB);
//	
//	for (Shape shape : level.getShapeList()) {
//		shape.initCells();
//		for (int i = 0; i < shape.sides+1; i++) {
//			int brightness = shape.coincidence.list.get(0);
////			System.out.println("paint(...) brightness="+brightness);
//			Color color = new Color(brightness, brightness, brightness);
//			double x = (shape.xpoints[i]-this.meritoki.document.cortex.getX());
//			double y = (shape.ypoints[i]-this.meritoki.document.cortex.getY());
//			x *= scale;
//			y *= scale;
////			if(x > 0 && y > 0) {
////				System.out.println("x="+x+", y="+y);
//				bufferedImage.setRGB((int)x+dimension/2,(int)y+dimension/2, color.getRGB());
////			}
//		}
//		AffineTransform affineTransform = new AffineTransform();
//		affineTransform.scale(1, 1);// this handles scaling the
//		if (bufferedImage != null) {
//			graphics2D.drawImage(bufferedImage, affineTransform, null);
//		}
//	}
//}