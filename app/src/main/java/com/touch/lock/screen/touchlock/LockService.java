package com.touch.lock.screen.touchlock;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Vibrator;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.view.ViewCompat;

import java.util.Timer;
import java.util.TimerTask;

public class LockService extends Service {


    private static final boolean f94D = false;
    public static int LONG_PRESS_TIME = 3000;
    private static final String TAG = "TSL";

    public boolean EnableHalfScreenLockGravityChange = false;

    public int Half_Screen_Lock_Gravity = 0;

    public TextView brightnessPercentage;
    NotificationCompat.Builder builder;
    private boolean changeScreenImg = true;
    private int currentApiVersion;
    private int currentBrightness = 255;

    public ImageView dragGuideImg;
    private TextView dragGuideText;

    public ImageView insertPaperImg;

    public boolean isDoubleTouchTimerEnd = true;

    public boolean isLongTouch = false;

    public boolean isMove = false;

    public boolean isTimerEnd = true;

    public ImageView lockedImg;

    public int mBrightness = 255;

    public float mFirstTouchX;

    public float mFirstTouchY;
    final Handler mHandler = new Handler();

    public int mInitBrightness = 255;
    Runnable mLongPressed = new Runnable() {
        public void run() {
            if (!LockService.this.isMove) {
                boolean unused = LockService.this.isLongTouch = true;
                LockService.this.brightnessPercentage.setVisibility(View.VISIBLE);
                if (LockService.this.myApp.get_Service_Mode() == 1) {
                    LockService.this.mView.setBackgroundColor(ViewCompat.MEASURED_STATE_MASK);
                    int unused2 = LockService.this.mBrightness = 0;
                    Settings.System.putInt(LockService.this.getContentResolver(), "screen_brightness", LockService.this.mBrightness);
                }
            }
        }
    };
    private WindowManager mManager;
    private NotificationManager mNM;
    private Notification mNoti;

    public WindowManager.LayoutParams mParams;

    public float mSecondTouchDeltaX;

    public float mSecondTouchDeltaY;
    private TimerTask mTask;
    private Timer mTimer;

    public float mTouchX;

    public float mTouchY;

