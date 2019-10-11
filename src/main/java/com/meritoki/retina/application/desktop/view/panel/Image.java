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

import javax.swing.JPanel;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.meritoki.retina.application.desktop.model.Command;
import com.meritoki.retina.application.desktop.model.Model;
import com.meritoki.retina.application.desktop.model.Operation;
import com.meritoki.retina.application.desktop.model.project.Page;
import com.meritoki.retina.application.desktop.model.project.Point;
import com.meritoki.retina.application.desktop.model.project.Project;
import com.meritoki.retina.application.desktop.model.project.Shape;
import com.meritoki.retina.application.desktop.view.frame.Main;
import java.util.UUID;

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
		Dimension size = new Dimension(1028, 512);
		if (this.model != null && this.model.project != null && this.model.project.getPage() != null
				&& this.model.project.getPage().getBufferedImage() != null) {
			size.width = (int) Math.round(this.model.project.getPage().getBufferedImage().getWidth() * this.model.scale);
			size.height = (int) Math.round(this.model.project.getPage().getBufferedImage().getHeight() * this.model.scale);
		}
		return size;
	}

	/**
	 * Need to refactor this method so that it is much more simple
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D graphics2D = (Graphics2D) g.create();
		Page page = (this.model.project != null) ? this.model.project.getPage() : null;
		AffineTransform affineTransform = new AffineTransform();
		affineTransform.scale(this.model.scale, this.model.scale);
		BufferedImage bufferedImage = (page != null) ? page.getBufferedImage() : null;
		if (bufferedImage != null) {
			graphics2D.drawImage(bufferedImage, affineTransform, this);
		}
		List<Shape> shapeList = (page != null) ? page.getShapeList() : null;
		if (shapeList != null) {
			for (Shape shape : shapeList) {
				if (shape.uuid.equals(page.getShape().uuid)) {
					graphics2D.setColor(Color.RED);
				} else {
					graphics2D.setColor(Color.BLUE);
				}
				if(shape.classification == null) {
					if(this.model.rectangle) {
						shape.classification = Shape.RECTANGLE;
					} else if(this.model.ellipse) {
						shape.classification = Shape.ELLIPSE;
					}
				}
				if (shape.pointList.size() > 1) {
					shape.draw(graphics2D);
				}
			}
		}
	}

	/**
	 * A mousePressed begins an Add, Move, or Resize. Call each one a Command that
	 * affects the model. A command can have one or more Operations.
	 * @param point
	 * @return
	 */
	public Shape shapeContains(Point point) {
		Shape shape = null;
		Project project = (this.model != null) ? this.model.project : null;
		Page image = (project != null) ? this.model.project.getPage() : null;
		List<Shape> shapeList = (image != null) ? image.getShapeList() : null;
		if (shapeList != null) {
			for (Shape s : this.model.project.getPage().getShapeList()) {
				if (s.contains(point)) {
					shape = s;
					break;
				}
			}
		}
		return shape;
	}

	/**
	 * Consider moving
	 * @param point
	 * @return
	 */
	public int shapeIntersects(Point point) {
		int selection = -1;
		Page image = (this.model.project != null) ? this.model.project.getPage() : null;
		List<Shape> shapeList = (image != null) ? image.getShapeList() : null;
		if (shapeList != null) {
			for (Shape s : this.model.project.getPage().getShapeList()) {
				if (s.intersects(point)) {
					selection = s.intersect(point);
					break;
				}
			}
		}
		return selection;
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
	public void mousePressed(MouseEvent e) {
		this.model.pressedPoint = new Point();
		this.model.pressedPoint.x = e.getX();
		this.model.pressedPoint.y = e.getY();
		this.model.shape = this.shapeContains(this.model.pressedPoint);
	}

	/**
	 * Here a mouse is released, telling us where an Add, Move, Resize, and Remove
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
		this.model.releasedPoint = new Point();
		this.model.releasedPoint.x = e.getX();
		this.model.releasedPoint.y = e.getY();
		if (this.model.pressedPoint.x == this.model.releasedPoint.x && this.model.pressedPoint.y == this.model.releasedPoint.y) {
			if (this.model.shape != null) {
				logger.info("Selected...");
				this.model.project.getPage().setShape(this.model.shape.uuid);// Done
			} else {
				logger.info("***********" + this.model.pressedPoint);
				logger.info("Nothing...");
			}
		} else {
			int selection = this.shapeIntersects(this.model.pressedPoint);
			if (selection > -1) {
				if(this.model.shape != null) {
					logger.info("Resizing...");
					this.model.shape.resize(this.model.releasedPoint, selection);
					// use a copy constructor to remake shape then rezize the copy,
					// save the old one and add a reference to its new id in the
					// operation as id.
				}
			} else {
				if (this.model.shape != null) {
					logger.info("Moving...");
					this.model.project.getPage().setShape(this.model.shape.uuid);
					Point movePoint = new Point();
					movePoint.x = this.model.releasedPoint.x - this.model.pressedPoint.x;
					movePoint.y = this.model.releasedPoint.y - this.model.pressedPoint.y;
					this.model.shape.move(movePoint);
				} else {
					logger.info("Adding...");
					this.model.shape = new Shape();
					this.model.shape.addScale = this.round(this.model.scale, 6);
					this.model.shape.pointList.add(this.model.pressedPoint);
					this.model.shape.pointList.add(this.model.releasedPoint);
					this.model.shape.sortPointList();
					this.model.project.getPage().getShapeList().add(this.model.shape);
					this.model.project.getPage().setShape(this.model.shape.uuid);
					Command command = new Command();
					Operation operation = new Operation();
					operation.object = this.model.shape;
					operation.sign = 1;
					operation.id = UUID.randomUUID().toString();
					operation.uuid = this.model.shape.uuid;
					command.operationList.push(operation);
					this.model.undoStack.push(command);
				}
			}
		}
		this.main.shapeDialog.setModel(this.model);
		this.main.pageDialog.setModel(this.model);
		repaint();
	}

	public void mouseDragged(MouseEvent e) {
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
		this.model.scale += delta;
		this.model.scale = this.round(this.model.scale, 6);
		if(this.model.scale >= 0 && this.model.scale<=2) {
			logger.debug("mouseWheelMoved(...) scale = " + this.model.scale);
			for (Shape shape : this.model.project.getPage().getShapeList()) {
				shape.scale(this.model.scale);
			}
			revalidate();
			repaint();
		}
	}

	public double round(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();

		long factor = (long) Math.pow(10, places);
		value = value * factor;
		long tmp = Math.round(value);
		return (double) tmp / factor;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if ((e.getKeyCode() == KeyEvent.VK_Z) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {
			System.out.println("undo");
			if (this.model.undoStack.size() > 0) {
				Command command = this.model.undoStack.pop();
				Operation operation = null;
				for (int i = 0; i < command.operationList.size(); i++) {
					operation = command.operationList.get(i);
					if (operation.sign == 1) {
						if (operation.object instanceof Shape) {
							this.model.project.getPage().removeShape((Shape) operation.object);
						}
					} else if (operation.sign == 0) {
						if (operation.object instanceof Shape) {
							this.model.project.getPage().addShape((Shape) operation.object);
						}
					}
				}
				this.model.redoStack.push(command);
				repaint();
			}
		} else if ((e.getKeyCode() == KeyEvent.VK_Y) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {
			System.out.println("redo");
			if (this.model.redoStack.size() > 0) {
				Command command = this.model.redoStack.pop();
				Operation operation = null;
				for (int i = command.operationList.size() - 1; i >= 0; i--) {
					operation = command.operationList.get(i);
					if (operation.sign == 1) {
						if (operation.object instanceof Shape) {
							this.model.project.getPage().addShape((Shape) operation.object);
						}
					} else if (operation.sign == 0) {
						if (operation.object instanceof Shape) {
							this.model.project.getPage().removeShape((Shape) operation.object);
						}
					}
				}
				this.model.undoStack.push(command);
				repaint();
			}
		} else {
			int keyCode = e.getKeyCode();
			int index = this.model.project.getIndex();
			switch (keyCode) {
			case KeyEvent.VK_BACK_SPACE: {
				logger.debug("KeyPressed(BACK_SPACE)");
				this.model.shape = this.model.project.getPage().getShape();
				this.model.project.getPage().removeShape(this.model.shape);
				Command command = new Command();
				Operation operation = new Operation();
				operation.object = this.model.shape;
				operation.sign = 0;
				operation.id = UUID.randomUUID().toString();
				operation.uuid = this.model.shape.uuid;
				command.operationList.push(operation);
				this.model.undoStack.push(command);
				this.repaint();
				break;
			}
			case KeyEvent.VK_LEFT: {
				logger.debug("keyPressed(LEFT)");
				this.model.project.setIndex(--index);
				this.main.imageDialog.init();
				this.main.shapeDialog.init();
				this.main.pageDialog.init();
				this.repaint();
				break;
			}
			case KeyEvent.VK_RIGHT: {
				logger.debug("keyPressed(RIGHT)");
				this.model.project.setIndex(++index);
				this.main.imageDialog.init();
				this.main.shapeDialog.init();
				this.main.pageDialog.init();
				this.repaint();
				break;
			}
			case KeyEvent.VK_UP: {
				logger.debug("keyPressed(UP)");
				this.model.project.setIndex(--index);
				this.main.imageDialog.init();
				this.main.shapeDialog.init();
				this.main.pageDialog.init();
				this.repaint();
				break;
			}
			case KeyEvent.VK_DOWN: {
				logger.debug("keyPressed(DOWN)");
				this.model.project.setIndex(++index);
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
