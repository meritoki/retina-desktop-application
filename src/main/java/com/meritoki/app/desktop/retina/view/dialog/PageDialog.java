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
import com.meritoki.app.desktop.retina.model.document.Image;
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
        Document document = (this.model != null) ? this.model.document: null;
        Page page = (document != null)? document.getPage():null;
        int pageIndex = (document != null)? document.getIndex():0;
        Image image = (page != null)? page.getImage():null;
        int imageIndex = (page != null)? page.getIndex():0;
        if(page != null) {
        	this.pageIndexLabel.setText(pageIndex+"");
        	this.pageUUIDValueLabel.setText(page.uuid);
        }
        if(image != null) {
        	this.imageIndexValueLabel.setText(imageIndex+"");
        	this.imageUUIDValueLabel.setText(image.uuid);
        	this.imageNameValueLabel.setText(image.fileName);
        	this.imagePathValueLabel.setText(image.filePath);
        }
    }
    
    public void initList(){
        logger.debug("initList()");
        Document document = (this.model != null) ? this.model.document: null;
        int pageIndex = (document != null)? document.getIndex():0;
        List<Page> pageList = (document != null)? document.getPageList():null;
        this.initPageList(pageList);
        this.setPageListSelectedIndex(pageIndex);
        Page page = (document != null)? document.getPage():null;
        int imageIndex = (page != null)? page.getIndex():0;
        List<Image> imageList = (page != null)? page.getImageList():null;
        this.initImageList(imageList);
        this.setImageListSelectedIndex(imageIndex);
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
    
    public void initImageList(List<Image> imageList) {
        logger.debug("initImageList(...)");
    	DefaultListModel<String> defaultListModel = new DefaultListModel<>();
        if(imageList != null){
            for(int i=0;i<imageList.size(); i++) {
                defaultListModel.addElement(imageList.get(i).uuid);    
            }
        }
        this.imageList.setModel(defaultListModel);
    }
    
    public void setImageListSelectedIndex(int index){
        logger.debug("setImageListSelectedIndex("+index+")");
        this.imageList.setSelectedIndex(index);
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getClickCount()==1){
            String uuid = (String) this.pageList.getSelectedValue();
            Document document = (this.model != null) ? this.model.document: null;
            document.cache.pageUUID = uuid;
            try {
				document.pattern.execute("setPage");
                this.main.init();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
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
        Document document = (this.model != null) ? this.model.document: null;
        int index = document.getIndex();
        switch(keyCode) {
            case KeyEvent.VK_LEFT:{
                logger.debug("keyEvent.VK_LEFT");
                document.cache.pageIndex = --index;
                try {
					document.pattern.execute("setPage");
	                this.setPageListSelectedIndex(index);
	                this.main.init();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
//                document.setIndex(index);
                break;
            }
            case KeyEvent.VK_RIGHT:{
                logger.debug("keyEvent.VK_RIGHT");
                document.cache.pageIndex = ++index;
                try {
					document.pattern.execute("setPage");
	                this.setPageListSelectedIndex(index);
	                this.main.init();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
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
        Document document = (this.model != null) ? this.model.document: null;
        Page page = document.getPage();
        if(page != null && uuid != null && !uuid.equals(page.uuid)){
            document.setPage(uuid);
            this.main.init();
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
        pageUUIDValueLabel = new javax.swing.JLabel();
        pageIndexLabel = new javax.swing.JLabel();
        pageIndexValueLabel = new javax.swing.JLabel();
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
        removePageButton = new javax.swing.JButton();
        removeScriptButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        imageList = new javax.swing.JList<>();
        imageListLabel = new javax.swing.JLabel();
        removeImageButton = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();
        imageIndexLabel = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        imageUUIDLabel = new javax.swing.JLabel();
        imageNameLabel = new javax.swing.JLabel();
        imagePathLabel = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JSeparator();
        imageIndexValueLabel = new javax.swing.JLabel();
        imageUUIDValueLabel = new javax.swing.JLabel();
        imageNameValueLabel = new javax.swing.JLabel();
        imagePathValueLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        uuidLabel.setText("UUID:");

        pageUUIDValueLabel.setText("null");

        pageIndexLabel.setText("Index:");

        pageIndexValueLabel.setText("null");

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

        jLabel1.setText("Pages");

        jLabel2.setText("Size:");

        listSizeValueLabel.setText("null");

        jLabel3.setText("Script");

        removePageButton.setText("X");
        removePageButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removePageButtonActionPerformed(evt);
            }
        });

        removeScriptButton.setText("X");
        removeScriptButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeScriptButtonActionPerformed(evt);
            }
        });

        imageList.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(imageList);

        imageListLabel.setText("Images");

        removeImageButton.setText("X");
        removeImageButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeImageButtonActionPerformed(evt);
            }
        });

        imageIndexLabel.setText("Index:");

        imageUUIDLabel.setText("UUID:");

        imageNameLabel.setText("Name:");

        imagePathLabel.setText("Path:");

        imageIndexValueLabel.setText("null");

        imageUUIDValueLabel.setText("null");

        imageNameValueLabel.setText("null");

        imagePathValueLabel.setText("null");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator3, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(259, 259, 259)
                                .addComponent(jLabel2))
                            .addComponent(imageListScrollPane, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 350, Short.MAX_VALUE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(listSizeValueLabel)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(removePageButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(imageScriptScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 343, Short.MAX_VALUE)
                            .addComponent(executeImageScriptButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(removeScriptButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(uuidLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(pageUUIDValueLabel))
                            .addComponent(imageListLabel)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(imageIndexLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(imageIndexValueLabel))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(imageUUIDLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(imageUUIDValueLabel))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(imagePathLabel)
                                    .addComponent(imageNameLabel))
                                .addGap(3, 3, 3)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(imageNameValueLabel)
                                    .addComponent(imagePathValueLabel)))
                            .addComponent(jLabel3)
                            .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 343, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 343, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 343, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(removeImageButton, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(pageIndexLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(pageIndexValueLabel)
                                .addGap(372, 372, 372)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(pageIndexValueLabel)
                    .addComponent(pageIndexLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(pageUUIDValueLabel)
                    .addComponent(uuidLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(listSizeValueLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(imageListScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(removePageButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(imageIndexLabel)
                    .addComponent(imageIndexValueLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(imageUUIDLabel)
                    .addComponent(imageUUIDValueLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(imageNameLabel)
                    .addComponent(imageNameValueLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(imagePathLabel)
                    .addComponent(imagePathValueLabel))
                .addGap(7, 7, 7)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(imageListLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(removeImageButton)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(imageScriptScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(removeScriptButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(executeImageScriptButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void executeImageScriptButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_executeImageScriptButtonActionPerformed
        Document document = (this.model != null) ? this.model.document: null; 
        if(document != null) {
        	document.cache.script = this.pageScriptTextArea.getText();
        	document.cache.pageList = document.getPageList();//BUG 201912212221 this step is stripping the fileList, which it why the shapes do not appear after a join.
        	try {
				document.pattern.execute("executeScript");
//		        this.initPageList(document.cache.pageList);
//		        this.main.repaint();
		        this.main.init();
			} catch (Exception e) {
				logger.error("Exception "+e.getMessage());
				///TODO add dialog that shows error;
			}
        }        
    }//GEN-LAST:event_executeImageScriptButtonActionPerformed

    private void removeScriptButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeScriptButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_removeScriptButtonActionPerformed

    private void removePageButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removePageButtonActionPerformed
    	Document document = (this.model != null) ? this.model.document: null; 
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
    }//GEN-LAST:event_removePageButtonActionPerformed

    private void removeImageButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeImageButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_removeImageButtonActionPerformed

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
    private javax.swing.JButton executeImageScriptButton;
    private javax.swing.JLabel imageIndexLabel;
    private javax.swing.JLabel imageIndexValueLabel;
    private javax.swing.JList<String> imageList;
    private javax.swing.JLabel imageListLabel;
    private javax.swing.JScrollPane imageListScrollPane;
    private javax.swing.JLabel imageNameLabel;
    private javax.swing.JLabel imageNameValueLabel;
    private javax.swing.JLabel imagePathLabel;
    private javax.swing.JLabel imagePathValueLabel;
    private javax.swing.JScrollPane imageScriptScrollPane;
    private javax.swing.JLabel imageUUIDLabel;
    private javax.swing.JLabel imageUUIDValueLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JLabel listSizeValueLabel;
    private javax.swing.JLabel pageIndexLabel;
    private javax.swing.JLabel pageIndexValueLabel;
    private javax.swing.JList pageList;
    private javax.swing.JTextArea pageScriptTextArea;
    private javax.swing.JLabel pageUUIDValueLabel;
    private javax.swing.JButton removeImageButton;
    private javax.swing.JButton removePageButton;
    private javax.swing.JButton removeScriptButton;
    private javax.swing.JLabel uuidLabel;
    // End of variables declaration//GEN-END:variables


}

//@Override
//public void keyPressed(KeyEvent e) {
//    int keyCode = e.getKeyCode();
//    Document document = (this.model != null) ? this.model.document: null;
//    int index = document.getIndex();
//    switch(keyCode) {
//        case KeyEvent.VK_LEFT:{
//            logger.debug("keyPressed LEFT");
//            document.cache.pageIndex = index-1;
//            try {
//				document.pattern.execute("setPage");
//                this.initLabel();
//                this.setPageListSelectedIndex(index);
//                this.main.repaint();
//                this.main.init();
//			} catch (Exception e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
////            document.setIndex(index);
//
//            break;
//        }
//        case KeyEvent.VK_RIGHT:{
//            logger.debug("keyPressed RIGHT");
//            index = document.getIndex();
//            index=index+1;
//            document.setIndex(index);
//            this.initLabel();
//            this.setPageListSelectedIndex(index);
//            this.main.repaint();
//            this.main.init();
//            break;
//        }
//
//    }
//}
//public void initLabel(){
//Document document = (this.model != null) ? this.model.document: null;
//Page page = (document != null)? document.getPage():null;
//BufferedImage bufferedPage = (page != null)?page.getBufferedImage():null;
//int pageIndex = (document != null)? document.getIndex():0;
//List<Page> pageList = (document != null)? document.getPageList():null;
//this.pageIndexValueLabel.setText(pageIndex+"");
//if(page!=null){
//	if(page.imageList.size() == 1) {
//      this.nameValueLabel.setText(page.imageList.get(0).file.getName());
//      this.pathValueLabel.setText(page.imageList.get(0).file.getParent());
//	} else {
//		this.nameValueLabel.setText("NA");
//      this.pathValueLabel.setText("NA");
//	}
//  this.pageUUIDValueLabel.setText(page.uuid);
//}else{
//  this.nameValueLabel.setText("null");
//  this.pathValueLabel.setText("null");
//  this.pageUUIDValueLabel.setText("null");
//}
//int size = (pageList != null)?pageList.size():0;
//this.listSizeValueLabel.setText(size+"");
//int width = (bufferedPage != null)?bufferedPage.getWidth():0;
//int height = (bufferedPage != null)?bufferedPage.getHeight():0;
//this.sizeValueLabel.setText(width+"p x "+height+"p");
//}
