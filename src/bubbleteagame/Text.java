package bubbleteagame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

/**
 * The Text class stores all the needed information to display and locate a text
 * on the user interface.
 *
 * @author Sarah Simionescu
 * @version 1
 */
public class Text implements UIComponent {

    /**
     * Access to user interface.
     */
    protected Display D;
    /**
     * The x position of the text.
     */
    int xpos;
    /**
     * The y position of the text.
     */
    int ypos;
    /**
     * The size of the text.
     */
    int size;
    /**
     * The width of the text.
     */
    int width = 0;
    /**
     * The height of the text.
     */
    int height;
    /**
     * What the text says.
     */
    String text;
    /**
     * The background color of the text.
     */
    Color backgroundColor;
    /**
     * The color of the text.
     */
    Color textColor;
    /**
     * Whether or not the text is aligned to the center or to the right.
     */
    boolean align;
    
    /**
     * Initializes the text.
     * @param t What the text says.
     * @param x The x position of the text.
     * @param y The y position of the text.
     * @param s The size of the text.
     * @param c The color of the text.
     * @param bc The background color of the text.
     * @param a Whether or not the text on the tab is aligned to the center or to the right.
     * @param d Accesses the user interface.
     */
    public Text(String t, int x, int y, int s, Color c, Color bc, boolean a, Display d) {

        xpos = x;
        ypos = y;
        text = t;
        size = s;
        textColor = c;
        backgroundColor = bc;
        D = d;
        align = a;
    }
    
    /**
     * Paints the text on to the user interface.
     * @param g Graphics object used to create graphics.
     */
    @Override
    public void paint(Graphics g) {
        g.setFont(new Font("bold", Font.PLAIN, size));
        D.metrics = g.getFontMetrics(new Font("bold", Font.PLAIN, size));
        width = D.metrics.stringWidth(text) + D.metrics.getAscent();
        height = ((D.metrics.getHeight()) / 2) + D.metrics.getAscent();
        g.setColor(backgroundColor);
        if (align == false) {
            g.fillRoundRect(xpos - width / 2, ypos - height / 2, width, height, 20, 20);
        } else {
            g.fillRoundRect(xpos - 10, ypos - height / 2, width, height, 20, 20);
        }
        width = D.metrics.stringWidth(text);
        g.setColor(textColor);
        if (align == false) {
            g.drawString(text, (xpos - width / 2) + (width - D.metrics.stringWidth(text)) / 2, ypos - height / 2 + ((height - D.metrics.getHeight()) / 2) + D.metrics.getAscent());
        } else {
            g.drawString(text, (xpos) + (width - D.metrics.stringWidth(text)) / 2, ypos - height / 2 + ((height - D.metrics.getHeight()) / 2) + D.metrics.getAscent());
        }
    }
    
    /**
     * Checks whether or not the mouse is currently on the text.
     * @return Whether or not the mouse is currently on the text.
     */
    @Override
    public boolean checkMouse() {
        if (D.mouse.x < xpos + width / 2 && D.mouse.x > xpos - width / 2 && D.mouse.y < ypos + height / 2 && D.mouse.y > ypos - height / 2) {
            return true;
        } else {
            return false;
        }
    }
    

}
