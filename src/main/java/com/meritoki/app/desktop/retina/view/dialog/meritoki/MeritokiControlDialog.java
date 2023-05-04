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
package com.meritoki.app.desktop.retina.view.dialog.meritoki;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.meritoki.app.desktop.retina.model.Model;
import com.meritoki.app.desktop.retina.model.command.Command;
import com.meritoki.app.desktop.retina.model.command.ScriptCortex;
import com.meritoki.app.desktop.retina.model.provider.meritoki.Document;
import com.meritoki.app.desktop.retina.model.provider.meritoki.Input;
import com.meritoki.app.desktop.retina.model.provider.meritoki.Meritoki;
import com.meritoki.app.desktop.retina.view.frame.MainFrame;
import com.meritoki.library.cortex.model.Belief;
import com.meritoki.library.cortex.model.Concept;
import com.meritoki.library.cortex.model.cortex.Cortex;

/**
 *
 * @author jorodriguez
 */
public class MeritokiControlDialog extends javax.swing.JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7676357082487259494L;
	private static Logger logger = LogManager.getLogger(MeritokiControlDialog.class.getName());
	public Model model = null;
	public MainFrame mainFrame = null;
	private Meritoki meritoki;

	/**
	 * Creates new form Control
	 */
	public MeritokiControlDialog(java.awt.Frame parent, boolean modal) {
		super(parent, modal);
		this.initComponents();
		this.inputListAddMouseListener();
		this.beliefListAddKeyListener();
		this.beliefListAddMouseListener();
		this.mainFrame = (MainFrame) parent;
	}

	public void setModel(Model model) {
		this.model = model;
		this.setPreferredSize(this.getPreferredSize());
		this.meritoki = (Meritoki) this.model.system.providerMap.get("meritoki");
		this.retinaPanel.setModel(this.model);
		this.init();
	}
	
	public void beliefListAddKeyListener() {
		this.beliefList.addKeyListener(new KeyAdapter() {
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
					int index = beliefList.getSelectedIndex();
					switch (keyCode) {
					case KeyEvent.VK_LEFT: {
						logger.debug("keyEvent.VK_LEFT");
						setBeliefListSelectedIndex(--index);
						model.cache.pageUUID = (String)beliefList.getSelectedValue();
						try {
							model.pattern.execute("setBelief");
							mainFrame.init();
						} catch (Exception e) {
							JOptionPane.showMessageDialog(mainFrame, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
						}
						break;
					}
					case KeyEvent.VK_RIGHT: {
						logger.debug("keyEvent.VK_RIGHT");
						setBeliefListSelectedIndex(++index);
						model.cache.pageUUID = (String)beliefList.getSelectedValue();
						try {
							model.pattern.execute("setBelief");
							mainFrame.init();
						} catch (Exception e) {
							JOptionPane.showMessageDialog(mainFrame, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
						}
						break;
					}
					case KeyEvent.VK_UP: {
						logger.debug("keyEvent.VK_UP");
						setBeliefListSelectedIndex(--index);
						model.cache.pageUUID = (String)beliefList.getSelectedValue();
						try {
							model.pattern.execute("setBelief");
							mainFrame.init();
						} catch (Exception e) {
							JOptionPane.showMessageDialog(mainFrame, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
						}
						break;
					}
					case KeyEvent.VK_DOWN: {
						logger.debug("keyEvent.VK_DOWN");
						setBeliefListSelectedIndex(++index);
						model.cache.pageUUID = (String)beliefList.getSelectedValue();
						try {
							model.pattern.execute("setBelief");
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

	public void beliefListAddMouseListener() {
		this.beliefList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent me) {
				String uuid = (String)beliefList.getSelectedValue();
				Document document = (meritoki != null) ? meritoki.document : null;
				Cortex cortex = (document != null) ? (Cortex)document.cortex : null;
				if (cortex != null) {
					cortex.setIndex(uuid);
				}
				setBeliefListSelectedUUID(uuid);
				init();
			}
		});
	}
	
	public void inputListAddMouseListener() {
		this.inputList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent me) {
				String uuid = (String)inputList.getSelectedValue();
				if (meritoki != null) {
					meritoki.setIndex(uuid);
					meritoki.scale();
				}
				setInputListSelectedUUID(uuid);
				mainFrame.init();
			}
		});
	}

	public void init() {
		logger.info("init()");
		this.revalidate();
		this.initButton();
		this.initList();
		this.initLabel();
		this.retinaPanel.init();
	}
	
	public void initButton() {
		if(this.meritoki.loop) {
			this.startButton.setText("Stop");
		} else {
			this.startButton.setText("Start");
		}
	}

	public void initLabel() {
		Document document = (this.model != null) ? this.meritoki.document : null;
		Cortex cortex = (document != null) ? (Cortex)document.cortex : null;
		if (cortex != null) {
			this.xLabel.setText(Double.toString(cortex.origin.x));
			this.yLabel.setText(Double.toString(cortex.origin.y));
		}
	}

	public void initList() {
		Document document = (this.meritoki != null) ? this.meritoki.document : null;
		Cortex cortex = (document != null) ? (Cortex)document.cortex : null;
		if (cortex != null) {
			Belief belief = cortex.getBelief();
//			logger.info("initList() belief="+belief);
			if (belief != null) {
				List<Belief> beliefList = cortex.beliefList;
				List<Concept> conceptList = belief.conceptList;
				this.initBeliefList(beliefList);
				this.setBeliefListSelectedIndex(cortex.index);
				this.initConceptList(conceptList);
			}
		}
		List<Input> inputList = meritoki.inputList;
		if(inputList != null) {
			this.initInputList(inputList);
		}
		// this.initPredictionConceptList(predictionConceptList);
	}
	
	public void setInputListSelectedUUID(String uuid) {
		this.inputList.setSelectedValue(uuid, true);
	}

	public void setBeliefListSelectedUUID(String uuid) {
		this.beliefList.setSelectedValue(uuid, true);
	}

	public void setBeliefListSelectedIndex(int index) {
		this.beliefList.setSelectedIndex(index);
	}
	
	public void setConceptListSelectedIndex(int index) {
		this.conceptList.setSelectedIndex(index);
	}

	public void initInputList(List<Input> conceptList) {
//      logger.debug("initConceptList(...)");
		DefaultListModel<String> defaultListModel = new DefaultListModel<>();
		if (conceptList != null) {
			for (int i = 0; i < conceptList.size(); i++) {
				Input concept = conceptList.get(i);
				if(concept != null) {
				String uuid = concept.uuid;
				if(uuid != null) {
					defaultListModel.addElement(uuid);
				}
				}
			}
		}
		this.inputList.setModel(defaultListModel);
	}
	
	public void initBeliefList(List<Belief> beliefList) {
//        logger.debug("initConceptList(...)");
		DefaultListModel<String> defaultListModel = new DefaultListModel<>();
		if (beliefList != null) {
			for (int i = 0; i < beliefList.size(); i++) {
				defaultListModel.addElement(beliefList.get(i).uuid);
			}
		}
		this.beliefList.setModel(defaultListModel);
	}

	public void initConceptList(List<Concept> conceptList) {
//        logger.info("initConceptList("+conceptList+")");
		DefaultListModel<String> defaultListModel = new DefaultListModel<>();
		if (conceptList != null) {
			for (int i = 0; i < conceptList.size(); i++) {
				defaultListModel.addElement(
						i + ":" + String.format("%.2f", conceptList.get(i).rank) + ":" + conceptList.get(i).value);
			}
		}
		this.conceptList.setModel(defaultListModel);
	}

	public void initPredictionConceptList(List<Concept> conceptList) {
//      logger.debug("initConceptList(...)");
		DefaultListModel<String> defaultListModel = new DefaultListModel<>();
		if (conceptList != null) {
			for (int i = 0; i < conceptList.size(); i++) {
				defaultListModel.addElement(conceptList.get(i).rank + ":" + conceptList.get(i).value);
			}
		}
//		this.predictionConceptList.setModel(defaultListModel);
	}

//    public void initConceptList(
	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
	// <editor-fold defaultstate="collapsed" desc="Generated
	// <editor-fold defaultstate="collapsed" desc="Generated
	// <editor-fold defaultstate="collapsed" desc="Generated
	// <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        inputLabel = new javax.swing.JLabel();
        conceptLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        conceptList = new javax.swing.JList<>();
        inputTextField = new javax.swing.JTextField();
        trainButton = new javax.swing.JButton();
        inferButton = new javax.swing.JButton();
        stateLabel = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator3 = new javax.swing.JSeparator();
        jLabel8 = new javax.swing.JLabel();
        xLabel = new javax.swing.JLabel();
        yLabel = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        retinaPanel = new com.meritoki.app.desktop.retina.view.panel.RetinaPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        conceptTextArea = new javax.swing.JTextArea();
        doButton = new javax.swing.JButton();
        undoButton = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        beliefList = new javax.swing.JList<>();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        inputList = new javax.swing.JList<>();
        startButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        inputLabel.setText("input:");

        conceptLabel.setText("null");

        conceptList.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(conceptList);

        trainButton.setText("Train");
        trainButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                trainButtonActionPerformed(evt);
            }
        });

        inferButton.setText("Infer");
        inferButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inferButtonActionPerformed(evt);
            }
        });

        stateLabel.setText("null");

        jLabel3.setText("concept:");

        jLabel8.setText("center:");

        xLabel.setText("null");

        yLabel.setText("null");

        jLabel11.setText("x:");

        jLabel12.setText("y:");

        javax.swing.GroupLayout retinaPanelLayout = new javax.swing.GroupLayout(retinaPanel);
        retinaPanel.setLayout(retinaPanelLayout);
        retinaPanelLayout.setHorizontalGroup(
            retinaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 77, Short.MAX_VALUE)
        );
        retinaPanelLayout.setVerticalGroup(
            retinaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 74, Short.MAX_VALUE)
        );

        conceptTextArea.setColumns(20);
        conceptTextArea.setRows(5);
        jScrollPane3.setViewportView(conceptTextArea);

        doButton.setText("Do");
        doButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                doButtonActionPerformed(evt);
            }
        });

        undoButton.setText("Undo");
        undoButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                undoButtonActionPerformed(evt);
            }
        });

        beliefList.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(beliefList);

        jLabel1.setText("belief:");

        inputList.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane4.setViewportView(inputList);

        startButton.setText("Start");
        startButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 147, Short.MAX_VALUE)
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(xLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(yLabel)
                        .addGap(124, 124, 124))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jSeparator3, javax.swing.GroupLayout.Alignment.TRAILING)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(49, 49, 49)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane1)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(doButton, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(undoButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jScrollPane2)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(66, 66, 66)
                        .addComponent(inputLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(inputTextField)
                            .addComponent(trainButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(inferButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane4)
                            .addComponent(startButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(stateLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(conceptLabel)
                        .addGap(154, 154, 154))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(retinaPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(141, 141, 141))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(startButton)
                .addGap(8, 8, 8)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(inputLabel)
                    .addComponent(inputTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(trainButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(conceptLabel)
                    .addComponent(stateLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(inferButton)
                .addGap(18, 18, 18)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(xLabel)
                    .addComponent(yLabel)
                    .addComponent(jLabel11)
                    .addComponent(jLabel12))
                .addGap(24, 24, 24)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(retinaPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(doButton)
                            .addComponent(undoButton)))
                    .addComponent(jLabel1))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void startButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startButtonActionPerformed
        if(this.startButton.getText().equals("Start")) {
        	this.mainFrame.getMachinePanel().start();
        	this.startButton.setText("Stop");
        } else if(this.startButton.getText().equals("Stop")) {
        	this.mainFrame.getMachinePanel().stop();
        	
        	this.startButton.setText("Start");
        }
    }//GEN-LAST:event_startButtonActionPerformed

	private void doButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_doButtonActionPerformed
		this.model.cache.cortex = this.meritoki.document.cortex;
		this.model.cache.conceptScript = this.conceptTextArea.getText();
		try {
			Command command = new ScriptCortex(this.model);
			command.execute();
			this.init();
		} catch (Exception e) {
//			JOptionPane.showMessageDialog(mainFrame, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}// GEN-LAST:event_doButtonActionPerformed

	private void undoButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_undoButtonActionPerformed
		// TODO add your handling code here:
	}// GEN-LAST:event_undoButtonActionPerformed

	private void trainButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_trainButtonActionPerformed
		this.stateLabel.setText("Training...");
		String value = this.inputTextField.getText().trim();
		this.model.cache.concept = new Concept(value);
		this.conceptLabel.setText(value);
	}// GEN-LAST:event_trainButtonActionPerformed

	private void inferButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_inferButtonActionPerformed
		this.stateLabel.setText("Inferring...");
		this.conceptLabel.setText("");
		this.model.cache.concept = null;
	}// GEN-LAST:event_inferButtonActionPerformed

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
			java.util.logging.Logger.getLogger(MeritokiControlDialog.class.getName()).log(java.util.logging.Level.SEVERE, null,
					ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(MeritokiControlDialog.class.getName()).log(java.util.logging.Level.SEVERE, null,
					ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(MeritokiControlDialog.class.getName()).log(java.util.logging.Level.SEVERE, null,
					ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(MeritokiControlDialog.class.getName()).log(java.util.logging.Level.SEVERE, null,
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

		/* Create and display the dialog */
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				MeritokiControlDialog dialog = new MeritokiControlDialog(new javax.swing.JFrame(), true);
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
    private javax.swing.JList<String> beliefList;
    private javax.swing.JLabel conceptLabel;
    private javax.swing.JList<String> conceptList;
    private javax.swing.JTextArea conceptTextArea;
    private javax.swing.JButton doButton;
    private javax.swing.JButton inferButton;
    private javax.swing.JLabel inputLabel;
    private javax.swing.JList<String> inputList;
    private javax.swing.JTextField inputTextField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator3;
    private com.meritoki.app.desktop.retina.view.panel.RetinaPanel retinaPanel;
    private javax.swing.JButton startButton;
    private javax.swing.JLabel stateLabel;
    private javax.swing.JButton trainButton;
    private javax.swing.JButton undoButton;
    private javax.swing.JLabel xLabel;
    private javax.swing.JLabel yLabel;
    // End of variables declaration//GEN-END:variables
}

//if (cortex instanceof Group) {
//Group group = (Group) cortex;
//conceptList = group.getLevel().getCoincidenceConceptList();
//} else if (cortex instanceof Network) {
//Network network = (Network) cortex;
//conceptList = network.getRootLevel().getCoincidenceConceptList();
//}

//public void initList() {
//Document document = (this.model != null) ? this.model.getDocument() : null;
//Network network = (document != null) ? document.getNetwork() : null;
//List<Concept> conceptList = (network.getLevelList().size() > 0)
//		? network.getRootLevel().getCoincidenceConceptList()
//		: null;
//// List<Concept> predictionConceptList = (network.getLevelStack().size() >
//// 0)?network.getLevelStack().get(network.getIndex()).getPredictionConceptList():null;
//this.initConceptList(conceptList);
//// this.initPredictionConceptList(predictionConceptList);
//}