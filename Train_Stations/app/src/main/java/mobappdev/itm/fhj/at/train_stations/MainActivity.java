package mobappdev.itm.fhj.at.train_stations;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Viktoria on 28.10.2015.
 */
public class MainActivity extends Activity implements ICallBack, ICallBackDistance{
    private LocationManager lm;
    private LocationListener ls;
    private TextView txtlongitude;
    private TextView txtlatitude;
    private TextView txtOutput;
    private TextView txtCity;
    private Button butshowPos;
    private TimePicker tpActualTime;
    private int hour, min;
    private  HttpHelperDistance helperDistance;
    private int value;
    private ListStations listStations;
    private String cityName="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtlongitude=(TextView) findViewById(R.id.txtLong);
        butshowPos=(Button) findViewById(R.id.butGPS);
        txtlatitude=(TextView) findViewById(R.id.txtLati);
        txtOutput=(TextView) findViewById(R.id.txtOutput);
        txtCity=(TextView) findViewById(R.id.txtCityOutput);
      //txtCity.setVisibility(View.INVISIBLE);
        /*Testzwecke*/
        txtlatitude.setText("47.423189");
        txtlongitude.setText("15.271334");

    }

    //gets the current position by GPS (allow it in the manifest file)
    public void getPosition(View view){
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        ls = new GPS_Position(txtlatitude,txtlongitude);
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, ls);

        if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (GPS_Position.latitude >0 && GPS_Position.longitude > 0) {
                txtlatitude.setText(GPS_Position.latitude + "");
                txtlongitude.setText(GPS_Position.longitude + "");
            }else{
                txtlongitude.setText("GPS not ready yet");
            }
        } else {
            txtlongitude.setText("GPS is not turned on...");
        }

    }

    private boolean isConnectingToInternet(Context applicationContext){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
            txtOutput.setText("No internet connection! Activate your internet.");
            return false;
        } else
            return true;

    }

    public void getNextStation(View v){

        if (isConnectingToInternet(getApplicationContext())){
            if(txtlatitude.getText().equals("") || txtlongitude.getText().equals("")){
                txtOutput.setText("No position found! Calculate your position.");
            }else {
                value=1000000;
                //reads the current time form the timepicker
                tpActualTime = (TimePicker) findViewById(R.id.tpActualTimeCalc);
                hour = tpActualTime.getCurrentHour();
                min = tpActualTime.getCurrentMinute();

                txtOutput.setText("");
                txtCity.setText("");
                //is invisible because it iterats the citys with lower costs
               // txtCity.setVisibility(View.INVISIBLE);

                String cityLat = txtlatitude.getText().toString();
                String cityLon = txtlongitude.getText().toString();

                //Mit for Schleife Liste der Bahnhoefe durchgehen und dabei die latiduen und longituden vom aktuellen Standort beziwhungsweise allen Bahnoefe durchgehen
                listStations = new ListStations();
                for (Station s : listStations.stations) {
                    String nextStatURL = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=" + cityLat + "," + cityLon + "&destinations=" + s.getLat() + "," + s.getLon();
                    helperDistance = new HttpHelperDistance();
                    helperDistance.setCallback(this);
                    helperDistance.execute(nextStatURL);
                    txtCity.setText(s.getName());

                }
            }}


    }

    public void printDepartureTable(View v)
    {
        if(isConnectingToInternet(getApplicationContext())){
            if(txtCity.getText().equals("")){
                txtOutput.setText("No train station found at the moment! Please calculate the next one.");
            }else {
                txtCity.setVisibility(View.VISIBLE);
                String nearestStation = txtCity.getText().toString();
                double idStation = 0;
                for (Station s : listStations.stations) {
                    if (s.getName().equals(nearestStation)) {
                        idStation = s.getId();
                    }
                }
                //mit der id, die gleich der des naehsten Bahnhofes ist, sowie der Zeit aus dem Timepicker den Stationsplan abfragen
                String sUrl = "http://fahrplan.oebb.at/bin/stboard.exe/dn?L=vs_scotty.vs_liveticker&evaId=" + idStation + "&boardType=dep&time=" + hour + ":" + min + "&productsFilter=1111111111111111&additionalTime=0&disableEquivs=yes&maxJourneys=10&outputMode=tickerDataOnly&start=yes&selectDate=today\n";
                HttpHelper helper = new HttpHelper();
                helper.setCallback(this);
                helper.execute(sUrl);
        }
        }
    }

    @Override
    public void handleJSonString(String jsonString) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString.substring(14));
            JSONArray ja = jsonObject.getJSONArray("journey");

            final int n = ja.length();
            txtOutput.setText("time:   number:    direction: \n");
            for (int i = 0; i < n; ++i) {
                final JSONObject station = ja.getJSONObject(i);
                String train=station.getString("ti")+"  |  "+station.getString("pr")+"  |  "+station.getString("lastStop");
                this.txtOutput.setText(txtOutput.getText() + train + "\n");
            }



        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleJSonStringDistance(String jsonString) {

        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray rows=jsonObject.getJSONArray("rows");

            JSONArray elements = rows.getJSONObject(0).getJSONArray("elements");
            int distanceValue = elements.getJSONObject(0).getJSONObject("distance").getInt("value");
            //die mimimale Distanz mittels while Schleife ermitteln. Dann den Namen der Stadt aus dem JSON  herausfiltern
            while(value>distanceValue){
                value=distanceValue;
                String nearest=jsonObject.getString("destination_addresses");
                String[] parts = nearest.split(",");
                String plz_citys=parts[1];
                cityName=plz_citys.substring(6);
                //wenn eine Stadt mit mehr Namen vorkommt wird auf den Straﬂennamen geschaut
                if(cityName.equals("Kapfenberg")){
                    String streets=parts[0];
                    String street=streets.substring(2,5);
                    Log.i("!!!!!",street);
                    if(street.equals("Bah")){
                        txtCity.setText("Kapfenberg");
                    }else{
                        txtCity.setText("Kapfenberg Fachhochschule");
                    }
                }else{
                    txtCity.setText(cityName);
                }
            }
            Toast.makeText(this,"Fertig",Toast.LENGTH_SHORT).show();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
