package itm.fhj.at.canteenapp.model;

import java.io.Serializable;

/**
 * Created by rwachtler on 29.10.15.
 */
public class Location implements Serializable{

    private int id = 0;
    private String name = "";

    /**
     * Initializes a Location object
     * @param id Location ID, required for the menu request
     * @param name Location name
     */
    public Location(int id, String name){
        this.id = id;
        this.name = name;
    }

    /**
     * @return ID of a Location object
     */
    public int getId() {
        return id;
    }

    /**
     * @return Name of a Location object
     */
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return this.name.replaceAll("&amp;", "&");
    }
}
