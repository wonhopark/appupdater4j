package org.gdteam.appupdater4j.install;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Properties;

public class AntHelper {

    public static void invokeANT(File baseDir, File buildFile, String target, Properties properties) throws Exception {
        try {
            Class projectClass = Thread.currentThread().getContextClassLoader().loadClass("org.apache.tools.ant.Project");
            Object projectInstance = projectClass.newInstance();
            
            Method initMethod = projectClass.getMethod("init", new Class[0]);
            initMethod.invoke(projectInstance, new Object[0]);
            
            Class[] setBaseDirParams = {File.class};
            Object[] setBaseDirArgs = {baseDir};
            Method setBaseDirMethod = projectClass.getMethod("setBaseDir", setBaseDirParams);
            setBaseDirMethod.invoke(projectInstance, setBaseDirArgs);
            
            Class projectHelperClass = Thread.currentThread().getContextClassLoader().loadClass("org.apache.tools.ant.ProjectHelper");
            Object projectHelperInstance = projectHelperClass.newInstance();
            
            Method[] projectHelperClassMethods = projectHelperClass.getMethods();
            
            for (Method method : projectHelperClassMethods) {
                if (method.getName().equals("configureProject")){
                    Object[] args = {projectInstance, buildFile};
                    method.invoke(projectHelperInstance, args);
                    break;
                }
            }
            
            //TODO: set properties
            //project.setProperty(name, properties.getProperty(name));
            Iterator<Object> propIt = properties.keySet().iterator();
            
            Class[] setPropertyParams = {String.class, String.class};
            Method setPropertyMethod = projectClass.getMethod("setProperty", setPropertyParams);
            
            while (propIt.hasNext()) {
                String key = (String) propIt.next();
                String value = properties.getProperty(key);
                
                Object[] setPropertyArgs = {key, value};
                
                setPropertyMethod.invoke(projectInstance, setPropertyArgs);
            }
            
            Class[] executeTargetParams = {String.class};
            Object[] executeTargetArgs = {target};
            Method executeTargetMethod = projectClass.getMethod("executeTarget", executeTargetParams);
            
            executeTargetMethod.invoke(projectInstance, executeTargetArgs);
            
        } catch (InvocationTargetException e) {
            // Build exception is in e.getCause
            throw new Exception(e.getCause());
        }       
    }
}
