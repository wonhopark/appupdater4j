package org.gdteam.appupdater4j;

public class UpdateControllerFactory {

    /**
     * Get new instance of update controller
     * @return
     */
    public static UpdateController getUpdateController(){
        return new DefaultUpdateController();
    }
}
