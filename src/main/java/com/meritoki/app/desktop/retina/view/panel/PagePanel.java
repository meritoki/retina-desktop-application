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
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.meritoki.app.desktop.retina.model.Model;
import com.meritoki.app.desktop.retina.model.document.Document;
import com.meritoki.app.desktop.retina.model.document.Grid;
import com.meritoki.app.desktop.retina.model.document.Image;
import com.meritoki.app.desktop.retina.model.document.Page;
import com.meritoki.app.desktop.retina.model.document.Point;
import com.meritoki.app.desktop.retina.model.document.Position;
import com.meritoki.app.desktop.retina.model.document.Shape;
import com.meritoki.app.desktop.retina.model.tool.Tool;
import com.meritoki.app.desktop.retina.view.frame.MainFrame;

public class PagePanel extends JPanel implements MouseListener, MouseWheelListener, KeyListener {

	private static final long serialVersionUID = 3989576625299550361L;
	private static Logger logger = LogManager.getLogger(PagePanel.class.getName());
	private MainFrame mainFrame = null;
	private Model model = null;

	public PagePanel() {
		super();
		this.setBackground(Color.black);
		this.addMouseListener(this);
		this.addMouseWheelListener(this);
		this.addKeyListener(this);

	}

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
	public Dimension getPreferredSize() {
		Dimension dimension = new Dimension(1028, 512);
		Document document = (this.model != null) ? this.model.document : null;
		Page page = (document != null) ? document.getPage() : null;
		if (page != null) {
			dimension.setSize(page.position.dimension.width, page.position.dimension.height);
		}
		return dimension;
	}

	@Override
	public void paint(Graphics graphics) {
		super.paint(graphics);
		if (this.model != null) {
			Graphics2D graphics2D = (Graphics2D) graphics.create();
			Document document = (this.model != null) ? this.model.document : null;
			Page page = (document != null) ? document.getPage() : null;
			BufferedImage bufferedImage = null;
			if (page != null) {
				AffineTransform affineTransform = new AffineTransform();
				affineTransform.scale(page.position.scale, page.position.scale);// this handles scaling the
																				// bufferedImage
				bufferedImage = page.getBufferedImage(this.model);
				if (bufferedImage != null) {
					graphics2D.drawImage(bufferedImage, affineTransform, null);
				}
				List<Image> imageList = page.getImageList();
				Image image = page.getImage();
				if (imageList != null) {
					for (Image i : imageList) {
						Position p = i.position;
						if (image != null && i.uuid.equals(image.uuid)) {
							graphics2D.setColor(Color.RED);
						} else {
							graphics2D.setColor(Color.YELLOW);
						}
						Rectangle2D.Double rectangle = new Rectangle2D.Double(p.point.x, p.point.y, p.dimension.width,
								p.dimension.height);
						graphics2D.draw(rectangle);
					}

				}
				List<Shape> selectorShapeList = null;
				if (this.model.cache.selector != null) {
					graphics2D.setColor(Color.PINK);
					Position position = this.model.cache.selector.position;
					Rectangle2D.Double rectangle = new Rectangle2D.Double(position.point.x, position.point.y,
							position.dimension.width, position.dimension.height);
					graphics2D.draw(rectangle);
					selectorShapeList = this.model.cache.selector.shapeList;
				}
				List<Shape> shapeList = page.getSortedShapeList();
				Shape shape = page.getShape();
				Shape gridShape = page.getGridShape();
				Shape previousShape = null;
				if (shapeList != null) {
					for (Shape s : shapeList) {
						Position position = s.position;
						if (selectorShapeList != null && selectorShapeList.contains(s)) {
							graphics2D.setColor(Color.GREEN);
						} else if (shape != null && s.uuid.equals(shape.uuid)) {
							graphics2D.setColor(Color.RED);
						} else {
							graphics2D.setColor(Color.BLUE);
						}
						switch (s.type) {
						case ELLIPSE: {
							Ellipse2D.Double ellipse = new Ellipse2D.Double(position.point.x, position.point.y,
									position.dimension.width, position.dimension.height);
							graphics2D.draw(ellipse);
							break;
						}
						case RECTANGLE: {
							Rectangle2D.Double rectangle = new Rectangle2D.Double(position.point.x, position.point.y,
									position.dimension.width, position.dimension.height);
							graphics2D.draw(rectangle);
							if (s instanceof Grid) {
								Shape[][] matrix = ((Grid) s).matrix;
								for (int i = 0; i < matrix.length; i++) {
									for (int j = 0; j < matrix[i].length; j++) {
										Shape gs = matrix[i][j];
										if (gridShape != null && gs.uuid.equals(gridShape.uuid)) {
											graphics2D.setColor(Color.YELLOW);
										} else {
											if (shape != null && s.uuid.equals(shape.uuid)) {
												graphics2D.setColor(Color.RED);
											} else {
												graphics2D.setColor(Color.BLUE);
											}
										}
										rectangle = new Rectangle2D.Double(gs.position.point.x, gs.position.point.y,
												gs.position.dimension.width, gs.position.dimension.height);
										graphics2D.draw(rectangle);
									}
								}
							}
							break;
						}
						}
						if (previousShape != null) {
							Position p = previousShape.position;
							graphics2D.drawLine((int) (p.center.x), (int) (p.center.y), (int) (position.center.x),
									(int) (position.center.y));
						}
						previousShape = s;
					}
				}
				graphics2D.setColor(Color.BLUE);
				Rectangle2D.Double frame = new Rectangle2D.Double(0, 0, page.position.dimension.width,
						page.position.dimension.height);
				graphics2D.draw(frame);
			}
//			this.meritoki.document.cortex.process(graphics2D, bufferedImage, this.model.cache.concept);
		}
	}

