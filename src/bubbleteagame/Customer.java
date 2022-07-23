package bubbleteagame;

import java.awt.Color;
import java.awt.Graphics;
import static java.lang.Math.abs;
import java.util.Arrays;

/**
 * The Customer class simulates a customer viewing the shop's page, and
 * purchasing and/or commenting, and also stores all the needed information to
 * display and locate a delivery car on the user interface.
 *
 * @author Sarah Simionescu
 * @version 1
 */
public class Customer extends Thread implements UIComponent {

    /**
     * Access to long term memory.
     */
    Game G;
    /**
     * Access to all information with regards to the current day of the week.
     */
    Day today;
    /**
     * Access to user interface.
     */
    Display D;
    /**
     * The customer's perfect recipe.
     */
    int[] optimalRecipe;
    /**
     * The customer's rating out of 100 on a variety of factors.
     */
    Rating[] ratings;
    /**
     * Double value representing the time at which the customer will view the
     * page.
     */
    double time;
    /**
     * Graphic of how the customer's delivery car looks.
     */
    Graphic car;
    /**
     * Graphics of bubble tea logo to be displayed on to the car.
     */
    Graphic teaLogo;
    /**
     * The name of the shop to be displayed on to the car.
     */
    Text shopLogo;
    /**
     * The direction of the delivery car.
     */
    int dir;
    /**
     * The velocity of the delivery car
     */
    double velocity;
    /**
     * Whether or not this particular customer will purchase from the shop.
     */
    boolean willPurchase;
    /**
     * Whether or not this particular customer will comment on the shop's page.
     */
    boolean willComment;
    /**
     * Whether or not this particular customer has purchased a drink.
     */
    boolean purchaseComplete;
    /**
     * Accesses MathTool class to use mathematical functions.
     */
    MathTool math;

    /**
     * The Rating class conveniently organizes and stores information regarding
     * a Customer's 0-100 rating for a particular factor.
     *
     * @author Sarah Simionescu
     * @version 1
     */
    private class Rating {

        /**
         * The name of the factor.
         */
        String name;
        /**
         * The customer's 0-100 rating for this particular factor.
         */
        int rate;
        /**
         * The index of this factor.
         */
        int index;

        /**
         * Initializes and stores information regarding a Customer's rating for
         * a particular factor.
         *
         * @param i The index of this factor.@param i The index of this factor.
         * @param n The name of this factor.
         * @param r The customer's 0-100 rating for this particular factor.
         *
         */
        public Rating(int i, String n, int r) {
            index = i;
            name = n;
            rate = r;
        }
    }

    /**
     * Initializes the Customer, randomly selects it's car's direction, randomly
     * decides whether or not the customer will comment on the page and decides
     * at what time of day the Customer will view/interact with the page.
     *
     * @param t Access to all information with regards to the current day of the
     * week.
     * @param g Access to long term memory.
     * @param d Access to user interface.
     *
     */
    public Customer(Day t, Game g, Display d) {
        G = g;
        D = d;
        math = new MathTool();
        today = t;
        ratings = new Rating[11];
        dir = (int) math.random(0, 1);
        if (dir == 0) {

            dir = -1;
        } else {
            dir = 1;
        }
        int p = (int) math.random(0, 1);
        if (p == 0) {
            willComment = true;
        } else {
            willComment = false;
        }
        velocity = 100;
        time = math.random(7, 19);
        purchaseComplete = false;
    }

    /**
     * This method updates the customer's actions once their time has passed:
     * generates their ratings, decides whether or not they will purchase,
     * animates their delivery and initiates their comment.
     */
    @Override
    public void run() {
        if (G.gameMode == 10) {
            optimalRecipe = generateOptimalRecipe();
            generateRatings();
            if ((int) math.random(0, 100) <= purchaseChance()) {
                willPurchase = true;
            } else {
                willPurchase = false;
            }
            if (willPurchase == true && today.stockCheck() == true) {
                if (dir == 1) {
                    int x = 200 - 500;
                    int y = 580;
                    car = new Graphic(x, y, 230, 230, "car1", D);
                    teaLogo = new Graphic(x - 50, y - 40, 45, 77, "bubbleTeaLogo", D);
                    shopLogo = new Text(G.shopName, x, y + 20, (int) math.map(G.shopName.length(), 1, 15, 40, 20), Color.white, new Color(0, 0, 0, 0), false, D);
                } else {
                    int x = 200 + 500;
                    int y = 630;
                    dir = -1;
                    car = new Graphic(x, y, 230, 122, "car2", D);
                    teaLogo = new Graphic(x + 55, y, 45, 77, "bubbleTeaLogo", D);
                    shopLogo = new Text(G.shopName, x - 10, y + 10, (int) math.map(G.shopName.length(), 1, 15, 30, 10), Color.white, new Color(0, 0, 0, 0),false, D);
                }
                velocity = abs(car.xpos - 200) / 10;
            } else if (willComment == true && today.stockCheck() == true) {
                today.comments.add(comment());
            }
        }
    }

