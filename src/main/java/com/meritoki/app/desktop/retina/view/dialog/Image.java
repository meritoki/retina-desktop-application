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
package com.meritoki.app.desktop.retina.view.dialog;

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

import com.meritoki.app.desktop.retina.controller.script.ScriptController;
import com.meritoki.app.desktop.retina.model.Model;
import com.meritoki.app.desktop.retina.model.document.Document;
import com.meritoki.app.desktop.retina.model.document.Page;
import com.meritoki.app.desktop.retina.model.document.Project;
import com.meritoki.app.desktop.retina.view.frame.Main;

/**
 *
 * @author osvaldo.rodriguez
 */
public class Image extends javax.swing.JDialog implements MouseListener, KeyListener {

    /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 8390605747809634118L;
	/**
	 * Logger for class.
	 */
	private static Logger logger = LogManager.getLogger(Image.class.getName());
	/**
	 * Reference to Model class.
	 */
    private Model model;
    
    private Main main;
    
    /**
     * Image dialog class.
     * @param parent
     * @param modal
     */
    public Image(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        this.setTitle("Page");
        this.main = (Main)this.getParent();
        this.initComponents();
        this.pageList.addMouseListener(this);
        this.pageList.addKeyListener(this);
    }
    
    /**
     * Function set Model for instance.
     * @param model
     */
    public void setModel(Model model){
        logger.debug("setModel("+model+")");
        this.model = model;
        this.init();
    }
    
    /**
     * Function instantiates view. 
     */
    public void init(){
        logger.debug("init()");
        this.initLabel();
        this.initList();
    }
      
    public void initLabel(){
        logger.debug("initLabel()");
        Document document = (this.model != null) ? this.model.getDocument() : null;
        Project project = (document != null) ? document.getProject() : null;
        Page page = (project != null)? project.getPage():null;
        BufferedImage bufferedPage = (page != null)?page.getBufferedImage():null;
        int pageIndex = (project != null)? project.getIndex():0;
        List<Page> pageList = (project != null)? project.getPageList():null;
        this.indexValueLabel.setText(pageIndex+"");
        if(page!=null){
        	if(page.fileList.size() == 1) {
	            this.nameValueLabel.setText(page.fileList.get(0).name);
	            this.pathValueLabel.setText(page.fileList.get(0).path);
        	} else {
        		this.nameValueLabel.setText("NA");
	            this.pathValueLabel.setText("NA");
        	}
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
        Document document = (this.model != null) ? this.model.getDocument() : null;
        Project project = (document != null) ? document.getProject() : null;
        int pageIndex = (project != null)? project.getIndex():0;
        List<Page> pageList = (project != null)? project.getPageList():null;
        this.initPageList(pageList);
        this.setPageListSelectedIndex(pageIndex);
    }
    
    public void initPageList(List<Page> pageList) {
        logger.debug("initPageList(...)");
    	DefaultListModel<String> defaultListModel = new DefaultListModel<>();
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
    
    
    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getClickCount()==1){
            String selectedItem = (String) this.pageList.getSelectedValue();
            Document document = (this.model != null) ? this.model.getDocument() : null;
            Project project = (document != null) ? document.getProject() : null;
            project.setPage(selectedItem);
            this.initLabel();
            this.main.repaint();
            this.main.selectionDialog.init();
//            ((Main)this.getParent()).matrixDialog.init();
        }
    }
    
     @Override
    public void mousePressed(MouseEvent e) {
    	//TODO
    }

    @Override
    public void mouseReleased(MouseEvent e) {	
    	//TODO
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    	//TODO
    }

