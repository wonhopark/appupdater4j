package org.gdteam.appupdater4j.demo.server;

import java.io.IOException;
import java.net.ServerSocket;

import javax.swing.JOptionPane;

import org.restlet.Component;
import org.restlet.data.Protocol;

public class Main {
    
    public static final int PORT = 8729;

    /**
     * @param args
     */
    public static void main(String[] args) {
        
        //Check for port availability
        if (!Main.isPortAvailable(PORT)) {
            JOptionPane.showMessageDialog(null, "Cannot start update feed server. Port " + PORT + " is not available", "Cannot start update feed server", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
        
        //Start feed update
        try {
            Component component = new Component();
            component.getServers().add(Protocol.HTTP, PORT);
            component.getDefaultHost().attach(new DemoApplication());
            component.start();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Cannot start update feed server : " + e.getMessage(), "Cannot start update feed server", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        
        
    }
    
    
    
    /**
     * Return true if specified port is available
     * @param port
     * @return true if specified port is available
     */
    private static boolean isPortAvailable(int port) {
        ServerSocket srv = null;
        try {
            srv = new ServerSocket(port);
            return true;
        } catch (IOException e) {
            return false;
        } finally {
            if (srv != null) {
                try {
                    srv.close();
                } catch (IOException e) {
                    //Do nothing
                }
                srv = null;
            }
        }
    }

}