    public View mView;
    private View.OnTouchListener mViewTouchListener = new View.OnTouchListener() {
        Animation ani01;
        AnimationSet set;

        public boolean onTouch(View view, MotionEvent motionEvent) {
            int action = motionEvent.getAction();
            if (action == 0) {
                if (LockService.this.myApp.get_Service_Mode() == 1) {
                    LockService.this.brightnessPercentage.setVisibility(View.VISIBLE);
                } else if (LockService.this.myApp.get_Service_Mode() == 4) {
                    boolean unused = LockService.this.EnableHalfScreenLockGravityChange = true;
                }
                boolean unused2 = LockService.this.isMove = false;
                boolean unused3 = LockService.this.isLongTouch = false;
                LockService.this.mHandler.postDelayed(LockService.this.mLongPressed, (long) LockService.LONG_PRESS_TIME);
                float unused4 = LockService.this.mTouchX = motionEvent.getRawX();
                float unused5 = LockService.this.mTouchY = motionEvent.getRawY();
                LockService lockService = LockService.this;
                int unused6 = lockService.mViewX = lockService.mParams.x;
                LockService lockService2 = LockService.this;
                int unused7 = lockService2.mViewY = lockService2.mParams.y;
                if (LockService.this.mBrightness <= 10) {
                    Settings.System.putInt(LockService.this.getContentResolver(), "screen_brightness", LockService.this.mInitBrightness);
                } else {
                    Settings.System.putInt(LockService.this.getContentResolver(), "screen_brightness", LockService.this.mBrightness);
                }
                if (LockService.this.isTimerEnd) {
                    boolean unused8 = LockService.this.isTimerEnd = false;
                    LockService.this.mTransferScreen(view);
                }
                if (LockService.this.isDoubleTouchTimerEnd) {
                    boolean unused9 = LockService.this.isDoubleTouchTimerEnd = false;
                    LockService lockService3 = LockService.this;
                    float unused10 = lockService3.mFirstTouchX = lockService3.mTouchX;
                    LockService lockService4 = LockService.this;
                    float unused11 = lockService4.mFirstTouchY = lockService4.mTouchY;
                    LockService.this.Double_Touch_Check();
                } else {
                    LockService lockService5 = LockService.this;
                    float unused12 = lockService5.mSecondTouchDeltaX = lockService5.mFirstTouchX - LockService.this.mTouchX;
                    LockService lockService6 = LockService.this;
                    float unused13 = lockService6.mSecondTouchDeltaY = lockService6.mFirstTouchY - LockService.this.mTouchY;
                    if (LockService.this.mSecondTouchDeltaX < 40.0f && LockService.this.mSecondTouchDeltaY < 40.0f) {
                        LockService.access$1908(LockService.this);
                        if (LockService.this.threeTouchCount > 0) {
                            if (LockService.this.myApp.get_Partial_Touch_Enable()) {
                                if (LockService.this.nowPatialTouchEnable) {
                                    LockService.this.nowPatialTouchEnable = false;
                                    LockService.this.enableTouchSpaceOpen();
                                } else {
                                    LockService.this.nowPatialTouchEnable = true;
                                    LockService.this.disableTouchSpaceOpen();
                                }
                            } else if (LockService.this.myApp.get_Service_continue_touch_unlock()) {
                                LockService.this.Change_to_UnlockService();
                            }
                        }
                    }
                }
                AnimationSet animationSet = new AnimationSet(true);
                this.set = animationSet;
                animationSet.setInterpolator(new AccelerateInterpolator());
                AlphaAnimation alphaAnimation = new AlphaAnimation(0.3f, 1.0f);
                this.ani01 = alphaAnimation;
                alphaAnimation.setDuration(500);
                this.set.addAnimation(this.ani01);
                LockService.this.lockedImg.setAnimation(this.set);
            } else if (action == 1) {
                LockService.this.brightnessPercentage.setVisibility(4);
                if (LockService.this.isLongTouch) {
                    boolean access$000 = LockService.this.isMove;
                } else {
                    boolean unused14 = LockService.this.isMove;
                }
                LockService.this.mHandler.removeCallbacks(LockService.this.mLongPressed);
            } else if (action == 2) {
                boolean unused15 = LockService.this.isMove = true;
                int rawX = (int) (motionEvent.getRawX() - LockService.this.mTouchX);
                int rawY = (int) (motionEvent.getRawY() - LockService.this.mTouchY);
                int i = LockService.this.myApp.get_Display_Size();
                LockService.this.myApp.get_Display_Size_X();
                if (rawX <= -40 || rawX >= 40 || rawY <= -40 || rawY >= 40) {
                    LockService.this.mHandler.removeCallbacks(LockService.this.mLongPressed);
                    if (rawX <= -100 || rawX >= 100 || rawY <= -100 || rawY >= 100) {
                        if (LockService.this.myApp.get_Service_Mode() == 1) {
                            if (rawY < -500 || rawY > 100) {
                                LockService lockService7 = LockService.this;
                                int unused16 = lockService7.mBrightness = lockService7.mBrightness - ((rawY * 10) / i);
                            }
                            if (LockService.this.mBrightness <= 12) {
                                int unused17 = LockService.this.mBrightness = 10;
                                LockService.this.mView.setBackgroundColor(ViewCompat.MEASURED_STATE_MASK);
                            } else {
                                LockService.this.mView.setBackgroundColor(0);
                            }
                            if (LockService.this.mBrightness > LockService.this.mInitBrightness) {
                                LockService lockService8 = LockService.this;
                                int unused18 = lockService8.mBrightness = lockService8.mInitBrightness;
                                LockService.this.brightnessPercentage.setText("100 % ");
                            } else if (LockService.this.mBrightness == 10) {
                                LockService.this.brightnessPercentage.setText("0 % ");
                            } else {
                                TextView access$200 = LockService.this.brightnessPercentage;
                                access$200.setText(((LockService.this.mBrightness * 100) / LockService.this.mInitBrightness) + " % ");
                            }
                            LockService.this.brightnessPercentage.setBackgroundColor(ViewCompat.MEASURED_STATE_MASK);
                            LockService.this.brightnessPercentage.setPadding(20, 10, 20, 10);
                            Settings.System.putInt(LockService.this.getContentResolver(), "screen_brightness", LockService.this.mBrightness);
                        } else if (LockService.this.myApp.get_Service_Mode() == 2) {
                            LockService.this.vibrator.vibrate(100);
                            LockService.this.mediaPlayer.start();
                            LockService.this.dragGuideImg.setImageResource(R.drawable.monster_1);
                            AnimationSet animationSet2 = new AnimationSet(true);
                            this.set = animationSet2;
                            animationSet2.setInterpolator(new AccelerateInterpolator());
                            AlphaAnimation alphaAnimation2 = new AlphaAnimation(0.3f, 1.0f);
                            this.ani01 = alphaAnimation2;
                            alphaAnimation2.setDuration(500);
                            this.set.addAnimation(this.ani01);
                            LockService.this.dragGuideImg.setAnimation(this.set);
                        } else if (LockService.this.myApp.get_Service_Mode() == 4 && LockService.this.EnableHalfScreenLockGravityChange) {
                            if (rawY < -200) {
                                if (LockService.this.Half_Screen_Lock_Gravity > 0) {
                                    LockService.access$2410(LockService.this);
                                } else {
                                    int unused19 = LockService.this.Half_Screen_Lock_Gravity = 0;
                                }
                                boolean unused20 = LockService.this.EnableHalfScreenLockGravityChange = false;
                                LockService.this.nowPatialTouchEnable = true;
                                LockService.this.WrapWindowLock();
                            } else if (rawY > 200) {
                                if (LockService.this.Half_Screen_Lock_Gravity < 2) {
                                    LockService.access$2408(LockService.this);
                                } else {
                                    int unused21 = LockService.this.Half_Screen_Lock_Gravity = 2;
                                }
                                boolean unused22 = LockService.this.EnableHalfScreenLockGravityChange = false;
                                LockService.this.nowPatialTouchEnable = true;
                                LockService.this.WrapWindowLock();
                            }
                        }
                        if (LockService.this.isLongTouch && rawY > 100) {
                            LockService.this.mHandler.removeCallbacks(LockService.this.mLongPressed);
                            boolean unused23 = LockService.this.isLongTouch = false;
                        }
                        if (LockService.this.isLongTouch && rawX > 100) {
                            LockService.this.mHandler.removeCallbacks(LockService.this.mLongPressed);
                            boolean unused24 = LockService.this.isLongTouch = false;
                        }
                    } else {
                        boolean unused25 = LockService.this.isMove = false;
                    }
                } else {
                    boolean unused26 = LockService.this.isMove = false;
                }
            }
            return true;
        }
    };

