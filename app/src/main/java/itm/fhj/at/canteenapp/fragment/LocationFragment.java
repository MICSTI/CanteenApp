package itm.fhj.at.canteenapp.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import org.jsoup.nodes.Document;

import java.util.ArrayList;

import itm.fhj.at.canteenapp.R;

import itm.fhj.at.canteenapp.fragment.dummy.DummyContent;
import itm.fhj.at.canteenapp.handler.HTMLDataHandler;
import itm.fhj.at.canteenapp.handler.LocationsHandler;
import itm.fhj.at.canteenapp.interfaces.IParseCallback;
import itm.fhj.at.canteenapp.model.Location;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * Activities containing this fragment MUST implement the {@link itm.fhj.at.canteenapp.fragment.LocationFragment.OnLocationFragmentInteractionListener}
 * interface.
 */
public class LocationFragment extends Fragment implements AbsListView.OnItemClickListener, IParseCallback {

    private OnLocationFragmentInteractionListener mListener;

    private ArrayList<Location> retrievedLocations = new ArrayList<Location>();

    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private ListAdapter mAdapter;

    // TODO: Rename and change types of parameters
    public static LocationFragment newInstance() {
        LocationFragment fragment = new LocationFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public LocationFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load locations
        /*HTMLDataHandler dataHandler = new HTMLDataHandler();
        dataHandler.setCallback(this);
        dataHandler.loadHTMLStringFromURL("http://www.mensen.at");*/

        // TODO: Change Adapter to display your content
        /*mAdapter = new ArrayAdapter<DummyContent.DummyItem>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, DummyContent.ITEMS);*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_location, container, false);

        // Set the adapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnLocationFragmentInteractionListener) activity;
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


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            mListener.onLocationFragmentInteraction(DummyContent.ITEMS.get(position).id);
        }
    }

    /**
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();

        if (emptyView instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }

    @Override
    public void processLocationData(Document locationData) {
        LocationsHandler locationsHandler = new LocationsHandler(locationData);
        this.retrievedLocations = locationsHandler.getLocations();

        mAdapter = new ArrayAdapter<Location>(getActivity(), android.R.layout.simple_list_item_1, this.retrievedLocations);
        ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);
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
    public interface OnLocationFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onLocationFragmentInteraction(String id);
    }
}
