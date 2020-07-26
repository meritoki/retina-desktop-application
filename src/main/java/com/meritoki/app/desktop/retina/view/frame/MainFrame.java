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

import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.meritoki.app.desktop.retina.controller.client.ClientController;
import com.meritoki.app.desktop.retina.controller.document.DocumentController;
import com.meritoki.app.desktop.retina.controller.module.ModuleController;
import com.meritoki.app.desktop.retina.model.Model;
import com.meritoki.app.desktop.retina.model.document.Document;
import com.meritoki.app.desktop.retina.view.dialog.AttributionDialog;
import com.meritoki.app.desktop.retina.view.dialog.CommandDialog;
import com.meritoki.app.desktop.retina.view.dialog.OpenDialog;
import com.meritoki.app.desktop.retina.view.dialog.PageDialog;
import com.meritoki.app.desktop.retina.view.dialog.PropertyDialog;
import com.meritoki.app.desktop.retina.view.dialog.RecognitionDialog;
import com.meritoki.app.desktop.retina.view.dialog.SaveAsDialog;
import com.meritoki.app.desktop.retina.view.dialog.ShapeDialog;
import com.meritoki.app.desktop.retina.view.dialog.ShareDialog;
import com.meritoki.app.desktop.retina.view.dialog.audio.AudioExportDialog;
import com.meritoki.app.desktop.retina.view.dialog.image.ImageImportDialog;
import com.meritoki.app.desktop.retina.view.dialog.microsoft.MicrosoftExportDialog;
import com.meritoki.app.desktop.retina.view.dialog.user.UserLoginDialog;
import com.meritoki.app.desktop.retina.view.dialog.user.UserRegisterDialog;
import com.meritoki.app.desktop.retina.view.dialog.zooniverse.ZooniverseCSVImportDialog;
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
	public UserRegisterDialog registerDialog = new UserRegisterDialog(this, false);
	public UserLoginDialog loginDialog = new UserLoginDialog(this, false);
	public OpenDialog openDialog = null;
	public SaveAsDialog saveAsDialog = null;
	public PageDialog pageDialog = new PageDialog(this, false);
	public ShapeDialog shapeDialog = new ShapeDialog(this, false);
	public RecognitionDialog recognitionDialog = new RecognitionDialog(this, false);
	public CommandDialog commandDialog = new CommandDialog(this, false);
	public AttributionDialog attributionDialog = new AttributionDialog(this, false);
	public ImageImportDialog imageImportDialog = null;
	public ZooniverseExportDialog zooniverseExportDialog = new ZooniverseExportDialog(this, false);
	public ZooniverseImportDialog zooniverseImportDialog = new ZooniverseImportDialog(this, false);
	public ZooniverseCSVImportDialog zooniverseCSVImportDialog = null;
	public MicrosoftExportDialog microsoftExportDialog = new MicrosoftExportDialog(this, false);
	public AudioExportDialog audioExportDialog = new AudioExportDialog(this, false);
	public PropertyDialog propertyDialog = new PropertyDialog(this, false);
	public ShareDialog shareDialog = new ShareDialog(this, false);
	public ModuleController moduleController;
	public JMenuItem shareMenuItem;

	public MainFrame(Model model) {
		this.initComponents();
		this.setTitle("Retina Desktop Application");
		this.setModel(model);
		this.setMainFrame();
		this.initIconImage();
		this.init();
		this.pagePanel.setFocusable(true);
		this.pagePanel.requestFocusInWindow();
	}

	public void initIconImage() {
		URL url = getClass().getResource("/Icon.png");
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Image image = toolkit.createImage(url);
		this.setIconImage(image);
	}

	public void setMainFrame() {
		this.pagePanel.setMainFrame(this);
	}

	public void setModel(Model model) {
		logger.debug("setModel(" + model + ")");
		this.model = model;
		this.moduleController = new ModuleController(this.model);
		this.pagePanel.setModel(this.model);
		this.matrixPanel.setModel(this.model);
		this.tablePanel.setModel(this.model);
		this.archivePanel.setModel(this.model);
		this.attributionDialog.setModel(this.model);
		this.commandDialog.setModel(this.model);
		this.pageDialog.setModel(this.model);
		this.shapeDialog.setModel(this.model);
		this.zooniverseExportDialog.setModel(this.model);
		this.zooniverseImportDialog.setModel(this.model);
		this.microsoftExportDialog.setModel(this.model);
		this.audioExportDialog.setModel(this.model);
		this.registerDialog.setModel(this.model);
		this.registerDialog.setLoginDialog(this.loginDialog);
		this.loginDialog.setModel(this.model);
		this.propertyDialog.setModel(this.model);
		this.loginDialog.setRegisterDialog(this.registerDialog);
		if (this.model.system.newUser) {
//			this.registerDialog.setVisible(true);
		} else if (this.model.system.loginUser) {
//			this.loginDialog.setVisible(true);
		}
		this.shapeDialog.setVisible(true);
//		this.pageDialog.setVisible(true);
	}

	public void init() {
		logger.info("init()");
		this.repaint();
		this.pagePanel.init();
		this.matrixPanel.init();
		this.tablePanel.init();
		this.archivePanel.init();
		this.pageDialog.init();
		this.shapeDialog.init();
		this.commandDialog.init();
		this.attributionDialog.init();
		if (this.model.system.loggedIn) {
			this.logInOutMenuItem.setText("Logout");
		} else {
			this.logInOutMenuItem.setText("Login");
		}
		this.initMenu();
	}

	public void initMenu() {
		if (this.model.system.isConnected) {
			List<String> uuidList = this.model.getDocumentList();
			this.openRecentMenu.removeAll();
			Iterator<String> uuidIterator = uuidList.iterator();
			while (uuidIterator.hasNext()) {
				String uuid = uuidIterator.next();
				JMenuItem recentMenuItem = new javax.swing.JMenuItem();
				recentMenuItem.setText(uuid);
				recentMenuItem.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						model.openDocument(uuid);
						init();
					}
				});
				this.openRecentMenu.add(recentMenuItem);
			}
			this.shareMenuItem = new JMenuItem();
			this.shareMenuItem.setFont(this.shareMenuItem.getFont().deriveFont(Font.BOLD));
			this.shareMenuItem.setText("Share");
			this.shareMenuItem.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					shareMenuItemActionPerformed(evt);
				}
			});
			this.fileMenu.add(this.shareMenuItem);
		} else {
			List<String> recentList = this.model.resource.recentList;
			this.openRecentMenu.removeAll();
			Iterator<String> recentIterator = recentList.iterator();
			while (recentIterator.hasNext()) {
				String recent = recentIterator.next();
				File file = new File(recent);
				if (file.exists()) {
					JMenuItem recentMenuItem = new javax.swing.JMenuItem();
					recentMenuItem.setText(file.getName());
					recentMenuItem.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent evt) {
							model.openDocument(file);
							init();
						}
					});
					this.openRecentMenu.add(recentMenuItem);
				} else {
					this.model.resource.removeRecent(recent);
				}
			}
		}
	}
	
	private void shareMenuItemActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_pageMenuItemActionPerformed
		this.shareDialog.setVisible(true);
	}// GEN-LAST:event_pageMenuItemActionPerformed
	

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
	// <editor-fold defaultstate="collapsed" desc="Generated
	// <editor-fold defaultstate="collapsed" desc="Generated
	// <editor-fold defaultstate="collapsed" desc="Generated
	// Code">//GEN-BEGIN:initComponents
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
		connectMenuItem = new javax.swing.JMenuItem();
		logInOutMenuItem = new javax.swing.JMenuItem();
		newMenuItem = new javax.swing.JMenuItem();
		openMenuItem = new javax.swing.JMenuItem();
		openRecentMenu = new javax.swing.JMenu();
		saveMenuItem = new javax.swing.JMenuItem();
		saveAsMenuItem = new javax.swing.JMenuItem();
		importMenu = new javax.swing.JMenu();
		importImageMenuItem = new javax.swing.JMenuItem();
		zooniverseImportMenuItem = new javax.swing.JMenuItem();
		zooniverseCSVImportMenuItem = new javax.swing.JMenuItem();
		exportMenu = new javax.swing.JMenu();
		zooniverseExportMenuItem = new javax.swing.JMenuItem();
		microsoftExportMenuItem = new javax.swing.JMenuItem();
		audioMenuItem = new javax.swing.JMenuItem();
		editMenu = new javax.swing.JMenu();
		undoMenuItem = new javax.swing.JMenuItem();
		redoMenuItem = new javax.swing.JMenuItem();
		jMenu1 = new javax.swing.JMenu();
		recognitionMenu = new javax.swing.JMenu();
		recognitionStartMenuItem = new javax.swing.JMenuItem();
		recognitionStopMenuItem = new javax.swing.JMenuItem();
		windowMenu = new javax.swing.JMenu();
		dialogMenu = new javax.swing.JMenu();
		pageMenuItem = new javax.swing.JMenuItem();
		selectionMenuItem = new javax.swing.JMenuItem();
		recognitionMenuItem = new javax.swing.JMenuItem();
		commandMenuItem = new javax.swing.JMenuItem();
		attributionMenuItem = new javax.swing.JMenuItem();
		propertyMenuItem = new javax.swing.JMenuItem();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

		javax.swing.GroupLayout pagePanelLayout = new javax.swing.GroupLayout(pagePanel);
		pagePanel.setLayout(pagePanelLayout);
		pagePanelLayout.setHorizontalGroup(pagePanelLayout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 1028, Short.MAX_VALUE));
		pagePanelLayout.setVerticalGroup(pagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGap(0, 562, Short.MAX_VALUE));

		jScrollPane1.setViewportView(pagePanel);

		imagePageTabbedPane.addTab("Page", jScrollPane1);

		javax.swing.GroupLayout matrixPanelLayout = new javax.swing.GroupLayout(matrixPanel);
		matrixPanel.setLayout(matrixPanelLayout);
		matrixPanelLayout.setHorizontalGroup(matrixPanelLayout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 1028, Short.MAX_VALUE));
		matrixPanelLayout.setVerticalGroup(matrixPanelLayout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 562, Short.MAX_VALUE));

		jScrollPane2.setViewportView(matrixPanel);

		imagePageTabbedPane.addTab("Matrix", jScrollPane2);

		jScrollPane3.setViewportView(tablePanel);

		imagePageTabbedPane.addTab("Table", jScrollPane3);

		jScrollPane4.setViewportView(archivePanel);

		imagePageTabbedPane.addTab("Archive", jScrollPane4);

		fileMenu.setText("File");

		connectMenuItem.setText("Connect");
		connectMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				connectMenuItemActionPerformed(evt);
			}
		});
		fileMenu.add(connectMenuItem);

		logInOutMenuItem.setText("Login");
		logInOutMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				logInOutMenuItemActionPerformed(evt);
			}
		});
		fileMenu.add(logInOutMenuItem);

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

		openRecentMenu.setText("Open Recent");
		fileMenu.add(openRecentMenu);

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

		zooniverseCSVImportMenuItem.setText("Zooniverse CSV");
		zooniverseCSVImportMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				zooniverseCSVImportMenuItemActionPerformed(evt);
			}
		});
		importMenu.add(zooniverseCSVImportMenuItem);

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

		jMenu1.setText("Run");

		recognitionMenu.setText("Recognition");

		recognitionStartMenuItem.setText("Start");
		recognitionStartMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				recognitionStartMenuItemActionPerformed(evt);
			}
		});
		recognitionMenu.add(recognitionStartMenuItem);

		recognitionStopMenuItem.setText("Stop");
		recognitionStopMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				recognitionStopMenuItemActionPerformed(evt);
			}
		});
		recognitionMenu.add(recognitionStopMenuItem);

		jMenu1.add(recognitionMenu);

		mainMenuBar.add(jMenu1);

		windowMenu.setText("Window");

		dialogMenu.setText("Dialog");

		pageMenuItem.setText("Page");
		pageMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				pageMenuItemActionPerformed(evt);
			}
		});
		dialogMenu.add(pageMenuItem);

		selectionMenuItem.setText("Shape");
		selectionMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				selectionMenuItemActionPerformed(evt);
			}
		});
		dialogMenu.add(selectionMenuItem);

		recognitionMenuItem.setText("Recognition");
		recognitionMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				recognitionMenuItemActionPerformed(evt);
			}
		});
		dialogMenu.add(recognitionMenuItem);

		commandMenuItem.setText("Command");
		commandMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				commandMenuItemActionPerformed(evt);
			}
		});
		dialogMenu.add(commandMenuItem);

		attributionMenuItem.setText("Attribution");
		attributionMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				attributionMenuItemActionPerformed(evt);
			}
		});
		dialogMenu.add(attributionMenuItem);

		propertyMenuItem.setText("Property");
		propertyMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				propertyMenuItemActionPerformed(evt);
			}
		});
		dialogMenu.add(propertyMenuItem);

		windowMenu.add(dialogMenu);

		mainMenuBar.add(windowMenu);

		setJMenuBar(mainMenuBar);

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addComponent(imagePageTabbedPane));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addComponent(imagePageTabbedPane));

		pack();
	}// </editor-fold>//GEN-END:initComponents

	private void zooniverseCSVImportMenuItemActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_zooniverseCSVImportMenuItemActionPerformed
		this.zooniverseCSVImportDialog = new com.meritoki.app.desktop.retina.view.dialog.zooniverse.ZooniverseCSVImportDialog(
				this, false, this.model);
	}// GEN-LAST:event_zooniverseCSVImportMenuItemActionPerformed

	private void connectMenuItemActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_connectMenuItemActionPerformed
		ClientController clientController = new ClientController(this.model);
		boolean fileFlag = clientController.fileClient.checkHealth();
		boolean userFlag = clientController.userClient.checkHealth();
		boolean retinaFlag = clientController.retinaClient.checkHealth();
		if (fileFlag && userFlag && retinaFlag) {
			this.model.system.isConnected = true;
			JOptionPane.showMessageDialog(this, "Connected", "Message", JOptionPane.INFORMATION_MESSAGE);
			this.init();
		} else {
			JOptionPane.showMessageDialog(this, "Services Unavailable", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}// GEN-LAST:event_connectMenuItemActionPerformed

	private void attributionMenuItemActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_attributionMenuItemActionPerformed
		this.attributionDialog.setVisible(true);
	}// GEN-LAST:event_attributionMenuItemActionPerformed

	private void recognitionStartMenuItemActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_recognitionStartMenuItemActionPerformed
		this.moduleController = new ModuleController(this.model);
		this.moduleController.start();
	}// GEN-LAST:event_recognitionStartMenuItemActionPerformed

	private void recognitionStopMenuItemActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_recognitionStopMenuItemActionPerformed
		this.moduleController.stop();
	}// GEN-LAST:event_recognitionStopMenuItemActionPerformed

	private void recognitionMenuItemActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_recognitionMenuItemActionPerformed
		this.recognitionDialog.setVisible(true);
	}// GEN-LAST:event_recognitionMenuItemActionPerformed

	private void propertyMenuItemActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_propertyMenuItemActionPerformed
		this.propertyDialog.setVisible(true);
	}// GEN-LAST:event_propertyMenuItemActionPerformed

	private void newMenuItemActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_newMenuItemActionPerformed
		this.model.document = new Document();
		this.model.system.newDocument = true;
