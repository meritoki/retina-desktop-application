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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.meritoki.app.desktop.retina.model.Model;
import com.meritoki.app.desktop.retina.model.command.Command;
import com.meritoki.app.desktop.retina.model.document.Document;
import com.meritoki.app.desktop.retina.model.pattern.Pattern;

/**
 *
 * @author jorodriguez
 */
public class AttributionDialog extends javax.swing.JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1372194743157163045L;
	/**
	 * Logger for class.
	 */
	private static Logger logger = LogManager.getLogger(AttributionDialog.class.getName());
	/**
	 * Reference to Model class.
	 */
	private Model model;

	public Map<String, Integer> userCountMap = new HashMap<>();

	/**
	 * Creates new form AttributionDialog
	 */
	public AttributionDialog(java.awt.Frame parent, boolean modal) {
		super(parent, modal);
		initComponents();
		this.attributionTextArea.setEditable(false);
	}

	public void setModel(Model model) {
		this.model = model;
		this.init();
	}

	public void init() {
		this.initTextArea();
	}

	public void initTextArea() {
		Document document = (this.model != null) ? this.model.document : null;
		List<Command> undoStack;
		List<Command> logStack;
		if (document != null) {
			userCountMap = new HashMap<>();
			String userName;
			Integer count;
			logStack = document.logStack;
			int logStackSize = 0;
			int undoStackSize = 0;
			for (Command c : logStack) {
				if (this.isAttributable(c)) {
					logStackSize++;
					userName = c.user.name;
					count = userCountMap.get(userName);
					if (count == null) {
						count = 1;
					} else {
						count++;
					}
					userCountMap.put(userName, count);
				}
			}
			undoStack = this.model.pattern.undoStack;
			for (Command c : undoStack) {
				if (this.isAttributable(c)) {
					undoStackSize++;
					userName = c.user.name;
					count = userCountMap.get(userName);
					if (count == null) {
						count = 1;
					} else {
						count++;
					}
					userCountMap.put(userName, count);
				}
			}
			int total = logStackSize + undoStackSize;
			this.attributionTextArea.setText("");
			for (Map.Entry<String, Integer> entry : userCountMap.entrySet()) {
				this.attributionTextArea
						.append(entry.getKey() + " - " + (double) entry.getValue() / (double) total * 100 + "% \n");
			}
		}
	}

	public boolean isAttributable(Command command) {
		boolean flag = false;
		switch (command.name) {
		case "addGrid": {
			flag = true;
			break;
		}
		case "addPage": {
			flag = true;
			break;
		}
		case "addShape": {
			flag = true;
			break;
		}
		case "executeScript": {
			flag = true;
			break;
		}
		case "moveShape": {
			flag = true;
			break;
		}
		case "removeImage": {
			flag = true;
			break;
		}
		case "removePage": {
			flag = true;
			break;
		}
		case "removeShape": {
			flag = true;
			break;
		}
		case "scaleImage": {
			flag = true;
			break;
		}
		case "scalePage": {
			flag = true;
			break;
		}
		case "shiftImage": {
			flag = true;
			break;
		}
		}
		return flag;
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

		jScrollPane1 = new javax.swing.JScrollPane();
		attributionTextArea = new javax.swing.JTextArea();

		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

		attributionTextArea.setColumns(20);
		attributionTextArea.setRows(5);
		jScrollPane1.setViewportView(attributionTextArea);

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addContainerGap()
						.addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 673, Short.MAX_VALUE)
						.addContainerGap()));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addContainerGap()
						.addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 304, Short.MAX_VALUE)
						.addContainerGap()));

		pack();
	}// </editor-fold>//GEN-END:initComponents

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
			java.util.logging.Logger.getLogger(AttributionDialog.class.getName()).log(java.util.logging.Level.SEVERE,
					null, ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(AttributionDialog.class.getName()).log(java.util.logging.Level.SEVERE,
					null, ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(AttributionDialog.class.getName()).log(java.util.logging.Level.SEVERE,
					null, ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(AttributionDialog.class.getName()).log(java.util.logging.Level.SEVERE,
					null, ex);
		}
		// </editor-fold>

		/* Create and display the dialog */
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				AttributionDialog dialog = new AttributionDialog(new javax.swing.JFrame(), true);
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
	private javax.swing.JTextArea attributionTextArea;
	private javax.swing.JScrollPane jScrollPane1;
	// End of variables declaration//GEN-END:variables
}
