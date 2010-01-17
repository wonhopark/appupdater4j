package org.gdteam.appupdater4j;

import java.util.List;

import org.gdteam.appupdater4j.model.ApplicationVersion;

public interface UpdateControllerListener {

    /**
     * Method called when controller wants to start udate
     * @param source controller which call the method
     * @param versionList version to install
     */
    public void startUpdate(UpdateController source, List<ApplicationVersion> versionList);
    
    /**
     * Method called buy controller, when wrapped application can be run
     * @param source
     */
    public void canStartWrappedApplication(UpdateController source);
}
