package org.gdteam.appupdater4j;

import java.io.File;
import java.util.List;

import org.gdteam.appupdater4j.model.ApplicationVersion;

public interface UpdateListener {

    /**
     * Method called when application update found
     * @param versionList
     */
    public void setVersionToInstall(List<ApplicationVersion> versionList);
    
    /**
     * Notify listener for installation
     * @param applicationVersion
     * @param installation basedir
     */
    public void installationStarted(ApplicationVersion applicationVersion, String basedir);
    
    /**
     * Notify listener for installation end
     * @param applicationVersion
     */
    public void installationEnded(ApplicationVersion applicationVersion);
    
    /**
     * Notify listener tha restoration failed
     * @Param exception
     * @param applicationVersion
     */
    public void restorationFailed(ApplicationVersion applicationVersion, Exception e);
    
    /**
     * Notify listener that installation failed
     * @param applicationVersion
     * @Param exception
     */
    public void installationFailed(ApplicationVersion applicationVersion, Exception e);
    
    /**
     * Notify listener that download flow size changed
     * @param applicationVersion
     * @param size bytes / second
     */
    public void flowSizeChanged(ApplicationVersion applicationVersion, long size);
    
    /**
     * Notify listener that size of downloaded data change
     * @param applicationVersion
     * @param size in bytes
     */
    public void downloadedDataChanged(ApplicationVersion applicationVersion, long size);
    
    /**
     * Notify listener that download is done
     * @param applicationVersion
     * @param dest
     */
    public void downloadDone(ApplicationVersion applicationVersion, File dest);
    
    /**
     * Notify listener that download failed
     * @param applicationVersion
     */
    public void downloadFailed(ApplicationVersion applicationVersion);
    
    /**
     * Notify listener that downloaded file is invalid
     * @param applicationVersion
     */
    public void fileDownloadedIsInvalid(ApplicationVersion applicationVersion);
    
    /**
     * Notify listener that download is started
     * @param applicationVersion
     * @param size initial file size (bytes)
     */
    public void downloadStarted(ApplicationVersion applicationVersion, long size);
    
    /**
     * Notify listener that wrapped application is ready to be run
     */
    public void wrappedApplicationReadyToBeRun();
    
    
}
