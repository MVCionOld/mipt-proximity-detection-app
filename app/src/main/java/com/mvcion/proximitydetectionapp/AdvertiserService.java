package com.mvcion.proximitydetectionapp;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.content.Intent;
import android.os.IBinder;
import android.os.ParcelUuid;
import android.util.Log;

import com.mvcion.proximitydetectionapp.common.preferences.PreferencesFacade;
import com.mvcion.proximitydetectionapp.common.service.ServiceUuis;

public class AdvertiserService extends Service {
    private static final String TAG = "AdvertiserService";

    private int advertiserMode;
    private int advertiserTxPower;
    private boolean isConnectable;

    private BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private BluetoothLeAdvertiser bluetoothLeAdvertiser;

    private final AdvertiseCallback leAdvertiseCallback = new AdvertiseCallback() {

        @Override
        public void onStartSuccess(AdvertiseSettings settingsInEffect) {
            super.onStartSuccess(settingsInEffect);
            Log.v(TAG, settingsInEffect.toString());
        }

        @Override
        public void onStartFailure(int errorCode) {
            super.onStartFailure(errorCode);
            Log.e(TAG, String.valueOf(errorCode));
        }
    };

    private Thread scanAdvertiser = new Thread(new Runnable() {
        @Override
        public void run() {
            bluetoothLeAdvertiser = bluetoothAdapter.getBluetoothLeAdvertiser();

            if (bluetoothLeAdvertiser != null) {
                Log.v(TAG, "Scanner is found.");
            } else {
                Log.e(TAG, "Scanner is not found.");
                return;
            }

            ParcelUuid serviceUuid = ServiceUuis.getServiceUuid();
            AdvertiseData.Builder advertiseDataBuilder = new AdvertiseData
                    .Builder()
                    .addServiceUuid(serviceUuid)
                    .setIncludeTxPowerLevel(true);

            AdvertiseSettings.Builder advertiseSettingsBuilder = new AdvertiseSettings
                    .Builder()
                    .setAdvertiseMode(advertiserMode)
                    .setTxPowerLevel(advertiserTxPower)
                    .setConnectable(isConnectable);

            bluetoothLeAdvertiser.startAdvertising(
                    /*settings = */advertiseSettingsBuilder.build(),
                    /*advertiseData = */advertiseDataBuilder.build(),
                    /*scanResponse = */null,
                    /*callback = */leAdvertiseCallback);
        }
    });

    public AdvertiserService() {
        Log.d(TAG, "constructor");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");

        advertiserMode = intent.getIntExtra(
                "advertiserMode", PreferencesFacade.DEFAULT_ADVERTISER_MODE_VALUE);
        advertiserTxPower = intent.getIntExtra(
                "advertiserTxPower", PreferencesFacade.DEFAULT_ADVERTISER_TX_POWER_VALUE);
        isConnectable = intent.getBooleanExtra(
                "isConnectable", PreferencesFacade.DEFAULT_ADVERTISER_IS_CONNECTABLE_VALUE);

        if (bluetoothAdapter == null) {
            Log.e(TAG, "BluetoothAdapter is not found.");
        } else {
            if (!bluetoothAdapter.isEnabled()) {
                Log.v(TAG, "Enabling Bluetooth Adapter.");
                bluetoothAdapter.enable();
            }
            scanAdvertiser.start();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
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