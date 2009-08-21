package org.gdteam.appupdater4j;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.gdteam.appupdater4j.model.ApplicationVersion;

public class DefaultUpdateController implements UpdateController {

    private JTextArea logTextArea;
    private JScrollPane scrollPane;
    private JFrame dialog;
    private List<ApplicationVersion> versionList;
    private JButton updateButton;
    
    private List<UpdateControllerListener> listenerList = new ArrayList<UpdateControllerListener>();
    
    public DefaultUpdateController() {
        
        dialog = new JFrame("Update management");
        dialog.setPreferredSize(new Dimension(800, 600));
        
        logTextArea = new JTextArea();
        logTextArea.setEditable(false);
        scrollPane = new JScrollPane(logTextArea);
        
        dialog.getContentPane().add(scrollPane, BorderLayout.CENTER);
        
        this.updateButton = new JButton("Update");
        this.updateButton.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent e) {
                updateButton.setEnabled(false);
                for (UpdateControllerListener listener : listenerList) {
                    listener.startUpdate(DefaultUpdateController.this, versionList);
                }
            }
            
        });
        
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
        buttonPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        buttonPane.add(Box.createHorizontalGlue());
        buttonPane.add(updateButton);
        buttonPane.add(Box.createHorizontalGlue());
        
        dialog.getContentPane().add(buttonPane, BorderLayout.PAGE_END);
        
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
        this.logTextArea.append(".");
    }

    public void downloadFailed() {
        this.logTextArea.append("\nDownload failed");
    }

    public void downloadStarted(long size) {
        this.logTextArea.append("\nDownload started (file size : " + size + ") ");
    }

    public void flowSizeChanged(long size) {
        //Do nothing
    }

    public void setVersionToInstall(List<ApplicationVersion> versionList) {
        this.versionList = versionList;
        
        for (ApplicationVersion applicationVersion : versionList) {
            this.logTextArea.append("\n Need to install version : " + applicationVersion.toString() + "\n    - " + applicationVersion.getUpdateURL());
        }
        this.logTextArea.append("\n");
        
    }

    public void addUpdateControllerListener(UpdateControllerListener listener) {
        this.listenerList.add(listener);
    }

    public void removeUpdateControllerListener(UpdateControllerListener listener) {
        this.listenerList.remove(listener);
    }
    
    
}
