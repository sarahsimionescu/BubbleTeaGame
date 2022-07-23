package bubbleteagame;

import java.awt.Graphics;
import java.util.ArrayList;

/**
 * The UI class conveniently stores all three types of UI components (Graphic,
 * Text and Button) to be managed and displayed with ease
 *
 * @author Sarah Simionescu
 * @version 1
 */
public class UI {
    /**
     * A list of images to be displayed on the user interface.
     */
    ArrayList<Graphic> graphic = new ArrayList<Graphic>();
    /**
     * A list of buttons to be displayed on the user interface.
     */
    ArrayList<Button> button = new ArrayList<Button>();
    /**
     * A list of texts to be displayed on the user interface.
     */
    ArrayList<Text> text = new ArrayList <Text> ();
    
    /**
     * Paints all the components on to the user interface.
     * @param g Graphics object used to create graphics.
     */
    public void paint(Graphics g)
    {
        for(int i = 0; i < graphic.size(); i++)
        {
            graphic.get(i).paint(g);
        }
        for(int i = 0; i < button.size(); i++)
        {
            button.get(i).paint(g);
        }
        for(int i = 0; i < text.size(); i++)
        {
            text.get(i).paint(g);
        }
    }
    
    /**
     * Deletes all the components currently stored.
     */
    public void clear()
    {
        graphic.clear();
        button.clear();
        text.clear();
    }
    
    /**
     * Finds which graphic the mouse is currently on.
     * @return The index of the graphic the mouse is currently on.
     */
    public Integer checkGraphic()
    {
        for(int i = 0; i < graphic.size(); i++)
        {
            if (graphic.get(i).checkMouse() == true)
            {
                return i;
            }  
        }
        return null;
    }
    
    /**
     * Finds which button the mouse is currently on.
     * @return The index of the button the mouse is currently on.
     */
    public Integer checkButton()
    {   
        for(int i = 0; i < button.size(); i++)
        {
            if (button.get(i).checkMouse() == true)
            {
                return i;  
            }  
        }
        return null;
    }
    
    /**
     * Finds which text the mouse is currently on.
     * @return The index of the text the mouse is currently on.
     */
    public Integer checkText()
    {
        for(int i = 0; i < text.size(); i++)
        {
            if (text.get(i).checkMouse() == true)
            {
                return i;
            }  
        }
        return null;
    }
}