	/**
	 * Here is mouse is pressed, this is the start of many operations. Here we know
	 * what page is selected, if the event lies in or outside of a shape,etc.
	 * Commands we need to support include Add, Select, Move, Resize, and Remove
	 * Shape the commands that we need to support require checking the current page
	 * for a contains or intersect with respect to Shapes. if contains or intersect
	 * are false, we have an Add. Every other case is a Select followed by a Move,
	 * Resize, or Remove Looking for a general architecture to capture these
	 * possibilities that is maintainable and supports the Do/Undo/Redo system An
	 * undo/redo also remembers the Page where something happened, taking us to the
	 * page where something is undone if necessary. Here we know that we are
	 * choosing an existing Shape This can be used for Select, Remove, Move, If
	 * point pressed and point released are equal we have a select, once we have a
	 * select we can Remove. if pressed point does not equal released, we have a
	 * move. Here we know we are choosing an edge to resize, and we know what edge
	 * or corner is selected based on the selection integer value, all we need now
	 * is the release point.
	 */
	@Override
	public void mousePressed(MouseEvent me) {
		Point pressedPoint = new Point();
		pressedPoint.x = me.getX();
		pressedPoint.y = me.getY();
		this.model.system.pressedPoint = pressedPoint;
		logger.trace("mousePressed(me) pressedPoint=" + pressedPoint);
		this.model.system.pressedImage = this.model.document.getImage(pressedPoint);
		Image image = this.model.document.getImage();
		if (this.model.system.pressedImage != null && image != null && !image.equals(this.model.system.pressedImage)) {
			try {
				this.model.cache.pressedImageUUID = this.model.system.pressedImage.uuid;
				this.model.cache.imageUUID = image.uuid;
				this.model.pattern.execute("setImage");
			} catch (NullPointerException e) {
				e.printStackTrace();
			} catch (Exception e) {
				JOptionPane.showMessageDialog(mainFrame, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
		this.model.system.pressedShape = this.model.document.getShape(pressedPoint);
	}

	/**
	 * Here a mouse is released, telling us where an Add, Move, Resize, and Remove
	 */
	@Override
	public void mouseReleased(MouseEvent me) {
		this.model.system.releasedPoint = new Point(me.getX(), me.getY());
		if (this.model.system.tool != null) {
			switch (this.model.system.tool) {
			case SELECT: {
				if (this.model.system.pressedPoint.equals(this.model.system.releasedPoint)) {
					this.model.cache.selector = null;
					if (this.model.system.pressedShape != null) {
						try {
							this.model.cache.shapeUUID = this.model.system.pressedShape.uuid;
							this.model.cache.pressedShapeUUID = this.model.system.pressedShape.uuid;
							this.model.pattern.execute("setShape");
						} catch (NullPointerException e) {
							e.printStackTrace();
						} catch (Exception e) {
							JOptionPane.showMessageDialog(mainFrame, e.getMessage(), "Error",
									JOptionPane.ERROR_MESSAGE);
						}
					}
				} else {
					try {
						this.model.cache.pressedPageUUID = this.model.document.getPage().uuid;
						this.model.cache.pressedImageUUID = this.model.system.pressedImage.uuid;
						this.model.cache.pressedPoint = this.model.system.pressedPoint;
						this.model.cache.releasedPoint = this.model.system.releasedPoint;
						this.model.pattern.execute("addSelector");
					} catch (NullPointerException e) {
						e.printStackTrace();
					} catch (Exception e) {
						JOptionPane.showMessageDialog(mainFrame, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
				break;
			}
			case RESIZE: {
				if (this.model.document.getShape() != null) {
					this.model.system.selection = this.model.document.getPage()
							.intersectShape(this.model.system.pressedPoint);
					if (this.model.system.selection != null) {
						try {
							this.model.cache.selection = this.model.system.selection;
							this.model.cache.pressedShapeUUID = this.model.document.getShape().uuid;
							this.model.cache.releasedPoint = this.model.system.releasedPoint;
							this.model.pattern.execute("resizeShape");
						} catch (NullPointerException e) {
							e.printStackTrace();
						} catch (Exception e) {
							JOptionPane.showMessageDialog(mainFrame, e.getMessage(), "Error",
									JOptionPane.ERROR_MESSAGE);
						}
					}
				}
				break;
			}
			case MOVE: {
				if (this.model.cache.selector != null) {
					try {
						this.model.cache.pressedPageUUID = this.model.document.getPage().uuid;
						this.model.cache.pressedPoint = this.model.system.pressedPoint;
						this.model.cache.releasedPoint = this.model.system.releasedPoint;
						this.model.pattern.execute("moveSelector");
					} catch (NullPointerException e) {
						e.printStackTrace();
					} catch (Exception e) {
						JOptionPane.showMessageDialog(mainFrame, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					}
				} else if (this.model.document.getShape() != null) {
					try {
						this.model.cache.pressedPageUUID = this.model.document.getPage().uuid;
						this.model.cache.pressedShapeUUID = this.model.document.getShape().uuid;
						this.model.cache.pressedPoint = this.model.system.pressedPoint;
						this.model.cache.releasedPoint = this.model.system.releasedPoint;
						this.model.pattern.execute("moveShape");
					} catch (NullPointerException e) {
						e.printStackTrace();
					} catch (Exception e) {
						JOptionPane.showMessageDialog(mainFrame, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
				break;
			}
			case DRAW: {
				try {
					this.model.cache.pressedPageUUID = this.model.document.getPage().uuid;
					this.model.cache.pressedImageUUID = this.model.system.pressedImage.uuid;
					this.model.cache.pressedPoint = this.model.system.pressedPoint;
					this.model.cache.releasedPoint = this.model.system.releasedPoint;
					this.model.pattern.execute("addShape");
				} catch (NullPointerException e) {
					e.printStackTrace();
				} catch (Exception e) {
					JOptionPane.showMessageDialog(mainFrame, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
				break;
			}
			default: {

			}
			}
		}
		this.mainFrame.init();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		this.requestFocus();
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent ke) {
		ke.consume();
		if (ke.isControlDown()) {
			switch (ke.getKeyCode()) {
			case KeyEvent.VK_S: {
				logger.debug("keyPressed(e) KeyEvent.VK_S");
				this.mainFrame.save();
				break;
			}
			case KeyEvent.VK_EQUALS: {
				logger.debug("keyPressed(e) KeyEvent.VK_EQUALS");
				try {
					this.model.cache.pressedPageUUID = this.model.document.getPage().uuid;
					this.model.cache.scaleOperator = '*';
					this.model.cache.scaleFactor = 1.5;
					this.model.pattern.execute("scalePage");
					this.mainFrame.init();
				} catch (Exception e) {
					JOptionPane.showMessageDialog(mainFrame, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
				break;
			}
			case KeyEvent.VK_PLUS: {
				logger.debug("keyPressed(e) KeyEvent.VK_PLUS");

				try {
					this.model.cache.pressedPageUUID = this.model.document.getPage().uuid;
					this.model.cache.scaleOperator = '*';
					this.model.cache.scaleFactor = 1.5;
					this.model.pattern.execute("scalePage");
					this.mainFrame.init();
				} catch (Exception e) {
					JOptionPane.showMessageDialog(mainFrame, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
				break;
			}
			case KeyEvent.VK_MINUS: {
				logger.debug("keyPressed(e) KeyEvent.VK_MINUS");

				try {
					this.model.cache.pressedPageUUID = this.model.document.getPage().uuid;
					this.model.cache.scaleOperator = '/';
					this.model.cache.scaleFactor = 1.5;
					this.model.pattern.execute("scalePage");
					this.mainFrame.init();
				} catch (Exception e) {
					JOptionPane.showMessageDialog(mainFrame, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
				break;
			}
			case KeyEvent.VK_DOWN: {
				logger.debug("keyPressed(e) KeyEvent.VK_DOWN");
				try {
					this.model.cache.pressedPageUUID = this.model.document.getPage().uuid;
					this.model.cache.pressedImageUUID = this.model.document.getImage().uuid;
					this.model.cache.shiftOperator = '+';
					this.model.cache.shiftFactor = 10;
					this.model.pattern.execute("shiftImage");
					this.mainFrame.init();
				} catch (Exception e) {
					JOptionPane.showMessageDialog(mainFrame, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}

				break;
			}
			case KeyEvent.VK_UP: {
				logger.debug("keyPressed(e) KeyEvent.VK_UP");
				try {
					this.model.cache.pressedPageUUID = this.model.document.getPage().uuid;
					this.model.cache.pressedImageUUID = this.model.document.getImage().uuid;
					this.model.cache.shiftOperator = '-';
					this.model.cache.shiftFactor = 10;
					this.model.pattern.execute("shiftImage");
					this.mainFrame.init();
				} catch (Exception e) {
					JOptionPane.showMessageDialog(mainFrame, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
				break;
			}
			case KeyEvent.VK_LEFT: {
				logger.info("keyPressed(e) KeyEvent.VK_LEFT");

				try {
					this.model.cache.pressedPageUUID = this.model.document.getPage().uuid;
					this.model.cache.pressedImageUUID = this.model.document.getImage().uuid;
					this.model.cache.scaleOperator = '/';
					this.model.cache.scaleFactor = 1.01;
					this.model.pattern.execute("scaleImage");
					this.mainFrame.init();
				} catch (NullPointerException e) {
					e.printStackTrace();
				} catch (Exception e) {
					JOptionPane.showMessageDialog(mainFrame, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
				break;
			}
			case KeyEvent.VK_RIGHT: {
				logger.info("keyPressed(e) KeyEvent.VK_RIGHT");
				try {
					this.model.cache.pressedPageUUID = this.model.document.getPage().uuid;
					this.model.cache.pressedImageUUID = this.model.document.getImage().uuid;
					this.model.cache.scaleOperator = '*';
					this.model.cache.scaleFactor = 1.01;
					this.model.pattern.execute("scaleImage");
					this.mainFrame.init();
				} catch (NullPointerException e) {
					e.printStackTrace();
				} catch (Exception e) {
					JOptionPane.showMessageDialog(mainFrame, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
				break;
			}
			case KeyEvent.VK_Z: {
				logger.debug("keyPressed(e) KeyEvent.VK_Z");
				try {
					this.model.pattern.undo();
					this.mainFrame.init();
				} catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(mainFrame, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
				break;
			}
			case KeyEvent.VK_Y: {
				logger.debug("keyPressed(e) KeyEvent.VK_Y");
				try {
					this.model.pattern.redo();
					this.mainFrame.init();
				} catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(mainFrame, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
				break;

			}
			}
		} else {
			int keyCode = ke.getKeyCode();
			int index = this.model.document.getIndex();
			switch (keyCode) {
			case KeyEvent.VK_BACK_SPACE: {
				try {
					this.model.cache.pressedShapeUUID = (this.model.document.getPage().getShape() != null)
							? this.model.document.getPage().getShape().uuid
							: null;
					this.model.pattern.execute("removeShape");
					this.mainFrame.init();
				} catch (Exception e) {
					JOptionPane.showMessageDialog(mainFrame, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
				break;
			}
			case KeyEvent.VK_LEFT: {
				logger.debug("keyPressed(e) KeyEvent.VK_LEFT");
				this.model.cache.pageIndex = --index;
				try {
					this.model.pattern.execute("setPage");
					this.mainFrame.init();
				} catch (Exception e) {
					JOptionPane.showMessageDialog(mainFrame, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
				break;
			}
			case KeyEvent.VK_RIGHT: {
				logger.debug("keyPressed(e) KeyEvent.VK_RIGHT");
				this.model.cache.pageIndex = ++index;
				try {
					this.model.pattern.execute("setPage");
					this.mainFrame.init();
				} catch (Exception e) {
					JOptionPane.showMessageDialog(mainFrame, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
				break;
			}
			case KeyEvent.VK_UP: {
				logger.debug("keyPressed(e) KeyEvent.VK_UP");
				this.model.cache.pageIndex = --index;
				try {
					this.model.pattern.execute("setPage");
					this.mainFrame.init();
				} catch (Exception e) {
					JOptionPane.showMessageDialog(mainFrame, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
				break;
			}
			case KeyEvent.VK_DOWN: {
				logger.debug("keyPressed(e) KeyEvent.VK_DOWN");
				this.model.cache.pageIndex = ++index;
				try {
					this.model.pattern.execute("setPage");
					this.mainFrame.init();
				} catch (Exception e) {
					JOptionPane.showMessageDialog(mainFrame, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
				break;
			}
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		if (e.isControlDown()){
			int wheelRotation = e.getWheelRotation();
			
			logger.info("mouseWheelMoved(e) wheelRotation="+wheelRotation);
			try {
				this.model.cache.pressedPageUUID = this.model.document.getPage().uuid;
				this.model.cache.scaleOperator = (wheelRotation == -1)?'/':'*';
				this.model.cache.scaleFactor = 1.5;
				this.model.pattern.execute("scalePage");
				this.mainFrame.init();
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(mainFrame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
        }
		
	}
}