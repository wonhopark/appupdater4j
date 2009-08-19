package org.gdteam.appupdater4j.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ApplicationVersion {

    //See: http://en.wikipedia.org/wiki/Software_versioning
    private String major, minor, build, revision;
    private String updateURL;
    private boolean needReboot = false;
    private Map<Locale, String> localizedDescription = new HashMap<Locale, String>();
    private Date publicationDate;
    private String name;
    
    public String getMajor() {
        return major;
    }
    public void setMajor(String major) {
        this.major = major;
    }
    public String getMinor() {
        return minor;
    }
    public void setMinor(String minor) {
        this.minor = minor;
    }
    public String getBuild() {
        return build;
    }
    public void setBuild(String build) {
        this.build = build;
    }
    public String getRevision() {
        return revision;
    }
    public void setRevision(String revision) {
        this.revision = revision;
    }
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
    
}
