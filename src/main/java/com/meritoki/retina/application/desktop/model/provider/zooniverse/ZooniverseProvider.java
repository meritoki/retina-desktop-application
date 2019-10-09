package com.meritoki.retina.application.desktop.model.provider.zooniverse;

import com.meritoki.retina.application.desktop.model.provider.Provider;

public class ZooniverseProvider extends Provider {
	public Zooniverse zooniverse = new Zooniverse();
	public ZooniverseProvider() {
		super("zooniverse");
	}
}
