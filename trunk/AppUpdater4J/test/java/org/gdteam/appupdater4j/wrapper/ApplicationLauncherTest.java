package org.gdteam.appupdater4j.wrapper;

import java.io.File;

import junit.framework.Assert;

import org.gdteam.appupdater4j.TestUtil;
import org.junit.Test;

public class ApplicationLauncherTest {

    @Test
    public void testBasic() {
        
        File testFile = new File(System.getProperty("java.io.tmpdir"), "test_" + System.currentTimeMillis());
        testFile.deleteOnExit();
        
        File jar = null;
        try {
            File testwrapperMain = TestUtil.extractInJarFile("testwrapper-main.jar", true);
            File testwrapperWriter = TestUtil.extractInJarFile("testwrapper-writer.jar", true);
            jar = testwrapperMain;
            
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
        
        String[] args = {testFile.getPath()};
        
        ApplicationLauncher launcher = new ApplicationLauncher(jar, args);
        launcher.setJoinThread(true);
        
        try {
            launcher.extractManifestInfoAndListFiles();
            launcher.run();
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
        
        //Check for file
        Assert.assertTrue("File does not exist : " + testFile, testFile.exists());
    }
}
