package com.dlmlabs.lastride;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Calendar;

import com.dlmlabs.lastride.background.MyService;
import com.dlmlabs.lastride.util.Constants;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class DriverActivity extends Activity implements OnMapReadyCallback, OnClickListener {
	
	public static GoogleMap googleMap;
	private MapFragment mMapFragment;
	private LatLng latlng, new_latlng;
	double d_lat, d_long , current_lat, current_lng;
	public PendingIntent pendingIntent;
	Button confirm;
	MyReceiver myReceiver;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_driver);
		
		mMapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.container_driver);
		init();
		/*if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
			.add(R.id.container_driver, MapFragment.newInstance(), Constants.TAG_DRIVER_FRAGMENT)
			.commit();
		}*/
		
		// Getting LocationManager object from System Service LOCATION_SERVICE
		LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		
		// Creating a criteria object to retrieve provider
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		// Getting the name of the best provider
		String provider = locationManager.getBestProvider(criteria, true);

		// Getting Current Location
		Location location = null;
		try{
			location = locationManager.getLastKnownLocation(provider);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		if(location!=null) {
			latlng = new LatLng(location.getLatitude(),location.getLongitude());
			current_lat = location.getLatitude();
			current_lng = location.getLongitude();
			setupMap();
			googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, Constants.ZOOM_LEVEL));
		}
		
		startService();
	}

	private void init() {
		//mMapFragment = MapFragment.newInstance();
		googleMap = mMapFragment.getMap();
		confirm = (Button) findViewById(R.id.buttonConfirm);
		confirm.setOnClickListener(this);
		googleMap.setMyLocationEnabled(true);
		googleMap.setPadding(0, 20, 20, 20);
		googleMap.getUiSettings().setMyLocationButtonEnabled(true);
		googleMap.getUiSettings().setRotateGesturesEnabled(false);
		googleMap.getUiSettings().setTiltGesturesEnabled(false);
		googleMap.getUiSettings().setCompassEnabled(true);
	}
	
	private void setupMap() {
		if(null!=googleMap) {
			 
		}
	}
	
	@Override
	public void onMapReady(GoogleMap map) {
		/*googleMap = map;*/
		
	}
	
	public void startService() {
		   Intent myIntent = new Intent(DriverActivity.this, MyService.class);
		    pendingIntent = PendingIntent.getService(DriverActivity.this, 0, myIntent, 0);
			           AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
			           Calendar calendar = Calendar.getInstance();
			           calendar.setTimeInMillis(System.currentTimeMillis());
			          calendar.add(Calendar.SECOND, 10);

			           alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),10000 ,pendingIntent);
	      startService(new Intent(this, MyService.class));
	   }

	 public void stopService() {
		 AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
		 alarmManager.cancel(pendingIntent);
  }
	
	@Override
	public void onClick(View v) {
		switch(v.getId()) {
			case R.id.buttonConfirm : {
				try{
					LatLng coordinates=googleMap.getCameraPosition().target;
					d_lat = coordinates.latitude;
					d_long = coordinates.longitude;
					sendLastRideData(Double.toString(d_lat), Double.toString(d_long));
				}
				catch(Exception e){
		        	e.printStackTrace();
		        }
				
				break;
			} 
		}
	}
	
	public void update() {
		//startService();
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		stopService();
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	}
	
	private void sendLastRideData(String lat, String lng) {
		String userId = "100";/*
        String lat = "13.204492";
        String lng = "77.707687";*/
        String address = "Santacruz (E), Mumbai, India";
		new lastRideDriver().execute(userId, lat, lng, address);
	}
	
	private class MyReceiver extends BroadcastReceiver {
		 
		 @Override
		 public void onReceive(Context arg0, Intent arg1) {
		  // TODO Auto-generated method stub
		  
		  int datapassed = arg1.getIntExtra("DATAPASSED", 0);
		  
		  /*Toast.makeText(AndroidServiceTestActivity.this,
		    "Triggered by Service!\n"
		    + "Data passed: " + String.valueOf(datapassed),
		    Toast.LENGTH_LONG).show();*/
		  
		 }
		 
	}
	
	class lastRideDriver extends AsyncTask<String, String, Void> {
        
		private ProgressDialog progressDialog = new ProgressDialog(DriverActivity.this);
        String result = "";

        protected void onPreExecute() {
            progressDialog.setMessage("Inserting data...");
            progressDialog.show();
            progressDialog.setOnCancelListener(new OnCancelListener() {
                @Override
                public void onCancel(DialogInterface arg0) {
                    lastRideDriver.this.cancel(true);
                }
            });
        }


        @Override
        protected Void doInBackground(String... strings) {
            try {
                String userId = strings[0];
                String lat = strings[1];
                String lng = strings[2];
                String address = strings[3];
                String link = "http://aaola.byethost13.com/lastridepost.php";
                String data = URLEncoder.encode("userId", "UTF-8")
                        + "=" + URLEncoder.encode(userId, "UTF-8");
                data += "&" + URLEncoder.encode("lat", "UTF-8")
                        + "=" + URLEncoder.encode(lat, "UTF-8");
                data += "&" + URLEncoder.encode("lng", "UTF-8")
                        + "=" + URLEncoder.encode(lng, "UTF-8");
                data += "&" + URLEncoder.encode("address", "UTF-8")
                        + "=" + URLEncoder.encode(address, "UTF-8");
                URL url = new URL(link);
                URLConnection conn = url.openConnection();
                conn.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter
                        (conn.getOutputStream());
                wr.write(data);
                wr.flush();
                BufferedReader reader = new BufferedReader
                        (new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = null;
                // Read Server Response
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                    break;
                }
                result = sb.toString();
            } catch (Exception e) {
                Log.e("log_tag", "Error converting result " + e.getMessage());

            }
            return null;
        }

        protected void onPostExecute(Void v) {
            try {
                Log.v(Constants.APP_NAME, result);
                this.progressDialog.dismiss();
                update();
            } catch (Exception e) {
                // TODO: handle exception
                Log.e("log_tag", "Error parsing data " + e.toString());
            }
        }
    }
}