package com.meritoki.retina.application.desktop.model;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Configuration {
	public static void save(Properties properties) {
		
		
	}
	
	public static Properties open(String fileName) {
        Properties properties = new Properties();
		try (InputStream input = new FileInputStream(fileName)) {
            properties.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
		return properties;
	}
}
