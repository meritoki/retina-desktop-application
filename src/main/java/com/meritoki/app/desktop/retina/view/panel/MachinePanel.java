/*
 * Copyright 2020 Joaquin Osvaldo Rodriguez
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
import java.awt.image.BufferedImage;
import java.util.LinkedList;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.meritoki.app.desktop.retina.model.Model;
import com.meritoki.app.desktop.retina.model.document.Document;
import com.meritoki.app.desktop.retina.model.document.Page;
import com.meritoki.app.desktop.retina.model.provider.Provider;
import com.meritoki.app.desktop.retina.model.provider.meritoki.Meritoki;
import com.meritoki.app.desktop.retina.view.frame.MainFrame;
import com.meritoki.library.cortex.model.Point;
import com.meritoki.library.cortex.model.retina.Retina;

public class MachinePanel extends JPanel implements MouseListener, KeyListener, Runnable {

	private static final long serialVersionUID = 3989576625299550361L;
	private static Logger logger = LogManager.getLogger(MachinePanel.class.getName());
	private MainFrame mainFrame = null;
	private Model model = null;
	private Meritoki meritoki;
	private Retina retina;
	private double distanceFactor = 8;
	private double distance = 0;
	private Thread thread;
	private LinkedList<Point> pointStack = new LinkedList<>();
	private int index;
	private int interval = 8;
	private int size;
	private boolean flag = false;

	public MachinePanel() {
		super();
		this.setBackground(Color.black);
		this.addMouseListener(this);
		this.addKeyListener(this);
	}

	public void setMainFrame(MainFrame main) {
		this.mainFrame = main;
	}

	public void setModel(Model model) {
		this.model = model;
		this.init();
		for (Provider provider : this.model.system.providerList) {
			if (provider instanceof Meritoki) {
				this.meritoki = (Meritoki) provider;
			}
		}
		this.retina = new Retina(this.getBufferedImage(), this.meritoki.document.cortex);
	}

	public void init() {
		logger.info("init()");
		this.repaint();
		this.revalidate();
		this.setPreferredSize(this.getPreferredSize());
	}

	@Override
	public Dimension getPreferredSize() {
		Dimension dimension = new Dimension(1028, 512);
		if (this.retina != null)
			dimension.setSize(this.retina.getObjectWidth(), this.retina.getObjectHeight());
		return dimension;
	}

	@Override
	public void run() {
		System.out.println("run()");
		while (flag) {
			this.init();
			try {
				Thread.sleep(500);
			} catch (Exception e) {
			}
		}
	}

	@Override
	public void paint(Graphics graphics) {
		System.out.println("paint(...)");
		super.paint(graphics);
		if (flag) {
			if (pointStack.size() > 0) {
				Point point = this.pointStack.pop();
				this.retina.setOrigin(point.x, point.y);
				this.retina.scan(graphics);
			} else {
				this.retina.setBufferedImage(this.getBufferedImage());
				this.retina.setCortex(meritoki.document.cortex);
				System.out.println("this.retina.getMagnification()=" + this.retina.getMagnification());
				if (this.distance == 0) {
					System.out.println("this.distance == 0");
					this.retina.maxDistance = this.retina.getMaxDistance();
					this.distance = this.retina.maxDistance;
					this.size = (int) (this.retina.getMaxDistance() - this.retina.focalLength);
					System.out.println("size=" + size);
					this.index = 0;
					this.retina.setDistance(this.distance);
					this.retina.scan(graphics);
					this.pointStack = this.retina.getPointList();
					System.out.println("this.pointStack.size()=" + this.pointStack.size());
				} else {
					this.interval = this.size /12;
					if ((index * this.interval) < this.size) {
						System.out.println("index=" + index);
						this.distance = size;
						this.distance -= index * this.interval;
						this.index++;
						this.retina.setDistance(this.distance);
						this.retina.scan(graphics);
						this.pointStack = this.retina.getPointList();
						System.out.println("this.pointStack.size()=" + this.pointStack.size());
					} else {
						flag = false;
					}
				}
			}
		} else {
			
			this.retina.setBufferedImage(this.getBufferedImage());
			this.retina.setCortex(meritoki.document.cortex);
			if (this.distance == 0) {
				System.out.println("this.distance == 0");
				this.retina.maxDistance = this.retina.getMaxDistance();
				this.distance = this.retina.maxDistance;
				this.setPreferredSize(this.getPreferredSize());
			}
			this.retina.setDistance(this.distance);
			this.retina.scan(graphics);
		}
	}
	
	public BufferedImage getBufferedImage() {
		System.out.println("getBufferedImage()");
		Document document = (this.model != null) ? this.model.document : null;
		Page page = (document != null) ? document.getPage() : null;
		BufferedImage bufferedImage = (page != null) ? page.getBufferedImage(this.model) : null;
		return bufferedImage;
	}

	public double function(int index) {
		return Math.sqrt(index);
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
//		this.requestFocus();
//		int x = me.getX();
//		int y = me.getY();
//		this.meritoki.document.cortex.setOrigin(x, y);
//		this.meritoki.document.cortex.update();
//		Point pressedPoint = new Point();
//		pressedPoint.x = me.getX();
//		pressedPoint.y = me.getY();
//		this.model.system.pressedPoint = pressedPoint;
//		logger.trace("mousePressed(me) pressedPoint=" + pressedPoint);
//		this.model.system.pressedImage = this.model.document.getImage(pressedPoint);
//		Image image = this.model.document.getImage();
//		if (this.model.system.pressedImage != null && image != null && !image.equals(this.model.system.pressedImage)) {
//			try {
//				this.model.cache.pressedImageUUID = this.model.system.pressedImage.uuid;
//				this.model.cache.imageUUID = image.uuid;
//				this.model.pattern.execute("setImage");
//			} catch (NullPointerException e) {
//				e.printStackTrace();
//			} catch (Exception e) {
//				JOptionPane.showMessageDialog(mainFrame, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
//			}
//		}
//		revalidate();
//		repaint();
	}

	/**
	 * Here a mouse is released, telling us where an Add, Move, Resize, and Remove
	 */
	@Override
	public void mouseReleased(MouseEvent me) {
	}

	@Override
	public void mouseClicked(MouseEvent me) {
		this.requestFocus();
		int x = me.getX();
		int y = me.getY();
//		System.out.println("mouseClicked(me) x=" + x);
//		System.out.println("mouseClicked(me) y=" + y);
		this.retina.x = x;
		this.retina.y = y;
		this.retina.flag = true;
		this.mainFrame.init();
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
			case KeyEvent.VK_EQUALS: {
				this.distance += distanceFactor;
				this.mainFrame.init();
//				logger.debug("keyPressed(e) KeyEvent.VK_EQUALS");
//				try {
//					this.model.cache.pressedPageUUID = this.model.document.getPage().uuid;
//					this.model.cache.scaleOperator = '*';
//					this.model.cache.scaleFactor = 1.5;
//					this.model.pattern.execute("scalePage");
//					this.mainFrame.init();
//				} catch(NullPointerException e) {
//					e.printStackTrace();
//				}catch (Exception e) {
//					JOptionPane.showMessageDialog(mainFrame, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
//				}
				break;
			}
			case KeyEvent.VK_PLUS: {
				logger.debug("keyPressed(e) KeyEvent.VK_PLUS");
				this.distance += distanceFactor;
				this.mainFrame.init();
//				try {
//					this.model.cache.pressedPageUUID = this.model.document.getPage().uuid;
//					this.model.cache.scaleOperator = '*';
//					this.model.cache.scaleFactor = 1.5;
//					this.model.pattern.execute("scalePage");
//					this.mainFrame.init();
//				} catch(NullPointerException e) {
//					e.printStackTrace();
//				}catch (Exception e) {
//					JOptionPane.showMessageDialog(mainFrame, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
//				}
				break;
			}
			case KeyEvent.VK_MINUS: {
				logger.debug("keyPressed(e) KeyEvent.VK_MINUS");
				this.distance -= distanceFactor;
				this.mainFrame.init();
//
//				try {
//					this.model.cache.pressedPageUUID = this.model.document.getPage().uuid;
//					this.model.cache.scaleOperator = '/';
//					this.model.cache.scaleFactor = 1.5;
//					this.model.pattern.execute("scalePage");
//					this.mainFrame.init();
//				} catch(NullPointerException e) {
//					e.printStackTrace();
//				}catch (Exception e) {
//					JOptionPane.showMessageDialog(mainFrame, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
//				}
				break;
			}
			case KeyEvent.VK_DOWN: {
				logger.info("keyPressed(e) KeyEvent.VK_DOWN");
				this.distance -= distanceFactor;
				this.mainFrame.init();
//				try {
//					this.model.cache.pressedPageUUID = this.model.document.getPage().uuid;
//					this.model.cache.pressedImageUUID = this.model.document.getImage().uuid;
//					this.model.cache.shiftOperator = '+';
//					this.model.cache.shiftFactor = 10;
//					this.model.pattern.execute("shiftImage");
//					this.mainFrame.init();
//				} catch (Exception e) {
//					JOptionPane.showMessageDialog(mainFrame, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
//				}

				break;
			}
			case KeyEvent.VK_UP: {
				logger.debug("keyPressed(e) KeyEvent.VK_UP");
				this.distance += distanceFactor;
				this.mainFrame.init();
//				try {
//					this.model.cache.pressedPageUUID = this.model.document.getPage().uuid;
//					this.model.cache.pressedImageUUID = this.model.document.getImage().uuid;
//					this.model.cache.shiftOperator = '-';
//					this.model.cache.shiftFactor = 10;
//					this.model.pattern.execute("shiftImage");
//					this.mainFrame.init();
//				} catch (Exception e) {
//					JOptionPane.showMessageDialog(mainFrame, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
//				}
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
			case KeyEvent.VK_S: {
				if (this.thread != null && this.thread.isAlive()) {
					this.flag = false;
				} else {
					this.flag = true;
					this.distance = 0;
					this.thread = new Thread(this);
					this.thread.start();
				}
				break;
			}
			case KeyEvent.VK_Z: {
				logger.debug("keyPressed(e) KeyEvent.VK_Z");
				this.model.pattern.undo();
				this.mainFrame.init();
				break;
			}
			case KeyEvent.VK_Y: {
				logger.debug("keyPressed(e) KeyEvent.VK_Y");
				this.model.pattern.redo();
				this.mainFrame.init();
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

}

//Graphics2D graphics2D = (Graphics2D) graphics.create();
//if (this.distance == 0) {
//	this.distance = this.retina.minDistance;
//	this.retina.setDistance(this.distance);
//} else {
//	this.retina.setDistance(this.distance);
//}
//bufferedImage = this.retina.scaledBufferedImage;
//
//
//graphics2D.drawImage(bufferedImage, null, null);
//
////this.meritoki.document.cortex.process(graphics2D, bufferedImage, this.model.cache.concept);
//if (this.mainFrame != null)
//	this.mainFrame.controlDialog.init();
//
//graphics2D.setColor(Color.BLUE);
//double r = this.retina.getSensorRadius();
//double x = this.meritoki.document.cortex.getX();
//double y = this.meritoki.document.cortex.getY();
//double newX = x - r;// width / 2.0;
//double newY = y - r;// height / 2.0;
//Ellipse2D.Double ellipse = new Ellipse2D.Double(newX, newY, r * 2, r * 2);
//graphics2D.draw(ellipse);