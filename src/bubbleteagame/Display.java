package bubbleteagame;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.awt.geom.RoundRectangle2D;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import java.util.ArrayList;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;

/**
 * The Display class manages the user interface; It stores objects and short
 * term variables related to creating, updating and displaying the user
 * interface.
 *
 * @author Sarah Simionescu
 * @version 1
 */
public class Display extends Thread {

    /**
     * Access to long term memory.
     */
    protected Game G;
    /**
     * A mouse object to respond to user input from the mouse
     */
    Mouse mouse;
    /**
     * A keyboard object to respond to user input from the keyboard
     */
    Keyboard keyboard;
    /**
     * An object for initializing the user interface and painting graphics
     * through JFrame.
     */
    DisplayScreen displayScreen;
    /**
     * Whether or not graphics are currently being updated.
     */
    boolean run;
    /**
     * Object used to initialize and paint graphics.
     */
    protected Image dbImage;
    /**
     * Object used to calculate metrics of text.
     */
    public FontMetrics metrics;
    /**
     * Object used to initialize and paint graphics.
     */
    private Graphics dbg;
    /**
     * Whether or not a game has begun.
     */
    boolean newGame;
    /**
     * Data to be displayed on the statistics page.
     */
    ArrayList<Statistic> statistics;
    
    /**
     * Initiates the user interface.
     */
    @Override
    public void run() {
        statistics = new ArrayList<Statistic>();
        mouse = new Mouse(this, G);
        keyboard = new Keyboard(this, G);
        displayScreen = new DisplayScreen(this);
    }

    /**
     * The Statistic class conveniently organizes and stores information on how
     * to properly display a given set a data for the statistics page.
     *
     * @author Sarah Simionescu
     * @version 1
     */
    public class Statistic {

        /**
         * The index of the data set.
         */
        int index;
        /**
         * The title of the data set.
         */
        String title;
        /**
         * The x position for each data point.
         */
        int[] xpos;
        /**
         * The y position for each data point.
         */
        int[] ypos;
        /**
         * The written value for each data point.
         */
        String[] value;

        /**
         * Initializes and scales data values to be painted on to the user
         * interface.
         *
         * @param i The index of the statistic.
         * @param t The title for the statistic.
         * @param v The numerical values to be scaled and displayed through this
         * statistic.
         */
        public Statistic(int i, String t, double[] v) {
            index = i;
            title = t;
            xpos = new int[G.day];
            ypos = new int[G.day];
            value = new String[G.day];

            double low = v[0];
            double high = v[0];
            for (int j = 0; j < v.length; j++) {
                if (v[j] < low) {
                    low = v[j];
                }
                if (v[j] > high) {
                    high = v[j];
                }
            }
            for (int j = 0; j < v.length; j++) {
                value[j] = "";
                if (index == 0 || index > 4 && index < 9) {
                    value[j] += "$";
                    value[j] += format(v[j]);

                } else if (index == 2) {
                    value[j] += format(v[j]);
                    value[j] += " star";
                    if (v[j] != 1) {
                        value[j] += "s";
                    }
                } else {
                    value[j] += String.valueOf((int) v[j]);
                    if (index == 1) {
                        value[j] += " view";
                        if (v[j] != 1) {
                            value[j] += "s";
                        }
                    } else if (index == 3) {

                        value[j] += "°";
                    } else if (index == 4) {
                        value[j] += " cup";
                        if (v[j] != 1) {
                            value[j] += "s";
                        }
                    } else if (index > 8) {
                        value[j] += " " + G.week[0].ingredient[index - 8].unit;
                    }
                }
                xpos[j] = (int) new MathTool().map(j, 0, G.day - 1, 250, 950);
                ypos[j] = (int) new MathTool().map(v[j], low, high, 600, 200);
            }

        }

        /**
         * Formats a decimal number to two decimal places.
         *
         * @param d A decimal number.
         * @return A string of the decimal number to two decimal places.
         */
        public String format(Double d) {
            return String.format("%.02f", d);
        }

    }

    /**
     * The DisplayScreen class supports the Display class by initializing the
     * user interface and painting graphics that can only be done through
     * JFrame.
     *
     * @author Sarah Simionescu
     * @version 1
     */
    public class DisplayScreen extends JFrame {