    /**
     * Paints the delivery car/truck on to the user interface.
     *
     * @param g Graphics object used to create graphics.
     */
    @Override
    public void paint(Graphics g) {
        velocity = abs(car.xpos - 200) / 10;
        if (velocity < 0.5) {
            velocity = 1;
            if (purchaseComplete == false && today.stockCheck() == true) {
                    purchase();
                    if(willComment == true)
                    {
                        today.comments.add(comment());
                    }
                }
            }
            car.xpos += dir * velocity;
            if (dir == 1) {
                teaLogo.xpos = car.xpos + 50;
                shopLogo.xpos = car.xpos + 100;
            } else {
                teaLogo.xpos = car.xpos + 155;
                shopLogo.xpos = car.xpos + 118;
            }
            car.paint(g);
            teaLogo.paint(g);
            shopLogo.paint(g);
            if (dir == -1) {
                for (int i = 0; i < 2; i++) {
                    g.setColor(new Color(62, 62, 62));
                g.fillOval(car.xpos + 150 + i * -124, car.ypos + 67, 55, 55);
                g.setColor(new Color(203, 203, 203));
                g.fillOval(car.xpos + 150 + 14 + i * -124, car.ypos + 67 + 14, 28, 28);
            }
        }
    }

    /**
     * Completes the purchase for the customer by taking away stock and adding
     * profits.
     */
    public void purchase() {
        for (int i = 0; i < today.ingredient.length; i++) {
            today.ingredient[i].stock -= today.ingredient[i].recipe;
        }
        today.cupsSold += 1;
        today.revenue += today.price;
        today.money += today.price;
        purchaseComplete = true;
    }

    /**
     * Generates an optimal recipe for the particular customer based around a
     * set popular recipe.
     * 
     * @return An array of the customer's optimal recipe.
     */
    public int[] generateOptimalRecipe() {
        int[] oR = new int[today.ingredient.length];
        oR[0] = 1;
        oR[1] = math.gaussian(2, 0.25, 0, 10);
        oR[2] = math.gaussian(8, 0.25, 0, 10);
        oR[3] = math.gaussian(6, 0.25, 0, 10);
        oR[4] = math.gaussian(3, 0.25, 0, 10);
        oR[5] = math.gaussian(8, 0.25, 0, 10);
        return oR;
    }

    /**
     * Generates a 0-100 rating from the particular for a variety of factors
     * based around set popular opinions for each factor.
     */
    public void generateRatings() {
        ratings[0] = new Rating(0, "cup", 0);
        ratings[1] = new Rating(1, "tea", 0);
        ratings[2] = new Rating(2, "milk", 0);
        ratings[3] = new Rating(3, "sugar", 0);
        ratings[4] = new Rating(4, "tapioca", 0);
        ratings[5] = new Rating(5, "ice", 0);
        for(int i = 0; i <= 5; i++)
        {
            ratings[i].rate = (int)math.map(abs(today.ingredient[i].recipe - optimalRecipe[i]), 5, 0, 0, 100);
        }
        ratings[6]= new Rating(6, "recipe", 0);
        for(int i = 0; i <= 5; i++)
        {
            ratings[6].rate += ratings[i].rate;
        }
        ratings[6].rate /= 6;
        ratings[7] = new Rating(7, "price", math.gaussian(math.map(-today.price*2*(5-today.rating)+100, 0, 100, 10, 100), 2, 0, 100));
        ratings[8] = new Rating (8, "rating", math.gaussian(math.map(today.rating, 0, 5, 0, 100), 2, 0, 100));
        ratings[9] = new Rating (9, "temperature", math.gaussian(math.map(today.temperature, -10, 35, 20, 100), 2, 0, 100));
        ratings[10] = new Rating(10, "condition", 0);
        if (today.condition == 0) {
            ratings[10].rate = math.gaussian(20, 2, 0, 100);
        } else if (today.condition == 1) {
            ratings[10].rate = math.gaussian(70, 2, 0, 100);
        } else if (today.condition == 2) {
            ratings[10].rate = math.gaussian(40, 2, 0, 100);
        }else
        {
            ratings[10].rate = math.gaussian(100, 2, 0, 100);
        }   
        
    }

    /**
     * Generates a rate of purchase out of 100 based on the customer's opinions
     * for the weather, price and reputation.
     * 
     * @return A 0-100% chance the customer will purchase a drink.
     */
    public int purchaseChance() {
        int c = 0;
        for (int i = 7; i < ratings.length; i++)
        {
            c += ratings[i].rate;
        }
        return c / 4;

    }

