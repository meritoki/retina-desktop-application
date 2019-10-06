package com.meritoki.retina.application.desktop.model.project;

import java.awt.image.BufferedImage;
import java.util.UUID;

public class File {
	public String uuid;
    public String name = null;
    public String path = null;
    public String cachePath = null;
    public BufferedImage bufferedImage = null;
    public File(){ 
        this.uuid = UUID.randomUUID().toString();
    }
}
