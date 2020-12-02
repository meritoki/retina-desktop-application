package com.meritoki.app.desktop.retina.view.panel;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import javax.swing.JPanel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.meritoki.app.desktop.retina.model.Model;
import com.meritoki.app.desktop.retina.model.provider.meritoki.Meritoki;
import com.meritoki.app.desktop.retina.view.frame.MainFrame;
import com.meritoki.library.cortex.model.Belief;

public class CortexPanel extends JPanel {

	private static Logger logger = LogManager.getLogger(RetinaPanel.class.getName());
	private MainFrame mainFrame = null;
	private Model model = null;
	private Meritoki meritoki;

	public void setMainFrame(MainFrame main) {
		this.mainFrame = main;
	}

	public void setModel(Model model) {
		this.model = model;
//		this.init();
		this.setPreferredSize(this.getPreferredSize());
		this.meritoki = (Meritoki)this.model.system.providerMap.get("meritoki");
	}
	public void init() {
		logger.debug("init()");
		this.repaint();
		this.revalidate();
	}

	@Override
	public void paint(Graphics graphics) {
		super.paint(graphics);
		if (this.model != null) {
			Graphics2D graphics2D = (Graphics2D) graphics.create();
			Belief belief = this.meritoki.document.cortex.getBelief();
			if(belief != null) {
				AffineTransform affineTransform = new AffineTransform();
				affineTransform.scale(1, 1);// this handles scaling the
				graphics2D.drawImage(belief.getBufferedImage(), affineTransform, null);
			}
		}
	}
}

