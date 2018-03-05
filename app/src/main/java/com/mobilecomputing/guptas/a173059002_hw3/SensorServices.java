package com.mobilecomputing.guptas.a173059002_hw3;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by guptas on 5/3/18.
 */

public class SensorServices extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
