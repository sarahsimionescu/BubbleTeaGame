package bubbleteagame;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 * The Button class stores all the needed information to display and locate a
 * button on the user interface.
 *
 * @author Sarah Simionescu
 * @version 1
 */
public class Button implements UIComponent {
    
    /**
     * Access to user interface.
     */
    protected Display D;
    /**
     * The x position of the button.
     */
    int xpos;
    /**
     * The y position of the button.
     */
    int ypos;
    /**
     * The size of text on the button.
     */
    int size;
    /**
     * The width of the button.
     */
    int width = 0;
    /**
     * The height of the button.
     */
    int height;
    /**
     * The text written on the button.
     */
    String text;
    /**
     * The the color of the button.
     */
    Color buttonColor;
    /**
     * The color of the text on the button.
     */
    Color textColor;
    /**
     * Whether or not you can currently click the button.
     */
    boolean active;

    /**
     * Initializes the button and sets a font color that will contrast with it's
     * background color.
     * @param t The text on the button.
     * @param x The x position of the button.
     * @param y The y position of the button.
     * @param s The size of text on the button.
     * @param c The background color of the button.
     * @param a Whether or not you can currently click on the button.
     * @param d Access to the user interface.
     */
    public Button(String t, int x, int y, int s, Color c, boolean a, Display d) {

        xpos = x;
        ypos = y;
        text = t;
        size = s;
        active = a;
        D = d;

        buttonColor = c;
        if ((buttonColor.getRed() + buttonColor.getGreen() + buttonColor.getBlue()) / 3 > 112) {

            textColor = Color.BLACK;
        } else {
            textColor = Color.white;
        }

    }
    
    /**
     * Paints the button on to the user interface.
     * @param g Graphics object used to create graphics.
     */
    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(1));
        g.setFont(new Font("bold", Font.PLAIN, size));
        D.metrics = g.getFontMetrics(new Font("bold", Font.PLAIN, size));
        width = D.metrics.stringWidth(text) + D.metrics.getAscent();
        height = ((D.metrics.getHeight()) / 2) + D.metrics.getAscent();
        if (checkMouse() == true && active == true) {
            g.setColor(buttonColor.brighter());
        } else if (active == false) {
            g.setColor(new Color(buttonColor.getRed(), buttonColor.getGreen(), buttonColor.getBlue(), 100));
        } else {
            g.setColor(buttonColor);
        }
        g.fillRect(xpos - width / 2, ypos - height / 2, width, height);
        if (active == false) {
            g.setColor(new Color(textColor.getRed(), textColor.getGreen(), textColor.getBlue(), 100));
        } else {
            g.setColor(textColor);
        }
        g.drawRect(xpos - width / 2, ypos - height / 2, width, height);

        g.drawString(text, (xpos - width / 2) + (width - D.metrics.stringWidth(text)) / 2, ypos - height / 2 + ((height - D.metrics.getHeight()) / 2) + D.metrics.getAscent());

    }
    
    /**
     * Checks whether or not the mouse is currently on the button.
     * @return Whether or not the mouse is currently on the button.
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
