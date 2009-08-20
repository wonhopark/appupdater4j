package org.gdteam.appupdater4j.install;

import java.io.File;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;


public class AntTargetExecutionRunnable implements Runnable {

    private Properties properties = new Properties();
    
    private CountDownLatch workDone;
    private File antFile;
    private File basedir;
    
    private Exception throwed;

    public AntTargetExecutionRunnable(CountDownLatch workDone, String antFile, File basedir, Properties properties){
        this.workDone = workDone;
        this.antFile = new File(antFile);
        this.basedir = basedir;
        this.properties = properties;
    }
    
    private void runBackupTarget() throws Exception{
        this.runTarget("backup");
    }
    
    private void runInstallTarget() throws Exception{
        this.runTarget("install");
    }
    
    private void runRestoreTarget() throws Exception{
        this.runTarget("restore");
    }
    

    private void runTarget(String target) throws Exception {
        AntHelper.invokeANT(basedir, antFile, target, properties);
    }

    public Exception getThrowed() {
        return throwed;
    }

    public void run() {
        try {
            //Run backup target
            this.runBackupTarget();
          } catch (Exception e) {
              this.throwed = e;
              this.workDone.countDown();
              return;
          }
          
          try {
              //Backup done. Perform installation
              this.runInstallTarget();
          } catch (Exception e) {
              //Perform restore, if installation failed
              try {
                  this.runRestoreTarget();
                  
                  this.throwed = e;
              } catch (Exception e1) {
                  //Bad bad
                  this.throwed = e1;
              }
          }
          
          this.workDone.countDown();
    }

}
