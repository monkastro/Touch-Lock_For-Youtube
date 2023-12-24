package com.touch.lock.screen.touchlock;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.ArrayList;

public class UnLockService extends Service {


    private static final boolean f112D = false;
    public static int LONG_PRESS_TIME = 500;
    private static final int RESULT_OK = 0;
    private static final String TAG = "TSL";
    private final String CMD_BACK = "backwards";
    private final String CMD_GO = "Forward";
    private final String CMD_LEFT = "left";
    private final String CMD_RIGHT = "Right side";
    private final String CMD_STOP = "stop";
    private final int GOOGLE_STT = 1000;
    private final int MY_UI = 1001;
    Drawable SizeChangedLockButton;

    public boolean accelometer_landscape = false;
    NotificationCompat.Builder builder;

    public boolean isLongTouch = false;

    public boolean isMove = false;
    private SensorEventListener mAccLis;
    private Sensor mAccelometerSensor = null;
    final Handler mHandler = new Handler();
    Runnable mLongPressed = new Runnable() {
        public void run() {
            UnLockService.this.stopSelf();
            if (!UnLockService.this.accelometer_landscape && !UnLockService.this.proximity_near) {
                Intent intent = new Intent(UnLockService.this.getApplicationContext(), MainActivity.class);
                intent.addFlags(872415232);
                UnLockService.this.startActivity(intent);
                boolean unused = UnLockService.this.isLongTouch = true;
            }
        }
    };
    /* access modifiers changed from: private */
    public WindowManager mManager;
    private NotificationManager mNM;
    private Notification mNoti;
    /* access modifiers changed from: private */
    public WindowManager.LayoutParams mParams;
    private ArrayList<String> mResult;
    private TextView mResultTextView;
    private String mSelectedString;
    /* access modifiers changed from: private */
    public float mTouchX;
    /* access modifiers changed from: private */
    public float mTouchY;
    /* access modifiers changed from: private */
    public View mView;
    private View.OnTouchListener mViewTouchListener = new View.OnTouchListener() {
        public boolean onTouch(View view, MotionEvent motionEvent) {
            int action = motionEvent.getAction();
            if (action == 0) {
                boolean unused = UnLockService.this.isMove = false;
                boolean unused2 = UnLockService.this.isLongTouch = false;
                float unused3 = UnLockService.this.mTouchX = motionEvent.getRawX();
                float unused4 = UnLockService.this.mTouchY = motionEvent.getRawY();
                UnLockService unlockservice = UnLockService.this;
                int unused5 = unlockservice.mViewX = unlockservice.mParams.x;
                UnLockService unlockservice2 = UnLockService.this;
                int unused6 = unlockservice2.mViewY = unlockservice2.mParams.y;
                UnLockService.this.mHandler.postDelayed(UnLockService.this.mLongPressed, (long) UnLockService.LONG_PRESS_TIME);
            } else if (action == 1) {
                if (!UnLockService.this.isMove && !UnLockService.this.isLongTouch && !UnLockService.this.proximity_near) {
                    UnLockService.this.onClickUnLockButton();
                }
                UnLockService.this.mHandler.removeCallbacks(UnLockService.this.mLongPressed);
            } else if (action == 2) {
                boolean unused7 = UnLockService.this.isMove = true;
                int rawX = (int) (motionEvent.getRawX() - UnLockService.this.mTouchX);
                int rawY = (int) (motionEvent.getRawY() - UnLockService.this.mTouchY);
                if (rawX <= -50 || rawX >= 50 || rawY <= -50 || rawY >= 50) {
                    UnLockService.this.mHandler.removeCallbacks(UnLockService.this.mLongPressed);
                    UnLockService.this.mParams.x = UnLockService.this.mViewX + rawX;
                    UnLockService.this.mParams.y = UnLockService.this.mViewY + rawY;
                    UnLockService.this.mManager.updateViewLayout(UnLockService.this.mView, UnLockService.this.mParams);
                } else {
                    boolean unused8 = UnLockService.this.isMove = false;
                }
            }
            return true;
        }
    };
    /* access modifiers changed from: private */
    public int mViewX;
    /* access modifiers changed from: private */
    public int mViewY;
    /* access modifiers changed from: private */
    public MyApplication myApp;
    /* access modifiers changed from: private */
    public float p_value;
    private SensorEventListener proxi;
    private Sensor proxiSensor;
    /* access modifiers changed from: private */
    public boolean proximity_near = false;
    private String returnText;

