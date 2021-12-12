package com.meritoki.app.desktop.retina.model.vendor.google;

import com.meritoki.app.desktop.retina.controller.node.NodeController;

public class GoogleController {

	
	public static String getGoogleHome() {
		return NodeController.getSystemHome() + NodeController.getSeperator() + "vendor"
				+ NodeController.getSeperator() + "google";
	}
}
