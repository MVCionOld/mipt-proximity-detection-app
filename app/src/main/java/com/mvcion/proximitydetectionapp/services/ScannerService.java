package com.mvcion.proximitydetectionapp.services;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.mvcion.proximitydetectionapp.common.preferences.PreferencesFacade;
import com.mvcion.proximitydetectionapp.dto.ProximityDetectionScanResultDto;
import com.mvcion.proximitydetectionapp.common.service.ServiceUuis;

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

    boolean smsBounded = false;
    ServiceConnection smsConnection;
    Intent smsIntent;
    StorageManagerService.StorageManagerBinder smsBinder;

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

    private Thread proximityResultsDumper;

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
        private ArrayList<ProximityDetectionScanResultDto> proximityResults = new ArrayList<>();

        private void consumeScanResults() {
            if (nearbyDevices != null) {
                uniqueLeDevices.addAll(nearbyDevices.keySet());
            }
            proximityResults = new ArrayList<>();
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

        private ArrayList<String> getProximityReport() {
            ArrayList<String> reports = new ArrayList<>(nearbyDevices.size());
            for (Map.Entry<String, LinkedList<ScanResult>> entry: nearbyDevices.entrySet()) {
                ProximityDetectionScanResultDto result = new ProximityDetectionScanResultDto(
                        entry.getKey(),
                        entry.getValue()
                );
                proximityResults.add(result);
                reports.add(result.toString());
            }
            return reports;
        }

        private Thread getProximityResultsDumper() {
            return new Thread(() -> smsBinder
                    .getService()
                    .putProximityResults(proximityResults));
        }

        @Override
        public void run() {
            while (true) {

                if (proximityResultsDumper != null && proximityResultsDumper.isAlive()) {
                    try {
                        proximityResultsDumper.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                consumeScanResults();

                ArrayList<String> proximityReport = getProximityReport();

                Intent proximityReportIntent = new Intent("ScannerService");
                proximityReportIntent
                        .putExtra("devicesNearbyNum", nearbyDevices.size())
                        .putExtra("scannerIteration", ++scannerIteration)
                        .putExtra("allUniqueDevicesNum", uniqueLeDevices.size())
                        .putStringArrayListExtra("proximityReport", proximityReport);

                sendBroadcast(proximityReportIntent);

                proximityResultsDumper = getProximityResultsDumper();
                proximityResultsDumper.start();
            }
        }
    });


    public ScannerService() {
        Log.d(TAG, "constructor");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");

        smsIntent = new Intent(getApplicationContext(), StorageManagerService.class);
        smsConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.d(TAG, "StorageManagerService onServiceConnected");
                smsBounded = true;
                smsBinder = (StorageManagerService.StorageManagerBinder) service;
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.d(TAG, "StorageManagerService onServiceDisconnected");
                smsBounded = false;
            }
        };

        bindService(smsIntent, smsConnection, BIND_AUTO_CREATE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");

        processingWindowNanos = intent.getLongExtra(
                "processingWindowNanos",
                PreferencesFacade.DEFAULT_SCANNER_PROCESSING_WINDOW_NANOS_VALUE
        );
        reportDelayMillis = intent.getLongExtra(
                "reportDelayMillis",
                PreferencesFacade.DEFAULT_SCANNER_REPORT_DELAY_MILLIS
        );
        scannerMode = intent.getIntExtra(
                "scannerMode",
                PreferencesFacade.DEFAULT_SCANNER_MODE_VALUE
        );
        callbackType = intent.getIntExtra(
                "callbackType",
                PreferencesFacade.DEFAULT_CALLBACK_TYPE_VALUE
        );
        matchMode = intent.getIntExtra(
                "matchMode",
                PreferencesFacade.DEFAULT_MATCH_MODE_VALUE
        );
        numOfMatches = intent.getIntExtra(
                "numOfMatches",
                PreferencesFacade.DEFAULT_NUM_OF_MATCHES_VALUE
        );

        if (bluetoothAdapter == null) {
            Log.e(TAG, "BluetoothAdapter is not found.");
        } else {
            if (!bluetoothAdapter.isEnabled()) {
                Log.v(TAG, "Enabling Bluetooth Adapter.");
                bluetoothAdapter.enable();
            }

            bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();

            if (bluetoothLeScanner != null) {
                Log.v(TAG, "Scanner is found.");
                scanResultsProducer.start();
                scanResultsConsumer.start();
            } else {
                Log.e(TAG, "Scanner is not found.");
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");

        if (scanResultsProducer != null) {
            if (scanResultsProducer.isAlive()) {
                scanResultsProducer.interrupt();
            }
            try {
                scanResultsProducer.join();
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }
        }

        if (scanResultsConsumer != null) {
            if (scanResultsConsumer.isAlive()) {
                scanResultsConsumer.interrupt();
            }
            try {
                scanResultsConsumer.join();
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }
        }

        if (proximityResultsDumper != null) {
            if (proximityResultsDumper.isAlive()) {
                proximityResultsDumper.interrupt();
            }
            try {
                proximityResultsDumper.join();
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }
        }

        if (bluetoothLeScanner != null) {
            bluetoothLeScanner.stopScan(leScanCallback);
        }

        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        Log.d(TAG, "onRebind");
        super.onRebind(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind");
        return null;
    }
}