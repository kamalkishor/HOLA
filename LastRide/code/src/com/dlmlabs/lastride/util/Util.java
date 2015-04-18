package com.dlmlabs.lastride.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class Util {
	
	public static final boolean isNetworkAvailable(Context context) {
	    if (null==context)
	    	return false;
		ConnectivityManager connectivityManager 
	          = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
	
	public static final void showToastLong(Context context, String text) {
		Toast.makeText(context, text, Toast.LENGTH_LONG).show();
	}
	
	public static final void showToastShort(Context context, String text) {
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}
}
