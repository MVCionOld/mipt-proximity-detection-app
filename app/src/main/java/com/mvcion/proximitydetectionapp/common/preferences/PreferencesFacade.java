package com.mvcion.proximitydetectionapp.common.preferences;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesFacade extends PreferencesOptions {

    private static SharedPreferences getAdvertiseSharedPrefs(Context context) {
        return context.getSharedPreferences(advertiserPreferences, Context.MODE_PRIVATE);
    }

    public static int getAdvertiseMode(Context context) {
        PreferenceTemplateGetter<Integer> ptg = new PreferenceTemplateGetter<>();
        ptg.setMap(ADVERTISE_MODE_MAP);
        ptg.setSharedPreferences(getAdvertiseSharedPrefs(context));
        ptg.setKey(ADVERTISE_MODE_KEY);
        ptg.setDefaultKey(DefaultPreferences.getAdvertiseModeKey());
        ptg.setDefaultValue(DefaultPreferences.getAdvertiseModeValue());
        return ptg.get();
    }

    public static int getAdvertiseTxPower(Context context) {
        PreferenceTemplateGetter<Integer> ptg = new PreferenceTemplateGetter<>();
        ptg.setMap(ADVERTISE_TX_POWER_MAP);
        ptg.setSharedPreferences(getAdvertiseSharedPrefs(context));
        ptg.setKey(ADVERTISE_TX_POWER_KEY);
        ptg.setDefaultKey(DefaultPreferences.getAdvertiseTxPowerKey());
        ptg.setDefaultValue(DefaultPreferences.getAdvertiseTxPowerValue());
        return ptg.get();

    }

    public static boolean getAdvertiseIsConnectable(Context context) {
        PreferenceTemplateGetter<Boolean> ptg = new PreferenceTemplateGetter<>();
        ptg.setMap(ADVERTISE_IS_CONNECTABLE_MAP);
        ptg.setSharedPreferences(getAdvertiseSharedPrefs(context));
        ptg.setKey(ADVERTISE_IS_CONNECTABLE_KEY);
        ptg.setDefaultKey(DefaultPreferences.getAdvertiseIsConnectableKey());
        ptg.setDefaultValue(DefaultPreferences.isAdvertiseIsConnectableValue());
        return ptg.get();
    }

    private static SharedPreferences getScanSharedPrefs(Context context) {
        return context.getSharedPreferences(scannerPreferences, Context.MODE_PRIVATE);
    }

    public static long getScanProcessingWindowNanos(Context context) {
        PreferenceTemplateGetter<Long> ptg = new PreferenceTemplateGetter<>();
        ptg.setMap(SCAN_PROCESSING_WINDOW_NANOS_MAP);
        ptg.setSharedPreferences(getScanSharedPrefs(context));
        ptg.setKey(SCAN_PROCESSING_WINDOW_NANOS_KEY);
        ptg.setDefaultKey(DefaultPreferences.getScanProcessingWindowNanosKey());
        ptg.setDefaultValue(DefaultPreferences.getScanProcessingWindowNanosValue());
        return ptg.get();
    }

    public static long getScanReportDelayMillis(Context context) {
        PreferenceTemplateGetter<Long> ptg = new PreferenceTemplateGetter<>();
        ptg.setMap(SCAN_REPORT_DELAY_MILLIS_MAP);
        ptg.setSharedPreferences(getScanSharedPrefs(context));
        ptg.setKey(SCAN_REPORT_DELAY_MILLIS_KEY);
        ptg.setDefaultKey(DefaultPreferences.getScanReportDelayMillisKey());
        ptg.setDefaultValue(DefaultPreferences.getScanReportDelayMillisValue());
        return ptg.get();
    }

    public static int getScanMode(Context context) {
        PreferenceTemplateGetter<Integer> ptg = new PreferenceTemplateGetter<>();
        ptg.setMap(SCAN_MODE_MAP);
        ptg.setSharedPreferences(getScanSharedPrefs(context));
        ptg.setKey(SCAN_MODE_KEY);
        ptg.setDefaultKey(DefaultPreferences.getScanModeKey());
        ptg.setDefaultValue(DefaultPreferences.getScanModeValue());
        return ptg.get();
    }

    public static int getScanCallbackType(Context context) {
        PreferenceTemplateGetter<Integer> ptg = new PreferenceTemplateGetter<>();
        ptg.setMap(SCAN_CALLBACK_TYPE_MAP);
        ptg.setSharedPreferences(getScanSharedPrefs(context));
        ptg.setKey(SCAN_CALLBACK_TYPE_KEY);
        ptg.setDefaultKey(DefaultPreferences.getScanCallbackTypeKey());
        ptg.setDefaultValue(DefaultPreferences.getScanCallbackTypeValue());
        return ptg.get();
    }

    public static int getScanMatchMode(Context context) {
        PreferenceTemplateGetter<Integer> ptg = new PreferenceTemplateGetter<>();
        ptg.setMap(SCAN_MATCH_MODE_MAP);
        ptg.setSharedPreferences(getScanSharedPrefs(context));
        ptg.setKey(SCAN_MATCH_MODE_KEY);
        ptg.setDefaultKey(DefaultPreferences.getScanMatchModeKey());
        ptg.setDefaultValue(DefaultPreferences.getScanMatchModeValue());
        return ptg.get();
    }

    public static int getScanNumOfMatches(Context context) {
        PreferenceTemplateGetter<Integer> ptg = new PreferenceTemplateGetter<>();
        ptg.setMap(SCAN_NUM_OF_MATCHES_MAP);
        ptg.setSharedPreferences(getScanSharedPrefs(context));
        ptg.setKey(SCAN_NUM_OF_MATCHES_KEY);
        ptg.setDefaultKey(DefaultPreferences.getScanNumOfMatchesKey());
        ptg.setDefaultValue(DefaultPreferences.getScanNumOfMatchesValue());
        return ptg.get();
    }
}