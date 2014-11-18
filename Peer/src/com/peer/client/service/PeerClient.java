package com.peer.client.service;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

public class PeerClient {

    private static PeerClient instance = new PeerClient();
    private PeerClientService peerClientService;
    private SessionManager sessionManager;

    public static void autoStartReceived(Context context) {
        Intent i = new Intent();
        i.setClass(context, PeerClientService.class);
        context.startService(i);
    }

    public static PeerClient getInstance() {
        return instance;
    }

    public void start(PeerClientService socialLabelService) {
        this.peerClientService = socialLabelService;
        sessionManager = new SessionManager();
    }

    public IBinder getXmppService() {
        return sessionManager;
    }
}
