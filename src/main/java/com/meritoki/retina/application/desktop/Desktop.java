/*
 * Copyright 2019 Joaquin Osvaldo Rodriguez
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
package com.meritoki.retina.application.desktop;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.meritoki.retina.application.desktop.view.window.Splash;
import com.meritoki.retina.application.desktop.model.system.Configuration;
import com.meritoki.retina.application.desktop.view.frame.Main;
import java.util.Properties;

public class Desktop {

    static Logger logger = LogManager.getLogger(Desktop.class.getName());

    public static void main(String args[]) {
        logger.info("Starting Retina Desktop Application...");
        Configuration configuration = new Configuration();
        Properties properties = configuration.open("./desktop.properties");
        final Main main = new Main(properties);
//        final Splash splash = new Splash("/splash.png", main, 4000);
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
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
                main.setVisible(true);
           }
        });
    }
}
