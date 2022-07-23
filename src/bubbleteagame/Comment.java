package bubbleteagame;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Path2D;

/**
 * The Comment class stores all the needed information to display and locate a
 * comment on the user interface.
 *
 * @author Sarah Simionescu
 * @version 1
 */
public class Comment implements UIComponent{
    
    /**
     * Access to user interface.
     */
    Display D;
    /**
     * Accesses MathTool class to use mathematical functions.
     */
    MathTool math;
    /**
     * The name of the customer writing the comment.
     */
    Text name;
    /**
     * What the customer has said about the shop.
     */
    Text comment;
    /**
     * The rating out of 5 stars the customer has given with regard to their
     * experience at the shop.
     */
    double rating;
    /**
     * The x position of the comment.
     */
    int xpos;
    /**
     * The y position of the comment.
     */
    int ypos;
    /**
     * The width of the comment.
     */
    int width;
    /**
     * The height of the comment.
     */
    int height;
    
    /**
     * Initializes the comment.
     * @param c The text on the comment.
     * @param r The rating given by the customer.
     * @param d Access to the user interface.
     */
    public Comment(String c, Double r, Display d) {
        D = d;
        math = new MathTool();
        xpos = 0;
        ypos = 0;
        name = new Text(name(), xpos, ypos, 20, Color.black, new Color(0, 0, 0, 0), true, D);
        comment = new Text(c, xpos, ypos, 15, Color.black, new Color(0, 0, 0, 0), true, D);
        if (r == null) {
            rating = 0;
        } else {
            rating = r / 20;
        }
        height = 70;
        width = D.displayScreen.getWidth() / 12 * 5;

    }
    
    /**
     * Paints the comment on to the user interface.
     * @param g Graphics object used to create graphics.
     */
    @Override
    public void paint(Graphics g) {
        name.ypos = ypos - 30;
        comment.xpos = xpos;
        name.xpos = xpos;
        if (rating == 0) {
            comment.ypos = ypos-10;
        } else {
            comment.ypos = ypos + 10;
        }
        paintStars(g, xpos + 10, ypos - 10, rating);
        name.paint(g);
        comment.paint(g);
        g.setColor(new Color(210, 210,210));
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(1));
        g.drawRect(xpos, ypos+25, 400, 1);
    }

    /**
     * Paints customer's out of five star rating.
     *
     * @param g Graphics object used to create graphics.
     * @param x The x position of the first star.
     * @param y The y position of the first star.
     * @param r The rating given by the customer.
     */
    public void paintStars(Graphics g, int x, int y, double r) {
        Graphics2D g2 = (Graphics2D) g;
        for (int i = 0; i < r; i++) {
            g.setColor(Color.black);
            g2.fill(createStar(x + i * 20, y, 5, 11, 5, Math.toRadians(-18)));
            if (i == (int) r && r - (int) r >= 0.5) {
                g.setColor(Color.white);
                g.fillRect(x + i * 20, y - 10, 10, 20);
            }
        }
    }

    /**
     * The shape of a star to be painted an represent a Customer's rating on the
     * user interface.
     *
     * @param centerX The x position of the center of the star.
     * @param centerY The y position of the center of the star.
     * @param innerRadius The length of the inner radius of the star (circular
     * center piece).
     * @param outerRadius The length of the outer radius of the star (length of
     * star's rays).
     * @param numRays The number of rays the star has.
     * @param startAngleRad The initial angle for the first ray given in
     * radians.
     * @return The shape of a star.
     */
    public Shape createStar(double centerX, double centerY, double innerRadius, double outerRadius, int numRays, double startAngleRad) {
        Path2D path = new Path2D.Double();
        double deltaAngleRad = Math.PI / numRays;
        for (int i = 0; i < numRays * 2; i++) {
            double angleRad = startAngleRad + i * deltaAngleRad;
            double ca = Math.cos(angleRad);
                double sa = Math.sin(angleRad);
                double relX = ca;
                double relY = sa;
                if ((i & 1) == 0) {
                    relX *= outerRadius;
                    relY *= outerRadius;
                } else {
                    relX *= innerRadius;
                    relY *= innerRadius;
                }
                if (i == 0) {
                    path.moveTo(centerX + relX, centerY + relY);
                } else {
                    path.lineTo(centerX + relX, centerY + relY);
                }
            }
            path.closePath();
            return path;
        }
    
    /**
     * Checks whether or not the mouse is currently on the comment.
     * @return Whether or not the mouse is currently on the comment.
     */
    @Override
    public boolean checkMouse() {

        if (D.mouse.x < xpos + width && D.mouse.x > xpos && D.mouse.y < ypos + height && D.mouse.y > ypos) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Generates a random name for the customer.
     * @return A randomly generate name.
     */
    public String name() {
        String name = "";
        String[] options = new String[0];
        options = new String[]{"Nicole", "Vlad", "Alice", "Jane", "Olivia", "Sarah", "Stephanie", "Cristina", "Gilda", "Maria", "Olivia", "Emma", "Ava", "Sophia", "Isabella", "Charlotte", "Amelia", "Mia", "Harper", "Evelyn", "Abby", "Emily", "Ava", "Bob", "Nikos", "Liam", "Noah", "Oliver", "Elijah", "William", "James", "Ben", "Lucas", "Henry", "Alexander", "Mason", "Micheal", "Ethan", "Daniel", "Jacob", "Logan", "Jackson", "Levi", "Sebastian", "Mateo", "Jack", "Owen", "Theodore", "Aiden", "Samuel", "Joseph", "John", "David", "Wyatt", "Jamie", "Julian", "Karen", "Jacob", "Josh", "Nick", "Dave", "Justin", "Nathan", "Austin", "Ayden","Connor", "Evan", "Kyle", "Cole", "Ian", "Owen", "Blake", "Seth", "Diego", "Alex", "Ashley", "Grace", "Alyssa", "Lauren", "Samantha", "Kayla", "Jessica", "Taylor", "Victoria", "Vicky", "Natalie", "Chloe", "Sydney", "Hailey", "Jasmine", "Rachel", "Morgan", "Megan", "Jennifer", "Ella", "Brooke", "Allison", "Paige", "Jenna", "Mary", "Andrea", "Michelle", "Rebecca", "Kimberly", "Jada", "Erin", "Madara", "Saske", "Sakura", "Naruto", "Godzilla", "Mom", "Goku", "Mikasa", "Donald", "Mickey", "Gon", "Killua", "Saitama", "Kakashi", "Hisoka", "Midoriya", "Reigen", "Riley", "Chidi"};
        name += options[(int) math.random(0, options.length - 1)] + " ";
        name += (char)(int)math.random(65, 70);
        name += ".";
        return name;
    }
}
