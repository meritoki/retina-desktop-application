package com.meritoki.app.desktop.retina.model.provider.zooniverse;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.meritoki.library.controller.node.Exit;
import com.meritoki.library.controller.node.NodeController;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ZooniverseExecuteCommandTest {

	static Logger logger = LogManager.getLogger(ZooniverseExecuteCommandTest.class.getName());

	@Test
	@Order(1)
	public void failure() {
		try {
			Exit exit = NodeController.executeCommand("panoptes project ls | grep retina", 10);
			if(exit.value != 0) {
				throw new Exception("Non-zero Exit Value: "+exit.value);
			} else {
				List<String> stringList = exit.list;
				assertEquals(stringList.size(), 0);
			}
		} catch (Exception e) {
			logger.error("Exception " + e.getMessage());
		}
	}

	@Test
	@Order(2)
	public void success() {
		try {
			Exit exit = NodeController.executeCommand("panoptes project ls | grep retina", 360);
			if(exit.value != 0) {
				throw new Exception("Non-zero Exit Value: "+exit.value);
			} else {
				List<String> stringList = exit.list;
				assertEquals(stringList.size(), 0);
			}
		} catch (Exception e) {
			logger.error("Exception " + e.getMessage());
		}
	}
}
