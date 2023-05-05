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
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
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
import com.meritoki.app.desktop.retina.model.provider.meritoki.Meritoki;
import com.meritoki.app.desktop.retina.view.frame.MainFrame;
import com.meritoki.library.cortex.model.Point;

public class MachinePanel extends JPanel implements MouseListener, MouseWheelListener, KeyListener, Runnable, HierarchyListener, ComponentListener {

	private static final long serialVersionUID = 3989576625299550361L;
	private static Logger logger = LogManager.getLogger(MachinePanel.class.getName());
	private MainFrame mainFrame = null;
	private Model model = null;
	private Meritoki meritoki;
	private Thread thread;
	private double zoomFactor = 1;

	public MachinePanel() {
		super();
		this.setBackground(Color.black);
		this.addMouseListener(this);
		this.addKeyListener(this);
		this.addComponentListener(this);
		this.addMouseWheelListener(this);
//		this.addComponentListener(new ComponentAdapter() {
//		      @Override
//		      public void componentResized(ComponentEvent e) {
//		        System.out.println("Resized to " + e.getComponent().getSize());
//		        Dimension dimension = new Dimension(this.getWidth(), this.getHeight());
//				this.meritoki.setDimension(dimension);
//		      }
//		      @Override
//		      public void componentMoved(ComponentEvent e) {
//		        System.out.println("Moved to " + e.getComponent().getLocation());
//		      }
//		    });
	}

	public void setMainFrame(MainFrame main) {
		this.mainFrame = main;
	}

	public void setModel(Model model) {
		this.model = model;
		this.meritoki = (Meritoki) this.model.system.providerMap.get("meritoki");
		Dimension dimension = new Dimension(this.getWidth(), this.getHeight());
		if(this.meritoki != null) {
			this.meritoki.setDimension(dimension);
		}
		this.init();
	}
	


	public void init() {
		logger.debug("init()");
		this.repaint();
		this.revalidate();
//		this.setPreferredSize(this.getPreferredSize());
		if(this.meritoki != null) {
			this.meritoki.update();
			
		}
//		if(this.mainFrame != null)
//			this.mainFrame.controlDialog.init();
	}
//
//	@Override
//	public Dimension getPreferredSize() {
//		Dimension dimension = new Dimension(this.getWidth(), this.getHeight());
//		//Need to know the size of the Panel, for sure;
//		if(this.meritoki != null) {
//			this.meritoki.setDimension(dimension);
//		}
////		dimension = (meritoki != null) ? this.meritoki.getDimension() : dimension;
//		
//		return dimension;
//	}

	private boolean visible() {
		Container c = getParent();
		while (c != null)
			if (!c.isVisible())
				return false;
			else
				c = c.getParent();
		return true;
	}

	public void start() {
		this.meritoki.start();
		this.thread = new Thread(this);
		this.thread.start();
	}

	public void stop() {
		this.meritoki.stop();
		if (this.thread != null && this.thread.isAlive()) {
			this.thread.interrupt();
		}
	}

	@Override
	public void run() {
		System.out.println("run()");
		while (this.meritoki.loop) {
//			if(this.mainFrame != null)
//				this.mainFrame.init();
			this.init();
			try {
				Thread.sleep(100);
			} catch (Exception e) {
			}
		}
	}

	@Override
	public void paint(Graphics graphics) {
//        System.out.println("paint("+String.valueOf(graphics != null)+")");
		super.paint(graphics);
		this.meritoki.paint(graphics);
		if(this.mainFrame != null)
			this.mainFrame.meritokiControlDialog.init();
	}

