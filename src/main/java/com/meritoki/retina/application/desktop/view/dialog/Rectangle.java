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
package com.meritoki.retina.application.desktop.view.dialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.meritoki.retina.application.desktop.model.project.Data;
import com.meritoki.retina.application.desktop.model.project.Page;
import com.meritoki.retina.application.desktop.model.project.Project;
import com.meritoki.retina.application.desktop.model.project.Shape;
import com.meritoki.retina.application.desktop.model.project.Text;
import com.meritoki.retina.application.desktop.model.project.Unit;
import com.meritoki.retina.application.desktop.view.frame.Main;

/**
 *
 * @author osvaldo.rodriguez
 */
public class Rectangle extends javax.swing.JDialog implements MouseListener, KeyListener {

    static Logger logger = LogManager.getLogger(Rectangle.class.getName());
    private Project project;
    private List<String> emptyList = new ArrayList<>();
    private List<String> timeList = Arrays.asList("year", "month", "week", "day", "hour", "minute", "second");
    private List<String> spaceList = Arrays.asList("latitude", "longitude", "locale", "location");
    private List<String> energyList = Arrays.asList("letter", "word", "sentance", "temperature", "pressure");

    /**
     * Creates new form Rectangle
     *
     * @param parent
     * @param modal
     */
    public Rectangle(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        this.setTitle("Shape");
        this.initComponents();
        this.rectangleList.addMouseListener(this);
        this.rectangleList.addKeyListener(this);
    }

    public void setModel(Project model) {
        logger.debug("setModel(" + model + ")");
        this.project = model;
        this.init();
    }

    public void init() {
        if (this.project != null) {
            this.initLabel();
            this.initList();
            this.initComboBox();
        }
    }

    public void initLabel() {
        logger.debug("initLabel()");
        Page image = this.project.getPage();
        Shape rectangle = (image != null) ? image.getShape() : null;
        List<Shape> shapeList = (image != null) ? image.getShapeList() : null;
        Data data = (rectangle != null) ? rectangle.data : null;
        List<Text> textList = (data != null) ? data.getTextList() : null;
    }

    public void initList() {
        logger.debug("initList()");
        Page image = this.project.getPage();
        int index = (image != null) ? image.getIndex() : 0;
        Shape rectangle = (image != null) ? image.getShape() : null;
        List<Shape> shapeList = (image != null) ? image.getShapeList() : null;
        Data data = (rectangle != null) ? rectangle.data : null;
        List<Text> textList = (data != null) ? data.getTextList() : null;
        this.initRectangleList(shapeList);
        this.setRectangleListSelectedIndex(index);
    }

    public void initComboBox() {
        logger.debug("initComboBox()");
        Page image = this.project.getPage();
        Shape rectangle = (image != null) ? image.getShape() : null;
        Data data = (rectangle != null) ? rectangle.data : null;
        List<Text> textList = (data != null) ? data.getTextList() : null;
        this.initTextValueComboBox(textList);
        List<String> unitTypeList = new ArrayList<>();
        unitTypeList.add("data");
        unitTypeList.add("time");
        unitTypeList.add("space");
        unitTypeList.add("energy");
        this.initUnitTypeComboBox(unitTypeList);
        if (data != null) {
            this.unitTypeComboBox.setSelectedItem(data.unit.type);
            switch (data.unit.type) {
                case "data": {
                    this.initUnitValueComboBox(this.emptyList);
                    break;
                }
                case "time": {
                    this.initUnitValueComboBox(this.timeList);
                    break;
                }
                case "space": {
                    this.initUnitValueComboBox(this.spaceList);
                    break;
                }
                case "energy": {
                    this.initUnitValueComboBox(this.energyList);
                    break;
                }
            }
        } else {
            this.initUnitValueComboBox(this.emptyList);
        }
    }