    public int mViewX;

    public int mViewY;
    private ImageView mainScreenImg;
    private Runnable makeToast;
    Runnable mdisableTouchSpaceOpen_Timer = new Runnable() {
        public void run() {
            LockService.this.disableTouchSpaceOpen();
        }
    };

    public MediaPlayer mediaPlayer;

    public MyApplication myApp;

    public int newToastCount = 0;
    public boolean nowInsertPaper = true;
    public boolean nowPatialTouchEnable = true;
    Toast nowToast = null;
    private Paint paint = new Paint();

    public int threeTouchCount = 0;

    public Vibrator vibrator = null;

    public IBinder onBind(Intent intent) {
        return null;
    }

    static  int access$1908(LockService lockService) {
        int i = lockService.threeTouchCount;
        lockService.threeTouchCount = i + 1;
        return i;
    }

    static  int access$2408(LockService lockService) {
        int i = lockService.Half_Screen_Lock_Gravity;
        lockService.Half_Screen_Lock_Gravity = i + 1;
        return i;
    }

    static  int access$2410(LockService lockService) {
        int i = lockService.Half_Screen_Lock_Gravity;
        lockService.Half_Screen_Lock_Gravity = i - 1;
        return i;
    }

    public void onClickInsertPaperButton(View view) {
        if (this.nowInsertPaper) {
            this.nowInsertPaper = false;
            this.mView.setBackgroundColor(-1);
            return;
        }
        this.nowInsertPaper = true;
        this.mView.setBackgroundColor(0);
    }

