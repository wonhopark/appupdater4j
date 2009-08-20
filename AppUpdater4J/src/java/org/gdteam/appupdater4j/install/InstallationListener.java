package org.gdteam.appupdater4j.install;


public interface InstallationListener {

    /**
     * Notify listener for installation
     * @param installation basedir
     */
    public void installationStarted(String basedir);
    
    /**
     * Notify listener for installation end
     */
    public void installationEnded();
    
    /**
     * Notify listener that installation failed
     * @Param exception
     */
    public void installationFailed(Exception e);
}
