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
package com.meritoki.app.desktop.retina.view.dialog.zooniverse;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.meritoki.app.desktop.retina.controller.node.NodeController;
import com.meritoki.app.desktop.retina.model.Model;
import com.meritoki.app.desktop.retina.model.document.Page;
import com.meritoki.app.desktop.retina.model.document.Shape;
import com.meritoki.app.desktop.retina.model.provider.Provider;
import com.meritoki.app.desktop.retina.model.provider.zooniverse.Credential;
import com.meritoki.app.desktop.retina.model.provider.zooniverse.Project;
import com.meritoki.app.desktop.retina.model.provider.zooniverse.SubjectSet;
import com.meritoki.app.desktop.retina.model.provider.zooniverse.Workflow;
import com.meritoki.app.desktop.retina.model.provider.zooniverse.Zooniverse;
import com.meritoki.app.desktop.retina.view.dialog.LoadDialog;

/**
 *
 * @author osvaldo.rodriguez
 */
public class ZooniverseExportDialog extends javax.swing.JDialog {

    private static final long serialVersionUID = 3200033012988617201L;
    private static Logger logger = LogManager.getLogger(ZooniverseExportDialog.class.getName());
    public Model model;
    public LoadDialog loadDialog;
    public Zooniverse zooniverse;

    /**
     * Instantiate new Zooniverse Export Dialog
     */
    public ZooniverseExportDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        this.initComponents();
        this.loadDialog = new LoadDialog(parent, true);
    }

    private void showLoad() {
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
        for (Provider provider : this.model.system.providerList) {
            if (provider instanceof Zooniverse) {
                this.zooniverse = (Zooniverse) provider;
            }
        }
    }

    public void initComboBox() {
        List<Project> projectList = (this.zooniverse != null) ? this.zooniverse.getProjectList() : null;
        this.initProjectComboBox(projectList);
        this.initSearchProjectComboBox(new ArrayList<Project>());
        this.initProjectWorkflowComboBox(new ArrayList<Workflow>());

    }

    public void initProjectComboBox(List<Project> projectList) {
        String[] array = new String[0];
        if (projectList != null && projectList.size() > 0) {
            array = new String[projectList.size()];
            for (int i = 0; i < array.length; i++) {
                array[i] = projectList.get(i).name;
            }
        }
        this.projectComboBox.setModel(new DefaultComboBoxModel(array));
    }

    public void initSearchProjectComboBox(List<com.meritoki.app.desktop.retina.model.provider.zooniverse.Project> projectList) {
        String[] array = new String[0];
        if (projectList != null && projectList.size() > 0) {
            array = new String[projectList.size()];
            for (int i = 0; i < array.length; i++) {
                array[i] = projectList.get(i).name;
            }
        }
        this.searchProjectComboBox.setModel(new DefaultComboBoxModel(array));
    }

    public void initProjectWorkflowComboBox(List<com.meritoki.app.desktop.retina.model.provider.zooniverse.Workflow> workflowList) {
        String[] array = new String[0];
        if (workflowList != null && workflowList.size() > 0) {
            array = new String[workflowList.size()];
            for (int i = 0; i < array.length; i++) {
                array[i] = workflowList.get(i).title;
            }
        }
        this.projectWorkflowComboBox.setModel(new DefaultComboBoxModel(array));
    }

    public String getSubjectSetPath() {
        return NodeController.getRetinaHome() + NodeController.getSeperator() + "provider" + NodeController.getSeperator() + "zooniverse" + NodeController.getSeperator() + "subject-set" + NodeController.getSeperator();
    }
    
	public List<Integer> parsePages(String value) throws Exception {
		List<Integer> pageList = new ArrayList<>();
		value = value.toLowerCase();
		value.trim();
		if (value.contains("all")) {
			if (value.equals("all")) {
				pageList.add(-1);
			} else {
				throw new Exception("Invalid page(s)");
			}
		} else {
			String[] commaArray = value.split(",");
			for (String c : commaArray) {
				c.trim();
				if (c.contains("-")) {
					String[] dashArray = c.split("-");
					try {
						int a = Integer.parseInt(dashArray[0].trim());
						int b = Integer.parseInt(dashArray[1].trim());
						if (a < b) {
							for (int i = a; i <= b; i++) {
								pageList.add(i);
							}
						}
					} catch (Exception e) {
						throw new Exception("Not integer page(s)");
					}
				} else {
					try {
						int a = Integer.parseInt(c);
						pageList.add(a);
					} catch (Exception e) {
						throw new Exception("Not integer page(s)");
					}
				}
			}
		}
		logger.info("parsePages(" + value + ") pageList=" + pageList);
		return pageList;
	}


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSeparator3 = new javax.swing.JSeparator();
        jSeparator4 = new javax.swing.JSeparator();
        jSeparator6 = new javax.swing.JSeparator();
        jLabel1 = new javax.swing.JLabel();
        userNameTextField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        passwordTextField = new javax.swing.JTextField();
        setCredential = new javax.swing.JButton();
        projectComboBox = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        projectWorkflowComboBox = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        projectTitleTextField = new javax.swing.JTextField();
        projectDescriptionTextField = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        addNewProject = new javax.swing.JButton();
        updateProjectWorkflowButton = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        projectSearchTextField = new javax.swing.JTextField();
        findProjectButton = new javax.swing.JButton();
        searchProjectComboBox = new javax.swing.JComboBox();
        jLabel10 = new javax.swing.JLabel();
        addProjectButton = new javax.swing.JButton();
        subjectSetNameTextField = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        uploadButton = new javax.swing.JButton();
        pageTextField = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        randomCheckBox = new javax.swing.JCheckBox();
        orderLabel = new javax.swing.JLabel();
        typeLabel = new javax.swing.JLabel();
        languageCheckBox = new javax.swing.JCheckBox();
        timeCheckBox = new javax.swing.JCheckBox();
        spaceCheckBox = new javax.swing.JCheckBox();
        energyCheckBox = new javax.swing.JCheckBox();
        jSeparator8 = new javax.swing.JSeparator();
        jSeparator9 = new javax.swing.JSeparator();
        jSeparator10 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setText("Username:");

        jLabel2.setText("Password:");

        setCredential.setText("Set");
        setCredential.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setCredentialActionPerformed(evt);
            }
        });

        projectComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel3.setText("List:");

        projectWorkflowComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel4.setText("Workflow:");

        jLabel5.setText("Title:");

        projectTitleTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                projectTitleTextFieldActionPerformed(evt);
            }
        });

        jLabel6.setText("Description:");

        jLabel7.setText("Project");

        addNewProject.setText("Add");
        addNewProject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addNewProjectActionPerformed(evt);
            }
        });

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

        searchProjectComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel10.setText("List:");

        addProjectButton.setText("Add");
        addProjectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addProjectButtonActionPerformed(evt);
            }
        });

        jLabel11.setText("Title:");

        uploadButton.setText("Upload");
        uploadButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                uploadButtonActionPerformed(evt);
            }
        });

        jLabel12.setText("Page(s):");

        randomCheckBox.setText("Random");

        orderLabel.setText("Order:");

        typeLabel.setText("Type:");

        languageCheckBox.setText("Language");

        timeCheckBox.setText("Time");

        spaceCheckBox.setText("Space");

        energyCheckBox.setText("Energy");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(searchProjectComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 338, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(projectWorkflowComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 337, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(addProjectButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 338, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(projectComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 337, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(updateProjectWorkflowButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 337, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jSeparator8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 337, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jSeparator10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 337, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(projectSearchTextField, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(findProjectButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 338, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(70, 70, 70)
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(subjectSetNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 337, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel5)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                        .addComponent(jLabel6)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(projectDescriptionTextField)
                                    .addComponent(projectTitleTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 336, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel2))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(setCredential, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(passwordTextField)
                                    .addComponent(jSeparator9)
                                    .addComponent(userNameTextField)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(39, 39, 39)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(typeLabel)
                                    .addComponent(orderLabel))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(randomCheckBox)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(timeCheckBox)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(spaceCheckBox)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(energyCheckBox)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(languageCheckBox))))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(31, 31, 31)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addComponent(jLabel12)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(pageTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 337, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(addNewProject, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 336, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 336, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(uploadButton, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE)))))))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addGap(150, 150, 150))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addGap(136, 136, 136))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(userNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(passwordTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(setCredential)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator9, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(projectTitleTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(projectDescriptionTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(addNewProject)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 7, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(projectSearchTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addGap(7, 7, 7)
                .addComponent(findProjectButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(searchProjectComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(addProjectButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator8, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(projectComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(projectWorkflowComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(updateProjectWorkflowButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator10, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(subjectSetNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(pageTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(typeLabel)
                    .addComponent(timeCheckBox)
                    .addComponent(spaceCheckBox)
                    .addComponent(energyCheckBox)
                    .addComponent(languageCheckBox))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(randomCheckBox)
                    .addComponent(orderLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(uploadButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void projectTitleTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_projectTitleTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_projectTitleTextFieldActionPerformed

    private void addNewProjectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addNewProjectActionPerformed
        String title = this.projectTitleTextField.getText();
        String description = this.projectDescriptionTextField.getText();
        if (zooniverse != null) {
            Project project = new Project(title, description);
            zooniverse.createProject(project);
            this.initProjectComboBox(zooniverse.getProjectList());
        }
    }//GEN-LAST:event_addNewProjectActionPerformed

    private void setCredentialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setCredentialActionPerformed
        Credential credential = new Credential();
        credential.password = this.passwordTextField.getText();
        credential.userName = this.userNameTextField.getText();
        if (zooniverse != null) {
            zooniverse.setCredential(credential);
            zooniverse.setConfig();
        }
    }//GEN-LAST:event_setCredentialActionPerformed

    private void findProjectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_findProjectButtonActionPerformed
        String query = this.projectSearchTextField.getText().trim();
        if (zooniverse != null) {
            if (!query.isEmpty()) {
                this.showLoad();
               	this.zooniverse.projectList = zooniverse.getProjectList(query);
                this.initSearchProjectComboBox(this.zooniverse.projectList);
                this.hideLoad();
            } else {
                JOptionPane.showMessageDialog(this, "Search query is empty");
            }
        }
    }//GEN-LAST:event_findProjectButtonActionPerformed

    /**
     * Function is invoked when pressing the addProject button
     *
     * @param evt
     */
    private void addProjectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addProjectButtonActionPerformed
        if (zooniverse != null) {
            String searchProjectName = (String) this.searchProjectComboBox.getSelectedItem();
            if (this.zooniverse.projectList != null) {
                for (Project p : this.zooniverse.projectList) {
                    if (p.name.equals(searchProjectName)) {
                        zooniverse.addProject(p);
                        break;
                    }
                }
            }
            this.initProjectComboBox(zooniverse.getProjectList());
        }
    }//GEN-LAST:event_addProjectButtonActionPerformed

    private void updateProjectWorkflowButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateProjectWorkflowButtonActionPerformed
        if (zooniverse != null) {
            this.showLoad();
            String projectName = (String) this.projectComboBox.getSelectedItem();
            for (Project p : zooniverse.getProjectList()) {
                if (p.name.equals(projectName)) {
                    zooniverse.updateProjectWorkflowList(p);
                    this.initProjectWorkflowComboBox(p.getWorkflowList());
                    break;
                }
            }
            this.hideLoad();
        }
    }//GEN-LAST:event_updateProjectWorkflowButtonActionPerformed

    private void uploadButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_uploadButtonActionPerformed
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String subjectSetTitle = this.subjectSetNameTextField.getText();
        subjectSetTitle.trim();
        subjectSetTitle.replaceAll(" ", "_");
        String projectName = (String) this.projectComboBox.getSelectedItem();
        String workflowTitle = (String) this.projectWorkflowComboBox.getSelectedItem();
        String page = this.pageTextField.getText();
        List<Integer> pageList = null;
        boolean timeFlag = this.timeCheckBox.isSelected();
        boolean spaceFlag = this.spaceCheckBox.isSelected();
        boolean energyFlag = this.energyCheckBox.isSelected();
        boolean languageFlag = this.languageCheckBox.isSelected();
        try {
			pageList = this.parsePages(page);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
        if(pageList != null) {
        	List<Shape> shapeList = new ArrayList<>();
        	if (pageList.contains(-1)) {
				shapeList = this.model.document.getShapeList();
			} else {
				for (Integer i : pageList) {
					if (this.model.document.setIndex(i)) {
						Page p = this.model.document.getPage();
						if (p != null) {
							shapeList = p.getShapeList();
						}
					}
				}
			}
    		ListIterator<Shape> shapeListIterator = shapeList.listIterator();
    		while (shapeListIterator.hasNext()) {
    			Shape shape = shapeListIterator.next();
    			switch(shape.data.unit.type) {
    			case TIME:{
    				if(!timeFlag) {
    					shapeListIterator.remove();
    				}
    				break;
    			}
    			case SPACE:{
    				if(!spaceFlag) {
    					shapeListIterator.remove();
    				}
    				break;
    			}
    			case ENERGY:{
    				if(!energyFlag) {
    					shapeListIterator.remove();
    				}
    				break;
    			}
    			case LANGUAGE:{
    				if(!languageFlag) {
    					shapeListIterator.remove();
    				}
    				break;
    			}
				default:
					break;
    			}
    		}
	        SubjectSet subjectSet = new SubjectSet();
	        subjectSet.title = subjectSetTitle;
	        if (zooniverse != null) {
	            this.showLoad();
	            zooniverse.generateManifest(this.getSubjectSetPath() + timeStamp, shapeList);
	            Project project = zooniverse.getProject(projectName);
	            if (project != null) {
	                Workflow workflow = project.getWorkflow(workflowTitle);
	                zooniverse.createSubjectSet(project.getId(), subjectSet);
	                zooniverse.uploadSubjectSet(subjectSet, this.getSubjectSetPath() + timeStamp, "manifest.csv");
	                zooniverse.workflowUploadSubjectSet(workflow, subjectSet);
	            }
	            this.hideLoad();
	        }
        }
    }//GEN-LAST:event_uploadButtonActionPerformed

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
    private javax.swing.JButton addNewProject;
    private javax.swing.JButton addProjectButton;
    private javax.swing.JCheckBox energyCheckBox;
    private javax.swing.JButton findProjectButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JSeparator jSeparator10;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JSeparator jSeparator9;
    private javax.swing.JCheckBox languageCheckBox;
    private javax.swing.JLabel orderLabel;
    private javax.swing.JTextField pageTextField;
    private javax.swing.JTextField passwordTextField;
    private javax.swing.JComboBox projectComboBox;
    private javax.swing.JTextField projectDescriptionTextField;
    private javax.swing.JTextField projectSearchTextField;
    private javax.swing.JTextField projectTitleTextField;
    private javax.swing.JComboBox projectWorkflowComboBox;
    private javax.swing.JCheckBox randomCheckBox;
    private javax.swing.JComboBox searchProjectComboBox;
    private javax.swing.JButton setCredential;
    private javax.swing.JCheckBox spaceCheckBox;
    private javax.swing.JTextField subjectSetNameTextField;
    private javax.swing.JCheckBox timeCheckBox;
    private javax.swing.JLabel typeLabel;
    private javax.swing.JButton updateProjectWorkflowButton;
    private javax.swing.JButton uploadButton;
    private javax.swing.JTextField userNameTextField;
    // End of variables declaration//GEN-END:variables

}