        /**
         * How many times the display screen has been updated (used to animate
         * graphics).
         */
        double aCounter;
        /**
         * How far the user has scrolled through the comments.
         */
        int scroll = 0;
        /**
         * Accesses Display class to read and update information related to the
         * user interface.
         */
        protected Display D;

        /**
         * Initializes the JFrame for the user interface.
         *
         * @param d Accesses all information regarding the user interface.
         */
        public DisplayScreen(Display d) {
            D = d;
            G.gameMode = 0;
            newGame = true;
            run = false;
            aCounter = 0;
            windowManager();
            addKeyListener(keyboard);
            addMouseMotionListener(mouse);
            addMouseListener(mouse);
            addMouseWheelListener(mouse);
            G.start();
        }
        
        /**
         * Initializes the window for the user interface.
         */
        public void windowManager() {
            setTitle("Engine");
            setVisible(true);
            setResizable(false);
            setSize(1024, 768);
            setBackground(new Color(255, 255, 255));
            setDefaultCloseOperation(EXIT_ON_CLOSE);
        }

        /**
         * Initializes the image where graphics will be created and displayed.
         *
         * @param g Graphics object used to create graphics.
         */
        @Override
        public void paint(Graphics g) {
            dbImage = createImage(getWidth(), getHeight());
            dbg = dbImage.getGraphics();
            paintComponent(dbg);
            if (run == true) {
                g.drawImage(dbImage, 0, 0, this);
            }
        }
        
        /**
         * Continuously paints and updates the user interface.
         *
         * @param g Graphics object used to create graphics.
         */
        public void paintComponent(Graphics g) {
            if (run == true) {
                if (G.gameMode == 0) {
                    paintDrink(g, 150, 1, 100, 0.006);
                    G.ui.paint(g);
                    paintTextBlinker(g, 700, 260, 5, 90);
                    paintTabBar(g);

                } else if (G.gameMode == 1 || G.gameMode == 2 || G.gameMode == 3) {
                    paintDrink(g, 150, 1, 100, 0.006);
                    G.ui.paint(g);
                    paintTabBar(g);
                } else if (G.gameMode == 5 || G.gameMode == 6) {
                    paintDrink(g, 150, 1, 100, 0.006);
                    paintChartBackdrop(g);
                    G.ui.paint(g);
                    paintTabBar(g);
                } else if (G.gameMode == 7) {
                    paintDrink(g, 150, 1, 100, 0.006);
                    paintLiveScreen(g, getWidth() / 16, getHeight() / 2, getWidth() / 12 * 5, getHeight() / 12 * 5);
                    paintComments(g, getWidth() / 16 * 15 - getWidth() / 24 * 5, getHeight() / 2 + 50, getWidth() / 12 * 5, 550);
                    G.ui.paint(g);
                    paintTabBar(g);
                } else if (G.gameMode == 8) {
                    paintDrink(g, 150, 1, 100, 0.006);
                    paintStatistics(g);
                    G.ui.paint(g);
                    paintTabBar(g);
                } else if (G.gameMode == 10) {
                    paintDrink(g, 150, 1, 100, 0.006);
                    paintLiveScreen(g, getWidth() / 16, getHeight() / 2, getWidth() / 12 * 5, getHeight() / 12 * 5);
                    paintComments(g, getWidth() / 16 * 15 - getWidth() / 24 * 5, getHeight() / 2 + 50, getWidth() / 12 * 5, 550);
                    G.ui.paint(g);
                    paintTabBar(g);
                    if (G.week[G.day].timeValue() > 19) {
                        mouse.runGameMode(11);
                    } else if (G.week[G.day].stockCheck() == false && G.ui.button.size() == 0) {
                        mouse.runGameMode(10);
                    }
                } else if (G.gameMode == 11) {
                    paintDrink(g, 150, 1, 100, 0.006);
                    G.ui.paint(g);
                    paintComments(g, 10 + 275, getHeight() / 2 + 50, getWidth() / 12 * 5, 550);
                    paintTabBar(g);
                } else if (G.gameMode == 13) {
                    paintDrink(g, 150, 1, 100, 0.006);
                    G.ui.paint(g);
                    paintTabBar(g);
                } else if (G.gameMode == 14) {
                    paintDrink(g, 150, 1, 100, 0.006);
                    G.ui.paint(g);
                    paintTabBar(g);
                }
            }
            aCounter += 0.005;
            repaint();
        }
        
