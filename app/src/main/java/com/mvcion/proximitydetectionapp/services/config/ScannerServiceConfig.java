package com.mvcion.proximitydetectionapp.services.config;

import android.bluetooth.le.ScanSettings;

import com.mvcion.proximitydetectionapp.common.preferences.DefaultPreferences;

import org.jetbrains.annotations.NotNull;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;

@Getter
@NoArgsConstructor
public class ScannerServiceConfig {
    @Setter private int serviceId;
    @Setter private AtomicInteger scannerMode = new AtomicInteger(DefaultPreferences.getScanModeValue());
    @Setter private AtomicInteger matchMode = new AtomicInteger(DefaultPreferences.getScanMatchModeValue());
    @Setter private AtomicInteger numOfMatches = new AtomicInteger(DefaultPreferences.getScanNumOfMatchesValue());
    @Setter private AtomicLong processingWindowNanos = new AtomicLong(DefaultPreferences.getScanProcessingWindowNanosValue());
    private final int scanCallbackType = DefaultPreferences.getScanCallbackTypeValue();
    private final long scanReportDelayMillis = DefaultPreferences.getScanReportDelayMillisValue();

    @SneakyThrows
    @NotNull
    @Override
    public String toString() {
        final HashMap<Long, String> INVERSE_SCAN_PROCESSING_WINDOW_NANOS_MAP = new HashMap<Long, String>(){{
            put(1_000_000_000L, "1");
            put(2_000_000_000L, "2");
            put(5_000_000_000L, "5");
            put(10_000_000_000L, "10");
        }};
        final HashMap<Integer, String> INVERSE_SCAN_MODE_MAP = new HashMap<Integer, String>(){{
            put(ScanSettings.SCAN_MODE_LOW_POWER, "LOW POWER");
            put(ScanSettings.SCAN_MODE_BALANCED, "BALANCED");
            put(ScanSettings.SCAN_MODE_LOW_LATENCY, "LOW LATENCY");
        }};
        final HashMap<Integer, String> INVERSE_SCAN_MATCH_MODE_MAP = new HashMap<Integer, String>(){{
            put(ScanSettings.MATCH_MODE_AGGRESSIVE, "AGGRESSIVE");
            put(ScanSettings.MATCH_MODE_STICKY,     "STICKY");
        }};
        final HashMap<Integer, String> INVERSE_SCAN_NUM_OF_MATCHES_MAP = new HashMap<Integer, String>(){{
            put(ScanSettings.MATCH_NUM_ONE_ADVERTISEMENT, "ONE");
            put(ScanSettings.MATCH_NUM_FEW_ADVERTISEMENT, "FEW");
            put(ScanSettings.MATCH_NUM_MAX_ADVERTISEMENT, "MAX");
        }};
        return MessageFormat.format(
                "Update frequency: {0} seconds\n"
                + "Mode: {1}\nMatch mode: {2}\nNum of matches: {3}",
                INVERSE_SCAN_PROCESSING_WINDOW_NANOS_MAP.get(processingWindowNanos.get()),
                INVERSE_SCAN_MODE_MAP.get(scannerMode.get()),
                INVERSE_SCAN_MATCH_MODE_MAP.get(matchMode.get()),
                INVERSE_SCAN_NUM_OF_MATCHES_MAP.get(numOfMatches.get())
        );
    }

    @SneakyThrows
    @NotNull
    @Override
    public ScannerServiceConfig clone() {
        ScannerServiceConfig clone = (ScannerServiceConfig) super.clone();
        clone.getScannerMode().set(this.scannerMode.get());
        clone.getMatchMode().set(this.matchMode.get());
        clone.getNumOfMatches().set(this.numOfMatches.get());
        clone.getProcessingWindowNanos().set(this.processingWindowNanos.get());
        return clone;
    }
}
