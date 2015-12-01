package itm.fhj.at.canteenapp.activity;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import itm.fhj.at.canteenapp.R;
import itm.fhj.at.canteenapp.fragment.CanteenDetailFragment;
import itm.fhj.at.canteenapp.fragment.FavouriteMealFragment;
import itm.fhj.at.canteenapp.fragment.LocationFragment;
import itm.fhj.at.canteenapp.model.Location;
import itm.fhj.at.canteenapp.service.FavouriteMealService;
import itm.fhj.at.canteenapp.util.Config;


public class MainActivity extends AppCompatActivity implements
        CanteenDetailFragment.OnFragmentInteractionListener,
        FavouriteMealFragment.OnFragmentInteractionListener,
        LocationFragment.OnLocationFragmentInteractionListener {

    private ViewPager vpHost;
    private CanteenPagerAdapter adapter;

    private SharedPreferences preferences;

    private LocationFragment locationFragment;
    private CanteenDetailFragment canteenDetailFragment;
    private FavouriteMealFragment favouriteMealFragment;

    private boolean fromNotification = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // init shared preferences
        preferences = getSharedPreferences(Config.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        int savedCanteenId = preferences.getInt(Config.KEY_CANTEEN_ID, -1);
        String savedCanteenName = preferences.getString(Config.KEY_CANTEEN_NAME, null);

        if (savedCanteenId == -1) {
            editor.putInt(Config.KEY_CANTEEN_ID, Config.DEFAULT_CANTEEN_ID);
            editor.commit();
        }

        if (savedCanteenName == null) {
            editor.putString(Config.KEY_CANTEEN_NAME, Config.DEFAULT_CANTEEN_NAME);
            editor.commit();
        }

        // get intent
        Intent passedIntent = getIntent();

        if (passedIntent != null) {
            try {
                // if app was started from notification, dismiss the notification
                int notificationId = (int)passedIntent.getSerializableExtra(Config.NOTIFICATION_ID);

                if (notificationId > 0) {
                    fromNotification = true;

                    NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                    notificationManager.cancel(notificationId);
                } else
                    fromNotification = false;
            } catch (Exception e) {
                e.printStackTrace();
                fromNotification = false;
            }
        } else {
            fromNotification = false;
        }

        setContentView(R.layout.activity_main);

        vpHost = (ViewPager) findViewById(R.id.vpHost);

        // init fragments
        locationFragment = LocationFragment.newInstance();
        canteenDetailFragment = CanteenDetailFragment.newInstance();
        canteenDetailFragment.setFavourites(getFavouriteMeals());
        favouriteMealFragment = FavouriteMealFragment.newInstance();
        favouriteMealFragment.setCanteenDetailFragment(canteenDetailFragment);

        preparePager();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!fromNotification && !isMyServiceRunning(FavouriteMealService.class)) {
            // create intent with service
            Intent intent = new Intent(this, FavouriteMealService.class);

            // start favourite meal service to run every day at a specified time
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, Config.NOTIFICATION_HOUR);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);

            PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(pendingIntent);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

            Toast.makeText(this, "Favourite meal service started", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Favourite meal service already started", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void preparePager() {
        int defaultItem = -1;

        // check if a default canteen is saved in shared preferences
        int canteenId = preferences.getInt(Config.KEY_CANTEEN_ID, 0);

        if (canteenId > 0) {
            // a default canteen has already been selected
            defaultItem = 1;
        } else {
            // show canteen selection
            defaultItem = 0;
        }

        adapter = new CanteenPagerAdapter(getSupportFragmentManager());
        vpHost.setAdapter(adapter);
        vpHost.setCurrentItem(defaultItem);

        vpHost.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                setTitle(adapter.getPageTitle(position));
            }
        });

        // initially set app bar title
        setTitle(adapter.getPageTitle(defaultItem));
    }

    // CanteenDetail
    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    // Location
    @Override
    public void onLocationFragmentInteraction(Location location) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(Config.KEY_CANTEEN_ID, location.getId());

        String canteenName =  Html.fromHtml(location.getName()).toString().trim();

        if (canteenName.startsWith("- ")) {
            canteenName = canteenName.substring(2);
        }

        editor.putString(Config.KEY_CANTEEN_NAME, canteenName);
        editor.commit();

        vpHost.setCurrentItem(1);
    }

    // Favourite Meal
    @Override
    public void onFragmentInteraction(String id) {

    }

    public class CanteenPagerAdapter extends FragmentStatePagerAdapter {

        public CanteenPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                    return locationFragment;
                case 1:
                    return canteenDetailFragment;
                case 2:
                    return favouriteMealFragment;
            }

            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.title_locations);
                case 1:
                    return getString(R.string.title_meal_schedule);
                case 2:
                    return getString(R.string.title_favourite_meals);
                default:
                    return "X";
            }
        }
    }

    public ArrayList<String> getFavouriteMeals() {
        String favouritesString = preferences.getString(Config.KEY_FAVOURITE_MEALS, "");

        String[] favouritesArray = favouritesString.split(";");

        ArrayList<String> favourites = new ArrayList<String>();

        for (String fav : favouritesArray) {
            favourites.add(fav);
        }

        return favourites;
    }

    public boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }

        return false;
    }
}
