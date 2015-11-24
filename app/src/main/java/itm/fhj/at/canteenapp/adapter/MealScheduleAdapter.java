package itm.fhj.at.canteenapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

import itm.fhj.at.canteenapp.R;

/**
 * Created by michael.stifter on 04.11.2015.
 */
public class MealScheduleAdapter extends ArrayAdapter<Item> {

    public static final int TYPE_ITEM = 0;
    public static final int TYPE_HEADER = 1;

    private List<Item> mData;

    private LayoutInflater mInflater;

    public MealScheduleAdapter(Context context, List<Item> items) {
        super(context, 0, items);
        mInflater = LayoutInflater.from(context);
        mData = items;
    }

    public void addItem(Item item) {
        mData.add(item);
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getViewType();
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Item getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        int rowType = getItemViewType(position);

        if (convertView == null) {
            holder = new ViewHolder();

            switch (rowType) {
                case TYPE_ITEM:
                    convertView = mInflater.inflate(R.layout.item_meal_schedule_day_meal, null);
                    holder.view = getItem(position).getView(mInflater, convertView);

                    break;

                case TYPE_HEADER:
                    convertView = mInflater.inflate(R.layout.item_meal_schedule_day_head, null);
                    holder.view = getItem(position).getView(mInflater, convertView);

                    break;
            }

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        return convertView;
    }

    public static class ViewHolder {
        public View view;
    }
}
