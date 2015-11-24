package itm.fhj.at.canteenapp.interfaces;

/**
 * Created by rwachtler on 29.10.15.
 */
public interface ICallback {

    /**
     * Parses the given HTML String
     * @param htmlString - HTML String retrieved from a URL
     * @return Document object (JSoup) for further processing
     */
    public void parseHTMLString(String htmlString);
}
