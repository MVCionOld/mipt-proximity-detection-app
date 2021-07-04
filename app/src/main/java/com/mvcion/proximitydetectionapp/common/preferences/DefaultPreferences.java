package com.mvcion.proximitydetectionapp.common.preferences;

import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.ScanSettings;

import lombok.Getter;

public final class DefaultPreferences {
    @Getter private static final String advertiseModeKey = "ADVERTISE_MODE_LOW_LATENCY";
    @Getter private static final int advertiseModeValue = AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY;

    @Getter private static final String advertiseTxPowerKey = "ADVERTISE_TX_POWER_HIGH";
    @Getter private static final int advertiseTxPowerValue = AdvertiseSettings.ADVERTISE_TX_POWER_HIGH;

    @Getter private static final String advertiseIsConnectableKey = "ADVERTISER_IS_CONNECTABLE";
    @Getter private static final boolean advertiseIsConnectableValue = false;

    @Getter private static final String scanProcessingWindowNanosKey = "5";
    @Getter private static final long scanProcessingWindowNanosValue = 5_000_000_000L;

    @Getter private static final String scanReportDelayMillisKey = "0";
    @Getter private static final long scanReportDelayMillisValue = 0L;

    @Getter private static final String scanModeKey = "SCAN_MODE_LOW_LATENCY";
    @Getter private static final int scanModeValue = ScanSettings.SCAN_MODE_LOW_LATENCY;

    @Getter private static final String scanCallbackTypeKey = "CALLBACK_TYPE_ALL_MATCHES";
    @Getter private static final int scanCallbackTypeValue = ScanSettings.CALLBACK_TYPE_ALL_MATCHES;

    @Getter private static final String scanMatchModeKey = "MATCH_MODE_AGGRESSIVE";
    @Getter private static final int scanMatchModeValue = ScanSettings.MATCH_MODE_AGGRESSIVE;

    @Getter private static final String scanNumOfMatchesKey = "MATCH_NUM_ONE_ADVERTISEMENT";
    @Getter private static final int scanNumOfMatchesValue = ScanSettings.MATCH_NUM_ONE_ADVERTISEMENT;
}
