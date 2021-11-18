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
package com.meritoki.app.desktop.retina.view.dialog.microsoft;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import com.meritoki.app.desktop.retina.model.Model;
import com.meritoki.app.desktop.retina.model.document.Archive;
import com.meritoki.app.desktop.retina.model.document.Page;

/**
 *
 * @author jorodriguez
 */
public class MicrosoftExportDialog extends javax.swing.JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4647075616714431634L;
	private static Logger logger = LogManager.getLogger(MicrosoftExportDialog.class.getName());
	public Model model;

	/**
	 * Creates new form Export
	 */
	public MicrosoftExportDialog(java.awt.Frame parent, boolean modal) {
		super(parent, modal);
		initComponents();
		this.init();
	}

	public void setModel(Model model) {
		this.model = model;
	}

	public void init() {
		this.initComboBox();
	}

	public void initComboBox() {
		this.initTypeComboBox();
	}

	public void initTypeComboBox() {
		String[] array = { "Word", "Excel" };
		this.typeComboBox.setModel(new DefaultComboBoxModel(array));
	}

	// Create Word
	public void createWord(String fileName, List<String> lines) throws IOException {
		for (String line : lines) {
			// Blank Document
			XWPFDocument document = new XWPFDocument();
			// Write the Document in file system
			FileOutputStream out = new FileOutputStream(new File(fileName + ".docx"));

			// create Paragraph
			XWPFParagraph paragraph = document.createParagraph();
			XWPFRun run = paragraph.createRun();
			run.setText(line);
			document.write(out);

			// Close document
			out.close();
			System.out.println(fileName + ".docx" + " written successfully");
		}
	}

	public void createExcel(String fileName, List<Page> pageList) {
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet;
		System.out.println("Creating excel");
		for (Page page : pageList) {
			sheet = workbook.createSheet(page.uuid);
			Object[][] datatypes = page.getTable().getSpreadsheetObjectArray();
			int rowNum = 0;
			for (Object[] datatype : datatypes) {
				Row row = sheet.createRow(rowNum++);
				int colNum = 0;
				for (Object field : datatype) {
					Cell cell = row.createCell(colNum++);
					if (field instanceof String) {
						cell.setCellValue((String) field);
					} else if (field instanceof Integer) {
						cell.setCellValue((Integer) field);
					}
				}
			}
		}
		try {
			FileOutputStream outputStream = new FileOutputStream(fileName + ".xlsx");
			workbook.write(outputStream);
			workbook.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Done");
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
	// <editor-fold defaultstate="collapsed" desc="Generated
	// Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		typeComboBox = new javax.swing.JComboBox<>();
		jLabel1 = new javax.swing.JLabel();
		pageTextField = new javax.swing.JTextField();
		nameTextField = new javax.swing.JTextField();
		exportButton = new javax.swing.JButton();
		jLabel2 = new javax.swing.JLabel();
		jLabel3 = new javax.swing.JLabel();

		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

		typeComboBox.setModel(
				new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

		jLabel1.setText("Type:");

		exportButton.setText("Export");
		exportButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				exportButtonActionPerformed(evt);
			}
		});

		jLabel2.setText("Page(s):");

		jLabel3.setText("Name:");

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addGap(14, 14, 14)
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
								.addComponent(jLabel1).addComponent(jLabel2).addComponent(jLabel3))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addComponent(exportButton, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(pageTextField).addComponent(typeComboBox, 0, 298, Short.MAX_VALUE)
								.addComponent(nameTextField, javax.swing.GroupLayout.Alignment.TRAILING))
						.addContainerGap()));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addContainerGap()
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(typeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addComponent(jLabel1))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(pageTextField, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addComponent(jLabel2))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(nameTextField, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addComponent(jLabel3))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(exportButton)
						.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

		pack();
	}// </editor-fold>//GEN-END:initComponents

	private void exportButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_exportButtonActionPerformed
		String type = (String) this.typeComboBox.getSelectedItem();
		type = type.toLowerCase();
		String page = this.pageTextField.getText();
		List<Integer> pageList = null;
		try {
			pageList = this.parsePages(page);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
		String name = this.nameTextField.getText();

		if (pageList != null) {
			if (type.equals("word")) {
				List<String> stringList = new ArrayList<String>();
				Archive archive;
				if (pageList.contains(-1)) {
					for (Page p : this.model.document.getPageList()) {
						archive = p.getArchive();
						stringList.addAll(archive.stringList);
					}
				} else {
					for (Integer i : pageList) {
						if (this.model.document.setIndex(i)) {
							Page p = this.model.document.getPage();
							if (p != null) {
								archive = p.getArchive();
								stringList.addAll(archive.stringList);
							}
						}
					}
				}
				try {
					this.createWord(name, stringList);
				} catch (IOException ex) {
					logger.error("IOException " + ex.getMessage());
				}
			} else if (type.equals("excel")) {
				System.out.println("excel");
				List<Page> pList = new ArrayList<>();
				if (pageList.contains(-1)) {
					pList = this.model.document.getPageList();
				} else {
					for (Integer i : pageList) {
						if (this.model.document.setIndex(i)) {
							Page p = this.model.document.getPage();
							pList.add(p);
						}
					}
				}
				this.createExcel(name, pList);
			}
		}
	}// GEN-LAST:event_exportButtonActionPerformed

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
			java.util.logging.Logger.getLogger(MicrosoftExportDialog.class.getName())
					.log(java.util.logging.Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(MicrosoftExportDialog.class.getName())
					.log(java.util.logging.Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(MicrosoftExportDialog.class.getName())
					.log(java.util.logging.Level.SEVERE, null, ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(MicrosoftExportDialog.class.getName())
					.log(java.util.logging.Level.SEVERE, null, ex);
		}
		// </editor-fold>
		// </editor-fold>

		/* Create and display the dialog */
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				MicrosoftExportDialog dialog = new MicrosoftExportDialog(new javax.swing.JFrame(), true);
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
	private javax.swing.JButton exportButton;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JLabel jLabel3;
	private javax.swing.JTextField nameTextField;
	private javax.swing.JTextField pageTextField;
	private javax.swing.JComboBox<String> typeComboBox;
	// End of variables declaration//GEN-END:variables
}
