/*
 * Copyright 2021 Joaquin Osvaldo Rodriguez
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.meritoki.app.desktop.retina;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.meritoki.app.desktop.retina.model.Model;
import com.meritoki.app.desktop.retina.view.frame.MainFrame;
import com.meritoki.app.desktop.retina.view.window.SplashWindow;
import com.meritoki.library.controller.node.NodeController;

public class Desktop {

	static Logger logger = LogManager.getLogger(Desktop.class.getName());
	public static String versionNumber = "1.0.202209";
	public static String vendor = "Meritoki";
	public static String about = "Version " + versionNumber + " Copyright " + vendor + " 2019-2022";
	public static Option helpOption = new Option("h", "help", false, "Print usage information");
	public static Option versionOption = new Option("v", "version", false, "Print version information");

	public static void main(String args[]) {
		System.out.println("Hello World");
		Options options = new Options();
		options.addOption(helpOption);
		options.addOption(versionOption);
		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine commandLine = parser.parse(options, args);
			if (commandLine.hasOption("help")) {
				logger.info("main(args) help");
				HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp("retina", options, true);
			} else if (commandLine.hasOption("version")) {
				System.out.println(about);
			} else {
				logger.info("Starting Retina Desktop Application...");
				final Model model = new Model();
				model.system.init();
				model.setProviderModel();
				final MainFrame mainFrame = new MainFrame(model);
				final SplashWindow splashWindow = new SplashWindow("/Splash.png", mainFrame, 4000);
				try {
					if (NodeController.isMac()) {
						javax.swing.UIManager.installLookAndFeel("Aqua", "com.apple.laf.AquaLookAndFeel");
					} else {
						for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager
								.getInstalledLookAndFeels()) {
							if ("Nimbus".equals(info.getName())) {
								javax.swing.UIManager.setLookAndFeel(info.getClassName());
								break;
							}
						}
					}
				} catch (ClassNotFoundException ex) {
					logger.error(ex);
				} catch (InstantiationException ex) {
					logger.error(ex);
				} catch (IllegalAccessException ex) {
					logger.error(ex);
				} catch (javax.swing.UnsupportedLookAndFeelException ex) {
					logger.error(ex);
				}
				java.awt.EventQueue.invokeLater(new Runnable() {
					public void run() {
						mainFrame.setVisible(true);
					}
				});
			}
		} catch (org.apache.commons.cli.ParseException ex) {
			logger.error(ex);
		}

	}
}
// public static Option machineOption = new Option("m", "machine", false, "Load
// Machine Interface");
// options.addOption(machineOption);
//if (commandLine.hasOption("machine")) {
//model.system.machine = true;
//batchPath = commandLine.getOptionValue("batch");
//logger.info("main(args) batch=" + batchPath);
//}
//model.system.machine = true;
