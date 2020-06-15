package com.meritoki.retina.application.desktop.model.document;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.meritoki.app.desktop.retina.controller.node.NodeController;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ZooniverseExecuteCommandTest {

	static Logger logger = LogManager.getLogger(ZooniverseExecuteCommandTest.class.getName());

	@Test
	@Order(1)
	public void failure() {
		try {
			List<String> stringList = NodeController.executeCommand("panoptes project ls | grep retina", 10);
			assertEquals(stringList.size(), 0);
		} catch (Exception e) {
			logger.error("Exception " + e.getMessage());
		}
	}

	@Test
	@Order(2)
	public void success() {
		try {
			List<String> stringList = NodeController.executeCommand("panoptes project ls | grep retina", 360);
			for (String s : stringList) {
				logger.info(s);
			}
		} catch (Exception e) {
			logger.error("Exception " + e.getMessage());
		}
	}
}
