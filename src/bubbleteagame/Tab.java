package bubbleteagame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

/**
 * The Tab class stores all the needed information to display and locate a
 * tab on the user interface.
 *
 * @author Sarah Simionescu
 * @version 1
 */
public class Tab extends Text {
    
    /**
     * Initializes the tab.
     * @param t The text on the tab.
     * @param x The x position of the tab.
     * @param y The y position of the tab.
     * @param s The size of the text.
     * @param c The color of the text on the tab.
     * @param bc The color of the tab.
     * @param a Whether or not the text on the tab is aligned to the center or to the right.
     * @param d Accesses the user interface.
     */
    public Tab(String t, int x, int y, int s, Color c, Color bc, boolean a, Display d) {
        super(t, x, y, s, c, bc, a, d);
        xpos = x;
        ypos = y;
        text = t;
        size = s;
        textColor = c;
        backgroundColor = bc;
        D = d;
    }
    
    /**
     * Paints the tab on to the user interface.
     * @param g Graphics object used to create graphics.
     */
    @Override
    public void paint(Graphics g) {
        g.setFont(new Font("bold", Font.PLAIN, size));
        D.metrics = g.getFontMetrics(new Font("bold", Font.PLAIN, size));
        g.setColor(backgroundColor);
        g.fillRoundRect(xpos - D.displayScreen.getWidth() / 10, ypos - 45/2, D.displayScreen.getWidth() / 5, 45, 30, 20);
        width = D.metrics.stringWidth(text);
        g.setColor(textColor);
        g.drawString(text, (xpos - width / 2) + (width - D.metrics.stringWidth(text)) / 2, ypos);
    }
    
    /**
     * Checks whether or not the mouse is currently on the tab.
     * @return Whether or not the mouse is currently on the tab.
     */
    @Override
    public boolean checkMouse() {
        if (D.mouse.x < xpos + D.displayScreen.getWidth() / 5 / 2 && D.mouse.x > xpos - D.displayScreen.getWidth() / 5 / 2 && D.mouse.y < ypos + 45 / 2 && D.mouse.y > ypos - 45 / 2) {
            return true;
        } else {
            return false;
        }
    }
   

    
    
}
