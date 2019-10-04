/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.retina.view.panel;

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
import javax.swing.JFrame;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.retina.view.frame.Main;
import org.retina.model.Model;
import org.retina.model.Rectangle;

public class Image extends JPanel implements MouseListener, MouseWheelListener, KeyListener {

    static Logger logger = LogManager.getLogger(org.retina.view.dialog.Image.class.getName());
    private static final long serialVersionUID = 3989576625299550361L;
    private Model model;
    private Main main;
    private boolean move = false;
    private boolean resize = false;
    private float scale = 1;
    private int x;
    private int y;
    private int i;
    private int j;
    

    public Image() {
        super();
        this.setBackground(Color.black);
        this.addMouseListener(this);
        this.addMouseWheelListener(this);  
        this.addKeyListener(this);
    }
    
    public void setMain(Main main){
        this.main = main;
    }
    
    public void setModel(Model model) {
        logger.debug("setModel("+model+")");
        this.model = model;
        this.setPreferredSize(this.getPreferredSize());
    }
    
    @Override
    public Dimension getPreferredSize() {            
        Dimension size = new Dimension(1028, 512);
        if (this.model != null && this.model.getImage() != null && this.model.getImage().getBufferedImage() != null) {            
            size.width = Math.round(this.model.getImage().getBufferedImage().getWidth() * scale);
            size.height = Math.round(this.model.getImage().getBufferedImage().getHeight() * scale);                
        }        
        return size;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        org.retina.model.Image image = (this.model != null)?this.model.getImage():null;
        BufferedImage bufferedImage = (image != null)?image.getBufferedImage():null;
        AffineTransform at = new AffineTransform();
        at.scale(scale, scale);
        if(bufferedImage != null){
            g2d.drawImage(bufferedImage, at, this);
            g2d.dispose();
        }
        org.retina.model.Rectangle rectangle = (image != null)?image.getRectangle():null;
        List<org.retina.model.Rectangle> rectangleList = (image != null)?image.getRectangleList():null;
        if(rectangleList!=null){
            for(Rectangle r:rectangleList){
                if(rectangle != null && r.uuid.equals(rectangle.uuid)){
                    g.setColor(Color.RED);
                }else{
                    g.setColor(Color.BLUE);
                }
                int x = r.getX();
                int y = r.getY();
                int i = r.getI();
                int j = r.getJ();
                int px = Math.min(x,i);
                int py = Math.min(y,j);
                int pw=Math.abs(x-i);
                int ph=Math.abs(y-j);
                px*=scale;
                py*=scale;
                pw*=scale;
                ph*=scale;
                g.drawRect(px, py, pw, ph);
            }        
        }
    }
    

    @Override
    public void mousePressed(MouseEvent e) {
        move = false;
        resize=false;
        org.retina.model.Image image = (this.model != null)? this.model.getImage():null;
        List<org.retina.model.Rectangle> rectangleList = (image != null)? image.getRectangleList():null;
        if(rectangleList != null) {
            for(Rectangle r:this.model.getImage().getRectangleList()) {
                if(r.contains(e.getX(),e.getY())){
                    this.model.getImage().setRectangle(r);
                    move = true;
                    break;
                }
            }
        }
//        for(Rectangle r:this.model.getImage().getRectangleList()) {
//            if(r.intersect(e.getX(),e.getY())){
//                this.model.getImage().setRectangle(r);
//                this.resize = true;
//                break;
//            }
//        }
        if(move){
            System.out.println("move");
            this.x = e.getX();
            this.y = e.getY();
        } else if(resize){
            System.out.println("resize");
            this.x = e.getX();
            this.y = e.getY();
        }else {
            if(image != null) {
                image.setRectangle(new Rectangle());
                image.getRectangle().setX(e.getX());
                image.getRectangle().setY(e.getY());
            }
        }
        repaint();
    }

    public void mouseDragged(MouseEvent e) {}

    public void mouseReleased(MouseEvent e) {
        if(move){
            System.out.println("move");
            this.i = e.getX();
            this.j = e.getY();
            this.model.getImage().getRectangle().move(this.i-this.x,this.j-this.y);
            
        }else{
            this.model.getImage().getRectangle().setI(e.getX());
            this.model.getImage().getRectangle().setJ(e.getY());
            if(this.model.getImage().getRectangle().isValid()){
                this.model.getImage().getRectangleList().add(this.model.getImage().getRectangle());
                this.model.getImage().getPage().setRectangleList(this.model.getImage().getRectangleList());
                this.main.rectangleDialog.setModel(this.model);
                this.main.pageDialog.setModel(this.model);
            }
        }
        
        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        double delta = 0.05f * e.getPreciseWheelRotation();
        scale += delta;
        revalidate();
        repaint();
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        int index = this.model.getIndex();
        switch(keyCode) {
            case KeyEvent.VK_LEFT:{
                logger.debug("keyPressed(LEFT)");
                this.model.setIndex(--index);
                this.main.imageDialog.init();
                this.main.rectangleDialog.init();
                this.main.pageDialog.init();
                this.repaint();
                break;
            }
            case KeyEvent.VK_RIGHT:{
                logger.debug("keyPressed(RIGHT)");
                this.model.setIndex(++index);
                this.main.imageDialog.init();
                this.main.rectangleDialog.init();
                this.main.pageDialog.init();
                this.repaint();
                break;
            }
            case KeyEvent.VK_UP:{
                logger.debug("keyPressed(UP)");
                this.model.setIndex(--index);
                this.main.imageDialog.init();
                this.main.rectangleDialog.init();
                this.main.pageDialog.init();
                this.repaint();
                break;
            }
            case KeyEvent.VK_DOWN:{
                logger.debug("keyPressed(DOWN)");
                this.model.setIndex(++index);
                this.main.imageDialog.init();
                this.main.rectangleDialog.init();
                this.main.pageDialog.init();
                this.repaint();
                break;
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
}