//		this.model.document.pattern.user = this.model.system.user;
		this.init();
	}// GEN-LAST:event_newMenuItemActionPerformed

	private void saveMenuItemActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_saveMenuItemActionPerformed
		if (this.model.system.newDocument) {
			this.saveAsDialog = new com.meritoki.app.desktop.retina.view.dialog.SaveAsDialog(this, false, this.model);
		} else {
			this.model.saveDocument();
			this.init();
		}

	}// GEN-LAST:event_saveMenuItemActionPerformed

	private void openMenuItemActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_openMenuItemActionPerformed
		this.openDialog = new com.meritoki.app.desktop.retina.view.dialog.OpenDialog(this, false, this.model);
	}// GEN-LAST:event_openMenuItemActionPerformed

	private void pageMenuItemActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_pageMenuItemActionPerformed
		this.pageDialog.setVisible(true);
	}// GEN-LAST:event_pageMenuItemActionPerformed

	private void selectionMenuItemActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_selectionMenuItemActionPerformed
		this.shapeDialog.setVisible(true);
	}// GEN-LAST:event_selectionMenuItemActionPerformed

	private void zooniverseExportMenuItemActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_zooniverseExportMenuItemActionPerformed
		this.zooniverseExportDialog.setVisible(true);
	}// GEN-LAST:event_zooniverseExportMenuItemActionPerformed

	private void saveAsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_saveAsMenuItemActionPerformed
		this.saveAsDialog = new com.meritoki.app.desktop.retina.view.dialog.SaveAsDialog(this, false, this.model);
	}// GEN-LAST:event_saveAsMenuItemActionPerformed

	private void importImageMenuItemActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_importImageMenuItemActionPerformed
		this.imageImportDialog = new com.meritoki.app.desktop.retina.view.dialog.image.ImageImportDialog(this, false,
				this.model);
