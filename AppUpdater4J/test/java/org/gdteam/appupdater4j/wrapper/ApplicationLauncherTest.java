package org.gdteam.appupdater4j.wrapper;

import java.io.File;
import java.net.URISyntaxException;

import junit.framework.Assert;

import org.junit.Test;

public class ApplicationLauncherTest {

    @Test
    public void testBasic() {
        
        File testFile = new File(System.getProperty("java.io.tmpdir"), "test_" + System.currentTimeMillis());
        testFile.deleteOnExit();
        
        File jar = null;
        try {
            jar = new File(this.getClass().getClassLoader().getResource("testwrapper-main.jar").toURI());
        } catch (URISyntaxException e) {
            Assert.fail(e.getMessage());
        }
        
        String[] args = {testFile.getPath()};
        
        ApplicationLauncher launcher = new ApplicationLauncher(jar, args);
        launcher.setJoinThread(true);
        
        try {
            launcher.extractManifestInfo();
            launcher.run();
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
        
        //Check for file
        Assert.assertTrue("File does not exist : " + testFile, testFile.exists());
    }
}
