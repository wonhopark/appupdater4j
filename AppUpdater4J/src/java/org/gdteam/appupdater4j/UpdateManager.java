package org.gdteam.appupdater4j;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.gdteam.appupdater4j.download.FileManager;
import org.gdteam.appupdater4j.model.ApplicationVersion;
import org.gdteam.appupdater4j.model.UpdateFile;
import org.gdteam.appupdater4j.model.Version;
import org.gdteam.appupdater4j.version.VersionHandler;

public class UpdateManager {
    
    public static final String PROPERTY_PREFIX = "org.gdteam.appupdater4j";

    private String applicationID;
    private String currentVersion;
    private VersionHandler versionHandler;
    
    private FileManager tempFileManager;
    
    /**
     * If update files are in this store, they will be automatically installed
     */
    private FileManager autoFileManager;
    
    private List<ApplicationVersion> versionToInstallList;

    /**
     * Configure update manager
     * @param properties
     */
    public void configure(Properties propertiesParam) {
      //First get properties from classpath
        Properties props = new Properties();
        try {
            props.load(this.getClass().getClassLoader().getResourceAsStream("appupdater4j.cfg.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        //Override properties
        Iterator argKeySet = propertiesParam.keySet().iterator();
        while (argKeySet.hasNext()) {
            Object key = (Object) argKeySet.next();
            props.put(key, System.getProperty((String) key));
        }
        
        this.applicationID = props.getProperty("application.id");
        this.currentVersion = props.getProperty("application.version");
        try {
            this.versionHandler = new VersionHandler(new URL(props.getProperty("feed.url")));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        
        this.autoFileManager = new FileManager(new File(System.getProperty("user.dir")));
    }
    
    public void performCheckForUpdate() {
        this.versionToInstallList =  this.versionHandler.getInstallVersionList(applicationID, currentVersion);
    }
    
    public boolean needUpdate() {
        return !this.versionToInstallList.isEmpty();
    }
    
    /**
     * Get files which must be installed sorted by version()
     * @return
     */
    public List<UpdateFile> getFilesToAutomaticallyInstall() {
        return this.autoFileManager.getDownloadedFiles(this.applicationID, Version.createVersion(this.currentVersion));
    }
}
