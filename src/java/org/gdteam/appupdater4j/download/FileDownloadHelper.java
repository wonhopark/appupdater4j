package org.gdteam.appupdater4j.download;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FileDownloadHelper {

    private List<FileDownloadListener> listeners = new ArrayList<FileDownloadListener>();
    private File tempDir;
    
    public FileDownloadHelper(File tempDir) {
        this.tempDir = tempDir;
    }

    public boolean addFileDownloadListener(FileDownloadListener o) {
        return listeners.add(o);
    }

    public boolean removeFileDownloadListener(FileDownloadListener o) {
        return listeners.remove(o);
    }
    
    public File downloadFile(URL url, File dest) throws Exception {
        
        FileDownload download = new FileDownload(url, dest);
        
        for (FileDownloadListener listener : this.listeners) {
            download.addFileDownloadListener(listener);
        }
        
        download.performDownload();
        
        return dest;
    }
    
}
