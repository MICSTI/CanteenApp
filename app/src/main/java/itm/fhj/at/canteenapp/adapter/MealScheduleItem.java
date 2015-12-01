package itm.fhj.at.canteenapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import itm.fhj.at.canteenapp.R;
import itm.fhj.at.canteenapp.model.Meal;
import itm.fhj.at.canteenapp.util.Config;

/**
 * Created by michael.stifter on 05.11.2015.
 */
public class MealScheduleItem implements Item {

    private static int type = MealScheduleAdapter.TYPE_ITEM;

    private Meal meal;

    public MealScheduleItem(Meal meal, Context context) {
        this.meal = meal;
        this.mContext = context;
    }

    public Meal getMeal() {
        return meal;
    }

    private Context mContext;

    private ArrayList<String> favourites;

    @Override
    public int getViewType() {
        return type;
    }

    @Override
    public View getView(LayoutInflater inflater, View convertView) {
        View view;

        if (convertView == null) {
            view = (View) inflater.inflate(R.layout.item_meal_schedule_day_meal, null);
        } else {
            view = convertView;
        }

        TextView txtMealDescription = (TextView) view.findViewById(R.id.text_meal_description);
        txtMealDescription.setText(meal.getDescription());

        // check if meal is a favourite meal
        if (checkFavourite(meal.getDescription())) {
            txtMealDescription.setTextColor(mContext.getResources().getColor(R.color.california));
        }

        TextView txtMealPrice = (TextView) view.findViewById(R.id.text_meal_price);
        txtMealPrice.setText(meal.getPrice() + " " + Config.CURRENCY);

        TextView txtMealType = (TextView) view.findViewById(R.id.text_meal_type);
        txtMealType.setText(meal.getType());

        return view;
    }

    @Override
    public String toString() {
        if (meal != null)
            return meal.toString();

        return "null";
    }

    public void setFavourites(ArrayList<String> favourites) {
        this.favourites = favourites;
    }

    private boolean checkFavourite(String meal) {
        if (favourites == null)
            return false;

        for (String fav : favourites) {
            if (!fav.trim().isEmpty() && meal.toLowerCase().contains(fav.toLowerCase()))
                return true;
        }

        return false;
    }
}
