package bubbleteagame;

import java.awt.Graphics;
import java.util.ArrayList;

/**
 * The Day class stores, manages and generates all the needed information with
 * regards to a particular day of the week.
 *
 * @author Sarah Simionescu
 * @version 1
 */
public class Day extends Thread {
    
    /**
     * This index of this particular day.
     */
    int index;
    /**
     * What time the day started.
     */
    long systemTimer;
    /**
     * This temperature of this particular day.
     */
    int temperature;
    /**
     * An integer representing the conditions outside on this particular day.
     */
    int condition;
    /**
     * The price set by the user for this particular day.
     */
    double price;
    /**
     * The expenditure on this particular day.
     */
    double expenditure;
    /**
     * The revenue on this particular day.
     */
    double revenue;
    /**
     * The cups sold on this particular day.
     */
    int cupsSold;
    /**
     * The average rating on this particular day.
     */
    double rating;
    /**
     * The number of viewers that will be on this particular day.
     */
    int customerNum;
    /**
     * The number of viewers that have viewed on this particular day.
     */
    int views;
    /**
     * An array of all the customers that will view and possibly interact with
     * the shop's page on this particular day.
     */
    Customer[] customers;
    /**
     * An array list of the comments given on this particular day.
     */
    ArrayList<Comment> comments;
    /**
     * Access to long term memory.
     */
    protected Game G;
    /**
     * Access to user interface.
     */
    protected Display D;
    /**
     * An array storing all information with regards to a particular ingredient
     * on this particular day.
     */
    Ingredient[] ingredient;
    /**
     * The money the user has on this particular day.
     */
    double money;
    /**
     * Accesses MathTool class to use mathematical functions.
     */
    MathTool math;

    /**
     * Initializes all the information with regards to a particular day of the
     * week.
     *
     * @param i The index of the Day
     * @param g Access to long term memory
     * @param d Access to the user interface
     */
    public Day(int i, Game g, Display d) {
        G = g;
        D = d;
        math = new MathTool();
        index = i;
        temperature = temperature();
        condition = condition();
        comments = new ArrayList<Comment>();
        if (index == 0) {
            ingredient = new Ingredient[6];
            ingredient[0] = new Ingredient(0, "Cup", "cup", 2.00, 10, 4.00, 25, 6.00, 50, 0, 1, null);
            ingredient[1] = new Ingredient(1, "Tea", "tsp", 10.00, 20, 14.00, 40, 18.00, 60, 0, 1, null);
            ingredient[2] = new Ingredient(2, "Milk", "tbsp", 1.00, 10, 1.80, 20, 2.00, 40, 0, 1, "expired");
            ingredient[3] = new Ingredient(3, "Sugar", "tsp", 3.00, 10, 5.00, 20, 8.00, 50, 0, 1, null);
            ingredient[4] = new Ingredient(4, "Tapioca", "tbsp", 3.00, 10, 5.00, 20, 8.00, 50, 0, 1, null);
            ingredient[5] = new Ingredient(5, "Ice", "tbsp", 1, 25, 2.25, 60, 4.00, 90, 0, 1, "melted");
            rating = averageRating();
            price = 5;
            money = 50.00;
        } else {
            rating = G.week[index - 1].rating;
            price = G.week[index - 1].price;
            money = G.week[index - 1].money;
            ingredient = new Ingredient[G.week[index - 1].ingredient.length];
            for(int j = 0; j < ingredient.length; j++)
            {
                ingredient[j] = new Ingredient(j, G.week[i-1].ingredient[j].name, G.week[i-1].ingredient[j].unit, G.week[i-1].ingredient[j].store[0][0], G.week[i-1].ingredient[j].store[0][1], G.week[i-1].ingredient[j].store[1][0], G.week[i-1].ingredient[j].store[1][1], G.week[i-1].ingredient[j].store[2][0], G.week[i-1].ingredient[j].store[2][1],  G.week[i-1].ingredient[j].stock, G.week[i-1].ingredient[j].recipe, G.week[i-1].ingredient[j].stockOut);
            }
        }
        systemTimer = 0;
    }
    
    /**
     * Begins the day.
     */
    @Override
    public void run() {
        customers = new Customer[customerNum()];
        revenue = 0;
        for (int i = 0; i < customers.length; i++) {
            customers[i] = new Customer(this,G, D);
        }
        quickSortCustomers(customers, 0, customers.length - 1);
        systemTimer = System.currentTimeMillis() / 100;
    }

    /**
     * Updates the customer's actions and information changing throughout the
     * day (views, rating, etc.).
     *
     * @param g Graphics object used to create graphics.
     */
    public void paintCustomers(Graphics g) {
        rating = averageRating();
        for (int i = 0; i < customers.length; i++) {
            if (G.week[index].timeValue() > customers[i].time) {
                if (customers[i].getState() == Thread.State.NEW) {
                    views++;
                    customers[i].start();
                } else if (customers[i].willPurchase == true && customers[i].velocity < 100) {
                    customers[i].paint(g);
                }
            }
        }
    }
    
    /**
     * Whether or not there is enough stock to sell another drink.
     * 
     * @return Whether or not there is enough stock to sell another drink.
     */
    public boolean stockCheck() {
        boolean b = true;
        for (int i = 0; i < ingredient.length; i++) {
            if (ingredient[i].stock < ingredient[i].recipe) {
                b = false;
            }
        }
        return b;
    }

    /**
     * Calculates the average rating from customers based on the comments
     * received.
     *
     * @return The average rating for the day.
     */
    public double averageRating() {
        double a = 0;
        if (comments != null && comments.size() > 0) {
            int counter = 0;
            for (int i = 0; i < comments.size(); i++) {
                if (comments.get(i).rating != 0) {
                    a += comments.get(i).rating;
                    counter++;
                }
            }
            if (counter > 0) {
                a /= counter;
            }
        }
        if (a == 0) {
            if (index == 0) {
                a = 2.5;
            } else {
                a = G.week[index - 1].rating;
            }
        }
        return a;

    }

