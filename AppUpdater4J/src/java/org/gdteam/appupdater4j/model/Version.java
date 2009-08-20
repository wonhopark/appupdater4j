package org.gdteam.appupdater4j.model;

public class Version implements Comparable<Version>{

    //See: http://en.wikipedia.org/wiki/Software_versioning
    private String major, minor, build, revision;
    
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
    
    public String toString() {
        StringBuilder ret = new StringBuilder(this.major);
        if (this.minor != null) {
            ret.append(".").append(this.minor);
            
            if (this.build != null) {
                ret.append(".").append(this.build);
                
                if (this.revision != null) {
                    ret.append(".").append(this.revision);
                }
            }
        }
        return ret.toString();
    }
    
    public int compareTo(Version version) {
        
        //Compare major
        int majorComp = this.compareVersionItem(this.major, version.major);
        if (majorComp != 0) {
            return majorComp;
        }
        
        //Compare minor
        int minorComp = this.compareVersionItem(this.minor, version.minor);
        if (minorComp != 0) {
            return minorComp;
        }
        
        //Compare build
        int buildComp = this.compareVersionItem(this.build, version.build);
        if (buildComp != 0) {
            return buildComp;
        }
        
        //Compare revision
        return this.compareVersionItem(this.revision, version.revision);
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
        
        Version version = new Version();
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
    
    @Override
    public boolean equals(Object obj) {
        return this.compareTo((Version) obj) == 0;
    }
    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }
    
    
}
