package com.touch.lock.screen.touchlock;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;


import com.eminayar.panter.PanterDialog;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Locale;


public class MainActivity extends Activity implements RadioGroup.OnCheckedChangeListener {


    private static final boolean f95D = false;
    private static final int REQ_CODE_OVERLAY_PERMISSION = 1234;
    private static final String TAG = "TSL";
    Drawable AlphaChangedLockButton;
    Dialog myDialog;
    Drawable ChangedLockButtonIcon;
    HorizontalScrollView IconSelectScrollView;
    Intent Service_Intent = null;
    private BottomNavigationView bottomNavigationView;
    Drawable SizeChangedLockButton;
    private float UPDATE_TIP_VER = 0.0f;
    TextView alphaSeekbar;
    private BackPressCloseHandler backPressCloseHandler;
    NotificationCompat.Builder builder;
    SeekBar changeIconAlpha_seekbar;
    SeekBar changeIconSize_seekbar;
    private int count = 0;
    Dialog dialog = null;
    private ImageView guideImg;


    private NotificationChannel mNC;
    private NotificationManager mNM;
    private Notification mNoti;
    ComponentName mService;

    public MyApplication myApp;
    private Switch mySwitch;
    private Switch mySwitch_booting_start;

    public Switch mySwitch_continue_touch_unlock;
    private Switch mySwitch_half_lock_screen;
    private Switch mySwitch_lockbutton_hide;

