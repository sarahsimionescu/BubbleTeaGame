package bubbleteagame;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * The Keyboard class is responsible for responding to user input from the
 * keyboard.
 *
 * @author Sarah Simionescu
 * @version 1
 */
public class Keyboard extends KeyAdapter {

    /**
     * Access to long term memory.
     */
    protected Game G;
    /**
     * Access to user interface.
     */
    protected Display D;
    
    /**
     * Initiates the Keyboard component.
     * @param d Accesses the user interface.
     * @param g Accesses long term memory.
     */
    public Keyboard(Display d, Game g) {
        G = g;
        D = d;
    }

    /**
     * Responds to a key being pressed in order for the user to input their name
     * and the name of the shop.
     *
     * @param e A key event for a particular key being pressed.
     */
    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if(G.gameMode == 2)
        {
            G.shopName = textBoxType(keyCode, G.shopName);
        }else if(G.gameMode ==3)
        {
             G.userName = textBoxType(keyCode, G.userName);
        }
        D.mouse.runGameMode(G.gameMode);
    }

    /**
     * Adds or deletes a character from the string based on the user's input for
     * the selected text box.
     *
     * @param keyCode The ASCII code for the key pressed.
     * @param str The string in the text box.
     * 
     * @return The string altered by the user's input.
     */
    public String textBoxType(int keyCode, String str) {
        if (keyCode >= 65 && keyCode <= 90 && str.length() < 15 || keyCode == 32 && str.length() < 15) {
                str += Character.toString((char) keyCode);
            } else if (keyCode == 8) {
                str = deleteOne(str);
            }
        
        return str;
    }
    
    /**
     * Deletes the last character in the given string.
     *
     * @param str The string in the text box.
     * @return The string with the last character removed.
     */
    public String deleteOne(String str) {
        if (str != null && str.length() > 0) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }



    @Override
    public void keyReleased(KeyEvent e) {
    }
}
