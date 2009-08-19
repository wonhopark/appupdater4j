package org.gdteam.appupdater4j.download;

public interface FileDownloadListener {

    /**
     * Notify listener that download flow size changed
     * @param size bytes / second
     */
    public void flowSizeChanged(long size);
    
    /**
     * Notify listener that size of downloaded data change
     * @param size in bytes
     */
    public void downloadedDataChanged(long size);
    
    /**
     * Notify listener that download is done
     */
    public void downloadDone();
    
    /**
     * Notify listener that download failed
     */
    public void downloadFailed();
    
    /**
     * Notify listener that download is started
     * @param size initial file size (bytes)
     */
    public void downloadStarted(long size);

}
