package com.meritoki.retina.application.desktop.controller.system;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

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

import com.meritoki.retina.application.desktop.model.document.User;
import com.meritoki.retina.application.desktop.model.provider.zooniverse.Zooniverse;

public class NodeController {
	private static Logger logger = LogManager.getLogger(NodeController.class.getName());

	@JsonIgnore
	public static Object open(java.io.File file, Class className) {
		logger.info("open(" +file+", "+className+ ")");
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
	public static void save(String path, String name, Object object) {
		logger.info("save()");
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
	public static void save(File file, Object object) {
		logger.info("save()");
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

	public static Object open(File file, TypeReference<List<User>> typeReference) {
		logger.info("open(" +file+", "+typeReference+ ")");
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
}
