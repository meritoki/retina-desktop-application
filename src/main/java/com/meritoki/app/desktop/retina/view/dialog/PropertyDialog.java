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

import com.meritoki.app.desktop.retina.model.Model;
import com.meritoki.app.desktop.retina.model.document.Page;
import com.meritoki.app.desktop.retina.view.frame.MainFrame;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author jorodriguez
 */
public class PropertyDialog extends javax.swing.JDialog {

	/**
	 * Logger for class.
	 */
	static Logger logger = LogManager.getLogger(PropertyDialog.class.getName());
	/**
	 * Model for class.
	 */
	private Model model = null;
	/**
	 * Reference to Main Frame
	 */
	private MainFrame mainFrame = null;

	/**
	 * Creates new form PropertyDialog
	 */
	public PropertyDialog(java.awt.Frame parent, boolean modal) {
		super(parent, modal);
		this.mainFrame = (MainFrame) parent;
		initComponents();
	}

	public void setModel(Model model) {
		this.model = model;
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
	// <editor-fold defaultstate="collapsed" desc="Generated
	// Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		defaultScaleLabel = new javax.swing.JLabel();
		defaultScaleTextField = new javax.swing.JTextField();
		setButton = new javax.swing.JButton();
		jLabel1 = new javax.swing.JLabel();
		defaultThresholdTextField = new javax.swing.JTextField();

		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

		defaultScaleLabel.setText("Default Scale:");

		setButton.setText("Set");
		setButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				setButtonActionPerformed(evt);
			}
		});

		jLabel1.setText("Default Threshold:");

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout
				.createSequentialGroup().addContainerGap()
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addComponent(setButton, javax.swing.GroupLayout.Alignment.TRAILING,
								javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
								Short.MAX_VALUE)
						.addGroup(layout.createSequentialGroup()
								.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
										.addComponent(jLabel1).addComponent(defaultScaleLabel))
								.addGap(4, 4, 4)
								.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
										.addComponent(defaultScaleTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 96,
												Short.MAX_VALUE)
										.addComponent(defaultThresholdTextField))))
				.addContainerGap()));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addContainerGap()
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(defaultScaleLabel).addComponent(defaultScaleTextField,
										javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(jLabel1).addComponent(defaultThresholdTextField,
										javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 22, Short.MAX_VALUE)
						.addComponent(setButton).addContainerGap()));

		pack();
	}// </editor-fold>//GEN-END:initComponents

	private void setButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_setButtonActionPerformed
		String defaultScaleText = this.defaultScaleTextField.getText().trim();
		if (!defaultScaleText.equals("")) {
			double defaultScale = Double.parseDouble(defaultScaleText);
			for (Page page : this.model.document.pageList) {
				page.setScale(defaultScale);
			}
		}
		String defaultThresholdValue = this.defaultThresholdTextField.getText().trim();
		if (!defaultThresholdValue.equals("")) {
			double defaultThreshold = Double.parseDouble(defaultThresholdValue);
			for (Page page : this.model.document.pageList) {

				page.setThreshold(defaultThreshold);
			}
		}
		this.mainFrame.init();
	}// GEN-LAST:event_setButtonActionPerformed

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
			java.util.logging.Logger.getLogger(PropertyDialog.class.getName()).log(java.util.logging.Level.SEVERE, null,
					ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(PropertyDialog.class.getName()).log(java.util.logging.Level.SEVERE, null,
					ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(PropertyDialog.class.getName()).log(java.util.logging.Level.SEVERE, null,
					ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(PropertyDialog.class.getName()).log(java.util.logging.Level.SEVERE, null,
					ex);
		}
		// </editor-fold>

		/* Create and display the dialog */
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				PropertyDialog dialog = new PropertyDialog(new javax.swing.JFrame(), true);
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
	private javax.swing.JLabel defaultScaleLabel;
	private javax.swing.JTextField defaultScaleTextField;
	private javax.swing.JTextField defaultThresholdTextField;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JButton setButton;
	// End of variables declaration//GEN-END:variables
}
