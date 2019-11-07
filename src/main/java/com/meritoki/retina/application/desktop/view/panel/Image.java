/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meritoki.retina.application.desktop.view.panel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.meritoki.retina.application.desktop.model.Document;
import com.meritoki.retina.application.desktop.model.Model;
import com.meritoki.retina.application.desktop.model.document.File;
import com.meritoki.retina.application.desktop.model.document.Page;
import com.meritoki.retina.application.desktop.model.document.Point;
import com.meritoki.retina.application.desktop.model.document.Project;
import com.meritoki.retina.application.desktop.model.document.Shape;
import com.meritoki.retina.application.desktop.view.frame.Main;

/**
 * Image extends JPanel and contains many of the functions and features for
 * preparing subset images to upload to a provider such as Zooniverse.
 * 
 * @author jorodriguez
 *
 */
public class Image extends JPanel implements MouseListener, MouseWheelListener, KeyListener {

	private static final long serialVersionUID = 3989576625299550361L;
	private static Logger logger = LogManager.getLogger(Image.class.getName());
	private Main main = null;
	private Model model = null;

	public Image() {
		super();
		this.setBackground(Color.black);
		this.addMouseListener(this);
		this.addMouseWheelListener(this);
		this.addKeyListener(this);
	}

	public void setMain(Main main) {
		this.main = main;
	}

	public void setModel(Model model) {
		logger.debug("setModel(" + model + ")");
		this.model = model;
		this.setPreferredSize(this.getPreferredSize());
	}

	@Override
	public Dimension getPreferredSize() {
		Dimension dimension = new Dimension(1028, 512);
		Document document = (this.model != null) ? this.model.getDocument() : null;
		Project project = (document != null) ? document.getProject() : null;
		Page page = (project != null) ? project.getPage() : null;
		BufferedImage bufferedImage = (page != null) ? page.getBufferedImage() : null;
		if (bufferedImage != null) {
			dimension.setSize(bufferedImage.getWidth(), bufferedImage.getHeight());
		}
		return dimension;
	}

	/**
	 * Need to refactor this method so that it is much more simple
	 */
	@Override
	protected void paintComponent(Graphics graphics) {
		logger.trace("paintComponent(" + graphics + ")");
		super.paintComponent(graphics);
		if (this.model != null) {
			Graphics2D graphics2D = (Graphics2D) graphics.create();
			Document document = (this.model != null) ? this.model.getDocument() : null;
			Project project = (document != null) ? document.getProject() : null;
			project.getPage().setScale(this.model.variable.scale);
			AffineTransform affineTransform = new AffineTransform();
			affineTransform.scale(this.model.variable.scale, this.model.variable.scale);
			BufferedImage bufferedImage = project.getPage().getBufferedImage();
			if (bufferedImage != null) {
				graphics2D.drawImage(bufferedImage, affineTransform, null);

			}
			// TODO Attempt to build buffered image here instead of above, build it with
			// each
			// File bufferedImage.
			List<File> fileList = project.getPage().getFileList();
			File file = project.getPage().getFile();
			com.meritoki.retina.application.desktop.model.document.Dimension d = null;
			if (fileList != null) {
				for (File f : fileList) {
					d = f.getDimension();
					if (file != null && f.uuid.equals(file.uuid)) {
						graphics2D.setColor(Color.RED);
					} else {
						graphics2D.setColor(Color.YELLOW);
					}
					Rectangle2D.Double rectangle = new Rectangle2D.Double(d.x, d.y, d.w, d.h);
					graphics2D.draw(rectangle);
				}

			}
			List<Shape> shapeList = project.getPage().getShapeList();
			Shape shape = project.getPage().getShape();
			if (shapeList != null) {
				for (Shape s : shapeList) {
					d = s.getDimension();
					if (shape != null && s.uuid.equals(shape.uuid)) {
						graphics2D.setColor(Color.RED);
					} else {
						graphics2D.setColor(Color.BLUE);
					}
					if (s.classification.equals(Shape.ELLIPSE)) {
						Ellipse2D.Double ellipse = new Ellipse2D.Double(d.x, d.y, d.w, d.h);
						graphics2D.draw(ellipse);
					} else if (s.classification.equals(Shape.RECTANGLE)) {
						Rectangle2D.Double rectangle = new Rectangle2D.Double(d.x, d.y, d.w, d.h);
						graphics2D.draw(rectangle);
					}
				}
			}
		}
	}

