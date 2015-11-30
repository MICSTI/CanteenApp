package itm.fhj.at.canteenapp.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by michael.stifter on 04.11.2015.
 */
public class MealSchedule {
    private Location location;
    private String valid;

    private LinkedHashMap<String, ArrayList<Meal>> calendar;

    public MealSchedule() {
        this.calendar = new LinkedHashMap<>();
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setValid(String valid) {
        this.valid = valid;
    }

    public String getValid() {
        return valid;
    }

    public LinkedHashMap<String, ArrayList<Meal>> getCalendar() {
        return calendar;
    }

    public void addCalendarDay(String day) {
        if (!dayExists(day)) {
            addEmptyDay(day);
        }
    }

    public void addMeal(String day, Meal meal) {
        // make sure calendar day exists
        addCalendarDay(day);

        // add meal to this day
        this.calendar.get(day).add(meal);
    }

    public ArrayList<Meal> getMeals(String day) {
        return this.calendar.get(day);
    }

    private boolean dayExists(String day) {
        return this.calendar.containsKey(day);
    }

    private void addEmptyDay(String day) {
        this.calendar.put(day, new ArrayList<Meal>());
    }
}
