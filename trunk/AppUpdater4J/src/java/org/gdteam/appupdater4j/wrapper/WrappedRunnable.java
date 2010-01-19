package org.gdteam.appupdater4j.wrapper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

import org.gdteam.appupdater4j.os.macosx.ReflectiveApplication;

public class WrappedRunnable implements Runnable {

    private String mainClass;
    private String[] argvs;

    public WrappedRunnable(String mainClass, String[] argvs) {
        this.mainClass = mainClass;
        this.argvs = argvs;
    }

    public void run() {

        ClassLoader wrappedClassLoader = Thread.currentThread().getContextClassLoader();

        try {
        	
        	//Load reflective application to avoid quit event problems
        	try {
        		this.loadReflectiveApplication(wrappedClassLoader);
        	} catch (Exception e) {
				e.printStackTrace();
			}
        	
            Class wrappedClass = wrappedClassLoader.loadClass(mainClass);
            Method main = null;

            Method[] allMethods = wrappedClass.getMethods();
            for (Method method : allMethods) {
                if (method.getName().equals("main")) {
                    main = method;
                }
            }

            Object[] newargs = new Object[] { argvs };

            main.invoke(null, newargs);
        } catch (ClassNotFoundException e) {
            StringBuilder userMessage = new StringBuilder("Cannot find ").append(mainClass).append(" in :\n");
            URLClassLoader urlClassLoader = (URLClassLoader) wrappedClassLoader;

            for (URL url : urlClassLoader.getURLs()) {
                userMessage.append("  - ").append(url.toString()).append("\n");
            }

            System.out.println(userMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Load ReflectiveApplication in wrapped class loader
     * @param wrappedClassLoader
     * @throws Exception
     */
    private void loadReflectiveApplication(ClassLoader wrappedClassLoader) throws Exception {
    	Class reflectiveApplicationClass = wrappedClassLoader.loadClass(ReflectiveApplication.class.getName());
    	Method getApplication = null;
    	Method[] allMethods = reflectiveApplicationClass.getMethods();
        for (Method method : allMethods) {
            if (method.getName().equals("getApplication")) {
            	getApplication = method;
            }
        }
        
        getApplication.invoke(null, new Object[0]);
    }

}
