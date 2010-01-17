package org.gdteam.appupdater4j.model;

import java.io.File;
import java.net.URI;

public class UpdateFile  extends File {

    private Version updateVersion = new Version();
    private Version previousVersion = new Version();
    private String applicationID;
    
    public UpdateFile(File parent, String child) {
        super(parent, child);
    }
    
    public UpdateFile(String path) {
        super(path);
    }
    
    public UpdateFile(URI uri) {
        super(uri);
    }

    public Version getUpdateVersion() {
        return updateVersion;
    }

    public Version getPreviousVersion() {
        return previousVersion;
    }

    public String getApplicationID() {
        return applicationID;
    }

    public void setApplicationID(String applicationID) {
        this.applicationID = applicationID;
    }
}
