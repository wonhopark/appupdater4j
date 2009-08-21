package org.gdteam.appupdater4j;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.gdteam.appupdater4j.download.FileDownloadHelper;
import org.gdteam.appupdater4j.download.FileManager;
import org.gdteam.appupdater4j.install.InstallationHelper;
import org.gdteam.appupdater4j.model.ApplicationVersion;
import org.gdteam.appupdater4j.model.UpdateFile;
import org.gdteam.appupdater4j.model.Version;
import org.gdteam.appupdater4j.version.VersionHandler;

public class UpdateManager implements UpdateControllerListener{
    
    public static final String PROPERTY_PREFIX = "org.gdteam.appupdater4j";

    private String applicationID;
    private String currentVersion;
    private VersionHandler versionHandler;
    private InstallationHelper installationHelper = new InstallationHelper();
    private FileDownloadHelper fileDownloadHelper;
    
    
    /**
     * If update files are in this store, they will be automatically installed
     */
    private FileManager autoFileManager;
    
    private List<ApplicationVersion> versionToInstallList;

    private FileManager tempFileManager;

    /**
     * Configure update manager
     * @param properties
     */
    public void configure(Properties propertiesParam) {
      //First get properties from classpath
        Properties props = new Properties();
        try {
            props.load(this.getClass().getClassLoader().getResourceAsStream("updatemanager.cfg.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        //Override properties
        Iterator argKeySet = propertiesParam.keySet().iterator();
        while (argKeySet.hasNext()) {
            Object key = (Object) argKeySet.next();
            props.put(key, propertiesParam.get(key));
        }
        
        this.applicationID = props.getProperty("application.id");
        this.currentVersion = props.getProperty("application.version");
        try {
            this.versionHandler = new VersionHandler(new URL(props.getProperty("feed.url")));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        
        this.autoFileManager = new FileManager(new File(System.getProperty("user.dir")));
        
        File tempDir = new File(System.getProperty("java.io.tmpdir"), "appupdater4j");
        
        this.fileDownloadHelper = new FileDownloadHelper(tempDir);
        this.tempFileManager = new FileManager(tempDir);
    }
    
    public void performCheckForUpdate() {
        this.versionHandler.parse();
        this.versionToInstallList =  this.versionHandler.getInstallVersionList(applicationID, currentVersion);
    }
    
    public boolean needUpdate() {
        return !this.versionToInstallList.isEmpty();
    }
    
    public List<ApplicationVersion> getVersionToInstallList() {
        return this.versionToInstallList;
    }
    
    public void installAutoUpdate() {
        
        List<UpdateFile> files = this.getFilesToAutomaticallyInstall();
        
        if (!files.isEmpty()) {
            for (UpdateFile updateFile : files) {                
                try {
                    this.installUpdateFile(updateFile);
                } catch (Exception e) {
                    //TODO: define what to do
                    e.printStackTrace();
                }
            }
        }
    }
    
    private void installUpdateFile(UpdateFile file) throws Exception {
        this.installationHelper.installUpdate(file);
        Version installedVersion = file.getUpdateVersion();
        //Delete updateFile
        file.delete();
        
        //Update current version
        this.currentVersion = installedVersion.toString();
    }
    
    /**
     * Get files which must be installed sorted by version()
     * @return
     */
    public List<UpdateFile> getFilesToAutomaticallyInstall() {
        return this.autoFileManager.getDownloadedFiles(this.applicationID, Version.createVersion(this.currentVersion));
    }

    public void startUpdate(UpdateController source, List<ApplicationVersion> versionList) {
        
        final List<ApplicationVersion> rVersionList = versionList;
        Thread thread = new Thread(new Runnable(){
            public void run() {
                try {
                    for (ApplicationVersion applicationVersion : rVersionList) {
                        downloadAndInstallAppVersion(applicationVersion);
                    }
                }catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
            }
        });
        
        thread.start();
    }
    
    private void downloadAndInstallAppVersion(ApplicationVersion appVersion) throws Exception {
        //First, download
        File dest = new File(this.tempFileManager.getFileStore(), System.currentTimeMillis() + ".zip");
        
        File downloaded = this.fileDownloadHelper.downloadFile(new URL(appVersion.getUpdateURL()), dest);
        //check downloaded file
        UpdateFile updateFile = this.tempFileManager.getUpdateFile(downloaded.getName());
        
        if (!this.tempFileManager.isUpdateFileValid(updateFile, applicationID, Version.createVersion(currentVersion))) {
            throw new Exception("Invalid file");
        }
        
        //Second, install
        this.installUpdateFile(updateFile);
        
    }

    public InstallationHelper getInstallationHelper() {
        return installationHelper;
    }

    public FileDownloadHelper getFileDownloadHelper() {
        return fileDownloadHelper;
    }

    public String getCurrentVersion() {
        return currentVersion;
    }
}
