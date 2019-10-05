package com.meritoki.retina.application.desktop.model.properties;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Configuration {
	public void save() {
		
		
	}
	
	public Properties open(String fileName) {
        Properties properties = new Properties();
		try (InputStream input = new FileInputStream(fileName)) {
            properties.load(input);
            System.out.println(properties.getProperty("service.web.gateway.url"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
		return properties;
	}
}
