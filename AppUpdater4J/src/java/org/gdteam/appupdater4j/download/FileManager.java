package org.gdteam.appupdater4j.download;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.gdteam.appupdater4j.FileUtil;
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
    
    private Version getFileVersion(String fileName) {
        String md5 = null;
        String md5ToCompare = null;
        Version ret = new Version();
        
        ZipFile zipFile = null;
        try {
            zipFile = new ZipFile(new File(fileStore, fileName));
            Enumeration<ZipEntry> entries = (Enumeration<ZipEntry>) zipFile.entries();
            
            while (entries.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                if (!entry.isDirectory()) {
                    if (entry.getName().endsWith(".md5")) {
                        md5 = entry.getName().substring(0, entry.getName().indexOf(".md5"));
                    } else if (PROPERTY_FILE_NAME.equals(entry.getName())) {
                        Properties updateProperties = new Properties();
                        updateProperties.load(zipFile.getInputStream(entry));
                        
                        ret.setMajor(updateProperties.getProperty("version.major"));
                        ret.setMinor(updateProperties.getProperty("version.minor"));
                        ret.setBuild(updateProperties.getProperty("version.build"));
                        ret.setRevision(updateProperties.getProperty("version.revision"));
                        
                        md5ToCompare = FileUtil.getMD5(zipFile.getInputStream(entry));
                    }
                }
            }
            
            if (md5.equals(md5ToCompare)) {
                return ret;
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

    public Map<Version, File> getDownloadedFiles() {
        Map<Version, File> ret = new HashMap<Version, File>();
        
        for (File file : fileStore.listFiles(UPDATE_FILE_FILTER)) {
            Version version = this.getFileVersion(file.getName());
            if (version != null) {
                ret.put(version, file);
            }
        }
        return ret;
    }
    
    
}
