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
package com.meritoki.app.desktop.retina.view.frame;

import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;

import javax.swing.JFrame;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.meritoki.app.desktop.retina.controller.document.DocumentController;
import com.meritoki.app.desktop.retina.model.Model;
import com.meritoki.app.desktop.retina.model.document.Document;
import com.meritoki.app.desktop.retina.view.dialog.PageDialog;
import com.meritoki.app.desktop.retina.view.dialog.CommandDialog;
import com.meritoki.app.desktop.retina.view.dialog.OpenDialog;
import com.meritoki.app.desktop.retina.view.dialog.SaveAsDialog;
import com.meritoki.app.desktop.retina.view.dialog.SelectionDialog;
import com.meritoki.app.desktop.retina.view.dialog.audio.AudioExportDialog;
import com.meritoki.app.desktop.retina.view.dialog.image.ImageImportDialog;
import com.meritoki.app.desktop.retina.view.dialog.microsoft.MicrosoftExportDialog;
import com.meritoki.app.desktop.retina.view.dialog.user.UserLoginDialog;
import com.meritoki.app.desktop.retina.view.dialog.user.UserRegisterDialog;
import com.meritoki.app.desktop.retina.view.dialog.zooniverse.ZooniverseExportDialog;
import com.meritoki.app.desktop.retina.view.dialog.zooniverse.ZooniverseImportDialog;

/**
 *
 * @author osvaldo.rodriguez
 */
public final class MainFrame extends JFrame {

    /**
     *
     */
    private static final long serialVersionUID = 4699683145704846741L;
    private static Logger logger = LogManager.getLogger(MainFrame.class.getName());
    public Model model = null;
    public UserLoginDialog loginDialog = new UserLoginDialog(this, false);
    public UserRegisterDialog registerDialog = new UserRegisterDialog(this, false);
    public OpenDialog openDialog = null;
    public SaveAsDialog saveAsDialog = null;
    public ImageImportDialog imageImportDialog = null;
    public PageDialog imageDialog = new PageDialog(this, false);
    public CommandDialog commandDialog = new CommandDialog(this, false);
    public SelectionDialog selectionDialog = new SelectionDialog(this, false);
    public ZooniverseExportDialog zooniverseExportDialog = new ZooniverseExportDialog(this, false);
    public ZooniverseImportDialog zooniverseImportDialog = new ZooniverseImportDialog(this, false);
    public MicrosoftExportDialog microsoftExportDialog = new MicrosoftExportDialog(this, false);
    public AudioExportDialog audioExportDialog = new AudioExportDialog(this, false);

    public MainFrame(Model model) {
        this.initComponents();
        this.setModel(model);
        this.initIconImage();
        this.init();
    }

    public void initIconImage() {
        URL url = getClass().getResource("/Icon.png");
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image image = toolkit.createImage(url);
        this.setIconImage(image);
    }

    public void setModel(Model model) {
        logger.debug("setModel(" + model + ")");
        this.model = model;
        //Panel
        this.pagePanel.setModel(this.model);
        this.matrixPanel.setModel(this.model);
        this.tablePanel.setModel(this.model);
        this.archivePanel.setModel(this.model);
        //Dialog
        this.commandDialog.setModel(this.model);
        this.imageDialog.setModel(this.model);
        this.selectionDialog.setModel(this.model);
        this.zooniverseExportDialog.setModel(this.model);
        this.zooniverseImportDialog.setModel(this.model);
        this.microsoftExportDialog.setModel(this.model);
        this.audioExportDialog.setModel(this.model);
        this.registerDialog.setModel(this.model);
        this.registerDialog.setLoginDialog(this.loginDialog);
        this.loginDialog.setModel(this.model);
        this.loginDialog.setRegisterDialog(this.registerDialog);
        if (this.model.system.newUser) {
        	logger.info("New User");
            this.registerDialog.setVisible(true);
        } else if (this.model.system.loginUser) {
            this.loginDialog.setVisible(true);
        }
    }

