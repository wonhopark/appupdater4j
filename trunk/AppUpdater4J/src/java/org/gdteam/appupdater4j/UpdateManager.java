package org.gdteam.appupdater4j;

import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.gdteam.appupdater4j.model.ApplicationVersion;
import org.gdteam.appupdater4j.version.VersionHandler;

public class UpdateManager {
    
    public static final String PROPERTY_PREFIX = "org.gdteam.appupdater4j";

    private String applicationID;
    private String currentVersion;
    private VersionHandler versionHandler;
    
    private List<ApplicationVersion> versionToInstallList;
    private Properties properties;

    public UpdateManager(String applicationID, String currentVersion, URL feedURL) {
        this.applicationID = applicationID;
        this.currentVersion = currentVersion;
        
        this.versionHandler = new VersionHandler(feedURL);

        this.loadProperties();
    }
    
    private void loadProperties() {
        //First get properties from classpath
        try {
            this.properties.load(this.getClass().getClassLoader().getResourceAsStream("updatemanager.cfg.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        //Second get system.properties
        Iterator systemKeySet = System.getProperties().keySet().iterator();
        while (systemKeySet.hasNext()) {
            Object key = (Object) systemKeySet.next();
            if (key instanceof String && ((String) key).startsWith(PROPERTY_PREFIX)) {
                this.properties.put(key, System.getProperty((String) key));
            }
        }
    }
    
    public void performCheckForUpdate() {
        this.versionToInstallList =  this.versionHandler.getInstallVersionList(applicationID, currentVersion);
    }
    
    public boolean needUpdate() {
        return !this.versionToInstallList.isEmpty();
    }
}
