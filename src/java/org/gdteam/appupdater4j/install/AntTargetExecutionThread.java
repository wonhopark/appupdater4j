package org.gdteam.appupdater4j.install;

public class AntTargetExecutionThread extends Thread {

    public AntTargetExecutionThread(ClassLoader classloader, AntTargetExecutionRunnable runnable){
        super(runnable);
        this.setContextClassLoader(classloader);
    }
}
