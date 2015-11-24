package itm.fhj.at.canteenapp.handler;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import itm.fhj.at.canteenapp.interfaces.ICallback;
import itm.fhj.at.canteenapp.interfaces.IParseCallback;

/**
 * Created by rwachtler on 29.10.15.
 */
public class HTMLDataHandler implements ICallback{

    private IParseCallback parseCallback;

    /**
     * Starts an asynchronous request for given URL (String)
     * @param URLString - URLString which has to be requested
     */
    public void loadHTMLStringFromURL(String URLString){
        AsyncLoader asyncLoader = new AsyncLoader();
        asyncLoader.setCallback(this);
        asyncLoader.execute(URLString);
    }


    @Override
    public void parseHTMLString(String htmlString) {
        Document doc = Jsoup.parse(htmlString);
        parseCallback.processLocationData(doc);
    }

    public void setCallback(IParseCallback callback){
        this.parseCallback = callback;
    }
}

