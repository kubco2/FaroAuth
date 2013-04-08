package sk.shifty;


/**
 * User: shifty
 * Date: 4/7/13
 * Time: 1:59 PM
 */
public interface ControlableGUI {

    public void setStatus(boolean isOnline);
    public void setFreeLimit(Double free);
    public void showNotAuthentizedError();
    public void setActivateButton(boolean start);

}
