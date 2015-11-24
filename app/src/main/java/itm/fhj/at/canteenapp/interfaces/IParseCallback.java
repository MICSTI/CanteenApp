package itm.fhj.at.canteenapp.interfaces;

import org.jsoup.nodes.Document;

/**
 * Created by rwachtler on 30.10.15.
 */
public interface IParseCallback {

    /**
     * Acts as a processing method for the retrieved HTML
     * to initialize further location data retrieval
     * @param htmlDocument - HTML as JSoup Document object
     */
    public void processLocationData(Document htmlDocument);
}