//        this.imageImportDialog.setModel(this.model);
	}// GEN-LAST:event_importImageMenuItemActionPerformed

	private void logInOutMenuItemActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_loginMenuItemActionPerformed
		if (!this.model.system.loggedIn) {
			this.loginDialog.setVisible(true);
		} else {
			this.model.logoutUser();
			this.init();
		}
	}// GEN-LAST:event_loginMenuItemActionPerformed

	private void undoMenuItemActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_undoMenuItemActionPerformed
		this.model.pattern.undo();
	}// GEN-LAST:event_undoMenuItemActionPerformed

	private void redoMenuItemActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_redoMenuItemActionPerformed
		this.model.pattern.redo();
	}// GEN-LAST:event_redoMenuItemActionPerformed

	private void zooniverseImportMenuItemActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_zooniverseImportMenuItemActionPerformed
		this.zooniverseImportDialog.setVisible(true);
	}// GEN-LAST:event_zooniverseImportMenuItemActionPerformed

	private void microsoftExportMenuItemActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_microsoftExportMenuItemActionPerformed
		this.microsoftExportDialog.setVisible(true);
	}// GEN-LAST:event_microsoftExportMenuItemActionPerformed

	private void audioMenuItemActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_audioMenuItemActionPerformed
		this.audioExportDialog.setVisible(true);
	}// GEN-LAST:event_audioMenuItemActionPerformed

	private void commandMenuItemActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_commandMenuItemActionPerformed
		this.commandDialog.setVisible(true);
	}// GEN-LAST:event_commandMenuItemActionPerformed

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
	private javax.swing.JMenuItem attributionMenuItem;
	private javax.swing.JMenuItem audioMenuItem;
	private javax.swing.JMenuItem commandMenuItem;
	private javax.swing.JMenuItem connectMenuItem;
	private javax.swing.JMenu dialogMenu;
	private javax.swing.JMenu editMenu;
	private javax.swing.JMenu exportMenu;
	private javax.swing.JMenu fileMenu;
	private javax.swing.JTabbedPane imagePageTabbedPane;
	private javax.swing.JMenuItem importImageMenuItem;
	private javax.swing.JMenu importMenu;
	private javax.swing.JMenu jMenu1;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JScrollPane jScrollPane2;
	private javax.swing.JScrollPane jScrollPane3;
	private javax.swing.JScrollPane jScrollPane4;
	private javax.swing.JMenuItem logInOutMenuItem;
	private javax.swing.JMenuBar mainMenuBar;
	private com.meritoki.app.desktop.retina.view.panel.MatrixPanel matrixPanel;
	private javax.swing.JMenuItem microsoftExportMenuItem;
	private javax.swing.JMenuItem newMenuItem;
	private javax.swing.JMenuItem openMenuItem;
	private javax.swing.JMenu openRecentMenu;
	private javax.swing.JMenuItem pageMenuItem;
	private com.meritoki.app.desktop.retina.view.panel.PagePanel pagePanel;
	private javax.swing.JMenuItem propertyMenuItem;
	private javax.swing.JMenu recognitionMenu;
	private javax.swing.JMenuItem recognitionMenuItem;
	private javax.swing.JMenuItem recognitionStartMenuItem;
	private javax.swing.JMenuItem recognitionStopMenuItem;
	private javax.swing.JMenuItem redoMenuItem;
	private javax.swing.JMenuItem saveAsMenuItem;
	private javax.swing.JMenuItem saveMenuItem;
	private javax.swing.JMenuItem selectionMenuItem;
	private com.meritoki.app.desktop.retina.view.panel.TablePanel table1;
	private com.meritoki.app.desktop.retina.view.panel.TablePanel tablePanel;
	private javax.swing.JMenuItem undoMenuItem;
	private javax.swing.JMenu windowMenu;
	private javax.swing.JMenuItem zooniverseCSVImportMenuItem;
	private javax.swing.JMenuItem zooniverseExportMenuItem;
	private javax.swing.JMenuItem zooniverseImportMenuItem;
	// End of variables declaration//GEN-END:variables
}
