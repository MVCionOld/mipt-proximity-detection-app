package com.mvcion.proximitydetectionapp.common.preferences;

import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.ScanSettings;

import lombok.Getter;

public final class DefaultPreferences {
    @Getter public static final String advertiseModeKey = "ADVERTISE_MODE_LOW_LATENCY";
    @Getter public static final int advertiseModeValue = AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY;

    @Getter public static final String advertiseTxPowerKey = "ADVERTISE_TX_POWER_HIGH";
    @Getter public static final int advertiseTxPowerValue = AdvertiseSettings.ADVERTISE_TX_POWER_HIGH;

    @Getter public static final String advertiseIsConnectableKey = "ADVERTISER_IS_CONNECTABLE";
    @Getter public static final boolean advertiseIsConnectableValue = true;

    @Getter public static final String scanProcessingWindowNanosKey = "5";
    @Getter public static final long scanProcessingWindowNanosValue = 5_000_000_000L;

    @Getter public static final String scanReportDelayMillisKey = "0";
    @Getter public static final long scanReportDelayMillisValue = 0L;

    @Getter public static final String scanModeKey = "SCAN_MODE_LOW_LATENCY";
    @Getter public static final int scanModeValue = ScanSettings.SCAN_MODE_LOW_LATENCY;

    @Getter public static final String scanCallbackTypeKey = "CALLBACK_TYPE_ALL_MATCHES";
    @Getter public static final int scanCallbackTypeValue = ScanSettings.CALLBACK_TYPE_ALL_MATCHES;

    @Getter public static final String scanMatchModeKey = "MATCH_MODE_AGGRESSIVE";
    @Getter public static final int scanMatchModeValue = ScanSettings.MATCH_MODE_AGGRESSIVE;

    @Getter public static final String scanNumOfMatchesKey = "MATCH_NUM_ONE_ADVERTISEMENT";
    @Getter public static final int scanNumOfMatchesValue = ScanSettings.MATCH_NUM_ONE_ADVERTISEMENT;
}
