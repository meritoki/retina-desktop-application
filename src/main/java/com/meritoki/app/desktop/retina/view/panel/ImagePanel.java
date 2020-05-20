/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.JPanel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.meritoki.app.desktop.retina.controller.node.NodeController;
import com.meritoki.app.desktop.retina.model.Model;
import com.meritoki.app.desktop.retina.model.document.Document;
import com.meritoki.app.desktop.retina.model.document.Image;
import com.meritoki.app.desktop.retina.model.document.Page;
import com.meritoki.app.desktop.retina.model.document.Point;
import com.meritoki.app.desktop.retina.model.document.Shape;
import com.meritoki.app.desktop.retina.view.frame.MainFrame;

/**
 * Image extends JPanel and contains many of the functions and features for
 * preparing subset images to upload to a provider such as Zooniverse.
 * 
 * @author jorodriguez
 *
 */
public class ImagePanel extends JPanel implements MouseListener, MouseWheelListener, KeyListener {

	private static final long serialVersionUID = 3989576625299550361L;
	private static Logger logger = LogManager.getLogger(ImagePanel.class.getName());
	private MainFrame main = null;
	private Model model = null;

	public ImagePanel() {
		super();
		this.setBackground(Color.black);
		this.addMouseListener(this);
		this.addMouseWheelListener(this);
		this.addKeyListener(this);
	}

