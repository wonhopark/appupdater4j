package org.gdteam.appupdater4j.wrapper;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.gdteam.appupdater4j.FileUtil;

public class ApplicationLauncher {
	
    private String mainClass;
    private String manifestClasspath;
    
    private File applicationJar;
    
    private String[] args;
    private boolean joinThread = false;;

    public ApplicationLauncher(File applicationJar, String[] args) {
        this.applicationJar = applicationJar;
        this.args = args;
    }
    
    public void extractManifestInfoAndListFiles() throws Exception {
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
                        manifestClasspath = manifest.getMainAttributes().getValue("Class-Path");
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
        
        List<URL> classpathURLs = new ArrayList<URL>();
        
        if (this.manifestClasspath != null) {
        	String[] classpathJars = this.manifestClasspath.split(" ");
            
            for (int i = 0; i < classpathJars.length; i++) {
                try {
                	classpathURLs.add(new File(basedir.getPath() + System.getProperty("file.separator") + classpathJars[i]).toURL());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        }
        
        //Extracts files of application jar to add in classpath
        File applicationJarDir = new File(System.getProperty("java.io.tmpdir") + "/gdteam-cp/");
        
        //Clean directory
        FileUtil.deleteFile(applicationJarDir);
        applicationJarDir.mkdirs();
        
        try {
			FileUtil.unzipFile(applicationJar, applicationJarDir.getAbsolutePath() + "/");
			new File(applicationJarDir, "META-INF/MANIFEST.MF").delete();
	        
	        classpathURLs.add(applicationJarDir.toURL());
		} catch (Exception e1) {
			e1.printStackTrace();
		}
        
        URLClassLoader classLoader = new URLClassLoader(classpathURLs.toArray(new URL[0]), this.getClass().getClassLoader().getParent());
        
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
            return new File(splitted[splitted.length - 2]);
        }
        
        return null;
    }

    public void setJoinThread(boolean joinThread) {
        this.joinThread = joinThread;
    }
}
