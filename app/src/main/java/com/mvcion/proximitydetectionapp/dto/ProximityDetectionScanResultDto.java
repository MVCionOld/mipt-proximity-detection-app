package com.mvcion.proximitydetectionapp.dto;

import android.bluetooth.le.ScanResult;
import android.os.Build;

import org.jetbrains.annotations.NotNull;

import java.text.MessageFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.TimeZone;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProximityDetectionScanResultDto {

    private String mac;
    private String name;
    private String processedDttm;
    private int minRssi;
    private int maxRssi;
    private double avgRssi;
    private int minTxPower;
    private int maxTxPower;
    private double avgTxPower;
    private int counter;

    public ProximityDetectionScanResultDto() {
    }

    public ProximityDetectionScanResultDto(String mac, LinkedList<ScanResult> scanResults) {
        this.mac = mac;
        name = scanResults.get(0).getDevice().getName();
        processedDttm = String.format(
                "%tFT%<tRZ",
                Calendar.getInstance(TimeZone.getTimeZone("GMT+3"))
        );
        minRssi = Integer.MAX_VALUE;
        maxRssi = Integer.MIN_VALUE;
        avgRssi = 0;
        minTxPower = Integer.MAX_VALUE;
        maxTxPower = Integer.MIN_VALUE;
        avgTxPower = 0;
        counter = scanResults.size();
        for (ScanResult scanResult: scanResults) {
            minRssi = Math.min(minRssi, scanResult.getRssi());
            maxRssi = Math.max(maxRssi, scanResult.getRssi());
            avgRssi += scanResult.getRssi();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
                    && scanResult.getTxPower() != ScanResult.TX_POWER_NOT_PRESENT) {
                minTxPower = Math.min(minTxPower, scanResult.getTxPower());
                maxTxPower = Math.max(maxTxPower, scanResult.getTxPower());
                avgTxPower += scanResult.getTxPower();
            }
        }
        avgRssi /= counter;
        avgTxPower /= counter;
    }

    @NotNull
    @Override
    public String toString() {
        if (minTxPower != Integer.MAX_VALUE) {
            return MessageFormat.format(
                    "Device: {0}\nname: {9}\nprocessed datetime: {8}\nRSSI: {3} in [{1}; {2}]"
                            + "TxPower: {6} in [{4}; {5}]\ncounter: {7}",
                    mac, minRssi, maxRssi, avgRssi,
                    minTxPower, maxTxPower, avgTxPower,
                    counter, processedDttm, name
            );
        } else {
            return MessageFormat.format(
                    "Device: {0}\nname: {6}\nprocessed datetime: {5}\nRSSI: {3} in [{1}; {2}]"
                            + "\ncounter: {4}",
                    mac, minRssi, maxRssi, avgRssi,
                    counter, processedDttm, name
            );
        }
    }
}