package com.meritoki.retina.application.desktop.model.project;

import java.awt.image.BufferedImage;
import java.util.UUID;
import org.codehaus.jackson.annotate.JsonIgnore;

public class File {
	public String uuid;
    public String name = null;
    public String path = null;
    public String cachePath = null;
    @JsonIgnore
    public BufferedImage bufferedImage = null;
    public File(){ 
        this.uuid = UUID.randomUUID().toString();
    }
}