    public Switch mySwitch_partial_touch_enable;
    private Switch mySwitch_proxi;
    private Switch mySwitch_text_guide;
    private Switch mySwitch_unlock_using_volume_longkey;
    RadioGroup radioGroup;
    RadioButton rb1;
    RadioButton rb2;
    RadioButton rb3;
    RadioButton rb4;
    RadioButton rb5;
    TextView sizeSeekbar;


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setRequestedOrientation(1);
        setContentView(R.layout.activity_main);
        myDialog = new Dialog(this);
        this.IconSelectScrollView = (HorizontalScrollView) findViewById(R.id.horizontalScrollView1);
        this.myApp = (MyApplication) getApplicationContext();
        this.Service_Intent = new Intent(this, UnLockService.class);
        DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
        int i = displayMetrics.widthPixels;
        int i2 = displayMetrics.heightPixels;
        this.myApp.set_Display_Size_X(i);
        this.myApp.set_Display_Size(i2);
        Switch switchR = (Switch) findViewById(R.id.mySwitch);
        this.mySwitch = switchR;
        switchR.setChecked(this.myApp.get_Service_Status());
        Switch switchR2 = (Switch) findViewById(R.id.mySwitch2);
        this.mySwitch_booting_start = switchR2;
        switchR2.setChecked(this.myApp.get_Service_Booting_Start());
        Switch switchR3 = (Switch) findViewById(R.id.mySwitch3);
        this.mySwitch_proxi = switchR3;
        switchR3.setChecked(this.myApp.get_Service_Proxi());
        Switch switchR4 = (Switch) findViewById(R.id.mySwitch8);
        this.mySwitch_partial_touch_enable = switchR4;
        switchR4.setChecked(this.myApp.get_Partial_Touch_Enable());
        Switch switchR5 = (Switch) findViewById(R.id.mySwitch4);
        this.mySwitch_lockbutton_hide = switchR5;
        switchR5.setChecked(this.myApp.get_Service_unlock_button_hide());
        Switch switchR6 = (Switch) findViewById(R.id.mySwitch5);
        this.mySwitch_text_guide = switchR6;
        switchR6.setChecked(this.myApp.get_Service_Unlock_Noti_setting());
        Switch switchR7 = (Switch) findViewById(R.id.mySwitch6);
        this.mySwitch_continue_touch_unlock = switchR7;
        switchR7.setChecked(this.myApp.get_Service_continue_touch_unlock());
        this.changeIconSize_seekbar = (SeekBar) findViewById(R.id.seekBar1);
        this.sizeSeekbar = (TextView) findViewById(R.id.subtext_size_seekbar);
        this.changeIconAlpha_seekbar = (SeekBar) findViewById(R.id.seekBar2);
        this.alphaSeekbar = (TextView) findViewById(R.id.subtext_alpha_seekbar);
        bottomNavigationView=(BottomNavigationView)findViewById(R.id.navigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Locale locale = getResources().getConfiguration().locale;
                if (item.getItemId() == R.id.option_menu_how_to) {
                    ShowPopup();
                } else if (item.getItemId() == R.id.option_menu_share) {
                    Intent intent = new Intent("android.intent.action.SEND");
                    intent.addCategory("android.intent.category.DEFAULT");
                    if (locale.getLanguage().equals("ko")) {
                        intent.putExtra("android.intent.extra.SUBJECT", "SCREEN TOUCH!MODE!");
                        intent.putExtra("android.intent.extra.TEXT", "\n Prevent accidental screen touch mode."+getPackageName());
                        intent.putExtra("android.intent.extra.TITLE", "title");
                    } else {
                        intent.putExtra("android.intent.extra.SUBJECT", "★Accidental Screen TOUCH!! Mode ★");
                        intent.putExtra("android.intent.extra.TEXT", "\nScreen TOUCH ! check it out!! service.\n "+getPackageName());
                        intent.putExtra("android.intent.extra.TITLE", "Screen Touch!!");
                    }
                    intent.setType("text/plain");
                    startActivity(Intent.createChooser(intent, "share"));
                } else if (item.getItemId() == R.id.option_menu_evaluation) {
                    Intent intent2 = new Intent("android.intent.action.VIEW");
                    intent2.setData(Uri.parse("market://details?id=com.touch.lock.screen.touchlock"));
                    startActivity(intent2);
                }
                return false;
            }
        });
        this.mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                if (z) {
                    MainActivity.this.myApp.set_Service_Status(true);
                    MainActivity mainActivity = MainActivity.this;
                    mainActivity.startService(mainActivity.Service_Intent);
                    MainActivity.this.finish();
                    return;
                }
                MainActivity mainActivity2 = MainActivity.this;
                mainActivity2.stopService(mainActivity2.Service_Intent);
                MainActivity.this.myApp.set_Service_Status(false);
            }
        });
        this.mySwitch_booting_start.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                if (z) {
                    MainActivity.this.myApp.set_Service_Booting_Start(true);
                } else {
                    MainActivity.this.myApp.set_Service_Booting_Start(false);
                }
            }
        });
        this.mySwitch_proxi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                if (z) {
                    MainActivity.this.myApp.set_Service_Proxi(true);
                    if (MainActivity.this.myApp.get_Service_Status()) {
                        try {
                            PendingIntent.getService(MainActivity.this.getApplicationContext(), 0, new Intent("android.intent.action.unLockService.proximity_on"), 0).send();
                        } catch (PendingIntent.CanceledException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    MainActivity.this.myApp.set_Service_Proxi(false);
                    if (MainActivity.this.myApp.get_Service_Status()) {
                        try {
                            PendingIntent.getService(MainActivity.this.getApplicationContext(), 0, new Intent("android.intent.action.unLockService.proximity_off"), 0).send();
                        } catch (PendingIntent.CanceledException e2) {
                            e2.printStackTrace();
                        }
                    }
                }
            }
        });
        this.mySwitch_partial_touch_enable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                if (z) {
                    MainActivity.this.myApp.set_Partial_Touch_Enable(true);
                    MainActivity.this.myApp.set_Service_continue_touch_unlock(false);
                    MainActivity.this.mySwitch_continue_touch_unlock.setChecked(false);
                    return;
                }
                MainActivity.this.myApp.set_Partial_Touch_Enable(false);
            }
        });
        this.mySwitch_lockbutton_hide.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                if (z) {
                    MainActivity.this.myApp.set_Service_unlock_button_hide(true);
                    Toast.makeText(MainActivity.this.getApplicationContext(), "Service After start, you can use the screen lock function from the Notification", Toast.LENGTH_LONG).show();
                    try {
                        PendingIntent.getService(MainActivity.this.getApplicationContext(), 0, new Intent("android.intent.action.unLockService.unlock_button_hide_on"), 0).send();
                    } catch (PendingIntent.CanceledException e) {
                        e.printStackTrace();
                    }
                } else {
                    MainActivity.this.myApp.set_Service_unlock_button_hide(false);
                    try {
                        PendingIntent.getService(MainActivity.this.getApplicationContext(), 0, new Intent("android.intent.action.unLockService.unlock_button_hide_off"), 0).send();
                    } catch (PendingIntent.CanceledException e2) {
                        e2.printStackTrace();
                    }
                }
            }
        });
        this.mySwitch_text_guide.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                if (z) {
                    MainActivity.this.myApp.set_Service_Unlock_Noti_setting(true);
                } else {
                    MainActivity.this.myApp.set_Service_Unlock_Noti_setting(false);
                }
            }
        });
        this.mySwitch_continue_touch_unlock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                if (z) {
                    MainActivity.this.myApp.set_Service_continue_touch_unlock(true);
                    MainActivity.this.myApp.set_Partial_Touch_Enable(false);
                    MainActivity.this.mySwitch_partial_touch_enable.setChecked(false);
                    return;
                }
                MainActivity.this.myApp.set_Service_continue_touch_unlock(false);
            }
        });
        this.radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        this.rb1 = (RadioButton) findViewById(R.id.rb1);
        this.rb2 = (RadioButton) findViewById(R.id.rb2);
        this.rb3 = (RadioButton) findViewById(R.id.rb3);
        this.rb4 = (RadioButton) findViewById(R.id.rb4);
        this.rb5 = (RadioButton) findViewById(R.id.rb5);
        this.radioGroup.setOnCheckedChangeListener(this);
        this.guideImg = (ImageView) findViewById(R.id.guideimg1);

        Resources resources = getResources();
        Drawable drawable = resources.getDrawable(R.drawable.unlocked);
        if (this.myApp.get_Lock_Button_Icon() != 0) {
            drawable = resources.getDrawable(this.myApp.get_Lock_Button_Icon());
        }
        this.ChangedLockButtonIcon = new BitmapDrawable(resources, Bitmap.createScaledBitmap(((BitmapDrawable) drawable).getBitmap(), 200, 200, true));
        this.changeIconSize_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                if (i < 10) {
                    i = 10;
                }
                MainActivity.this.myApp.set_Service_Lock_Icon_Size(i);
                MainActivity.this.changeLockIconSize();
            }
        });
        this.changeIconAlpha_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                if (i < 10) {
                    i = 10;
                }
                MainActivity.this.myApp.set_Service_Lock_Icon_Alpha(i);
                MainActivity.this.changeLockIconAlpha();
            }
        });
        ImageView imageView = (ImageView) findViewById(R.id.lockIcon_default);
        ImageView imageView2 = (ImageView) findViewById(R.id.lockIcon1);
        ImageView imageView3 = (ImageView) findViewById(R.id.lockIcon2);
        ImageView imageView4 = (ImageView) findViewById(R.id.lockIcon3);
        ImageView imageView5 = (ImageView) findViewById(R.id.lockIcon4);
        ImageView imageView6 = (ImageView) findViewById(R.id.lockIcon5);
        ImageView imageView7 = (ImageView) findViewById(R.id.lockIcon6);
        ImageView imageView8 = (ImageView) findViewById(R.id.lockIcon7);
        ImageView imageView9 = (ImageView) findViewById(R.id.lockIcon8);
        ImageView imageView10 = (ImageView) findViewById(R.id.lockIcon9);
        ImageView imageView11 = (ImageView) findViewById(R.id.lockIcon11);
        ImageView imageView12 = (ImageView) findViewById(R.id.lockIcon21);
        ImageView imageView13 = (ImageView) findViewById(R.id.lockIcon31);
        ImageView imageView14 = (ImageView) findViewById(R.id.lockIcon41);
        ImageView imageView15 = (ImageView) findViewById(R.id.lockIcon51);
        ImageView imageView16 = (ImageView) findViewById(R.id.lockIcon61);
        ImageView imageView17 = (ImageView) findViewById(R.id.lockIcon71);
        ImageView imageView18 = (ImageView) findViewById(R.id.lockIcon81);
        ImageView imageView19 = (ImageView) findViewById(R.id.lockIcon91);
        ImageView imageView20 = (ImageView) findViewById(R.id.lockIcon12);
        ImageView imageView21 = (ImageView) findViewById(R.id.lockIcon22);
        ImageView imageView22 = (ImageView) findViewById(R.id.lockIcon32);
        ImageView imageView23 = (ImageView) findViewById(R.id.lockIcon42);
        ImageView imageView24 = (ImageView) findViewById(R.id.lockIcon52);
        ImageView imageView25 = (ImageView) findViewById(R.id.lockIcon62);
        ImageView imageView26 = (ImageView) findViewById(R.id.lockIcon72);
        ImageView imageView27 = (ImageView) findViewById(R.id.lockIcon82);
        ImageView imageView28 = (ImageView) findViewById(R.id.lockIcon92);
        ImageView imageView29 = (ImageView) findViewById(R.id.lockIcon13);
        ImageView imageView30 = (ImageView) findViewById(R.id.lockIcon23);
        ImageView imageView31 = (ImageView) findViewById(R.id.lockIcon33);
        ImageView imageView32 = (ImageView) findViewById(R.id.lockIcon43);
        ImageView imageView33 = (ImageView) findViewById(R.id.lockIcon53);
        ImageView imageView34 = (ImageView) findViewById(R.id.lockIcon63);
        ImageView imageView35 = (ImageView) findViewById(R.id.lockIcon73);
        ImageView imageView36 = (ImageView) findViewById(R.id.lockIcon83);
        ImageView imageView37 = (ImageView) findViewById(R.id.lockIcon93);
        imageView.setAdjustViewBounds(true);
        imageView.setMaxWidth(200);
        imageView.setMaxHeight(200);
        imageView2.setAdjustViewBounds(true);
        imageView2.setMaxWidth(200);
        imageView2.setMaxHeight(200);
        imageView3.setAdjustViewBounds(true);
        imageView3.setMaxWidth(200);
        imageView3.setMaxHeight(200);
        imageView4.setAdjustViewBounds(true);
        imageView4.setMaxWidth(200);
        imageView4.setMaxHeight(200);
        imageView5.setAdjustViewBounds(true);
        imageView5.setMaxWidth(200);
        imageView5.setMaxHeight(200);
        imageView6.setAdjustViewBounds(true);
        imageView6.setMaxWidth(200);
        imageView6.setMaxHeight(200);
        imageView7.setAdjustViewBounds(true);
        imageView7.setMaxWidth(200);
        imageView7.setMaxHeight(200);
        imageView8.setAdjustViewBounds(true);
        imageView8.setMaxWidth(200);
        imageView8.setMaxHeight(200);
        imageView9.setAdjustViewBounds(true);
        imageView9.setMaxWidth(200);
        imageView9.setMaxHeight(200);
        imageView10.setAdjustViewBounds(true);
        imageView10.setMaxWidth(200);
        imageView10.setMaxHeight(200);
        imageView11.setAdjustViewBounds(true);
        imageView11.setMaxWidth(200);
        imageView11.setMaxHeight(200);
        imageView12.setAdjustViewBounds(true);
        imageView12.setMaxWidth(200);
        imageView12.setMaxHeight(200);
        imageView13.setAdjustViewBounds(true);
        imageView13.setMaxWidth(200);
        imageView13.setMaxHeight(200);
        imageView14.setAdjustViewBounds(true);
        imageView14.setMaxWidth(200);
        imageView14.setMaxHeight(200);
        ImageView imageView38 = imageView14;
        ImageView imageView39 = imageView15;
        imageView39.setAdjustViewBounds(true);
        imageView39.setMaxWidth(200);
        imageView39.setMaxHeight(200);
        ImageView imageView40 = imageView16;
        imageView40.setAdjustViewBounds(true);
        imageView40.setMaxWidth(200);
        imageView40.setMaxHeight(200);
        ImageView imageView41 = imageView17;
        imageView41.setAdjustViewBounds(true);
        imageView41.setMaxWidth(200);
        imageView41.setMaxHeight(200);
        ImageView imageView42 = imageView18;
        imageView42.setAdjustViewBounds(true);
        imageView42.setMaxWidth(200);
        imageView42.setMaxHeight(200);
        ImageView imageView43 = imageView19;
        imageView43.setAdjustViewBounds(true);
        imageView43.setMaxWidth(200);
        imageView43.setMaxHeight(200);
        ImageView imageView44 = imageView20;
        imageView44.setAdjustViewBounds(true);
        imageView44.setMaxWidth(200);
        imageView44.setMaxHeight(200);
        ImageView imageView45 = imageView21;
        imageView45.setAdjustViewBounds(true);
        imageView45.setMaxWidth(200);
        imageView45.setMaxHeight(200);
        ImageView imageView46 = imageView22;
        imageView46.setAdjustViewBounds(true);
        imageView46.setMaxWidth(200);
        imageView46.setMaxHeight(200);
        ImageView imageView47 = imageView23;
        imageView47.setAdjustViewBounds(true);
        imageView47.setMaxWidth(200);
        imageView47.setMaxHeight(200);
        ImageView imageView48 = imageView24;
        imageView48.setAdjustViewBounds(true);
        imageView48.setMaxWidth(200);
        imageView48.setMaxHeight(200);
        ImageView imageView49 = imageView25;
        imageView49.setAdjustViewBounds(true);
        imageView49.setMaxWidth(200);
        imageView49.setMaxHeight(200);
        ImageView imageView50 = imageView26;
        imageView50.setAdjustViewBounds(true);
        imageView50.setMaxWidth(200);
        imageView50.setMaxHeight(200);
        ImageView imageView51 = imageView27;
        imageView51.setAdjustViewBounds(true);
        imageView51.setMaxWidth(200);
        imageView51.setMaxHeight(200);
        ImageView imageView52 = imageView28;
        imageView52.setAdjustViewBounds(true);
        imageView52.setMaxWidth(200);
        imageView52.setMaxHeight(200);
        ImageView imageView53 = imageView29;
        imageView53.setAdjustViewBounds(true);
        imageView53.setMaxWidth(200);
        imageView53.setMaxHeight(200);
        ImageView imageView54 = imageView30;
        imageView54.setAdjustViewBounds(true);
        imageView54.setMaxWidth(200);
        imageView54.setMaxHeight(200);
        ImageView imageView55 = imageView31;
        imageView55.setAdjustViewBounds(true);
        imageView55.setMaxWidth(200);
        imageView55.setMaxHeight(200);
        ImageView imageView56 = imageView32;
        imageView56.setAdjustViewBounds(true);
        imageView56.setMaxWidth(200);
        imageView56.setMaxHeight(200);
        ImageView imageView57 = imageView33;
        imageView57.setAdjustViewBounds(true);
        imageView57.setMaxWidth(200);
        imageView57.setMaxHeight(200);
        ImageView imageView58 = imageView34;
        imageView58.setAdjustViewBounds(true);
        imageView58.setMaxWidth(200);
        imageView58.setMaxHeight(200);
        ImageView imageView59 = imageView35;
        imageView59.setAdjustViewBounds(true);
        imageView59.setMaxWidth(200);
        imageView59.setMaxHeight(200);
        ImageView imageView60 = imageView36;
        imageView60.setAdjustViewBounds(true);
        imageView60.setMaxWidth(200);
        imageView60.setMaxHeight(200);
        imageView37.setAdjustViewBounds(true);
        imageView37.setMaxWidth(200);
        imageView37.setMaxHeight(200);
        imageView.setImageDrawable(getResources().getDrawable(R.drawable.unlocked));
        imageView2.setImageDrawable(getResources().getDrawable(R.drawable.unlocked_10));
        imageView3.setImageDrawable(getResources().getDrawable(R.drawable.unlocked_20));
        imageView4.setImageDrawable(getResources().getDrawable(R.drawable.unlocked_30));
        imageView5.setImageDrawable(getResources().getDrawable(R.drawable.unlocked_40));
        imageView6.setImageDrawable(getResources().getDrawable(R.drawable.unlocked_50));
        imageView7.setImageDrawable(getResources().getDrawable(R.drawable.unlocked_60));
        imageView8.setImageDrawable(getResources().getDrawable(R.drawable.unlocked_70));
        imageView9.setImageDrawable(getResources().getDrawable(R.drawable.unlocked_80));
        imageView10.setImageDrawable(getResources().getDrawable(R.drawable.unlocked_90));
        imageView11.setImageDrawable(getResources().getDrawable(R.drawable.unlocked_11));
        imageView12.setImageDrawable(getResources().getDrawable(R.drawable.unlocked_21));
        imageView13.setImageDrawable(getResources().getDrawable(R.drawable.unlocked_31));
        imageView38.setImageDrawable(getResources().getDrawable(R.drawable.unlocked_41));
        imageView15.setImageDrawable(getResources().getDrawable(R.drawable.unlocked_51));
        imageView16.setImageDrawable(getResources().getDrawable(R.drawable.unlocked_61));
        imageView17.setImageDrawable(getResources().getDrawable(R.drawable.unlocked_71));
        imageView18.setImageDrawable(getResources().getDrawable(R.drawable.unlocked_81));
        imageView19.setImageDrawable(getResources().getDrawable(R.drawable.unlocked_91));
        imageView20.setImageDrawable(getResources().getDrawable(R.drawable.unlocked_12));
        imageView21.setImageDrawable(getResources().getDrawable(R.drawable.unlocked_22));
        imageView22.setImageDrawable(getResources().getDrawable(R.drawable.unlocked_32));
        imageView23.setImageDrawable(getResources().getDrawable(R.drawable.unlocked_42));
        imageView24.setImageDrawable(getResources().getDrawable(R.drawable.unlocked_52));
        imageView25.setImageDrawable(getResources().getDrawable(R.drawable.unlocked_62));
        imageView26.setImageDrawable(getResources().getDrawable(R.drawable.unlocked_72));
        imageView27.setImageDrawable(getResources().getDrawable(R.drawable.unlocked_82));
        imageView28.setImageDrawable(getResources().getDrawable(R.drawable.unlocked_92));
        imageView29.setImageDrawable(getResources().getDrawable(R.drawable.unlocked_13));
        imageView30.setImageDrawable(getResources().getDrawable(R.drawable.unlocked_23));
        imageView31.setImageDrawable(getResources().getDrawable(R.drawable.unlocked_33));
        imageView32.setImageDrawable(getResources().getDrawable(R.drawable.unlocked_43));
        imageView33.setImageDrawable(getResources().getDrawable(R.drawable.unlocked_53));
        imageView34.setImageDrawable(getResources().getDrawable(R.drawable.unlocked_63));
        imageView35.setImageDrawable(getResources().getDrawable(R.drawable.unlocked_73));
        imageView60.setImageDrawable(getResources().getDrawable(R.drawable.unlocked_83));
        imageView37.setImageDrawable(getResources().getDrawable(R.drawable.unlocked_93));
        this.backPressCloseHandler = new BackPressCloseHandler(this);
        int i3 = Build.VERSION.SDK_INT;
        createNotificationChannel();
        //setFullAd();
        final int lockIconScrolPositionX = getLockIconScrolPositionX();
        this.IconSelectScrollView.post(new Runnable() {
            public void run() {
                MainActivity.this.IconSelectScrollView.scrollTo(lockIconScrolPositionX, 0);
            }
        });
    }

    public int getLockIconScrolPositionX() {
        switch (this.myApp.get_Lock_Button_Icon()) {
            case R.drawable.unlocked_10 :
                return 240;
            case R.drawable.unlocked_11 :
                return 2400;
            case R.drawable.unlocked_12 :
                return 4560;
            case R.drawable.unlocked_13 :
                return 6720;
            case R.drawable.unlocked_20 :
                return 480;
            case R.drawable.unlocked_21 :
                return 2640;
            case R.drawable.unlocked_22 :
                return 4800;
            case R.drawable.unlocked_23 :
                return 6960;
            case R.drawable.unlocked_30 :
                return 720;
            case R.drawable.unlocked_31 :
                return 2880;
            case R.drawable.unlocked_32 :
                return 5040;
            case R.drawable.unlocked_33 :
                return 7200;
            case R.drawable.unlocked_40 :
                return 960;
            case R.drawable.unlocked_41 :
                return 3120;
            case R.drawable.unlocked_42 :
                return 5280;
            case R.drawable.unlocked_43 :
                return 7440;
            case R.drawable.unlocked_50 :
                return 1200;
            case R.drawable.unlocked_51 :
                return 3360;
            case R.drawable.unlocked_52 :
                return 5520;
            case R.drawable.unlocked_53 :
                return 7680;
            case R.drawable.unlocked_60 :
                return 1440;
            case R.drawable.unlocked_61 :
                return 3600;
            case R.drawable.unlocked_62 :
                return 5760;
            case R.drawable.unlocked_63 :
                return 7920;
            case R.drawable.unlocked_70 :
                return 1680;
            case R.drawable.unlocked_71 :
                return 3840;
            case R.drawable.unlocked_72:

            case R.drawable.unlocked_73 :
                return 8160;
            case R.drawable.unlocked_80 :
                return 1920;
            case R.drawable.unlocked_81 :
                return 4080;
            case R.drawable.unlocked_82 :
                return 6240;
            case R.drawable.unlocked_83 :
                return 8400;
            case R.drawable.unlocked_90 :
                return 2160;
            case R.drawable.unlocked_91 :
                return 4320;
            case R.drawable.unlocked_92 :
                return 6480;
            case R.drawable.unlocked_93 :
                return 8640;
            default:
                return 0;
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel notificationChannel = new NotificationChannel("notouch_noti_ch", "No_touch_ch", NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("noti_channel_desc");
            ((NotificationManager) getSystemService(NotificationManager.class)).createNotificationChannel(notificationChannel);
        }
    }



    public float convertDpToPixel(float f) {
        return f * (((float) getResources().getDisplayMetrics().densityDpi) / 160.0f);
    }

    public void changeLockIconSize() {
        this.SizeChangedLockButton = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(((BitmapDrawable) this.ChangedLockButtonIcon).getBitmap(), (int) convertDpToPixel((float) this.myApp.get_Service_Lock_Icon_Size()), (int) convertDpToPixel((float) this.myApp.get_Service_Lock_Icon_Size()), true));
        this.changeIconSize_seekbar.setProgress(this.myApp.get_Service_Lock_Icon_Size());
        this.changeIconSize_seekbar.setThumb(this.SizeChangedLockButton);
    }

    public void changeLockIconAlpha() {
        Drawable drawable = this.ChangedLockButtonIcon;
        drawable.setAlpha((this.myApp.get_Service_Lock_Icon_Alpha() * 255) / 100);
        this.changeIconAlpha_seekbar.setThumb(drawable);
    }

    public void onDestroy() {
        super.onDestroy();
    }


    public void onStop() {
        super.onStop();
        Dialog dialog2 = this.dialog;
        if (dialog2 != null) {
            dialog2.dismiss();
            this.dialog = null;
        }
    }


    public void onPause() {
        super.onPause();
    }

    private boolean isMyServiceRunning(Context context, String str) {
        for (ActivityManager.RunningServiceInfo runningServiceInfo : ((ActivityManager) context.getSystemService(ACTIVITY_SERVICE)).getRunningServices(Integer.MAX_VALUE)) {
            if (str.equals(runningServiceInfo.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public synchronized void onResume() {
        Drawable drawable;
        super.onResume();
        if (Build.VERSION.SDK_INT >= 26) {
            Settings.System.canWrite(this);
        }
        stopService(new Intent(this, UnLockService.class));
        this.myApp.set_Service_Status(false);
        Resources resources = getResources();
        resources.getDrawable(R.drawable.unlocked);
        if (this.myApp.get_Lock_Button_Icon() != 0) {
            drawable = resources.getDrawable(this.myApp.get_Lock_Button_Icon());
        } else {
            Drawable drawable2 = resources.getDrawable(R.drawable.unlocked);
            this.myApp.set_Lock_Button_Icon(R.drawable.unlocked);
            drawable = drawable2;
        }
        this.ChangedLockButtonIcon = new BitmapDrawable(resources, Bitmap.createScaledBitmap(((BitmapDrawable) drawable).getBitmap(), 200, 200, true));
        this.mySwitch.setChecked(this.myApp.get_Service_Status());
        changeLockIconSize();
        this.changeIconSize_seekbar.setProgress(this.myApp.get_Service_Lock_Icon_Size());
        changeLockIconAlpha();
        this.changeIconAlpha_seekbar.setProgress(this.myApp.get_Service_Lock_Icon_Alpha());
        this.mySwitch.setChecked(this.myApp.get_Service_Status());
        this.mySwitch_booting_start.setChecked(this.myApp.get_Service_Booting_Start());
        this.mySwitch_proxi.setChecked(this.myApp.get_Service_Proxi());
        this.mySwitch_partial_touch_enable.setChecked(this.myApp.get_Partial_Touch_Enable());
        this.mySwitch_lockbutton_hide.setChecked(this.myApp.get_Service_unlock_button_hide());
        this.mySwitch_continue_touch_unlock.setChecked(this.myApp.get_Service_continue_touch_unlock());
        int i = this.myApp.get_Service_Mode();
        if (i == 0) {
            this.rb1.setChecked(true);
            this.guideImg.setImageResource(R.drawable.guide_img1);
        } else if (i == 1) {
            this.rb2.setChecked(true);
            this.guideImg.setImageResource(R.drawable.guide_img2);
        } else if (i == 2) {
            this.rb3.setChecked(true);
            this.guideImg.setImageResource(R.drawable.monster_1);
        } else if (i == 3) {
            this.rb4.setChecked(true);
            this.guideImg.setImageResource(R.drawable.guide_img4);
        } else if (i == 4) {
            this.rb5.setChecked(true);
            this.guideImg.setImageResource(R.drawable.guide_img5);
        }
        final int lockIconScrolPositionX = getLockIconScrolPositionX();
        this.IconSelectScrollView.post(new Runnable() {
            public void run() {
                MainActivity.this.IconSelectScrollView.scrollTo(lockIconScrolPositionX, 0);
            }
        });
    }








    public void onUserLeaveHint() {
        super.onUserLeaveHint();
        startService(this.Service_Intent);
    }

   /* public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        Locale locale = getResources().getConfiguration().locale;
        if (menuItem.getItemId() == R.id.option_menu_app_info) {
           // AppInfo_DialogHtmlView();
        } else if (menuItem.getItemId() == R.id.option_menu_how_to) {
           // HowTo_DialogHtmlView();
        } else if (menuItem.getItemId() == R.id.option_menu_share) {
            Intent intent = new Intent("android.intent.action.SEND");
            intent.addCategory("android.intent.category.DEFAULT");
            if (locale.getLanguage().equals("ko")) {
                intent.putExtra("android.intent.extra.SUBJECT", "NO TOUCH!!");
                intent.putExtra("android.intent.extra.TEXT", "\n promotional text\n https://play.google.com/store/apps/details?id=johnny.studio.notouch");
                intent.putExtra("android.intent.extra.TITLE", "제목");
            } else {
                intent.putExtra("android.intent.extra.SUBJECT", "★Release NO TOUCH!! service ★");
                intent.putExtra("android.intent.extra.TEXT", "\nNO TOUCH is released! Let me introduce NO TOUCH!! service.\n https://play.google.com/store/apps/details?id=johnny.studio.notouch");
                intent.putExtra("android.intent.extra.TITLE", "NO TOUCH!!");
            }
            intent.setType("text/plain");
            startActivity(Intent.createChooser(intent, "share"));
        } else if (menuItem.getItemId() == R.id.option_menu_evaluation) {
            Intent intent2 = new Intent("android.intent.action.VIEW");
            intent2.setData(Uri.parse("market://details?id=johnny.studio.notouch"));
            startActivity(intent2);
        }
        return super.onOptionsItemSelected(menuItem);
    }*/

    public void onCheckedChanged(RadioGroup radioGroup2, int i) {
        if (this.radioGroup.getCheckedRadioButtonId() == R.id.rb1) {
            this.myApp.set_Service_Mode(0);
            this.guideImg.setImageResource(R.drawable.guide_img1);
        } else if (this.radioGroup.getCheckedRadioButtonId() == R.id.rb2) {
            this.myApp.set_Service_Mode(1);
            this.guideImg.setImageResource(R.drawable.guide_img2);
        } else if (this.radioGroup.getCheckedRadioButtonId() == R.id.rb3) {
            this.myApp.set_Service_Mode(2);
            this.guideImg.setImageResource(R.drawable.monster_1);
        } else if (this.radioGroup.getCheckedRadioButtonId() == R.id.rb4) {
            this.myApp.set_Service_Mode(3);
            this.guideImg.setImageResource(R.drawable.guide_img4);
        } else if (this.radioGroup.getCheckedRadioButtonId() == R.id.rb5) {
            this.myApp.set_Service_Mode(4);
            this.guideImg.setImageResource(R.drawable.guide_img5);
        }
    }

    public void onBackPressed() {
        new AlertDialog.Builder(this).setIcon(17301543).setTitle(R.string.hello).setMessage(R.string.Service_End_Alert_Dialog).setPositiveButton(R.string.Service_End_Alert_Dialog_Start, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                MainActivity.this.myApp.set_Service_Status(true);
                MainActivity mainActivity = MainActivity.this;
                mainActivity.startService(mainActivity.Service_Intent);
                //MainActivity.this.displayAD();
                MainActivity.this.finish();
            }
        }).setNegativeButton(R.string.Service_End_Alert_Dialog_Finish, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                //MainActivity.this.displayAD();
                MainActivity.this.finish();
            }
        }).show();
    }

    public void icon_iv_Click(View view) {
        int id = view.getId();
        int i = R.drawable.unlocked;
        switch (id) {
            case R.id.lockIcon1 :
                i = R.drawable.unlocked_10;
                break;
            case R.id.lockIcon11 :
                i = R.drawable.unlocked_11;
                break;
            case R.id.lockIcon12 :
                i = R.drawable.unlocked_12;
                break;
            case R.id.lockIcon13 :
                i = R.drawable.unlocked_13;
                break;
            case R.id.lockIcon2 :
                i = R.drawable.unlocked_20;
                break;
            case R.id.lockIcon21 :
                i = R.drawable.unlocked_21;
                break;
            case R.id.lockIcon22 :
                i = R.drawable.unlocked_22;
                break;
            case R.id.lockIcon23 :
                i = R.drawable.unlocked_23;
                break;
            case R.id.lockIcon3 :
                i = R.drawable.unlocked_30;
                break;
            case R.id.lockIcon31 :
                i = R.drawable.unlocked_31;
                break;
            case R.id.lockIcon32 :
                i = R.drawable.unlocked_32;
                break;
            case R.id.lockIcon33 :
                i = R.drawable.unlocked_33;
                break;
            case R.id.lockIcon4 :
                i = R.drawable.unlocked_40;
                break;
            case R.id.lockIcon41 :
                i = R.drawable.unlocked_41;
                break;
            case R.id.lockIcon42 :
                i = R.drawable.unlocked_42;
                break;
            case R.id.lockIcon43 :
                i = R.drawable.unlocked_43;
                break;
            case R.id.lockIcon5 :
                i = R.drawable.unlocked_50;
                break;
            case R.id.lockIcon51 :
                i = R.drawable.unlocked_51;
                break;
            case R.id.lockIcon52 :
                i = R.drawable.unlocked_52;
                break;
            case R.id.lockIcon53 :
                i = R.drawable.unlocked_53;
                break;
            case R.id.lockIcon6 :
                i = R.drawable.unlocked_60;
                break;
            case R.id.lockIcon61 :
                i = R.drawable.unlocked_61;
                break;
            case R.id.lockIcon62 :
                i = R.drawable.unlocked_62;
                break;
            case R.id.lockIcon63 :
                i = R.drawable.unlocked_63;
                break;
            case R.id.lockIcon7 :
                i = R.drawable.unlocked_70;
                break;
            case R.id.lockIcon71 :
                i = R.drawable.unlocked_71;
                break;
            case R.id.lockIcon72 :
                i = R.drawable.unlocked_72;
                break;
            case R.id.lockIcon73 :
                i = R.drawable.unlocked_73;
                break;
            case R.id.lockIcon8 :
                i = R.drawable.unlocked_80;
                break;
            case R.id.lockIcon81 :
                i = R.drawable.unlocked_81;
                break;
            case R.id.lockIcon82 :
                i = R.drawable.unlocked_82;
                break;
            case R.id.lockIcon83 :
                i = R.drawable.unlocked_83;
                break;
            case R.id.lockIcon9 :
                i = R.drawable.unlocked_90;
                break;
            case R.id.lockIcon91 :
                i = R.drawable.unlocked_91;
                break;
            case R.id.lockIcon92 :
                i = R.drawable.unlocked_92;
                break;
            case R.id.lockIcon93 :
                i = R.drawable.unlocked_93;
                break;
        }
        this.myApp.set_Lock_Button_Icon(i);
        Resources resources = getResources();
        this.ChangedLockButtonIcon = new BitmapDrawable(resources, Bitmap.createScaledBitmap(((BitmapDrawable) resources.getDrawable(i)).getBitmap(), 200, 200, true));
        changeLockIconSize();
        changeLockIconAlpha();
        // ads place here
    }

    public void ShowPopup() {
        TextView txtclose;

        myDialog.setContentView(R.layout.custom_popup);
        myDialog.setCancelable(false);
        txtclose =(TextView) myDialog.findViewById(R.id.txtclose);
        txtclose.setText("X");
        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }
}
