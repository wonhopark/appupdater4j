package org.gdteam.appupdater4j.model;

import java.util.ArrayList;
import java.util.List;

public class Application {

    private String id;
    private String name;
    private ApplicationUpdateType updateType;
    
    private List<ApplicationVersion> versions = new ArrayList<ApplicationVersion>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ApplicationVersion> getVersions() {
        return versions;
    }

    public boolean addVersion(ApplicationVersion o) {
        return versions.add(o);
    }

    public boolean removeVersion(ApplicationVersion o) {
        return versions.remove(o);
    }

    public ApplicationUpdateType getUpdateType() {
        return updateType;
    }

    public void setUpdateType(ApplicationUpdateType updateType) {
        this.updateType = updateType;
    }
    
    
}
