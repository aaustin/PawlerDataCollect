package com.trypawler.datacollect;

import java.util.Date;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class DataCollect extends Service implements LocationListener, SensorEventListener{

	DataFileWriter writerGPS;
	String headerGPS = "Index,Time,Accuracy,Latitude,Longitude,Altitude,Temp";
	DataFileWriter writerAcc;
	String headerAcc = "Index,Time,xAcc,yAcc,zAcc";
	
	Sensor acc;
	Sensor temp;
	SensorManager sm;
	LocationManager lm;
	int counter = 0;
	double lastTemp = 0;

	@Override
	public void onCreate() {
		Log.i(getClass().getSimpleName(), "Service on create called");
		lm = (LocationManager) getSystemService(LOCATION_SERVICE);
		sm = (SensorManager) getSystemService(SENSOR_SERVICE);
		
		writerGPS =  new DataFileWriter("GPS", headerGPS);
		writerAcc = new DataFileWriter("Acc", headerAcc);
		
		super.onCreate();				
	}	
	
	@Override
	public int onStartCommand(Intent i, int j, int k) {
		Log.i(getClass().getSimpleName(), "Service on start called.");
		counter = 0;
		acc = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		temp = sm.getDefaultSensor(Sensor.TYPE_TEMPERATURE);
		
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);
		lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, this);
		sm.registerListener(this, acc, SensorManager.SENSOR_DELAY_NORMAL);
		sm.registerListener(this, temp, SensorManager.SENSOR_DELAY_NORMAL);
		
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		
		Log.i(getClass().getSimpleName(), "Cleaning up service.");
		
		lm.removeUpdates(this);
		sm.unregisterListener(this);
		
		writerGPS.closeFile();
		writerAcc.closeFile();
		
		stopForeground(true);
	}
	
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	public void onSensorChanged(SensorEvent event) {
		
		if (event.sensor == acc) {		
			long timeinMillis = (new Date()).getTime() + (event.timestamp - System.nanoTime()) / 1000000L;
			Log.i(getClass().getSimpleName(), "Accelerometer changed: time-" + timeinMillis);
			writerAcc.writeLine(String.valueOf(counter) + ","
					+ String.valueOf(timeinMillis) + ","
					+ String.valueOf(event.values[0]) + ","
					+ String.valueOf(event.values[1]) + ","
					+ String.valueOf(event.values[2]));
		} else if (event.sensor == temp) {
			Log.i(getClass().getSimpleName(), "Temp changed: temp-" + String.valueOf(event.values[0]));
			lastTemp = event.values[0];
		}
		
	}

	public void onLocationChanged(Location arg0) {
		Log.i(getClass().getSimpleName(), "Location changed: acc-" + arg0.getAccuracy());
		writerGPS.writeLine(String.valueOf(counter) + ","
				+ String.valueOf(arg0.getTime()) + ","
				+ String.valueOf(arg0.getAccuracy()) + ","
				+ String.valueOf(arg0.getLatitude()) + ","
				+ String.valueOf(arg0.getLongitude()) + ","
				+ String.valueOf(arg0.getAltitude()) + ","
				+ String.valueOf(lastTemp));
		counter = counter + 1;
	}

	public void onProviderDisabled(String provider) {
		/* bring up the GPS settings */
		Intent intent = new Intent(
				android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		startActivity(intent);
		
	}

	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