    /* renamed from: sm */
    private SensorManager f113sm;
    private ImageView unlockbutton;

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onClickUnLockButton() {
        stopService(new Intent(this, UnLockService.class));
        startService(new Intent(this, LockService.class));
    }

    public void onCreate() {
        super.onCreate();
        Log.d("Check-4", "start Unloack Service onCreate()");
        this.myApp = (MyApplication) getApplicationContext();
        this.f113sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        this.proxi = new proxiListener();
        this.proxiSensor = this.f113sm.getDefaultSensor(8);
        if (this.myApp.get_Service_Proxi()) {
            this.f113sm.registerListener(this.proxi, this.proxiSensor, 2);
        } else {
            this.f113sm.unregisterListener(this.proxi);
        }
        View inflate = ((LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.service_unlocked, (ViewGroup) null);
        this.mView = inflate;
        ImageView imageView = (ImageView) inflate.findViewById(R.id.unlocked_button);
        this.unlockbutton = imageView;
        imageView.setAlpha((this.myApp.get_Service_Lock_Icon_Alpha() * 255) / 100);
        if (this.myApp.get_Service_unlock_button_hide()) {
            this.unlockbutton.setVisibility(8);
        }
        this.unlockbutton.setImageBitmap(Bitmap.createScaledBitmap(((BitmapDrawable) getResources().getDrawable(this.myApp.get_Lock_Button_Icon())).getBitmap(), (int) convertDpToPixel((float) this.myApp.get_Service_Lock_Icon_Size()), (int) convertDpToPixel((float) this.myApp.get_Service_Lock_Icon_Size()), true));
        this.mView.setOnTouchListener(this.mViewTouchListener);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(-2, -2, Build.VERSION.SDK_INT >= 26 ? 2038 : 2002, 8, -3);
        this.mParams = layoutParams;
        layoutParams.gravity = 51;
        WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        this.mManager = windowManager;
        windowManager.addView(this.mView, this.mParams);
        this.myApp.set_Service_Status(true);
    }

    public float convertDpToPixel(float f) {
        return f * (((float) getResources().getDisplayMetrics().densityDpi) / 160.0f);
    }

    private void buttonNoti() {
        this.mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Intent intent = new Intent("android.intent.action.unLockService.switch");
        Intent intent2 = new Intent("android.intent.action.unLockService.youtube");
        Intent intent3 = new Intent("android.intent.action.unLockService.finish");
        Intent intent4 = new Intent(getApplicationContext(), MainActivity.class);
        intent4.addFlags(872415232);
        PendingIntent activity = PendingIntent.getActivity(getApplicationContext(), 0, intent4, 0);
        PendingIntent service = PendingIntent.getService(getApplicationContext(), 0, intent, 0);
        PendingIntent service2 = PendingIntent.getService(getApplicationContext(), 0, intent2, 0);
        PendingIntent service3 = PendingIntent.getService(getApplicationContext(), 0, intent3, 0);
        if (Build.VERSION.SDK_INT >= 26) {
            BitmapFactory.decodeResource(getResources(), R.mipmap.app_icon);
            String string = getString(R.string.NotiTitle);
            String string2 = getString(R.string.subNotiTitle);
            String string3 = getString(R.string.NotiLock);
            String string4 = getString(R.string.NotiFinish);
            NotificationCompat.Builder builder2 = new NotificationCompat.Builder(this, "default");
            this.builder = builder2;
            builder2.setSmallIcon(R.mipmap.app_icon);
            this.builder.setContentTitle(string);
            this.builder.setContentText(string2);
            this.builder.addAction(R.drawable.small_changetolock, string3, service);
            this.builder.addAction(R.drawable.small_changetolock, string4, service3);
            this.builder.setPriority(0);
            this.builder.setAutoCancel(true);
            this.builder.setContentIntent(activity);
            this.mNM.createNotificationChannel(new NotificationChannel("default", "Default channel", 2));
            return;
        }
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.notifiview_unlocked);
        remoteViews.setOnClickPendingIntent(R.id.btn_servicesSwitch, service);
        remoteViews.setOnClickPendingIntent(R.id.btn_youtube, service2);
        remoteViews.setOnClickPendingIntent(R.id.btn_servicesFinish, service3);
        Notification notification = new Notification(R.drawable.unlocked, "Touch Screen Lock", System.currentTimeMillis());
        this.mNoti = notification;
        notification.flags |= 16;
        this.mNoti.flags |= 2;
        this.mNoti.contentView = remoteViews;
        this.mNoti.contentIntent = activity;
    }