	public void hierarchyChanged(HierarchyEvent e) {
		boolean visible = visible();
		if (visible) {
			this.requestFocus();
		}
		if (this.meritoki != null) {
			this.meritoki.setVisible(visible);
			if (this.meritoki.loop && visible) {
				this.start();
			} else {
				this.stop();
			}
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
		this.meritoki.input(new Point(x, y));
		//Found the bug, clicking with mouse used to lead to immediate processing
		//Now it is dependant on a screen click
		//ControlDialog works, it is just not getting the info from a future execution.
		//To solve, need to call paint();
//		this.meritoki.paint(null);
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
				this.meritoki.zoom(zoomFactor);
				this.mainFrame.init();
				break;
			}
			case KeyEvent.VK_PLUS: {
				logger.debug("keyPressed(e) KeyEvent.VK_PLUS");
				this.meritoki.zoom(zoomFactor);
				this.mainFrame.init();
				break;
			}
			case KeyEvent.VK_MINUS: {
				logger.debug("keyPressed(e) KeyEvent.VK_MINUS");
				this.meritoki.zoom(-zoomFactor);
				this.mainFrame.init();
				break;
			}
			case KeyEvent.VK_DOWN: {
				logger.info("keyPressed(e) KeyEvent.VK_DOWN");
				this.meritoki.zoom(-zoomFactor);
				this.mainFrame.init();
				break;
			}
			case KeyEvent.VK_UP: {
				logger.debug("keyPressed(e) KeyEvent.VK_UP");
				this.meritoki.zoom(zoomFactor);
				this.mainFrame.init();
				break;
			}
			case KeyEvent.VK_LEFT: {
				logger.info("keyPressed(e) KeyEvent.VK_LEFT");
				this.meritoki.zoom(-zoomFactor);
				this.mainFrame.init();
				break;
			}
			case KeyEvent.VK_RIGHT: {
				logger.info("keyPressed(e) KeyEvent.VK_RIGHT");
				this.meritoki.zoom(zoomFactor);
				this.mainFrame.init();
				break;
			}
//			case KeyEvent.VK_Z: {
//				logger.debug("keyPressed(e) KeyEvent.VK_Z");
//				this.model.pattern.undo();
//				this.mainFrame.init();
//				break;
//			}
//			case KeyEvent.VK_Y: {
//				logger.debug("keyPressed(e) KeyEvent.VK_Y");
//				this.model.pattern.redo();
//				this.mainFrame.init();
//				break;
//			}
			}
		} else {
			int keyCode = ke.getKeyCode();
			switch (keyCode) {
			case KeyEvent.VK_LEFT: {
				logger.debug("keyPressed(e) KeyEvent.VK_LEFT");
				break;
			}
			case KeyEvent.VK_RIGHT: {
				logger.debug("keyPressed(e) KeyEvent.VK_RIGHT");
				break;
			}
			case KeyEvent.VK_UP: {
				logger.debug("keyPressed(e) KeyEvent.VK_UP");
				break;
			}
			case KeyEvent.VK_DOWN: {
				logger.debug("keyPressed(e) KeyEvent.VK_DOWN");
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

	public void removeNotify() {
		removeHierarchyListener(this);
		super.removeNotify();
	}

	public void addNotify() {
		super.addNotify();
		addHierarchyListener(this);
	}

	@Override
	public void componentHidden(ComponentEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentMoved(ComponentEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentResized(ComponentEvent arg0) {
		Dimension dimension = new Dimension(this.getWidth(), this.getHeight());
		if(this.meritoki != null) {
			this.meritoki.setDimension(dimension);
		}
		this.repaint();
	}

	@Override
	public void componentShown(ComponentEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		e.consume();
		if (e.isControlDown())
        {
            if (e.getWheelRotation() < 0)
            {
                System.out.println("mouse wheel Up");
                this.meritoki.zoom(zoomFactor);
				this.mainFrame.init();
            }
            else
            {
                System.out.println("mouse wheel Down");
                this.meritoki.zoom(-zoomFactor);
				this.mainFrame.init();
            }
        }
        else
        {
            getParent().dispatchEvent(e);
        }
		
	}
}

//Graphics2D graphics2D = (Graphics2D) graphics.create();
//if (this.distance == 0) {
//	this.distance = this.meritoki.retina.minDistance;
//	this.meritoki.retina.setDistance(this.distance);
//} else {
//	this.meritoki.retina.setDistance(this.distance);
//}
//bufferedImage = this.meritoki.retina.scaledBufferedImage;
//
//
//graphics2D.drawImage(bufferedImage, null, null);
//
////this.meritoki.document.cortex.process(graphics2D, bufferedImage, this.model.cache.concept);
//if (this.mainFrame != null)
//	this.mainFrame.controlDialog.init();
//
//graphics2D.setColor(Color.BLUE);
//double r = this.meritoki.retina.getSensorRadius();
//double x = this.meritoki.document.cortex.getX();
//double y = this.meritoki.document.cortex.getY();
//double newX = x - r;// width / 2.0;
//double newY = y - r;// height / 2.0;
//Ellipse2D.Double ellipse = new Ellipse2D.Double(newX, newY, r * 2, r * 2);
//graphics2D.draw(ellipse);

//if (this.meritoki != null && this.meritoki.retina != null) {
//dimension.setSize(this.meritoki.retina.getObjectWidth(), this.meritoki.retina.getObjectHeight());
//}

//if(this.meritoki.retina.manual) {
//this.meritoki.retina.setOrigin(x, y);
//this.mainFrame.init();
//}