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
package com.meritoki.app.desktop.retina.view.panel;

import com.meritoki.app.desktop.retina.model.Model;
import com.meritoki.app.desktop.retina.model.document.Document;
import com.meritoki.app.desktop.retina.model.document.Grid;
import com.meritoki.app.desktop.retina.model.document.Page;
import com.meritoki.app.desktop.retina.model.document.Shape;
import com.meritoki.app.desktop.retina.view.frame.MainFrame;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author jorodriguez
 */
public class ShapePanel extends JPanel {

    private static Logger logger = LogManager.getLogger(ShapePanel.class.getName());
    private MainFrame mainFrame = null;
    private Model model = null;

    public void setMainFrame(MainFrame main) {
        this.mainFrame = main;
    }

    public void setModel(Model model) {
        this.model = model;
        this.init();
        this.setPreferredSize(this.getPreferredSize());
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
			Document document = (this.model != null) ? this.model.document : null;
			Page page = (document != null)?document.getPage():null;
			Shape shape = (page != null) ? page.getShape(): null;
			if (shape != null) {
				if(shape instanceof Grid) {
					Grid grid = (Grid)shape;
					shape = grid.getShape();
				}
				AffineTransform affineTransform = new AffineTransform();
				affineTransform.scale(1, 1);
				BufferedImage bufferedImage = this.model.document.getShapeBufferedImage(page.getScaledBufferedImage(this.model), shape);
				if (bufferedImage != null) {
					graphics2D.drawImage(bufferedImage, affineTransform, null);
				}
			}
		}
	}
}
