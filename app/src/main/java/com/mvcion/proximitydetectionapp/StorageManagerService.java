package com.mvcion.proximitydetectionapp;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class StorageManagerService extends Service {
    public StorageManagerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new StorageManagerBinder();
    }

    class StorageManagerBinder extends Binder {
        StorageManagerService getService() {
            return StorageManagerService.this;
        }
    }
}