package org.gdteam.appupdater4j;

import org.gdteam.appupdater4j.ui.dialog.UpdateDialog;

public class UpdateControllerFactory {

    /**
     * Get new instance of update controller
     * @return
     */
    public static UpdateController getUpdateController(String className){
        
        if (className == null) {
            className = UpdateDialog.class.getName();
        }
        
        try {
            return (UpdateController) Class.forName(className).newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            
            return null;
        }
    }
}
