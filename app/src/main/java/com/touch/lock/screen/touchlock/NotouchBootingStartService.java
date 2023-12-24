package com.touch.lock.screen.touchlock;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class NotouchBootingStartService extends Service {
    private MyApplication myApp;

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
        MyApplication myApplication = (MyApplication) getApplicationContext();
        this.myApp = myApplication;
        if (myApplication.get_Service_Booting_Start()) {
            startService(new Intent(this, UnLockService.class));
        } else {
            stopSelf();
        }
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        super.onStartCommand(intent, i, i2);
        return START_STICKY;
    }

    public void onDestroy() {
        super.onDestroy();
    }
}