    @Override
    public void mouseExited(MouseEvent e) {
    	//TODO
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        Document document = (this.model != null) ? this.model.getDocument() : null;
        Project project = (document != null) ? document.getProject() : null;
        int index = project.getIndex();
        switch(keyCode) {
            case KeyEvent.VK_LEFT:{
                logger.debug("keyPressed LEFT");
                index = project.getIndex();
                index=index-1;
                project.setIndex(index);
                this.initLabel();
                this.setPageListSelectedIndex(index);
                this.getParent().repaint();
                ((Main)this.getParent()).selectionDialog.init();
//                ((Main)this.getParent()).matrixDialog.init();
                break;
            }
            case KeyEvent.VK_RIGHT:{
                logger.debug("keyPressed RIGHT");
                index = project.getIndex();
                index=index+1;
                project.setIndex(index);
                this.initLabel();
                this.setPageListSelectedIndex(index);
                this.getParent().repaint();
                ((Main)this.getParent()).selectionDialog.init();
//                ((Main)this.getParent()).matrixDialog.init();
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
        Document document = (this.model != null) ? this.model.getDocument() : null;
        Project project = (document != null) ? document.getProject() : null;
        Page page = project.getPage();
        if(page != null && !uuid.equals(page.uuid)){
            project.setPage(uuid);
            this.initLabel();
            this.getParent().repaint();
            ((Main)this.getParent()).selectionDialog.init();
//            ((Main)this.getParent()).matrixDialog.init();
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
        imageListScrollPane = new javax.swing.JScrollPane();
        pageList = new javax.swing.JList();
        imageScriptScrollPane = new javax.swing.JScrollPane();
        pageScriptTextArea = new javax.swing.JTextArea();
        executeImageScriptButton = new javax.swing.JButton();
        resetPageScriptButton = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        listSizeValueLabel = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();

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
        imageListScrollPane.setViewportView(pageList);

        pageScriptTextArea.setColumns(20);
        pageScriptTextArea.setRows(5);
        imageScriptScrollPane.setViewportView(pageScriptTextArea);

        executeImageScriptButton.setText("Execute");
        executeImageScriptButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                executeImageScriptButtonActionPerformed(evt);
            }
        });

        resetPageScriptButton.setText("Reset");
        resetPageScriptButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetPageScriptButtonActionPerformed(evt);
            }
        });

        jLabel1.setText("List");

        jLabel2.setText("Size:");

        listSizeValueLabel.setText("null");

        jLabel3.setText("Script");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(imageScriptScrollPane, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(imageListScrollPane)
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
                        .addComponent(executeImageScriptButton, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(setPageList, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(0, 0, Short.MAX_VALUE)))
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
                .addComponent(imageListScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(imageScriptScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(executeImageScriptButton)
                    .addComponent(resetPageScriptButton)
                    .addComponent(setPageList))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void setPageListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setPageListActionPerformed
        //COMMAND
    	Document document = (this.model != null) ? this.model.getDocument() : null;
        Project project = (document != null) ? document.getProject() : null;
    	project.pageList = this.model.variable.pageList;
        this.pageScriptTextArea.setText("");
    }//GEN-LAST:event_setPageListActionPerformed

    private void executeImageScriptButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_executeImageScriptButtonActionPerformed
        String value = this.pageScriptTextArea.getText();
        Document document = (this.model != null) ? this.model.getDocument() : null;
        Project project = (document != null) ? document.getProject() : null;
        this.model.variable.pageList = new ArrayList<>(project.getPageList());//BUG 201912212221 this step is stripping the fileList, which it why the shapes do not appear after a join.
//        this.model.variable.script.setPageList(this.model.variable.pageList);
        try {
            ScriptController.sortPageList(this.model.variable.pageList, value);
        } catch (Exception ex) {
            System.err.println(ex);
        }
        this.initPageList(this.model.variable.pageList);
    }//GEN-LAST:event_executeImageScriptButtonActionPerformed

    private void resetPageScriptButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetPageScriptButtonActionPerformed
    	Document document = (this.model != null) ? this.model.getDocument() : null;
        Project project = (document != null) ? document.getProject() : null;
    	this.initPageList(project.getPageList());
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
            java.util.logging.Logger.getLogger(Image.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Image.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Image.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Image.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Image dialog = new Image(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton executeImageScriptButton;
    private javax.swing.JScrollPane imageListScrollPane;
    private javax.swing.JScrollPane imageScriptScrollPane;
    private javax.swing.JLabel indexLabel;
    private javax.swing.JLabel indexValueLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel listSizeValueLabel;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JLabel nameValueLabel;
    private javax.swing.JList pageList;
    private javax.swing.JTextArea pageScriptTextArea;
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
