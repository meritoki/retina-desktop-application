package com.meritoki.retina.application.desktop.model.project;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class File {
	@JsonIgnore
    static Logger logger = LogManager.getLogger(Page.class.getName());
	@JsonProperty
	public String uuid;
	@JsonProperty
    public String name = null;
	@JsonProperty
    public String path = null;
	@JsonProperty
    public String cachePath = null;
    @JsonIgnore
    public BufferedImage bufferedImage = null;
    @JsonProperty
    public int width = 0;
    @JsonProperty
    public int height = 0;
    
    /**
     * Instantiate new instance of File
     */
    public File(){ 
        this.uuid = UUID.randomUUID().toString();
    }
    
    @JsonIgnore
    public void loadBufferedImage() {
        logger.info("loadBufferedImage()");
        if (this.path != null && this.name != null) {
            try {
            	logger.info("loadBufferedImage() "+this.path + "/" + this.name);
                this.bufferedImage = ImageIO.read(new java.io.File(this.path + "/" + this.name));
                this.width = this.bufferedImage.getWidth();
                this.height = this.bufferedImage.getHeight();
            } catch (IOException ex) {
                logger.error(ex);
            }
        }
    }
}
