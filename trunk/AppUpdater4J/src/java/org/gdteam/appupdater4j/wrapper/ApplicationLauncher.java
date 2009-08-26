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
        File basedir = this.applicationJar.getParentFile();
        
        URL[] classpathURLs = null;
        
        if (this.classpath == null) {
            classpathURLs = new URL[1];
        } else {
            String[] classpathJars = this.classpath.split(" ");
            classpathURLs = new URL[classpathJars.length + 1];
            
            for (int i = 0; i < classpathJars.length; i++) {
                try {
                    classpathURLs[i] = new File(basedir.getPath() + System.getProperty("file.separator") + classpathJars[i]).toURL();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        }
        
        try {
            classpathURLs[classpathURLs.length - 1] = this.applicationJar.toURL();
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        }
        
        URLClassLoader classLoader = new URLClassLoader(classpathURLs, this.getClass().getClassLoader().getParent());
        
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



    public void setJoinThread(boolean joinThread) {
        this.joinThread = joinThread;
    }
}
