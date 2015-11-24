package itm.fhj.at.canteenapp.model;

/**
 * Created by rwachtler on 04.11.15.
 */
public class Meal {
    String price = "";
    String description = "";
    String type = "";

    /**
     * Initializes a Meal object (case: all params are parsed from website)
     * @param price - Price of the meal in EURO
     * @param description - Textual description (e.g. Tomato soup, Schnitzel with rice)
     * @param type - Vegetarian, Classic or Brainfood
     */
    public Meal(String price, String description, String type){
        this.price = price;
        this.description = description;
        this.type = type;
    }

    /**
     * Initializes a Meal object (case: only price and description are parsed from website)
     * @param price - Price of the meal in EURO
     * @param description - Textual description (e.g. Tomato soup, Schnitzel with rice)
     */
    public Meal(String price, String description){
        this.price = price;
        this.description = description;
    }

    /**
     * Get the meal price
     * @return price in EURO
     */
    public String getPrice() {
        return price;
    }

    /**
     * Get the meal description
     * @return (e.g. Tomato soup, Schnitzel with rice)
     */
    public String getDescription() {
        return description;
    }

    /**
     * Get the meal type
     * @return Vegetarian, Classic or Brainfood
     */
    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return description + ", " + type + ", " + price;
    }
}
