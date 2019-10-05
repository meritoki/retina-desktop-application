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
package com.meritoki.retina.application.desktop.view.dialog;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.meritoki.retina.application.desktop.controller.script.PageScript;
import com.meritoki.retina.application.desktop.model.project.Project;
import com.meritoki.retina.application.desktop.view.frame.Main;

/**
 *
 * @author osvaldo.rodriguez
 */
public class Image extends javax.swing.JDialog implements MouseListener, KeyListener{

    static Logger logger = LogManager.getLogger(Matrix.class.getName());
    private Project model;
    private PageScript pageScript = new PageScript();
    private List<com.meritoki.retina.application.desktop.model.project.Page> sortedPageList;
    
    /**
     * Creates new form PageDialog
     * @param parent
     * @param modal
     */
    public Image(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        this.setTitle("Page");
        this.initComponents();
        this.pageList.addMouseListener(this);
        this.pageList.addKeyListener(this);
    }
    
    public void setModel(Project model){
        logger.debug("setModel("+model+")");
        this.model = model;
        this.init();
    }
      
    public void init(){
        logger.debug("init()");
        this.initLabel();
        this.initList();
    }
      
    public void initLabel(){
        logger.debug("initLabel()");
        com.meritoki.retina.application.desktop.model.project.Page page = (this.model != null)?this.model.getPage():null;
        BufferedImage bufferedPage = (page != null)?page.getBufferedImage():null;
        int pageIndex = (this.model != null)? this.model.getIndex():0;
        List<com.meritoki.retina.application.desktop.model.project.Page> pageList = this.model.getPageList();
        this.indexValueLabel.setText(pageIndex+"");
        if(page!=null){
            this.nameValueLabel.setText(page.fileName);
            this.pathValueLabel.setText(page.filePath);
            this.uuidValueLabel.setText(page.uuid);
        }else{
            this.nameValueLabel.setText("null");
            this.pathValueLabel.setText("null");
            this.uuidValueLabel.setText("null");
        }
        int size = (pageList != null)?pageList.size():0;
        this.listSizeValueLabel.setText(size+"");
        int width = (bufferedPage != null)?bufferedPage.getWidth():0;
        int height = (bufferedPage != null)?bufferedPage.getHeight():0;
        this.sizeValueLabel.setText(width+"p x "+height+"p");
    }
    
    public void initList(){
        logger.debug("initList()");
        int pageIndex = (this.model != null)? this.model.getIndex():0;
        List<com.meritoki.retina.application.desktop.model.project.Page> pageList = this.model.getPageList();
        this.initPageList(pageList);
        this.setPageListSelectedIndex(pageIndex);
    }
    
    public void initPageList(List<com.meritoki.retina.application.desktop.model.project.Page> pageList) {
        DefaultListModel defaultListModel = new DefaultListModel();
        if(pageList != null){
            for(int i=0;i<pageList.size(); i++) {
                defaultListModel.addElement(pageList.get(i).uuid);    
            }
        }
        this.pageList.setModel(defaultListModel);
    }
    
    public void setPageListSelectedIndex(int index){
        logger.debug("setPageListSelectedIndex("+index+")");
        this.pageList.setSelectedIndex(index);
    }
    
    
    
    public void mouseClicked(MouseEvent e) {
        if(e.getClickCount()==1){
            String selectedItem = (String) this.pageList.getSelectedValue();
            this.model.setPage(selectedItem);
            this.initLabel();
            this.getParent().repaint();
            ((Main)this.getParent()).shapeDialog.init();
            ((Main)this.getParent()).pageDialog.init();
        }
    }
    