    public void onClickLockButton(View view) {
        this.mBrightness = this.mInitBrightness;
        stopService(new Intent(this, LockService.class));
        startService(new Intent(this, UnLockService.class));
        this.myApp.set_Service_Status(false);
    }

    public void mOnlyLesteningMode() {
        if (this.myApp.get_Service_Mode() == 1) {
            this.dragGuideImg.setVisibility(View.VISIBLE);
            AnimationSet animationSet = new AnimationSet(true);
            animationSet.setInterpolator(new AccelerateInterpolator());
            AlphaAnimation alphaAnimation = new AlphaAnimation(0.3f, 1.0f);
            alphaAnimation.setDuration(500);
            animationSet.addAnimation(alphaAnimation);
            this.dragGuideImg.setAnimation(animationSet);
            this.mManager.updateViewLayout(this.mView, this.mParams);
        } else if (this.myApp.get_Service_Mode() == 2) {
            this.dragGuideImg.setVisibility(View.VISIBLE);
            AnimationSet animationSet2 = new AnimationSet(true);
            animationSet2.setInterpolator(new AccelerateInterpolator());
            AlphaAnimation alphaAnimation2 = new AlphaAnimation(0.3f, 1.0f);
            alphaAnimation2.setDuration(500);
            animationSet2.addAnimation(alphaAnimation2);
            this.dragGuideImg.setAnimation(animationSet2);
            this.mManager.updateViewLayout(this.mView, this.mParams);
        } else {
            this.mView.setBackgroundResource(R.drawable.main_screen);
            this.mManager.updateViewLayout(this.mView, this.mParams);
            this.dragGuideImg.setVisibility(View.INVISIBLE);
        }
    }


    public void enableTouchSpaceOpen() {
        this.mHandler.removeCallbacks(this.mdisableTouchSpaceOpen_Timer);
        this.mView.setBackgroundResource(R.drawable.main_screen);
        int i = Build.VERSION.SDK_INT >= 26 ? 2038 : 2002;
        if (this.myApp.get_Service_Mode() == 4) {
            this.mParams = new WindowManager.LayoutParams(-1, this.myApp.get_Display_Size() / 3, i, 1160, -3);
            int i2 = this.Half_Screen_Lock_Gravity;
            int i3 = 19;
            if (i2 == 0) {
                i3 = 51;
            } else if (i2 != 1 && i2 == 2) {
                i3 = 83;
            }
            this.mParams.gravity = i3;
        } else {
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(-1, -1, i, 1160, -3);
            this.mParams = layoutParams;
            layoutParams.gravity = 49;
        }
        this.mView.setSystemUiVisibility(5894);
        if (this.myApp.get_Service_Mode() == 1 && this.mBrightness == 10) {
            this.mView.setBackgroundColor(ViewCompat.MEASURED_STATE_MASK);
        }
        this.mManager.updateViewLayout(this.mView, this.mParams);}

    public void disableTouchSpaceOpen() {
        this.mHandler.removeCallbacks(this.mdisableTouchSpaceOpen_Timer);
        this.mView.setBackgroundResource(R.drawable.main_screen);
        int i = Build.VERSION.SDK_INT >= 26 ? 2038 : 2002;
        if (this.myApp.get_Service_Mode() == 4) {
            this.mParams = new WindowManager.LayoutParams(-1, this.myApp.get_Display_Size() / 3, i, 1160, -3);
            int i2 = this.Half_Screen_Lock_Gravity;
            int i3 = 19;
            if (i2 == 0) {
                i3 = 51;
            } else if (i2 != 1 && i2 == 2) {
                i3 = 83;
            }
            this.mParams.gravity = i3;
        } else {
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(-1, -1, i, 1160, -3);
            this.mParams = layoutParams;
            layoutParams.gravity = 49;
        }
        this.mView.setSystemUiVisibility(5894);
        if (this.myApp.get_Service_Mode() == 1 && this.mBrightness == 10) {
            this.mView.setBackgroundColor(ViewCompat.MEASURED_STATE_MASK);
        }
        this.mManager.updateViewLayout(this.mView, this.mParams);
    }

