package org.gdteam.appupdater4j;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class DefaultUpdateController implements UpdateController {

    private JTextArea logTextArea;
    private JScrollPane scrollPane;
    private JFrame dialog;
    
    public DefaultUpdateController() {
        
        dialog = new JFrame("Update management");
        dialog.setPreferredSize(new Dimension(600, 400));
        
        logTextArea = new JTextArea();
        scrollPane = new JScrollPane(logTextArea);
        
        dialog.getContentPane().add(scrollPane);
        
        dialog.pack();
        dialog.setLocationRelativeTo(null);
    }
    
    public void displayController() {
        this.dialog.setVisible(true);
    }

    public void installationEnded() { 
        this.logTextArea.append("\nInstallation done");
    }

    public void installationFailed(Exception e) {
        this.logTextArea.append("\nInstallation failed : " + e.getMessage());
    }

    public void installationStarted(String basedir) {
        this.logTextArea.append("\nInstallation started (" + basedir + ")");
    }

    public void downloadDone() {
        this.logTextArea.append("\nDownload done");
    }

    public void downloadedDataChanged(long size) {
        this.logTextArea.append("\nDownload : " + size + " bytes");
    }

    public void downloadFailed() {
        this.logTextArea.append("\nDownload failed");
    }

    public void downloadStarted(long size) {
        this.logTextArea.append("\nDownload started (file size : " + size + ")");
    }

    public void flowSizeChanged(long size) {
        this.logTextArea.append("\nDownload rate : " + size + " bytes/ms");
    }
    
    
}
