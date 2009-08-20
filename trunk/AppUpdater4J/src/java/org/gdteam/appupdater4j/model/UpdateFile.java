package org.gdteam.appupdater4j.model;

import java.io.File;

public class UpdateFile  extends File {

    private Version updateVersion = new Version();
    private Version previousVersion = new Version();
    
    public UpdateFile(File parent, String child) {
        super(parent, child);
    }

    public Version getUpdateVersion() {
        return updateVersion;
    }

    public Version getPreviousVersion() {
        return previousVersion;
    }
}