    /**
     * Creates text for a label on the Live Screen window saying the current
     * day, time and weather conditions.
     *
     * @return Text describing the current day, time and weather conditions.
     */
    public String screenLabel() {
        String label = "Day " + (G.day + 1) + " | " + time() + " | ";
        if (condition == 0) {
            label += "Snowy";
        } else if (condition == 1) {
            label += "Cloudy";
        }else if (condition == 2)
        {
            label += "Rainy";
        }else
        {
            label += "Sunny";
        }
        label += " " + temperature + "°";
        return label;
    }
    
    /**
     * Formats the decimal time value into a "00:00am/pm" style.
     *
     * @return Formatted time value.
     */
    public String time() {
        double time = timeValue();
        String t = "";
        if ((int)time <= 12) {
            t = String.valueOf((int) time) + ":";

        } else {
            t = String.valueOf((int)time - 12) + ":";
        }
        t += String.format("%02d", (int) (math.map(time - (int) time, 0, 1, 0, 60)));
        if (time < 12) {
            t += "am";
        } else {
            t += "pm";
        }
        return t;
    }

    /**
     * Creates a decimal time value based on how long the day has been going on
     * for.
     *
     * @return Decimal value representing time.
     */
    public double timeValue() {
        double time = 0;
        if (systemTimer == 0) {
            time = 7.00;
        } else {
            time = math.map(System.currentTimeMillis() / 100 - systemTimer, 0, 600, 7, 19);
        }
        return time;
    }

    /**
     * Generates a random temperature for the day.
     *
     * @return Temperature in degrees Celsius.
     */
    public int temperature() {
        return (int) math.random(-10, 35);
    }

    /**
     * Generates a random weather condition based on the temperature for the
     * day.
     *
     * @return Integer representing weather condition.
     */
    public int condition() {
        int condition = 0;
        if (temperature < 0) {
            condition = (int) math.random(0, 1);
        } else if (temperature < 10) {
            condition = (int) math.random(1, 3);
        } else if (temperature < 25) {
            condition = (int)math.random(2,3);
        } else
        {
            condition = 3;
        }
        return condition;
    }
    
    /**
     * Calculates the profit or loss for the day.
     *
     * @return Decimal value representing profit/loss in dollars.
     */
    public double profit() {
        return revenue - expenditure;
    }

    /**
     * Decides how many customers will view the page today based on yesterday's
     * viewers, yesterday's ratings and the current weather.
     *
     * @return Integer of how many customers will view the page.
     */
    public int customerNum() {

        int n = (int) (30 * weatherFactor());
        if (index > 0) {
            n = (int) ((G.week[index - 1].customers.length/G.week[index - 1].weatherFactor()) * math.map(G.week[index - 1].rating, 0, 5, 0.6, 1.4) * weatherFactor());
        }
        if (n < 10) {
            n = 10;
        }
        return n;
    }

    /**
     * Creates a factor from 0.75-1.25 to increase or decrease the amount of
     * customers that will view the page based on the weather conditions
     * outside.
     *
     * @return Decimal value 0.75-1.25 based on the weather conditions.
     */
    public double weatherFactor() {
        int temperatureFactor = math.gaussian(math.map(temperature, -10, 35, 20, 100), 2, 0, 100);
        int conditionFactor;
        if (condition == 0) {
            conditionFactor = math.gaussian(20, 2, 0, 100);
        } else if (condition == 1) {
            conditionFactor = math.gaussian(70, 2, 0, 100);
        } else if (condition == 2) {
            conditionFactor = math.gaussian(50, 2, 0, 100);
        } else {
            conditionFactor = math.gaussian(100, 2, 0, 100);
        }
        return math.map((conditionFactor + temperatureFactor) / 2, 0, 100, 0.75, 1.25);
    }

    /**
     * Calculates how many cups of tea can be made with the current stock of
     * ingredients, and which ingredient is limiting this value.
     *
     * @return An array with the number of cups that can be made stored in index
     * 0, and the index of the limiting ingredient stored in index 1.
     */
    public int[] recipeInfo() {
        int[] m = new int[2];
        m[0] = -1;
        for (int i = 0; i < ingredient.length; i++) {
            if (ingredient[i].recipe != 0) {

                if (ingredient[i].stock / ingredient[i].recipe < m[0] || m[0] < 0) {
                    m[0] = ingredient[i].stock / ingredient[i].recipe;
                    m[1] = i;
                }
            }
        }
        return m;
    }

    /**
     * Sorts the customer's based on which direction their delivery truck/car
     * travels so when the vehicles are painted, all the vehicles driving on the
     * same lane will be painted together.
     *
     * @param arr The array of customers.
     * @param low The lowest index in the array/sub-array of customers.
     * @param high The highest index in the array/sub-array of customers.
     * @return An array of customers sorted by the direction of their delivery
     * vehicles.
     */
    public Customer[] quickSortCustomers(Customer[] arr, int low, int high) {
        int i = low;
        int j = high;

        Customer pivot = arr[low + (high - low) / 2];
        while (i <= j) {

            while (arr[i].dir > pivot.dir) {
                i++;
            }
            while (arr[j].dir < pivot.dir) {
                j--;
            }
            if (i <= j) {
                Customer n = arr[j];
                arr[j] = arr[i];
                arr[i] = n;
                i++;
                j--;
            }
        }
        if (low < j) {
            quickSortCustomers(arr, low, j);
        }
        if (i < high) {
            quickSortCustomers(arr, i, high);
        }
        return arr;
    }

}
