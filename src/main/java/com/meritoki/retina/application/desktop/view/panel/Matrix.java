/*
 * Copyright 2019 osvaldo.rodriguez.
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
package com.meritoki.retina.application.desktop.view.panel;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JPanel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.meritoki.retina.application.desktop.model.Model;
import com.meritoki.retina.application.desktop.model.project.Data;
import com.meritoki.retina.application.desktop.model.project.Unit;
import com.meritoki.retina.application.desktop.view.frame.Main;

/**
 *
 * @author osvaldo.rodriguez
 */
public class Matrix extends JPanel implements MouseListener, MouseWheelListener, KeyListener {

	private static final long serialVersionUID = 6483831845668642285L;
	private static Logger logger = LogManager.getLogger(Data.class.getName());
    private Main main = null;
    private Model model = null;
    
    /**
     * Instantiate new Matrix Panel
     */
    public Matrix(){
        super();
        this.setOpaque(true);
        this.setBackground(Color.white);
        
    }
    
    /**
     * Set the parent component Main
     * @param main
     */
    public void setMain(Main main){
        this.main = main;
    }
    
    /**
     * Set the Model for use in the Panel
     * @param model
     */
    public void setModel(Model model) {
        logger.debug("setModel("+model+")");
        this.model = model;
    }
    
    
    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        com.meritoki.retina.application.desktop.model.project.Page page = (this.model.project!= null)?this.model.project.getPage():null;
        List<LinkedList<Data>> dataMatrix = (page !=null)?page.getDataMatrix():null;
        int width = 0;
        int height = 0;
        int heightIndex = 0;
        int widthIndex = 0;
        int maxColumn = 0;
        if(dataMatrix != null){
            int w = getWidth();
            int h = getHeight();
            int boxWidth = (int)(w*0.05);
            int boxHeight = (int)(h*0.05);
            height = dataMatrix.size() * boxHeight;
            Data data;
            for (int i = 0;i < dataMatrix.size();i++) {
                if(dataMatrix.get(i).size() > maxColumn) {
                    maxColumn = dataMatrix.get(i).size();
                }
                widthIndex = 0;
                heightIndex += boxHeight;
                g2.setColor(Color.BLACK);
                g2.drawString(""+i, widthIndex+(boxWidth/2), heightIndex+(boxHeight/2));
                for (int j = 0;j < dataMatrix.get(i).size();j++) {
                    widthIndex += boxWidth;
                    g2.setColor(Color.BLACK);
                    g2.drawRect(widthIndex, heightIndex, boxWidth, boxHeight);
                   
                    data = dataMatrix.get(i).get(j);
                    if(data != null){
                        String unitType = data.unit.type;
                        switch(unitType) {
                            case Unit.DATA:{
                                g2.setColor(Color.BLACK);
                                g2.drawString("D", widthIndex+(boxWidth/2), heightIndex+(boxHeight/2));
                                break;
                            }
                            case Unit.TIME:{
                                g2.setColor(Color.BLUE);
                                g2.drawString("T", widthIndex+(boxWidth/2), heightIndex+(boxHeight/2));
                                break;
                            }
                            case Unit.SPACE:{
                                g2.setColor(Color.RED);
                                g2.drawString("S", widthIndex+(boxWidth/2), heightIndex+(boxHeight/2));
                                break;
                            }
                            case Unit.ENERGY:{
                                g2.setColor(Color.GREEN);
                                g2.drawString("E", widthIndex+(boxWidth/2), heightIndex+(boxHeight/2));
                                break;
                            }
                        }
                    }
                }
            }
            heightIndex = 0;
            widthIndex = 0;
            for(int i=0;i<maxColumn;i++){
                widthIndex += boxWidth;
                g2.drawString(""+i, widthIndex+(boxWidth/2), heightIndex+(boxHeight/2));
            }
        }
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mousePressed(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseExited(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void keyTyped(KeyEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void keyPressed(KeyEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void keyReleased(KeyEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    } 
}
