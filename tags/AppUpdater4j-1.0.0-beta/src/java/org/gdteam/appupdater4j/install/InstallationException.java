package org.gdteam.appupdater4j.install;

public class InstallationException extends Exception{

    private String target;
    
    public InstallationException(String target, Throwable t) {
        super(t);
        this.target = target;
    }

    public String getTarget() {
        return target;
    }
}
