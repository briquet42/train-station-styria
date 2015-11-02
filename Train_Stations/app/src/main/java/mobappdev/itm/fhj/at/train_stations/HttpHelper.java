package mobappdev.itm.fhj.at.train_stations;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Viktoria on 02.11.2015.
 */
public class HttpHelper extends AsyncTask<String, Void, String> {
    // add callback interface
    ICallBack callback;

    // setter for callback
    public void setCallback(ICallBack callback){

        this.callback = callback;
    }





    @Override
    protected String doInBackground(String... params) {

        // create Http Client & Co

        StringBuilder out = new StringBuilder();
        try {
            //for Testing: Hardcoded URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q=kapfenberg");

            // get the string parameter from execute()
            URL url = new URL(params[0]);

            // creat Urlconnection
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            // read inputstrem
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = reader.readLine()) != null) {
                out.append(line);
            }
            Log.i("INTERNET", out.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return out.toString(); // return of do in background method is input paramet od onpostexecude method
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);


        callback.handleJSonString(s);
    }


}
