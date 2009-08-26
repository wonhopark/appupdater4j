package org.gdteam.appupdater4j.demo;

import java.io.IOException;
import java.util.Properties;

import javax.swing.JOptionPane;

public class Main {

    public static void main(String[] args) {
        Properties props = new Properties();
        try {
            props.load(Main.class.getClassLoader().getResourceAsStream("appupdater4j.cfg.properties"));
            
            String version = props.getProperty("application.version");
            String message = null;
            if (version.equals("1.0.0")) {
                message = "Application demo not updated";
            } else {
                message = "Application Demo updated. New version is 2.0.0";
            }
            
            JOptionPane.showMessageDialog(null, message);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        System.exit(0);
    }
}
