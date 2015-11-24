package itm.fhj.at.canteenapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import itm.fhj.at.canteenapp.R;

/**
 * Created by michael.stifter on 05.11.2015.
 */
public class MealScheduleHeader implements Item {

    private static int type = MealScheduleAdapter.TYPE_HEADER;

    private String text;

    public MealScheduleHeader(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    @Override
    public int getViewType() {
        return type;
    }

    @Override
    public View getView(LayoutInflater inflater, View convertView) {
        View view;

        if (convertView == null) {
            view = (View) inflater.inflate(R.layout.item_meal_schedule_day_head, null);
        } else {
            view = convertView;
        }

        TextView txtDay = (TextView) view.findViewById(R.id.text_day);
        txtDay.setText(text);

        return view;
    }

    @Override
    public String toString() {
        if (text != null)
            return text;

        return "null";
    }
}
