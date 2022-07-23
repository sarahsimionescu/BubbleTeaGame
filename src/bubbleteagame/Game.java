package bubbleteagame;

import java.awt.Color;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * The Game class manages, updates and stores all long-term objects required for
 * playing the game.
 *
 * @author Sarah Simionescu
 * @version 1
 */
public class Game extends Thread implements Runnable {

    /**
     * Access to user interface.
     */
    protected Display D;
    /**
     * Stores the name of the shop given by the user.
     */
    String shopName;
    /**
     * Stores the user's name given by the user to record high scores.
     */
    String userName;
    /**
     * Stores an array of Day classes, one to manage each day of the week.
     */
    Day[] week;
    /**
     * An integer that tracks which day it currently is.
     */
    int day;
    /**
     * The width of the display screen.
     */
    int w;
    /**
     * The height of the display screen.
     */
    int h;
    /**
     * An class that can store all three types of UI components (Graphic, Text
     * and Button) to be managed and displayed with ease.
     */
    UI ui = new UI();
    /**
     * An integer representing which game screen is currently being displayed.
     */
    int gameMode;
    /**
     * The highest score.
     */
    double highscore;
    /**
     * The name of the user who achieved the highest score.
     */
    String highscoreName;

    /**
     * Reads or creates the txt file responsible for storing the highest score
     * and stores it in memory, and is only run by the Main class when starting
     * the program.
     */
    public Game() {
        try {
            File hs = new File("highscore.txt");
            if (hs.createNewFile()) {
                System.out.println("File created: " + hs.getName());
                highscore = 0;
                highscoreName = "";
            } else {
            }
            try {
                Scanner scanner = new Scanner(hs);
                String[] data;
                while (scanner.hasNextLine()) {
                    data = scanner.nextLine().split(":");
                    highscore = Double.parseDouble(data[1]);
                    highscoreName = data[0];
                }
                scanner.close();
            } catch (FileNotFoundException e) {
                System.out.println("An error occurred with reading highscore file.");
                e.printStackTrace();
            }
        } catch (IOException e) {
            System.out.println("An error occurred with initiating highscore file.");
            e.printStackTrace();
        }
    }
    
