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

    // mensa id
    private int mensaId;

    // adapter
    private MealScheduleAdapter mealScheduleAdapter;

    private TextView txtMensaId;
    private TextView txtMensaName;
    private ListView lstMealSchedule;

    private OnFragmentInteractionListener mListener;

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
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_canteen_detail, container, false);

        txtMensaId = (TextView) layout.findViewById(R.id.txt_mensa_id);
        txtMensaName = (TextView) layout.findViewById(R.id.txt_mensa_name);
        lstMealSchedule = (ListView) layout.findViewById(R.id.list_meal_schedule);

        // get mensa id from intent extra
        //mensaId = (int)getIntent().getSerializableExtra("MENSA_ID");

        // TODO remove hard-coded mensa id
        mensaId = 50;

        // check shared preferences for a valid meal schedule
        SharedPreferences preferences = getActivity().getSharedPreferences(Config.SHARED_PREFERENCES, Context.MODE_PRIVATE);

        // TODO remove fake JSON and implement real parsing

        JSONObject demoMealSchedule = new JSONObject();

        try {
            demoMealSchedule.put("id", mensaId);
            demoMealSchedule.put("timestamp", 12345678);
            demoMealSchedule.put("datetime", "26.10.2015T08:00:00");
            demoMealSchedule.put("name", "FH JOANNEUM Kapfenberg");

            JSONArray mealSchedule = new JSONArray();

            JSONObject day1 = new JSONObject();
            day1.put("date", "26.10.2015");

            JSONObject day2 = new JSONObject();
            day2.put("date", "27.10.2015");

            JSONArray meals = new JSONArray();

            JSONObject meal1 = new JSONObject();
            meal1.put("price", 4.30);
            meal1.put("description", "Gemüsesupppe, Marillenknödel");
            meal1.put("type", "Vegetarian");

            JSONObject meal2 = new JSONObject();
            meal2.put("price", 4.90);
            meal2.put("description", "Gemüsesuppe, Wiener Schnitzel");
            meal2.put("type", "Classic");

            JSONObject meal3 = new JSONObject();
            meal3.put("price", 5.90);
            meal3.put("description", "Paprikaschnitzel mit Salzkartoffeln und Salat vom Buffet");
            meal3.put("type", "Brainfood");

            meals.put(meal1);
            meals.put(meal2);
            meals.put(meal3);

            day2.put("meals", meals);

            mealSchedule.put(day1);
            mealSchedule.put(day2);

            demoMealSchedule.put("mealSchedule", mealSchedule);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(String.valueOf(mensaId), demoMealSchedule.toString());
        editor.commit();

        String defaultString = "";
        String mealJson = preferences.getString(String.valueOf(mensaId), defaultString);

        // TODO add check if timestamp is valid

        // display meal schedule
        try {
            JSONObject mealScheduleJson = new JSONObject(mealJson);

            MealSchedule mealSchedule = parseMealScheduleJsonObject(mealScheduleJson);

            // mensa id
            txtMensaId.setText(String.valueOf(mensaId));

            // mensa name
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
                    MealScheduleItem item = new MealScheduleItem(meal);
                    items.add(item);
                }
            }

            mealScheduleAdapter = new MealScheduleAdapter(getContext(), items);
            lstMealSchedule.setAdapter(mealScheduleAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }

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

    private MealSchedule parseMealScheduleJsonObject(JSONObject mealScheduleJson) {
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
