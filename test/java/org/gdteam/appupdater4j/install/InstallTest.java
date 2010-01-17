package org.gdteam.appupdater4j.install;

import java.io.File;

import junit.framework.Assert;

import org.gdteam.appupdater4j.TestUtil;
import org.gdteam.appupdater4j.model.UpdateFile;
import org.junit.Test;

public class InstallTest {

    @Test
    public void testFirst() {
        try {
            
            UpdateFile file = new UpdateFile(TestUtil.extractInJarFile("ant-test-first.zip").toURI());
            
            InstallationHelper installHelper = new InstallationHelper();
            installHelper.installUpdate(file);
            installHelper.addInstallationListener(new InstallationListener() {
                
                private File basedir;
    
                public void installationEnded(File installFile) {
                    //Check if dirs are created
                    File installSuccess = new File(basedir, "install-success");
                    Assert.assertTrue("Cannot find install-success", installSuccess.exists());
                    File backupSuccess = new File(basedir, "backup-success");
                    Assert.assertTrue("Cannot find backup-success", backupSuccess.exists());
                }
    
                public void installationFailed(File installFile, Exception e) {
                    Assert.fail("Install failed : " + e.getMessage());
                }
    
                public void installationStarted(File installFile, String basedir) {
                    this.basedir = new File(basedir);
                    System.out.println(this.basedir.getPath());
                }

                public void restorationFailed(File installFile, Exception e) {
                    // TODO Auto-generated method stub
                    
                }
                
            });
        
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }
    
    @Test
    public void testSecond() {
        try {
            UpdateFile file = new UpdateFile(TestUtil.extractInJarFile("ant-test-second.zip").toURI());
            
            InstallationHelper installHelper = new InstallationHelper();
            installHelper.installUpdate(file);
            installHelper.addInstallationListener(new InstallationListener() {
                
                private File basedir;
    
                public void installationEnded(File installFile) {
                    //Check if dirs are created
                    File restoreSuccess = new File(basedir, "restore-success");
                    Assert.assertTrue("Cannot find restore-success", restoreSuccess.exists());
                    File backupSuccess = new File(basedir, "backup-success");
                    Assert.assertTrue("Cannot find backup-success", backupSuccess.exists());
                }
    
                public void installationFailed(File installFile, Exception e) {
                    Assert.assertTrue("Bad ant message : " + e.getMessage(), e.getMessage().contains("Explicitly failed for test purpose"));
                }
    
                public void installationStarted(File installFile, String basedir) {
                    this.basedir = new File(basedir);
                    System.out.println(this.basedir.getPath());
                }

                public void restorationFailed(File installFile, Exception e) {
                    // TODO Auto-generated method stub
                    
                }
                
            });
        
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
        
    }
    
}
