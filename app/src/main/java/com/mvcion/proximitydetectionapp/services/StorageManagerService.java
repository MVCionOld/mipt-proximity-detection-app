package com.mvcion.proximitydetectionapp.services;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.mvcion.proximitydetectionapp.common.db.DBHelper;
import com.mvcion.proximitydetectionapp.dto.ProximityDetectionScanResultDto;
import com.mvcion.proximitydetectionapp.dao.ProximityDetectionScanResultDao;

import java.util.ArrayList;

public class StorageManagerService extends Service {
    private static final String TAG = "StorageManagerService";

    private DBHelper dbHelper;

    public StorageManagerService() {
    }

    public void putProximityResults(ArrayList<ProximityDetectionScanResultDto> proximityResults) {
        SQLiteDatabase writableDatabase = dbHelper.getWritableDatabase();
        for (ProximityDetectionScanResultDto result: proximityResults) {
            ContentValues contentValues = ProximityDetectionScanResultDao.putDto(result);
            writableDatabase.insert(
                    DBHelper.SCANNED_RECORDS_TABLE_NAME,
                    null,
                    contentValues
            );
        }
    }

    public ArrayList<ProximityDetectionScanResultDto> getProximityResults() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                DBHelper.SCANNED_RECORDS_TABLE_NAME,
                null, null, null, null, null, null
        );
        ArrayList<ProximityDetectionScanResultDto> proximityResults = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                proximityResults.add(ProximityDetectionScanResultDao.getDto(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return proximityResults;
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate");
        if (dbHelper == null) {
            dbHelper = new DBHelper(getApplicationContext());
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        if (dbHelper == null) {
            dbHelper = new DBHelper(getApplicationContext());
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
        return new StorageManagerBinder();
    }

    class StorageManagerBinder extends Binder {
        StorageManagerService getService() {
            return StorageManagerService.this;
        }
    }
}