	/**
	 * Consider moving
	 * 
	 * @param point
	 * @return
	 */

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
	public void mousePressed(MouseEvent e) {
		e.consume();
		this.model.variable.pressedPoint = new Point();
		this.model.variable.pressedPoint.x = e.getX();
		this.model.variable.pressedPoint.y = e.getY();
		logger.trace("mousePressed(e) this.model.variable.pressedPoint=" + this.model.variable.pressedPoint);
		this.model.variable.pressedFile = this.model.getDocument().getProject().getPage().getFile(new Point(this.model.variable.pressedPoint));
		if (this.model.variable.pressedFile != null) {
			this.model.getDocument().getProject().getPage().setFile(this.model.variable.pressedFile.uuid);
			this.model.variable.pressedShape = this.model.getDocument().getProject().getPage().getShape(new Point(this.model.variable.pressedPoint));
		}
	}

	/**
	 * Here a mouse is released, telling us where an Add, Move, Resize, and Remove
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
		e.consume();
		this.model.variable.releasedPoint = new Point(e.getX(),e.getY());
		if (this.model.variable.pressedPoint.equals(this.model.variable.releasedPoint)) {
			this.model.getDocument().execute("setShape");
		} else {
			this.model.variable.selection = this.model.getDocument().getProject().getPage().intersectShape(this.model.variable.pressedPoint);
			if (this.model.variable.selection > -1) {
				this.model.getDocument().execute("resizeShape");
			} else {
				if (this.model.variable.pressedShape != null) {
					this.model.variable.releasedFile = this.model.getDocument().getProject().getPage().getFile(this.model.variable.releasedPoint);
					this.model.getDocument().execute("moveShape");
				} else {
					this.model.getDocument().execute("addShape");
				}
			}
		}
		this.main.shapeDialog.setModel(this.model);
		this.main.pageDialog.setModel(this.model);
		repaint();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		double delta = 0.05f * e.getPreciseWheelRotation();
		this.model.variable.scale += delta;
		if (this.model.variable.scale >= 0 && this.model.variable.scale <= 2) {
			logger.trace("mouseWheelMoved(...) scale = " + this.model.variable.scale);
			this.model.getDocument().getProject().getPage().setScale(this.model.variable.scale);
			revalidate();
			repaint();
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if ((e.getKeyCode() == KeyEvent.VK_DOWN) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {
			e.consume();
			Page page = this.model.getDocument().getProject().getPage();
			page.setBufferedImage(null);
			File file = (page != null) ? page.getFile() : null;
			if (file != null) {
				file.setMargin(file.margin + 10);
				page.setBufferedImage(null);
			}
			repaint();
		} else if ((e.getKeyCode() == KeyEvent.VK_UP) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {
			e.consume();
			Page page = this.model.getDocument().getProject().getPage();
			page.setBufferedImage(null);
			File file = (page != null) ? page.getFile() : null;
			if (file != null) {
				file.setMargin(file.margin - 10);
				page.setBufferedImage(null);
			}
			repaint();
		} else if ((e.getKeyCode() == KeyEvent.VK_Z) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {
			e.consume();
			this.model.getDocument().undo();
			repaint();
		} else if ((e.getKeyCode() == KeyEvent.VK_Y) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {
			e.consume();
			this.model.getDocument().redo();
			repaint();
		} else {
			e.consume();
			int keyCode = e.getKeyCode();
			int index = this.model.getDocument().getProject().getIndex();
			switch (keyCode) {
			case KeyEvent.VK_BACK_SPACE: {
				logger.debug("KeyPressed(BACK_SPACE)");
				this.model.variable.pressedShape = this.model.getDocument().getProject().getPage().getShape();
				this.model.getDocument().execute("removeShape");
				this.repaint();
				break;
			}
			case KeyEvent.VK_LEFT: {
				logger.debug("keyPressed(LEFT)");
				this.model.getDocument().getProject().setIndex(--index);
				this.main.imageDialog.init();
				this.main.shapeDialog.init();
				this.main.pageDialog.init();
				this.repaint();
				break;
			}
			case KeyEvent.VK_RIGHT: {
				logger.debug("keyPressed(RIGHT)");
				this.model.getDocument().getProject().setIndex(++index);
				this.main.imageDialog.init();
				this.main.shapeDialog.init();
				this.main.pageDialog.init();
				this.repaint();
				break;
			}
			case KeyEvent.VK_UP: {
				logger.debug("keyPressed(UP)");
				this.model.getDocument().getProject().setIndex(--index);
				this.main.imageDialog.init();
				this.main.shapeDialog.init();
				this.main.pageDialog.init();
				this.repaint();
				break;
			}
			case KeyEvent.VK_DOWN: {
				logger.debug("keyPressed(DOWN)");
				this.model.getDocument().getProject().setIndex(++index);
				this.main.imageDialog.init();
				this.main.shapeDialog.init();
				this.main.pageDialog.init();
				this.repaint();
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
//public double round(double value, int places) {
//if (places < 0)
//	throw new IllegalArgumentException();
//long factor = (long) Math.pow(10, places);
//value = value * factor;
//long tmp = Math.round(value);
//return (double) tmp / factor;
//}
