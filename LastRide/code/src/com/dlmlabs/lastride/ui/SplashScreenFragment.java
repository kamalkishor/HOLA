package com.dlmlabs.lastride.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dlmlabs.lastride.R;
import com.dlmlabs.lastride.util.Constants;

public class SplashScreenFragment extends Fragment {
	
	public static SplashScreenFragment newInstance() {
		SplashScreenFragment frag = new SplashScreenFragment();
		return frag;
	}
	
	public static int getMyId() {
		return Constants.ID_SPLASH_SCREEN_FRAGMENT;
	}
	
	public static String getMyTag() {
		return Constants.TAG_SPLASH_SCREEN_FRAGMENT;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_splash_screen, container, false);
		return view;
	}
	
}
