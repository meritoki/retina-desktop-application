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

import com.meritoki.retina.application.desktop.model.project.Page;
import com.meritoki.retina.application.desktop.model.project.Point;
import com.meritoki.retina.application.desktop.model.project.Project;
import com.meritoki.retina.application.desktop.model.project.Shape;
import com.meritoki.retina.application.desktop.model.system.Command;
import com.meritoki.retina.application.desktop.model.system.Operation;
import com.meritoki.retina.application.desktop.view.frame.Main;
import java.util.UUID;

public class Image extends JPanel implements MouseListener, MouseWheelListener, KeyListener {

	static Logger logger = LogManager.getLogger(Image.class.getName());
	
	private static final long serialVersionUID = 3989576625299550361L;
	private Project project = null;
	private Point pressedPoint = new Point();
	private Point releasedPoint = new Point();
	private Shape shape = null;
	private Main main = null;
	private float scale = 1;

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

	public void setModel(Project project) {
		logger.debug("setProject(" + project + ")");
		this.project = project;
		this.setPreferredSize(this.getPreferredSize());
	}

	@Override
	public Dimension getPreferredSize() {
		Dimension size = new Dimension(1028, 512);
		if (this.project != null && this.project.getPage() != null
				&& this.project.getPage().getBufferedImage() != null) {
			size.width = Math.round(this.project.getPage().getBufferedImage().getWidth() * scale);
			size.height = Math.round(this.project.getPage().getBufferedImage().getHeight() * scale);
		}
		return size;
	}

