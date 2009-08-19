package org.gdteam.appupdater4j.version;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.gdteam.appupdater4j.model.Application;
import org.gdteam.appupdater4j.model.ApplicationVersion;
import org.gdteam.appupdater4j.version.feed.RSSReader;


//TODO: local data
public class VersionHandler {

    private URL feedURL;
    
    private Application parsedApplication = null;

    public VersionHandler(URL feedURL) {
        this.feedURL = feedURL;
    }
    
    public void parse() {
        try {
            this.parsedApplication = RSSReader.getApplication(this.feedURL);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            if (applicationVersion.compareToStringVersion(currentVersion) > 0) {
                versionToInstall.add(applicationVersion);
            }
        }
        
        Collections.sort(versionToInstall);
        
        return versionToInstall;
    }
}