        /**
         * Paints statistics to be viewed on statistics screen.
         *
         * @param g Graphics object used to create graphics.
         */
        public void paintStatistics(Graphics g)
        {
            Graphics2D g2 = (Graphics2D) g;
            
            g.setColor(new Color(210, 210, 210));
            g.fillRoundRect(getWidth() / 12 * 2, getHeight() / 4, getWidth() - getWidth() / 12 * 2 - 50, 500, 20, 20);
            g.setColor(Color.black);
            g.fillRect(250, 200, 2, 400);
            g.fillRect(250, 600, 700, 2);
            if (G.day > 0) {
                for (int i = 0; i < G.day; i++) {
                    new Text("Day " + String.valueOf(i), (int) new MathTool().map(i, 0, G.day-1, 250, 950), 650, 20, Color.BLACK, new Color(0, 0, 0, 0), false, D).paint(g);
                }
            } else {
                new Text("No Statistics Available Yet", 600, 400, 40, Color.BLACK, new Color(0, 0, 0, 0), false, D).paint(g);
            }
            new Text("Select a Topic to View Statistics", 600, 680, 18, Color.BLACK, new Color(0, 0, 0, 0), false, D).paint(g);
            for (int i = 0; i < statistics.size(); i++) {
                if (i == statistics.size() - 1) {
                    g.setColor(new Color(254, 159, 159));
                } else {
                    g.setColor(new Color(190, 190, 190));
                }
                for (int j = 0; j < statistics.get(i).xpos.length; j++) {
                    g.fillOval(statistics.get(i).xpos[j] -10, statistics.get(i).ypos[j] - 10, 20, 20);
                    if (j > 0) {
                        g2.setStroke(new BasicStroke(5));
                        g2.drawLine(statistics.get(i).xpos[j], statistics.get(i).ypos[j], statistics.get(i).xpos[j-1], statistics.get(i).ypos[j-1]);
                        g2.setStroke(new BasicStroke(1));
                    }
                }
                if(statistics.size() > 0 && i == statistics.size()-1)
                {
                    new Text(statistics.get(i).title, 600, 170, 40, Color.BLACK, new Color(0, 0, 0, 0), false, D).paint(g);
                    new Text(statistics.get(i).value[G.day-1], statistics.get(i).xpos[G.day-1], statistics.get(i).ypos[G.day-1]-20, 20, Color.BLACK, new Color(0, 0, 0, 0), false, D).paint(g);
                    new Text(statistics.get(i).value[0], statistics.get(i).xpos[0], statistics.get(i).ypos[0]-20, 20, Color.BLACK, new Color(0, 0, 0, 0), false, D).paint(g);
                }
            }
        }
        
