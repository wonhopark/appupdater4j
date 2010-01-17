package org.gdteam.appupdater4j.install;

import java.io.File;


public interface InstallationListener {

    /**
     * Notify listener for installation
     * @param installFile install file
     * @param installation basedir
     */
    public void installationStarted(File installFile, String basedir);
    
    /**
     * Notify listener for installation end
     * @param installFile install file
     */
    public void installationEnded(File installFile);
    
    /**
     * Notify listener that installation failed
     * @param installFile install file
     * @Param exception
     */
    public void installationFailed(File installFile, Exception e);
    
    /**
     * Notify listener that restoration failed
     * @param installFile
     * @param e
     */
    public void restorationFailed(File installFile, Exception e);
}
