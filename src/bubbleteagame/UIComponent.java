package bubbleteagame;

import java.awt.Graphics;

/**
 * The UIComponent interface stores every function that an object classified as
 * a UI component must preform in order to be painted on the display and
 * detected by the mouse.
 *
 * @author Sarah Simionescu
 * @version 1
 */
public interface UIComponent {

    /**
     * Paints the UI component on to the user interface.
     * @param g Graphics object used to create graphics.
     */
    public void paint(Graphics g);

    /**
     * Checks whether or not the mouse is currently on the UI component.
     * @return Whether or not the mouse is current on the UI component
     */
    public boolean checkMouse();

}
