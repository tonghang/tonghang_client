package com.peer.client.service;

import com.peer.client.ServiceAction;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;


public class PeerClientService extends Service {

    private PeerClient peerClient = null;
    @Override
    public void onCreate() {
        super.onCreate();
        peerClient = PeerClient.getInstance();
        peerClient.start(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        if(intent.getAction().equals(ServiceAction.ACTION_SESSION_SERVICE)) {
            return peerClient.getXmppService();
        }
        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
    	// TODO Auto-generated method stub
    	 flags = START_STICKY;
    	return super.onStartCommand(intent, flags, startId);
    }
}
