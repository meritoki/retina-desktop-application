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
package com.meritoki.app.desktop.retina.view.dialog;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

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
public class PageDialog extends javax.swing.JDialog implements KeyListener, MouseListener {

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

	private MainFrame mainFrame;

	/**
	 * Image dialog class.
	 * 
	 * @param parent
	 * @param flag
	 */
	public PageDialog(java.awt.Frame parent, boolean flag) {
		super(parent, flag);
		this.setTitle("Page");
		this.mainFrame = (MainFrame) parent;
		this.initComponents();
		this.addKeyListener(this);
		this.addMouseListener(this);
		this.pageListAddKeyListener();
		this.pageListAddMouseListener();
		this.imageListAddKeyListener();
		this.imageListAddMouseListener();
	}

	public void pageListAddKeyListener() {
		this.pageList.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent ke) {
				ke.consume();
				if (ke.isControlDown()) {
					switch (ke.getKeyCode()) {
					case KeyEvent.VK_Z: {
						logger.debug("keyPressed(e) KeyEvent.VK_Z");
						try {
							model.pattern.undo();
							mainFrame.init();
						} catch (Exception e) {
							e.printStackTrace();
							JOptionPane.showMessageDialog(mainFrame, e.getMessage(), "Error",
									JOptionPane.ERROR_MESSAGE);
						}
						break;
					}
					case KeyEvent.VK_Y: {
						logger.debug("keyPressed(e) KeyEvent.VK_Y");
						try {
							model.pattern.redo();
							mainFrame.init();
						} catch (Exception e) {
							e.printStackTrace();
							JOptionPane.showMessageDialog(mainFrame, e.getMessage(), "Error",
									JOptionPane.ERROR_MESSAGE);
						}
						break;
					}
					}
				} else {
					int keyCode = ke.getKeyCode();
					int index = pageList.getSelectedIndex();
					switch (keyCode) {
					case KeyEvent.VK_LEFT: {
						logger.debug("keyEvent.VK_LEFT");
						setPageListSelectedIndex(--index);
						model.cache.pageUUID = (String) pageList.getSelectedValue();
						try {
							model.pattern.execute("setPage");
							mainFrame.init();
						} catch (Exception e) {
							JOptionPane.showMessageDialog(mainFrame, e.getMessage(), "Error",
									JOptionPane.ERROR_MESSAGE);
						}
						break;
					}
					case KeyEvent.VK_RIGHT: {
						logger.debug("keyEvent.VK_RIGHT");
						setPageListSelectedIndex(++index);
						model.cache.pageUUID = (String) pageList.getSelectedValue();
						try {
							model.pattern.execute("setPage");
							mainFrame.init();
						} catch (Exception e) {
							JOptionPane.showMessageDialog(mainFrame, e.getMessage(), "Error",
									JOptionPane.ERROR_MESSAGE);
						}
						break;
					}
					case KeyEvent.VK_UP: {
						logger.debug("keyEvent.VK_UP");
						setPageListSelectedIndex(--index);
						model.cache.pageUUID = (String) pageList.getSelectedValue();
						try {
							model.pattern.execute("setPage");
							mainFrame.init();
						} catch (Exception e) {
							JOptionPane.showMessageDialog(mainFrame, e.getMessage(), "Error",
									JOptionPane.ERROR_MESSAGE);
						}
						break;
					}
					case KeyEvent.VK_DOWN: {
						logger.debug("keyEvent.VK_DOWN");
						setPageListSelectedIndex(++index);
						model.cache.pageUUID = (String) pageList.getSelectedValue();
						try {
							model.pattern.execute("setPage");
							mainFrame.init();
						} catch (Exception e) {
							JOptionPane.showMessageDialog(mainFrame, e.getMessage(), "Error",
									JOptionPane.ERROR_MESSAGE);
						}
						break;
					}
					}
				}
			}

		});
	}

	public void pageListAddMouseListener() {
		this.pageList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent me) {
				model.cache.pageUUID = (String) pageList.getSelectedValue();
				try {
					model.pattern.execute("setPage");
					mainFrame.init();
				} catch (Exception e) {
					JOptionPane.showMessageDialog(mainFrame, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}

	public void imageListAddKeyListener() {
		this.imageList.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent ke) {
				ke.consume();
				if (ke.isControlDown()) {
					switch (ke.getKeyCode()) {
					case KeyEvent.VK_Z: {
						logger.debug("keyPressed(e) KeyEvent.VK_Z");
						try {
							model.pattern.undo();
							mainFrame.init();
						} catch (Exception e) {
							e.printStackTrace();
							JOptionPane.showMessageDialog(mainFrame, e.getMessage(), "Error",
									JOptionPane.ERROR_MESSAGE);
						}
						break;
					}
					case KeyEvent.VK_Y: {
						logger.debug("keyPressed(e) KeyEvent.VK_Y");
						try {
							model.pattern.redo();
							mainFrame.init();
						} catch (Exception e) {
							e.printStackTrace();
							JOptionPane.showMessageDialog(mainFrame, e.getMessage(), "Error",
									JOptionPane.ERROR_MESSAGE);
						}
						break;
					}
					}
				} else {
					int keyCode = ke.getKeyCode();
					int index = imageList.getSelectedIndex();
					switch (keyCode) {
					case KeyEvent.VK_LEFT: {
						logger.debug("keyEvent.VK_LEFT");
						setImageListSelectedIndex(--index);
						model.cache.imageUUID = imageList.getSelectedValue();
						try {
							model.pattern.execute("setImage");
							mainFrame.init();
						} catch (Exception e) {
							JOptionPane.showMessageDialog(mainFrame, e.getMessage(), "Error",
									JOptionPane.ERROR_MESSAGE);
						}
						break;
					}
					case KeyEvent.VK_RIGHT: {
						logger.debug("keyEvent.VK_RIGHT");
						setImageListSelectedIndex(++index);
						model.cache.imageUUID = imageList.getSelectedValue();
						try {
							model.pattern.execute("setImage");
							mainFrame.init();
						} catch (Exception e) {
							JOptionPane.showMessageDialog(mainFrame, e.getMessage(), "Error",
									JOptionPane.ERROR_MESSAGE);
						}
						break;
					}
					}
				}
			}
		});
	}

	public void imageListAddMouseListener() {
		this.imageList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent me) {
				model.cache.imageUUID = imageList.getSelectedValue();
				try {
					model.pattern.execute("setImage");
					mainFrame.init();
				} catch (Exception e) {
					JOptionPane.showMessageDialog(mainFrame, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}

	/**
	 * Function set Model for instance.
	 * 
	 * @param model
	 */
	public void setModel(Model model) {
		this.model = model;
	}

	/**
	 * Function instantiates view.
	 */
	public void init() {
		logger.debug("init()");
		this.initTextField();
		this.initLabel();
		this.initList();
	}

	public void initTextField() {
		Document document = (this.model != null) ? this.model.document : null;
		Page page = (document != null) ? document.getPage() : null;
		if (page != null) {
			double threshold = page.threshold;
			this.pageThresholdTextField.setText(String.valueOf(threshold));
		}
	}

	public void initLabel() {
		Document document = (this.model != null) ? this.model.document : null;
		if (document != null) {
			this.pageListSizeValueLabel.setText(document.pageList.size() + "");
		}
		Page page = (document != null) ? document.getPage() : null;
		int pageIndex = (document != null) ? document.getIndex() : 0;
		Image image = (page != null) ? page.getImage() : null;
		int imageIndex = (page != null) ? page.getIndex() : 0;
		if (page != null) {
			this.pageIndexValueLabel.setText(pageIndex + "");
			this.pageUUIDValueLabel.setText(page.uuid);
		}
		if (image != null) {
			this.imageIndexValueLabel.setText(imageIndex + "");
			this.imageUUIDValueLabel.setText(image.uuid);
			this.imageNameValueLabel.setText(image.fileName);
			this.imagePathValueLabel.setText(image.filePath);
		}
	}

	public void initList() {
		logger.debug("initList()");
		Document document = (this.model != null) ? this.model.document : null;
		int pageIndex = (document != null) ? document.getIndex() : 0;
		List<Page> pageList = (document != null) ? document.getPageList() : null;
		this.initPageList(pageList);
		this.setPageListSelectedIndex(pageIndex);
		Page page = (document != null) ? document.getPage() : null;
		int imageIndex = (page != null) ? page.getIndex() : 0;
		List<Image> imageList = (page != null) ? page.getImageList() : null;
		this.initImageList(imageList);
		this.setImageListSelectedIndex(imageIndex);
	}

	public void initPageList(List<Page> pageList) {
		logger.debug("initPageList(...)");
		DefaultListModel<String> defaultListModel = new DefaultListModel<>();
		if (pageList != null) {
			for (int i = 0; i < pageList.size(); i++) {
				defaultListModel.addElement(pageList.get(i).uuid);
			}
		}
		this.pageList.setModel(defaultListModel);
	}

	public void initImageList(List<Image> imageList) {
		logger.debug("initImageList(...)");
		DefaultListModel<String> defaultListModel = new DefaultListModel<>();
		if (imageList != null) {
			for (int i = 0; i < imageList.size(); i++) {
				defaultListModel.addElement(imageList.get(i).uuid);
			}
		}
		this.imageList.setModel(defaultListModel);
	}

	public void setPageListSelectedIndex(int index) {
		logger.debug("setPageListSelectedIndex(" + index + ")");
		this.pageList.setSelectedIndex(index);
	}

	public void setImageListSelectedIndex(int index) {
		logger.debug("setImageListSelectedIndex(" + index + ")");
		this.imageList.setSelectedIndex(index);
	}

	public void setPageListSelectedUUID(String uuid) {
		this.pageList.setSelectedValue(uuid, true);
	}

	public void setImageListSelectedUUID(String uuid) {
		this.imageList.setSelectedValue(uuid, true);
	}

	// End of variables declaration

	@Override
	public void keyPressed(KeyEvent ke) {
		ke.consume();
		if (ke.isControlDown()) {
			switch (ke.getKeyCode()) {
			case KeyEvent.VK_Z: {
				logger.debug("keyPressed(e) KeyEvent.VK_Z");
				try {
					model.pattern.undo();
					mainFrame.init();
				} catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(mainFrame, e.getMessage(), "Error",
							JOptionPane.ERROR_MESSAGE);
				}
				break;
			}
			case KeyEvent.VK_Y: {
				logger.debug("keyPressed(e) KeyEvent.VK_Y");
				try {
					model.pattern.redo();
					mainFrame.init();
				} catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(mainFrame, e.getMessage(), "Error",
							JOptionPane.ERROR_MESSAGE);
				}
				break;
			}
			}
		}
	}

	// End of variables declaration

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	// End of variables declaration

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
	// <editor-fold defaultstate="collapsed" desc="Generated
	// <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        uuidLabel = new javax.swing.JLabel();
        pageUUIDValueLabel = new javax.swing.JLabel();
        pageIndexLabel = new javax.swing.JLabel();
        pageIndexValueLabel = new javax.swing.JLabel();
        imageListScrollPane = new javax.swing.JScrollPane();
        pageList = new javax.swing.JList();
        imageScriptScrollPane = new javax.swing.JScrollPane();
        pageScriptTextArea = new javax.swing.JTextArea();
        executeImageScriptButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        pageListSizeValueLabel = new javax.swing.JLabel();
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
        imageIndexValueLabel = new javax.swing.JLabel();
        imageUUIDValueLabel = new javax.swing.JLabel();
        imageNameValueLabel = new javax.swing.JLabel();
        imagePathValueLabel = new javax.swing.JLabel();
        jSeparator5 = new javax.swing.JSeparator();
        jLabel4 = new javax.swing.JLabel();
        pageThresholdTextField = new javax.swing.JTextField();
        setPageThresholdButton = new javax.swing.JButton();

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

        jLabel1.setText("Page:");

        jLabel2.setText("Size:");

        pageListSizeValueLabel.setText("null");

        jLabel3.setText("Script:");

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

        imageListLabel.setText("Image:");

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

        jLabel4.setText("Threshold:");

        setPageThresholdButton.setText("Set");
        setPageThresholdButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setPageThresholdButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jSeparator3)
                            .addComponent(imageListScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 351, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(removePageButton, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(imageIndexLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(imageIndexValueLabel))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(executeImageScriptButton, javax.swing.GroupLayout.PREFERRED_SIZE, 345, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(imageScriptScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 339, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(removeScriptButton, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(imageListLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jSeparator2)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 342, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(removeImageButton, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(imageUUIDLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(imageUUIDValueLabel))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(imagePathLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(imagePathValueLabel))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(pageIndexLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 346, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pageIndexValueLabel)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pageThresholdTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(setPageThresholdButton, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(imageNameLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(imageNameValueLabel))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(uuidLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(pageUUIDValueLabel))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(pageListSizeValueLabel)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(pageThresholdTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(setPageThresholdButton))
                .addGap(21, 21, 21)
                .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(pageIndexLabel)
                    .addComponent(pageIndexValueLabel))
                .addGap(5, 5, 5)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(pageListSizeValueLabel))
                .addGap(5, 5, 5)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(uuidLabel)
                    .addComponent(pageUUIDValueLabel))
                .addGap(26, 26, 26)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(imageListScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel1))
                    .addComponent(removePageButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(imageIndexLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(imageIndexValueLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(imageUUIDLabel)
                    .addComponent(imageUUIDValueLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(imageNameLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(imageNameValueLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(imagePathLabel)
                    .addComponent(imagePathValueLabel))
                .addGap(26, 26, 26)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(removeImageButton, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(imageListLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(imageScriptScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3)))
                    .addComponent(removeScriptButton, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(executeImageScriptButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jScrollPane2.setViewportView(jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 511, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 737, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

	private void setPageThresholdButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_setPageThresholdButtonActionPerformed
		String thresholdString = this.pageThresholdTextField.getText();
		try {
			double threshold = Double.parseDouble(thresholdString);
			Page page = this.model.document.getPage();
			if (page != null) {
				page.threshold = threshold;
				this.mainFrame.init();
			} else {
				JOptionPane.showMessageDialog(mainFrame, "page is null", "Error", JOptionPane.ERROR_MESSAGE);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(mainFrame, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}// GEN-LAST:event_setPageThresholdButtonActionPerformed

	private void executeImageScriptButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_executeImageScriptButtonActionPerformed
		Document document = (this.model != null) ? this.model.document : null;
		this.model.cache.script = this.pageScriptTextArea.getText();
		this.model.cache.pageList = (document != null) ? document.getPageList() : null;
		try {
			this.model.pattern.execute("executeScript");
			this.mainFrame.init();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(mainFrame, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
		this.requestFocus();
	}// GEN-LAST:event_executeImageScriptButtonActionPerformed

	private void removeScriptButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_removeScriptButtonActionPerformed
		this.pageScriptTextArea.setText("");
	}// GEN-LAST:event_removeScriptButtonActionPerformed

	private void removePageButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_removePageButtonActionPerformed
		Document document = (this.model != null) ? this.model.document : null;
		Page pressedPage = (document != null) ? document.getPage() : null;
		try {
			this.model.cache.pressedPageUUID = pressedPage.uuid;
			this.model.pattern.execute("removePage");
			this.mainFrame.init();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(mainFrame, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
		this.requestFocus();
	}// GEN-LAST:event_removePageButtonActionPerformed

	private void removeImageButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_removeImageButtonActionPerformed
		Document document = (this.model != null) ? this.model.document : null;
		Page page = (document != null) ? document.getPage() : null;
		Image image = (page != null) ? page.getImage() : null;
		try {
			this.model.cache.pressedPageUUID = page.uuid;
			this.model.cache.pressedImageUUID = image.uuid;
			this.model.pattern.execute("removeImage");
			this.mainFrame.init();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(mainFrame, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
		this.requestFocus();
	}// GEN-LAST:event_removeImageButtonActionPerformed

	/**
	 * @param args the command line arguments
	 */
	public static void main(String args[]) {
		/* Set the Nimbus look and feel */
		// <editor-fold defaultstate="collapsed" desc=" Look and feel setting code
		// (optional) ">
		/*
		 * If Nimbus (introduced in Java SE 6) is not available, stay with the default
		 * look and feel. For details see
		 * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
		 */
		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(PageDialog.class.getName()).log(java.util.logging.Level.SEVERE, null,
					ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(PageDialog.class.getName()).log(java.util.logging.Level.SEVERE, null,
					ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(PageDialog.class.getName()).log(java.util.logging.Level.SEVERE, null,
					ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(PageDialog.class.getName()).log(java.util.logging.Level.SEVERE, null,
					ex);
		}
		// </editor-fold>
		// </editor-fold>
		// </editor-fold>
		// </editor-fold>

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
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JLabel pageIndexLabel;
    private javax.swing.JLabel pageIndexValueLabel;
    private javax.swing.JList pageList;
    private javax.swing.JLabel pageListSizeValueLabel;
    private javax.swing.JTextArea pageScriptTextArea;
    private javax.swing.JTextField pageThresholdTextField;
    private javax.swing.JLabel pageUUIDValueLabel;
    private javax.swing.JButton removeImageButton;
    private javax.swing.JButton removePageButton;
    private javax.swing.JButton removeScriptButton;
    private javax.swing.JButton setPageThresholdButton;
    private javax.swing.JLabel uuidLabel;
    // End of variables declaration//GEN-END:variables

	@Override
	public void mouseClicked(MouseEvent arg0) {
		this.requestFocus();

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

}
