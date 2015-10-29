package mobappdev.itm.fhj.at.train_stations;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Created by Viktoria on 29.10.2015.
 */
public class StartActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
    }

    public void openCalcMain(View v){
        Intent i=new Intent(this,MainActivity.class);
        startActivity(i);
    }

    public void openEnterName(View v){
        Intent i=new Intent(this,EnterNameActivity.class);
        startActivity(i);
    }
}
