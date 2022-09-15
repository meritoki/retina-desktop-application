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
package com.meritoki.app.desktop.retina.view.panel;

import java.awt.Color;
import java.awt.Component;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.meritoki.app.desktop.retina.model.Model;
import com.meritoki.app.desktop.retina.model.document.Grid;
import com.meritoki.app.desktop.retina.model.document.Shape;

public class TableRenderer extends DefaultTableCellRenderer {

	private static Logger logger = LogManager.getLogger(TableRenderer.class.getName());
	Color backgroundColor = Color.white;
	Model model =null;
	
	public TableRenderer(Model model) {
		this.model = model;
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		Shape shape = model.system.matrix.getShape(row, column);
		if(shape != null) {
			String text = shape.getData().getText().value;
			if(text != null) {
				c.setBackground(Color.green);
			} else {
				c.setBackground(Color.blue);
			}
			Shape s = model.document.getShape();
			if(s != null) {
				if(s instanceof Grid) {
//				logger.info("getTableCellRendererComponent(...) s="+s);
					Grid g = (Grid)s;
					List<Shape> shapeList = g.getShapeList();
					if(shapeList.contains(shape)) {
						if(g.getShape().uuid.equals(shape.uuid)) {
							c.setBackground(Color.YELLOW);
						} else {
							c.setBackground(Color.RED);
						}
					}
				} else {
					if(shape.uuid.equals(s.uuid)) {
						c.setBackground(Color.RED);
					}
				}
			}
		} else {
			c.setBackground(backgroundColor);
		}
		return c;
	}
}
