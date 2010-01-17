package org.gdteam.appupdater4j.install;

import java.io.File;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;


public class AntTargetExecutionRunnable implements Runnable {

    public static final String TARGET_BACKUP = "backup";
    public static final String TARGET_INSTALL = "install";
    public static final String TARGET_RESTORE = "restore";
    
    
    private Properties properties = new Properties();
    
    private CountDownLatch workDone;
    private File antFile;
    private File basedir;
    
    private InstallationException throwed;

    public AntTargetExecutionRunnable(CountDownLatch workDone, String antFile, File basedir, Properties properties){
        this.workDone = workDone;
        this.antFile = new File(antFile);
        this.basedir = basedir;
        this.properties = properties;
    }
    
    private void runBackupTarget() throws Exception{
        this.runTarget(TARGET_BACKUP);
    }
    
    private void runInstallTarget() throws Exception{
        this.runTarget(TARGET_INSTALL);
    }
    
    private void runRestoreTarget() throws Exception{
        this.runTarget(TARGET_RESTORE);
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
              this.throwed = new InstallationException(TARGET_BACKUP, e);
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
                  
                  this.throwed = new InstallationException(TARGET_INSTALL, e);
              } catch (Exception e1) {
                  //Bad bad
                  this.throwed = new InstallationException(TARGET_RESTORE, e1);
              }
          }
          
          this.workDone.countDown();
    }

}
