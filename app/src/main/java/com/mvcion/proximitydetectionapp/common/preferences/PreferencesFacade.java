package com.mvcion.proximitydetectionapp.common.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import java.util.HashMap;

public class PreferencesFacade extends PreferencesOptions {

    public static int getAdvertiseMode(Context context) {
        return new PreferenceTemplateGetterArgs<>(
                context,
                ADVERTISE_MODE_MAP,
                ADVERTISE_MODE_KEY,
                DefaultPreferences.getAdvertiseModeKey(),
                DefaultPreferences.getAdvertiseModeValue()
        ).buildGetter().get();
    }

    public static int getAdvertiseTxPower(Context context) {
        return new PreferenceTemplateGetterArgs<>(
                context,
                ADVERTISE_TX_POWER_MAP,
                ADVERTISE_TX_POWER_KEY,
                DefaultPreferences.getAdvertiseTxPowerKey(),
                DefaultPreferences.getAdvertiseTxPowerValue()
        ).buildGetter().get();
    }

    public static long getScanProcessingWindowNanos(Context context) {
        return new PreferenceTemplateGetterArgs<>(
                context,
                SCAN_PROCESSING_WINDOW_NANOS_MAP,
                SCAN_PROCESSING_WINDOW_NANOS_KEY,
                DefaultPreferences.getScanProcessingWindowNanosKey(),
                DefaultPreferences.getScanProcessingWindowNanosValue()
        ).buildGetter().get();
    }

    public static int getScanMode(Context context) {
        return new PreferenceTemplateGetterArgs<>(
                context,
                SCAN_MODE_MAP,
                SCAN_MODE_KEY,
                DefaultPreferences.getScanModeKey(),
                DefaultPreferences.getScanModeValue()
        ).buildGetter().get();
    }

    public static int getScanMatchMode(Context context) {
        return new PreferenceTemplateGetterArgs<>(
                context,
                SCAN_MATCH_MODE_MAP,
                SCAN_MATCH_MODE_KEY,
                DefaultPreferences.getScanMatchModeKey(),
                DefaultPreferences.getScanMatchModeValue()
        ).buildGetter().get();
    }

    public static int getScanNumOfMatches(Context context) {
        return new PreferenceTemplateGetterArgs<>(
                context,
                SCAN_NUM_OF_MATCHES_MAP,
                SCAN_NUM_OF_MATCHES_KEY,
                DefaultPreferences.getScanNumOfMatchesKey(),
                DefaultPreferences.getScanNumOfMatchesValue()
        ).buildGetter().get();
    }
}