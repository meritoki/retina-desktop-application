/*
 * Copyright 2019 osvaldo.rodriguez.
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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JPanel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.meritoki.app.desktop.retina.model.Model;
import com.meritoki.app.desktop.retina.model.document.Data;
import com.meritoki.app.desktop.retina.model.document.Document;
import com.meritoki.app.desktop.retina.model.document.Matrix;
import com.meritoki.app.desktop.retina.model.document.Page;

/**
 *
 * @author osvaldo.rodriguez
 */
public class MatrixPanel extends JPanel implements MouseListener, MouseWheelListener, KeyListener {

	private static final long serialVersionUID = 6483831845668642285L;
	private static Logger logger = LogManager.getLogger(Data.class.getName());
	private Model model = null;

	/**
	 * Instantiate new Matrix Panel
	 */
	public MatrixPanel() {
		super();
//		this.setOpaque(true);
		this.setBackground(Color.white);
		this.addMouseListener(this);
		this.addMouseWheelListener(this);
		this.addKeyListener(this);

	}

	/**
	 * Set the Model for use in the Panel
	 * 
	 * @param model
	 */
	public void setModel(Model model) {
		logger.debug("setModel(" + model + ")");
		this.model = model;
	}
	

	@Override
	public Dimension getPreferredSize() {
		Dimension dimension = new Dimension(1028, 512);
		Document document = (this.model != null) ? this.model.getDocument() : null;
		Page page = (document != null) ? document.getPage() : null;
		Matrix matrix = (page != null) ? page.getMatrix():null;
		if(matrix != null) {
			matrix.setScale(this.model.document.cache.scale);
			dimension.setSize(matrix.dimension.width, matrix.dimension.height);
		}
		return dimension;
	}

	@Override
	public void paint(Graphics graphics) {
		super.paint(graphics);
		graphics.setColor(Color.white);
		graphics.fillRect(0, 0, this.getWidth(), this.getHeight());
		if (this.model != null) {
			Document document = (this.model != null) ? this.model.getDocument() : null;
			Page page = (document != null) ? document.getPage() : null;
			Matrix matrix = page.getMatrix();
			matrix.setScale(this.model.document.cache.scale);
			matrix.paint(graphics);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		this.requestFocus();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {

	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void keyPressed(KeyEvent e) {

	}

	@Override
	public void keyReleased(KeyEvent ke) {
		ke.consume();
		if (ke.isControlDown()) {
			switch (ke.getKeyCode()) {
			case KeyEvent.VK_EQUALS: {
				logger.info("keyPressed(e) KeyEvent.VK_EQUALS");
				double scale = this.model.document.cache.scale;
				scale = scale * 1.5;
				this.model.document.cache.scale = scale;
				this.repaint();
				this.revalidate();
				break;
			}
			case KeyEvent.VK_PLUS: {
				logger.info("keyPressed(e) KeyEvent.VK_PLUS");
				double scale = this.model.document.cache.scale;
				scale = scale * 1.5;
				this.model.document.cache.scale = scale;
				this.repaint();
				this.revalidate();
				break;
			}
			case KeyEvent.VK_MINUS: {
				logger.info("keyPressed(e) KeyEvent.VK_MINUS");
				double scale = this.model.document.cache.scale;
				scale = scale / 1.5;
				this.model.document.cache.scale = scale;
				this.repaint();
				this.revalidate();
				break;
			}
			}
		}
	}
}
