/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.retina.controller;

import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.configuration.server.ServerRuntime;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.retina.model.cayenne.persistent.Model;
import org.retina.view.window.Splash;

/**
 *
 * @author osvaldo.rodriguez
 */
public class Main {

	static Logger logger = LogManager.getLogger(org.retina.view.frame.Main.class.getName());

	/**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        logger.info("Retina starting...");
        
        ServerRuntime cayenneRuntime = ServerRuntime.builder()
                .addConfig("cayenne-application-database.xml")
                .build();
        ObjectContext context = cayenneRuntime.newContext();
        Model artist = context.newObject(Model.class);
        artist.setUuid("test");
        context.commitChanges();
        final org.retina.view.frame.Main main = new org.retina.view.frame.Main();
        Splash splash = new Splash("/splash.png", main, 4000);
        
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
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                main.setVisible(true);
            }
        });
    }

}
