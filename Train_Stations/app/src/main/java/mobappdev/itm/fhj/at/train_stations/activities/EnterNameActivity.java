package mobappdev.itm.fhj.at.train_stations.activities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import mobappdev.itm.fhj.at.train_stations.httphelper.HttpHelper;
import mobappdev.itm.fhj.at.train_stations.interfaces.ICallBack;
import mobappdev.itm.fhj.at.train_stations.model.ListStations;
import mobappdev.itm.fhj.at.train_stations.R;
import mobappdev.itm.fhj.at.train_stations.model.Station;

/**
 * Created by Viktoria on 29.10.2015.
 */
public class EnterNameActivity extends Activity implements ICallBack {
    private String STATION="next railway station";
    private EditText inputStation;
    private TextView outputDisplay;
    private SharedPreferences prefs;
    private TimePicker actTime;
    private int hour, min;
    private String station="";
    private int idStation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_name);

        outputDisplay=(TextView)findViewById(R.id.txtOutput);

        inputStation=(EditText)findViewById(R.id.txtStation);
        prefs = getSharedPreferences("Station", 0);
        inputStation.setText(prefs.getString(STATION, "Kapfenberg"));

    }

    private boolean isConnectingToInternet(Context applicationContext){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
            outputDisplay.setText("No internet connection! Activate your internet.");
            return false;
        } else
            return true;

    }

    public void getDisplay(View v){
        if(isConnectingToInternet(getApplicationContext())) {
            outputDisplay.setText("");
            //read time from Timepicker
            actTime = (TimePicker) findViewById(R.id.tpActualTime);
            hour = actTime.getCurrentHour();
            min = actTime.getCurrentMinute();

            station = inputStation.getText().toString();
            ListStations listStations = new ListStations();
            //alle bahenhoefe durchgehen, bis der mit der gleichen id gefunden wird, wie der in der Eingabe ; sonst wird eine Fehlermeldung ausgegeben
            for (Station s : listStations.stations) {
                if (station.equals(s.getName())) {
                    idStation = s.getId();
                    String sUrl = "http://fahrplan.oebb.at/bin/stboard.exe/dn?L=vs_scotty.vs_liveticker&evaId=" + idStation + "&boardType=dep&time=" + hour + ":" + min + "&productsFilter=1111111111111111&additionalTime=0&disableEquivs=yes&maxJourneys=10&outputMode=tickerDataOnly&start=yes&selectDate=today\n";
                    HttpHelper helper = new HttpHelper();
                    helper.setCallback(this);
                    helper.execute(sUrl);
                    break;
                } else {
                    outputDisplay.setText("No train station found for this name");
                }
            }

        }

        //save the input as standard
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString(STATION, inputStation.getText().toString());
        edit.commit();
    }

    @Override
    public void handleJSonString(String jsonString) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString.substring(14));
            JSONArray ja = jsonObject.getJSONArray("journey");
            outputDisplay.setText("time:   number:    direction: \n");

            final int n = ja.length();
            for (int i = 0; i < n; ++i) {
                final JSONObject station = ja.getJSONObject(i);
                String train=station.getString("ti")+"  |  "+station.getString("pr")+"  |  "+station.getString("lastStop");
                this.outputDisplay.setText(outputDisplay.getText() + train + "\n");

            }



        } catch (JSONException e) {
            e.printStackTrace();
        }



    }


}
