package com.mvcion.proximitydetectionapp;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import com.mvcion.proximitydetectionapp.common.ServiceUuis;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ScannerService extends Service {
    private static final String TAG = "ScannerService";

    private long processingWindowNanos;
    private long reportDelayMillis;
    private int scannerMode;
    private int callbackType;
    private int matchMode;
    private int numOfMatches;

    private Queue<ScanResult> leDevicesStream = new ConcurrentLinkedQueue<>();

    private BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private BluetoothLeScanner bluetoothLeScanner;

    private final ScanCallback leScanCallback = new ScanCallback() {

        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            if (result != null) {
                Log.d(TAG, result.toString());
                leDevicesStream.add(result);
            } else {
                Log.w(TAG, "Nullable ScanResult.");
            }
        }
    };

    private Thread scanResultsProducer = new Thread(() -> {
        List<ScanFilter> scanFilters = new ArrayList<ScanFilter>(){{
            add(new ScanFilter
                    .Builder()
                    .setServiceUuid(ServiceUuis.getServiceUuid())
                    .build());
        }};
        ScanSettings scanSettings = new ScanSettings
                .Builder()
                .setScanMode(scannerMode)
                .setCallbackType(callbackType)
                .setMatchMode(matchMode)
                .setNumOfMatches(numOfMatches)
                .setReportDelay(reportDelayMillis)
                .build();
        bluetoothLeScanner.startScan(scanFilters, scanSettings, leScanCallback);
    });

    private Thread scanResultsConsumer = new Thread(new Runnable() {

        private int scannerIteration = 0;
        private Set<String> uniqueLeDevices = new HashSet<>();
        private volatile Map<String, LinkedList<ScanResult>> nearbyDevices = new ConcurrentHashMap<>();

        private String[] getProximityReport() {
            String[] reports = new String[nearbyDevices.size()];
            int i = 0;
            for (Map.Entry<String, LinkedList<ScanResult>> entry: nearbyDevices.entrySet()) {
                entry.getValue().get(0).describeContents();
                int minRssi = Integer.MAX_VALUE;
                int maxRssi = Integer.MIN_VALUE;
                double avgRssi = 0;
                int minTxPower = Integer.MAX_VALUE;
                int maxTxPower = Integer.MIN_VALUE;
                double avgTxPower = 0;
                int counter = entry.getValue().size();
                for (ScanResult scanResult: entry.getValue()) {
                    minRssi = Math.min(minRssi, scanResult.getRssi());
                    maxRssi = Math.max(maxRssi, scanResult.getRssi());
                    avgRssi += scanResult.getRssi();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && scanResult.getTxPower() != ScanResult.TX_POWER_NOT_PRESENT) {
                        minTxPower = Math.min(minTxPower, scanResult.getTxPower());
                        maxTxPower = Math.max(maxTxPower, scanResult.getTxPower());
                        avgTxPower += scanResult.getTxPower();
                    }
                }
                avgRssi /= counter;
                avgTxPower /= counter;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    reports[i++] = MessageFormat.format(
                            "{0}\nmin RSSI: {1}\nmax RSSI: {2}\navg RSSI: {3}"
                                    + "\nmin Tx power: {4}\nmax Tx power: {5}\navg Tx power: {6}"
                                    + "\ncounter: {7}",
                            entry.getKey(), minRssi, maxRssi, avgRssi,
                            minTxPower, maxTxPower, avgTxPower,
                            counter);
                } else {
                    reports[i++] = MessageFormat.format(
                            "{0}\nmin RSSI: {1}\nmax RSSI: {2}\navgRssi: {3}\ncounter: {4}",
                            entry.getKey(), minRssi, maxRssi, avgRssi, counter);
                }
            }
            return reports;
        }

        private void consumeScanResults() {
            if (nearbyDevices != null) {
                uniqueLeDevices.addAll(nearbyDevices.keySet());
            }
            nearbyDevices = new ConcurrentHashMap<>();
            final long startTime = System.nanoTime();
            long currTime = System.nanoTime();
            while (currTime - startTime < processingWindowNanos) {
                if (leDevicesStream.size() > 0) {
                    ScanResult scanResult = leDevicesStream.remove();
                    String deviceAddress = scanResult.getDevice().toString();
                    if(nearbyDevices.containsKey(deviceAddress)) {
                        Objects.requireNonNull(nearbyDevices.get(deviceAddress)).add(scanResult);
                    } else {
                        nearbyDevices.put(deviceAddress, new LinkedList<>(Collections.singletonList(scanResult)));
                    }
                }
                currTime = System.nanoTime();
            }
        }

        @Override
        public void run() {
            while (true) {
                consumeScanResults();
//                updateLeScannerUi(scannerIteration++, nearbyDevices.size(),
//                        uniqueLeDevices.size(), getProximityReport());
            }
        }
    });


    public ScannerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}