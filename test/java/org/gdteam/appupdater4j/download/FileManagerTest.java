package org.gdteam.appupdater4j.download;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import junit.framework.Assert;

import org.gdteam.appupdater4j.FileUtil;
import org.gdteam.appupdater4j.model.UpdateFile;
import org.gdteam.appupdater4j.model.Version;
import org.junit.Test;

public class FileManagerTest {

    @Test
    public void testGetDownloadedFiles() {
        FileManager fileManager = new FileManager(new File(System.getProperty("java.io.tmpdir")));
        
        //Copy test file in store
        String fileName = "md5-test-update.zip";
        
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(new File(fileManager.getFileStore(), fileName));
            FileUtil.copy(this.getClass().getClassLoader().getResourceAsStream(fileName), out, 1024);
            
            Version badCurrent = Version.createVersion("0.0.4");
            Version goodCurrent = Version.createVersion("0.0.5");
            
            List<UpdateFile> downloadedFiles = fileManager.getDownloadedFiles("myappid", badCurrent);
            
            Assert.assertEquals(0, downloadedFiles.size());
            
            downloadedFiles = fileManager.getDownloadedFiles("myappidbad", goodCurrent);
            
            Assert.assertEquals(0, downloadedFiles.size());
            
            downloadedFiles = fileManager.getDownloadedFiles("myappid", goodCurrent);
            
            Assert.assertEquals(1, downloadedFiles.size());
            
            Version updateVersion = Version.createVersion("0.0.5.1");
            
            Assert.assertEquals(updateVersion, downloadedFiles.get(0).getUpdateVersion());
            
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
