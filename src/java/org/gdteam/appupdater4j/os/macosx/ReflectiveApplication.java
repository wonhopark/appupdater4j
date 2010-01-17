package org.gdteam.appupdater4j.os.macosx;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collection;

public class ReflectiveApplication {

    public static final String HANDLE_RE_OPEN_APPLICATION = "handleReOpenApplication";
    public static final String HANDLE_QUIT = "handleQuit";
    
    private static final ReflectiveApplication instance = new ReflectiveApplication();

    private Collection<ActionListener> applicationListeners = new ArrayList<ActionListener>();
    
    private boolean acceptQuitRequest = true;
    
    private ReflectiveApplication() {
        try {
            InvocationHandler invocationHandler = new ApplicationListenerHandler();
            
            Class appleApplicationListenerClass = Class.forName("com.apple.eawt.ApplicationListener"); 
            
            Object appleApplicationListener = Proxy.newProxyInstance(ReflectiveApplication.class.getClassLoader(), new Class[]{appleApplicationListenerClass}, invocationHandler);
            
            Class appleApplicationClass = Class.forName("com.apple.eawt.Application");
            Method method = appleApplicationClass.getMethod("addApplicationListener", new Class[]{appleApplicationListenerClass});
            
            method.invoke(this.getAppleApplicationInstance(), new Object[]{appleApplicationListener});
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static ReflectiveApplication getApplication() {
        return instance;
    }
    
    public Image getDockIconImage() {
        try {
            Class appleApplicationClass = Class.forName("com.apple.eawt.Application");
            Method method = appleApplicationClass.getMethod("getDockIconImage", new Class[0]);
            
            return (Image) method.invoke(this.getAppleApplicationInstance(), new Object[0]);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    public void setDockIconImage(Image image) {
        
        try {
            Class appleApplicationClass = Class.forName("com.apple.eawt.Application");
            Class[] paramTypes = {Image.class};
            Method method = appleApplicationClass.getMethod("setDockIconImage", paramTypes);
            
            Object[] args = {image};
            
            method.invoke(this.getAppleApplicationInstance(), args);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void addApplicationListener(ActionListener listener) {
        this.applicationListeners.add(listener);
    }
    
    public void removeApplicationListener(ActionListener listener) {
        this.applicationListeners.remove(listener);
    }
        
    private Object getAppleApplicationInstance() throws Exception {
        Class appleApplicationClass = Class.forName("com.apple.eawt.Application");
        Method method = appleApplicationClass.getMethod("getApplication", new Class[0]);
        
        return method.invoke(null, new Object[0]);
    }
    
    private class ApplicationListenerHandler implements InvocationHandler {

        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            
            if ("handleReOpenApplication".equals(method.getName())) {
                ActionEvent evt = new ActionEvent(ReflectiveApplication.this, Long.valueOf(System.currentTimeMillis()).intValue(), HANDLE_RE_OPEN_APPLICATION);
                for (ActionListener listener : applicationListeners) {
                    listener.actionPerformed(evt);
                }
            } else if ("handleQuit".equals(method.getName())) {
                Object applicationEvent = args[0];
                
                Class applicationEventClass = Class.forName("com.apple.eawt.ApplicationEvent");
                Method setHandled = applicationEventClass.getMethod("setHandled", new Class[]{Boolean.TYPE});
                
                setHandled.invoke(applicationEvent, new Object[]{isAcceptQuitRequest()});
                
                ActionEvent evt = new ActionEvent(ReflectiveApplication.this, Long.valueOf(System.currentTimeMillis()).intValue(), HANDLE_QUIT);
                for (ActionListener listener : applicationListeners) {
                    listener.actionPerformed(evt);
                }
                
            }
            
            return proxy;
            
        }
        
    }

    /**
     * Return true if ReflectiveApplication accepts the request to quit
     * @return true if ReflectiveApplication accepts the request to quit
     */
	public boolean isAcceptQuitRequest() {
		return acceptQuitRequest;
	}

	/**
	 * Set to true if ReflectiveApplication accepts the request to quit
	 * @param acceptQuitRequest true if ReflectiveApplication accepts the request to quit
	 */
	public void setAcceptQuitRequest(boolean acceptQuitRequest) {
		this.acceptQuitRequest = acceptQuitRequest;
	}
   
}