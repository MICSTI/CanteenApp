package itm.fhj.at.canteenapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import itm.fhj.at.canteenapp.R;
import itm.fhj.at.canteenapp.model.Meal;

/**
 * Created by michael.stifter on 05.11.2015.
 */
public class MealScheduleItem implements Item {

    private static int type = MealScheduleAdapter.TYPE_ITEM;

    private Meal meal;

    public MealScheduleItem(Meal meal) {
        this.meal = meal;
    }

    public Meal getMeal() {
        return meal;
    }

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

        TextView txtMealPrice = (TextView) view.findViewById(R.id.text_meal_price);
        txtMealPrice.setText(meal.getPrice());

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
}
