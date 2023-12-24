package com.touch.lock.screen.touchlock;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;


public class MyApplication extends Application {


    private static final boolean f93D = false;
    public static final String KEY_DISPLAY_SIZE = "1080";
    public static final String KEY_DISPLAY_SIZE_X = "1355";
    public static final String KEY_HALF_TOUCH_ENABLE = "false";
    public static final String KEY_LOCK_BUTTON_ICON = "0";
    public static final String KEY_PARTIAL_TOUCH_ENABLE = "false";
    public static final String KEY_SERVICE_ACCEL = "false";
    public static final String KEY_SERVICE_BOOTING_START = "false";
    public static final String KEY_SERVICE_CONTINUE_TOUCH_UNLOCK = "true";
    public static final String KEY_SERVICE_LOCK_ICON_ALPHA = "60";
    public static final String KEY_SERVICE_LOCK_ICON_SIZE = "60";
    public static final String KEY_SERVICE_MODE = "0";
    public static final String KEY_SERVICE_PROXI = "false";
    public static final String KEY_SERVICE_STATUS = "false";
    public static final String KEY_SERVICE_UNLOCK_BUTTON_HIDE = "false";
    public static final String KEY_SERVICE_UNLOCK_NOTI = "true";
    public static final String KEY_SERVICE_UPDATE_TIP = "0";
    public static final String KEY_SERVICE_VISIBLE = "false";
    public static final String KEY_UNLOCK_USING_VOLUME_KEY = "false";
    private static final String TAG = "TSL";


    public void attachBaseContext(Context context) {
        super.attachBaseContext(context);

    }

    public void onCreate() {
        super.onCreate();
    }

    public void onTerminate() {
        super.onTerminate();
    }

    public void set_Service_Status(boolean z) {
        SharedPreferences.Editor edit = getSharedPreferences("Service_Status", 0).edit();
        edit.putBoolean("false", z);
        edit.apply();
    }

    public boolean get_Service_Status() {
        return getSharedPreferences("Service_Status", 0).getBoolean("false", false);
    }

    public void set_Service_Mode(int i) {
        SharedPreferences.Editor edit = getSharedPreferences("Service_Mode", 0).edit();
        edit.putInt("0", i);
        edit.apply();
    }

    public int get_Service_Mode() {
        return getSharedPreferences("Service_Mode", 0).getInt("0", 0);
    }

    public void set_Service_Update_Tip_ver(int i) {
        SharedPreferences.Editor edit = getSharedPreferences("Service_Update_Tip_ver", 0).edit();
        edit.putInt("0", i);
        edit.apply();
    }

    public int get_Service_Update_Tip_ver() {
        return getSharedPreferences("Service_Update_Tip_ver", 0).getInt("0", 0);
    }

    public void set_Service_Booting_Start(boolean z) {
        SharedPreferences.Editor edit = getSharedPreferences("Service_Booting_Start", 0).edit();
        edit.putBoolean("false", z);
        edit.apply();
    }

    public boolean get_Service_Booting_Start() {
        return getSharedPreferences("Service_Booting_Start", 0).getBoolean("false", false);
    }

    public void set_Service_Proxi(boolean z) {
        SharedPreferences.Editor edit = getSharedPreferences("Service_Proxi", 0).edit();
        edit.putBoolean("false", z);
        edit.apply();
    }

    public boolean get_Service_Proxi() {
        return getSharedPreferences("Service_Proxi", 0).getBoolean("false", false);
    }

    public void set_Service_Accel(boolean z) {
        SharedPreferences.Editor edit = getSharedPreferences("Service_Accel", 0).edit();
        edit.putBoolean("false", z);
        edit.apply();
    }

    public boolean get_Service_Accel() {
        return getSharedPreferences("Service_Accel", 0).getBoolean("false", false);
    }

    public void set_Partial_Touch_Enable(boolean z) {
        SharedPreferences.Editor edit = getSharedPreferences("Partial_Touch_Enable", 0).edit();
        edit.putBoolean("false", z);
        edit.apply();
    }

    public boolean get_Partial_Touch_Enable() {
        return getSharedPreferences("Partial_Touch_Enable", 0).getBoolean("false", false);
    }

