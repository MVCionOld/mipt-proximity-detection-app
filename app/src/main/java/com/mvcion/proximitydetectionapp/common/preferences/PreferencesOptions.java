package com.mvcion.proximitydetectionapp.common.preferences;

import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.ScanSettings;

import java.util.HashMap;

public class PreferencesOptions {
    /**
     * BLE Advertiser Preferences
     */
    // Advertiser Mode
    static final String ADVERTISE_MODE_KEY = "advertiser_mode";
    static final HashMap<String, Integer> ADVERTISE_MODE_MAP = new HashMap<String, Integer>(){{
        put("ADVERTISE_MODE_LOW_POWER",   AdvertiseSettings.ADVERTISE_MODE_LOW_POWER);
        put("ADVERTISE_MODE_BALANCED",    AdvertiseSettings.ADVERTISE_MODE_BALANCED);
        put("ADVERTISE_MODE_LOW_LATENCY", AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY);
    }};
    // Advertiser Tx Power
    static final String ADVERTISE_TX_POWER_KEY = "advertiser_tx_power";
    static final HashMap<String, Integer> ADVERTISE_TX_POWER_MAP = new HashMap<String, Integer>(){{
        put("ADVERTISE_TX_POWER_ULTRA_LOW", AdvertiseSettings.ADVERTISE_TX_POWER_ULTRA_LOW);
        put("ADVERTISE_TX_POWER_LOW",       AdvertiseSettings.ADVERTISE_TX_POWER_LOW);
        put("ADVERTISE_TX_POWER_MEDIUM",    AdvertiseSettings.ADVERTISE_TX_POWER_MEDIUM);
        put("ADVERTISE_TX_POWER_HIGH",      AdvertiseSettings.ADVERTISE_TX_POWER_HIGH);
    }};

    /**
     * BLE Scanner Preferences
     */
    // Scanner processing window
    static final String SCAN_PROCESSING_WINDOW_NANOS_KEY = "scanner_processing_window_nanos";
    static final HashMap<String, Long> SCAN_PROCESSING_WINDOW_NANOS_MAP = new HashMap<String, Long>(){{
        put("1",  1_000_000_000L);
        put("2",  2_000_000_000L);
        put("5",  5_000_000_000L);
        put("10", 10_000_000_000L);
    }};
    // Scanner mode
    static final String SCAN_MODE_KEY = "scanner_mode";
    static final HashMap<String, Integer> SCAN_MODE_MAP = new HashMap<String, Integer>(){{
        put("SCAN_MODE_LOW_POWER",   ScanSettings.SCAN_MODE_LOW_POWER);
        put("SCAN_MODE_BALANCED",    ScanSettings.SCAN_MODE_BALANCED);
        put("SCAN_MODE_LOW_LATENCY", ScanSettings.SCAN_MODE_LOW_LATENCY);
    }};
    // Scanner match mode
    static final String SCAN_MATCH_MODE_KEY = "scanner_match_mode";
    static final HashMap<String, Integer> SCAN_MATCH_MODE_MAP = new HashMap<String, Integer>(){{
        put("MATCH_MODE_AGGRESSIVE", ScanSettings.MATCH_MODE_AGGRESSIVE);
        put("MATCH_MODE_STICKY",     ScanSettings.MATCH_MODE_STICKY);
    }};
    // Scanner num of matches
    static final String SCAN_NUM_OF_MATCHES_KEY = "scanner_num_of_matches";
    static final HashMap<String, Integer> SCAN_NUM_OF_MATCHES_MAP = new HashMap<String, Integer>(){{
        put("MATCH_NUM_ONE_ADVERTISEMENT", ScanSettings.MATCH_NUM_ONE_ADVERTISEMENT);
        put("MATCH_NUM_FEW_ADVERTISEMENT", ScanSettings.MATCH_NUM_FEW_ADVERTISEMENT);
        put("MATCH_NUM_MAX_ADVERTISEMENT", ScanSettings.MATCH_NUM_MAX_ADVERTISEMENT);
    }};
}
