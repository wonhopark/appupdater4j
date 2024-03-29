package org.gdteam.appupdater4j.install;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

import org.apache.log4j.Logger;
import org.gdteam.appupdater4j.FileUtil;
import org.gdteam.appupdater4j.model.UpdateFile;

public class InstallationHelper {

    private List<InstallationListener> listeners = new ArrayList<InstallationListener>();
    
    private Logger logger = Logger.getLogger(InstallationHelper.class);
    
    /**
     * Install update from zip file
     * @param zip
     * @param listener
     * @throws UpdateException
     */
    public void installUpdate(UpdateFile zip) throws Exception {
        
        logger.info("Start installation from " + zip.getPath());
        
        String dirName = zip.getName().split(".zip")[0];
        StringBuilder dir = new StringBuilder(System.getProperty("java.io.tmpdir")).append("gdteam/").append(dirName).append("/");
        
        for (InstallationListener listener : listeners) {
            listener.installationStarted(zip, dir.toString());
        }

        //Unzip update file
        FileUtil.unzipFile(zip, dir.toString());
        
        //Run ANT targets
        
        Properties properties = new Properties();
        properties.put("installdir", System.getProperty("user.dir"));
        
        try {
            this.runAntTargets(dir.toString(), properties);
            
            for (InstallationListener listener : listeners) {
                listener.installationEnded(zip);
            }
            logger.info("Installation successfully done");
        } catch (Exception e) {
            if (e instanceof InstallationException && ((InstallationException) e).getTarget().equals(AntTargetExecutionRunnable.TARGET_RESTORE)) {
                for (InstallationListener listener : listeners) {
                    listener.restorationFailed(zip, e);
                }
                logger.error("Installation and restoration failed", e);
            } else {
                for (InstallationListener listener : listeners) {
                    listener.installationFailed(zip, e);
                }
                logger.error("Installation failed", e);
            }
            
            
        }
        
    }
    
    private void runAntTargets(String basedir, Properties properties) throws Exception {
        //Create ANT context classloader
        File libDir = new File(basedir + "/lib");
        File[] classpathLibs = libDir.listFiles();
        
        List<URL> urls = new ArrayList<URL>();
        
        for (int i = 0; i < classpathLibs.length; i++) {
            urls.add(classpathLibs[i].toURI().toURL());
        }
        
        
        URLClassLoader cl = new URLClassLoader(urls.toArray(new URL[urls.size()]));
    
        //Run targets
        CountDownLatch workdone = new CountDownLatch(1);
        
        AntTargetExecutionRunnable runnable = new AntTargetExecutionRunnable(workdone, basedir + "/build.xml", new File(basedir), properties);
        new AntTargetExecutionThread(cl, runnable).start();
        
        //Wait for installation ended before continue
        workdone.await();
        
        if (runnable.getThrowed() != null){
            throw runnable.getThrowed();
        }
    }

    public boolean addInstallationListener(InstallationListener o) {
        return listeners.add(o);
    }

    public boolean removeInstallationListener(InstallationListener o) {
        return listeners.remove(o);
    }
}
