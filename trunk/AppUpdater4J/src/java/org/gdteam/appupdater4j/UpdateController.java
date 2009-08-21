package org.gdteam.appupdater4j;

import java.util.List;

import org.gdteam.appupdater4j.download.FileDownloadListener;
import org.gdteam.appupdater4j.install.InstallationListener;
import org.gdteam.appupdater4j.model.ApplicationVersion;

public interface UpdateController extends InstallationListener, FileDownloadListener {

    /**
     * Add listener
     * @param listener
     */
    public void addUpdateControllerListener(UpdateControllerListener listener);
    
    /**
     * Remove listener
     * @param listener
     */
    public void removeUpdateControllerListener(UpdateControllerListener listener);
    /**
     * Displays controller
     */
    public void displayController();
    
    /**
     * Method called when application update found
     * @param versionList
     */
    public void setVersionToInstall(List<ApplicationVersion> versionList);
    
}
