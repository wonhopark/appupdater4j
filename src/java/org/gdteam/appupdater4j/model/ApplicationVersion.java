package org.gdteam.appupdater4j.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ApplicationVersion implements Comparable<ApplicationVersion> {

    private String updateURL;
    private boolean needReboot = false;
    private Map<Locale, String> localizedDescription = new HashMap<Locale, String>();
    private Date publicationDate;
    private String name;
    private Long fileSize;
    private Version version = new Version();
    
    
    public String getUpdateURL() {
        return updateURL;
    }
    public void setUpdateURL(String updateURL) {
        this.updateURL = updateURL;
    }
    public boolean isNeedReboot() {
        return needReboot;
    }
    public void setNeedReboot(boolean needReboot) {
        this.needReboot = needReboot;
    }
    
    public Date getPublicationDate() {
        return publicationDate;
    }
    public void setPublicationDate(Date publicationDate) {
        this.publicationDate = publicationDate;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public boolean containsDescription(Locale locale) {
        return localizedDescription.containsKey(locale);
    }
    public String getDescription(Locale locale) {
        return localizedDescription.get(locale);
    }
    public String putDescription(Locale locale, String description) {
        return localizedDescription.put(locale, description);
    }
    public int compareTo(ApplicationVersion appversion) {
        return this.version.compareTo(appversion.version);
    }
    
    public String toString(){
        return this.name;
    }
    public Version getVersion() {
        return version;
    }
    public Long getFileSize() {
        return fileSize;
    }
    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }
    
}
