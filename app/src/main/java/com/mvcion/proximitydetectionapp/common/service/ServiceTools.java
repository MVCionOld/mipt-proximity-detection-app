package com.mvcion.proximitydetectionapp.common.service;

import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

import java.util.List;

public class ServiceTools {
    private static final String TAG = ServiceTools.class.getName();

    public static boolean isServiceRunning(Context context, Class<?> serviceClass) {
        final ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        final List<ActivityManager.RunningServiceInfo> services = activityManager.getRunningServices(Integer.MAX_VALUE);

        for (ActivityManager.RunningServiceInfo runningServiceInfo : services) {
            Log.d(TAG, String.format("Service: %s", runningServiceInfo.service.getClassName()));
            if (runningServiceInfo.service.getClassName().equals(serviceClass.getName())) {
                return true;
            }
        }
        return false;
    }
}