     @Override
    public void mousePressed(MouseEvent e) {
//        System.out.println("mousePressed");
//        String selectedItem = (String) jList1.getSelectedValue();
//        System.out.println(selectedItem);
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseReleased(MouseEvent e) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseEntered(MouseEvent e) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseExited(MouseEvent e) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        int index = this.model.getIndex();
        switch(keyCode) {
            case KeyEvent.VK_LEFT:{
                logger.debug("keyPressed LEFT");
                index = this.model.getIndex();
                index=index-1;
                this.model.setIndex(index);
                this.initLabel();
                this.setPageListSelectedIndex(index);
                this.getParent().repaint();
                ((Main)this.getParent()).shapeDialog.init();
                ((Main)this.getParent()).pageDialog.init();
                break;
            }
            case KeyEvent.VK_RIGHT:{
                logger.debug("keyPressed RIGHT");
                index = this.model.getIndex();
                index=index+1;
                this.model.setIndex(index);
                this.initLabel();
                this.setPageListSelectedIndex(index);
                this.getParent().repaint();
                ((Main)this.getParent()).shapeDialog.init();
                ((Main)this.getParent()).pageDialog.init();
                break;
            }

        }
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    @Override
    public void keyReleased(KeyEvent e) {
        String uuid = (String)pageList.getSelectedValue();
        com.meritoki.retina.application.desktop.model.project.Page page = this.model.getPage();
        if(page != null && !uuid.equals(page.uuid)){
            this.model.setPage(uuid);
            this.initLabel();
            this.getParent().repaint();
            ((Main)this.getParent()).shapeDialog.init();
            ((Main)this.getParent()).pageDialog.init();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        uuidLabel = new javax.swing.JLabel();
        uuidValueLabel = new javax.swing.JLabel();
        nameLabel = new javax.swing.JLabel();
        nameValueLabel = new javax.swing.JLabel();
        pathLabel = new javax.swing.JLabel();
        pathValueLabel = new javax.swing.JLabel();
        indexLabel = new javax.swing.JLabel();
        sizeLabel = new javax.swing.JLabel();
        indexValueLabel = new javax.swing.JLabel();
        sizeValueLabel = new javax.swing.JLabel();
        setPageList = new javax.swing.JButton();
        pageListScrollPane = new javax.swing.JScrollPane();
        pageList = new javax.swing.JList();
        pageScriptScrollPane = new javax.swing.JScrollPane();
        pageScriptTextArea = new javax.swing.JTextArea();
        executePageScriptButton = new javax.swing.JButton();
        resetPageScriptButton = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        listSizeValueLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        uuidLabel.setText("UUID:");

        uuidValueLabel.setText("null");

        nameLabel.setText("Name:");

        nameValueLabel.setText("null");

        pathLabel.setText("Path:");

        pathValueLabel.setText("null");

        indexLabel.setText("Index:");

        sizeLabel.setText("Size:");

        indexValueLabel.setText("null");

        sizeValueLabel.setText("null");

        setPageList.setText("Set");
        setPageList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setPageListActionPerformed(evt);
            }
        });

        pageList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        pageListScrollPane.setViewportView(pageList);

        pageScriptTextArea.setColumns(20);
        pageScriptTextArea.setRows(5);
        pageScriptScrollPane.setViewportView(pageScriptTextArea);

        executePageScriptButton.setText("Execute");
        executePageScriptButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                executePageScriptButtonActionPerformed(evt);
            }
        });

        resetPageScriptButton.setText("Reset");
        resetPageScriptButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetPageScriptButtonActionPerformed(evt);
            }
        });

        jLabel1.setText("List:");

        jLabel2.setText("Size:");

        listSizeValueLabel.setText("null");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pageScriptScrollPane, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(pageListScrollPane)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(7, 7, 7)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(uuidLabel)
                                    .addComponent(indexLabel)))
                            .addComponent(nameLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(pathLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(sizeLabel, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(nameValueLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(uuidValueLabel)
                                    .addComponent(indexValueLabel)
                                    .addComponent(pathValueLabel)
                                    .addComponent(sizeValueLabel))
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(listSizeValueLabel))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(resetPageScriptButton, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(executePageScriptButton, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(setPageList, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(indexValueLabel)
                    .addComponent(indexLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(uuidValueLabel)
                    .addComponent(uuidLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(pathLabel)
                    .addComponent(pathValueLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nameLabel)
                    .addComponent(nameValueLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(sizeLabel)
                    .addComponent(sizeValueLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(listSizeValueLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pageListScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pageScriptScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(executePageScriptButton)
                    .addComponent(resetPageScriptButton)
                    .addComponent(setPageList))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void setPageListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setPageListActionPerformed
        this.model.pageList = this.sortedPageList;
        this.pageScriptTextArea.setText("");
    }//GEN-LAST:event_setPageListActionPerformed

    private void executePageScriptButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_executePageScriptButtonActionPerformed
        String value = this.pageScriptTextArea.getText();
        this.sortedPageList = new ArrayList<>(this.model.getPageList());
        this.pageScript.setPageList(this.sortedPageList);
        try {
            this.pageScript.page(value);
        } catch (Exception ex) {
            System.err.println(ex);
        }
        this.initPageList(this.sortedPageList);
    }//GEN-LAST:event_executePageScriptButtonActionPerformed

    private void resetPageScriptButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetPageScriptButtonActionPerformed
        this.initPageList(this.model.getPageList());
    }//GEN-LAST:event_resetPageScriptButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Matrix.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Matrix.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Matrix.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Matrix.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Matrix dialog = new Matrix(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton executePageScriptButton;
    private javax.swing.JList pageList;
    private javax.swing.JScrollPane pageListScrollPane;
    private javax.swing.JScrollPane pageScriptScrollPane;
    private javax.swing.JTextArea pageScriptTextArea;
    private javax.swing.JLabel indexLabel;
    private javax.swing.JLabel indexValueLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel listSizeValueLabel;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JLabel nameValueLabel;
    private javax.swing.JLabel pathLabel;
    private javax.swing.JLabel pathValueLabel;
    private javax.swing.JButton resetPageScriptButton;
    private javax.swing.JButton setPageList;
    private javax.swing.JLabel sizeLabel;
    private javax.swing.JLabel sizeValueLabel;
    private javax.swing.JLabel uuidLabel;
    private javax.swing.JLabel uuidValueLabel;
    // End of variables declaration//GEN-END:variables


}
