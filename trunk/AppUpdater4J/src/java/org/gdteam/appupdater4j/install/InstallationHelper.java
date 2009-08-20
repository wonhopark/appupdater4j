package org.gdteam.appupdater4j.install;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

import org.gdteam.appupdater4j.FileUtil;
import org.gdteam.appupdater4j.model.UpdateFile;

public class InstallationHelper {

    private InstallationListener listener;

    /**
     * Install update from zip file
     * @param zip
     * @param listener
     * @throws UpdateException
     */
    public void installUpdate(UpdateFile zip, InstallationListener listener) throws Exception {
        
        this.listener = listener;
        
        String dirName = zip.getName().split(".zip")[0];
        StringBuilder dir = new StringBuilder(System.getProperty("java.io.tmpdir")).append("/gdteam/").append(dirName).append("/");
        
        if (listener != null){
            listener.installationStarted(dir.toString());
        }

        //Unzip update file
        FileUtil.unzipFile(zip, dir.toString());
        
        //Run ANT targets
        
        Properties properties = new Properties();
        properties.put("installdir", System.getProperty("user.dir"));
        
        this.runAntTargets(dir.toString(), properties);
        
        
        if (listener != null){
            listener.installationEnded();
        }
    }
    
    private void runAntTargets(String basedir, Properties properties) {
        try {
            
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
        
        } catch (Exception e) {
            if (listener != null){
                listener.installationFailed(e);
            }
        }
    }
}
