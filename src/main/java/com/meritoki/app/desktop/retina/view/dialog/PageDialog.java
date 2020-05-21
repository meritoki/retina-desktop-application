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
import java.util.List;

import javax.swing.DefaultListModel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.meritoki.app.desktop.retina.model.Model;
import com.meritoki.app.desktop.retina.model.document.Document;
import com.meritoki.app.desktop.retina.model.document.Page;
import com.meritoki.app.desktop.retina.view.frame.MainFrame;

/**
 *
 * @author osvaldo.rodriguez
 */
public class PageDialog extends javax.swing.JDialog implements MouseListener, KeyListener {

    /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 8390605747809634118L;
	/**
	 * Logger for class.
	 */
	private static Logger logger = LogManager.getLogger(PageDialog.class.getName());
	/**
	 * Reference to Model class.
	 */
    private Model model;
    
    private MainFrame main;
    
    /**
     * Image dialog class.
     * @param parent
     * @param flag
     */
    public PageDialog(java.awt.Frame parent, boolean flag) {
        super(parent, flag);
        this.setTitle("Page");
        this.main = (MainFrame)this.getParent();
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
        Page page = (document != null)? document.getPage():null;
        BufferedImage bufferedPage = (page != null)?page.getBufferedImage():null;
        int pageIndex = (document != null)? document.getIndex():0;
        List<Page> pageList = (document != null)? document.getPageList():null;
        this.indexValueLabel.setText(pageIndex+"");
        if(page!=null){
        	if(page.imageList.size() == 1) {
	            this.nameValueLabel.setText(page.imageList.get(0).file.getName());
	            this.pathValueLabel.setText(page.imageList.get(0).file.getParent());
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
        int pageIndex = (document != null)? document.getIndex():0;
        List<Page> pageList = (document != null)? document.getPageList():null;
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
            document.setPage(selectedItem);
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
        int index = document.getIndex();
        switch(keyCode) {
            case KeyEvent.VK_LEFT:{
                logger.debug("keyPressed LEFT");
                index = document.getIndex();
                index=index-1;
                document.setIndex(index);
                this.initLabel();
                this.setPageListSelectedIndex(index);
                this.getParent().repaint();
                ((MainFrame)this.getParent()).selectionDialog.init();
//                ((Main)this.getParent()).matrixDialog.init();
                break;
            }
            case KeyEvent.VK_RIGHT:{
                logger.debug("keyPressed RIGHT");
                index = document.getIndex();
                index=index+1;
                document.setIndex(index);
                this.initLabel();
                this.setPageListSelectedIndex(index);
                this.getParent().repaint();
                ((MainFrame)this.getParent()).selectionDialog.init();
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
        String uuid = (String) pageList.getSelectedValue();
        Document document = (this.model != null) ? this.model.getDocument() : null;
        Page page = document.getPage();
        if(page != null && uuid != null && !uuid.equals(page.uuid)){
            document.setPage(uuid);
            this.initLabel();
            this.getParent().repaint();
            ((MainFrame)this.getParent()).selectionDialog.init();
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
        imageListScrollPane = new javax.swing.JScrollPane();
        pageList = new javax.swing.JList();
        imageScriptScrollPane = new javax.swing.JScrollPane();
        pageScriptTextArea = new javax.swing.JTextArea();
        executeImageScriptButton = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        listSizeValueLabel = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        deletePageButton = new javax.swing.JButton();
        clearScriptButton = new javax.swing.JButton();

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

        jLabel1.setText("List");

        jLabel2.setText("Size:");

        listSizeValueLabel.setText("null");

        jLabel3.setText("Script");

        deletePageButton.setText("X");
        deletePageButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deletePageButtonActionPerformed(evt);
            }
        });

        clearScriptButton.setText("Clear");
        clearScriptButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearScriptButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
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
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(executeImageScriptButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(imageScriptScrollPane, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(259, 259, 259)
                                .addComponent(jLabel2))
                            .addComponent(imageListScrollPane, javax.swing.GroupLayout.Alignment.LEADING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(deletePageButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(17, 17, 17)
                                .addComponent(listSizeValueLabel)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(clearScriptButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 82, Short.MAX_VALUE))))
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
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(listSizeValueLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(imageListScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(deletePageButton))
                .addGap(5, 5, 5)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(imageScriptScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(clearScriptButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(executeImageScriptButton)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void executeImageScriptButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_executeImageScriptButtonActionPerformed
        Document document = (this.model != null) ? this.model.getDocument() : null; 
        if(document != null) {
        	document.cache.script = this.pageScriptTextArea.getText();
        	document.cache.pageList = document.getPageList();//BUG 201912212221 this step is stripping the fileList, which it why the shapes do not appear after a join.
        	try {
				document.pattern.execute("executeScript");
		        this.initPageList(document.cache.pageList);
			} catch (Exception e) {
				logger.error("Exception "+e.getMessage());
				///TODO add dialog that shows error;
			}
        }        
    }//GEN-LAST:event_executeImageScriptButtonActionPerformed

    private void clearScriptButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearScriptButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_clearScriptButtonActionPerformed

    private void deletePageButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deletePageButtonActionPerformed
    	Document document = (this.model != null) ? this.model.getDocument() : null; 
    	if(document != null) {
            String selectedItem = (String) this.pageList.getSelectedValue();
            document.setPage(selectedItem);
            document.cache.pressedPage = document.getPage();
            try {
				document.pattern.execute("removePage");
				this.init();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    }//GEN-LAST:event_deletePageButtonActionPerformed

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
            java.util.logging.Logger.getLogger(PageDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PageDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PageDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PageDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                PageDialog dialog = new PageDialog(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton clearScriptButton;
    private javax.swing.JButton deletePageButton;
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
    private javax.swing.JLabel sizeLabel;
    private javax.swing.JLabel sizeValueLabel;
    private javax.swing.JLabel uuidLabel;
    private javax.swing.JLabel uuidValueLabel;
    // End of variables declaration//GEN-END:variables


}
