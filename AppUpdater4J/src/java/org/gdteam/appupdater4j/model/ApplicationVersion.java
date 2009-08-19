package org.gdteam.appupdater4j.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ApplicationVersion implements Comparable<ApplicationVersion> {

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
    public int compareTo(ApplicationVersion appversion) {
        
        //Compare major
        int majorComp = this.compareVersionItem(this.major, appversion.major);
        if (majorComp != 0) {
            return majorComp;
        }
        
        //Compare minor
        int minorComp = this.compareVersionItem(this.minor, appversion.minor);
        if (minorComp != 0) {
            return minorComp;
        }
        
        //Compare build
        int buildComp = this.compareVersionItem(this.build, appversion.build);
        if (buildComp != 0) {
            return buildComp;
        }
        
        //Compare revision
        return this.compareVersionItem(this.revision, appversion.revision);
    }
    
    private int compareVersionItem(String item1, String item2) {
        if (item1 == item2) {
            return 0;
        } else if (item1 != null && item2 == null) {
            return 1;
        } else if (item1 == null && item2 != null) {
            return -1;
        } else {
            Integer i1 = 0;
            Integer i2 = 0;
            
            try {
                i1 = Integer.valueOf(item1);
                i2 = Integer.valueOf(item2);
            } catch (Exception e) {
                //Do nothing
            }
            
            return i1.compareTo(i2);
        }
    }
    
    public int compareToStringVersion(String stringVersion) {
        String[] splittedVersion = stringVersion.split("\\.");
        
        ApplicationVersion version = new ApplicationVersion();
        if (splittedVersion.length > 0) {
            version.setMajor(splittedVersion[0]);
        }
        
        if (splittedVersion.length > 1) {
            version.setMinor(splittedVersion[1]);
        }
        
        if (splittedVersion.length > 2) {
            version.setBuild(splittedVersion[2]);
        }
        
        if (splittedVersion.length > 3) {
            version.setRevision(splittedVersion[3]);
        }
        
        return this.compareTo(version);
    }
    
    public String toString(){
        return this.name;
    }
    
}