    /**
     * Generates a comment based on whether or not the customer purchased a
     * drink and what their rating is for a particular factor.
     *
     * @return A comment "written" by the Customer.
     */
    Comment comment() {
        String comment = "";
        String[] options;
        Rating[] topics;
        Integer topic = null;
        if (willPurchase == false) {
            topics = Arrays.copyOfRange(ratings, 7, 11);
            topic = quickSortRatings(topics, 0, topics.length - 1)[math.gaussian(topics.length - 1, 1.5, 0, topics.length - 1)].index;

        } else {
            topics = Arrays.copyOfRange(ratings, 1, 8);
            topic = ratings[(int) math.random(1, topics.length - 1)].index;
            
        }
        if (topic <= 5) {
            if (today.ingredient[topic].recipe > optimalRecipe[topic]) {
                if (today.ingredient[topic].recipe - optimalRecipe[topic] < 2) {
                    options = new String[]{"Amazing, ", "Good, ", "My tea was so good, ", "So yummy, ", "Soooo good, ", "AMazing taste, ", "I loved my tea, ", "Sooo yummy, ", "So delicious, ", "So refreshing, ", "The tea is so good, ", "Tea here is amazing, ", "My drink tastes amazing, ", "I loove my drink, ", "This is so good, ", "Tea here is so delicous, ", "So delicous, ", "Litterally amazing, ", "Tastes almost perfect, ", "So close to perfection, "};
                    comment += options[(int) math.random(0, options.length - 1)];
                    options = new String[]{"but little too much ", "but it had a touch too much ", "but it has a touch too much ", "but they add a little too much ", "but I would have a bit less ", "but I want a bit less ", "but please put less ", "but I would just put a little less ", "but I want less ", "but, next time, a little less "};
                } else {
                    options = new String[]{"Too much ", "My drink had too much ", "The bubble tea here has too much ", "They add too much ", "I would have less ", "I want less ", "Please put less ", "I would have less ", "I would add less ", "Definitely too much ", "I'm gonna need less ", "Got to tone down the ", "Way to much ", "They got way too much "};
                }

            } else if (today.ingredient[topic].recipe < optimalRecipe[topic]) {
                if (optimalRecipe[topic] - today.ingredient[topic].recipe < 2) {
                    options = new String[]{"Amazing, ", "Good, ", "My tea was so good, ", "So yummy, ", "Soooo good, ", "AMazing taste, ", "I loved my tea, ", "Sooo yummy, ", "So delicious, ", "So refreshing, ", "The tea is so good, ", "Tea here is amazing, ", "My drink tastes amazing, ", "I loove my drink, ", "This is so good, ", "Tea here is so delicous, ", "So delicous, ", "Litterally amazing, ", "Tastes almost perfect, ", "So close to perfection, "};
                    comment += options[(int) math.random(0, options.length - 1)];
                    options = new String[]{"but not enough ", "but it had a touch too little ", "but it has a touch too little ", "but it lacked some ", "but not enough ", "but I want a bit more ", "but please put more ", "but I would just put a little more ", "but I want more ", "but, next time, a little more "};
                } else {
                    options = new String[]{"Not enough ", "My drink didn't have enough ", "The bubble tea here has not enough ", "They add too little of ", "I would have more ", "I want more ", "Please add more ", "This needs more ", "I would have more ", "I would add more ", "Definitely too little ", "I'm gonna need more ", "Fill up on the ", "Way too little ", "It's like they're running out of "};
                }
            } else {
                options = new String[]{"The perfect amount of ", "They add a perfect amount of ", "YES! I love just the right amount of ", "Their recipe has just the right amount of ", "Love the amount of ", "Just the right amount of ", "My bubble tea had the perfect amount of ", "Just the right amount of ", "They know how to put the exact amount of ", "They know how to put the perfect amount of ", "They know how to put the right amount of "};
            }
            comment += options[(int) math.random(0, options.length - 1)];
            comment += ratings[topic].name;
            options = new String[]{".", "!"};
            comment += options[(int) math.random(0, options.length - 1)];

        } else if (topic == 6) {
            options = new String[]{"The taste is ", "The bubble tea here is ", "My drink is ", "It tastes "};
            comment += options[(int) math.random(0, options.length - 1)];
            if (ratings[topic].rate > 70) {
                options = new String[]{"so good!", "delicous!", "yummy!", "amAzing!", "soooo yummy!", "the best bubble tea i've ever had!", "so YUM!"};
            } else if (ratings[topic].rate > 40) {
                options = new String[]{"just ok.", "average.", "like any bubble tea.", "meh.", "only decent.", "boring.", "not any better."};
            } else {
                options = new String[]{"so bad...", "gross!", "yucky!", "meh.", "pretty bad.", "the worst bubble tea i've ever had.", "kinda weird."};
            }
            comment += options[(int) math.random(0, options.length - 1)];
        } else if (topic == 7) {
            options = new String[]{"The cost is ", "The price is ", "The price for drinks here is ", "The cost of bubble tea is "};
            comment += options[(int) math.random(0, options.length - 1)];
            if (ratings[topic].rate > 70) {
                options = new String[]{"so low!", "amazing!", "so cheap!", "worth it!"};
            } else if (ratings[topic].rate > 40) {
                options = new String[]{"a little high.", "a bit too much.", "a little expensive.", "not really worth."};
            } else {
                options = new String[]{"wayy to high.", "way to much!", "soooo expensive.", "not worth it."};
            }
            comment += options[(int) math.random(0, options.length - 1)];
        } else if (topic == 8) {
            options = new String[]{"I heard this bubble tea shop is ", "I read that this place is ", "All my friends say this place is ", "Everyone is saying this shop is ", "Everyone says this place is ", "People are saying "};
            comment += options[(int) math.random(0, options.length - 1)];
            if (ratings[topic].rate > 70) {
                options = new String[]{"so good!", "the best tea shop around!", "really good!", "amAzing!", "soooo good!", "the best!", "really popular!"};
            } else if (ratings[topic].rate > 40) {
                options = new String[]{"ok.", "average.", "meh.", "pretty decent.", "fine.", "not super great.", "not that bad."};
            } else {
                options = new String[]{"so bad...", "gross!", "yucky!", "meh.", "pretty bad.", "the worst.", "pretty sucky."};
            }
            comment += options[(int) math.random(0, options.length - 1)];
        } else if (topic == 9) {
            options = new String[]{"The weather is ", "Temperatures are ", "The temperature outside is "};
            comment += options[(int) math.random(0, options.length - 1)];
            if (ratings[topic].rate > 70) {
                options = new String[]{"perfect for bubble tea!", "nice and warm!", "beautiful!", "making me want some bubble tea!", "making me crave bubble tea!", "sooo nice!"};
            } else if (ratings[topic].rate > 40) {
                options = new String[]{"ok for bubble tea.", "pretty good for some bubble tea.", "meh.", "pretty decent for bubble tea.", "fine.", "ok."};
            } else {
                options = new String[]{"so bad...", "not good for bubble tea!", "definitly not good for bubble tea!", "pretty bad.", "the worst for bubble tea.", "not for bubble tea."};
            }
            comment += options[(int) math.random(0, options.length - 1)];
        } else if (topic == 10) {
            options = new String[]{"The weather is ", "Conditions outside are ", "The way it's looking outside is ", "The way the sky looks is "};
            comment += options[(int) math.random(0, options.length - 1)];
            if (ratings[topic].rate > 70) {
                options = new String[]{"perfect for bubble tea!", "nice and warm!", "beautiful!", "making me want some bubble tea!", "making me crave bubble tea!", "sooo nice!"};
            } else if (ratings[topic].rate > 40) {
                options = new String[]{"ok for bubble tea.", "pretty good for some bubble tea.", "meh.", "pretty decent for bubble tea.", "fine.", "ok."};
            } else {
                options = new String[]{"so bad...", "not good for bubble tea.", "definitly not good for bubble tea!", "pretty bad.", "the worst for bubble tea.", "not for bubble tea."};
            }
            comment += options[(int) math.random(0, options.length - 1)];
        }
        if (willPurchase == false) {
            return new Comment(comment, null, D);
        } else {
            return new Comment(comment, (double) ratings[6].rate, D);
        }
    }

    /**
     * Copies and sorts the customer's ratings from lowest to highest using the
     * Quick Sort method.
     *
     * @param a The array of ratings.
     * @param low The lowest index in the array/sub-array of ratings.
     * @param high The highest index in the array/sub-array of ratings.
     * @return An array of the sorted ratings.
     */
    public Rating[] quickSortRatings(Rating[] a, int low, int high) {
        Rating[] arr = a.clone();
        int i = low;
        int j = high;

        Rating pivot = arr[low + (high - low) / 2];
        while (i <= j) {

            while (arr[i].rate > pivot.rate) {
                i++;
            }
            while (arr[j].rate < pivot.rate) {
                j--;
            }
            if (i <= j) {
                Rating n = arr[j];
                arr[j] = arr[i];
                arr[i] = n;
                i++;
                j--;
            }
        }
        if (low < j) {
            quickSortRatings(arr, low, j);
        }
        if (i < high) {
            quickSortRatings(arr, i, high);
        }
        return arr;
    }

    /**
     * Checks whether or not the mouse is currently on the customer's delivery
     * car.
     *
     * @return Whether or not the mouse is currently on the customer's delivery
     * car.
     */
    @Override
    public boolean checkMouse() {
        return car.checkMouse();
    }

}
