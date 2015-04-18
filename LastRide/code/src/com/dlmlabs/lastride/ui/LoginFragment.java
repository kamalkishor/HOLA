package com.dlmlabs.lastride.ui;

import com.dlmlabs.lastride.DriverActivity;
import com.dlmlabs.lastride.R;
import com.dlmlabs.lastride.UserActivity;
import com.dlmlabs.lastride.util.Constants;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class LoginFragment extends Fragment implements OnClickListener{
	
	private Context mContext;
	
	private ImageButton mDriver;
	private ImageButton mUser;
	
	public static LoginFragment newInstance() {
		LoginFragment frag = new LoginFragment();
		return frag;
	}
	
	public static int getMyId() {
		return Constants.ID_LOGIN_FRAGMENT;
	}
	
	public static String getMyTag() {
		return Constants.TAG_LOGIN_FRAGMENT;
	}
	
	private void initView(final View view) {
		mDriver = (ImageButton) view.findViewById(R.id.imageButtonDriver);
		mUser = (ImageButton) view.findViewById(R.id.imageButtonUser);
		mDriver.setOnClickListener(this);
		mUser.setOnClickListener(this);
	}
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		mContext = activity;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_login, container, false);
		initView(view);
		return view;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()) {
			case R.id.imageButtonDriver : {
				Intent intent = new Intent(mContext, DriverActivity.class);
				startActivity(intent);
				break;
			}
			case R.id.imageButtonUser : {
				Intent intent = new Intent(mContext, UserActivity.class);
				startActivity(intent);
				break;
			}
		}
	}
	
}
