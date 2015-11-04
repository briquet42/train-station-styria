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


public class MainActivity extends Activity implements ICallBack{
    private LocationManager lm;
    private LocationListener ls;
    private TextView txtlongitude;
    private TextView txtlatitude;
    private TextView txtOutput;
    private Button butshowPos;
    private TimePicker tpActualTime;
    private int hour, min;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtlongitude=(TextView) findViewById(R.id.txtLong);
        butshowPos=(Button) findViewById(R.id.butGPS);
        txtlatitude=(TextView) findViewById(R.id.txtLati);
        txtOutput=(TextView) findViewById(R.id.txtOutput);

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
            tpActualTime=(TimePicker) findViewById(R.id.tpActualTimeCalc);
            txtOutput.setText("");
            hour=tpActualTime.getCurrentHour();
            min=tpActualTime.getCurrentMinute();
            String sUrl = "http://fahrplan.oebb.at/bin/stboard.exe/dn?L=vs_scotty.vs_liveticker&evaId=8100031&boardType=dep&time="+hour+":"+min+"&productsFilter=1111111111111111&additionalTime=0&disableEquivs=yes&maxJourneys=10&outputMode=tickerDataOnly&start=yes&selectDate=today\n";
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
                final JSONObject person = ja.getJSONObject(i);
                String train=person.getString("ti")+"; "+person.getString("pr")+"; "+person.getString("lastStop");
                this.txtOutput.setText(txtOutput.getText()+train+"\n");
                System.out.println(person.getString("ti"));
                System.out.println(person.getString("pr"));
                System.out.println(person.getString("lastStop"));
            }



        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
