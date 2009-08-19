package org.gdteam.appupdater4j.download;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FileDownload {

    private URL source;
    private File dest;
    private List<FileDownloadListener> listeners;

    public FileDownload(URL src, File dest) {
        this.source = src;
        this.dest = dest;
        this.listeners = new ArrayList<FileDownloadListener>();
    }

    public void performDownload() throws Exception{
        OutputStream out = null;
        URLConnection connection = null;
        InputStream in = null;

        try {

            // Create local directories
            if (dest.getParentFile() != null && !dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();
            }

            out = new BufferedOutputStream(new FileOutputStream(dest));

            connection = source.openConnection();
            connection.setConnectTimeout(4000);

            // TODO: proxies
            connection.connect();
            
            for (FileDownloadListener listener : this.listeners) {
                listener.downloadStarted(Integer.valueOf(connection.getContentLength()).longValue());
            }

            in = connection.getInputStream();

            byte[] buffer = new byte[1024];
            int numRead;
            long numWritten = 0;

            long tempWritten = 0;
            Date tic = new Date(), tac = null;

            while ((numRead = in.read(buffer)) != -1) {
                tac = new Date();
                // More than one seconds
                long duration = tac.getTime() - tic.getTime();
                if (duration >= 5000) {
                    // Calc bytes / ms
                    long flowSize = 1000 * tempWritten / duration;
                    
                    for (FileDownloadListener listener : this.listeners) {
                        listener.flowSizeChanged(flowSize);
                    }
                    
                    tempWritten = 0;
                    tic = new Date();
                }
                out.write(buffer, 0, numRead);
                numWritten += numRead;
                tempWritten += numRead;
                
                for (FileDownloadListener listener : this.listeners) {
                    listener.downloadedDataChanged(numWritten);
                }
            }
            
            for (FileDownloadListener listener : this.listeners) {
                listener.downloadDone();
            }
            
        } catch (Exception e) {
            for (FileDownloadListener listener : this.listeners) {
                listener.downloadFailed();
            }            
            throw e;
        }finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public boolean addFileDownloadListener(FileDownloadListener o) {
        return listeners.add(o);
    }

    public boolean removeFileDownloadListener(FileDownloadListener o) {
        return listeners.remove(o);
    }

}
