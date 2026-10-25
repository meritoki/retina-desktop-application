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
package com.meritoki.app.desktop.retina.view.dialog.zooniverse;

import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.meritoki.app.desktop.retina.model.Model;
import com.meritoki.app.desktop.retina.model.document.Query;
import com.meritoki.app.desktop.retina.model.provider.zooniverse.Credential;
import com.meritoki.app.desktop.retina.model.provider.zooniverse.Project;
import com.meritoki.app.desktop.retina.model.provider.zooniverse.SubjectSet;
import com.meritoki.app.desktop.retina.model.provider.zooniverse.Workflow;
import com.meritoki.app.desktop.retina.model.provider.zooniverse.Zooniverse;
import com.meritoki.app.desktop.retina.view.dialog.LoadDialog;
import com.meritoki.app.desktop.retina.view.frame.MainFrame;

/**
 *
 * @author osvaldo.rodriguez
 */
public class ZooniverseExportDialog extends javax.swing.JDialog {

	private static final long serialVersionUID = 3200033012988617201L;
	private static Logger logger = LogManager.getLogger(ZooniverseExportDialog.class.getName());
	public Model model;
	private MainFrame mainFrame;
	public LoadDialog loadDialog;
	public Zooniverse zooniverse;

	/**
	 * Instantiate new Zooniverse Export Dialog
	 */
	public ZooniverseExportDialog(java.awt.Frame parent, boolean modal) {
		super(parent, modal);
		this.initComponents();
		this.mainFrame = (MainFrame) parent;
		this.loadDialog = new LoadDialog(parent, true);
	}

	private void showLoad() {
		System.out.println("showLoad()");
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				loadDialog.setVisible(true);
			}
		});
	}

	private void hideLoad() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				loadDialog.setVisible(false);
			}
		});
	}

	public void setModel(Model model) {
		logger.debug("setModel(" + model + ")");
		this.model = model;
		this.init();
	}

	public void init() {
		logger.debug("init()");
		this.initZooniverse();
		this.initComboBox();
	}

	public void initZooniverse() {
		this.zooniverse = ((Zooniverse) this.model.system.providerMap.get("zooniverse"));
	}

	public void initComboBox() {
		List<Project> projectList = (this.zooniverse != null) ? this.zooniverse.getProjectList() : null;
		this.initSearchProjectComboBox(projectList);
		this.initProjectWorkflowComboBox(new ArrayList<Workflow>());

	}

	public void initSearchProjectComboBox(
			List<com.meritoki.app.desktop.retina.model.provider.zooniverse.Project> projectList) {
		String[] array = new String[0];
		if (projectList != null && projectList.size() > 0) {
			array = new String[projectList.size()];
			for (int i = 0; i < array.length; i++) {
				array[i] = projectList.get(i).name;
			}
		}
		this.searchProjectComboBox.setModel(new DefaultComboBoxModel(array));
	}

	public void initProjectWorkflowComboBox(
			List<com.meritoki.app.desktop.retina.model.provider.zooniverse.Workflow> workflowList) {
		String[] array = new String[0];
		if (workflowList != null && workflowList.size() > 0) {
			array = new String[workflowList.size()];
			for (int i = 0; i < array.length; i++) {
				array[i] = workflowList.get(i).title;
			}
		}
		this.projectWorkflowComboBox.setModel(new DefaultComboBoxModel(array));
	}



