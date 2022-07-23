package bubbleteagame;

/**
 * The Ingredient class stores, manages and generates all the needed information
 * with regards to a particular ingredient in the recipe.
 *
 * @author Sarah Simionescu
 * @version 1
 */
public class Ingredient {
    /**
     * The index of the ingredient.
     */
    int index;
    /**
     * The name of the ingredient.
     */
    String name;
    /**
     * The unit the ingredient is measured in.
     */
    String unit;
    /**
     * The quantities and related costs for which you can purchase this
     * ingredient in the store.
     */
    double[][] store;
    /**
     * How much of this ingredient is in stock.
     */
    int stock;
    /**
     * How much of this ingredient is in the recipe.
     */
    int recipe;
    /**
     * How does this ingredient run out, if it runs out at all (expires, melts).
     */
    String stockOut;

    /**
     * Initializes an ingredient.
     *
     * @param i The index of the ingredient.
     * @param n The name of the ingredient.
     * @param u The unit the ingredient is measured in.
     * @param p1 The first price available in the store for this ingredient.
     * @param q1 The corresponding quantity for the first price available in the
     * store for this ingredient.
     * @param p2 The second price available in the store for this ingredient.
     * @param q2 The corresponding quantity for the second price available in
     * the store for this ingredient.
     * @param p3 The third price available in the store for this ingredient.
     * @param q3 The corresponding quantity for the third price available in the
     * store for this ingredient.
     * @param s How much of this ingredient is in stock.
     * @param r How does this ingredient run out, if it runs out at all
     * (expires, melts).
     * @param e How does this ingredient run out, if it runs out at all
     * (expires, melts).
     */
    public Ingredient(int i, String n, String u, double p1, double q1, double p2, double q2, double p3, double q3, int s, int r, String e) {
        index = i;
        name = n;
        unit = u;
        store = new double[3][2];
        store[0][0] = p1;
        store[0][1] = q1;
        store[1][0] = p2;
        store[1][1] = q2;
        store[2][0] = p3;
        store[2][1] = q3;
        stock = s;
        recipe = r;
        stockOut = e;
    }
    
    /**
     * Creates text describing how many of the ingredient are in the recipe.
     * @return A string describing how many of the ingredient are in the recipe.
     */
    public String displayRecipe(){

        String display = recipe + " " + unit;
        if (recipe != 1 && unit == "cup") {
            display += "s";
        }
        return display;
    }
    
    /**
     * Creates text describing how many of the ingredient are in stock.
     * @return A string describing how many of the ingredient are in stock.
     */
    public String displayStock() {
        String display = stock + " " + unit;
        if (stock != 1 && unit == "cup") {
            display += "s";
        }
        return display;
    }
    
    /**
     * Creates text describing the quantity of the ingredient available for purchase.
     * @return A string describing the quantity of the ingredient available for purchase.
     */
    public String displayStoreUnit() {
        String display;
        if (stock != 1 && unit == "cup") {
            display = unit + "s";
        } else {
            display = unit;
        }
        return display;
    }

}
