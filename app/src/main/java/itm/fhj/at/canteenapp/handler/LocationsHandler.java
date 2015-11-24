package itm.fhj.at.canteenapp.handler;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

import itm.fhj.at.canteenapp.model.Location;

/**
 * Created by rwachtler on 29.10.15.
 */

public class LocationsHandler {

    private Document htmlDocument;
    private ArrayList<Location> locationList = new ArrayList<Location>();

    /**
     * Initializes the LocationHandler
     * @param doc Document object (JSoup) - required for further processing
     */
    public LocationsHandler(Document doc){
        this.htmlDocument = doc;
    }

    /**
     * Parse the locations from a Document object (JSoup)
     */
    private void parseLocations(){
        Element locationSelector = this.htmlDocument.getElementById("locations");
        Elements locations = locationSelector.getElementsByTag("option");

        for(Element location : locations){
            int locationID = Integer.parseInt(location.attr("value"));
            if(locationID >= 0){
                Location l = new Location(locationID, location.childNode(0).toString());
                locationList.add(l);
            }
        }
    }

    /**
     * Calls the parseLocations() method
     * @return populated list of Mensa locations
     */
    public ArrayList<Location> getLocations() {
        parseLocations();
        return locationList;
    }


}
