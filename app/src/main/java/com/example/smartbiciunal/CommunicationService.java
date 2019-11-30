package com.example.smartbiciunal;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

public class CommunicationService extends Service {
    public CommunicationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        //  Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        //super.onCreate();
        // TODO establish connection to web server; handle incoming strings and call respective trigger methods
    }
}
