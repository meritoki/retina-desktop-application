/*
 * Copyright 2020 Joaquin Osvaldo Rodriguez
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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.meritoki.app.desktop.retina.model.Model;
import com.meritoki.app.desktop.retina.view.frame.MainFrame;
import com.meritoki.app.desktop.retina.view.window.SplashWindow;
import com.meritoki.library.controller.node.NodeController;

public class Desktop {

    static Logger logger = LogManager.getLogger(Desktop.class.getName());

    public static void main(String args[]) {
        logger.info("Starting Retina Desktop Application..."); 
        final Model model = new Model();
        final MainFrame mainFrame = new MainFrame(model);
        final SplashWindow splashWindow = new SplashWindow("/Splash.png", mainFrame, 4000);
        try {
        	if(NodeController.isMac()) {
        		javax.swing.UIManager.installLookAndFeel("Aqua","com.apple.laf.AquaLookAndFeel");
        	} else {
	            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
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
}
