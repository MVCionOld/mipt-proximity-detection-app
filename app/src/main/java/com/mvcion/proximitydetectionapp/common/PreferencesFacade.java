package com.mvcion.proximitydetectionapp.common;

import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesFacade {

    private static final String advertiserPreferences = "ble_advertiser_preferences";
    private static final String ADVERTISER_MODE_KEY = "advertiser_mode";
    public static final int DEFAULT_ADVERTISER_MODE_VALUE = AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY;
    private static final String ADVERTISER_TX_POWER_KEY = "advertiser_tx_power";
    public static final int DEFAULT_ADVERTISER_TX_POWER_VALUE = AdvertiseSettings.ADVERTISE_TX_POWER_HIGH;
    private static final String ADVERTISER_IS_CONNECTABLE_KEY = "advertiser_is_connectable";
    public static final boolean DEFAULT_ADVERTISER_IS_CONNECTABLE_VALUE = true;

    private static final String scannerPreferences = "ble_scanner_preferences";
    private static final String SCANNER_PROCESSING_WINDOW_NANOS_KEY = "scanner_processing_window_nanos";
    public static final long DEFAULT_SCANNER_PROCESSING_WINDOW_NANOS_VALUE = 5_000_000_000L;
    private static final String SCANNER_REPORT_DELAY_MILLIS_KEY = "scanner_report_delay_millis";
    public static final long DEFAULT_SCANNER_REPORT_DELAY_MILLIS = 0L;
    private static final String SCANNER_MODE_KEY = "scanner_mode";
    public static final int DEFAULT_SCANNER_MODE_VALUE = ScanSettings.SCAN_MODE_LOW_LATENCY;
    private static final String CALLBACK_TYPE_KEY = "scanner_callback_type";
    public static final int DEFAULT_CALLBACK_TYPE_VALUE = ScanSettings.CALLBACK_TYPE_ALL_MATCHES;
    private static final String MATCH_MODE_KEY = "scanner_match_mode";
    public static final int DEFAULT_MATCH_MODE_VALUE = ScanSettings.MATCH_MODE_AGGRESSIVE;
    private static final String NUM_OF_MATCHES_KEY = "scanner_num_of_matches";
    public static final int DEFAULT_NUM_OF_MATCHES_VALUE = ScanSettings.MATCH_NUM_ONE_ADVERTISEMENT;

    private static SharedPreferences getAdvertiserSharedPrefs(Context context) {
        return context.getSharedPreferences(advertiserPreferences, Context.MODE_PRIVATE);
    }

    public static int getAdvertiserMode(Context context) {
        return getAdvertiserSharedPrefs(context)
                .getInt(ADVERTISER_MODE_KEY, DEFAULT_ADVERTISER_MODE_VALUE);
    }

    public static void setAdvertiserMode(Context context, int advertiserMode) {
        getAdvertiserSharedPrefs(context)
                .edit()
                .putInt(ADVERTISER_MODE_KEY, advertiserMode)
                .apply();
    }

    public static int getAdvertiserTxPower(Context context) {
        return getAdvertiserSharedPrefs(context)
                .getInt(ADVERTISER_TX_POWER_KEY, DEFAULT_ADVERTISER_TX_POWER_VALUE);
    }

    public static void setAdvertiserTxPower(Context context, int txPower) {
        getAdvertiserSharedPrefs(context)
                .edit()
                .putInt(ADVERTISER_TX_POWER_KEY, txPower)
                .apply();
    }

    public static boolean getAdvertiserIsConnectable(Context context) {
        return getAdvertiserSharedPrefs(context)
                .getBoolean(ADVERTISER_IS_CONNECTABLE_KEY, DEFAULT_ADVERTISER_IS_CONNECTABLE_VALUE);
    }

    public static void setAdvertiserIsConnectable(Context context, boolean isConnectable) {
        getAdvertiserSharedPrefs(context)
                .edit()
                .putBoolean(ADVERTISER_IS_CONNECTABLE_KEY, isConnectable)
                .apply();
    }

    private static SharedPreferences getScannerSharedPrefs(Context context) {
        return context.getSharedPreferences(scannerPreferences, Context.MODE_PRIVATE);
    }

    public static long getProcessingWindowNanos(Context context) {
        return getScannerSharedPrefs(context)
                .getLong(SCANNER_PROCESSING_WINDOW_NANOS_KEY, DEFAULT_SCANNER_PROCESSING_WINDOW_NANOS_VALUE);
    }

    public static void setProcessingWindowNanos(Context context, long processingWindowNanos) {
        getScannerSharedPrefs(context)
                .edit()
                .putLong(SCANNER_PROCESSING_WINDOW_NANOS_KEY, processingWindowNanos)
                .apply();
    }

    public static long getReportDelayMillis(Context context) {
        return getScannerSharedPrefs(context)
                .getLong(SCANNER_REPORT_DELAY_MILLIS_KEY, DEFAULT_SCANNER_REPORT_DELAY_MILLIS);
    }

    public static void setReportDelayMillis(Context context, long reportDelayMillis) {
        getScannerSharedPrefs(context)
                .edit()
                .putLong(SCANNER_REPORT_DELAY_MILLIS_KEY, reportDelayMillis)
                .apply();
    }

    public static int getScannerMode(Context context) {
        return getScannerSharedPrefs(context)
                .getInt(SCANNER_MODE_KEY, DEFAULT_SCANNER_MODE_VALUE);
    }

    public static void setScannerMode(Context context, int scannerMode) {
        getScannerSharedPrefs(context)
                .edit()
                .putInt(SCANNER_MODE_KEY, scannerMode)
                .apply();
    }

    public static int getCallbackType(Context context) {
        return getScannerSharedPrefs(context)
                .getInt(CALLBACK_TYPE_KEY, DEFAULT_CALLBACK_TYPE_VALUE);
    }

    public static void setCallbackType(Context context, int callbackType) {
        getScannerSharedPrefs(context)
                .edit()
                .putInt(CALLBACK_TYPE_KEY, callbackType)
                .apply();
    }

    public static int getMatchMode(Context context) {
        return getScannerSharedPrefs(context)
                .getInt(MATCH_MODE_KEY, DEFAULT_MATCH_MODE_VALUE);
    }

    public static void setMatchMode(Context context, int matchMode) {
        getScannerSharedPrefs(context)
                .edit()
                .putInt(MATCH_MODE_KEY, matchMode)
                .apply();
    }

    public static int getNumOfMatches(Context context) {
        return getScannerSharedPrefs(context)
                .getInt(NUM_OF_MATCHES_KEY, DEFAULT_NUM_OF_MATCHES_VALUE);
    }

    public static void setNumOfMatches(Context context, int numOfMatches) {
        getScannerSharedPrefs(context)
                .edit()
                .putInt(NUM_OF_MATCHES_KEY, numOfMatches)
                .apply();
    }
}