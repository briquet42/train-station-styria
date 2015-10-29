package mobappdev.itm.fhj.at.train_stations;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends Activity {
    private LocationManager lm;
    private LocationListener ls;
    private TextView txtlongitude;
    private TextView txtlatitude;
    private Button butshowPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtlongitude=(TextView) findViewById(R.id.txtLong);
        butshowPos=(Button) findViewById(R.id.butGPS);
        txtlatitude=(TextView) findViewById(R.id.txtLati);

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


}
