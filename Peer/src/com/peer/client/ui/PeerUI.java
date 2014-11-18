package com.peer.client.ui;

import com.peer.application.Peerapplication;
import com.peer.client.ISessionManager;
import com.peer.client.ServiceAction;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;


public class PeerUI {
	
	private static PeerUI instance = null;
	
	 private ServiceProxy serviceProxy;
	
	private PeerUI() {
		Context appContext = Peerapplication.getInstance();
		serviceProxy = new ServiceProxy(appContext);
		Intent intent=new Intent(ServiceAction.ACTION_SESSION_SERVICE);
        appContext.bindService(intent, serviceProxy, Context.BIND_AUTO_CREATE);
	}
	
    public static PeerUI getInstance() {
        if (instance == null) {          	
            synchronized (PeerUI.class) {
                if (instance == null) {
                    instance = new PeerUI();
                }
            }
        }
        return instance;
    }
    
    public ISessionManager getISessionManager(){
        return serviceProxy.getISessionManager();
    }     
}


class ServiceProxy implements ServiceConnection {

    private ISessionManager sessionManager;

    public ServiceProxy(Context ctx) {
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        sessionManager = ISessionManager.Stub.asInterface(iBinder);
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        sessionManager = null;
    }

    public ISessionManager getISessionManager(){
        return sessionManager;
    }
}
