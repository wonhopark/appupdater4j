package org.gdteam.appupdater4j;

import org.gdteam.appupdater4j.download.FileDownloadListener;
import org.gdteam.appupdater4j.install.InstallationListener;

public interface UpdateController extends InstallationListener, FileDownloadListener {

    /**
     * Displays controller
     */
    public void displayController();
}
