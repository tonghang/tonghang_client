package com.peer.application;

import com.easemob.chat.EMChat;
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
		initwebsevice();
		initEMChat();
	}	
	private void initwebsevice() {
		// TODO Auto-generated method stub
		 Intent serviceIntent = new Intent(ServiceAction.ACTION_SERVICE);
		 startService(serviceIntent);
		 PeerUI.getInstance();
	}
	public static Peerapplication getInstance() {
		return instance;
	}
	/*初始化环信sdk*/
	private void initEMChat() {
		// TODO Auto-generated method stub
		EMChat.getInstance().init(instance);
	}
}
