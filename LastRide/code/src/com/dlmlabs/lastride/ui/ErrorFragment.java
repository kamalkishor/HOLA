package com.dlmlabs.lastride.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dlmlabs.lastride.R;
import com.dlmlabs.lastride.util.Constants;

public class ErrorFragment extends Fragment {

	public static ErrorFragment newInstance() {
		ErrorFragment frag = new ErrorFragment();
		return frag;
	}
	
	public static int getMyId() {
		return Constants.ID_ERROR_FRAGMENT;
	}
	
	public static String getMyTag() {
		return Constants.TAG_ERROR_FRAGMENT;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_error, container, false);
		return view;
	}
	
}
