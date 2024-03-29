package org.gdteam.appupdater4j;


public interface UpdateController extends UpdateListener {

    /**
     * Add listener
     * @param listener
     */
    public void addUpdateControllerListener(UpdateControllerListener listener);
    
    /**
     * Remove listener
     * @param listener
     */
    public void removeUpdateControllerListener(UpdateControllerListener listener);
    
    /**
     * Displays controller
     * @param
     */
    public void displayController(String title);
    
    /**
     * Dispose controller
     */
    public void disposeController();
    
}
