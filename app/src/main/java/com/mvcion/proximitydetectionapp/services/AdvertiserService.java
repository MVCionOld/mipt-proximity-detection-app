package com.mvcion.proximitydetectionapp.services;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.mvcion.proximitydetectionapp.common.preferences.DefaultPreferences;
import com.mvcion.proximitydetectionapp.common.service.ServiceUuis;
import com.mvcion.proximitydetectionapp.services.config.AdvertiserServiceConfig;

import java.util.Random;

@SuppressWarnings({"FieldMayBeFinal", "FieldCanBeLocal"})
public class AdvertiserService extends Service {
    private static final String TAG = "AdvertiserService";

    private AdvertiserServiceConfig config = new AdvertiserServiceConfig();

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

            AdvertiseData.Builder advertiseDataBuilder = new AdvertiseData
                    .Builder()
                    .addServiceUuid(ServiceUuis.getServiceUuid())
                    .setIncludeDeviceName(config.isIncludeDeviceName())
                    .setIncludeTxPowerLevel(config.isIncludeTxPowerLevel());

            AdvertiseSettings.Builder advertiseSettingsBuilder = new AdvertiseSettings
                    .Builder()
                    .setAdvertiseMode(config.getAdvertiserMode())
                    .setTxPowerLevel(config.getAdvertiserTxPower())
                    .setConnectable(config.isConnectable());

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

    private void loadAdvertiserServiceConfig(Intent intent) {
        config.setServiceId(new Random().nextInt());
        config.setAdvertiserMode(intent.getIntExtra(
                "advertiserMode",
                DefaultPreferences.getAdvertiseModeValue()
        ));
        config.setAdvertiserTxPower(intent.getIntExtra(
                "advertiserTxPower",
                DefaultPreferences.getAdvertiseTxPowerValue()
        ));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");

        loadAdvertiserServiceConfig(intent);

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
        Log.d(TAG, "onDestroy");
        if (scanAdvertiser != null) {
            try {
                scanAdvertiser.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (bluetoothLeAdvertiser != null) {
            bluetoothLeAdvertiser.stopAdvertising(leAdvertiseCallback);
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