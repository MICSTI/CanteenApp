package itm.fhj.at.canteenapp.util;

import android.app.ActivityManager;
import android.util.Log;

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

            // valid
            mealSchedule.setValid(mealScheduleJson.getString("valid"));

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

    public JSONObject getDemoData(int canteenId, String canteenName) {
        JSONObject demoMealSchedule = new JSONObject();

        try {
            demoMealSchedule.put("id", canteenId);
            demoMealSchedule.put("valid", "06.12.2015 23:59:59");
            demoMealSchedule.put("name", canteenName);

            JSONArray mealSchedule = new JSONArray();

            /*JSONObject day1 = new JSONObject();
            day1.put("date", "30.11.2015");

            JSONArray meals = new JSONArray();

            JSONObject meal1 = new JSONObject();
            meal1.put("price", "4.30");
            meal1.put("description", "Legierte Grießsuppe, Riesenrösti mit Gemüse und Tomaten überbacken dazu grüner Salat");
            meal1.put("type", "Vegetarian, Brainfood");

            JSONObject meal2 = new JSONObject();
            meal2.put("price", "4.90");
            meal2.put("description", "Legierte Grießsuppe, Hühnerragout mit Gemüse im Curryreisring und Salat");
            meal2.put("type", "Brainfood");

            JSONObject meal3 = new JSONObject();
            meal3.put("price", "5.90");
            meal3.put("description", "Wildlachs pochiert mit Dillkartoffeln und Salat vom Buffet");
            meal3.put("type", "Brainfood");

            meals.put(meal1);
            meals.put(meal2);
            meals.put(meal3);

            day1.put("meals", meals);

            JSONObject day2 = new JSONObject();
            day2.put("date", "01.12.2015");

            JSONArray meals2 = new JSONArray();

            JSONObject meal21 = new JSONObject();
            meal21.put("price", "4.30");
            meal21.put("description", "Karfiolcremesuppe, Hirse-Kartoffellaibchen dazu Blattsalat");
            meal21.put("type", "Vegetarian, Brainfood");

            JSONObject meal22 = new JSONObject();
            meal22.put("price", "4.90");
            meal22.put("description", "Karfiolcremesuppe, Grammelknödel auf Chinakohl-Paprika Gemüse");
            meal22.put("type", "Classic");

            JSONObject meal23 = new JSONObject();
            meal23.put("price", "5.90");
            meal23.put("description", "Schweinskotelette 'Capresiano' dazu cremige Polenta und Salat vom Buffet");
            meal23.put("type", "Brainfood");

            meals2.put(meal21);
            meals2.put(meal22);
            meals2.put(meal23);

            day2.put("meals", meals2);

            JSONObject day3 = new JSONObject();
            day3.put("date", "02.12.2015");

            JSONArray meals3 = new JSONArray();

            JSONObject meal31 = new JSONObject();
            meal31.put("price", "4.30");
            meal31.put("description", "Backerbsensuppe, Gemüsespätzle mit kleinem Salat");
            meal31.put("type", "Vegetarian");

            JSONObject meal32 = new JSONObject();
            meal32.put("price", "4.90");
            meal32.put("description", "Backerbsensuppe, Wiener Schnitzel vom Schwein mit Bratkartoffeln und Salat vom Buffet");
            meal32.put("type", "Classic");

            JSONObject meal33 = new JSONObject();
            meal33.put("price", "5.90");
            meal33.put("description", "Pikant gefülltes Hühnerfilet auf Kräuternudeln dazu Grillgemüse");
            meal33.put("type", "Brainfood");

            meals3.put(meal31);
            meals3.put(meal32);
            meals3.put(meal33);

            day3.put("meals", meals3);*/

            JSONObject day4 = new JSONObject();
            day4.put("date", "03.12.2015");

            JSONArray meals4 = new JSONArray();

            JSONObject meal41 = new JSONObject();
            meal41.put("price", "4.30");
            meal41.put("description", "Zucchinicremesuppe, Marillenknödel mit Butterbröseln");
            meal41.put("type", "Vegetarian");

            JSONObject meal42 = new JSONObject();
            meal42.put("price", "4.90");
            meal42.put("description", "Zucchinicremesuppe, Faschierter Braten mit Kartoffelpüree und Buttergemüse");
            meal42.put("type", "Classic");

            JSONObject meal43 = new JSONObject();
            meal43.put("price", "5.90");
            meal43.put("description", "Pute natur in Erdnusssauce dazu Hirse und Salat vom Buffet");
            meal43.put("type", "Brainfood");

            meals4.put(meal41);
            meals4.put(meal42);
            meals4.put(meal43);

            day4.put("meals", meals4);

            JSONObject day5 = new JSONObject();
            day5.put("date", "04.12.2015");

            JSONArray meals5 = new JSONArray();

            JSONObject meal51 = new JSONObject();
            meal51.put("price", "4.30");
            meal51.put("description", "Klare Gemüsesuppe mit Reibteig, Kartoffel-Wurstgulasch mit Semmel und Salat");
            meal51.put("type", "Classic");

            JSONObject meal52 = new JSONObject();
            meal52.put("price", "4.90");
            meal52.put("description", "Klare Gemüsesuppe mit Reibteig, Jungschweinsbraten mit Kartoffelschmarrn und Sauerkraut");
            meal52.put("type", "Classic");

            JSONObject meal53 = new JSONObject();
            meal53.put("price", "5.90");
            meal53.put("description", "Gegrillter Fischteller auf Zucchinireis dazu gemischter Reis");
            meal53.put("type", "Brainfood");

            meals5.put(meal51);
            meals5.put(meal52);
            meals5.put(meal53);

            day5.put("meals", meals5);

            /*mealSchedule.put(day1);
            mealSchedule.put(day2);
            mealSchedule.put(day3);*/
            mealSchedule.put(day4);
            mealSchedule.put(day5);

            demoMealSchedule.put("mealSchedule", mealSchedule);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return demoMealSchedule;
    }
}
