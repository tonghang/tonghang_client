package com.peer.client.service;


import com.peer.client.ISessionListener;

import android.os.IBinder;
import android.os.RemoteException;

public class SessionListener implements ISessionListener {

	private String message;
	private int code;
	
	@Override
	public IBinder asBinder() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCallBack(int status, String message)
			throws RemoteException {
		this.code = status;
		this.message = message;	
	}
	
	public String getMessage() {
		return message;
	}

	public int getCode() {
		return code;
	}
}
