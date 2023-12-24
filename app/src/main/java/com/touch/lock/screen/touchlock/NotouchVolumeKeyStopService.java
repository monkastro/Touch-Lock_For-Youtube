package com.touch.lock.screen.touchlock;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class NotouchVolumeKeyStopService extends Service {
    private MyApplication myApp;

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
        this.myApp = (MyApplication) getApplicationContext();
        Log.d("NoTouch", "NotouchVolumeKeyStopService");
        Log.d("NoTouch", "check-1 :" + this.myApp.get_unlock_using_volume_longkey());
        if (!this.myApp.get_unlock_using_volume_longkey() || !this.myApp.get_Service_Status()) {
            stopSelf();
            return;
        }
        stopService(new Intent(this, LockService.class));
        startService(new Intent(this, UnLockService.class));
        this.myApp.set_Service_Status(false);
        stopSelf();
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        super.onStartCommand(intent, i, i2);
        return 1;
    }

    public void onDestroy() {
        super.onDestroy();
    }
}
