package com.yottamobile.doraemon.utils;

import android.app.Activity;
import android.content.Intent;


public class ActivityUtil {

    public static boolean isFinishing(Activity activity) {
        return (activity == null || activity.isFinishing());
    }

    public static void startActivity(Activity activity, Class targetClass) {
        Intent intent = new Intent(activity, targetClass);
        activity.startActivity(intent);
    }
}
