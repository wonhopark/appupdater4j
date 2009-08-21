package org.gdteam.appupdater4j.download;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.gdteam.appupdater4j.FileUtil;
import org.gdteam.appupdater4j.model.UpdateFile;
import org.gdteam.appupdater4j.model.Version;

public class FileManager {
    
    public static final String PROPERTY_FILE_NAME = "update.properties";
    public static final FileFilter UPDATE_FILE_FILTER = new FileFilter() {

        public boolean accept(File pathname) {
            return pathname.getName().endsWith(".zip");
        }
        
    };
    
    private File fileStore;

    public FileManager(File fileStore) {
        this.fileStore = fileStore;        
    }
    
    
    public UpdateFile getUpdateFile(String fileName) {
        String md5 = null;
        String md5ToCompare = null;
        UpdateFile updateFile = new UpdateFile(fileStore, fileName);
        
        ZipFile zipFile = null;
        try {
            zipFile = new ZipFile(updateFile);
            Enumeration<ZipEntry> entries = (Enumeration<ZipEntry>) zipFile.entries();
            
            while (entries.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                if (!entry.isDirectory()) {
                    if (entry.getName().endsWith(".md5")) {
                        md5 = entry.getName().substring(0, entry.getName().indexOf(".md5"));
                    } else if (PROPERTY_FILE_NAME.equals(entry.getName())) {
                        Properties updateProperties = new Properties();
                        updateProperties.load(zipFile.getInputStream(entry));
                        
                        updateFile.setApplicationID(updateProperties.getProperty("product"));
                        
                        updateFile.getUpdateVersion().setMajor(updateProperties.getProperty("version.major"));
                        updateFile.getUpdateVersion().setMinor(updateProperties.getProperty("version.minor"));
                        updateFile.getUpdateVersion().setBuild(updateProperties.getProperty("version.build"));
                        updateFile.getUpdateVersion().setRevision(updateProperties.getProperty("version.revision"));
                        
                        Version previous = Version.createVersion(updateProperties.getProperty("previous.version"));
                        
                        updateFile.getPreviousVersion().setMajor(previous.getMajor());
                        updateFile.getPreviousVersion().setMinor(previous.getMinor());
                        updateFile.getPreviousVersion().setBuild(previous.getBuild());
                        updateFile.getPreviousVersion().setRevision(previous.getRevision());
                        
                        md5ToCompare = FileUtil.getMD5(zipFile.getInputStream(entry));
                        
                    }
                }
            }
            
            if (md5.equals(md5ToCompare)) {
                return updateFile;
            }
            
            return null;
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (zipFile != null) {
                try {
                    zipFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public File getFileStore() {
        return fileStore;
    }
    
    /**
     * Return true if file is a valid update file
     * @param updateFile
     * @param applicationID
     * @param currentVersion
     * @return
     */
    public boolean isUpdateFileValid(UpdateFile updateFile, String applicationID, Version currentVersion) {
        return updateFile != null && updateFile.getApplicationID().equals(applicationID) && updateFile.getPreviousVersion().equals(currentVersion);
    }

    /**
     * Get valid downloaded files which can be installed (sorted by version)
     * @param applicationID
     * @param currentVersion
     * @return
     */
    public List<UpdateFile> getDownloadedFiles(String applicationID, Version currentVersion) {
        List<UpdateFile> found = new ArrayList<UpdateFile>();
        
        for (File file : fileStore.listFiles(UPDATE_FILE_FILTER)) {
            UpdateFile updateFile = this.getUpdateFile(file.getName());
            if (this.isUpdateFileValid(updateFile, applicationID, currentVersion)) {
                found.add(updateFile);
            }
        }
        
        //Sort found files by version
        Collections.sort(found, new Comparator<UpdateFile>(){

            public int compare(UpdateFile o1, UpdateFile o2) {
                return o1.getUpdateVersion().compareTo(o2.getUpdateVersion());
            }
            
        });
        
        //Check if file suite is correct
        List<UpdateFile> ret = new ArrayList<UpdateFile>();
        Version previousVersion = currentVersion;
        for (UpdateFile updateFile : found) {
            if (updateFile.getPreviousVersion().equals(previousVersion)) {
                ret.add(updateFile);
                previousVersion = updateFile.getUpdateVersion();
            } else {
                break;
            }
        }
        
        return ret;
    }
    
    
}
