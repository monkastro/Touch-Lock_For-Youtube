package com.touch.lock.screen.touchlock;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

public class MybroadcastReceiverService extends BroadcastReceiver {


    private static final boolean f96D = false;

    public void onReceive(Context context, Intent intent) {
        Log.d("NoTouch", "Received EVENT");
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            if (Build.VERSION.SDK_INT >= 26) {
                ComponentName startForegroundService = context.startForegroundService(new Intent().setComponent(new ComponentName(context.getPackageName(), NotouchBootingStartService.class.getName())));
                return;
            }
            ComponentName startService = context.startService(new Intent().setComponent(new ComponentName(context.getPackageName(), NotouchBootingStartService.class.getName())));
        } else if (intent.getAction().equals("Intent.EXTRA_KEY_EVENT")) {
            Log.d("NoTouch", "Did you press the volume down?");
            if (Build.VERSION.SDK_INT >= 26) {
                context.startForegroundService(new Intent().setComponent(new ComponentName(context.getPackageName(), NotouchVolumeKeyStopService.class.getName())));
                return;
            }
            context.startService(new Intent().setComponent(new ComponentName(context.getPackageName(), NotouchVolumeKeyStopService.class.getName())));
        }
    }
}
