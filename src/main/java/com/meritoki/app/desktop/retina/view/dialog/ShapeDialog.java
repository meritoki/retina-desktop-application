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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.meritoki.app.desktop.retina.model.Model;
import com.meritoki.app.desktop.retina.model.document.Data;
import com.meritoki.app.desktop.retina.model.document.Document;
import com.meritoki.app.desktop.retina.model.document.Image;
import com.meritoki.app.desktop.retina.model.document.Page;
import com.meritoki.app.desktop.retina.model.document.Script;
import com.meritoki.app.desktop.retina.model.document.Shape;
import com.meritoki.app.desktop.retina.model.document.Text;
import com.meritoki.app.desktop.retina.model.document.UnitType;
import com.meritoki.app.desktop.retina.view.frame.MainFrame;

/**
 * Class is used to interact with Shapes on a Page.
 */
public class ShapeDialog extends javax.swing.JDialog implements KeyListener {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 8920899275256865747L;
	/**
	 * Logger for class.
	 */
	static Logger logger = LogManager.getLogger(ShapeDialog.class.getName());
	/**
	 * Model for class.
	 */
	private Model model = null;
	/**
	 * Reference to Main Frame
	 */
	private MainFrame mainFrame = null;

	/**
	 * Creates new form Rectangle
	 *
	 * @param parent
	 * @param modal
	 */
	public ShapeDialog(java.awt.Frame parent, boolean modal) {
		super(parent, modal);
		this.setTitle("Shape");
		this.mainFrame = (MainFrame) this.getParent();
		this.initComponents();
		this.addKeyListener(this);
		this.shapeListAddKeyListener();
		this.shapeListAddMouseListener();
		
	}
	
	public void setModel(Model model) {
		this.model = model;
		this.init();
	}

	public void init() {
		logger.debug("init()");
		this.initLabel();
		this.initList();
		this.initComboBox();
		this.initTextArea();
	}
	
	public void initTextArea() {
		Document document = (this.model != null) ? this.model.document : null;
		Page page = (document != null) ? document.getPage() : null;
		Script script = (page != null) ? page.script:null;
		if(script != null) {
			this.scriptTextArea.setText(script.value);
		}
	}

	public void initLabel() {
		Document document = (this.model != null) ? this.model.document : null;
		Page page = (document != null) ? document.getPage() : null;
		Image image = (page != null) ? page.getImage() : null;
		Shape shape = (image != null) ? image.getShape() : null;
		List<Text> textList = (shape != null) ? shape.getTextList() : null;
	}

	public void initList() {
		Document document = (this.model != null) ? this.model.document : null;
		Page page = (document != null) ? document.getPage() : null;
		List<Shape> shapeList = (page != null) ? page.getSortedShapeList() : null;
		this.initShapeList(shapeList);
		Shape shape = (page != null) ? page.getShape() : null;
		if (shape != null) {
			this.setShapeListSelectedUUID(shape.uuid);
		}
	}

	public void initComboBox() {
		Document document = (this.model != null) ? this.model.document : null;
		Page page = (document != null) ? document.getPage() : null;
		Image file = (page != null) ? page.getImage() : null;
		Shape shape = (file != null) ? file.getShape() : null;
		Data data = (shape != null) ? shape.data : null;
		List<Text> textList = (shape != null) ? shape.getTextList() : null;
		this.initTextValueComboBox(textList);
		List<String> unitTypeList = new ArrayList<>();
		unitTypeList.add("data");
		unitTypeList.add("time");
		unitTypeList.add("space");
		unitTypeList.add("energy");
		unitTypeList.add("language");
		this.initUnitTypeComboBox(unitTypeList);
		if (data != null) {
			this.unitTypeComboBox.setSelectedItem(data.unit.type);
			switch (data.unit.type) {
			case DATA: {
				this.initUnitValueComboBox(this.model.resource.emptyList);
				break;
			}
			case TIME: {
				this.initUnitValueComboBox(this.model.resource.timeList);
				break;
			}
			case SPACE: {
				this.initUnitValueComboBox(this.model.resource.spaceList);
				break;
			}
			case ENERGY: {
				this.initUnitValueComboBox(this.model.resource.energyList);
				break;
			}
			case LANGUAGE: {
				this.initUnitValueComboBox(this.model.resource.languageList);
				break;
			}
			}
		} else {
			this.initUnitValueComboBox(this.model.resource.emptyList);
		}
	}

