package itm.fhj.at.canteenapp.activity;

import android.app.Activity;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import itm.fhj.at.canteenapp.R;
import itm.fhj.at.canteenapp.fragment.CanteenDetailFragment;


public class MainActivity extends FragmentActivity implements CanteenDetailFragment.OnFragmentInteractionListener {

    private ViewPager vpHost;
    private CanteenPagerAdapter adapter;

    private TextView fragmentTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vpHost = (ViewPager) findViewById(R.id.vpHost);

        fragmentTitle = (TextView) findViewById(R.id.fragmentTitle);

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
        adapter = new CanteenPagerAdapter(getSupportFragmentManager());
        vpHost.setAdapter(adapter);
        vpHost.setCurrentItem(1);

        vpHost.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                fragmentTitle.setText(adapter.getPageTitle(position));
            }
        });
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public class CanteenPagerAdapter extends FragmentStatePagerAdapter {

        public CanteenPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return CanteenDetailFragment.newInstance();
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Locations".toUpperCase();
                case 1:
                    return "Meal Schedule".toUpperCase();
                case 2:
                    return "Favourite Meals".toUpperCase();
                default:
                    return "X";
            }
        }
    }
}
