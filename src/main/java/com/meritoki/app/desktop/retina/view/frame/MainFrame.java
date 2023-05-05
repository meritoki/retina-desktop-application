/*
 * Copyright 2021 Joaquin Osvaldo Rodriguez
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
import com.meritoki.app.desktop.retina.model.Model;
import com.meritoki.app.desktop.retina.model.provider.Provider;
import com.meritoki.app.desktop.retina.model.provider.meritoki.Meritoki;
import com.meritoki.app.desktop.retina.model.vendor.google.Google;
import com.meritoki.app.desktop.retina.view.dialog.AttributionDialog;
import com.meritoki.app.desktop.retina.view.dialog.CommandDialog;

import com.meritoki.app.desktop.retina.view.dialog.OpenDialog;
import com.meritoki.app.desktop.retina.view.dialog.PageDialog;
import com.meritoki.app.desktop.retina.view.dialog.PropertyDialog;
import com.meritoki.app.desktop.retina.view.dialog.SaveAsDialog;
import com.meritoki.app.desktop.retina.view.dialog.ShapeDialog;
import com.meritoki.app.desktop.retina.view.dialog.ShareDialog;
import com.meritoki.app.desktop.retina.view.dialog.ToolDialog;
import com.meritoki.app.desktop.retina.view.dialog.audio.AudioExportDialog;
import com.meritoki.app.desktop.retina.view.dialog.image.ImageImportDialog;
import com.meritoki.app.desktop.retina.view.dialog.meritoki.MeritokiExportDialog;
import com.meritoki.app.desktop.retina.view.dialog.meritoki.MeritokiImportDialog;
import com.meritoki.app.desktop.retina.view.dialog.meritoki.MeritokiControlDialog;
import com.meritoki.app.desktop.retina.view.dialog.microsoft.MicrosoftExportDialog;
import com.meritoki.app.desktop.retina.view.dialog.project.ProjectExportDialog;
import com.meritoki.app.desktop.retina.view.dialog.project.ProjectImportDialog;
import com.meritoki.app.desktop.retina.view.dialog.user.UserLoginDialog;
import com.meritoki.app.desktop.retina.view.dialog.user.UserRegisterDialog;
import com.meritoki.app.desktop.retina.view.dialog.zooniverse.ZooniverseCSVImportDialog;
import com.meritoki.app.desktop.retina.view.dialog.zooniverse.ZooniverseExportDialog;
import com.meritoki.app.desktop.retina.view.dialog.zooniverse.ZooniverseImportDialog;
import com.meritoki.app.desktop.retina.view.panel.meritoki.MachinePanel;

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
	public ToolDialog toolDialog = new ToolDialog(this,false);
	public CommandDialog commandDialog = new CommandDialog(this, false);
	public AttributionDialog attributionDialog = new AttributionDialog(this, false);
	public ImageImportDialog imageImportDialog = null;
	public ProjectImportDialog projectImportDialog = null;
	public ProjectExportDialog projectExportDialog = null;
	public ZooniverseExportDialog zooniverseExportDialog = new ZooniverseExportDialog(this, false);
	public ZooniverseImportDialog zooniverseImportDialog = new ZooniverseImportDialog(this, false);
	public ZooniverseCSVImportDialog zooniverseCSVImportDialog = null;
	public MicrosoftExportDialog microsoftExportDialog = new MicrosoftExportDialog(this, false);
	public MeritokiExportDialog meritokiExportDialog = new MeritokiExportDialog(this,false);
	public MeritokiImportDialog meritokiImportDialog = new MeritokiImportDialog(this,false);
	public MeritokiControlDialog meritokiControlDialog = new MeritokiControlDialog(this,false);
	public AudioExportDialog audioExportDialog = new AudioExportDialog(this, false);
	public PropertyDialog propertyDialog = new PropertyDialog(this, false);
	public ShareDialog shareDialog = new ShareDialog(this, false);
	
	public JMenuItem shareMenuItem;

	public MainFrame(Model model) {
		this.initComponents();
		logger.info("MainFrame("+model+")");
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
		this.tablePanel.setMainFrame(this);
		this.machinePanel.setMainFrame(this);
	}

	public void setModel(Model model) {
		logger.debug("setModel(" + model + ")");
		this.model = model;
		this.pagePanel.setModel(this.model);
		this.tablePanel.setModel(this.model);
		this.archivePanel.setModel(this.model);
		this.toolDialog.setModel(this.model);
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
			this.registerDialog.setVisible(true);
		} else if (this.model.system.loginUser) {
			this.loginDialog.setVisible(true);
		}
		this.toolDialog.setVisible(true);
		this.shapeDialog.setVisible(false);
		this.pageDialog.setVisible(false); 
		if(this.model.system.machine) {
			this.meritokiControlDialog.setModel(this.model);
			this.meritokiControlDialog.setVisible(true);
			this.machinePanel.setModel(this.model);
			this.mainTabbedPane.setSelectedIndex(this.mainTabbedPane.getTabCount()-1);
		} else {
			this.mainTabbedPane.remove(this.machineScrollPane);
		}
	}

	public void init() {
		logger.debug("init()");
		if(this.model.system.file != null) {
			this.setTitle("Retina Desktop Application ("+this.model.system.file.getName()+")");
		} else {
			this.setTitle("Retina Desktop Application");
		}
		this.repaint();
		this.pagePanel.init();
		this.tablePanel.init();
		this.archivePanel.init();
		this.machinePanel.init();
		this.pageDialog.init();
		this.shapeDialog.init();
		this.toolDialog.init();
		this.commandDialog.init();
		this.attributionDialog.init();
		if(this.model.system.machine)
			this.meritokiControlDialog.init();
		if (this.model.system.loggedIn) {
			this.logInOutMenuItem.setText("Logout");
		} else {
			this.logInOutMenuItem.setText("Login");
		}
		this.initMenu();
	}

	public void initMenu() {
		if (this.model.system.multiUser && this.model.system.isConnected) {
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
			if(!this.model.system.machine) {
				this.mainMenuBar.remove(this.providerMenu);
				this.exportMenu.remove(this.exportMeritokiMenuItem);
				this.importMenu.remove(this.importMeritokiMenuItem);
			}
			Provider zooniverse = this.model.system.providerMap.get("zooniverse");
			if(zooniverse != null) {
			try {
				if(!zooniverse.isAvailable()) {
					this.exportMenu.remove(this.zooniverseExportMenuItem);
					this.importMenu.remove(this.zooniverseImportMenuItem);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
			this.fileMenu.remove(this.connectMenuItem);
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
					//this.model.resource.removeRecent(recent);
					recentIterator.remove();
					this.model.resource.save();
				}
			}
		}
	}
	

	
	public MachinePanel getMachinePanel() {
		return this.machinePanel;
	}
	
	private void shareMenuItemActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_pageMenuItemActionPerformed
		this.shareDialog.setVisible(true);
	}// GEN-LAST:event_pageMenuItemActionPerformed
	
	public void save() {
		if (this.model.system.newDocument) {
			this.saveAsDialog = new com.meritoki.app.desktop.retina.view.dialog.SaveAsDialog(this, false, this.model);
		} else {
			this.model.saveDocument();
			this.init();
		}
	}
	
//	this.setSize(1024, 512);

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
	// <editor-fold defaultstate="collapsed" desc="Generated
	// <editor-fold defaultstate="collapsed" desc="Generated
	// <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        table1 = new com.meritoki.app.desktop.retina.view.panel.TablePanel();
        mainTabbedPane = new javax.swing.JTabbedPane();
        pageScrollPane = new javax.swing.JScrollPane();
        pagePanel = new com.meritoki.app.desktop.retina.view.panel.PagePanel();
        tableScrollPane = new javax.swing.JScrollPane();
        tablePanel = new com.meritoki.app.desktop.retina.view.panel.TablePanel();
        archiveScrollPane = new javax.swing.JScrollPane();
        archivePanel = new com.meritoki.app.desktop.retina.view.panel.ArchivePanel();
        machineScrollPane = new javax.swing.JScrollPane();
        machinePanel = new com.meritoki.app.desktop.retina.view.panel.meritoki.MachinePanel();
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
        importProjectMenuItem = new javax.swing.JMenuItem();
        zooniverseImportMenuItem = new javax.swing.JMenuItem();
        zooniverseCSVImportMenuItem = new javax.swing.JMenuItem();
        importMeritokiMenuItem = new javax.swing.JMenuItem();
        exportMenu = new javax.swing.JMenu();
        exportProjectMenuItem = new javax.swing.JMenuItem();
        zooniverseExportMenuItem = new javax.swing.JMenuItem();
        microsoftExportMenuItem = new javax.swing.JMenuItem();
        audioMenuItem = new javax.swing.JMenuItem();
        exportMeritokiMenuItem = new javax.swing.JMenuItem();
        editMenu = new javax.swing.JMenu();
        undoMenuItem = new javax.swing.JMenuItem();
        redoMenuItem = new javax.swing.JMenuItem();
        providerMenu = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();
        meritokiDialogMenu = new javax.swing.JMenu();
        controlMenuItem = new javax.swing.JMenuItem();
        startMeritokiMenuItem = new javax.swing.JMenuItem();
        stopMeritokiMenuItem = new javax.swing.JMenuItem();
        resetMeritokiMenuItem = new javax.swing.JMenuItem();
        vendorMenu = new javax.swing.JMenu();
        googleVendorMenu = new javax.swing.JMenu();
        tesseractGoogleMenu = new javax.swing.JMenu();
        executeTesseractMenuItem = new javax.swing.JMenuItem();
        windowMenu = new javax.swing.JMenu();
        dialogMenu = new javax.swing.JMenu();
        toolMenuItem = new javax.swing.JMenuItem();
        pageMenuItem = new javax.swing.JMenuItem();
        selectionMenuItem = new javax.swing.JMenuItem();
        commandMenuItem = new javax.swing.JMenuItem();
        attributionMenuItem = new javax.swing.JMenuItem();
        propertyMenuItem = new javax.swing.JMenuItem();

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

        pageScrollPane.setViewportView(pagePanel);

        mainTabbedPane.addTab("Page", pageScrollPane);

        tableScrollPane.setViewportView(tablePanel);

        mainTabbedPane.addTab("Table", tableScrollPane);

        archiveScrollPane.setViewportView(archivePanel);

        mainTabbedPane.addTab("Archive", archiveScrollPane);

        javax.swing.GroupLayout machinePanelLayout = new javax.swing.GroupLayout(machinePanel);
        machinePanel.setLayout(machinePanelLayout);
        machinePanelLayout.setHorizontalGroup(
            machinePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1028, Short.MAX_VALUE)
        );
        machinePanelLayout.setVerticalGroup(
            machinePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 562, Short.MAX_VALUE)
        );

        machineScrollPane.setViewportView(machinePanel);

        mainTabbedPane.addTab("Machine", machineScrollPane);

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

        openRecentMenu.setText("Recent");
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

        importProjectMenuItem.setText("Project");
        importProjectMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                importProjectMenuItemActionPerformed(evt);
            }
        });
        importMenu.add(importProjectMenuItem);

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

        importMeritokiMenuItem.setText("Meritoki");
        importMeritokiMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                importMeritokiMenuItemActionPerformed(evt);
            }
        });
        importMenu.add(importMeritokiMenuItem);

        fileMenu.add(importMenu);

        exportMenu.setText("Export");

        exportProjectMenuItem.setText("Project");
        exportProjectMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportProjectMenuItemActionPerformed(evt);
            }
        });
        exportMenu.add(exportProjectMenuItem);

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

        exportMeritokiMenuItem.setText("Meritoki");
        exportMeritokiMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportMeritokiMenuItemActionPerformed(evt);
            }
        });
        exportMenu.add(exportMeritokiMenuItem);

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

        providerMenu.setText("Provider");

        jMenu2.setText("Meritoki");

        meritokiDialogMenu.setText("Dialog");

        controlMenuItem.setText("Control");
        controlMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                controlMenuItemActionPerformed(evt);
            }
        });
        meritokiDialogMenu.add(controlMenuItem);

        jMenu2.add(meritokiDialogMenu);

        startMeritokiMenuItem.setText("Start");
        startMeritokiMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startMeritokiMenuItemActionPerformed(evt);
            }
        });
        jMenu2.add(startMeritokiMenuItem);

        stopMeritokiMenuItem.setText("Stop");
        stopMeritokiMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stopMeritokiMenuItemActionPerformed(evt);
            }
        });
        jMenu2.add(stopMeritokiMenuItem);

        resetMeritokiMenuItem.setText("Reset");
        resetMeritokiMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetMeritokiMenuItemActionPerformed(evt);
            }
        });
        jMenu2.add(resetMeritokiMenuItem);

        providerMenu.add(jMenu2);

        mainMenuBar.add(providerMenu);

        vendorMenu.setText("Vendor");

        googleVendorMenu.setText("Google");

        tesseractGoogleMenu.setText("Tesseract");

        executeTesseractMenuItem.setText("Execute");
        executeTesseractMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                executeTesseractMenuItemActionPerformed(evt);
            }
        });
        tesseractGoogleMenu.add(executeTesseractMenuItem);

        googleVendorMenu.add(tesseractGoogleMenu);

        vendorMenu.add(googleVendorMenu);

        mainMenuBar.add(vendorMenu);

        windowMenu.setText("Window");

        dialogMenu.setText("Dialog");

        toolMenuItem.setText("Tool");
        toolMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toolMenuItemActionPerformed(evt);
            }
        });
        dialogMenu.add(toolMenuItem);

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
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainTabbedPane)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainTabbedPane)
        );

        this.setSize(1024, 512);
    }// </editor-fold>//GEN-END:initComponents

    private void exportMeritokiMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportMeritokiMenuItemActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_exportMeritokiMenuItemActionPerformed

    private void importMeritokiMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_importMeritokiMenuItemActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_importMeritokiMenuItemActionPerformed

    private void startMeritokiMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startMeritokiMenuItemActionPerformed
    	Meritoki meritoki = (Meritoki)this.model.system.providerMap.get("meritoki");
    	meritoki.start();
    }//GEN-LAST:event_startMeritokiMenuItemActionPerformed

    private void stopMeritokiMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stopMeritokiMenuItemActionPerformed
    	Meritoki meritoki = (Meritoki)this.model.system.providerMap.get("meritoki");
    	meritoki.stop();
    }//GEN-LAST:event_stopMeritokiMenuItemActionPerformed

    private void resetMeritokiMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetMeritokiMenuItemActionPerformed
        Meritoki meritoki = (Meritoki)this.model.system.providerMap.get("meritoki");
        if(meritoki != null) {
        	meritoki.reset();
        }
        this.init();
    }//GEN-LAST:event_resetMeritokiMenuItemActionPerformed

    private void controlMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_controlMenuItemActionPerformed
        this.meritokiControlDialog.setVisible(true);
    }//GEN-LAST:event_controlMenuItemActionPerformed

    private void toolMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toolMenuItemActionPerformed
        this.toolDialog.setVisible(true);
    }//GEN-LAST:event_toolMenuItemActionPerformed

    private void importProjectMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_importProjectMenuItemActionPerformed
        this.projectImportDialog = new ProjectImportDialog(this, false, this.model);
    }//GEN-LAST:event_importProjectMenuItemActionPerformed

    private void exportProjectMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportProjectMenuItemActionPerformed
    	this.projectExportDialog = new ProjectExportDialog(this, false, this.model);
    }//GEN-LAST:event_exportProjectMenuItemActionPerformed

    private void executeTesseractMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_executeTesseractMenuItemActionPerformed
        Google google = (Google)this.model.system.vendorMap.get("google");
        google.setModel(this.model);
        google.setMainFrame(this);
        google.setProduct("Tesseract");
        try {
        	google.execute();
        } catch(Exception e) {
        	e.printStackTrace();
        	JOptionPane.showMessageDialog(this,e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_executeTesseractMenuItemActionPerformed

	private void zooniverseCSVImportMenuItemActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_zooniverseCSVImportMenuItemActionPerformed
		this.zooniverseCSVImportDialog = new com.meritoki.app.desktop.retina.view.dialog.zooniverse.ZooniverseCSVImportDialog(
				this, false, this.model);
	}// GEN-LAST:event_zooniverseCSVImportMenuItemActionPerformed

	private void connectMenuItemActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_connectMenuItemActionPerformed
		if(this.model.system.multiUser) {
			ClientController clientController = new ClientController(this.model);
			boolean fileFlag = false;//clientController.fileClient.checkHealth();
			boolean userFlag = false;//clientController.userClient.checkHealth();
			boolean retinaFlag = false;//clientController.retinaClient.checkHealth();
			if (fileFlag && userFlag && retinaFlag) {
				this.model.system.isConnected = true;
				JOptionPane.showMessageDialog(this, "Connected", "Message", JOptionPane.INFORMATION_MESSAGE);
				this.init();
			} else {
				JOptionPane.showMessageDialog(this, "Services Unavailable", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}// GEN-LAST:event_connectMenuItemActionPerformed

	private void attributionMenuItemActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_attributionMenuItemActionPerformed
		this.attributionDialog.setVisible(true);
	}// GEN-LAST:event_attributionMenuItemActionPerformed

	private void propertyMenuItemActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_propertyMenuItemActionPerformed
		this.propertyDialog.setVisible(true);
	}// GEN-LAST:event_propertyMenuItemActionPerformed

	private void newMenuItemActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_newMenuItemActionPerformed
		this.model.newDocument();
//		this.model.document = new Document();
//		this.model.system.newDocument = true;
//		for(Entry<String, Provider> entry:this.model.system.providerMap.entrySet()) {
//			Provider provider = entry.getValue();
//			provider.open();
//		}
//		for (Provider provider : this.model.system.providerList) {
//            if (provider instanceof Meritoki) {
//                Meritoki meritoki = (Meritoki) provider;
//                meritoki.open(this.model.document.uuid);
//            }
//        }
//		this.model.document.pattern.user = this.model.system.user;
		this.init();
	}// GEN-LAST:event_newMenuItemActionPerformed

	private void saveMenuItemActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_saveMenuItemActionPerformed
		this.save();
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
		try {
			this.model.pattern.undo();
			this.init();
		} catch(Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}// GEN-LAST:event_undoMenuItemActionPerformed

	private void redoMenuItemActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_redoMenuItemActionPerformed
		try {
			this.model.pattern.redo();
			this.init();
		} catch(Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
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
    private javax.swing.JScrollPane archiveScrollPane;
    private javax.swing.JMenuItem attributionMenuItem;
    private javax.swing.JMenuItem audioMenuItem;
    private javax.swing.JMenuItem commandMenuItem;
    private javax.swing.JMenuItem connectMenuItem;
    private javax.swing.JMenuItem controlMenuItem;
    private javax.swing.JMenu dialogMenu;
    private javax.swing.JMenu editMenu;
    private javax.swing.JMenuItem executeTesseractMenuItem;
    private javax.swing.JMenu exportMenu;
    private javax.swing.JMenuItem exportMeritokiMenuItem;
    private javax.swing.JMenuItem exportProjectMenuItem;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JMenu googleVendorMenu;
    private javax.swing.JMenuItem importImageMenuItem;
    private javax.swing.JMenu importMenu;
    private javax.swing.JMenuItem importMeritokiMenuItem;
    private javax.swing.JMenuItem importProjectMenuItem;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem logInOutMenuItem;
    private com.meritoki.app.desktop.retina.view.panel.meritoki.MachinePanel machinePanel;
    private javax.swing.JScrollPane machineScrollPane;
    private javax.swing.JMenuBar mainMenuBar;
    private javax.swing.JTabbedPane mainTabbedPane;
    private javax.swing.JMenu meritokiDialogMenu;
    private javax.swing.JMenuItem microsoftExportMenuItem;
    private javax.swing.JMenuItem newMenuItem;
    private javax.swing.JMenuItem openMenuItem;
    private javax.swing.JMenu openRecentMenu;
    private javax.swing.JMenuItem pageMenuItem;
    private com.meritoki.app.desktop.retina.view.panel.PagePanel pagePanel;
    private javax.swing.JScrollPane pageScrollPane;
    private javax.swing.JMenuItem propertyMenuItem;
    private javax.swing.JMenu providerMenu;
    private javax.swing.JMenuItem redoMenuItem;
    private javax.swing.JMenuItem resetMeritokiMenuItem;
    private javax.swing.JMenuItem saveAsMenuItem;
    private javax.swing.JMenuItem saveMenuItem;
    private javax.swing.JMenuItem selectionMenuItem;
    private javax.swing.JMenuItem startMeritokiMenuItem;
    private javax.swing.JMenuItem stopMeritokiMenuItem;
    private com.meritoki.app.desktop.retina.view.panel.TablePanel table1;
    private com.meritoki.app.desktop.retina.view.panel.TablePanel tablePanel;
    private javax.swing.JScrollPane tableScrollPane;
    private javax.swing.JMenu tesseractGoogleMenu;
    private javax.swing.JMenuItem toolMenuItem;
    private javax.swing.JMenuItem undoMenuItem;
    private javax.swing.JMenu vendorMenu;
    private javax.swing.JMenu windowMenu;
    private javax.swing.JMenuItem zooniverseCSVImportMenuItem;
    private javax.swing.JMenuItem zooniverseExportMenuItem;
    private javax.swing.JMenuItem zooniverseImportMenuItem;
    // End of variables declaration//GEN-END:variables
}
