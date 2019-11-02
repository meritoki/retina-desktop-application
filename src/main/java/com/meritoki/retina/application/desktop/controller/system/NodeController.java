package com.meritoki.retina.application.desktop.controller.system;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.type.TypeReference;

import com.meritoki.retina.application.desktop.model.User;
import com.meritoki.retina.application.desktop.model.provider.zooniverse.Zooniverse;

public class NodeController {
	private static Logger logger = LogManager.getLogger(NodeController.class.getName());

	public static BufferedImage openBufferedImage(String filePath, String fileName) {
		logger.info("openBufferedImage("+filePath + ", " + fileName+")");
		return openBufferedImage(new java.io.File(filePath + "/" + fileName));
	}
	
	public static BufferedImage openBufferedImage(java.io.File file) {
		BufferedImage bufferedImage = null;
        try {
            bufferedImage = ImageIO.read(file);
            logger.info("opened...");
        } catch (IOException ex) {
            logger.error(ex);
        }
        return bufferedImage;
	}
	
	@JsonIgnore
	public static Object openJson(java.io.File file, Class className) {
		logger.info("openJson(" +file+", "+className+ ")");
		Object object = null;
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);
		try {
			object = mapper.readValue(file, className);
			logger.info(object);
			logger.info("opened...");
		} catch (JsonGenerationException e) {
			logger.error(e);
		} catch (JsonMappingException e) {
			logger.error(e);
		} catch (IOException e) {
			logger.error(e);
		}
		return object;
	}
	
	@JsonIgnore
	public static void saveJson(String path, String name, Object object) {
		logger.info("saveJson()");
		ObjectMapper mapper = new ObjectMapper();
		mapper.setVisibility(JsonMethod.FIELD, JsonAutoDetect.Visibility.ANY);
		mapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
		try {
			mapper.writeValue(new java.io.File(path + "/" + name), object);
			logger.info("saved...");
		} catch (IOException ex) {
			logger.error(ex);
		}
	}
	
	@JsonIgnore
	public static void saveJson(File file, Object object) {
		logger.info("saveJson()");
		ObjectMapper mapper = new ObjectMapper();
		mapper.setVisibility(JsonMethod.FIELD, JsonAutoDetect.Visibility.ANY);
		mapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
		try {
			mapper.writeValue(file, object);
			logger.info("saved...");
		} catch (IOException ex) {
			logger.error(ex);
		}
	}
	
	public static Object openJson(File file, TypeReference<List<User>> typeReference) {
		logger.info("openJson(" +file+", "+typeReference+ ")");
		Object object = null;
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);
		try {
			object = mapper.readValue(file, typeReference);
			logger.info(object);
			logger.info("opened...");
		} catch (JsonGenerationException e) {
			logger.error(e);
		} catch (JsonMappingException e) {
			logger.error(e);
		} catch (IOException e) {
			logger.error(e);
		}
		return object;
	}
	
	@JsonIgnore
	public static void saveProperties(Properties properties) {

	}

	@JsonIgnore
	public static Properties openProperties(String fileName) {
		Properties properties = new Properties();
		try (InputStream input = new FileInputStream(fileName)) {
			properties.load(input);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return properties;
	}

	@JsonIgnore
	public static List<String> executeCommand(String command) {
	    return executeCommand(command, 120);
	}

	@JsonIgnore
	public static List<String> executeCommand(String command, int timeout) {
	    logger.info("executeCommand(" + command + ")");
	    ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c",command)
	            .redirectError(new File("error"))
	            .redirectOutput(new File("output"));
	
	    Process process;
	    int seconds = 120;
	    String output = null;
	    String error = null;
	    List<String> stringList = new ArrayList<>();
	    String string;
	    try {
	        process = processBuilder.start();
	        if (!process.waitFor(seconds, TimeUnit.SECONDS)) {
	            process.destroy();
	            logger.info("executeCommand(...) exitValue="+process.exitValue());
	        }
	        output = (FileUtils.readFileToString(new File("output"),"UTF8"));
	        error = (FileUtils.readFileToString(new File("error"),"UTF8"));
	        if(error != null && !error.equals("")){
	            string = "error";
	            stringList.add(string);
	        } else if(output != null && !output.equals("")){
	            string = output;
	            String[] stringArray = string.split("\n");
	            for(String s: stringArray){
	                stringList.add(s);
	            }
	        }
	    } catch (IOException ex) {
	        
	    } catch (InterruptedException ex) {
	        
	    }
	    return stringList;
	}


}
