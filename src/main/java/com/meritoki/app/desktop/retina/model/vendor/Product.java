package com.meritoki.app.desktop.retina.model.vendor;

import java.awt.image.BufferedImage;
import java.util.List;

import com.meritoki.app.desktop.retina.controller.node.NodeController;
import com.meritoki.app.desktop.retina.model.Model;
import com.meritoki.app.desktop.retina.model.document.Document;
import com.meritoki.app.desktop.retina.model.document.Page;
import com.meritoki.app.desktop.retina.model.document.Shape;
import com.meritoki.app.desktop.retina.view.frame.MainFrame;

public class Product {
	
	public Model model;
	public MainFrame mainFrame;
	
	public void setModel(Model model) {
		this.model = model;
	}
	
	public void setMainFrame(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
	}
	
	public void execute() throws Exception {

	}
}
