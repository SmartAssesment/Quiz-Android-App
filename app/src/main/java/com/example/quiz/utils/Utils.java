package com.example.quiz.utils;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;

import androidx.core.content.ContextCompat;

import com.example.quiz.R;

public class Utils {

    // The public static function which can be called from other classes
    public static void darkenStatusBar(Activity activity, int color) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {


            Window window = activity.getWindow();

            // clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            // finally change the color
            window.setStatusBarColor(activity.getResources().getColor(color));

        }


    }
}