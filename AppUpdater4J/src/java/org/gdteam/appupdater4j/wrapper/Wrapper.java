package org.gdteam.appupdater4j.wrapper;


public class Wrapper extends Thread {

    public Wrapper(ClassLoader wrappedClassLoader, String mainClass, String[] argvs){
        super(new WrappedRunnable(mainClass, argvs));
        this.setContextClassLoader(wrappedClassLoader);
    }
    
    
}
