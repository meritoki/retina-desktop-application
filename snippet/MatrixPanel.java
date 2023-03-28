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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.meritoki.app.desktop.retina.model.Model;
import com.meritoki.app.desktop.retina.model.document.Data;
import com.meritoki.app.desktop.retina.model.document.Document;
import com.meritoki.app.desktop.retina.model.document.Matrix;
import com.meritoki.app.desktop.retina.model.document.Page;
import com.meritoki.app.desktop.retina.model.document.Shape;
import com.meritoki.app.desktop.retina.model.document.Text;

/**
 *
 * @author osvaldo.rodriguez
 */
public class MatrixPanel extends JPanel implements MouseListener, MouseWheelListener, KeyListener {

	private static final long serialVersionUID = 6483831845668642285L;
	private static Logger logger = LogManager.getLogger(MatrixPanel.class.getName());
	private Model model = null;

	/**
	 * Instantiate new Matrix Panel
	 */
	public MatrixPanel() {
		super();
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
		this.model = model;
		this.init();
	}

	public void init() {
		logger.debug("init()");
		this.repaint();
		this.revalidate();
	}

	@Override
	public Dimension getPreferredSize() {
		Dimension dimension = new Dimension(1028, 512);
		Document document = (this.model != null) ? this.model.document : null;
		Page page = (document != null) ? document.getPage() : null;
		Matrix matrix = (page != null) ? page.getMatrix() : null;
		if (matrix != null) {
			dimension.setSize(matrix.position.dimension.width, matrix.position.dimension.height);
		}
		return dimension;
	}

	@Override
	public void paint(Graphics graphics) {
		super.paint(graphics);
		graphics.setColor(Color.white);
		graphics.fillRect(0, 0, this.getWidth(), this.getHeight());
		if (this.model != null) {
			Document document = (this.model != null) ? this.model.document : null;
			Page page = (document != null) ? document.getPage() : null;
			Matrix matrix = (page != null) ? page.getMatrix() : null;
			if (matrix != null) {
				graphics.setColor(Color.black);
				List<ArrayList<Shape>> rowList = matrix.getTableRowList();
				List<Shape> shapeList;
				int width = matrix.width;//(int) (this.position.dimension.width * 0.10);
				int height = matrix.height;//(int) (this.position.dimension.height * 0.10);
				int widthIndex = 0;
				int heightIndex = 0;
				graphics.setFont(new Font("default", Font.BOLD, (int) (8 * matrix.position.scale)));
				Data data;
				Shape shape = this.model.document.getPage().getShape();
				Shape gridShape = this.model.document.getPage().getGridShape();
				Shape s;
				for (int i = 0; i < rowList.size(); i++) {
					shapeList = rowList.get(i);
					heightIndex = (int) (i * height);
					for (int j = 0; j < shapeList.size(); j++) {
						graphics.setColor(Color.BLACK);
						widthIndex = (int) (j * width);
						s = shapeList.get(j);
						if (shape != null && s.uuid.equals(shape.uuid)) {
							graphics.setColor(Color.RED);
						} else if (gridShape != null && s.uuid.equals(gridShape.uuid)) {
							graphics.setColor(Color.RED);
						}
						else {
							graphics.setColor(Color.BLACK);
						}
						graphics.drawRect(widthIndex, heightIndex, width, height);
						data = s.data;
						if (data != null) {
							Text text = data.text;
							switch (data.unit.type) {
							case DATA: {
//								graphics.setColor(Color.BLACK);
								graphics.drawString("D", widthIndex + (width / 2), heightIndex + (height / 2));
								break;
							}
							case TIME: {
								graphics.drawString("T", widthIndex + (width / 2), heightIndex + (height / 2));
								break;
							}
							case SPACE: {
								graphics.drawString("S", widthIndex + (width / 2), heightIndex + (height / 2));
								break;
							}
							case ENERGY: {
								graphics.drawString("E", widthIndex + (width / 2), heightIndex + (height / 2));
								break;
							}
							}
//							if(text.value == null) {
								String id = s.uuid.substring(0,8);
								int z = graphics.getFontMetrics().stringWidth(id);
								graphics.drawString(id, widthIndex + (width/2) - (z / 2), heightIndex + (height*3/4));
//							} else {
//								String id = text.value;
//								int z = graphics.getFontMetrics().stringWidth(id);
//								graphics.drawString(id, widthIndex + (width/2) - (z / 2), heightIndex + (height*3/4));
//							}
						}
					}
				}
			}
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
//		if (ke.isControlDown()) {
//			switch (ke.getKeyCode()) {
//			case KeyEvent.VK_EQUALS: {
//				logger.info("keyPressed(e) KeyEvent.VK_EQUALS");
//				double scale = this.model.document.cache.scale;
//				scale = scale * 1.5;
//				this.model.document.cache.scale = scale;
//				this.repaint();
//				this.revalidate();
//				break;
//			}
//			case KeyEvent.VK_PLUS: {
//				logger.info("keyPressed(e) KeyEvent.VK_PLUS");
//				double scale = this.model.document.cache.scale;
//				scale = scale * 1.5;
//				this.model.document.cache.scale = scale;
//				this.repaint();
//				this.revalidate();
//				break;
//			}
//			case KeyEvent.VK_MINUS: {
//				logger.info("keyPressed(e) KeyEvent.VK_MINUS");
//				double scale = this.model.document.cache.scale;
//				scale = scale / 1.5;
//				this.model.document.cache.scale = scale;
//				this.repaint();
//				this.revalidate();
//				break;
//			}
//			}
//		}
	}
}