    public void set_Half_Touch_Enable(boolean z) {
        SharedPreferences.Editor edit = getSharedPreferences("Half_Touch_Enable", 0).edit();
        edit.putBoolean("false", z);
        edit.apply();
    }

    public boolean get_Half_Touch_Enable() {
        return getSharedPreferences("Half_Touch_Enable", 0).getBoolean("false", false);
    }

    public void set_unlock_using_volume_longkey(boolean z) {
        SharedPreferences.Editor edit = getSharedPreferences("Unlock_Using_Volue_LongKey", 0).edit();
        edit.putBoolean("false", z);
        edit.apply();
    }

    public boolean get_unlock_using_volume_longkey() {
        return getSharedPreferences("Unlock_Using_Volue_LongKey", 0).getBoolean("false", false);
    }

    public void set_Service_unlock_button_hide(boolean z) {
        SharedPreferences.Editor edit = getSharedPreferences("Service_unlock_button_hide", 0).edit();
        edit.putBoolean("false", z);
        edit.apply();
    }

    public boolean get_Service_unlock_button_hide() {
        return getSharedPreferences("Service_unlock_button_hide", 0).getBoolean("false", false);
    }

    public void set_Service_continue_touch_unlock(boolean z) {
        SharedPreferences.Editor edit = getSharedPreferences("Service_continue_touch_unlock", 0).edit();
        edit.putBoolean("true", z);
        edit.apply();
    }

    public boolean get_Service_continue_touch_unlock() {
        return getSharedPreferences("Service_continue_touch_unlock", 0).getBoolean("true", false);
    }

    public void set_Service_Update_Info_Visible(boolean z) {
        SharedPreferences.Editor edit = getSharedPreferences("Service_Visible", 0).edit();
        edit.putBoolean("false", z);
        edit.apply();
    }

    public boolean get_Service_Update_Info_Visible() {
        return getSharedPreferences("Service_Visible", 0).getBoolean("false", true);
    }

    public void set_Service_Unlock_Noti_setting(boolean z) {
        SharedPreferences.Editor edit = getSharedPreferences("Service_Unlock_Noti_Setting", 0).edit();
        edit.putBoolean("true", z);
        edit.apply();
    }

    public boolean get_Service_Unlock_Noti_setting() {
        return getSharedPreferences("Service_Unlock_Noti_Setting", 0).getBoolean("true", true);
    }

    public void set_Service_Lock_Icon_Size(int i) {
        SharedPreferences.Editor edit = getSharedPreferences("Service_Lock_Icon_Size", 0).edit();
        edit.putInt("60", i);
        edit.apply();
    }

    public int get_Service_Lock_Icon_Size() {
        return getSharedPreferences("Service_Lock_Icon_Size", 0).getInt("60", 60);
    }

    public void set_Service_Lock_Icon_Alpha(int i) {
        SharedPreferences.Editor edit = getSharedPreferences("Service_Lock_Icon_Alpha", 0).edit();
        edit.putInt("60", i);
        edit.apply();
    }

    public int get_Service_Lock_Icon_Alpha() {
        return getSharedPreferences("Service_Lock_Icon_Alpha", 0).getInt("60", 60);
    }

    public void set_Display_Size(int i) {
        SharedPreferences.Editor edit = getSharedPreferences("Display_Size", 0).edit();
        edit.putInt(KEY_DISPLAY_SIZE, i);
        edit.apply();
    }

    public int get_Display_Size() {
        return getSharedPreferences("Display_Size", 0).getInt(KEY_DISPLAY_SIZE, 0);
    }

    public void set_Display_Size_X(int i) {
        SharedPreferences.Editor edit = getSharedPreferences("Display_Size_X", 0).edit();
        edit.putInt(KEY_DISPLAY_SIZE_X, i);
        edit.apply();
    }

    public int get_Display_Size_X() {
        return getSharedPreferences("Display_Size_X", 0).getInt(KEY_DISPLAY_SIZE_X, 0);
    }

    public void set_Lock_Button_Icon(int i) {
        SharedPreferences.Editor edit = getSharedPreferences("Lock_Button_Icon", 0).edit();
        edit.putInt("0", i);
        edit.apply();
    }

    public int get_Lock_Button_Icon() {
        return getSharedPreferences("Lock_Button_Icon", 0).getInt("0", 0);
    }
}