	public void setMain(MainFrame main) {
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

		Page page = (document != null) ? document.getPage() : null;
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
	public void paint(Graphics graphics) {
		super.paint(graphics);
		if (this.model != null) {
			Graphics2D graphics2D = (Graphics2D) graphics.create();
			
			Document document = (this.model != null) ? this.model.getDocument() : null;
			document.getPage().setScale(this.model.document.cache.scale);
			AffineTransform affineTransform = new AffineTransform();
			affineTransform.scale(this.model.document.cache.scale, this.model.document.cache.scale);
			BufferedImage bufferedImage = document.getPage().getBufferedImage();
			if (bufferedImage != null) {
				graphics2D.drawImage(bufferedImage, affineTransform, null);
			}
			List<Image> imageList = document.getPage().getImageList();
			Image image = document.getImage();
			com.meritoki.app.desktop.retina.model.document.Dimension d = null;
			if (imageList != null) {
				for (Image i : imageList) {
					d = i.dimension;
					if (image != null && i.uuid.equals(image.uuid)) {
						graphics2D.setColor(Color.RED);
					} else {
						graphics2D.setColor(Color.YELLOW);
					}
					Rectangle2D.Double rectangle = new Rectangle2D.Double(d.x, d.y, d.width, d.height);
					graphics2D.draw(rectangle);
				}

			}
			List<Shape> shapeList = document.getPage().getShapeMatrixShapeList();
			Shape shape = document.getPage().getShape();
			Shape previousShape = null;
			if (shapeList != null) {
				for (Shape s : shapeList) {
					d = s.getDimension();
					if (shape != null && s.uuid.equals(shape.uuid)) {
						graphics2D.setColor(Color.RED);
					} else {
						graphics2D.setColor(Color.BLUE);
					}
					switch (s.type) {
					case ELLIPSE: {
						Ellipse2D.Double ellipse = new Ellipse2D.Double(d.x, d.y, d.width, d.height);
						graphics2D.draw(ellipse);
						break;
					}
					case RECTANGLE: {
						Rectangle2D.Double rectangle = new Rectangle2D.Double(d.x, d.y, d.width, d.height);
						graphics2D.draw(rectangle);
						break;
					}
					}
					if (previousShape != null) {
						com.meritoki.app.desktop.retina.model.document.Dimension dimension = previousShape
								.getDimension();
//					    g.drawLine(X1, Y1, X2, Y2);
						graphics2D.drawLine((int) (dimension.x + (dimension.width / 2)),
								(int) (dimension.y + (dimension.height / 2)), (int) (d.x + (d.width / 2)),
								(int) (d.y + (d.height / 2)));
					}
					previousShape = s;
//                                        NodeController.saveJpg("./", s.uuid+".jpg", s.bufferedImage);
				}
			}
//			graphics2D.setColor(Color.magenta);
//			for(int i =0; i < 1000; i++) {
//				graphics2D.drawLine(i*100, 0, i*100, 10000);
//			}
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
	public void mousePressed(MouseEvent e) {
		Point point = new Point();
		point.x = e.getX();
		point.y = e.getY();
		this.model.document.cache.pressedImage = this.model.document.getImage(point);
		if (this.model.document.cache.pressedImage != null) {
			this.model.document.setImage(this.model.document.cache.pressedImage.uuid);
			this.model.document.cache.pressedShape = this.model.document.getShape(point);
		}
		this.model.document.cache.pressedPoint = point;
	}

	/**
	 * Here a mouse is released, telling us where an Add, Move, Resize, and Remove
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
//		e.consume();
		this.model.document.cache.releasedPoint = new Point(e.getX(), e.getY());
		if (this.model.document.cache.pressedPoint.equals(this.model.document.cache.releasedPoint)) {
			if (this.model.document.cache.pressedShape != null)
				this.model.document.pattern.execute("setShape");
		} else {
			this.model.document.cache.selection = this.model.getDocument().getPage()
					.intersectShape(this.model.document.cache.pressedPoint);
			if (this.model.document.cache.selection != null) {
				this.model.document.pattern.execute("resizeShape");
			} else {
				if (this.model.document.cache.pressedShape != null) {
					this.model.document.cache.releasedImage = this.model.document.getImage(this.model.document.cache.releasedPoint);
					this.model.getDocument().pattern.execute("moveShape");
				} else {
					this.model.getDocument().pattern.execute("addShape");
				}
			}
		}
		this.main.selectionDialog.init();
		this.repaint();
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
		this.model.document.cache.scale += delta;
		if (this.model.document.cache.scale >= 0 && this.model.document.cache.scale <= 2) {
			logger.trace("mouseWheelMoved(...) scale = " + this.model.document.cache.scale);
			this.model.getDocument().getPage().setScale(this.model.document.cache.scale);
			revalidate();
			repaint();
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if ((e.getKeyCode() == KeyEvent.VK_DOWN) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {
			e.consume();
			Page page = this.model.getDocument().getPage();
			page.setBufferedImage(null);
			Image file = (page != null) ? page.getImage() : null;
			if (file != null) {
				file.setMargin(file.dimension.margin + 10);
				page.setBufferedImage(null);
			}
			repaint();
		} else if ((e.getKeyCode() == KeyEvent.VK_UP) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {
			e.consume();
			Page page = this.model.getDocument().getPage();
			page.setBufferedImage(null);
			Image file = (page != null) ? page.getImage() : null;
			if (file != null) {
				file.setMargin(file.dimension.margin - 10);
				page.setBufferedImage(null);
			}
			repaint();
		} else if ((e.getKeyCode() == KeyEvent.VK_Z) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {
			e.consume();
			this.model.getDocument().pattern.undo();
			repaint();
		} else if ((e.getKeyCode() == KeyEvent.VK_Y) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {
			e.consume();
			this.model.getDocument().pattern.redo();
			repaint();
		} else if ((e.getKeyCode() == KeyEvent.VK_M) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {
			String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
			List<Shape> shapeList = this.model.getDocument().getPage().getShapeList();
			this.generateManifest(timeStamp, shapeList);
		} else if ((e.getKeyCode() == KeyEvent.VK_T) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {
			List<String[]> stringArrayList = NodeController.openCsv("import.csv");
			this.model.getDocument().importText(stringArrayList);
		} else {
			e.consume();
			int keyCode = e.getKeyCode();
			int index = this.model.getDocument().getIndex();
			switch (keyCode) {
			case KeyEvent.VK_BACK_SPACE: {
				logger.debug("KeyPressed(BACK_SPACE)");
				this.model.document.cache.pressedShape = this.model.getDocument().getPage().getShape();
				this.model.getDocument().pattern.execute("removeShape");
				this.repaint();
				break;
			}
			case KeyEvent.VK_LEFT: {
				logger.debug("keyPressed(LEFT)");
				this.model.getDocument().setIndex(--index);
				this.main.imageDialog.init();
				this.main.selectionDialog.init();
//				this.main.matrixDialog.init();
				this.repaint();
				break;
			}
			case KeyEvent.VK_RIGHT: {
				logger.debug("keyPressed(RIGHT)");
				this.model.getDocument().setIndex(++index);
				this.main.imageDialog.init();
				this.main.selectionDialog.init();
//				this.main.matrixDialog.init();
				this.repaint();
				break;
			}
			case KeyEvent.VK_UP: {
				logger.debug("keyPressed(UP)");
				this.model.getDocument().setIndex(--index);
				this.main.imageDialog.init();
				this.main.selectionDialog.init();
//				this.main.matrixDialog.init();
				this.repaint();
				break;
			}
			case KeyEvent.VK_DOWN: {
				logger.debug("keyPressed(DOWN)");
				this.model.getDocument().setIndex(++index);
				this.main.imageDialog.init();
				this.main.selectionDialog.init();
//				this.main.matrixDialog.init();
				this.repaint();
				break;
			}
			}
		}
	}

	public void generateManifest(String timeStamp, List<Shape> shapeList) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("my_own_id");
		stringBuilder.append(",");
		stringBuilder.append("the_image");
		stringBuilder.append("\n");
		new java.io.File("./" + timeStamp).mkdir();
		Shape shape = null;
		for (int i = 0; i < shapeList.size(); i++) {
			shape = shapeList.get(i);
			stringBuilder.append(i);
			stringBuilder.append(",");
			NodeController.saveJpg("./" + timeStamp, shape.uuid + ".jpg", shape.bufferedImage);
			stringBuilder.append(shape.uuid + ".jpg");
			stringBuilder.append("\n");
		}
		NodeController.saveCsv("./" + timeStamp, "manifest.csv", stringBuilder);
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