    public void init() {
        logger.debug("init()");
        this.setTitle("Retina Desktop Application");
        this.pagePanel.setMain(this);
        this.pagePanel.setFocusable(true);
        this.pagePanel.requestFocusInWindow();
        this.imageDialog.init();
        this.selectionDialog.init();
        this.commandDialog.init();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        table1 = new com.meritoki.app.desktop.retina.view.panel.TablePanel();
        imagePageTabbedPane = new javax.swing.JTabbedPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        pagePanel = new com.meritoki.app.desktop.retina.view.panel.PagePanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        matrixPanel = new com.meritoki.app.desktop.retina.view.panel.MatrixPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tablePanel = new com.meritoki.app.desktop.retina.view.panel.TablePanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        archivePanel = new com.meritoki.app.desktop.retina.view.panel.ArchivePanel();
        mainMenuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        loginMenuItem = new javax.swing.JMenuItem();
        newMenuItem = new javax.swing.JMenuItem();
        openMenuItem = new javax.swing.JMenuItem();
        saveMenuItem = new javax.swing.JMenuItem();
        saveAsMenuItem = new javax.swing.JMenuItem();
        importMenu = new javax.swing.JMenu();
        importImageMenuItem = new javax.swing.JMenuItem();
        zooniverseImportMenuItem = new javax.swing.JMenuItem();
        exportMenu = new javax.swing.JMenu();
        zooniverseExportMenuItem = new javax.swing.JMenuItem();
        microsoftExportMenuItem = new javax.swing.JMenuItem();
        audioMenuItem = new javax.swing.JMenuItem();
        editMenu = new javax.swing.JMenu();
        undoMenuItem = new javax.swing.JMenuItem();
        redoMenuItem = new javax.swing.JMenuItem();
        windowMenu = new javax.swing.JMenu();
        dialogMenu = new javax.swing.JMenu();
        pageMenuItem = new javax.swing.JMenuItem();
        selectionMenuItem = new javax.swing.JMenuItem();
        commandMenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout pagePanelLayout = new javax.swing.GroupLayout(pagePanel);
        pagePanel.setLayout(pagePanelLayout);
        pagePanelLayout.setHorizontalGroup(
            pagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1028, Short.MAX_VALUE)
        );
        pagePanelLayout.setVerticalGroup(
            pagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 562, Short.MAX_VALUE)
        );

        jScrollPane1.setViewportView(pagePanel);

        imagePageTabbedPane.addTab("Page", jScrollPane1);

        javax.swing.GroupLayout matrixPanelLayout = new javax.swing.GroupLayout(matrixPanel);
        matrixPanel.setLayout(matrixPanelLayout);
        matrixPanelLayout.setHorizontalGroup(
            matrixPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1028, Short.MAX_VALUE)
        );
        matrixPanelLayout.setVerticalGroup(
            matrixPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 562, Short.MAX_VALUE)
        );

        jScrollPane2.setViewportView(matrixPanel);

        imagePageTabbedPane.addTab("Matrix", jScrollPane2);

        jScrollPane3.setViewportView(tablePanel);

        imagePageTabbedPane.addTab("Table", jScrollPane3);

        jScrollPane4.setViewportView(archivePanel);

        imagePageTabbedPane.addTab("Archive", jScrollPane4);

        fileMenu.setText("File");

        loginMenuItem.setText("Login");
        loginMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loginMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(loginMenuItem);

        newMenuItem.setText("New");
        newMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(newMenuItem);

        openMenuItem.setText("Open");
        openMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(openMenuItem);

        saveMenuItem.setText("Save");
        saveMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(saveMenuItem);

        saveAsMenuItem.setText("Save As");
        saveAsMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveAsMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(saveAsMenuItem);

        importMenu.setText("Import");

        importImageMenuItem.setText("Image");
        importImageMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                importImageMenuItemActionPerformed(evt);
            }
        });
        importMenu.add(importImageMenuItem);

        zooniverseImportMenuItem.setText("Zooniverse");
        zooniverseImportMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                zooniverseImportMenuItemActionPerformed(evt);
            }
        });
        importMenu.add(zooniverseImportMenuItem);

        fileMenu.add(importMenu);

        exportMenu.setText("Export");

        zooniverseExportMenuItem.setText("Zooniverse");
        zooniverseExportMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                zooniverseExportMenuItemActionPerformed(evt);
            }
        });
        exportMenu.add(zooniverseExportMenuItem);

        microsoftExportMenuItem.setText("Microsoft");
        microsoftExportMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                microsoftExportMenuItemActionPerformed(evt);
            }
        });
        exportMenu.add(microsoftExportMenuItem);

        audioMenuItem.setText("Audio");
        audioMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                audioMenuItemActionPerformed(evt);
            }
        });
        exportMenu.add(audioMenuItem);

        fileMenu.add(exportMenu);

        mainMenuBar.add(fileMenu);

        editMenu.setText("Edit");

        undoMenuItem.setText("Undo");
        undoMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                undoMenuItemActionPerformed(evt);
            }
        });
        editMenu.add(undoMenuItem);

        redoMenuItem.setText("Redo");
        redoMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                redoMenuItemActionPerformed(evt);
            }
        });
        editMenu.add(redoMenuItem);

        mainMenuBar.add(editMenu);

        windowMenu.setText("Window");

        dialogMenu.setText("Dialog");

        pageMenuItem.setText("Page");
        pageMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pageMenuItemActionPerformed(evt);
            }
        });
        dialogMenu.add(pageMenuItem);

        selectionMenuItem.setText("Selection");
        selectionMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectionMenuItemActionPerformed(evt);
            }
        });
        dialogMenu.add(selectionMenuItem);

        commandMenuItem.setText("Command");
        commandMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                commandMenuItemActionPerformed(evt);
            }
        });
        dialogMenu.add(commandMenuItem);

        windowMenu.add(dialogMenu);

        mainMenuBar.add(windowMenu);

        setJMenuBar(mainMenuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(imagePageTabbedPane)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(imagePageTabbedPane)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void newMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newMenuItemActionPerformed
        this.model.document = (new Document());
        this.model.system.newDocument = true;
    }//GEN-LAST:event_newMenuItemActionPerformed

    private void saveMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveMenuItemActionPerformed
        if (this.model.system.newDocument) {
            this.saveAsDialog = new com.meritoki.app.desktop.retina.view.dialog.SaveAsDialog(this, false, this.model);
        } else {
            DocumentController.save(model.system.file, this.model.document);
        }

    }//GEN-LAST:event_saveMenuItemActionPerformed

    private void openMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openMenuItemActionPerformed
        this.openDialog = new com.meritoki.app.desktop.retina.view.dialog.OpenDialog(this, false, this.model);
    }//GEN-LAST:event_openMenuItemActionPerformed

    private void pageMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pageMenuItemActionPerformed
        this.imageDialog.setVisible(true);
    }//GEN-LAST:event_pageMenuItemActionPerformed

    private void selectionMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectionMenuItemActionPerformed
        this.selectionDialog.setVisible(true);
    }//GEN-LAST:event_selectionMenuItemActionPerformed

    private void zooniverseExportMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_zooniverseExportMenuItemActionPerformed
        this.zooniverseExportDialog.setVisible(true);
    }//GEN-LAST:event_zooniverseExportMenuItemActionPerformed

    private void saveAsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveAsMenuItemActionPerformed
        this.saveAsDialog = new com.meritoki.app.desktop.retina.view.dialog.SaveAsDialog(this, false, this.model);
    }//GEN-LAST:event_saveAsMenuItemActionPerformed

    private void importImageMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_importImageMenuItemActionPerformed
        this.imageImportDialog = new com.meritoki.app.desktop.retina.view.dialog.image.ImageImportDialog(this, false);