        /**
         * Paints all comments for the current day.
         *
         * @param g Graphics object used to create graphics.
         * @param x The x position of the comments box.
         * @param y The y position of the comments box.
         * @param w The width of the comments box.
         * @param h The height of the comments box.
         */
        public void paintComments(Graphics g, int x, int y, int w, int h) {
            Graphics2D g2 = (Graphics2D) g;
            g.setColor(Color.white);
            g.fillRoundRect(x - w / 2, y - h / 2, w, h, 50, 50); //white background
            for (int i = 0; i < G.week[G.day].comments.size(); i++) {
                G.week[G.day].comments.get(i).xpos = x - w / 2 + 10;
                G.week[G.day].comments.get(i).ypos = y - h / 2 + (G.week[G.day].comments.size() - i) * 70 - scroll;
                if (G.week[G.day].comments.get(i).ypos > y - h / 2) {
                    G.week[G.day].comments.get(i).paint(g);
                }
            }
            g2.setStroke(new BasicStroke(10));
            g.setColor(new Color(210, 210, 210));
            g.fillRoundRect(x - w / 2, y - h / 2 - 60, w, 60, 20, 20);//rating and vews label
            
            
            g.setColor(new Color(245, 233, 190));
            g.fillRect(x - w / 2, y + h / 2, w, h + 100);
            g2.setStroke(new BasicStroke(10));
            g.setColor(new Color(254, 159, 159));
            g.drawRoundRect(x - w / 2, y - h / 2, w, h, 50, 50);//border

            new Text("Rating:", x - w / 2 + 10, y - h / 2 - 30, 20, Color.BLACK, new Color(0, 0, 0, 0), true, D).paint(g);
            paintStars(g, x - w / 2 + 85, y - h / 2 - 30, G.week[G.day].rating);
            new Graphic(x + 30, y - h / 2 - 30, getWidth() / 2, getHeight() / 2, "view", D).paint(g);
            new Text(String.valueOf(G.week[G.day].views), x + 50, y - h / 2 - 30, 20, Color.BLACK, new Color(0, 0, 0, 0), true, D).paint(g);

            new Graphic(x + 100, y - h / 2 - 30, getWidth() / 2, getHeight() / 2, "comment", D).paint(g);
            if (G.week[G.day].comments != null && G.week[G.day].comments.size() > 0) {
                new Text(String.valueOf(G.week[G.day].comments.size()), x + 120, y - h / 2 - 30, 20, Color.BLACK, new Color(0, 0, 0, 0), true, D).paint(g);
            } else {
                new Text("0", x + 120, y - h / 2 - 30, 20, Color.BLACK, new Color(0, 0, 0, 0), true, D).paint(g);
            }
            if(D.G.gameMode == 11)
            {
                g.setColor(new Color(210, 210, 210));
                g.fillRoundRect(x-20 - w/2, y - h/2, 10, h, 10, 10);
                g.setColor(new Color(150, 150, 150));
                float barSize = G.week[G.day].comments.size()-7;
                if (barSize < 1)
                {
                    barSize = 1;
                }
                g.fillRoundRect(x-20 - w/2, y -h/2 + (int)new MathTool().map(scroll, 0, (barSize)*70, 0, h-(int)new MathTool().map(1, 0, barSize, 0, h)), 10, (int)new MathTool().map(1, 0, barSize, 0, h), 10, 10);
            }

        }

        /**
         * Paints the average out of five star rating.
         *
         * @param g Graphics object used to create graphics.
         * @param x The x position of the first star.
         * @param y The y position of the first star.
         * @param r The average rating from comments for the current day.
         */
        public void paintStars(Graphics g, int x, int y, double r) {

            Graphics2D g2 = (Graphics2D) g;
            for (int i = 0; i < r; i++) {
                g.setColor(Color.black);
                g2.fill(createStar(x + i * 20, y, 5, 11, 5, Math.toRadians(-18)));
                if (i == (int) r && r - (int) r >= 0.5) {
                    g.setColor(new Color(210, 210, 210));
                    g.fillRect(x + i * 20, y - 10, 10, 20);
                }
            }
        }

