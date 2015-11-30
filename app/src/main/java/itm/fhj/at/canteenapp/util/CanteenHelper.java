package itm.fhj.at.canteenapp.util;

import android.app.ActivityManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import itm.fhj.at.canteenapp.model.Location;
import itm.fhj.at.canteenapp.model.Meal;
import itm.fhj.at.canteenapp.model.MealSchedule;

/**
 * Created by michael.stifter on 27.11.2015.
 */
public class CanteenHelper {

    public CanteenHelper() {

    }

    public MealSchedule parseMealScheduleJsonObject(JSONObject mealScheduleJson) {
        MealSchedule mealSchedule = new MealSchedule();

        try {
            // location
            int id = mealScheduleJson.getInt("id");
            String name = mealScheduleJson.getString("name");

            Location location = new Location(id, name);
            mealSchedule.setLocation(location);

            // timestamp
            mealSchedule.setTimestamp(mealScheduleJson.getLong("timestamp"));

            // meal schedule array
            JSONArray mealScheduleArray = mealScheduleJson.getJSONArray("mealSchedule");
            int mealScheduleArrayLength = mealScheduleArray.length();

            for (int i = 0; i < mealScheduleArrayLength; i++) {
                JSONObject day = (JSONObject) mealScheduleArray.get(i);

                String date = day.getString("date");
                JSONArray meals = day.optJSONArray("meals");

                if (meals != null) {
                    int mealsLength = meals.length();

                    for (int j = 0; j < mealsLength; j++) {
                        JSONObject mealObject = (JSONObject) meals.get(j);

                        String price = mealObject.getString("price");
                        String description = mealObject.getString("description");
                        String type = mealObject.getString("type");

                        mealSchedule.addMeal(date, new Meal(price, description, type));
                    }
                } else {
                    // add an empty day to the calendar
                    mealSchedule.addCalendarDay(date);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return mealSchedule;
    }
}
