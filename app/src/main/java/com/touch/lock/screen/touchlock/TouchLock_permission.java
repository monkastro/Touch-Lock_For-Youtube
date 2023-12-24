package com.touch.lock.screen.touchlock;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

public class TouchLock_permission extends Activity {


    private static final boolean f110D = false;
    private static final String TAG = "TSL";
    public String packageName;


    public PowerManager f111pm;


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.touch_lock_permission);
        this.packageName = getApplicationContext().getPackageName();
        this.f111pm = (PowerManager) getSystemService(POWER_SERVICE);
    }

    public synchronized void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= 23) {
            if (!Settings.System.canWrite(this) || !Settings.canDrawOverlays(this) || !this.f111pm.isIgnoringBatteryOptimizations(this.packageName)) {
                Toast.makeText(this, "onResume: Need get permission!!", Toast.LENGTH_LONG).show();
            } else {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onClickPermissionButton(View view) {
        if (!Settings.System.canWrite(this)) {
            Intent intent = new Intent();
            intent.setAction("android.settings.action.MANAGE_WRITE_SETTINGS");
            intent.setData(Uri.parse("package:" + this.packageName));
            startActivity(intent);
        }
        if (!Settings.canDrawOverlays(this)) {
            Intent intent2 = new Intent();
            intent2.setAction("android.settings.action.MANAGE_OVERLAY_PERMISSION");
            intent2.setData(Uri.parse("package:" + this.packageName));
            startActivity(intent2);
        }
        if (!this.f111pm.isIgnoringBatteryOptimizations(this.packageName)) {
            Intent intent3 = new Intent();
            intent3.setAction("android.settings.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS");
            intent3.setData(Uri.parse("package:" + this.packageName));
            startActivity(intent3);
        }
    }
}