    public boolean stopService(Intent intent) {
        SensorManager sensorManager = this.f113sm;
        if (sensorManager != null) {
            sensorManager.unregisterListener(this.proxi);
        }
        return super.stopService(intent);
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        Log.d("Check-5", "onStartCommand");
        buttonNoti();
        if (Build.VERSION.SDK_INT >= 26) {
            this.mNM.notify(7777, this.builder.build());
            Log.d("Check-1-1", "notify call!!");
        } else {
            this.mNM.notify(7777, this.mNoti);
        }
        super.onStartCommand(intent, i, i2);
        String str = null;
        if (intent != null) {
            str = intent.getAction();
        }
        if ("android.intent.action.unLockService.switch".equals(str)) {
            startService(new Intent(this, LockService.class));
            stopService(new Intent(this, UnLockService.class));
            if (this.myApp.get_Service_Proxi()) {
                this.f113sm.unregisterListener(this.proxi);
            }
        }
        if ("android.intent.action.unLockService.finish".equals(str)) {
            stopSelf();
            this.myApp.set_Service_Status(false);
            if (Build.VERSION.SDK_INT >= 26) {
                NotificationManagerCompat.from(this).cancel(7777);
            } else {
                this.mNM.cancelAll();
            }
        }
       /* if ("android.intent.action.unLockService.youtube".equals(str)) {
            startActivity(getPackageManager().getLaunchIntentForPackage("com.google.android.youtube"));
        }*/
        if ("android.intent.action.unLockService.proximity_on".equals(str)) {
            SensorManager sensorManager = this.f113sm;
            if (sensorManager != null) {
                sensorManager.registerListener(this.proxi, this.proxiSensor, 2);
            } else if (this.myApp.get_Service_Proxi()) {
                this.f113sm.registerListener(this.proxi, this.proxiSensor, 2);
            } else {
                this.f113sm.unregisterListener(this.proxi);
            }
        }
        if ("android.intent.action.unLockService.proximity_off".equals(str)) {
            this.unlockbutton.setVisibility(View.VISIBLE);
            SensorManager sensorManager2 = this.f113sm;
            if (sensorManager2 != null) {
                sensorManager2.unregisterListener(this.proxi);
            }
        }
        if ("android.intent.action.unLockService.accelometer_on".equals(str)) {
            SensorManager sensorManager3 = this.f113sm;
            if (sensorManager3 != null) {
                sensorManager3.registerListener(this.mAccLis, this.mAccelometerSensor, 2);
            } else if (this.myApp.get_Service_Accel()) {
                this.f113sm.registerListener(this.mAccLis, this.mAccelometerSensor, 2);
            } else {
                this.f113sm.unregisterListener(this.mAccLis);
            }
        }
        if ("android.intent.action.unLockService.accelometer_off".equals(str)) {
            this.unlockbutton.setVisibility(View.VISIBLE);
            SensorManager sensorManager4 = this.f113sm;
            if (sensorManager4 != null) {
                sensorManager4.unregisterListener(this.mAccLis);
            }
        }
        if ("android.intent.action.unLockService.unlock_button_hide_on".equals(str)) {
            this.unlockbutton.setVisibility(View.INVISIBLE);
        }
        if (!"android.intent.action.unLockService.unlock_button_hide_off".equals(str)) {
            return START_STICKY;
        }
        this.unlockbutton.setVisibility(View.VISIBLE);
        return START_STICKY;
    }

    private class proxiListener implements SensorEventListener {
        public void onAccuracyChanged(Sensor sensor, int i) {
        }

        private proxiListener() {
        }

