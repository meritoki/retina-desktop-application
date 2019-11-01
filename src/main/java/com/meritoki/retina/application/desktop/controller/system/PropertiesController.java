package com.meritoki.retina.application.desktop.controller.system;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.codehaus.jackson.annotate.JsonIgnore;

public class PropertiesController {


	@JsonIgnore
	public static void save(Properties properties) {

	}

	@JsonIgnore
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