    public void mTransferScreen(View view) {
        if (this.myApp.get_Service_Unlock_Noti_setting()) {
            if (this.myApp.get_Service_Mode() == 1) {
                Toast toast = this.nowToast;
                if (toast == null) {
                    this.nowToast = Toast.makeText(getApplicationContext(), R.string.Ulock_Guide_Toast, 0);
                } else {
                    toast.setText(R.string.drag_guide);
                    this.newToastCount++;
                }
                this.nowToast.show();
            } else {
                Toast toast2 = this.nowToast;
                if (toast2 == null) {
                    this.nowToast = Toast.makeText(getApplicationContext(), R.string.Ulock_Guide_Toast, 0);
                } else {
                    toast2.setText(R.string.Ulock_Guide_Toast);
                }
                this.nowToast.show();
            }
        }
        this.lockedImg.setVisibility(0);
        if (this.myApp.get_Service_Mode() == 1) {
            this.dragGuideImg.setVisibility(0);
            AnimationSet animationSet = new AnimationSet(true);
            animationSet.setInterpolator(new AccelerateInterpolator());
            AlphaAnimation alphaAnimation = new AlphaAnimation(0.3f, 1.0f);
            alphaAnimation.setDuration(500);
            animationSet.addAnimation(alphaAnimation);
            this.dragGuideImg.setAnimation(animationSet);
        } else if (this.myApp.get_Service_Mode() == 2) {
            this.dragGuideImg.setVisibility(0);
            AnimationSet animationSet2 = new AnimationSet(true);
            animationSet2.setInterpolator(new AccelerateInterpolator());
            AlphaAnimation alphaAnimation2 = new AlphaAnimation(0.3f, 1.0f);
            alphaAnimation2.setDuration(500);
            animationSet2.addAnimation(alphaAnimation2);
            this.dragGuideImg.setAnimation(animationSet2);
        } else if (this.myApp.get_Service_Mode() == 3) {
            this.insertPaperImg.setVisibility(0);
            AnimationSet animationSet3 = new AnimationSet(true);
            animationSet3.setInterpolator(new AccelerateInterpolator());
            AlphaAnimation alphaAnimation3 = new AlphaAnimation(0.3f, 1.0f);
            alphaAnimation3.setDuration(500);
            animationSet3.addAnimation(alphaAnimation3);
            this.insertPaperImg.setAnimation(animationSet3);
        }
        Disappear_lock();
    }

    /* access modifiers changed from: package-private */
    public void Change_to_UnlockService() {
        stopSelf();
        startService(new Intent(this, UnLockService.class));
    }

