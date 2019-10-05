/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meritoki.retina.application.desktop;

import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.configuration.server.ServerRuntime;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.meritoki.retina.application.desktop.view.window.Splash;

import com.meritoki.retina.application.desktop.model.properties.Configuration;
import com.meritoki.retina.application.desktop.view.frame.Main;
import java.util.Properties;

/**
 *
 * @author osvaldo.rodriguez
 */
public class Desktop {

    static Logger logger = LogManager.getLogger(com.meritoki.retina.application.desktop.view.frame.Main.class.getName());

    public static void main(String args[]) {

        logger.info("Starting Retina Desktop Application...");
        Configuration configuration = new Configuration();
        Properties properties = configuration.open("./desktop.properties");
        final Main main = new Main(properties);
        final Splash splash = new Splash("/splash.png", main, 4000);
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
