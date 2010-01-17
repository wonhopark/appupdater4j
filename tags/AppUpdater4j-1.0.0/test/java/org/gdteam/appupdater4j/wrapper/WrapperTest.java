package org.gdteam.appupdater4j.wrapper;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

import junit.framework.Assert;

import org.gdteam.appupdater4j.TestUtil;
import org.junit.Test;

public class WrapperTest {

    @Test
    public void testBasic() {
        
        File testFile = new File(System.getProperty("java.io.tmpdir"), "test_" + System.currentTimeMillis());
        testFile.deleteOnExit();
        
        try {
        
            URL[] libURLs = {TestUtil.extractInJarFile("testwrapper-main.jar").toURL(), TestUtil.extractInJarFile("testwrapper-writer.jar").toURL()};
            URLClassLoader classloader = new URLClassLoader(libURLs);
            
            
            String[] args = {testFile.getPath()};
            
            Wrapper wrapper = new Wrapper(classloader, "org.gdteam.appupdater4j.testwrapper.Main", args);
            
            wrapper.start();
            wrapper.join();
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
        
        //Check for file
        Assert.assertTrue("Directory does not exist : " + testFile, testFile.exists());
        
        Assert.assertTrue("File toto does not exist", new File(testFile, "toto").exists());
        
    }
}