        /**
         * The shape of a star to be painted an represent the average rating on
         * the user interface.
         *
         * @param centerX The x position of the center of the star.
         * @param centerY The y position of the center of the star.
         * @param innerRadius The length of the inner radius of the star
         * (circular center piece).
         * @param outerRadius The length of the outer radius of the star (length
         * of star's rays).
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
         * Paints the live screen window.
         *
         * @param g Graphics object used to create graphics.
         * @param x The x position of the live screen.
         * @param y The y position of the live screen.
         * @param w The width of the live screen.
         * @param h The height of the live screen.
         */
        public void paintLiveScreen(Graphics g, int x, int y, int w, int h) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(10));
            if (G.week[G.day].condition == 2) {
                g.setColor(dayNightColor(186, 217, 215));
            } else {
                g.setColor(dayNightColor(135, 232, 227));
            }
            g.fillRoundRect(x, y, w, h, 50, 50);//sky
            if (G.week[G.day].condition == 0) {
                g.setColor(Color.white);
            } else
             {
                  g.setColor(dayNightColor(138, 217, 102));
             }
            g.fillRoundRect(x, y + h / 2, w, h / 2, 100, 100);//grass
            if (G.week[G.day].condition == 0) {
                paintClouds(g, x, y, w, h, 20, Color.white);
                paintSnow(g, x, y, w, h);
            } else if (G.week[G.day].condition == 1) {
                paintClouds(g, x, y, w, h, 20, Color.white);
            } else if (G.week[G.day].condition == 2) {
                paintClouds(g, x, y, w, h, 20, Color.gray);
                paintRain(g, x, y, w, h);
            }
            paintTrees(g, x, y, w, h);
            g.setColor(dayNightColor(191, 188, 178));
            g.fillRect(x, y + h - h / 3, w, h / 3);//street
            g.setColor(dayNightColor(255, 227, 133));
            for (int i = 0; i < w / 20; i++) {
                g.fillRoundRect(x + i * 20, y + h - 20, 10, 5, 12, 12);//yellow dashes on street
            }
            if (G.gameMode == 7) {
                new Graphic(x + w / 2, y + h / 2 - 30, 281, 273, "shopNight", D).paint(g);
            } else {
                new Graphic(x + w / 2, y + h / 2 - 30, 281, 273, "shopDay", D).paint(g);
            }
            new Text(G.shopName, x + w / 2, y + 40, 15, Color.BLACK, new Color(0, 0, 0, 0),false, D).paint(g);//shop name
            if(G.gameMode == 10)
            {
                G.week[G.day].paintCustomers(g);
            }
            g.setColor(new Color(245, 233, 190));
            Area a = new Area(new Rectangle(0, y, getWidth(), y));
            a.subtract(new Area(new RoundRectangle2D.Double(x, y, w, h, 50, 50)));
            g2.fill(a); //out of bounds
            g.setColor(new Color(254, 159, 159));
            g2.drawRoundRect(x, y, w, h, 50, 50);//border
            new Text(G.week[G.day].screenLabel(), x + w / 2, y, 20, Color.BLACK, new Color(254, 159, 159),false, D).paint(g);
            new Text("Cups Sold: " + String.valueOf(G.week[G.day].cupsSold), x + w / 2, y - 100, 30, Color.BLACK, new Color(254, 159, 159),false, D).paint(g);
            new Text("Money: $" + String.valueOf(G.format(G.week[G.day].money)), x + w / 2, y - 45, 30, Color.BLACK, new Color(254, 159, 159),false, D).paint(g);
            new Text("Price: $" + String.valueOf(G.format(G.week[G.day].price)), x + w / 2, y - 155, 30, Color.BLACK, new Color(254, 159, 159),false, D).paint(g);
        }
        
        /**
         * Paints rain on to the live screen.
         *
         * @param g Graphics object used to create graphics.
         * @param x The x position of the live screen.
         * @param y The y position of the live screen.
         * @param w The width of the live screen.
         * @param h The height of the live screen.
         */
        public void paintRain(Graphics g, int x, int y, int w, int h)
        {
             
             for(int i = 0; i < w/5; i ++)
             {
                if (cos(aCounter*20 + (i)) > 0) {
                    g.setColor(dayNightColor(121, 144, 181));
                } else {
                    g.setColor(new Color(0, 0, 0, 0));
                }
                g.fillOval(x + i*5, y + h/2 + (int) (sin(aCounter*20 + (i)) * h/2), 2, 10);
             }
        }
        
        /**
         * Paints snow on to the live screen.
         *
         * @param g Graphics object used to create graphics.
         * @param x The x position of the live screen.
         * @param y The y position of the live screen.
         * @param w The width of the live screen.
         * @param h The height of the live screen.
         */
        public void paintSnow(Graphics g, int x, int y, int w, int h)
        {
             
             for(int i = 0; i < w/10; i ++)
             {
                if (cos(aCounter + (i)) > 0) {
                    g.setColor(dayNightColor(250, 250, 250));
                } else {
                    g.setColor(new Color(0, 0, 0, 0));
                }
                g.fillOval(x + i*10, y + h/2 + (int) (sin(aCounter + (i)) * h/2), 5, 5);
             }
        }
        
        /**
         * Paints trees on to the live screen.
         *
         * @param g Graphics object used to create graphics.
         * @param x The x position of the live screen.
         * @param y The y position of the live screen.
         * @param w The width of the live screen.
         * @param h The height of the live screen.
         */
        public void paintTrees(Graphics g, int x, int y, int w, int h) {
            for (int i = 0; i < w / 60; i++) {
                g.setColor(dayNightColor(117, 100, 69));
                g.fillRoundRect(x + i * 60 + 20, y + h - 120 - h / 3, 10, 100, 20, 20);//tree trunks
                if(G.week[G.day].condition == 0)
                {
                    g.setColor(new Color(255, 255, 255)); 
                    g.fillOval(x + i * 60 + 20 - 10, y + h - 155 - h / 3, 30, 50);
                }
                g.setColor(dayNightColor(116, 189, 83));
                g.fillOval(x + i * 60 + 20 - 10, y + h - 150 - h / 3, 30, 100);
                
            }
        }
        
        /**
         * Paints clouds on to the live screen.
         *
         * @param g Graphics object used to create graphics.
         * @param x The x position of the live screen.
         * @param y The y position of the live screen.
         * @param w The width of the live screen.
         * @param h The height of the live screen.
         * @param n The number of clouds that can fit in the width of the screen.
         * @param c The color of the clouds.
         */
        public void paintClouds(Graphics g, int x, int y, int w, int h, int n, Color c) {

            for (int i = 0; i < w / n; i++) {
                if (cos(aCounter / 8 + (i)) > 0) {
                    g.setColor(c);
                } else {
                    g.setColor(new Color(0, 0, 0, 0));
                }
                paintCloud(g, x + w / 2 + (int) (sin(aCounter / 8 + (i)) * w), y + 30);
                if (cos(aCounter / 8 + (i + 0.2)) > 0) {
                    g.setColor(c);
                } else {
                    g.setColor(new Color(0, 0, 0, 0));
                }
                paintCloud(g, x + w / 2 + (int) (sin(aCounter / 8 + (i + 0.2)) * w), y + 70);
            }

        }
        
        /**
         * Paints a cloud.
         *
         * @param g Graphics object used to create graphics.
         * @param x The x position of the cloud.
         * @param y The y position of the cloud.
         */
        public void paintCloud(Graphics g, int x, int y) {

            g.fillOval(x, y, 50, 20);
            g.fillOval(x + 10 - (x / 60), y - 10, 40, 30);

        }

        /**
         * Changes a given color value to what it's color would be in a night
         * sky.
         *
         * @param r The red color value.
         * @param g The green color value.
         * @param b The blue color value.
         *
         * @return A new color to match the lighting of the night sky.
         */
        public Color dayNightColor(int r, int g, int b) {
            if (G.gameMode == 7) {
                return new Color(r-14, g-29, b+2);
            } else {
                return new Color(r, g, b);
            }
        }

        /**
         * Paints a rectangular chart.
         *
         * @param g Graphics object used to create graphics.
         */
        public void paintChartBackdrop(Graphics g) {
            g.setColor(new Color(210, 210, 210));
            g.fillRoundRect(getWidth() / 2 - 800 / 2, getHeight() / 2 - 500 / 2 + 120, 800, 500, 20, 20);
            g.setColor(new Color(190, 190, 190));
            for (int i = 0; i < G.week[G.day].ingredient.length; i++) {
                g.fillRoundRect(getWidth() / 2 - 700 / 2, 330 + i * 80, 700, 2, 5, 5);
            }
        }
        
        /**
         * Paints a wavy tea backdrop.
         *
         * @param g Graphics object used to create graphics.
         * @param y The y position of the backdrop.
         * @param w How many waves can fit on the screen.
         * @param a The amplitude of the wave.
         * @param p The period of the wave.
         */
        public void paintDrink(Graphics g, int y, int w, double a, double p) {
            for (int i = 0; i < getWidth() / w + 1; i++) {
                int h = (int) (sin(aCounter + i * p) * a);
                g.setColor(new Color(245, 233, 190));
                g.fillRoundRect(i * w, h + y, w, getHeight() - h - y + w, w, w);
            }
        }
        
        /**
         * Paints a text typing blinker.
         *
         * @param g Graphics object used to create graphics.
         * @param x The x position of the blinker.
         * @param y The y position of the blinker.
         * @param w The width of the blinker.
         * @param h The height of the blinker.
         */
        public void paintTextBlinker(Graphics g, int x, int y, int w, int h) {
            if (sin(aCounter * 20) > 0) {
                g.setColor(Color.black);
            } else {
                g.setColor(new Color(0, 0, 0, 0));
            }
            g.fillRoundRect(x, y, w, h, 5, 5);
        }
        
        /**
         * Paints the bar in front of the tabs.
         *
         * @param g Graphics object used to create graphics.
         */
        public void paintTabBar(Graphics g) {
            g.setColor(new Color(240, 240, 240));
            g.fillRect(0, 70, getWidth(), 13);
        }

    }

}
