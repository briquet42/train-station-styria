package mobappdev.itm.fhj.at.train_stations.httphelper;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import mobappdev.itm.fhj.at.train_stations.interfaces.ICallBack;

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