	// Need to refactor this method so that it is much more simple
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D graphics2D = (Graphics2D) g.create();
		Page page = (this.project != null) ? this.project.getPage() : null;
		BufferedImage bufferedImage = (page != null) ? page.getBufferedImage() : null;
		AffineTransform affineTransform = new AffineTransform();
		affineTransform.scale(this.scale, this.scale);
		if (bufferedImage != null) {
			graphics2D.drawImage(bufferedImage, affineTransform, this);
			graphics2D.dispose();
		}
		List<Shape> shapeList = (page != null) ? page.getShapeList() : null;
		if (shapeList != null) {
			for (Shape s : shapeList) {
				if (!s.removed) {
					if (page.getShape() != null && s.uuid.equals(page.getShape().uuid)) {
						g.setColor(Color.RED);
					} else {
						g.setColor(Color.BLUE);
					}
					if (s.startPoint != null && s.stopPoint != null) {
						int x = s.startPoint.x;
						int y = s.startPoint.y;
						int i = s.stopPoint.x;
						int j = s.stopPoint.y;
						int px = Math.min(x, i);
						int py = Math.min(y, j);
						int pw = Math.abs(x - i);
						int ph = Math.abs(y - j);
						px *= s.scale;
						py *= s.scale;
						pw *= s.scale;
						ph *= s.scale;
						g.drawRect(px, py, pw, ph);
					}
				}
			}
		}
	}

	/*
	 * A mousePressed begins an Add, Move, or Resize. Call each one a Command that
	 * affects the model. A command can have one or more Operations.
	 * 
	 */

	public Shape shapeContains(Point point) {
		Shape shape = null;
		Page image = (this.project != null) ? this.project.getPage() : null;
		List<Shape> shapeList = (image != null) ? image.getShapeList() : null;
		if (shapeList != null) {
			for (Shape s : this.project.getPage().getShapeList()) {
				if (s.contains(point)) {
					shape = s;
					break;
				}
			}
		}
		return shape;
	}

	public int shapeIntersects(Point point) {
		int selection = -1;
		Page image = (this.project != null) ? this.project.getPage() : null;
		List<Shape> shapeList = (image != null) ? image.getShapeList() : null;
		if (shapeList != null) {
			for (Shape s : this.project.getPage().getShapeList()) {
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
		this.pressedPoint = new Point();
		this.pressedPoint.x = e.getX();
		this.pressedPoint.y = e.getY();
		this.shape = this.shapeContains(this.pressedPoint);
	}

	/**
	 * Here a mouse is released, telling us where an Add, Move, Resize, and Remove
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
		this.releasedPoint = new Point();
		this.releasedPoint.x = e.getX();
		this.releasedPoint.y = e.getY();
		if (this.pressedPoint.x == this.releasedPoint.x && this.pressedPoint.y == this.releasedPoint.y) {
			if (this.shape != null) {
				logger.info("Selected...");
				this.project.getPage().setShape(this.shape.uuid);// Done
			} else {
				logger.info("***********"+this.pressedPoint);
				logger.info("Nothing...");
			}
		} else {
			int selection = this.shapeIntersects(this.pressedPoint);
			if (selection > -1) {
				logger.info("Resizing...");
				this.shape.resize(this.releasedPoint, selection);
				// use a copy constructor to remake shape then rezize the copy,
				// save the old one and add a reference to its new id in the
				// operation as id.
			} else {
				if (this.shape != null) {
					logger.info("Moving...");
					Point movePoint = new Point();
					movePoint.x = this.releasedPoint.x - this.pressedPoint.x;
					movePoint.y = this.releasedPoint.y - this.pressedPoint.y;
					this.shape.move(movePoint);
				} else {
					logger.info("Adding...");
					this.shape = new Shape();
					this.shape.startPoint = this.pressedPoint;
					this.shape.stopPoint = this.releasedPoint;
					this.project.getPage().getShapeList().add(this.shape);
					this.project.getPage().setShape(this.shape.uuid);
					Command command = new Command();
					Operation operation = new Operation();
					operation.object = this.shape;
					operation.sign = 1;
					operation.id = UUID.randomUUID().toString();
					operation.uuid = this.shape.uuid;
					command.operationList.push(operation);
					this.main.undoStack.push(command);
				}
			}
		}
		this.main.shapeDialog.setModel(this.project);
		this.main.pageDialog.setModel(this.project);
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
		scale += delta;
		logger.info("mouseWheelMoved(...) scale = "+scale);
		for(Shape shape:this.project.getPage().getShapeList()) {
			shape.scale = scale;
		}
		revalidate();
		repaint();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if ((e.getKeyCode() == KeyEvent.VK_Z) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {
			System.out.println("undo");
			if (this.main.undoStack.size() > 0) {
				Command command = this.main.undoStack.pop();
				Operation operation = null;
				for (int i = 0; i < command.operationList.size(); i++) {
					operation = command.operationList.get(i);
					if (operation.sign == 1) {
						if (operation.object instanceof Shape) {
							this.project.getPage().removeShape((Shape) operation.object);
						}
					} else if (operation.sign == 0) {
						if (operation.object instanceof Shape) {
							this.project.getPage().addShape((Shape) operation.object);
						}
					}
				}
				this.main.redoStack.push(command);
				repaint();
			}
		} else if ((e.getKeyCode() == KeyEvent.VK_Y) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {
			System.out.println("redo");
			if (this.main.redoStack.size() > 0) {
				Command command = this.main.redoStack.pop();
				Operation operation = null;
				for (int i = command.operationList.size() - 1; i >= 0; i--) {
					operation = command.operationList.get(i);
					if (operation.sign == 1) {
						if (operation.object instanceof Shape) {
							this.project.getPage().addShape((Shape) operation.object);
						}
					} else if (operation.sign == 0) {
						if (operation.object instanceof Shape) {
							this.project.getPage().removeShape((Shape) operation.object);
						}
					}
				}
				this.main.undoStack.push(command);
				repaint();
			}
		} else {
			int keyCode = e.getKeyCode();
			int index = this.project.getIndex();
			switch (keyCode) {
			case KeyEvent.VK_BACK_SPACE: {
				logger.debug("KeyPressed(BACK_SPACE)");
				this.shape = this.project.getPage().getShape();
				this.project.getPage().removeShape(this.shape);
				Command command = new Command();
				Operation operation = new Operation();
				operation.object = this.shape;
				operation.sign = 0;
				operation.id = UUID.randomUUID().toString();
				operation.uuid = this.shape.uuid;
				command.operationList.push(operation);
				this.main.undoStack.push(command);
				this.repaint();
				break;
			}
			case KeyEvent.VK_LEFT: {
				logger.debug("keyPressed(LEFT)");
				this.project.setIndex(--index);
				this.main.imageDialog.init();
				this.main.shapeDialog.init();
				this.main.pageDialog.init();
				this.repaint();
				break;
			}
			case KeyEvent.VK_RIGHT: {
				logger.debug("keyPressed(RIGHT)");
				this.project.setIndex(++index);
				this.main.imageDialog.init();
				this.main.shapeDialog.init();
				this.main.pageDialog.init();
				this.repaint();
				break;
			}
			case KeyEvent.VK_UP: {
				logger.debug("keyPressed(UP)");
				this.project.setIndex(--index);
				this.main.imageDialog.init();
				this.main.shapeDialog.init();
				this.main.pageDialog.init();
				this.repaint();
				break;
			}
			case KeyEvent.VK_DOWN: {
				logger.debug("keyPressed(DOWN)");
				this.project.setIndex(++index);
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
