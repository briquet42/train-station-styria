package mobappdev.itm.fhj.at.train_stations;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Viktoria on 29.10.2015.
 */
public class EnterNameActivity extends Activity implements ICallBack{
    private String STATION="next railway station";
    private EditText inputStation;
    private TextView outputDisplay;
    private SharedPreferences prefs;
    private TimePicker actTime;
    private int hour, min;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_name);

        outputDisplay=(TextView)findViewById(R.id.txtOutput);

        inputStation=(EditText)findViewById(R.id.txtStation);
        prefs = getSharedPreferences("Station", 0);
        inputStation.setText(prefs.getString(STATION, "Kapfenberg"));

    }

    public void getDisplay(View v){
        outputDisplay.setText("");
        actTime=(TimePicker)findViewById(R.id.tpActualTime);
        hour=actTime.getCurrentHour();
        min=actTime.getCurrentMinute();
        String sUrl = "http://fahrplan.oebb.at/bin/stboard.exe/dn?L=vs_scotty.vs_liveticker&evaId=8100031&boardType=dep&time="+hour+":"+min+"&productsFilter=1111111111111111&additionalTime=0&disableEquivs=yes&maxJourneys=10&outputMode=tickerDataOnly&start=yes&selectDate=today\n";
        HttpHelper helper = new HttpHelper();
        helper.setCallback(this);
        helper.execute(sUrl);

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
           // String weatherStr = ja.getJSONObject(0).getString("st") + ", " + ja.getJSONObject(0).getString("st");
          //  this.outputDisplay.setText(weatherStr);
            //this.outputDisplay.setText((CharSequence) ja);

            final int n = ja.length();
            for (int i = 0; i < n; ++i) {
                final JSONObject person = ja.getJSONObject(i);
                String train=person.getString("ti")+"; "+person.getString("pr")+"; "+person.getString("lastStop");
                this.outputDisplay.setText(outputDisplay.getText()+train+"\n");
                System.out.println(person.getString("ti"));
                System.out.println(person.getString("pr"));
                System.out.println(person.getString("lastStop"));
            }



        } catch (JSONException e) {
            e.printStackTrace();
        }



    }
}
