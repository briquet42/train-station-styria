package mobappdev.itm.fhj.at.train_stations;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

/**
 * Created by Viktoria on 29.10.2015.
 */
public class EnterNameActivity extends Activity {
    private String STATION="next railway station";
    private EditText inputStation;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_name);

        inputStation=(EditText)findViewById(R.id.txtStation);
        prefs = getSharedPreferences("Station", 0);
        inputStation.setText(prefs.getString(STATION, "Kapfenberg"));

    }

    public void getDisplay(View v){

        SharedPreferences.Editor edit = prefs.edit();
        edit.putString(STATION, inputStation.getText().toString());
        edit.commit();
    }
}
