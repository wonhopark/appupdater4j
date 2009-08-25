package org.gdteam.appupdater4j;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.gdteam.appupdater4j.download.FileDownloadHelper;
import org.gdteam.appupdater4j.download.FileDownloadListener;
import org.gdteam.appupdater4j.download.FileManager;
import org.gdteam.appupdater4j.install.InstallationHelper;
import org.gdteam.appupdater4j.install.InstallationListener;
import org.gdteam.appupdater4j.model.ApplicationVersion;
import org.gdteam.appupdater4j.model.UpdateFile;
import org.gdteam.appupdater4j.model.Version;
import org.gdteam.appupdater4j.version.VersionHandler;

public class UpdateManager implements InstallationListener, FileDownloadListener{
    
    public static final String PROPERTY_PREFIX = "org.gdteam.appupdater4j";

    private String applicationID;
    private String currentVersion;
    private VersionHandler versionHandler;
    private InstallationHelper installationHelper;
    private FileDownloadHelper fileDownloadHelper;
    
    private ApplicationVersion usedApplicationVersion = null;
    
    private List<UpdateListener> listeners = new ArrayList<UpdateListener>();
    
    
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
    public void configure(Properties props) {
        this.applicationID = props.getProperty("application.id");
        this.currentVersion = props.getProperty("application.version");
        try {
            this.versionHandler = new VersionHandler(new URL(props.getProperty("feed.url")));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        
        this.installationHelper = new InstallationHelper();
        this.installationHelper.addInstallationListener(this);
        
        this.autoFileManager = new FileManager(new File(System.getProperty("user.dir")));
        
        File tempDir = new File(System.getProperty("java.io.tmpdir"), "appupdater4j");
        
        this.fileDownloadHelper = new FileDownloadHelper(tempDir);
        this.fileDownloadHelper.addFileDownloadListener(this);
        
        this.tempFileManager = new FileManager(tempDir);
    }
    
    public void performCheckForUpdate() {
        try {
            this.versionHandler.parse();
            this.versionToInstallList =  this.versionHandler.getInstallVersionList(applicationID, currentVersion);
        } catch (Exception e) {
            e.printStackTrace();
            this.versionToInstallList = new ArrayList<ApplicationVersion>();
        }
        
    }
    
    public boolean needUpdate() {
        return !this.versionToInstallList.isEmpty();
    }
    
    public List<ApplicationVersion> getVersionToInstallList() {
        return this.versionToInstallList;
    }
    
    /**
     * Return true, if auto update was performed
     * @return
     */
    public boolean installAutoUpdate() {
        
        boolean done = false;
        
        List<UpdateFile> files = this.getFilesToAutomaticallyInstall();
        
        if (!files.isEmpty()) {
            for (UpdateFile updateFile : files) {                
                try {
                    this.installUpdateFile(updateFile);
                    done = true;
                } catch (Exception e) {
                    //TODO: define what to do
                    e.printStackTrace();
                }
            }
        }
        
        return done;
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

    public void startUpdate(List<ApplicationVersion> versionList) {
        
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
                
                for (UpdateListener listener : listeners) {
                    listener.wrappedApplicationReadyToBeRun();
                }
            }
        });
        
        thread.start();
    }
    
    private void downloadAndInstallAppVersion(ApplicationVersion appVersion) throws Exception {
        this.usedApplicationVersion = appVersion;
        
        //First, download
        File dest = new File(this.tempFileManager.getFileStore(), System.currentTimeMillis() + ".zip");
        
        File downloaded = this.fileDownloadHelper.downloadFile(new URL(appVersion.getUpdateURL()), dest);
        //check downloaded file
        UpdateFile updateFile = this.tempFileManager.getUpdateFile(downloaded.getName());
        
        if (!this.tempFileManager.isUpdateFileValid(updateFile, applicationID, Version.createVersion(currentVersion))) {
            for (UpdateListener listener : this.listeners) {
                listener.fileDownloadedIsInvalid(appVersion);
            }
            
            throw new Exception("Invalid file : " + downloaded.getPath());
        }
        
        //Second, install
        this.installUpdateFile(updateFile);
        
    }

    public String getCurrentVersion() {
        return currentVersion;
    }

    public void installationEnded(File installFile) {
        for (UpdateListener listener : this.listeners) {
            listener.installationEnded(usedApplicationVersion);
        }
    }

    public void installationFailed(File installFile, Exception e) {
        for (UpdateListener listener : this.listeners) {
            listener.installationFailed(usedApplicationVersion, e);
        }
    }

    public void installationStarted(File installFile, String basedir) {
        for (UpdateListener listener : this.listeners) {
            listener.installationStarted(usedApplicationVersion, basedir);
        }
    }

    public void addUpdateListener(UpdateListener element) {
        listeners.add(element);
    }

    public boolean removeUpdateListener(UpdateListener o) {
        return listeners.remove(o);
    }

    public void downloadDone(URL source, File dest) {
        for (UpdateListener listener : this.listeners) {
            listener.downloadDone(usedApplicationVersion, dest);
        }
    }

    public void downloadFailed(URL source) {
        for (UpdateListener listener : this.listeners) {
            listener.downloadFailed(usedApplicationVersion);
        }
    }

    public void downloadStarted(URL source, long size) {
        for (UpdateListener listener : this.listeners) {
            listener.downloadStarted(usedApplicationVersion, size);
        }
    }

    public void downloadedDataChanged(URL source, long size) {
        for (UpdateListener listener : this.listeners) {
            listener.downloadedDataChanged(usedApplicationVersion, size);
        }
    }

    public void flowSizeChanged(URL source, long size) {
        for (UpdateListener listener : this.listeners) {
            listener.flowSizeChanged(usedApplicationVersion, size);
        }
    }

    public void restorationFailed(File installFile, Exception e) {
        for (UpdateListener listener : this.listeners) {
            listener.restorationFailed(usedApplicationVersion, e);
        }
    }
}
