package com.peer.application;

import com.peer.client.ServiceAction;
import com.peer.client.ui.PeerUI;
import com.peer.util.HomeWatcher;
import com.peer.util.OnHomePressedListener;

import android.app.Application;
import android.content.Intent;

public class Peerapplication extends Application {
	private static Peerapplication instance=null;
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub	
		instance=this;	
		initsevice();
//		HomeKeyWatcher();
	}
	private void initsevice() {
		// TODO Auto-generated method stub
		 Intent serviceIntent = new Intent(ServiceAction.ACTION_SERVICE);
		 startService(serviceIntent);
		 PeerUI.getInstance();
	}
	public static Peerapplication getInstance() {
		return instance;
	}
	/**
	 * homekey listner
	 */
	private void HomeKeyWatcher() {
		// TODO Auto-generated method stub
		HomeWatcher mHomeWatcher = new HomeWatcher(this);		
		mHomeWatcher.setOnHomePressedListener(new OnHomePressedListener() {
			
			@Override
			public void onHomePressed() {
				// TODO Auto-generated method stub
//				 Log.e(TAG, "onHomePressed..............");
//				Intent intent = new Intent(BasicActivity.this, FxService.class);
//				stopService(intent);
//				Log.e(TAG, "onHomePressed.....fhskhfkash.."); 
			}			
			@Override
			public void onHomeLongPressed() {
				// TODO Auto-generated method stub
				
			}
		});
		mHomeWatcher.startWatch();
	}
}
