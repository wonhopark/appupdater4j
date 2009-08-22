package org.gdteam.appupdater4j.download;

import java.io.File;
import java.net.URL;

public interface FileDownloadListener {

    /**
     * Notify listener that download flow size changed
     * @param source
     * @param size bytes / second
     */
    public void flowSizeChanged(URL source, long size);
    
    /**
     * Notify listener that size of downloaded data change
     * @param source
     * @param size in bytes
     */
    public void downloadedDataChanged(URL source, long size);
    
    /**
     * Notify listener that download is done
     * @param source
     * @param dest
     */
    public void downloadDone(URL source, File dest);
    
    /**
     * Notify listener that download failed
     * @param source
     */
    public void downloadFailed(URL source);
    
    /**
     * Notify listener that download is started
     * @param source
     * @param size initial file size (bytes)
     */
    public void downloadStarted(URL source, long size);

}
