package itm.fhj.at.canteenapp.service;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import itm.fhj.at.canteenapp.R;
import itm.fhj.at.canteenapp.activity.MainActivity;
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

            if (favourites.size() > 0) {
                // get meal schedule
                JSONObject mealScheduleJson = null;
                try {
                    mealScheduleJson = new JSONObject(preferences.getString(Config.KEY_SCHEDULE_PREFIX + String.valueOf(canteen.getId()), ""));
                    mealSchedule = canteenHelper.parseMealScheduleJsonObject(mealScheduleJson);

                    // get meals for today
                    SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
                    String today = format.format(new Date());

                    ArrayList<Meal> mealsToday = mealSchedule.getMeals(today);

                    // build array list with meals to notify the user about
                    ArrayList<String> toNotify = new ArrayList<String>();

                    for (Meal meal : mealsToday) {
                        String favourite = checkFavouriteMeals(meal);

                        if (favourite != null && !toNotify.contains(favourite)) {
                            toNotify.add(favourite);
                        }
                    }

                    if (toNotify.size() > 0) {
                        issueNotification(toNotify);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }

    private void getFavourites() {
        String favouritesString = preferences.getString(Config.KEY_FAVOURITE_MEALS, "").trim();

        if (favouritesString.isEmpty()) {
            favourites = new ArrayList<String>();
        } else {
            String[] favouritesArray = favouritesString.split(";");

            for (String fav : favouritesArray) {
                favourites.add(fav);
            }
        }
    }

    private String checkFavouriteMeals(Meal meal) {
        for (String fav : favourites) {
            if (meal.getDescription().toLowerCase().contains(fav.toLowerCase()))
                return fav;
        }

        return null;
    }

    private void issueNotification(ArrayList<String> toNotify) {
        // build notification content text
        String text = "";

        for (String fav : toNotify) {
            if (!text.isEmpty())
                text += " und ";

            text += fav;
        }

        // define notification action
        int notificationId = 1;

        Intent resultIntent = new Intent(this, MainActivity.class);
        resultIntent.putExtra(Config.NOTIFICATION_ID, notificationId);

        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.dinner)
                .setContentTitle("Heute gibt's was Gutes!")
                .setContentText("Es gibt " + text)
                .setContentIntent(resultPendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        notificationManager.notify(notificationId, mBuilder.build());
    }
}
