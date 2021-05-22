package com.mvcion.proximitydetectionapp.dao;

import android.content.ContentValues;
import android.database.Cursor;

import com.mvcion.proximitydetectionapp.common.db.DBHelper;
import com.mvcion.proximitydetectionapp.dto.ProximityDetectionScanResultDto;

public class ProximityDetectionScanResultDao {
    public static ProximityDetectionScanResultDto getDto(Cursor cursor) {
        ProximityDetectionScanResultDto result = new ProximityDetectionScanResultDto();
        result.setMac(
                cursor.getString(
                        cursor.getColumnIndex(
                                DBHelper.SCANNED_RECORDS_FIELD_DEVICE_MAC
                        )
                )
        );
        result.setProcessedDttm(
                cursor.getString(
                        cursor.getColumnIndex(
                                DBHelper.SCANNED_RECORDS_FIELD_PROCESSED_DTTM
                        )
                )
        );
        result.setMinRssi(
                cursor.getInt(
                        cursor.getColumnIndex(
                                DBHelper.SCANNED_RECORDS_FIELD_MIN_RSSI
                        )
                )
        );
        result.setMaxRssi(
                cursor.getInt(
                        cursor.getColumnIndex(
                                DBHelper.SCANNED_RECORDS_FIELD_MAX_RSSI
                        )
                )
        );
        result.setAvgRssi(
                cursor.getDouble(
                        cursor.getColumnIndex(
                                DBHelper.SCANNED_RECORDS_FIELD_AVG_RSSI
                        )
                )
        );
        result.setMinTxPower(
                cursor.getInt(
                        cursor.getColumnIndex(
                                DBHelper.SCANNED_RECORDS_FIELD_MIN_TXPOWER
                        )
                )
        );
        result.setMaxTxPower(
                cursor.getInt(
                        cursor.getColumnIndex(
                                DBHelper.SCANNED_RECORDS_FIELD_MAX_TXPOWER
                        )
                )
        );
        result.setAvgTxPower(
                cursor.getDouble(
                        cursor.getColumnIndex(
                                DBHelper.SCANNED_RECORDS_FIELD_AVG_TXPOWER
                        )
                )
        );
        result.setCounter(
                cursor.getInt(
                        cursor.getColumnIndex(
                                DBHelper.SCANNED_RECORDS_FIELD_COUNTER
                        )
                )
        );
        return result;
    }

    public static ContentValues putDto(ProximityDetectionScanResultDto result) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(
                DBHelper.SCANNED_RECORDS_FIELD_SERVICE_ID,
                0
        );
        contentValues.put(
                DBHelper.SCANNED_RECORDS_FIELD_DEVICE_MAC,
                result.getMac()
        );
        contentValues.put(
                DBHelper.SCANNED_RECORDS_FIELD_DEVICE_NAME,
                result.getMac()
        );
        contentValues.put(
                DBHelper.SCANNED_RECORDS_FIELD_PROCESSED_DTTM,
                result.getProcessedDttm()
        );
        contentValues.put(
                DBHelper.SCANNED_RECORDS_FIELD_MIN_RSSI,
                result.getMinRssi()
        );
        contentValues.put(
                DBHelper.SCANNED_RECORDS_FIELD_MAX_RSSI,
                result.getMaxRssi()
        );
        contentValues.put(
                DBHelper.SCANNED_RECORDS_FIELD_AVG_RSSI,
                result.getAvgRssi()
        );
        contentValues.put(
                DBHelper.SCANNED_RECORDS_FIELD_MIN_TXPOWER,
                result.getMinTxPower()
        );
        contentValues.put(
                DBHelper.SCANNED_RECORDS_FIELD_MAX_TXPOWER,
                result.getMaxTxPower()
        );
        contentValues.put(
                DBHelper.SCANNED_RECORDS_FIELD_AVG_TXPOWER,
                result.getAvgTxPower()
        );
        contentValues.put(
                DBHelper.SCANNED_RECORDS_FIELD_COUNTER,
                result.getCounter()
        );
        // TODO: пофиксить хардкод
        contentValues.put(
                DBHelper.SCANNED_RECORDS_FIELD_PROCESSING_WINDOW_MILLIS,
                5000
        );
        return contentValues;
    }
}
