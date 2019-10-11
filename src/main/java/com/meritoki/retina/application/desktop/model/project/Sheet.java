package com.meritoki.retina.application.desktop.model.project;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.annotate.JsonIgnore;

public class Sheet extends Page {
	private static Logger logger = LogManager.getLogger(Page.class.getName());
	public List<Page> pageList = new ArrayList<Page>();
	
//    @JsonIgnore
//    public BufferedImage getBufferedImage() {
//    	logger.info("getBufferedImage()");
//    	this.pageList.get(0).loadBufferedImage();
//    	this.pageList.get(1).loadBufferedImage();
////    	BufferedImage bufferedImageA = this.pageList.get(0).file.bufferedImage;
////    	BufferedImage bufferedImageB = this.pageList.get(1).file.bufferedImage;
//    	BufferedImage joinedBufferedImage = this.joinBufferedImage(bufferedImageA, bufferedImageB);
//        return joinedBufferedImage;
//    }
    
    public BufferedImage joinBufferedImage(BufferedImage img1,BufferedImage img2) {
    	logger.info("joinBufferedImage("+img1+","+img2+")");
        //do some calculate first
        int offset  = 5;
        int wid = img1.getWidth()+img2.getWidth()+offset;
        int height = Math.max(img1.getHeight(),img2.getHeight())+offset;
        //create a new buffer and draw two image into the new image
        BufferedImage newImage = new BufferedImage(wid,height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = newImage.createGraphics();
        Color oldColor = g2.getColor();
        //fill background
        g2.setPaint(Color.WHITE);
        g2.fillRect(0, 0, wid, height);
        //draw image
        g2.setColor(oldColor);
        g2.drawImage(img1, null, 0, 0);
        g2.drawImage(img2, null, img1.getWidth()+offset, 0);
        g2.dispose();
        return newImage;
    }
}
