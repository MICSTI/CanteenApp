package itm.fhj.at.canteenapp.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import itm.fhj.at.canteenapp.model.Location;
import itm.fhj.at.canteenapp.model.Meal;
import itm.fhj.at.canteenapp.model.MealSchedule;
import itm.fhj.at.canteenapp.util.CanteenHelper;
import itm.fhj.at.canteenapp.util.Config;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class FavouriteMealService extends IntentService {

    private SharedPreferences preferences;

    private ArrayList<String> favourites;

    private CanteenHelper canteenHelper;

    private MealSchedule mealSchedule;

    private Location canteen;

    public FavouriteMealService() {
        super("FavouriteMealService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // get shared preferences
        preferences = getSharedPreferences(Config.SHARED_PREFERENCES, Context.MODE_PRIVATE);

        // init helper
        canteenHelper = new CanteenHelper();

        // init favourites
        favourites = new ArrayList<String>();

        // get default canteen
        int canteenId = preferences.getInt(Config.KEY_CANTEEN_ID, 0);

        if (canteenId > 0) {
            canteen = new Location(canteenId, preferences.getString(Config.KEY_CANTEEN_NAME, ""));

            // get favourites from shared preferences
            getFavourites();

            // get meal schedule
            JSONObject mealScheduleJson = null;
            try {
                mealScheduleJson = new JSONObject(preferences.getString(Config.KEY_SCHEDULE_PREFIX + String.valueOf(canteen.getId()), ""));
                mealSchedule = canteenHelper.parseMealScheduleJsonObject(mealScheduleJson);

                // get meals for today
                ArrayList<Meal> mealsToday = mealSchedule.getMeals("27.10.2015");

                // build array list with meals to notify the user about
                ArrayList<Meal> toNotify = new ArrayList<Meal>();

                for (Meal meal : mealsToday) {
                    if (checkFavouriteMeals(meal)) {
                        toNotify.add(meal);
                    }
                }

                if (toNotify.size() > 0) {
                    // issue notification
                    String favs = "";

                    for (Meal m : toNotify) {
                        favs += m.getDescription() + " / ";
                    }

                    Toast.makeText(this, favs, Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }

    private void getFavourites() {
        String favouritesString = preferences.getString(Config.KEY_FAVOURITE_MEALS, "");

        String[] favouritesArray = favouritesString.split(";");

        for (String fav : favouritesArray) {
            favourites.add(fav);
        }
    }

    private boolean checkFavouriteMeals(Meal meal) {
        for (String fav : favourites) {
            if (meal.getDescription().toLowerCase().contains(fav.toLowerCase()))
                return true;
        }

        return false;
    }
}
