package bubbleteagame;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;

/**
 * The Graphic class stores all the needed information to display and locate a
 * .png file on the user interface.
 *
 * @author Sarah Simionescu
 * @version 1
 */
public class Graphic implements UIComponent {

    /**
     * Access to user interface.
     */
    protected Display D;
    /**
     * The name of the .png file.
     */
    String fileName;
    /**
     * The x position of the graphic.
     */
    int xpos;
    /**
     * The y position of the graphic.
     */
    int ypos;
    /**
     * The width of the graphic.
     */
    int width;
    /**
     * The height of the graphic.
     */
    int height;
    /**
     * The object used to initialize and resize the graphic.
     */
    ImageIcon myIcon;
    /**
     * The object where the graphic is stored.
     */
    Image image;

    /**
     * Initializes and, if desired, resizes the graphic.
     *
     * @param x The x position of the graphic.
     * @param y The y position of the graphic.
     * @param w The new width of the graphic.
     * @param h The new height of the graphic.
     * @param f The name of the .png file.
     * @param d Accesses the user interface.
     */
    public Graphic(int x, int y, Integer w, Integer h, String f, Display d) {
        myIcon = new ImageIcon("PNG/" + f + ".png");
        image = myIcon.getImage();
        width = myIcon.getIconWidth();
        height = myIcon.getIconHeight();
        xpos = x - width / 2;
        ypos = y - height / 2;
        D = d;
        if(w != null && h != null)
        {
            resize(w, h);
        }
    }
    
    /**
     * Resizes the graphic and re-centers the image to its position.
     * @param sw New width of graphic.
     * @param sh New height of graphic.
     */
    public void resize(int sw, int sh) {
        myIcon = new ImageIcon(getScaledImage(myIcon.getImage(), sw, sh));
        image = myIcon.getImage();
        xpos += width / 2;
        ypos += height / 2;
        width = myIcon.getIconWidth();
        height = myIcon.getIconHeight();
        xpos -= width / 2;
        ypos -= height / 2;
    }

    /**
     * Resizes an image.
     *
     * @param srcImg Image to be resized.
     * @param w New width of image.
     * @param h New height of image.
     * @return Returns the resized image.
     */
    private Image getScaledImage(Image srcImg, int w, int h) {
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();

        return resizedImg;
    }
    
    /**
     * Paints the graphic on to the user interface.
     * @param g Graphics object used to create graphics.
     */
    @Override
    public void paint(Graphics g) {
        g.drawImage(image, xpos, ypos, D.displayScreen);
    }
    
    /**
     * Checks whether or not the mouse is currently on the graphic.
     * @return Whether or not the mouse is currently on the graphic.
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
