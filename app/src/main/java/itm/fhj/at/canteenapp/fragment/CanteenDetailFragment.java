package itm.fhj.at.canteenapp.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import itm.fhj.at.canteenapp.R;
import itm.fhj.at.canteenapp.adapter.Item;
import itm.fhj.at.canteenapp.adapter.MealScheduleAdapter;
import itm.fhj.at.canteenapp.adapter.MealScheduleHeader;
import itm.fhj.at.canteenapp.adapter.MealScheduleItem;
import itm.fhj.at.canteenapp.model.Location;
import itm.fhj.at.canteenapp.model.Meal;
import itm.fhj.at.canteenapp.model.MealSchedule;
import itm.fhj.at.canteenapp.util.CanteenHelper;
import itm.fhj.at.canteenapp.util.Config;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CanteenDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CanteenDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CanteenDetailFragment extends Fragment {

    // canteen
    private Location canteen;

    // adapter
    private MealScheduleAdapter mealScheduleAdapter;

    // helper class
    private CanteenHelper canteenHelper;

    private SharedPreferences preferences;

    private TextView txtMensaName;
    private ListView lstMealSchedule;

    private OnFragmentInteractionListener mListener;

    private ArrayList<String> favourites;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CanteenDetailFragment.
     */
    public static CanteenDetailFragment newInstance() {
        CanteenDetailFragment fragment = new CanteenDetailFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public CanteenDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // init helper class
        canteenHelper = new CanteenHelper();

        // try to get default canteen from shared preferences
        preferences = getActivity().getSharedPreferences(Config.SHARED_PREFERENCES, Context.MODE_PRIVATE);

        int canteenId = preferences.getInt(Config.KEY_CANTEEN_ID, 0);

        if (canteenId > 0) {
            String canteenName = preferences.getString(Config.KEY_CANTEEN_NAME, "");

            canteen = new Location(canteenId, canteenName);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_canteen_detail, container, false);

        txtMensaName = (TextView) layout.findViewById(R.id.txt_mensa_name);
        lstMealSchedule = (ListView) layout.findViewById(R.id.list_meal_schedule);

        updateMealScheduleList();

        return layout;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    public void setFavourites(ArrayList<String> favourites) {
        this.favourites = favourites;
    }

    public void updateMealScheduleList() {
        // update favourite meals
        favourites = getFavouriteMeals();

        JSONObject demoMealSchedule = canteenHelper.getDemoData(canteen.getId(), canteen.getName());

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Config.KEY_SCHEDULE_PREFIX + String.valueOf(canteen.getId()), demoMealSchedule.toString());
        editor.commit();

        String defaultString = "";
        String mealJson = preferences.getString(Config.KEY_SCHEDULE_PREFIX + String.valueOf(canteen.getId()), defaultString);

        // display meal schedule
        try {
            JSONObject mealScheduleJson = new JSONObject(mealJson);

            MealSchedule mealSchedule = canteenHelper.parseMealScheduleJsonObject(mealScheduleJson);

            // canteen name
            txtMensaName.setText(mealSchedule.getLocation().getName());

            // list view initialisation
            List<Item> items = new ArrayList<Item>();

            // get dates from calendar hash map
            Set dateSet = mealSchedule.getCalendar().keySet();

            Iterator iterator = dateSet.iterator();

            // iterate over all days
            while (iterator.hasNext()) {
                String day = (String)iterator.next();

                // add header
                MealScheduleHeader header = new MealScheduleHeader(day);
                items.add(header);

                // add meals for this day
                ArrayList<Meal> meals = mealSchedule.getCalendar().get(day);

                for (Meal meal : meals) {
                    MealScheduleItem item = new MealScheduleItem(meal, getContext());

                    if (favourites.size() > 0) {
                        item.setFavourites(favourites);
                    } else {
                        item.setFavourites(new ArrayList<String>());
                    }


                    items.add(item);
                }
            }

            mealScheduleAdapter = new MealScheduleAdapter(getContext(), items);
            lstMealSchedule.setAdapter(mealScheduleAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
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
}
