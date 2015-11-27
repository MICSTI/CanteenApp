package itm.fhj.at.canteenapp.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import itm.fhj.at.canteenapp.R;
import itm.fhj.at.canteenapp.util.Config;

public class AddFavouriteMealFragment extends DialogFragment {

    private SharedPreferences preferences;

    private DialogFinishedListener listener;

    public AddFavouriteMealFragment() {

    }

    public void setPreferences(SharedPreferences preferences) {
        this.preferences = preferences;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        final View layout = inflater.inflate(R.layout.dialog_add_favourite_meal, null);

        builder.setView(layout)
               .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       // save favourite meal in shared preferences
                       EditText mealNameElement = (EditText) layout.findViewById(R.id.favourite_meal_name);

                       String mealName = mealNameElement.getText().toString().trim();

                       // check if entered text is not empty
                       if (!mealName.isEmpty()) {
                           String favourites = preferences.getString(Config.KEY_FAVOURITE_MEALS, "");

                           if (favourites.isEmpty())
                               favourites = mealName;
                           else
                               favourites += ";" + mealName;

                           SharedPreferences.Editor editor = preferences.edit();
                           editor.putString(Config.KEY_FAVOURITE_MEALS, favourites);
                           boolean success = editor.commit();

                           String message = success ? "Favourite meal successfully saved" : "Favourite meal could not be saved";
                           Toast toast = Toast.makeText(getContext(), message, Toast.LENGTH_SHORT);
                           toast.show();

                           listener.onDialogFinished(mealName);
                       }
                   }
               })
               .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       // User cancelled the dialog
                       AddFavouriteMealFragment.this.getDialog().cancel();
                   }
               });

        // Create the AlertDialog object and return it
        return builder.create();
    }

    public void setListener(DialogFinishedListener listener) {
        this.listener = listener;
    }

    public interface DialogFinishedListener {
        void onDialogFinished(String text);
    }

}
