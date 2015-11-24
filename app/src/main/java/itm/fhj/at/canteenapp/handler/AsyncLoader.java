package itm.fhj.at.canteenapp.handler;

import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import itm.fhj.at.canteenapp.interfaces.ICallback;

/**
 * Created by rwachtler on 29.10.15.
 */
public class AsyncLoader extends AsyncTask<String, Void, String>{

    ICallback callback;
    private URL url;


    public void setCallback(ICallback callback){
        this.callback = callback;
    }

    @Override
    protected String doInBackground(String... params) {

        try{
            url = new URL(params[0]);
        } catch (IOException e){
            e.printStackTrace();
        }

        StringBuilder stringBuilder = new StringBuilder();

        try{
            URL url = new URL(params[0]);

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch(Exception e){
            e.printStackTrace();
        }

        return stringBuilder.toString();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        callback.parseHTMLString(s);

    }

}
