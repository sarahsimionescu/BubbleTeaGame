package bubbleteagame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;


/**
 * The TextBox class stores all the needed information to display and locate a
 * text box on the user interface.
 *
 * @author Sarah Simionescu
 * @version 1
 */
public class TextBox extends Button {

    /**
     * Initializes the text box and sets a font color that will contrast with
     * it's background color.
     *
     * @param t The text in the text box.
     * @param x The x position of the text box.
     * @param y The y position of the text box.
     * @param s The size of text on the text box.
     * @param c The background color of the text box.
     * @param a Whether or not you can currently click on the text box.
     * @param d Access to the user interface.
     */
    public TextBox(String t, int x, int y, int s, Color c, boolean a, Display d) {
        super(t, x, y, s, c, a, d);
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
     * Paints the text box on to the user interface.
     * @param g Graphics object used to create graphics.
     */
    @Override
    public void paint(Graphics g) {
        g.setFont(new Font("bold", Font.PLAIN, size));
        D.metrics = g.getFontMetrics(new Font("bold", Font.PLAIN, size));
        width = 600;
        height = ((D.metrics.getHeight()) / 2) + D.metrics.getAscent();

        g.setColor(buttonColor);

        g.fillRect(xpos - width / 2, ypos - height / 2, width, height);

        g.setColor(textColor);

        g.drawRect(xpos - width / 2, ypos - height / 2, width, height);
        g.drawString(text, xpos - width / 2 + size, ypos - height / 2 + ((height - D.metrics.getHeight()) / 2) + D.metrics.getAscent());
        if (active) {
            D.displayScreen.paintTextBlinker(g, xpos - width / 2 + size + D.metrics.stringWidth(text), ypos - height / 2 + ((height - D.metrics.getHeight())), 1, size);
        }
    }
    
}