//	public List<Shape> getShapeList(Query query) {
//		MemoryController.log();
//		List<Shape> shapeList = null;
//		if (query.pageIndexList != null) {
//			shapeList = new ArrayList<>();
//			if (query.pageIndexList.contains(-1)) {
//				shapeList = this.model.document.getGridShapeList();
//			} else {
//				for (Integer i : query.pageIndexList) {
//					if (this.model.document.setIndex(i)) {
//						Page p = this.model.document.getPage();
//						if (p != null) {
//							List<Shape> tmpShapeList = p.getGridShapeList();
//							for (Shape s : tmpShapeList) {
//								s.bufferedImage = this.model.document
//										.getShapeBufferedImage(p.getScaledBufferedImage(this.model), s);
//							}
//							shapeList.addAll(tmpShapeList);
//						}
//					}
//				}
//			}
//			ListIterator<Shape> shapeListIterator = shapeList.listIterator();
//			while (shapeListIterator.hasNext()) {
//				Shape shape = shapeListIterator.next();
//				switch (shape.data.unit.type) {
//				case TIME: {
//					if (!query.timeFlag) {
//						shapeListIterator.remove();
//					}
//					break;
//				}
//				case SPACE: {
//					if (!query.spaceFlag) {
//						shapeListIterator.remove();
//					}
//					break;
//				}
//				case ENERGY: {
//					if (!query.energyFlag) {
//						shapeListIterator.remove();
//					}
//					break;
//				}
//				case LANGUAGE: {
//					if (!query.languageFlag) {
//						shapeListIterator.remove();
//					}
//					break;
//				}
//				default:
//					break;
//				}
//			}
//			if (query.randomFlag) {
//				Collections.shuffle(shapeList);
//			}
//		}
//		MemoryController.log();
//		return shapeList;
//	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
	// <editor-fold defaultstate="collapsed" desc="Generated
	// <editor-fold defaultstate="collapsed" desc="Generated
	// Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		jSeparator3 = new javax.swing.JSeparator();
		jSeparator4 = new javax.swing.JSeparator();
		jSeparator6 = new javax.swing.JSeparator();
		jLabel1 = new javax.swing.JLabel();
		userNameTextField = new javax.swing.JTextField();
		jLabel2 = new javax.swing.JLabel();
		passwordTextField = new javax.swing.JTextField();
		setCredential = new javax.swing.JButton();
		projectWorkflowComboBox = new javax.swing.JComboBox();
		jLabel4 = new javax.swing.JLabel();
		jLabel7 = new javax.swing.JLabel();
		updateProjectWorkflowButton = new javax.swing.JButton();
		jLabel8 = new javax.swing.JLabel();
		jLabel9 = new javax.swing.JLabel();
		projectSearchTextField = new javax.swing.JTextField();
		findProjectButton = new javax.swing.JButton();
		searchProjectComboBox = new javax.swing.JComboBox();
		jLabel10 = new javax.swing.JLabel();
		subjectSetNameTextField = new javax.swing.JTextField();
		jLabel11 = new javax.swing.JLabel();
		exportButton = new javax.swing.JButton();
		pageTextField = new javax.swing.JTextField();
		jLabel12 = new javax.swing.JLabel();
		randomCheckBox = new javax.swing.JCheckBox();
		orderLabel = new javax.swing.JLabel();
		typeLabel = new javax.swing.JLabel();
		languageCheckBox = new javax.swing.JCheckBox();
		timeCheckBox = new javax.swing.JCheckBox();
		spaceCheckBox = new javax.swing.JCheckBox();
		energyCheckBox = new javax.swing.JCheckBox();
		jSeparator9 = new javax.swing.JSeparator();
		jSeparator10 = new javax.swing.JSeparator();
		jSeparator11 = new javax.swing.JSeparator();
		jLabel5 = new javax.swing.JLabel();

		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

		jLabel1.setText("Username:");

		jLabel2.setText("Password:");

		setCredential.setText("Set");
		setCredential.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				setCredentialActionPerformed(evt);
			}
		});

		projectWorkflowComboBox.setModel(
				new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

		jLabel4.setText("Workflow");

		jLabel7.setText("Project");

		updateProjectWorkflowButton.setText("Update");
		updateProjectWorkflowButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				updateProjectWorkflowButtonActionPerformed(evt);
			}
		});

		jLabel8.setText("Subject Set");

		jLabel9.setText("Search:");

		findProjectButton.setText("Find");
		findProjectButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				findProjectButtonActionPerformed(evt);
			}
		});

		searchProjectComboBox.setModel(
				new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

		jLabel10.setText("List:");

		jLabel11.setText("Title:");

		exportButton.setText("Export");
		exportButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				exportButtonActionPerformed(evt);
			}
		});

		jLabel12.setText("Page(s):");

		randomCheckBox.setText("Random");

		orderLabel.setText("Order:");

		typeLabel.setText("Type:");

		languageCheckBox.setText("Lang");

		timeCheckBox.setText("Time");

		spaceCheckBox.setText("Space");

		energyCheckBox.setText("Energy");

		jLabel5.setText("Title:");

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				javax.swing.GroupLayout.Alignment.TRAILING,
				layout.createSequentialGroup().addGap(0, 0, Short.MAX_VALUE)
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
										layout.createSequentialGroup().addComponent(jLabel4).addGap(136, 136, 136))
								.addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
										layout.createSequentialGroup().addComponent(jLabel7).addGap(149, 149, 149))))
				.addGroup(layout.createSequentialGroup()
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addGroup(layout.createSequentialGroup().addGap(24, 24, 24)
										.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
												.addComponent(jLabel1).addComponent(jLabel2))
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addGroup(layout
												.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
												.addComponent(setCredential, javax.swing.GroupLayout.DEFAULT_SIZE, 334,
														Short.MAX_VALUE)
												.addComponent(passwordTextField).addComponent(jSeparator9)
												.addComponent(userNameTextField)))
								.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
										.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
												.addComponent(
														jSeparator11, javax.swing.GroupLayout.Alignment.TRAILING,
														javax.swing.GroupLayout.PREFERRED_SIZE, 332,
														javax.swing.GroupLayout.PREFERRED_SIZE)
												.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout
														.createSequentialGroup()
														.addGroup(layout.createParallelGroup(
																javax.swing.GroupLayout.Alignment.TRAILING)
																.addComponent(jLabel9).addComponent(jLabel10)
																.addComponent(jLabel5))
														.addPreferredGap(
																javax.swing.LayoutStyle.ComponentPlacement.RELATED)
														.addGroup(layout
																.createParallelGroup(
																		javax.swing.GroupLayout.Alignment.LEADING,
																		false)
																.addComponent(jSeparator10,
																		javax.swing.GroupLayout.Alignment.TRAILING)
																.addComponent(projectWorkflowComboBox,
																		javax.swing.GroupLayout.Alignment.TRAILING, 0,
																		332, Short.MAX_VALUE)
																.addComponent(updateProjectWorkflowButton,
																		javax.swing.GroupLayout.Alignment.TRAILING,
																		javax.swing.GroupLayout.DEFAULT_SIZE,
																		javax.swing.GroupLayout.DEFAULT_SIZE,
																		Short.MAX_VALUE)
																.addComponent(searchProjectComboBox, 0,
																		javax.swing.GroupLayout.DEFAULT_SIZE,
																		Short.MAX_VALUE)
																.addComponent(projectSearchTextField)
																.addComponent(
																		findProjectButton,
																		javax.swing.GroupLayout.DEFAULT_SIZE,
																		javax.swing.GroupLayout.DEFAULT_SIZE,
																		Short.MAX_VALUE)))
												.addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
														layout.createSequentialGroup().addComponent(jLabel8).addGap(119,
																119, 119))
												.addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
														layout.createSequentialGroup().addGroup(layout
																.createParallelGroup(
																		javax.swing.GroupLayout.Alignment.TRAILING)
																.addGroup(layout.createSequentialGroup()
																		.addComponent(jLabel11)
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.RELATED))
																.addGroup(layout.createSequentialGroup()
																		.addGroup(layout.createParallelGroup(
																				javax.swing.GroupLayout.Alignment.TRAILING)
																				.addComponent(typeLabel)
																				.addComponent(jLabel12)
																				.addComponent(orderLabel))
																		.addGap(12, 12, 12)))
																.addGroup(layout.createParallelGroup(
																		javax.swing.GroupLayout.Alignment.LEADING,
																		false)
																		.addComponent(exportButton,
																				javax.swing.GroupLayout.Alignment.TRAILING,
																				javax.swing.GroupLayout.DEFAULT_SIZE,
																				331, Short.MAX_VALUE)
																		.addGroup(layout.createSequentialGroup()
																				.addComponent(timeCheckBox)
																				.addPreferredGap(
																						javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																				.addComponent(spaceCheckBox)
																				.addPreferredGap(
																						javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																				.addComponent(energyCheckBox)
																				.addPreferredGap(
																						javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																				.addComponent(languageCheckBox))
																		.addComponent(pageTextField)
																		.addComponent(subjectSetNameTextField)
																		.addComponent(randomCheckBox))))))
						.addContainerGap()));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout
				.createSequentialGroup().addContainerGap()
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel1)
						.addComponent(userNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel2)
						.addComponent(passwordTextField, javax.swing.GroupLayout.PREFERRED_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(setCredential)
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
				.addComponent(jSeparator9, javax.swing.GroupLayout.PREFERRED_SIZE, 10,
						javax.swing.GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel7)
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
						.addComponent(projectSearchTextField, javax.swing.GroupLayout.PREFERRED_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addComponent(jLabel9))
				.addGap(7, 7, 7).addComponent(findProjectButton)
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
						.addComponent(searchProjectComboBox, javax.swing.GroupLayout.PREFERRED_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addComponent(jLabel10))
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
				.addComponent(jSeparator11, javax.swing.GroupLayout.PREFERRED_SIZE, 10,
						javax.swing.GroupLayout.PREFERRED_SIZE)
				.addGap(2, 2, 2).addComponent(jLabel4)
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
				.addComponent(updateProjectWorkflowButton).addGap(9, 9, 9)
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
						.addComponent(projectWorkflowComboBox, javax.swing.GroupLayout.PREFERRED_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addComponent(jLabel5))
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
				.addComponent(jSeparator10, javax.swing.GroupLayout.PREFERRED_SIZE, 10,
						javax.swing.GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel8)
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
						.addComponent(subjectSetNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addComponent(jLabel11))
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
						.addComponent(pageTextField, javax.swing.GroupLayout.PREFERRED_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addComponent(jLabel12))
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
						.addComponent(timeCheckBox).addComponent(spaceCheckBox).addComponent(energyCheckBox)
						.addComponent(languageCheckBox).addComponent(typeLabel))
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
						.addComponent(randomCheckBox).addComponent(orderLabel))
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(exportButton)
				.addContainerGap(76, Short.MAX_VALUE)));

		pack();
	}// </editor-fold>//GEN-END:initComponents

	private void setCredentialActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_setCredentialActionPerformed
		Runnable runnable = new Runnable() {
			public void run() {
				showLoad();
				Credential credential = new Credential();
				credential.password = passwordTextField.getText();
				credential.userName = userNameTextField.getText();
				if (zooniverse != null) {
					zooniverse.setCredential(credential);
					try {
						zooniverse.setConfig();
						Thread.sleep(1000);
					} catch (Exception e) {
						JOptionPane.showMessageDialog(mainFrame, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
				hideLoad();
			}
		};
		new Thread(runnable).start();
	}// GEN-LAST:event_setCredentialActionPerformed

	private void findProjectButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_findProjectButtonActionPerformed
		Runnable runnable = new Runnable() {
			public void run() {
				String query = projectSearchTextField.getText().trim();
				if (zooniverse != null) {
					if (!query.isEmpty()) {
						showLoad();
						try {
							zooniverse.findProject(query);
							initSearchProjectComboBox(zooniverse.getProjectList());
						} catch (Exception e) {
							JOptionPane.showMessageDialog(mainFrame, e.getMessage(), "Error",
									JOptionPane.ERROR_MESSAGE);
						} finally {
							hideLoad();
						}
					} else {
						JOptionPane.showMessageDialog(mainFrame, "Search query is empty");
					}
				}
			}
		};
		new Thread(runnable).start();
	}// GEN-LAST:event_findProjectButtonActionPerformed

	private void updateProjectWorkflowButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_updateProjectWorkflowButtonActionPerformed
		Runnable runnable = new Runnable() {
			public void run() {
				if (zooniverse != null) {
					showLoad();
					String projectName = (String) searchProjectComboBox.getSelectedItem();
					for (Project p : zooniverse.getProjectList()) {
						if (p.name.equals(projectName)) {
							try {
								zooniverse.updateProjectWorkflowList(p);
								initProjectWorkflowComboBox(p.getWorkflowList());
							} catch (Exception e) {
								JOptionPane.showMessageDialog(mainFrame, e.getMessage(), "Error",
										JOptionPane.ERROR_MESSAGE);
							}
							break;
						}
					}
					hideLoad();
				}
			}
		};
		new Thread(runnable).start();
	}// GEN-LAST:event_updateProjectWorkflowButtonActionPerformed

	private void exportButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_uploadButtonActionPerformed
		Runnable runnable = new Runnable() {
			public void run() {
				showLoad();
				String subjectSetTitle = subjectSetNameTextField.getText().trim().replaceAll(" ", "_");
				String projectName = (String) searchProjectComboBox.getSelectedItem();
				String workflowTitle = (String) projectWorkflowComboBox.getSelectedItem();
				String indexString = pageTextField.getText();
				Query query = new Query();
				query.map.put("index",indexString);
				query.map.put("time", String.valueOf(timeCheckBox.isSelected()));
				query.map.put("space", String.valueOf(spaceCheckBox.isSelected()));
				query.map.put("energy", String.valueOf(energyCheckBox.isSelected()));
				query.map.put("language", String.valueOf(languageCheckBox.isSelected()));
				query.map.put("random", String.valueOf(randomCheckBox.isSelected()));
				
//				query.timeFlag = timeCheckBox.isSelected();
//				query.spaceFlag = spaceCheckBox.isSelected();
//				query.energyFlag = energyCheckBox.isSelected();
//				query.languageFlag = languageCheckBox.isSelected();
//				query.randomFlag = randomCheckBox.isSelected();
				try {
					model.document.setQuery(query);
					SubjectSet subjectSet = zooniverse.getSubjectSet(subjectSetTitle, model.document);
					Project project = zooniverse.getProject(projectName);
					Workflow workflow = project.getWorkflow(workflowTitle);
					zooniverse.upload(project, workflow, subjectSet);
					model.document.setQuery(null);
					String report = zooniverse.report.toString();
					if(report.length() > 0) {
						JTextArea text = new JTextArea(5, 20);
						text.setText(report);
						text.setEnabled(true);
						JOptionPane.showMessageDialog(mainFrame, text);
					}
				} catch (Exception e) {
					JOptionPane.showMessageDialog(mainFrame, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
//				List<Shape> shapeList = getShapeList(query);
//				if (shapeList != null) {
//					try {
//						
//					} catch (Exception e) {
//						JOptionPane.showMessageDialog(mainFrame, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
//					}
//				}
				hideLoad();
			}
		};
		new Thread(runnable).start();
	}// GEN-LAST:event_uploadButtonActionPerformed

	/**
	 * @param args the command line arguments
	 */
//    public static void main(String args[]) {
//        /* Set the Nimbus look and feel */
//        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
//         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(Zooniverse.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(Zooniverse.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(Zooniverse.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(Zooniverse.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the dialog */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                Zooniverse dialog = new Zooniverse(new javax.swing.JFrame(), true);
//                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
//                    @Override
//                    public void windowClosing(java.awt.event.WindowEvent e) {
//                        System.exit(0);
//                    }
//                });
//                dialog.setVisible(true);
//            }
//        });
//    }

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JCheckBox energyCheckBox;
	private javax.swing.JButton exportButton;
	private javax.swing.JButton findProjectButton;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel10;
	private javax.swing.JLabel jLabel11;
	private javax.swing.JLabel jLabel12;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JLabel jLabel4;
	private javax.swing.JLabel jLabel5;
	private javax.swing.JLabel jLabel7;
	private javax.swing.JLabel jLabel8;
	private javax.swing.JLabel jLabel9;
	private javax.swing.JSeparator jSeparator10;
	private javax.swing.JSeparator jSeparator11;
	private javax.swing.JSeparator jSeparator3;
	private javax.swing.JSeparator jSeparator4;
	private javax.swing.JSeparator jSeparator6;
	private javax.swing.JSeparator jSeparator9;
	private javax.swing.JCheckBox languageCheckBox;
	private javax.swing.JLabel orderLabel;
	private javax.swing.JTextField pageTextField;
	private javax.swing.JTextField passwordTextField;
	private javax.swing.JTextField projectSearchTextField;
	private javax.swing.JComboBox projectWorkflowComboBox;
	private javax.swing.JCheckBox randomCheckBox;
	private javax.swing.JComboBox searchProjectComboBox;
	private javax.swing.JButton setCredential;
	private javax.swing.JCheckBox spaceCheckBox;
	private javax.swing.JTextField subjectSetNameTextField;
	private javax.swing.JCheckBox timeCheckBox;
	private javax.swing.JLabel typeLabel;
	private javax.swing.JButton updateProjectWorkflowButton;
	private javax.swing.JTextField userNameTextField;
	// End of variables declaration//GEN-END:variables

}
