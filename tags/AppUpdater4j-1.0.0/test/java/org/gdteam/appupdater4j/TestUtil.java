package org.gdteam.appupdater4j;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class TestUtil {

    public static File extractInJarFile(String name, boolean keepName) {
        
        String fileName = name;
        
        if (!keepName) {
            fileName = System.currentTimeMillis() + "_" + name;
        }
        
        File ret = new File(System.getProperty("java.io.tmpdir"), fileName);
        ret.deleteOnExit();
        
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(ret);
            FileUtil.copy(TestUtil.class.getClassLoader().getResourceAsStream(name), out, 1024);
            
            return ret;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
        }
    }
    
    public static File extractInJarFile(String name) {
        return TestUtil.extractInJarFile(name, false);
    }
}
