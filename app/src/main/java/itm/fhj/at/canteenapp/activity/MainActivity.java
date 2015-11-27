package itm.fhj.at.canteenapp.activity;

import android.content.Context;
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

import itm.fhj.at.canteenapp.R;
import itm.fhj.at.canteenapp.fragment.CanteenDetailFragment;
import itm.fhj.at.canteenapp.fragment.FavouriteMealFragment;
import itm.fhj.at.canteenapp.fragment.LocationFragment;
import itm.fhj.at.canteenapp.model.Location;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vpHost = (ViewPager) findViewById(R.id.vpHost);

        preferences = getSharedPreferences(Config.SHARED_PREFERENCES, Context.MODE_PRIVATE);

        // init fragments
        locationFragment = LocationFragment.newInstance();
        canteenDetailFragment = CanteenDetailFragment.newInstance();
        favouriteMealFragment = FavouriteMealFragment.newInstance();

        preparePager();
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
}
