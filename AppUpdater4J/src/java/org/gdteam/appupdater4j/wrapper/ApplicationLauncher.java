package org.gdteam.appupdater4j.wrapper;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ApplicationLauncher {
    
    private String mainClass;
    private String classpath;
    private File applicationJar;
    private String[] args;
    private boolean joinThread = false;;

    public ApplicationLauncher(File applicationJar, String[] args) {
        this.applicationJar = applicationJar;
        this.args = args;
    }
    
    public void extractManifestInfo() throws Exception {
        ZipFile zipFile = null;
        try {
            zipFile = new ZipFile(this.applicationJar);
            Enumeration<ZipEntry> entries = (Enumeration<ZipEntry>) zipFile.entries();
            
            while (entries.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                if (!entry.isDirectory()) {
                    if (entry.getName().equalsIgnoreCase("meta-inf/manifest.mf")) {
                        Manifest manifest = new Manifest(zipFile.getInputStream(entry));
                        mainClass = manifest.getMainAttributes().getValue("Main-Class");
                        classpath = manifest.getMainAttributes().getValue("Class-Path");
                    }
                }
            }
        } finally {
            if (zipFile != null) {
                try {
                    zipFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            
            if (mainClass == null) {
                throw new Exception("Cannot find Main-Class in jar file : " + this.applicationJar);
            }
        }
    }
    
    public void run() throws IllegalAccessException {
        if (this.mainClass == null) {
            throw new IllegalAccessException("Perform manifest extraction before calling run");
        }
        
        //Create classloader
        //Do not use getParentFile due a NPE bugs
        File basedir = this.getParentFile(this.applicationJar);
        
        URL[] classpathURLs = new URL[1];
        
        try {
			classpathURLs[classpathURLs.length - 1] = this.applicationJar.toURL();
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}        
        
        URLClassLoader classLoader = new URLClassLoader(classpathURLs, null);

        Wrapper wrapper = new Wrapper(classLoader, this.mainClass, this.args);
        wrapper.start();
        
        if (this.joinThread) {
            try {
                wrapper.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
    }

    /**
     * Retrieve parent file
     * @param child
     * @return parent file
     */
    private File getParentFile(File child) {
        if (child == null) {
            return null;
        }
        
        File ret = child.getParentFile();
        
        if (ret != null) {
            return ret;
        }
        
        String[] splitted = child.getAbsolutePath().replace(System.getProperty("file.separator"), "/").split("/");
        
        if (splitted.length > 1) {
        	
        	StringBuilder absolutePath = new StringBuilder();
        	
        	for(int i=0;i<splitted.length - 1;i++) {
        		absolutePath.append(splitted[i]).append("/");
        	}
        	
            return new File(absolutePath.toString());
        }
        
        return null;
    }

    public void setJoinThread(boolean joinThread) {
        this.joinThread = joinThread;
    }
}
