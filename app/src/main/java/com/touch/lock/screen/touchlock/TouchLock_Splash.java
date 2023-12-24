package com.touch.lock.screen.touchlock;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.os.PowerManager;
import android.provider.Settings;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;

public class TouchLock_Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.activity_intro);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                String packageName = TouchLock_Splash.this.getPackageName();
                PowerManager powerManager = (PowerManager) TouchLock_Splash.this.getSystemService(Context.POWER_SERVICE);
                Intent intent = new Intent(TouchLock_Splash.this.getApplicationContext(), MainActivity.class);
                if (Build.VERSION.SDK_INT >= 23) {
                    if (!Settings.System.canWrite(TouchLock_Splash.this.getApplicationContext()) || !Settings.canDrawOverlays(TouchLock_Splash.this.getApplicationContext()) || !powerManager.isIgnoringBatteryOptimizations(packageName)) {
                        intent = new Intent(TouchLock_Splash.this.getApplicationContext(), TouchLock_permission.class);
                    } else {
                        intent = new Intent(TouchLock_Splash.this.getApplicationContext(), MainActivity.class);
                    }
                }
                TouchLock_Splash.this.startActivity(intent);
                TouchLock_Splash.this.finish();
            }
        }, 1200);
    }


    public void onPause() {
        super.onPause();
        finish();
    }
}
