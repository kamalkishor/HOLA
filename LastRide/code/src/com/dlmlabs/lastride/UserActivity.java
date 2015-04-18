package com.dlmlabs.lastride;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONObject;

import com.dlmlabs.lastride.util.Constants;
import com.dlmlabs.lastride.util.Util;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Activity;
import android.app.ActionBar;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.os.Build;

public class UserActivity extends Activity implements OnClickListener {

	public static GoogleMap googleMap;
	private MapFragment mMapFragment;
	private LatLng latlng, new_latlng;
	double d_lat, d_long , current_lat, current_lng;
	
	TextView textDestination;
	Button confirm;
	Button cancel;
	ImageView marker;
	boolean isDestinationSelected = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user);
		
		mMapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.container_user);
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
			googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, Constants.ZOOM_LEVEL));
		}
		marker = (ImageView)findViewById(R.id.mapMarker) ;
		marker.setOnClickListener(this);
	}
	
	
	private void init() {
		//mMapFragment = MapFragment.newInstance();
		googleMap = mMapFragment.getMap();
		
		confirm = (Button) findViewById(R.id.buttonRideNow);
		confirm.setOnClickListener(this);
		textDestination = (TextView) findViewById(R.id.bottomLabelDestination);
		googleMap.setMyLocationEnabled(true);
		googleMap.setPadding(0, 20, 20, 20);
		googleMap.getUiSettings().setMyLocationButtonEnabled(true);
		googleMap.getUiSettings().setRotateGesturesEnabled(false);
		googleMap.getUiSettings().setTiltGesturesEnabled(false);
		googleMap.getUiSettings().setCompassEnabled(true);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
			case R.id.mapMarker : {
				try{
					LatLng coordinates=googleMap.getCameraPosition().target;
					d_lat = coordinates.latitude;
					d_long = coordinates.longitude;
					new lastRideUser().execute(Double.toString(d_lat),Double.toString(d_long) );
				}
				catch(Exception e){
		        	e.printStackTrace();
		        }
				break;
			}
			case R.id.buttonRideNow : {
				if(isDestinationSelected) {
					new bookRide().execute("1");
				} else {
					Util.showToastLong(getApplicationContext(), "Please Select A Destination First");
				}
			}
		}
	}
	
	class lastRideUser extends AsyncTask<String, String, Void> {
		
        private ProgressDialog progressDialog = new ProgressDialog(UserActivity.this);
        InputStream is = null;
        String result = "";

        protected void onPreExecute() {
            progressDialog.setMessage("Fetching Offer Rides..");
            progressDialog.show();
            progressDialog.setOnCancelListener(new OnCancelListener() {
                @Override
                public void onCancel(DialogInterface arg0) {
                    lastRideUser.this.cancel(true);
                }
            });
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                String lat = params[0];
                String lng = params[1];
                String link = "http://aaola.byethost13.com/lastrideget.php";
                String data = URLEncoder.encode("lat", "UTF-8")
                        + "=" + URLEncoder.encode(lat, "UTF-8");
                data += "&" + URLEncoder.encode("lng", "UTF-8")
                        + "=" + URLEncoder.encode(lng, "UTF-8");
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
        	Log.v("piyush","in post");
        	final String TAG_LAT ="lat";
        	final String TAG_LANG = "lng";
    			try {

    	
    					JSONArray array = new JSONArray(result);
    					JSONObject obj;

    					int len = array.length();
    					Log.v("piyush",result);
    					if (len != 0) {
    						for (int i = 0; i < len; i++) {
    							try{
    								obj = array.getJSONObject(i);
    								String Lat = obj.getString(TAG_LAT);
    								String lang = obj.getString(TAG_LANG);
    								double mLat = Double.parseDouble(Lat);
    								double mLong = Double.parseDouble(lang);

    								new_latlng = new LatLng(mLat,mLong);
;
    								//Log.v("piyush","check the code");
    								Marker melbourne = googleMap.addMarker(new MarkerOptions()
    								.position(new_latlng)
    								.snippet("Choose Your Car and Tap")
    								.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_car_marker)));

    							}   catch (Exception e1) {
    								// TODO Auto-generated catch block
    								e1.printStackTrace();
    							}
    						}
    					}
    			}
    						catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
    					
    			
    			isDestinationSelected = true;
    			this.progressDialog.dismiss();
    			textDestination.setVisibility(View.INVISIBLE);
    }
    }
	
	
	class bookRide extends AsyncTask<String, String, Void> {
			private ProgressDialog progressDialog = new ProgressDialog(UserActivity.this);
	        InputStream is = null;
	        String result = "";
	        protected void onPreExecute() {
	            progressDialog.setMessage("Booking Rides..");
	            progressDialog.show();
	            progressDialog.setOnCancelListener(new OnCancelListener() {
	                @Override
	                public void onCancel(DialogInterface arg0) {
	                    bookRide.this.cancel(true);
	                }
	            });
	        }
	        @Override
	        protected Void doInBackground(String... params) {
	            try {
	                String id=params[0];
	                String link = "http://aaola.byethost13.com/bookridepost.php";
	                String data = URLEncoder.encode("id", "UTF-8")
	                        + "=" + URLEncoder.encode(id, "UTF-8");
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
	        	Log.v("piyush","booking successful");
	    			try {
	    					JSONArray array = new JSONArray(result);
	    					JSONObject obj;
	    					Log.v("piyush",result);
	    					
	    				}
					catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
	    					
	    			
	    			isDestinationSelected = true;
	    			this.progressDialog.dismiss();
	    			textDestination.setVisibility(View.INVISIBLE);
	    			showBookingDetails();
	        }
		
		}
	
	public static class MyDialogFragment extends DialogFragment {
	    static MyDialogFragment newInstance() {
	        return new MyDialogFragment();
	    }

	    @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	            Bundle savedInstanceState) {
	        View v = inflater.inflate(R.layout.fragment_booking, container, false);
	        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
	        /*View tv = v.findViewById(R.id.text);
	        ((TextView)tv).setText("This is an instance of MyDialogFragment");*/
	        return v;
	    }
	}
	
	private void showBookingDetails() {
		Util.showToastLong(getApplicationContext(), "Booking Confirmed!");
		DialogFragment newFragment = MyDialogFragment.newInstance();
		newFragment.show(getFragmentManager(), "dialog");
	}
}

