package bubbleteagame;

/**
 * The Main class contains the main method and runs two classes in parallel
 * threads separating game functions from user interface and display functions.
 *
 * @author Sarah Simionescu
 * @version 1
 */
public class Main {

    /**
     * Creates, initializes and runs two parallel threads separating game
     * functions from user interface display functions.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Display display = new Display();
        Game game = new Game();
        display.G = game;
        game.D = display;
        display.start();
    }

}
