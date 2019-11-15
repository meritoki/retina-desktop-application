package com.meritoki.retina.application.desktop.controller.node;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
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
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import com.meritoki.retina.application.desktop.model.User;
import com.meritoki.retina.application.desktop.model.provider.zooniverse.Zooniverse;

public class NodeController {
	private static Logger logger = LogManager.getLogger(NodeController.class.getName());

	public static BufferedImage openBufferedImage(String filePath, String fileName) {
		logger.info("openBufferedImage(" + filePath + ", " + fileName + ")");
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

	public static void saveJpg(String filePath, String fileName, BufferedImage bufferedImage) {
		logger.info(filePath + ", " + fileName + ", " + bufferedImage);
		saveJpg(new File(filePath + "/" + fileName), bufferedImage);
	}

	@JsonIgnore
	public static void saveJpg(File file, BufferedImage bufferedImage) {
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
		mapper.disable(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);
		try {
			object = mapper.readValue(file, className);
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

	public static Object openJson(File file, TypeReference<List<User>> typeReference) {
		logger.info("openJson(" + file + ", " + typeReference + ")");
		Object object = null;
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);
		try {
			object = mapper.readValue(file, typeReference);
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
		saveJson(new java.io.File(path+"/"+name), object);
//		ObjectMapper mapper = new ObjectMapper();
//		mapper.setVisibility(JsonMethod.FIELD, JsonAutoDetect.Visibility.ANY);
//		mapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
//		try {
//			mapper.writeValue(new java.io.File(path + "/" + name), object);
//			logger.info("saved...");
//		} catch (IOException ex) {
//			logger.error(ex);
//		}
	}

	@JsonIgnore
	public static void saveJson(File file, Object object) {
		logger.info("saveJson("+file.getAbsolutePath()+",object)");
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
	public static void saveProperties(Properties properties) {

	}

	@JsonIgnore
	public static void saveYaml(String filePath, String fileName, Object object) {
		DumperOptions options = new DumperOptions();
		options.setPrettyFlow(true);
		options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
		Yaml yaml = new Yaml(options);
		FileWriter writer;
		try {
			writer = new FileWriter(filePath + "/" + fileName);
			yaml.dump(object, writer);
		} catch (IOException ex) {
			logger.error(ex);
		}
	}

	@JsonIgnore
	public static void saveCsv(String filePath, String fileName, Object object) {
		try (PrintWriter writer = new PrintWriter(new File(filePath + "/" + fileName))) {
			if (object instanceof StringBuilder)
				writer.write(((StringBuilder) object).toString());
			System.out.println("saved...");
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		}
	}

	@JsonIgnore
	public static List<String> executeCommand(String command) {
		return executeCommand(command, 120);
	}

	@JsonIgnore
	public static List<String> executeCommand(String command, int timeout) {
		logger.info("executeCommand(" + command + ", " + timeout + ")");
		ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", command).redirectError(new File("error"))
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
		} catch (IOException ex) {

		} catch (InterruptedException ex) {

		}
		return stringList;
	}

}
