package org.gdteam.appupdater4j;

import java.io.File;
import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.util.Properties;

public class MainTestUI {

    public static void main(String[] args) {
        
        //Create property file
        Properties testProperties = new Properties();
        testProperties.put("application.id", "myappid");
        testProperties.put("application.version", "0.0.5");
        try {
            testProperties.put("feed.url", TestUtil.extractInJarFile("basictestrss.xml").toURL().toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            System.exit(0);
        }
        
        Main main = new Main();
        
        try {
            File propertyFile = new File(System.getProperty("java.io.tmpdir"), "maintestui.properties");
            
            testProperties.store(new FileOutputStream(propertyFile), "Created for test purpose");
            
            String[] mainArgs = {propertyFile.getPath()};
            
            main.loadProperties(mainArgs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        main.configureUpdateManager();
        main.performModalCheck();
        
    }
}
