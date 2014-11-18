package com.peer.application;

import com.peer.client.ServiceAction;
import com.peer.client.ui.PeerUI;

import android.app.Application;
import android.content.Intent;

public class Peerapplication extends Application {
	private static Peerapplication instance=null;
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub	
		instance=this;	
		initsevice();		
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

}
