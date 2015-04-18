package com.dlmlabs.lastride.ui;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.dlmlabs.lastride.R;
import com.dlmlabs.lastride.util.Constants;
import com.dlmlabs.lastride.util.Util;
import com.google.android.gms.maps.MapFragment;



public class HomeActivity extends Activity {
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        
        if(null == savedInstanceState) {
        	addFragment(SplashScreenFragment.getMyId());
        }
        
        if(Util.isNetworkAvailable(this)) {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                	replaceFragment(LoginFragment.getMyId());
                }
            }, Constants.DELAY_FRAGMENT_SWITCH);
        } else {
        	replaceFragment(ErrorFragment.getMyId());
        	Util.showToastLong(this, getString(R.string.message_network_unavailable));
        }
        
        /*if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }*/
    }
    
    //Fragment Helpers
    private String getFragmentTag(int id) {
    	switch(id) {
	    	case Constants.ID_SPLASH_SCREEN_FRAGMENT : {
	    		return SplashScreenFragment.getMyTag();
			}
	    	case Constants.ID_LOGIN_FRAGMENT : {
	    		return LoginFragment.getMyTag();
			}
			case Constants.ID_HOME_FRAGMENT : {
				return Constants.TAG_HOME_FRAGMENT;
			}
			default : {
				return ErrorFragment.getMyTag();
			}
		}
    }
    
    private Fragment getNewFragment(int id) {
    	switch(id) {
	    	case Constants.ID_SPLASH_SCREEN_FRAGMENT : {
	    		return SplashScreenFragment.newInstance();
			}
	    	case Constants.ID_LOGIN_FRAGMENT : {
	    		return LoginFragment.newInstance();
			}
			case Constants.ID_HOME_FRAGMENT : {
				return MapFragment.newInstance();
			}
			default : {
				return ErrorFragment.newInstance();
			}
		}
    }
    
    public void addFragment(int id) {
    	Fragment frag = getNewFragment(id);
    	String tag = getFragmentTag(id);
    	FragmentManager manager = getFragmentManager();
    	FragmentTransaction trans = manager.beginTransaction();
    	//trans.setCustomAnimations(R.anim.abc_slide_in_bottom, R.anim.abc_slide_out_top);
    	trans.add(R.id.container, frag, tag);
    	trans.commit();
    }
    
    public void replaceFragment(int id) {
    	Fragment frag = getNewFragment(id);
    	String tag = getFragmentTag(id);
    	FragmentManager manager = getFragmentManager();
    	FragmentTransaction trans = manager.beginTransaction();
    	//trans.setCustomAnimations(R.anim.abc_slide_in_bottom, R.anim.abc_slide_out_top);
    	trans.replace(R.id.container, frag, tag);
    	trans.commit();
    }
}
