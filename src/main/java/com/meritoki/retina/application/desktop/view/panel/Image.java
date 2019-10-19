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

import com.meritoki.retina.application.desktop.model.Model;
import com.meritoki.retina.application.desktop.model.command.Command;
import com.meritoki.retina.application.desktop.model.command.Operation;
import com.meritoki.retina.application.desktop.model.project.File;
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
			size.width = (int) Math
					.round(this.model.project.getPage().getBufferedImage().getWidth() * this.model.scale);
			size.height = (int) Math
					.round(this.model.project.getPage().getBufferedImage().getHeight() * this.model.scale);
		}
		return size;
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
			Project project = this.model.project;
			project.setScale(this.model.scale);
			
			
			AffineTransform affineTransform = new AffineTransform();
			affineTransform.scale(this.model.scale, this.model.scale);
			BufferedImage bufferedImage = project.getBufferedImage();
			if (bufferedImage != null) {
				graphics2D.drawImage(bufferedImage, affineTransform, null);

			}
			//TODO Attempt to build buffered image here instead of above, build it with each
			//File bufferedImage.
			List<File> fileList = project.getFileList();
			File file = project.getFile();
			com.meritoki.retina.application.desktop.model.project.Dimension d = null;
			if (fileList != null) {
				for (File f : fileList) {
					d = f.dimension;
					if (file != null && f.uuid.equals(file.uuid)) {
						graphics2D.setColor(Color.RED);
					} else {
						graphics2D.setColor(Color.YELLOW);
					}
					Rectangle2D.Double rectangle = new Rectangle2D.Double(d.x,d.y,d.w,d.h);
					graphics2D.draw(rectangle);
				}
				
			}
			List<Shape> shapeList = project.getShapeList();
			Shape shape = project.getShape();
			if (shapeList != null) {
				for (Shape s : shapeList) {
					if (!s.removed) {
						d = s.dimension;
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
		this.model.pressedPoint = new Point();
		this.model.pressedPoint.x = e.getX();
		this.model.pressedPoint.y = e.getY();
		logger.info("mousePressed(e) this.model.pressedPoint="+this.model.pressedPoint);
		Point pressedPoint = new Point(this.model.pressedPoint);
		this.model.file = this.model.project.getFile(pressedPoint);
		if(model.file != null) {
			this.model.project.setFile(this.model.file.uuid);
			pressedPoint = new Point(this.model.pressedPoint);
			this.model.shape = this.model.project.getShape(pressedPoint);// returns new Shape if shape not found.
			if(this.model.shape != null) {
				this.model.shape.setScale(this.model.scale);
			}
		}
	}

	/**
	 * Here a mouse is released, telling us where an Add, Move, Resize, and Remove
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
		e.consume();
		this.model.releasedPoint = new Point();
		this.model.releasedPoint.x = e.getX();
		this.model.releasedPoint.y = e.getY();
		logger.info("mouseReleased(e) this.model.releasedPoint="+this.model.releasedPoint);
		Point releasedPoint = new Point(this.model.releasedPoint);
		File releasedFile = this.model.project.getFile(releasedPoint);
		if (this.model.pressedPoint.x == this.model.releasedPoint.x
				&& this.model.pressedPoint.y == this.model.releasedPoint.y) {
			if (this.model.shape != null) {
				logger.info("Selected...");
				this.model.project.setShape(this.model.shape.uuid);
			} else {
				logger.info("Nothing...");
			}
		} else {


			int selection = -1;// this.model.project.intersectShape(pressedPoint);
			if (selection > -1) {
				if (this.model.shape != null) {
					logger.info("Resizing...");
					releasedPoint = new Point(this.model.releasedPoint);
					this.model.shape.resize(releasedPoint, selection);
				}
			} else {
				
				if (this.model.shape != null) {
					logger.info("Moving...");
					if(!this.model.file.uuid.equals(releasedFile.uuid)) {
						this.model.project.setFile(releasedFile.uuid);
						Shape shape = new Shape(this.model.shape);
						shape = this.model.file.removeShape(shape);
						releasedFile.addShape(shape);
					}
					this.model.project.setShape(this.model.shape.uuid);
					Point movePoint = new Point();
					movePoint.x = this.model.releasedPoint.x - this.model.pressedPoint.x;
					movePoint.y = this.model.releasedPoint.y - this.model.pressedPoint.y;
					this.model.shape.move(movePoint);
				} else {
					logger.info("Adding...");
					this.model.shape = new Shape();
					if (this.model.shape.classification == null) {
						if (this.model.rectangle) {
							this.model.shape.classification = Shape.RECTANGLE;
						} else if (this.model.ellipse) {
							this.model.shape.classification = Shape.ELLIPSE;
						}
					}
					this.model.shape.addScale = this.round(this.model.scale, 6);
					this.model.shape.setScale(this.model.scale);//Fix includes setting scale of shape immediately
					//Discovered functions were changing pressedPoint from original value.
					this.model.shape.pointList.add(new Point(this.model.pressedPoint));
					this.model.shape.pointList.add(new Point(this.model.releasedPoint));
					logger.info("mouseReleased(e) this.model.shape.pointList="+this.model.shape.pointList);
//					this.model.shape.sortPointList();//breaks intersect function
					this.model.project.addShape(this.model.shape);
					this.model.project.setShape(this.model.shape.uuid);
//						Command command = new Command();
//						Operation operation = new Operation();
//						operation.object = this.model.shape;
//						operation.sign = 1;
//						operation.id = UUID.randomUUID().toString();
//						operation.uuid = this.model.shape.uuid;
//						command.operationList.push(operation);
//						this.model.undoStack.push(command);
					
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
		if (this.model.scale >= 0 && this.model.scale <= 2) {
			logger.debug("mouseWheelMoved(...) scale = " + this.model.scale);
			this.model.project.setScale(this.model.scale);
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
		if ((e.getKeyCode() == KeyEvent.VK_DOWN) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {
			e.consume();
			Page page = this.model.project.getPage();
			page.bufferedImage = null;
			File file = (page != null) ? page.getFile() : null;
			if (file != null) {
				logger.info("keyPressed(...) UP CTRL file.margin="+file.margin);
				file.setMargin(file.margin+10);
//				page.resetBufferedImage();
			}
			repaint();
		} 
		
		else if ((e.getKeyCode() == KeyEvent.VK_UP) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {
			e.consume();
			Page page = this.model.project.getPage();
			page.bufferedImage = null;
			File file = (page != null) ? page.getFile() : null;
			if (file != null) {
				file.setMargin(file.margin-10);
//				page.resetBufferedImage();
			}
			repaint();
		} else if ((e.getKeyCode() == KeyEvent.VK_Z) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {
			e.consume();
			System.out.println("undo");
			if (this.model.undoStack.size() > 0) {
				Command command = this.model.undoStack.pop();
				Operation operation = null;
				for (int i = 0; i < command.operationList.size(); i++) {
					operation = command.operationList.get(i);
					if (operation.sign == 1) {
						if (operation.object instanceof Shape) {
							this.model.project.getPage().getFile().removeShape((Shape) operation.object);
						}
					} else if (operation.sign == 0) {
						if (operation.object instanceof Shape) {
							this.model.project.getPage().getFile().addShape((Shape) operation.object);
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
							this.model.project.getPage().getFile().addShape((Shape) operation.object);
						}
					} else if (operation.sign == 0) {
						if (operation.object instanceof Shape) {
							this.model.project.getPage().getFile().removeShape((Shape) operation.object);
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
				this.model.shape = this.model.project.getPage().getFile().getShape();
				this.model.project.getPage().getFile().removeShape(this.model.shape);
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
