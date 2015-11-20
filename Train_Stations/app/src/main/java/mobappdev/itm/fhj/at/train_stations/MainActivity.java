package mobappdev.itm.fhj.at.train_stations;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;


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
    private int value=1000000;
    private ListStations listStations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtlongitude=(TextView) findViewById(R.id.txtLong);
        butshowPos=(Button) findViewById(R.id.butGPS);
        txtlatitude=(TextView) findViewById(R.id.txtLati);
        txtOutput=(TextView) findViewById(R.id.txtOutput);
        txtCity=(TextView) findViewById(R.id.txtCityOutput);

    }

    public void getPosition(View view){
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        ls = new GPS_Position();
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

    public void getNextStation(View v){
      /*  if(txtlatitude.getText()==null && txtlongitude.getText()==null){ //Geht noch nicht
            txtOutput.setText("No position found! Calculate your position.");
        }*/
            txtlatitude.setText("47.47947807");
            txtlongitude.setText("15.495085015");
            tpActualTime=(TimePicker) findViewById(R.id.tpActualTimeCalc);
            txtOutput.setText("");
            txtCity.setText("");
            hour=tpActualTime.getCurrentHour();
            min=tpActualTime.getCurrentMinute();

            //Mit for Schleife Liste der Bahnhöfe durchgehen und den nähseten suchen mit max-Variable, dann in den in andere URL einsetzen
           listStations =new ListStations();
            for(Station s:listStations.stations) {
                String nextStatURL = "https://maps.googleapis.com/maps/api/distancematrix/json?origins="+ txtlatitude.getText() +","+ txtlongitude.getText() + "&destinations="+s.getLat()+","+s.getLon();
                helperDistance=new HttpHelperDistance();
                helperDistance.setCallback(this);
                helperDistance.execute(nextStatURL);
                txtCity.setText(s.getName());
            }


        String nearestStation=txtCity.getText().toString();
        double idStation=0;
        for(Station s:listStations.stations){
            if(s.getName()==nearestStation){
                idStation=s.getId();
            }
        }
        String sUrl = "http://fahrplan.oebb.at/bin/stboard.exe/dn?L=vs_scotty.vs_liveticker&evaId="+idStation+"&boardType=dep&time="+hour+":"+min+"&productsFilter=1111111111111111&additionalTime=0&disableEquivs=yes&maxJourneys=10&outputMode=tickerDataOnly&start=yes&selectDate=today\n";
        HttpHelper helper = new HttpHelper();
        helper.setCallback(this);
        helper.execute(sUrl);
    }



    @Override
    public void handleJSonString(String jsonString) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString.substring(14));
            JSONArray ja = jsonObject.getJSONArray("journey");

            final int n = ja.length();
            for (int i = 0; i < n; ++i) {
                final JSONObject station = ja.getJSONObject(i);
                String train=station.getString("ti")+"; "+station.getString("pr")+"; "+station.getString("lastStop");
                this.txtOutput.setText(txtOutput.getText() + train + "\n");
                System.out.println(station.getString("ti"));
                System.out.println(station.getString("pr"));
                System.out.println(station.getString("lastStop"));
                //TODO Verspaetung hinzufuegen
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
            while(value>distanceValue){
                Log.i("!!!!!!!!!!!!!!!!!!!", distanceValue+"");
                value=distanceValue;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
