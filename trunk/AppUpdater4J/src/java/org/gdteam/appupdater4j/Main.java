package org.gdteam.appupdater4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.swing.ImageIcon;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.gdteam.appupdater4j.model.ApplicationVersion;
import org.gdteam.appupdater4j.model.Version;
import org.gdteam.appupdater4j.os.macosx.ReflectiveApplication;
import org.gdteam.appupdater4j.wrapper.ApplicationLauncher;

public class Main implements UpdateControllerListener {
    
    //Possible system properties
    public static final String SYSTEM_JAR_FILE_KEY = "org.gdteam.appupdater4j.jarfile";
    
    //Properties which can be found in property file or in jar file
    public static final String PROPERTY_JAR_FILE = "jar.file";
    public static final String PROPERTY_DOCK_ICON_PATH = "dockicon.path";
    public static final String PROPERTY_DIALOG_CLASS = "dialog.class";
    public static final String PROPERTY_DIALOG_TITLE = "dialog.title";
    public static final String PROPERTY_UPDATE_MODE = "update.mode";    
    
    private static Logger logger = Logger.getLogger(Main.class);
    
    private Properties properties = null;
    private UpdateManager updateManager = null;
    private ApplicationLauncher applicationLauncher;
    
    public Main() {
        //Configure log4j
        PropertyConfigurator.configure(this.getClass().getClassLoader().getResource("log4j.properties"));
    }
    
    public void loadProperties(String[] args) throws Exception {
        
        this.properties = new Properties();
        
        //First get built-in properties from classpath
        this.properties = new Properties();
        try {
            this.properties.load(this.getClass().getClassLoader().getResourceAsStream("config.properties"));
        } catch (IOException e) {
            logger.error("Unable to load properties from config.properties", e);
        }
        
        Properties userSpecifiedProperties = null;
        
        //Check for propertyfile named appupdater4j.properties
        File appUpdater4jPropertyFile = new File("appupdater4j.properties");
        if (appUpdater4jPropertyFile.exists()) {
            userSpecifiedProperties = new Properties();
            userSpecifiedProperties.load(new FileInputStream(appUpdater4jPropertyFile));
        } else {
            //No -Dorg.gdteam.appupdater4j.propertyfile
            //Check for properties in -Dorg.gdteam.appupdater4j.jarfile
            if (System.getProperty(SYSTEM_JAR_FILE_KEY) != null) {
                File zip = new File(System.getProperty(SYSTEM_JAR_FILE_KEY));
                
                if (!zip.exists() || !zip.isFile()) {
                    throw new Exception("Cannot find jar file : " + zip.getPath());
                }

                userSpecifiedProperties = FileUtil.getPropertiesFromFileInZip(zip, "appupdater4j.cfg.properties");
                userSpecifiedProperties.put(SYSTEM_JAR_FILE_KEY, zip.getPath());
                
            } else {
                StringBuilder message = new StringBuilder("Usage : java -D" + SYSTEM_JAR_FILE_KEY + "=<your_application_jar> -jar appupdater4j.jar\n");
                message.append("      - <your_application_jar> : path to your wrapped application jar which contains appupdater4j.cfg.properties");
                throw new Exception(message.toString());
            }
        }
        
        Iterator fileKeySet = userSpecifiedProperties.keySet().iterator();
        while (fileKeySet.hasNext()) {
            Object key = (Object) fileKeySet.next();
            this.properties.put(key, userSpecifiedProperties.get(key));
        }
        
        //Configure dock icon
        String dockIconPath = this.properties.getProperty(PROPERTY_DOCK_ICON_PATH);
        if (dockIconPath == null) {
            ReflectiveApplication.getApplication().setDockIconImage(new ImageIcon(this.getClass().getClassLoader().getResource("refresh-128.png")).getImage());
        } else  {
            ReflectiveApplication.getApplication().setDockIconImage(new ImageIcon(dockIconPath).getImage());
        }
    }
    
    public void configureUpdateManager() {
        this.updateManager = new UpdateManager();
        this.updateManager.configure(properties);
    }
    
    public void configureApplicationLauncher(){
        String jarFile = this.properties.getProperty(SYSTEM_JAR_FILE_KEY);
        
        if (jarFile == null) {
            jarFile = this.properties.getProperty(PROPERTY_JAR_FILE);
        }
        
        this.applicationLauncher = new ApplicationLauncher(new File(jarFile), new String[0]);
    }
    
    /**
     * Install updates which are stored in specific folder
     * @return installed version
     */
    public Version installAutoUpdate() {
        
        logger.info("Check for auto update");
        
        Version installedVersion = null;
        if (this.updateManager.installAutoUpdate()) {
            installedVersion = Version.createVersion(this.updateManager.getCurrentVersion());
            
            this.properties.put(UpdateManager.PROPERTY_APPLICATION_VERSION_KEY, installedVersion.toString());
        }
        
        logger.info("Auto update performed : " + String.valueOf(installedVersion != null));
        
        return installedVersion;
    }
    
    /**
     * Check for update and install update if necessary. Wait for the end of this method to start application
     */
    public void checkRemoteUpdateAndRun() {
        String updateMode = this.properties.getProperty(PROPERTY_UPDATE_MODE);
        
        logger.info("Update mode is : " + updateMode);
        
        if (updateMode.equals("default")) {
            this.updateManager.performCheckForUpdate();
            
            if (this.updateManager.needUpdate()) {
                UpdateController controller = UpdateControllerFactory.getUpdateController((String) this.properties.get(PROPERTY_DIALOG_CLASS));
                controller.addUpdateControllerListener(this);
                
                this.updateManager.addUpdateListener(controller);
                
                controller.setVersionToInstall(this.updateManager.getVersionToInstallList());
                controller.displayController(this.properties.getProperty(PROPERTY_DIALOG_TITLE));
            } else {
                this.runApplication();
            }
        } else if (updateMode.equals("download_only")) {
            this.updateManager.checkAndDownloadASync();
            this.runApplication();
        }
    }
    
    public void runApplication() {
        logger.info("Run wrapped application");
        try {
            this.applicationLauncher.extractManifestInfo();
            
        } catch (Exception e) {
            logger.error("Unable to extract data form jar manifest", e);
            System.exit(0);
        }
        try {
            this.applicationLauncher.run();
        } catch (Exception e) {
            logger.error("Unable to start wrapped application", e);
            System.exit(0);
        }
        
    }
    
    /**
     * @param args
     */
    public static void main(String[] args) {
        
        Main application = new Main();
        try {
            application.loadProperties(args);
        } catch (Exception e) {
            logger.error("Cannot load AppUpdater4j properties", e);
            System.exit(0);
        }
        
        application.configureUpdateManager();
        application.configureApplicationLauncher();
        
        Version installedVersion = application.installAutoUpdate();
        
        if (installedVersion == null) {
            //No autoupdate performed...
            application.checkRemoteUpdateAndRun();
        } else {
            application.runApplication();
        }
    }

    public void canStartWrappedApplication(UpdateController source) {
        this.runApplication();
    }

    public void startUpdate(UpdateController source, List<ApplicationVersion> versionList) {
        this.updateManager.startUpdate(versionList);
    }

}