//        this.imageImportDialog.setModel(this.model);
    }//GEN-LAST:event_importImageMenuItemActionPerformed

    private void loginMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loginMenuItemActionPerformed
        this.loginDialog.setVisible(true);
    }//GEN-LAST:event_loginMenuItemActionPerformed

    private void undoMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_undoMenuItemActionPerformed
        this.model.document.pattern.undo();
    }//GEN-LAST:event_undoMenuItemActionPerformed

    private void redoMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_redoMenuItemActionPerformed
        this.model.document.pattern.redo();
    }//GEN-LAST:event_redoMenuItemActionPerformed

    private void zooniverseImportMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_zooniverseImportMenuItemActionPerformed
        this.zooniverseImportDialog.setVisible(true);
    }//GEN-LAST:event_zooniverseImportMenuItemActionPerformed

    private void microsoftExportMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_microsoftExportMenuItemActionPerformed
       this.microsoftExportDialog.setVisible(true);
    }//GEN-LAST:event_microsoftExportMenuItemActionPerformed

    private void audioMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_audioMenuItemActionPerformed
       this.audioExportDialog.setVisible(true);
    }//GEN-LAST:event_audioMenuItemActionPerformed

    private void commandMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_commandMenuItemActionPerformed
       this.commandDialog.setVisible(true);
    }//GEN-LAST:event_commandMenuItemActionPerformed

    /**
     * @param args the command line arguments
     */
//    public static void main(String args[]) {
//        /* Set the Nimbus look and feel */
//        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
//         */
//        logger.info("Retina starting...");
//        final Main main = new Main();
//        Splash splash = new Splash("/splash.png", main, 4000);
//        
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            logger.error(ex);
//        } catch (InstantiationException ex) {
//            logger.error(ex);
//        } catch (IllegalAccessException ex) {
//            logger.error(ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            logger.error(ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                main.setVisible(true);
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.meritoki.app.desktop.retina.view.panel.ArchivePanel archivePanel;
    private javax.swing.JMenuItem audioMenuItem;
    private javax.swing.JMenuItem commandMenuItem;
    private javax.swing.JMenu dialogMenu;
    private javax.swing.JMenu editMenu;
    private javax.swing.JMenu exportMenu;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JTabbedPane imagePageTabbedPane;
    private javax.swing.JMenuItem importImageMenuItem;
    private javax.swing.JMenu importMenu;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JMenuItem loginMenuItem;
    private javax.swing.JMenuBar mainMenuBar;
    private com.meritoki.app.desktop.retina.view.panel.MatrixPanel matrixPanel;
    private javax.swing.JMenuItem microsoftExportMenuItem;
    private javax.swing.JMenuItem newMenuItem;
    private javax.swing.JMenuItem openMenuItem;
    private javax.swing.JMenuItem pageMenuItem;
    private com.meritoki.app.desktop.retina.view.panel.PagePanel pagePanel;
    private javax.swing.JMenuItem redoMenuItem;
    private javax.swing.JMenuItem saveAsMenuItem;
    private javax.swing.JMenuItem saveMenuItem;
    private javax.swing.JMenuItem selectionMenuItem;
    private com.meritoki.app.desktop.retina.view.panel.TablePanel table1;
    private com.meritoki.app.desktop.retina.view.panel.TablePanel tablePanel;
    private javax.swing.JMenuItem undoMenuItem;
    private javax.swing.JMenu windowMenu;
    private javax.swing.JMenuItem zooniverseExportMenuItem;
    private javax.swing.JMenuItem zooniverseImportMenuItem;
    // End of variables declaration//GEN-END:variables
}
