package mobappdev.itm.fhj.at.train_stations;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by Viktoria on 28.10.2015.
 */
public class GPS_Position implements LocationListener {

    public static double latitude;
    public static double longitude;
    private TextView lat, lon;

    public GPS_Position(TextView lat, TextView lon){
        this.lat=lat;
        this.lon=lon;
    }

    @Override
    public void onLocationChanged(Location loc)
    {
        latitude=loc.getLatitude();
        longitude=loc.getLongitude();
        lat.setText(latitude+"");
        lon.setText(longitude+"");
    }

    @Override
    public void onProviderDisabled(String provider)
    {

    }
    @Override
    public void onProviderEnabled(String provider)
    {

    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras)
    {
    }
}