    /**
     * Updates the game based on user input.
     */
    @Override
    public void run() {
        D.run = false;
        ui.clear();
        w = D.displayScreen.getWidth();
        h = D.displayScreen.getHeight();
        if (gameMode == 4) { //new week       

            week = new Day[7];
            day = 0;
            week[day] = new Day(day, this, D);
            gameMode = 7;
        } else if (gameMode == 9) //new day
        {
            D.statistics.clear();
            D.displayScreen.scroll = 0;

            week[day].run();
            gameMode = 10;
        } else if (gameMode == 12) {
            day++;
            week[day] = new Day(day, this, D);
            gameMode = 7;
        }
        if (gameMode == 0) { //main menu
            ui.graphic.add(new Graphic(w / 2, h / 2, null, null, "mainMenuGraphic", D));
            ui.graphic.add(new Graphic(w / 2, h / 2 + 26, null, null, "searchBar", D));
            ui.button.add(new Button("PLAY", w / 2, 500, 60, new Color(254, 159, 159), true, D));
            ui.button.add(new Button("TUTORIAL", w / 2, 600, 30, new Color(254, 159, 159), true, D));
            ui.text.add(new Tab("Main Menu", w / 10, 60, 20, Color.black, new Color(240, 240, 240), false, D));
            shopName = "";
            userName = "";
            ui.text.add(new Text("Highscore: " + highscoreName + " $" + String.valueOf(format(highscore)), w / 2, 700, 30, Color.BLACK, new Color(254, 159, 159, 100), false, D));
        } else if (gameMode == 1 || gameMode == 2 || gameMode == 3) { //login
            ui.graphic.add(new Graphic(w / 2, h / 2, null, null, "loginGraphic", D));
            ui.graphic.add(new Graphic(w / 2, h / 2 + 26, null, null, "searchBar", D));
            ui.text.add(new Tab("Login", w / 10, 60, 20, Color.black, new Color(240, 240, 240), false, D));
            ui.text.add(new Text("Enter the name of your shop.", w / 2, 360, 30, Color.black, new Color(254, 159, 159), false, D));
            ui.text.add(new Text("Enter your name.", w / 2, 560, 30, Color.black, new Color(254, 159, 159), false, D));
            ui.button.add(new TextBox(shopName, w / 2, 400, 30, Color.white, false, D));
            ui.button.add(new TextBox(userName, w / 2, 600, 30, Color.white, false, D));
            if (gameMode == 2) {
                ui.button.get(0).active = true;
                ui.button.get(1).active = false;

            } else if (gameMode == 3) {
                ui.button.get(0).active = false;
                ui.button.get(1).active = true;
            }
            ui.button.add(new Button("NEXT", 920, 700, 40, new Color(254, 159, 159), false, D));
            if (shopName.length() > 0 && userName.length() > 0) {
                ui.button.get(ui.button.size() - 1).active = true;
            }
            ui.text.add(new Text("Max 15 characters A-Z", w / 2, 432, 15, Color.black, new Color(0, 0, 0, 0), false, D));
            ui.text.add(new Text("Max 15 characters A-Z", w / 2, 632, 15, Color.black, new Color(0, 0, 0, 0), false, D));
        } else if (gameMode == 5) {//recipe
            ui.graphic.add(new Graphic(w / 2, h / 2 + 26, null, null, "searchBar", D));
            ui.text.add(new Tab("Live Feed", w / 10, 60, 20, Color.black, new Color(210, 210, 210), false, D));
            ui.text.add(new Tab("Recipe", w / 10 + w / 5, 60, 20, Color.black, new Color(240, 240, 240), false, D));
            ui.text.add(new Tab("Store", w / 10 + (w / 5) * 2, 60, 20, Color.black, new Color(210, 210, 210), false, D));
            ui.text.add(new Tab("Statistics", w / 10 + (w / 5) * 3, 60, 20, Color.black, new Color(210, 210, 210), false, D));
            ui.graphic.add(new Graphic(150, 130, null, null, "recipeTitle", D));
            for (int i = 0; i < week[day].ingredient.length; i++) {
                if (i != 0) {
                    ui.button.add(new Button("-", w / 4 * 2 - 80, 300 + i * 80, 20, new Color(254, 159, 159), false, D));
                    if (week[day].ingredient[i].recipe > 1) {
                        ui.button.get(ui.button.size() - 1).active = true;
                    }
                    ui.button.add(new Button("+", w / 4 * 2 + 80, 300 + i * 80, 20, new Color(254, 159, 159), false, D));
                    if (week[day].ingredient[i].recipe < 10) {
                        ui.button.get(ui.button.size() - 1).active = true;
                    }
                }
                ui.text.add(new Text(week[day].ingredient[i].name, w / 4 * 1, 300 + i * 80, 50, Color.black, new Color(0, 0, 0, 0), false, D));
                ui.text.add(new Text(week[day].ingredient[i].displayRecipe(), w / 4 * 2, 300 + i * 80, 30, Color.black, new Color(255, 255, 255), false, D));
                ui.text.add(new Text(week[day].ingredient[i].displayStock(), w / 4 * 3, 300 + i * 80, 30, Color.black, new Color(0, 0, 0, 0), false, D));
            }
            ui.text.add(new Text("Recipe", w / 4 * 2, 300 - 80, 50, Color.black, new Color(254, 159, 159), false, D));
            ui.text.add(new Text("Stock", w / 4 * 3, 300 - 80, 50, Color.black, new Color(254, 159, 159), false, D));
            ui.text.add(new Text("Makes: " + week[day].recipeInfo()[0], w / 2, 150, 30, Color.black, new Color(255, 255, 255), false, D));
            ui.text.add(new Text("Limited by: " + week[day].ingredient[week[day].recipeInfo()[1]].name, w / 4 * 3, 150, 30, Color.black, new Color(255, 255, 255), false, D));
        } else if (gameMode == 6) { //store
            ui.graphic.add(new Graphic(w / 2, h / 2 + 26, null, null, "searchBar", D));
            ui.text.add(new Tab("Live Feed", w / 10, 60, 20, Color.black, new Color(210, 210, 210), false, D));
            ui.text.add(new Tab("Recipe", w / 10 + w / 5, 60, 20, Color.black, new Color(210, 210, 210), false, D));
            ui.text.add(new Tab("Store", w / 10 + (w / 5) * 2, 60, 20, Color.black, new Color(240, 240, 240), false, D));
            ui.text.add(new Tab("Statistics", w / 10 + (w / 5) * 3, 60, 20, Color.black, new Color(210, 210, 210), false, D));
            ui.graphic.add(new Graphic(150, 130, null, null, "storeTitle", D));
            int countActiveButtons = 0;
            for (int i = 0; i < week[day].ingredient.length; i++) {
                ui.text.add(new Text(week[day].ingredient[i].name, w / 4 * 1, 300 + i * 80, 50, Color.black, new Color(0, 0, 0, 0), false, D));
                for (int j = 0; j < week[day].ingredient[i].store.length; j++) {
                    ui.button.add(new Button(Integer.toString((int) week[day].ingredient[i].store[j][1]), w / 4 * 2 + w / 12 * (j - 1), 295 + i * 80, 35, new Color(254, 159, 159), false, D));
                    if (week[day].money >= week[day].ingredient[i].store[j][0]) {
                        ui.button.get(ui.button.size() - 1).active = true;
                    }
                    ui.text.add(new Text("$" + format(week[day].ingredient[i].store[j][0]), w / 4 * 2 + w / 12 * (j - 1), 274 + i * 80, 18, Color.black, new Color(0, 0, 0, 0), false, D));
                    ui.text.add(new Text(week[day].ingredient[i].displayStoreUnit(), w / 4 * 2 + w / 12 * (j - 1), 315 + i * 80, 15, Color.black, new Color(0, 0, 0, 0), false, D));
                }
                ui.text.add(new Text(week[day].ingredient[i].displayStock(), w / 4 * 3, 300 + i * 80, 30, Color.black, new Color(0, 0, 0, 0), false, D));
            }
            ui.text.add(new Text("Store", w / 4 * 2, 300 - 80, 50, Color.black, new Color(254, 159, 159), false, D));
            ui.text.add(new Text("Stock", w / 4 * 3, 300 - 80, 50, Color.black, new Color(254, 159, 159), false, D));
            ui.text.add(new Text("Money: $" + format(week[day].money), w / 2, 150, 30, Color.black, new Color(255, 255, 255), false, D));
            ui.text.add(new Text("Makes: " + week[day].recipeInfo()[0], w / 4 * 3, 150, 30, Color.black, new Color(255, 255, 255), false, D));
        } else if (gameMode == 7) //live morning
        {
            ui.graphic.add(new Graphic(w / 2, h / 2 + 26, null, null, "searchBar", D));
            ui.text.add(new Tab("Live Feed", w / 10, 60, 20, Color.black, new Color(240, 240, 240), false, D));
            ui.text.add(new Tab("Recipe", w / 10 + w / 5, 60, 20, Color.black, new Color(210, 210, 210), false, D));
            ui.text.add(new Tab("Store", w / 10 + (w / 5) * 2, 60, 20, Color.black, new Color(210, 210, 210), false, D));
            ui.text.add(new Tab("Statistics", w / 10 + (w / 5) * 3, 60, 20, Color.black, new Color(210, 210, 210), false, D));
            ui.graphic.add(new Graphic(170, 130, null, null, "liveFeedTitle", D));
            ui.button.add(new Button("START DAY", w / 16 + (w / 12 * 5) / 2, h / 2 + (h / 12 * 5) / 2, 30, new Color(254, 159, 159, 200), true, D));
            if (week[day].price == 0 || week[day].recipeInfo()[0] == 0) {
                ui.button.get(ui.button.size() - 1).active = false;
                String requirements = "";
                if (week[day].price == 0) {
                    requirements += "Set a Price";
                    if (week[day].recipeInfo()[0] == 0) {
                        requirements += " and ";
                    }
                }
                if (week[day].recipeInfo()[0] == 0) {
                    requirements += "Buy Stock";
                }
                requirements += " to Start Day";
                ui.text.add(new Text(requirements, w / 16 + (w / 12 * 5) / 2, h / 2 + (h / 12 * 5) / 2 + 30, 20, Color.black, new Color(255, 255, 255, 100), false, D));
            }
            ui.button.add(new Button("+", w / 16 + (w / 12 * 5) / 2 + 120, h / 2 - 155, 20, new Color(254, 159, 159), false, D));
            if (week[day].price < 10) {
                ui.button.get(ui.button.size() - 1).active = true;
            }
            ui.button.add(new Button("-", w / 16 + (w / 12 * 5) / 2 - 120, h / 2 - 155, 20, new Color(254, 159, 159), false, D));
            if (week[day].price > 0) {
                ui.button.get(ui.button.size() - 1).active = true;
            }
            ui.button.add(new Button("Declare Bankruptcy", w / 16 + (w / 12 * 5) / 2, h - 20, 20, new Color(254, 159, 159), true, D));

        } else if (gameMode == 8) //statistics
        {
            ui.graphic.add(new Graphic(w / 2, h / 2 + 26, null, null, "searchBar", D));
            ui.text.add(new Tab("Live Feed", w / 10, 60, 20, Color.black, new Color(210, 210, 210), false, D));
            ui.text.add(new Tab("Recipe", w / 10 + w / 5, 60, 20, Color.black, new Color(210, 210, 210), false, D));
            ui.text.add(new Tab("Store", w / 10 + (w / 5) * 2, 60, 20, Color.black, new Color(210, 210, 210), false, D));
            ui.text.add(new Tab("Statistics", w / 10 + (w / 5) * 3, 60, 20, Color.black, new Color(240, 240, 240), false, D));
            ui.graphic.add(new Graphic(170, 130, null, null, "statisticsTitle", D));
            ui.button.add(new Button("Money", w / 12, 200 + 40 * 0, 20, new Color(254, 159, 159), true, D));
            ui.button.add(new Button("Views", w / 12, 200 + 40 * 1, 20, new Color(254, 159, 159), true, D));
            ui.button.add(new Button("Rating", w / 12, 200 + 40 * 2, 20, new Color(254, 159, 159), true, D));
            ui.button.add(new Button("Temperature", w / 12, 200 + 40 * 3, 20, new Color(254, 159, 159), true, D));
            ui.button.add(new Button("Cups Sold", w / 12, 200 + 40 * 4, 20, new Color(254, 159, 159), true, D));
            ui.button.add(new Button("Price", w / 12, 200 + 40 * 5, 20, new Color(254, 159, 159), true, D));
            ui.button.add(new Button("Expenditure", w / 12, 200 + 40 * 6, 20, new Color(254, 159, 159), true, D));
            ui.button.add(new Button("Revenue", w / 12, 200 + 40 * 7, 20, new Color(254, 159, 159), true, D));
            ui.button.add(new Button("Profit/Loss", w / 12, 200 + 40 * 8, 20, new Color(254, 159, 159), true, D));
            ui.button.add(new Button("Tea", w / 12, 200 + 40 * 9, 20, new Color(254, 159, 159), true, D));
            ui.button.add(new Button("Milk", w / 12, 200 + 40 * 10, 20, new Color(254, 159, 159), true, D));
            ui.button.add(new Button("Sugar", w / 12, 200 + 40 * 11, 20, new Color(254, 159, 159), true, D));
            ui.button.add(new Button("Tapioca", w / 12, 200 + 40 * 12, 20, new Color(254, 159, 159), true, D));
            ui.button.add(new Button("Ice", w / 12, 200 + 40 * 13, 20, new Color(254, 159, 159), true, D));
            if (day == 0) {
                for (int i = 0; i < ui.button.size(); i++) {
                    ui.button.get(i).active = false;
                }
            }

        } else if (gameMode == 10) {
            ui.graphic.add(new Graphic(w / 2, h / 2 + 26, null, null, "searchBar", D));
            ui.text.add(new Tab("Live Feed", w / 10, 60, 20, Color.black, new Color(240, 240, 240), false, D));
            ui.graphic.add(new Graphic(170, 130, null, null, "liveFeedTitle", D));
            if (week[day].stockCheck() == false) {
                ui.button.add(new Button("SKIP TO END OF DAY", w / 16 + (w / 12 * 5) / 2, h / 2 + (h / 12 * 5) / 2 + 40, 25, new Color(254, 159, 159, 200), true, D));
                ui.text.add(new Text("You are Sold Out of Stock", w / 16 + (w / 12 * 5) / 2, h / 2 + (h / 12 * 5) / 2, 20, Color.black, new Color(255, 255, 255, 100), false, D));
            }

        } else if (gameMode == 11) {
            week[day].views = week[day].customers.length;
            ui.graphic.add(new Graphic(w / 2, h / 2 + 26, null, null, "searchBar", D));
            ui.text.add(new Tab("Day " + (day + 1), w / 10, 60, 20, Color.black, new Color(240, 240, 240), false, D));
            ui.text.add(new Text("Day " + (day + 1) + " Over", w / 2 + 50, 150, 50, Color.BLACK, new Color(0, 0, 0, 0), true, D));
            ui.text.add(new Text("Views: " + String.valueOf(week[day].customers.length), w / 2 + 50, 200, 30, Color.BLACK, new Color(254, 159, 159), true, D));
            ui.text.add(new Text("Cups Sold: " + String.valueOf(week[day].cupsSold), w / 2 + 50, 250, 30, Color.BLACK, new Color(254, 159, 159), true, D));
            ui.text.add(new Text("Expenditure: $" + String.valueOf(format(week[day].expenditure)), w / 2 + 50, 300, 30, Color.BLACK, new Color(254, 159, 159), true, D));
            ui.text.add(new Text("Revenue: $" + String.valueOf(format(week[day].revenue)), w / 2 + 50, 350, 30, Color.BLACK, new Color(254, 159, 159), true, D));
            String profitOrLoss = "";
            if (week[day].revenue < week[day].expenditure) {
                profitOrLoss = "Loss";
            } else {
                profitOrLoss = "Profit";
            }
            profitOrLoss += ": $";
            ui.text.add(new Text(profitOrLoss + String.valueOf(format(week[day].profit())), w / 2 + 50, 400, 30, Color.BLACK, new Color(254, 159, 159), true, D));
            ui.text.add(new Text("Money: $" + String.valueOf(format(week[day].money)), w / 2 + 50, 450, 30, Color.BLACK, new Color(254, 159, 159), true, D));
            if (day > 0) {
                for (int i = 1; i < week[day].ingredient.length; i += 1) {
                    if (week[day].ingredient[i].stockOut != null) {
                        String stockMessage = "";
                        if (week[day - 1].ingredient[i].stock == 0 || week[day].ingredient[i].stock == 0) {

                        } else if (week[day - 1].ingredient[i].stock < week[day].ingredient[i].stock) {
                            stockMessage = week[day - 1].ingredient[i].stock + " " + week[day].ingredient[i].unit + " of " + week[day].ingredient[i].name + " has " + week[day].ingredient[i].stockOut + ".";
                            week[day].ingredient[i].stock -= week[day - 1].ingredient[i].stock;
                        } else {
                            stockMessage = "All of your " + week[day].ingredient[i].name + " has " + week[day].ingredient[i].stockOut + ".";
                            week[day].ingredient[i].stock = 0;
                        }
                        ui.text.add(new Text(stockMessage, w / 2 + 50, 500 + 10 * i, 20, Color.BLACK, new Color(0, 0, 0, 0), true, D));
                    }

                }
                ui.text.add(new Text("Money: $" + String.valueOf(format(week[day].money)), w / 2 + 50, 450, 30, Color.BLACK, new Color(254, 159, 159), true, D));
            }
            ui.button.add(new Button("NEXT", 920, 700, 40, new Color(254, 159, 159), true, D));
        } else if (gameMode == 13) {
            ui.graphic.add(new Graphic(w / 2, h / 2 + 26, null, null, "searchBar", D));
            ui.text.add(new Tab("Week Over", w / 10, 60, 20, Color.black, new Color(240, 240, 240), false, D));
            ui.text.add(new Text("Week Over", w / 2, 150, 50, Color.BLACK, new Color(0, 0, 0, 0), false, D));
            if (day > 0) {
                int totalViews = 0;
                int totalCupsSold = 0;
                double totalExpenditure = 0;
                double totalRevenue = 0;
                for (int i = 0; i < day; i++) {
                    totalViews += week[i].customers.length;
                    totalCupsSold += week[i].cupsSold;
                    totalExpenditure += week[i].expenditure;
                    totalRevenue += week[i].revenue;
                }
                ui.text.add(new Text("Total Views: " + String.valueOf(totalViews), w / 2, 200, 30, Color.BLACK, new Color(254, 159, 159), false, D));
                ui.text.add(new Text("Total Cups Sold: " + String.valueOf(totalCupsSold), w / 2, 250, 30, Color.BLACK, new Color(254, 159, 159), false, D));
                ui.text.add(new Text("Total Expenditure: $" + String.valueOf(format(totalExpenditure)), w / 2, 300, 30, Color.BLACK, new Color(254, 159, 159), false, D));
                ui.text.add(new Text("Total Revenue: $" + String.valueOf(format(totalRevenue)), w / 2, 350, 30, Color.BLACK, new Color(254, 159, 159), false, D));
                String profitOrLoss = "";
                if (totalRevenue < totalExpenditure) {
                    profitOrLoss = "Total Loss";
                } else {
                    profitOrLoss = "Total Profit";
                }
                profitOrLoss += ": $";
                ui.text.add(new Text(profitOrLoss + String.valueOf(format(totalRevenue - totalExpenditure)), w / 2, 400, 30, Color.BLACK, new Color(254, 159, 159), false, D));
            }
            if (week[day].money > highscore) {
                highscore = week[day].money;
                highscoreName = userName;
                try {
                    BufferedWriter writer = new BufferedWriter(new FileWriter("highscore.txt"));
                    writer.write(userName + ":" + week[day].money);
                    writer.close();
                    System.out.println("Successfully wrote new highscore to the file.");
                } catch (IOException e) {
                    System.out.println("An error occurred with recording new highscore.");
                    e.printStackTrace();
                }
                ui.text.add(new Text("New Highscore: " + userName + " $" + String.valueOf(format(week[day].money)), w / 2, 600, 30, Color.BLACK, new Color(254, 159, 159), false, D));
            } else {
                ui.text.add(new Text("Highscore: " + highscoreName + " $" + String.valueOf(format(highscore)), w / 2, 600, 30, Color.BLACK, new Color(254, 159, 159), false, D));
            }
            ui.text.add(new Text("Total Money: $" + String.valueOf(format(week[day].money)), w / 2, 500, 60, Color.BLACK, new Color(254, 159, 159), false, D));
            ui.button.add(new Button("PLAY AGAIN", w / 2, 700, 40, new Color(254, 159, 159), true, D));
        } else if (gameMode == 14) {
            ui.graphic.add(new Graphic(w / 2, h / 2 + 26, null, null, "searchBar", D));
            ui.text.add(new Tab("Tutorial", w / 10, 60, 20, Color.black, new Color(240, 240, 240), false, D));
            ui.graphic.add(new Graphic(w / 2, h / 2, null, null, "tutorial", D));
            ui.button.add(new Button("BACK", 920, 700, 40, new Color(254, 159, 159), true, D));
        }
        D.run = true;
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
