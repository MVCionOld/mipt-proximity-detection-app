package com.mvcion.proximitydetectionapp.services.config;

import com.mvcion.proximitydetectionapp.common.preferences.DefaultPreferences;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class ScannerServiceConfig {
    @Setter private int serviceId;
    @Setter private int scannerMode;
    @Setter private int matchMode;
    @Setter private int numOfMatches;
    @Setter private long processingWindowNanos;
    private final int scanCallbackType = DefaultPreferences.getScanCallbackTypeValue();
    private final long scanReportDelayMillis = DefaultPreferences.getScanReportDelayMillisValue();
}