    public void initTextValueComboBox(List<Text> textList) {
        String[] array = new String[0];
        if (textList != null) {
            array = new String[textList.size()];
            for (int i = 0; i < array.length; i++) {
                array[i] = textList.get(i).value;
            }
        }
        this.textValueComboBox.setModel(new DefaultComboBoxModel(array));
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
                        initUnitValueComboBox(emptyList);
                        break;
                    }
                    case 1: {
                        initUnitValueComboBox(timeList);
                        break;
                    }
                    case 2: {
                        initUnitValueComboBox(spaceList);
                        break;
                    }
                    case 3: {
                        initUnitValueComboBox(energyList);
                        break;
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

    public void initRectangleList(List<Shape> shapeList) {
        DefaultListModel defaultListModel = new DefaultListModel();
        if (shapeList != null && shapeList.size() > 0) {
            logger.info("initRectangleList(" + shapeList + ")");
            for (int i = 0; i < shapeList.size(); i++) {
                defaultListModel.addElement(shapeList.get(i).uuid);
            }

        }
        this.rectangleList.setModel(defaultListModel);
    }

    public void setRectangleListSelectedIndex(int index) {
        this.rectangleList.setSelectedIndex(index);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 1) {
            Page image = this.project.getPage();
            if (image != null) {
                String selectedItem = (String) this.rectangleList.getSelectedValue();
                image.setShape(selectedItem);
                this.getParent().repaint();
                this.init();
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        Page image = this.project.getPage();
        if (image != null) {
            int index = image.getIndex();
            switch (keyCode) {
                case KeyEvent.VK_LEFT: {
                    logger.debug("RectangleDialog.keyPressed LEFT");
                    index = image.getIndex();
                    index = index - 1;
                    image.setIndex(index);
                    this.init();
                    this.setRectangleListSelectedIndex(index);
                    this.getParent().repaint();
                    break;
                }
                case KeyEvent.VK_RIGHT: {
                    logger.debug("RectangleDialog.keyPressed RIGHT");
                    index = image.getIndex();
                    index = index + 1;
                    image.setIndex(index);
                    this.init();
                    this.setRectangleListSelectedIndex(index);
                    this.getParent().repaint();
                    break;
                }

            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        String uuid = (String) rectangleList.getSelectedValue();
        Page image = this.project.getPage();
        if (image != null && !uuid.equals(image.getShape().uuid)) {
            this.project.getPage().setShape(uuid);
            this.init();
            this.getParent().repaint();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
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
        rectangleList = new javax.swing.JList();
        deleteRectangleButton = new javax.swing.JButton();
        applyUnitButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

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

        rectangleList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        rectangleScrollPane.setViewportView(rectangleList);

        deleteRectangleButton.setText("X");
        deleteRectangleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteRectangleButtonActionPerformed(evt);
            }
        });

        applyUnitButton.setText("Apply");
        applyUnitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                applyUnitButtonActionPerformed(evt);
            }
        });

        jLabel1.setText("List");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(unitRectangleSeparator, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(textUnitSeparator)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(typeLabel))
                            .addComponent(unitValueLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(unitValueComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(applyUnitButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(unitTypeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(rectangleScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 291, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(deleteRectangleButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(149, 149, 149)
                                .addComponent(textLabel))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(151, 151, 151)
                                .addComponent(unitLabel))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(126, 126, 126)
                                .addComponent(inputAddButton, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(inputLabel)
                                    .addComponent(valueLabel))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(textInputTextField)
                                    .addComponent(textValueComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(noInputCheckBox)
                                    .addComponent(textValueDefaultCheckBox)))
                            .addComponent(jLabel1))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
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
                .addComponent(textUnitSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(unitLabel)
                .addGap(14, 14, 14)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(unitTypeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(typeLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(unitValueComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(unitValueLabel)
                    .addComponent(applyUnitButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(unitRectangleSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 7, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(deleteRectangleButton, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(rectangleScrollPane, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void noInputCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_noInputCheckBoxActionPerformed
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
    }//GEN-LAST:event_noInputCheckBoxActionPerformed

    private void inputAddButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inputAddButtonActionPerformed
        String value = this.textInputTextField.getText().trim();
        Text text = new Text();
        text.value = value;
        this.project.getPage().getShape().data.addText(text);
        this.setModel(this.project);
    }//GEN-LAST:event_inputAddButtonActionPerformed

    private void textValueDefaultCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textValueDefaultCheckBoxActionPerformed
        boolean flag = this.textValueDefaultCheckBox.isSelected();
        if (flag) {
            this.textValueComboBox.setSelectedItem(this.project.getPage().getShape().data.getDefaultText().value);
        }
    }//GEN-LAST:event_textValueDefaultCheckBoxActionPerformed

    private void unitTypeComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_unitTypeComboBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_unitTypeComboBoxActionPerformed

    private void deleteRectangleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteRectangleButtonActionPerformed
        int index = this.rectangleList.getSelectedIndex();
        Page image = this.project.getPage();
        image.getShapeList().remove(index);
        this.init();
        ((Main) this.getParent()).repaint();
    }//GEN-LAST:event_deleteRectangleButtonActionPerformed

    private void applyUnitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_applyUnitButtonActionPerformed
        Page image = (this.project != null) ? this.project.getPage() : null;
        Shape rectangle = (image != null) ? image.getShape() : null;
        Data data = (rectangle != null) ? rectangle.getData() : null;
        if (data != null) {
            String unitType = (String) this.unitTypeComboBox.getSelectedItem();
            switch (unitType) {
                case "Data": {
                    data.unit.type = Unit.DATA;
                    break;
                }
                case "Time": {
                    data.unit.type = Unit.TIME;
                    break;
                }
                case "Space": {
                    data.unit.type = Unit.SPACE;
                    break;
                }
                case "Energy": {
                    data.unit.type = Unit.ENERGY;
                    break;
                }
            }
            data.unit.value = (String) this.unitValueComboBox.getSelectedItem();
        }
    }//GEN-LAST:event_applyUnitButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Rectangle.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Rectangle.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Rectangle.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Rectangle.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Rectangle dialog = new Rectangle(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton deleteRectangleButton;
    private javax.swing.JButton inputAddButton;
    private javax.swing.JLabel inputLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JCheckBox noInputCheckBox;
    private javax.swing.JList rectangleList;
    private javax.swing.JScrollPane rectangleScrollPane;
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
