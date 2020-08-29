package com.meritoki.app.desktop.retina.controller.node;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.meritoki.library.controller.node.NodeController;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class NodeControllerExecuteCommandTest {

	static Logger logger = LogManager.getLogger(NodeControllerExecuteCommandTest.class.getName());

	@Test
	@Order(1)
	public void output() {
		try {
			List<String> stringList = NodeController.executeCommand("ifconfig");
			for (String s : stringList) {
				logger.info(s);
			}
		} catch (Exception e) {
			logger.error("Exception " + e.getMessage());
		}
	}

	@Test
	@Order(2)
	public void error() {
		try {
			List<String> stringList = NodeController.executeCommand("swesr");
			assertEquals(stringList.size(), 1);
			assertEquals(stringList.get(0), "error");
		} catch (Exception e) {
			logger.error("Exception " + e.getMessage());
		}
	}
}
