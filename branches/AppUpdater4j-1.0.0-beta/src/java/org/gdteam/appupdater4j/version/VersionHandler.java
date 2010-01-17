package org.gdteam.appupdater4j.version;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.gdteam.appupdater4j.model.Application;
import org.gdteam.appupdater4j.model.ApplicationVersion;
import org.gdteam.appupdater4j.version.feed.RSSReader;

public class VersionHandler {

    private URL feedURL;
    
    private Application parsedApplication = null;

    public VersionHandler(URL feedURL) {
        this.feedURL = feedURL;
    }
    
    public void parse() throws Exception {
        this.parsedApplication = RSSReader.getApplication(this.feedURL);
    }
    
    /**
     * Get versions to install
     * @param applicationID
     * @param currentVersion
     * @return
     */
    public List<ApplicationVersion> getInstallVersionList(String applicationID, String currentVersion) {
        if (this.parsedApplication == null) {
            throw new IllegalArgumentException("You must parse before calling these method");
        }
        
        if (!applicationID.equals(this.parsedApplication.getId())) {
            throw new IllegalArgumentException("Provided application id : " + applicationID + " does not correspond to feed application id : " + this.parsedApplication.getId());
        }
        
        List<ApplicationVersion> versionToInstall = new ArrayList<ApplicationVersion>();
        
        for (ApplicationVersion applicationVersion : this.parsedApplication.getVersions()) {
            if (applicationVersion.getVersion().compareToStringVersion(currentVersion) > 0) {
                versionToInstall.add(applicationVersion);
            }
        }
        
        Collections.sort(versionToInstall);
        
        //Do not return version after a reboot one
        int firstIndexOfRebootVersion = this.getFirstIndexOfRebootVersion(versionToInstall);
        
        if (firstIndexOfRebootVersion == -1) {
            return versionToInstall;
        } else {
            return versionToInstall.subList(0, firstIndexOfRebootVersion + 1);
        }
    }
    
    private int getFirstIndexOfRebootVersion(List<ApplicationVersion> versionToInstall) {
        
        int i = 0;
        
        for (ApplicationVersion applicationVersion : versionToInstall) {
            
            if (applicationVersion.isNeedReboot()) {
                return i;
            }
            
            i++;
        }
        
        return -1;
    }
}
