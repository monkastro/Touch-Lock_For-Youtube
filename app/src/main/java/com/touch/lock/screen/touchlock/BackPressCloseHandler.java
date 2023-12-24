package com.touch.lock.screen.touchlock;

import android.app.Activity;
import android.widget.Toast;

public class BackPressCloseHandler {
    private Activity activity;
    private long backKeyPressedTime = 0;
    private Toast toast;

    public BackPressCloseHandler(Activity activity2) {
        this.activity = activity2;
    }

    public void onBackPressed() {
        if (System.currentTimeMillis() > this.backKeyPressedTime + 2000) {
            this.backKeyPressedTime = System.currentTimeMillis();
            showGuide();
        } else if (System.currentTimeMillis() <= this.backKeyPressedTime + 2000) {
            this.activity.finish();
            this.toast.cancel();
        }
    }

    public void showGuide() {
        Toast makeText = Toast.makeText(this.activity, "Do you want to shut down the service without running it?\n" +
                "\n" +
                "Click the'Back' button again to finish.", Toast.LENGTH_SHORT);
        this.toast = makeText;
        makeText.show();
    }
}
