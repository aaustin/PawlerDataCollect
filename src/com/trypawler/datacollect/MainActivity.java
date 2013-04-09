package com.trypawler.datacollect;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends Activity {

    TextView txtCount;
    ProgressBar progBar;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        progBar = (ProgressBar) findViewById(R.id.progressBar1);
        progBar.setVisibility(ProgressBar.INVISIBLE);
    }

    
    public void cmdStartStop(View v) {
    	if (((Button)v).getText().equals("Start")) {
    		progBar.setVisibility(ProgressBar.VISIBLE);
    		startService(new Intent(this, DataCollect.class));	 
    		((Button)v).setText("Stop");
    	} else {
    		progBar.setVisibility(ProgressBar.INVISIBLE);
    		stopService(new Intent(this, DataCollect.class));
    		((Button)v).setText("Start");
    	}
    }
}
