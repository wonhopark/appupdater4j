package org.gdteam.appupdater4j.download;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import junit.framework.Assert;

import org.gdteam.appupdater4j.FileUtil;
import org.gdteam.appupdater4j.model.Version;
import org.junit.Test;

public class FileManagetTest {

    @Test
    public void testGetDownloadedFiles() {
        FileManager fileManager = new FileManager(new File(System.getProperty("java.io.tmpdir")));
        
        //Copy test file in store
        String fileName = "md5-test-update.zip";
        
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(new File(fileManager.getFileStore(), fileName));
            FileUtil.copy(this.getClass().getClassLoader().getResourceAsStream(fileName), out, 1024);
            
            Version version = new Version();
            version.setMajor("0");
            version.setMinor("0");
            version.setBuild("5");
            version.setRevision("1");
            
            Map<Version, File> downloadedFiles = fileManager.getDownloadedFiles();
            Assert.assertEquals(1, downloadedFiles.size());
            Assert.assertEquals(version, downloadedFiles.keySet().toArray()[0]);
            
            Assert.assertNotNull("Cannot find file for version :" + version, downloadedFiles.get(version));
            
        } catch (IOException e) {
            Assert.fail(e.getMessage());
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        
        
        
        
        
    }
}
