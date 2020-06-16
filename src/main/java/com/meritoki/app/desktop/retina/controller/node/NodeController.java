package com.meritoki.app.desktop.retina.controller.node;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.meritoki.app.desktop.retina.model.document.user.User;

public class NodeController {
	private static Logger logger = LogManager.getLogger(NodeController.class.getName());

	public static void main(String[] args) {
		System.out.println(getUserHome());
	}
	
	public static String getSeperator() {
		return FileSystems.getDefault().getSeparator();
	}
	
	public static String getUserHome() {
		return System.getProperty("user.home");
	}
	
	public static String getRetinaHome() {
		return getUserHome()+getSeperator()+".retina";
	}

	public static String getImageCache() {
		return getRetinaHome()+getSeperator()+"image";
	}
	
	public static String getPanoptesHome() {
		return getUserHome()+getSeperator()+".panoptes";
	}
	
	public static String getProviderHome() {
		return getRetinaHome()+getSeperator()+"provider";
	}
	
	public static BufferedImage openBufferedImage(String filePath, String fileName) {
		logger.debug("openBufferedImage(" + filePath + ", " + fileName + ")");
		return openBufferedImage(new java.io.File(filePath + getSeperator() + fileName));
	}

	public static BufferedImage openBufferedImage(java.io.File file) {
		BufferedImage bufferedImage = null;
		try {
			bufferedImage = ImageIO.read(file);
		} catch (IOException ex) {
			logger.error("IOException "+ex.getMessage());
		}
		return bufferedImage;
	}

	public static void saveJpg(String filePath, String fileName, BufferedImage bufferedImage) {
//		logger.info("saveJpg"+filePath + ", " + fileName + ", " + bufferedImage);
		saveJpg(new File(filePath + getSeperator() + fileName), bufferedImage);
	}

	@JsonIgnore
	public static void saveJpg(File file, BufferedImage bufferedImage) {
		logger.debug("saveJpg("+file+", "+bufferedImage+")");
		try {
			ImageIO.write(bufferedImage, "jpg", file);
			
		} catch (IOException ex) {
			logger.error(ex);
		}
	}

	@JsonIgnore
	public static Object openJson(java.io.File file, Class className) {
		logger.info("openJson(" + file + ", " + className + ")");
		Object object = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			object = mapper.readValue(file, className);
		} catch (JsonGenerationException e) {
			logger.error(e);
		} catch (JsonMappingException e) {
			logger.error(e);
		} catch (IOException e) {
			logger.error(e);
		}
		return object;
	}
	
//	public static Object openJson(File file, TypeReference<List<Input>> typeReference) {
//		logger.info("openJson(" + file + ", " + typeReference + ")");
//		Object object = null;
//		ObjectMapper mapper = new ObjectMapper();
////		mapper.disable(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);
//		try {
//			object = mapper.readValue(file, typeReference);
//			
//		} catch (JsonGenerationException e) {
//			logger.error(e);
//		} catch (JsonMappingException e) {
//			logger.error(e);
//		} catch (IOException e) {
//			logger.error(e);
//		}
//		return object;
//	}

	public static <T> Object openJson(File file, TypeReference<List<T>> typeReference) {
		logger.info("openJson(" + file + ", " + typeReference + ")");
		Object object = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			object = mapper.readValue(file, typeReference);
			
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
	public static List<String[]> openCsv(String fileName) {
		String line = "";
		List<String[]> stringArrayList = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
			while ((line = br.readLine()) != null) {
				logger.info(line);
				String[] array = line.split(",");
				stringArrayList.add(array);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return stringArrayList;
	}

	@JsonIgnore
	public static void saveJson(String path, String name, Object object) {
		logger.info("saveJson("+path+","+name+", object)");
		saveJson(new java.io.File(path+getSeperator()+name), object);
	}

	@JsonIgnore
	public static void saveJson(File file, Object object) {
		logger.info("saveJson("+file.getAbsolutePath()+",object)");
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		try {
			mapper.writerWithDefaultPrettyPrinter().writeValue(file, object);
		} catch (IOException ex) {
			logger.error(ex);
		}
	}

	@JsonIgnore
	public static void saveProperties(String path, String name, Properties properties) {
		logger.info("saveProperties("+path+","+name+", properties");
	       try (OutputStream output = new FileOutputStream(path+name)) {
	            properties.store(output, null);
	        } catch (IOException io) {
	            io.printStackTrace();
	        }

	}

	@JsonIgnore
	public static void saveYaml(String filePath, String fileName, Object object) {
		logger.info("saveYaml("+filePath+", "+fileName+", object)");
		DumperOptions options = new DumperOptions();
		options.setPrettyFlow(true);
		options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
		Yaml yaml = new Yaml(options);
		FileWriter writer;
		try {
			writer = new FileWriter(filePath + getSeperator() + fileName);
			yaml.dump(object, writer);
		} catch (IOException ex) {
			logger.error(ex);
		}
	}

	@JsonIgnore
	public static void saveCsv(String filePath, String fileName, Object object) {
		logger.info("saveCsv("+filePath+", "+fileName+", object)");
		try (PrintWriter writer = new PrintWriter(new File(filePath + getSeperator() + fileName))) {
			if (object instanceof StringBuilder)
				writer.write(((StringBuilder) object).toString());
			
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		}
	}

	@JsonIgnore
	public static List<String> executeCommand(String command) throws Exception {
		return executeCommand(command, 120);
	}

	@JsonIgnore
	public static List<String> executeCommand(String command, int timeout) throws Exception {
		logger.info("executeCommand(" + command + ", " + timeout + ")");
		ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", command).redirectError(new File("error"))
				.redirectOutput(new File("output"));

		Process process = null;
		String output = null;
		String error = null;
		List<String> stringList = new ArrayList<>();
		String string;
		try {
			process = processBuilder.start();
			if (!process.waitFor(timeout, TimeUnit.SECONDS)) {
				process.destroy();
				logger.info("executeCommand(...) exitValue=" + process.exitValue());
			}
			output = (FileUtils.readFileToString(new File("output"), "UTF8"));
			error = (FileUtils.readFileToString(new File("error"), "UTF8"));
			if (error != null && !error.equals("")) {
				string = "error";
				stringList.add(string);
			} else if (output != null && !output.equals("")) {
				string = output;
				String[] stringArray = string.split("\n");
				for (String s : stringArray) {
					stringList.add(s);
				}
			}
		}
		catch (Exception e) {
			logger.error("executeCommand(...) Exception "+e.getMessage());
			throw new Exception("process timed out");
		} 
		finally {
			logger.info("executeCommand(...) process.exitValue=" + process.exitValue());
		}
		return stringList;
	}
	
	@JsonIgnore
	public static Object toJson(String string, Class className) {
		logger.info("toJson(" + string + ", " + className + ")");
		Object object = null;
		ObjectMapper mapper = new ObjectMapper();
//		mapper.disable(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);
		try {
			object = mapper.readValue(string, className);
			
		} catch (JsonGenerationException e) {
			logger.error(e);
		} catch (JsonMappingException e) {
			logger.error(e);
		} catch (IOException e) {
			logger.error(e);
		}
		return object;
	}
	
	public static void deleteDirectory(File directory) {
		directory.delete();
	}
}
