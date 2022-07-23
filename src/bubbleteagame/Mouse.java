package bubbleteagame;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

/**
 * The Mouse class is responsible for responding to user input from the mouse.
 *
 * @author Sarah Simionescu
 * @version 1
 */
public class Mouse extends MouseAdapter {

    /**
     * Access to user interface.
     */
    protected Display D;
    /**
     * Access to long term memory.
     */
    protected Game G;
    /**
     * The x position of the mouse.
     */
    public double x;
    /**
     * The y position of the mouse.
     */
    public double y;
    /**
     * The game thread to update information according to the user's input.
     */
    Thread game;
    
    /**
     * Initiates the Mouse component.
     * @param d Accesses the user interface.
     * @param g Accesses long term memory.
     */
    public Mouse(Display d, Game g) {
        D = d;
        G = g;
    }
    
    /**
     * Sets the game mode and updates the user interface.
     * @param i Integer representing the game screen.
     */
    public void runGameMode(int i) {
        D.G.gameMode = i;
        game = new Thread(G);
        game.start();
    }
    
    /**
     * Updates the current position of the mouse on the user interface.
     * @param e The event that the mouse has moved.
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        x = e.getX();
        y = e.getY();

    }

    /**
     * Responds to the scroll wheel being moved to scroll through comments at
     * the end of a day.
     *
     * @param e The event that the mouse wheel has been moved.
     */
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if (D.G.gameMode == 11) {
            if (e.getWheelRotation() == 1 && D.displayScreen.scroll < (G.week[G.day].comments.size() - 7) * 70) {
                D.displayScreen.scroll += 20;
            }else if(D.displayScreen.scroll > 0 && e.getWheelRotation() == -1 )
            {
                D.displayScreen.scroll -= 20;
            }      
        }
    }
    
    /**
     * Responds to the mouse being clicked.
     *
     * @param e The event that the mouse has been clicked.
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        if (D.G.gameMode == 0) {
            Integer b = G.ui.checkButton();
            if (b == null) {
            } else if (b == 0) {
                runGameMode(1);
            } else if (b == 1) {
                runGameMode(14);
            }
        } else if (D.G.gameMode == 1||D.G.gameMode ==2||D.G.gameMode == 3) {
            Integer b = G.ui.checkButton();
            if (b == null) {
            } else if (b == 0) {
                runGameMode(2);
            } else if (b == 1) {
                runGameMode(3);
            } else if (b == 2 && G.ui.button.get(b).active == true)
            {
                runGameMode(4);
            }
        } else if (D.G.gameMode == 5 || D.G.gameMode == 6 || D.G.gameMode == 7 || D.G.gameMode == 8) {
            Integer t = G.ui.checkText();
            if (t == null) {
            } else if (t == 0) {
                runGameMode(7);
            } else if (t == 1) {
                runGameMode(5);
            } else if (t == 2) {
                runGameMode(6);
            } else if (t == 3) {
                runGameMode(8);
            }
            if (D.G.gameMode == 5) {
                Integer b = G.ui.checkButton();
                if (b == null) {
                } else if (G.ui.button.get(b).active == true) {
                    if (b % 2 == 0) {
                        G.week[G.day].ingredient[(int) b / 2 +1].recipe--;
                    } else {
                        G.week[G.day].ingredient[(int) b / 2 + 1].recipe++;
                    }
                    runGameMode(5);
                }

            } else if (D.G.gameMode == 6) {
                Integer b = G.ui.checkButton();
                if (b == null) {
                } else if (G.ui.button.get(b).active == true) {
                    G.week[G.day].ingredient[(int)b/3].stock += G.week[G.day].ingredient[(int)b/3].store[b%3][1];
                    G.week[G.day].money -= G.week[G.day].ingredient[(int)b/3].store[b%3][0];
                    G.week[G.day].expenditure += G.week[G.day].ingredient[(int)b/3].store[b%3][0];
                    runGameMode(6);
                }
            } else if (D.G.gameMode == 7) {
                Integer b = G.ui.checkButton();
                if (b == null) {
                } else if (G.ui.button.get(b).active == true) {
                    if (b == 0) {
                        runGameMode(9);
                    } else if (b == 1) {
                        G.week[G.day].price += 0.25;
                        runGameMode(7);
                    } else if (b == 2) {
                        G.week[G.day].price -= 0.25;
                        runGameMode(7);
                    } else if (b == 3) {
                        runGameMode(13);
                    }

                }
            } else if (D.G.gameMode == 8) {
                Integer b = G.ui.checkButton();
                if (b == null) {
                } else if (G.ui.button.get(b).active == true) {
                    double[] statistics = new double[G.day];
                    String topic = "";
                    if (b == 0) {
                        for (int i = 0; i < G.day; i++) {
                            statistics[i] = G.week[i].money;
                        }
                    } else if (b == 1) {
                        for (int i = 0; i < G.day; i++) {
                            statistics[i] = G.week[i].views;
                        }
                    } else if (b == 2) {
                        for (int i = 0; i < G.day; i++) {
                            statistics[i] = G.week[i].rating;
                        }
                    } else if (b == 3) {
                        for (int i = 0; i < G.day; i++) {
                            statistics[i] = G.week[i].temperature;
                        }
                    }else if (b == 4)
                    {
                        for (int i = 0; i < G.day; i++) {
                            statistics[i] = G.week[i].cupsSold;
                        }
                    }else if (b == 5)
                    {
                        for (int i = 0; i < G.day; i++) {
                            statistics[i] = G.week[i].price;
                        }
                    }else if (b == 6)
                    {
                        for (int i = 0; i < G.day; i++) {
                            statistics[i] = G.week[i].expenditure;
                        }
                    }else if (b == 7)
                    {
                        for (int i = 0; i < G.day; i++) {
                            statistics[i] = G.week[i].revenue;
                        }
                    } else if (b == 8) {
                        for (int i = 0; i < G.day; i++) {
                            statistics[i] = G.week[i].profit();
                        }
                    } else {
                        for (int i = 0; i < G.day; i++) {
                            statistics[i] = G.week[i].ingredient[b - 8].recipe;
                        }
                    }
                    topic = G.ui.button.get(b).text;
                    D.statistics.add(D.new Statistic(b, topic, statistics));
                }

            }
        } else if (D.G.gameMode == 10) {
            Integer b = G.ui.checkButton();
            if (b == null) {
            } else if (b==0){
                runGameMode(11);
            }
        } else if (D.G.gameMode == 11) {
            Integer b = G.ui.checkButton();
            if (b == null) {
            } else if (b == 0) {
                if (G.day < 6) {
                    runGameMode(12);
                } else {
                    runGameMode(13);
                }
            }
        } else if (D.G.gameMode == 13)
        {
             Integer b = G.ui.checkButton();
            if (b == null) {
        
            } else if (b == 0) {
                runGameMode(0);
            }
        } else if (D.G.gameMode == 14) {
            Integer b = G.ui.checkButton();
            if (b == null) {

            } else if (b == 0) {
                runGameMode(0);
            }
        }
    }

}