    /* access modifiers changed from: package-private */
    public void Double_Touch_Check() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                boolean unused = LockService.this.isDoubleTouchTimerEnd = true;
                int unused2 = LockService.this.threeTouchCount = 0;
            }
        }, 300);
    }

    /* access modifiers changed from: package-private */
    public void Disappear_lock() {
        Handler handler = new Handler();
        Runnable r1 = new Runnable() {
            public void run() {
                Settings.System.putInt(LockService.this.getContentResolver(), "screen_brightness", LockService.this.mBrightness);
                LockService.this.lockedImg.setVisibility(8);
                LockService.this.insertPaperImg.setVisibility(8);
                if (LockService.this.myApp.get_Service_Mode() == 1) {
                    LockService.this.dragGuideImg.setVisibility(4);
                } else if (LockService.this.myApp.get_Service_Mode() == 2) {
                    LockService.this.dragGuideImg.setVisibility(4);
                } else if (LockService.this.myApp.get_Service_Mode() == 3) {
                    LockService.this.insertPaperImg.setVisibility(4);
                }
                boolean unused = LockService.this.isTimerEnd = true;
                if (LockService.this.newToastCount / 3 == 1) {
                    LockService.this.nowToast = null;
                    int unused2 = LockService.this.newToastCount = 0;
                }
            }
        };
        this.makeToast = r1;
        handler.postDelayed(r1, 4000);
    }

    public void onCreate() {
        int i;
        int i2;
        int i3;
        super.onCreate();
        this.myApp = (MyApplication) getApplicationContext();
        View inflate = ((LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.service_locked, (ViewGroup) null);
        this.mView = inflate;
        TextView textView = (TextView) inflate.findViewById(R.id.alpha_percentage);
        this.brightnessPercentage = textView;
        textView.setVisibility(4);
        this.lockedImg = (ImageView) this.mView.findViewById(R.id.lock_button);
        this.insertPaperImg = (ImageView) this.mView.findViewById(R.id.insert_paper_button);
        this.mainScreenImg = (ImageView) this.mView.findViewById(R.id.main_screen);
        this.dragGuideImg = (ImageView) this.mView.findViewById(R.id.drag_imageView);
        if (this.myApp.get_Service_Mode() == 1) {
            this.dragGuideImg.setImageResource(R.drawable.drag_guide);
        } else if (this.myApp.get_Service_Mode() == 2) {
            this.dragGuideImg.setImageResource(R.drawable.monster_1);
        } else {
            this.myApp.get_Service_Mode();
        }
        this.insertPaperImg.setVisibility(4);
        this.dragGuideImg.setVisibility(4);
        this.vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        this.mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.ifartinurgeneraldirection_alert2);
        Resources resources = getResources();
        int i4 = this.myApp.get_Lock_Button_Icon();
        int i5 = R.drawable.locked;
        switch (i4) {
            case R.drawable.unlocked_10 /*2131165677*/:
                i5 = R.drawable.locked_10;
                break;
            case R.drawable.unlocked_11 /*2131165678*/:
                i5 = R.drawable.locked_11;
                break;
            case R.drawable.unlocked_12 /*2131165679*/:
                i5 = R.drawable.locked_12;
                break;
            case R.drawable.unlocked_13 /*2131165680*/:
                i5 = R.drawable.locked_13;
                break;
            case R.drawable.unlocked_20 /*2131165681*/:
                i5 = R.drawable.locked_20;
                break;
            case R.drawable.unlocked_21 /*2131165682*/:
                i5 = R.drawable.locked_21;
                break;
            case R.drawable.unlocked_22 /*2131165683*/:
                i5 = R.drawable.locked_22;
                break;
            case R.drawable.unlocked_23 /*2131165684*/:
                i5 = R.drawable.locked_23;
                break;
            case R.drawable.unlocked_30 /*2131165685*/:
                i5 = R.drawable.locked_30;
                break;
            case R.drawable.unlocked_31 /*2131165686*/:
                i5 = R.drawable.locked_31;
                break;
            case R.drawable.unlocked_32 /*2131165687*/:
                i5 = R.drawable.locked_32;
                break;
            case R.drawable.unlocked_33 /*2131165688*/:
                i5 = R.drawable.locked_33;
                break;
            case R.drawable.unlocked_40 /*2131165689*/:
                i5 = R.drawable.locked_40;
                break;
            case R.drawable.unlocked_41 /*2131165690*/:
                i5 = R.drawable.locked_41;
                break;
            case R.drawable.unlocked_42 /*2131165691*/:
                i5 = R.drawable.locked_42;
                break;
            case R.drawable.unlocked_43 /*2131165692*/:
                i5 = R.drawable.locked_43;
                break;
            case R.drawable.unlocked_50 /*2131165693*/:
                i5 = R.drawable.locked_50;
                break;
            case R.drawable.unlocked_51 /*2131165694*/:
                i5 = R.drawable.locked_51;
                break;
            case R.drawable.unlocked_52 /*2131165695*/:
                i5 = R.drawable.locked_52;
                break;
            case R.drawable.unlocked_53 /*2131165696*/:
                i5 = R.drawable.locked_53;
                break;
            case R.drawable.unlocked_60 /*2131165697*/:
                i5 = R.drawable.locked_60;
                break;
            case R.drawable.unlocked_61 /*2131165698*/:
                i5 = R.drawable.locked_61;
                break;
            case R.drawable.unlocked_62 /*2131165699*/:
                i5 = R.drawable.locked_62;
                break;
            case R.drawable.unlocked_63 /*2131165700*/:
                i5 = R.drawable.locked_63;
                break;
            case R.drawable.unlocked_70 /*2131165701*/:
                i5 = R.drawable.locked_70;
                break;
            case R.drawable.unlocked_71 /*2131165702*/:
                i5 = R.drawable.locked_71;
                break;
            case R.drawable.unlocked_72 /*2131165703*/:
                i5 = R.drawable.locked_72;
                break;
            case R.drawable.unlocked_73 /*2131165704*/:
                i5 = R.drawable.locked_73;
                break;
            case R.drawable.unlocked_80 /*2131165705*/:
                i5 = R.drawable.locked_80;
                break;
            case R.drawable.unlocked_81 /*2131165706*/:
                i5 = R.drawable.locked_81;
                break;
            case R.drawable.unlocked_82 /*2131165707*/:
                i5 = R.drawable.locked_82;
                break;
            case R.drawable.unlocked_83 /*2131165708*/:
                i5 = R.drawable.locked_83;
                break;
            case R.drawable.unlocked_90 /*2131165709*/:
                i5 = R.drawable.locked_90;
                break;
            case R.drawable.unlocked_91 /*2131165710*/:
                i5 = R.drawable.locked_91;
                break;
            case R.drawable.unlocked_92 /*2131165711*/:
                i5 = R.drawable.locked_92;
                break;
            case R.drawable.unlocked_93 /*2131165712*/:
                i5 = R.drawable.locked_93;
                break;
        }
        this.lockedImg.setImageBitmap(Bitmap.createScaledBitmap(((BitmapDrawable) resources.getDrawable(i5)).getBitmap(), (int) convertDpToPixel((float) this.myApp.get_Service_Lock_Icon_Size()), (int) convertDpToPixel((float) this.myApp.get_Service_Lock_Icon_Size()), true));
        try {
            this.currentBrightness = Settings.System.getInt(getContentResolver(), "screen_brightness");
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        int i6 = this.currentBrightness;
        this.mBrightness = i6;
        this.mInitBrightness = i6;
        this.mView.setOnTouchListener(this.mViewTouchListener);
        int i7 = Build.VERSION.SDK_INT >= 26 ? 2038 : 2002;
        if (this.myApp.get_Service_Mode() == 4) {
            int i8 = this.myApp.get_Display_Size() / 3;
            i2 = 1032;
            int i9 = this.Half_Screen_Lock_Gravity;
            if (i9 == 0) {
                i = i8;
                i3 = 51;
            } else if (i9 != 1 && i9 == 2) {
                i = i8;
                i3 = 83;
            } else {
                i = i8;
                i3 = 19;
            }
        } else {
            i2 = 1024;
            i3 = 51;
            i = -1;
        }
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(-1, i, i7, i2 | 128, -3);
        this.mParams = layoutParams;
        layoutParams.gravity = i3;
        this.mView.setSystemUiVisibility(5894);
        WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        this.mManager = windowManager;
        windowManager.addView(this.mView, this.mParams);
        this.myApp.set_Service_Status(true);
        Disappear_lock();
    }

    public void WrapWindowLock() {
        int i;
        int i2;
        int i3;
        this.mView.setOnTouchListener(this.mViewTouchListener);
        int i4 = Build.VERSION.SDK_INT >= 26 ? 2038 : 2002;
        if (this.myApp.get_Service_Mode() == 4) {
            int i5 = this.myApp.get_Display_Size() / 3;
            i2 = 1032;
            int i6 = this.Half_Screen_Lock_Gravity;
            if (i6 == 0) {
                i = i5;
                i3 = 51;
            } else if (i6 != 1 && i6 == 2) {
                i = i5;
                i3 = 83;
            } else {
                i = i5;
                i3 = 19;
            }
        } else {
            i2 = 1024;
            i3 = 51;
            i = -1;
        }
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(-1, i, i4, i2 | 128, -3);
        this.mParams = layoutParams;
        layoutParams.gravity = i3;
        this.mView.setSystemUiVisibility(5894);
        this.mManager.updateViewLayout(this.mView, this.mParams);
    }

    public float convertDpToPixel(float f) {
        return f * (((float) getResources().getDisplayMetrics().densityDpi) / 160.0f);
    }

    private void buttonNoti() {
        this.mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Intent intent = new Intent("android.intent.action.LockService.switch");
        //Intent intent2 = new Intent("android.intent.action.LockService.youtube");
        Intent intent3 = new Intent("android.intent.action.LockService.finish");
        Intent intent4 = new Intent(getApplicationContext(), MainActivity.class);
        intent4.addFlags(872415232);
        PendingIntent activity = PendingIntent.getActivity(getApplicationContext(), 0, intent4, 0);
        PendingIntent service = PendingIntent.getService(getApplicationContext(), 0, intent, 0);
        //PendingIntent service2 = PendingIntent.getService(getApplicationContext(), 0, intent2, 0);
        PendingIntent service3 = PendingIntent.getService(getApplicationContext(), 0, intent3, 0);
        if (Build.VERSION.SDK_INT >= 26) {
            BitmapFactory.decodeResource(getResources(), R.mipmap.app_icon);
            String string = getString(R.string.NotiTitle);
            String string2 = getString(R.string.subNotiTitle);
            String string3 = getString(R.string.NotiunLock);
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
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.notifiview_locked);
        remoteViews.setOnClickPendingIntent(R.id.btn_servicesSwitch, service);
       // remoteViews.setOnClickPendingIntent(R.id.btn_youtube, service2);
        remoteViews.setOnClickPendingIntent(R.id.btn_servicesFinish, service3);
        Notification notification = new Notification(R.drawable.locked, "TOUCH Service Start!!", System.currentTimeMillis());
        this.mNoti = notification;
        notification.flags |= 16;
        this.mNoti.flags |= 2;
        this.mNoti.contentView = remoteViews;
        this.mNoti.contentIntent = activity;
    }

    public void onDestroy() {
        super.onDestroy();
        this.mHandler.removeCallbacks(this.mLongPressed);
        View view = this.mView;
        if (view != null) {
            this.mManager.removeView(view);
            this.mView = null;
        }
        this.myApp.set_Service_Status(false);
        this.mBrightness = this.mInitBrightness;
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        super.onStartCommand(intent, i, i2);
        buttonNoti();
        if (Build.VERSION.SDK_INT >= 26) {
            this.mNM.notify(7777, this.builder.build());
            Log.d("Check-2", "notify call!!");
        } else {
            this.mNM.notify(7777, this.mNoti);
        }
        String str = null;
        if (intent != null) {
            str = intent.getAction();
        }
        if ("android.intent.action.LockService.switch".equals(str)) {
            Log.d("check-3", "start unlockService");
            startService(new Intent(this, UnLockService.class));
            stopSelf();
            this.myApp.set_Service_Status(true);
        }
        if ("android.intent.action.LockService.finish".equals(str)) {
            this.myApp.set_Service_Status(false);
            stopSelf();
            if (Build.VERSION.SDK_INT >= 26) {
                NotificationManagerCompat.from(this).cancel(7777);
            } else {
                this.mNM.cancelAll();
            }
        }
        /*if ("android.intent.action.LockService.youtube".equals(str)) {
            startActivity(getPackageManager().getLaunchIntentForPackage("com.google.android.youtube"));
        }*/
        mOnlyLesteningMode();
        return START_STICKY;
    }
}