        public void onSensorChanged(SensorEvent sensorEvent) {
            float unused = UnLockService.this.p_value = sensorEvent.values[0];
            Log.d("[SensorEvent2]", "p value : " + UnLockService.this.p_value);
            int i = Build.VERSION.SDK_INT >= 26 ? 2038 : 2002;
            if (!UnLockService.this.myApp.get_Service_Proxi()) {
                return;
            }
            if (UnLockService.this.p_value == 0.0f) {
                Log.d("[SensorEvent2]", "NEAR!!");
                boolean unused2 = UnLockService.this.proximity_near = true;
                if (UnLockService.this.myApp.get_Service_Unlock_Noti_setting()) {
                    Toast.makeText(UnLockService.this.getApplicationContext(), "Screen lock by proximity sensor", 0).show();
                }
                WindowManager.LayoutParams unused3 = UnLockService.this.mParams = new WindowManager.LayoutParams(-1, -1, i, 8, -3);
                UnLockService.this.mParams.gravity = 51;
                UnLockService unlockservice = UnLockService.this;
                WindowManager unused4 = unlockservice.mManager = (WindowManager) unlockservice.getSystemService("window");
                UnLockService.this.mManager.updateViewLayout(UnLockService.this.mView, UnLockService.this.mParams);
                return;
            }
            Log.d("[SensorEvent2]", "FAR!!");
            boolean unused5 = UnLockService.this.proximity_near = false;
            WindowManager.LayoutParams unused6 = UnLockService.this.mParams = new WindowManager.LayoutParams(-2, -2, i, 8, -3);
            UnLockService.this.mParams.gravity = 51;
            UnLockService unlockservice2 = UnLockService.this;
            WindowManager unused7 = unlockservice2.mManager = (WindowManager) unlockservice2.getSystemService("window");
            UnLockService.this.mManager.updateViewLayout(UnLockService.this.mView, UnLockService.this.mParams);
        }
    }

    private class AccelometerListener implements SensorEventListener {
        public void onAccuracyChanged(Sensor sensor, int i) {
        }

        private AccelometerListener() {
        }

        public void onSensorChanged(SensorEvent sensorEvent) {
            SensorEvent sensorEvent2 = sensorEvent;
            if (UnLockService.this.myApp.get_Service_Accel()) {
                double d = (double) sensorEvent2.values[0];
                double d2 = (double) sensorEvent2.values[1];
                double d3 = (double) sensorEvent2.values[2];
                //Log.e("LOG", "ACCELOMETER           [X]:" + String.format("%.4f", new Object[]{sensorEvent2.values[0]}) + "           [Y]:" + String.format("%.4f", new Object[]{sensorEvent2.values[1]}) + "           [Z]:" + String.format("%.4f", new Object[]{sensorEvent2.values[2]}) + "           [angleXZ]: " + String.format("%.4f", new Object[]{(Math.atan2(d, d3) * 180.0d) / 3.141592653589793d}) + "           [angleYZ]: " + String.format("%.4f", Double.valueOf((Math.atan2(d2, d3) * 180.0d) / 3.141592653589793d)));
                int i = Build.VERSION.SDK_INT >= 26 ? 2038 : 2002;
                if ((d > 7.0d || d < -7.0d) && !UnLockService.this.accelometer_landscape) {
                    Toast.makeText(UnLockService.this.getApplicationContext(), "Automatic screen lock by tilting the screen", 0).show();
                    boolean unused = UnLockService.this.accelometer_landscape = true;
                    WindowManager.LayoutParams unused2 = UnLockService.this.mParams = new WindowManager.LayoutParams(-1, -1, i, 8, -3);
                    UnLockService.this.mParams.gravity = 51;
                    UnLockService unlockservice = UnLockService.this;
                    WindowManager unused3 = unlockservice.mManager = (WindowManager) unlockservice.getSystemService(WINDOW_SERVICE);
                    UnLockService.this.mManager.updateViewLayout(UnLockService.this.mView, UnLockService.this.mParams);
                } else if (d < 7.0d && d > -7.0d && UnLockService.this.accelometer_landscape) {
                    boolean unused4 = UnLockService.this.accelometer_landscape = false;
                    WindowManager.LayoutParams unused5 = UnLockService.this.mParams = new WindowManager.LayoutParams(-2, -2, i, 8, -3);
                    UnLockService.this.mParams.gravity = 51;
                    UnLockService unlockservice2 = UnLockService.this;
                    WindowManager unused6 = unlockservice2.mManager = (WindowManager) unlockservice2.getSystemService(WINDOW_SERVICE);
                    UnLockService.this.mManager.updateViewLayout(UnLockService.this.mView, UnLockService.this.mParams);
                }
            }
        }
    }

    public void onDestroy() {
        SensorManager sensorManager = this.f113sm;
        if (sensorManager != null) {
            sensorManager.unregisterListener(this.proxi);
        }
        super.onDestroy();
        View view = this.mView;
        if (view != null) {
            this.mManager.removeView(view);
            this.mView = null;
        }
        this.myApp.set_Service_Status(false);
        this.mHandler.removeCallbacks(this.mLongPressed);
    }
}
