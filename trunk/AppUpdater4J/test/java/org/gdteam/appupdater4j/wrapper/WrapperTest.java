package org.gdteam.appupdater4j.wrapper;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

import junit.framework.Assert;

import org.junit.Test;

public class WrapperTest {

    @Test
    public void testBasic() {
        
        File testFile = new File(System.getProperty("java.io.tmpdir"), "test_" + System.currentTimeMillis());
        testFile.deleteOnExit();
        
        URL[] libURLs = {this.getClass().getClassLoader().getResource("testwrapper-main.jar"), this.getClass().getClassLoader().getResource("testwrapper-writer.jar")};
        URLClassLoader classloader = new URLClassLoader(libURLs);
        
        String[] args = {testFile.getPath()};
        
        Wrapper wrapper = new Wrapper(classloader, "org.gdteam.appupdater4j.testwrapper.Main", args);
        
        wrapper.start();
        try {
            wrapper.join();
        } catch (InterruptedException e) {
            Assert.fail(e.getMessage());
        }
        
        //Check for file
        Assert.assertTrue("File does not exist : " + testFile, testFile.exists());
        
    }
}