	/**
	 *
	 * @param textList
	 */
	public void initTextValueComboBox(List<Text> textList) {
		String[] array = new String[0];
		if (textList != null) {
			array = new String[textList.size()];
			for (int i = 0; i < array.length; i++) {
				array[i] = textList.get(i).value;
			}
		}
		this.textValueComboBox.setModel(new DefaultComboBoxModel(array));
		boolean flag = this.textValueDefaultCheckBox.isSelected();
		if (flag) {
			Document document = (this.model != null) ? this.model.document : null;
			Page page = (document != null) ? document.getPage() : null;
			Shape shape = (page != null) ? page.getImage().getShape() : null;
			Data data = (shape != null) ? shape.getData() : null;
			if (data != null) {
				this.textValueComboBox.setSelectedItem(shape.getDefaultText().value);
			}
		}
	}

	public void initUnitTypeComboBox(List<String> unitTypeList) {
		String[] array = new String[0];
		if (unitTypeList != null) {
			array = new String[unitTypeList.size()];
			for (int i = 0; i < array.length; i++) {
				array[i] = unitTypeList.get(i);
			}
		}
		this.unitTypeComboBox.setModel(new DefaultComboBoxModel(array));
		this.unitTypeComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selectedIndex = unitTypeComboBox.getSelectedIndex();
				switch (selectedIndex) {
				case 0: {
					initUnitValueComboBox(model.resource.emptyList);
					break;
				}
				case 1: {
					initUnitValueComboBox(model.resource.timeList);
					break;
				}
				case 2: {
					initUnitValueComboBox(model.resource.spaceList);
					break;
				}
				case 3: {
					initUnitValueComboBox(model.resource.energyList);
					break;
				}
				case 4: {
					initUnitValueComboBox(model.resource.languageList);
				}
				}
			}
		});
	}

	public void initUnitValueComboBox(List<String> unitValueList) {
		String[] array = new String[0];
		if (unitValueList != null) {
			array = new String[unitValueList.size()];
			for (int i = 0; i < array.length; i++) {
				array[i] = unitValueList.get(i);
			}
		}
		this.unitValueComboBox.setModel(new DefaultComboBoxModel(array));
	}

	public void initShapeList(List<Shape> shapeList) {
		DefaultListModel<String> defaultListModel = new DefaultListModel<String>();
		if (shapeList != null && shapeList.size() > 0) {
			logger.debug("initShapeList(" + shapeList + ")");
			for (int i = 0; i < shapeList.size(); i++) {
				defaultListModel.addElement(shapeList.get(i).uuid);
			}

		}
		this.shapeList.setModel(defaultListModel);
	}

	public void setShapeListSelectedUUID(String uuid) {
		this.shapeList.setSelectedValue(uuid, true);
	}
	
	public void setShapeListSelectedIndex(int index) {
		logger.debug("setShapeListSelectedIndex(" + index + ")");
		this.shapeList.setSelectedIndex(index);
	}

	public void shapeListAddKeyListener() {
		this.shapeList.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent ke) {
				ke.consume();
				if (ke.isControlDown()) {
					switch (ke.getKeyCode()) {
					case KeyEvent.VK_Z: {
						logger.debug("keyPressed(e) KeyEvent.VK_Z");
						model.document.pattern.undo();
						mainFrame.init();
						break;
					}
					case KeyEvent.VK_Y: {
						logger.debug("keyPressed(e) KeyEvent.VK_Y");
						model.document.pattern.redo();
						mainFrame.init();
						break;
					}
					}
				} else {
					int keyCode = ke.getKeyCode();
					int index = shapeList.getSelectedIndex();
					switch (keyCode) {
					case KeyEvent.VK_LEFT: {
						logger.debug("keyEvent.VK_LEFT");
						model.document.cache.pressedShape = null;
						setShapeListSelectedIndex(--index);
						model.document.cache.shapeUUID = (String)shapeList.getSelectedValue();
						try {
							model.document.pattern.execute("setShape");
							mainFrame.init();
						} catch (Exception e) {
							JOptionPane.showMessageDialog(mainFrame, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
						}
						break;
					}
					case KeyEvent.VK_RIGHT: {
						logger.debug("keyEvent.VK_RIGHT");
						model.document.cache.pressedShape = null;
						setShapeListSelectedIndex(++index);
						model.document.cache.shapeUUID = (String)shapeList.getSelectedValue();
						try {
							model.document.pattern.execute("setShape");
							mainFrame.init();
						} catch (Exception e) {
							JOptionPane.showMessageDialog(mainFrame, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
						}
						break;
					}
					}
				}
			}
		});
	}

	public void shapeListAddMouseListener() {
		this.shapeList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent me) {
				Document document = (model != null) ? model.document : null;
				document.cache.pressedShape = null;
				document.cache.shapeIndex = -1;
				document.cache.shapeUUID = (String)shapeList.getSelectedValue();
				try {
					document.pattern.execute("setShape");
					mainFrame.init();
				} catch (Exception e) {
					JOptionPane.showMessageDialog(mainFrame, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}

	/**
	 * Function response to keyPressed Event.
	 */
	@Override
	public void keyPressed(KeyEvent ke) {
		ke.consume();
		if (ke.isControlDown()) {
			switch (ke.getKeyCode()) {
			case KeyEvent.VK_Z: {
				logger.debug("keyPressed(e) KeyEvent.VK_Z");
				this.model.document.pattern.undo();
				this.mainFrame.init();
				break;
			}
			case KeyEvent.VK_Y: {
				logger.debug("keyPressed(e) KeyEvent.VK_Y");
				this.model.document.pattern.redo();
				this.mainFrame.init();
				break;
			}
			}
		}
	}

	/**
	 * Function responds to keyTyped Event.
	 */
	@Override
	public void keyTyped(KeyEvent e) {}

	/**
	 * Function responds to keyReleased Event.
	 */
	@Override
	public void keyReleased(KeyEvent e) {}

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

        textLabel = new javax.swing.JLabel();
        inputLabel = new javax.swing.JLabel();
        textInputTextField = new javax.swing.JTextField();
        noInputCheckBox = new javax.swing.JCheckBox();
        inputAddButton = new javax.swing.JButton();
        valueLabel = new javax.swing.JLabel();
        textValueComboBox = new javax.swing.JComboBox();
        textValueDefaultCheckBox = new javax.swing.JCheckBox();
        textUnitSeparator = new javax.swing.JSeparator();
        unitLabel = new javax.swing.JLabel();
        typeLabel = new javax.swing.JLabel();
        unitTypeComboBox = new javax.swing.JComboBox();
        unitValueLabel = new javax.swing.JLabel();
        unitValueComboBox = new javax.swing.JComboBox();
        unitRectangleSeparator = new javax.swing.JSeparator();
        rectangleScrollPane = new javax.swing.JScrollPane();
        shapeList = new javax.swing.JList();
        removeShapeButton = new javax.swing.JButton();
        applyUnitButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        rectangleButton = new javax.swing.JButton();
        ellipseButton = new javax.swing.JButton();
        setTextButton = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();
        scriptTextArea = new javax.swing.JTextArea();
        scriptLabel = new javax.swing.JLabel();
        resetScriptButton = new javax.swing.JButton();
        setScriptButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        textLabel.setText("Text");

        inputLabel.setText("Input:");

        noInputCheckBox.setText("No");
        noInputCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                noInputCheckBoxActionPerformed(evt);
            }
        });

        inputAddButton.setText("Add");
        inputAddButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inputAddButtonActionPerformed(evt);
            }
        });

        valueLabel.setText("Value:");

        textValueComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        textValueDefaultCheckBox.setText("Default");
        textValueDefaultCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textValueDefaultCheckBoxActionPerformed(evt);
            }
        });

        unitLabel.setText("Unit");

        typeLabel.setText("Type:");

        unitTypeComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        unitTypeComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                unitTypeComboBoxActionPerformed(evt);
            }
        });

        unitValueLabel.setText("Value:");

        unitValueComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        shapeList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        rectangleScrollPane.setViewportView(shapeList);

        removeShapeButton.setText("X");
        removeShapeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeShapeButtonActionPerformed(evt);
            }
        });

        applyUnitButton.setText("Apply");
        applyUnitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                applyUnitButtonActionPerformed(evt);
            }
        });

        jLabel1.setText("Shapes");

        rectangleButton.setText("Rectangle");
        rectangleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rectangleButtonActionPerformed(evt);
            }
        });

        ellipseButton.setText("Ellipse");
        ellipseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ellipseButtonActionPerformed(evt);
            }
        });

        setTextButton.setText("Set");
        setTextButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setTextButtonActionPerformed(evt);
            }
        });

        scriptTextArea.setColumns(20);
        scriptTextArea.setRows(5);
        jScrollPane1.setViewportView(scriptTextArea);

        scriptLabel.setText("Script");

        resetScriptButton.setText("Reset");
        resetScriptButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetScriptButtonActionPerformed(evt);
            }
        });

        setScriptButton.setText("Set");
        setScriptButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setScriptButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(224, 224, 224)
                .addComponent(textLabel)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(214, 214, 214)
                        .addComponent(unitLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(textUnitSeparator))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 409, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(setScriptButton, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(resetScriptButton, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(unitRectangleSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 409, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(6, 6, 6)
                                        .addComponent(typeLabel))
                                    .addComponent(unitValueLabel))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(applyUnitButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(unitValueComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(unitTypeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 349, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(scriptLabel)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 411, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                        .addComponent(rectangleButton, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(ellipseButton, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(inputLabel)
                                            .addComponent(valueLabel))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(inputAddButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(textValueComboBox, 0, 344, Short.MAX_VALUE)
                                            .addComponent(setTextButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(textInputTextField))))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(noInputCheckBox)
                                    .addComponent(textValueDefaultCheckBox))))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(rectangleScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 409, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(removeShapeButton, javax.swing.GroupLayout.DEFAULT_SIZE, 102, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rectangleButton)
                    .addComponent(ellipseButton))
                .addGap(19, 19, 19)
                .addComponent(textLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textInputTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(noInputCheckBox)
                    .addComponent(inputLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(inputAddButton)
                .addGap(14, 14, 14)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textValueComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(valueLabel)
                    .addComponent(textValueDefaultCheckBox))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(setTextButton)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(textUnitSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(unitLabel)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(unitTypeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(typeLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(unitValueComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(unitValueLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(applyUnitButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(unitRectangleSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(rectangleScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(removeShapeButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scriptLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(resetScriptButton)
                    .addComponent(setScriptButton))
                .addGap(10, 10, 10))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

	private void noInputCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_noInputCheckBoxActionPerformed
		boolean flag = this.noInputCheckBox.isSelected();
		if (flag) {
			this.textInputTextField.setEnabled(false);
			this.inputAddButton.setEnabled(false);
			this.textValueComboBox.setEnabled(false);
			this.textValueDefaultCheckBox.setEnabled(false);
		} else {
			this.textInputTextField.setEnabled(true);
			this.inputAddButton.setEnabled(true);
			this.textValueComboBox.setEnabled(true);
			this.textValueDefaultCheckBox.setEnabled(true);
		}
	}// GEN-LAST:event_noInputCheckBoxActionPerformed

	private void inputAddButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_inputAddButtonActionPerformed
		String value = this.textInputTextField.getText().trim();
		Text text = new Text();
		text.value = value;
		Document document = (this.model != null) ? this.model.document : null;
		Page page = (document != null) ? document.getPage() : null;
		Shape shape = (page != null) ? page.getImage().getShape() : null;
		if (shape != null) {
			shape.addText(text);
			System.out.println(shape.getTextMap());
			this.setModel(this.model);
		}

	}// GEN-LAST:event_inputAddButtonActionPerformed

	private void textValueDefaultCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_textValueDefaultCheckBoxActionPerformed
		boolean flag = this.textValueDefaultCheckBox.isSelected();
		if (flag) {
			Document document = (this.model != null) ? this.model.document : null;
			Page page = (document != null) ? document.getPage() : null;
			Shape shape = (page != null) ? page.getImage().getShape() : null;
			if (shape != null) {
				this.textValueComboBox.setSelectedItem(shape.getDefaultText().value);
			}
		}
	}// GEN-LAST:event_textValueDefaultCheckBoxActionPerformed

	private void unitTypeComboBoxActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_unitTypeComboBoxActionPerformed
		// TODO add your handling code here:
	}// GEN-LAST:event_unitTypeComboBoxActionPerformed

	private void removeShapeButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_deleteRectangleButtonActionPerformed
		this.model.document.cache.pressedShape = this.model.document.getPage().getShape();
		try {
			this.model.document.pattern.execute("removeShape");
			this.mainFrame.init();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(mainFrame, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
		
		//		int index = this.shapeList.getSelectedIndex();
//		Document document = (this.model != null) ? this.model.document : null;
//		Page page = (document != null) ? document.getPage() : null;
//		page.getShapeList().remove(index);
////		this.mainFrame.repaint();
//		this.mainFrame.init();
	}// GEN-LAST:event_deleteRectangleButtonActionPerformed

	private void applyUnitButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_applyUnitButtonActionPerformed
		logger.info("applyUnitButtonActionPerformed(...)");
		Document document = (this.model != null) ? this.model.document : null;
		Page page = (document != null) ? document.getPage() : null;
		Shape shape = (page != null) ? page.getImage().getShape() : null;
		Data data = (shape != null) ? shape.getData() : null;
		if (data != null) {
			String unitType = (String) this.unitTypeComboBox.getSelectedItem();
			switch (unitType) {
			case "data": {
				data.unit.type = UnitType.DATA;
				break;
			}
			case "time": {
				data.unit.type = UnitType.TIME;
				break;
			}
			case "space": {
				data.unit.type = UnitType.SPACE;
				break;
			}
			case "energy": {
				data.unit.type = UnitType.ENERGY;
				break;
			}
			case "language": {
				data.unit.type = UnitType.LANGUAGE;
			}
			}
			data.unit.value = (String) this.unitValueComboBox.getSelectedItem();
			logger.info("applyUnitButtonActionPerformed(e) data.unit.value=" + data.unit.value);
			logger.info("applyUnitButtonActionPerformed(e) data.unit.type=" + data.unit.type);
		}
//		this.mainFrame.repaint();
		this.mainFrame.init();
	}// GEN-LAST:event_applyUnitButtonActionPerformed

	private void rectangleButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_rectangleButtonActionPerformed

		this.model.document.cache.type = com.meritoki.app.desktop.retina.model.document.ShapeType.RECTANGLE;
	}// GEN-LAST:event_rectangleButtonActionPerformed

	private void ellipseButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_ellipseButtonActionPerformed
		this.model.document.cache.type = com.meritoki.app.desktop.retina.model.document.ShapeType.ELLIPSE;
	}// GEN-LAST:event_ellipseButtonActionPerformed

	private void setTextButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_setTextButtonActionPerformed
		Shape shape = this.model.document.getPage().getShape();
		Data data = (shape != null) ? shape.data : null;
		Text text = (data != null) ? data.text : null;
		String value = (String) this.textValueComboBox.getSelectedItem();
		text.value = value;

	}// GEN-LAST:event_setTextButtonActionPerformed

	private void applyParserButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_applyParserButtonActionPerformed
		// TODO add your handling code here:
	}// GEN-LAST:event_applyParserButtonActionPerformed

	private void setScriptButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_executeButtonActionPerformed
		// Executes the latest instructions added to the text field.
		String value = this.scriptTextArea.getText();
		Document document = (this.model != null) ? this.model.document : null;
		Page page = (document != null) ? document.getPage() : null;
		if (page != null) {
			page.script.value = value;
			this.mainFrame.init();
		}
	}// GEN-LAST:event_executeButtonActionPerformed

	private void resetScriptButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_resetButtonActionPerformed
		logger.info("resetButtonActionPerformed()");
		Document document = (this.model != null) ? this.model.document : null;
		Page page = (document != null) ? document.getPage() : null;
		Script script = (page != null) ? page.script : null;
		if (page != null && script != null) {
			this.scriptTextArea.setText(script.value);
			page.script.value = "";
			this.mainFrame.init();
		}
	}// GEN-LAST:event_resetButtonActionPerformed

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
			java.util.logging.Logger.getLogger(ShapeDialog.class.getName()).log(java.util.logging.Level.SEVERE, null,
					ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(ShapeDialog.class.getName()).log(java.util.logging.Level.SEVERE, null,
					ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(ShapeDialog.class.getName()).log(java.util.logging.Level.SEVERE, null,
					ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(ShapeDialog.class.getName()).log(java.util.logging.Level.SEVERE, null,
					ex);
		}
		// </editor-fold>
		// </editor-fold>
		// </editor-fold>
		// </editor-fold>
		// </editor-fold>
		// </editor-fold>
		// </editor-fold>
		// </editor-fold>
		// </editor-fold>
		// </editor-fold>
		// </editor-fold>
		// </editor-fold>
		// </editor-fold>
		// </editor-fold>
		// </editor-fold>
		// </editor-fold>
		// </editor-fold>
		// </editor-fold>
		// </editor-fold>
		// </editor-fold>
		// </editor-fold>
		// </editor-fold>
		// </editor-fold>
		// </editor-fold>
		// </editor-fold>
		// </editor-fold>
		// </editor-fold>
		// </editor-fold>
		// </editor-fold>
		// </editor-fold>
		// </editor-fold>
		// </editor-fold>

		/* Create and display the dialog */
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				ShapeDialog dialog = new ShapeDialog(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton applyUnitButton;
    private javax.swing.JButton ellipseButton;
    private javax.swing.JButton inputAddButton;
    private javax.swing.JLabel inputLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JCheckBox noInputCheckBox;
    private javax.swing.JButton rectangleButton;
    private javax.swing.JScrollPane rectangleScrollPane;
    private javax.swing.JButton removeShapeButton;
    private javax.swing.JButton resetScriptButton;
    private javax.swing.JLabel scriptLabel;
    private javax.swing.JTextArea scriptTextArea;
    private javax.swing.JButton setScriptButton;
    private javax.swing.JButton setTextButton;
    private javax.swing.JList shapeList;
    private javax.swing.JTextField textInputTextField;
    private javax.swing.JLabel textLabel;
    private javax.swing.JSeparator textUnitSeparator;
    private javax.swing.JComboBox textValueComboBox;
    private javax.swing.JCheckBox textValueDefaultCheckBox;
    private javax.swing.JLabel typeLabel;
    private javax.swing.JLabel unitLabel;
    private javax.swing.JSeparator unitRectangleSeparator;
    private javax.swing.JComboBox unitTypeComboBox;
    private javax.swing.JComboBox unitValueComboBox;
    private javax.swing.JLabel unitValueLabel;
    private javax.swing.JLabel valueLabel;
    // End of variables declaration//GEN-END:variables
}

