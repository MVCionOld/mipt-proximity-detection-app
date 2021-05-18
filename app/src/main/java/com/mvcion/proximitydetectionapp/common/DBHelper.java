package com.mvcion.proximitydetectionapp.common;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.MessageFormat;

public class DBHelper extends SQLiteOpenHelper {

    public static final String PROXIMITY_DETECTION_DB_NAME = "PROXIMITY_DETECTION_";
    public static final int SCANNED_RECORDS_DB_VERSION = 1;

    public static final String SCANNED_RECORDS_TABLE_NAME = "SCANNED_RECORDS";
    public static final String SCANNED_RECORDS_FIELD_ID = "_id";
    public static final String SCANNED_RECORDS_FIELD_SERVICE_ID = "service_id";
    public static final String SCANNED_RECORDS_FIELD_DEVICE_MAC = "device_mac";
    public static final String SCANNED_RECORDS_FIELD_DEVICE_NAME = "device_name";
    public static final String SCANNED_RECORDS_FIELD_PROCESSED_DTTM = "processed_dttm";
    public static final String SCANNED_RECORDS_FIELD_MIN_RSSI = "min_rssi";
    public static final String SCANNED_RECORDS_FIELD_MAX_RSSI = "max_rssi";
    public static final String SCANNED_RECORDS_FIELD_AVG_RSSI = "avg_rssi";
    public static final String SCANNED_RECORDS_FIELD_MIN_TXPOWER = "min_txpower";
    public static final String SCANNED_RECORDS_FIELD_MAX_TXPOWER = "max_txpower";
    public static final String SCANNED_RECORDS_FIELD_AVG_TXPOWER = "avg_txpower";
    public static final String SCANNED_RECORDS_FIELD_PROCESSING_WINDOW_MILLIS = "processing_window_millis";

    public DBHelper(Context context) {
        super(context, PROXIMITY_DETECTION_DB_NAME, null, SCANNED_RECORDS_DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(MessageFormat.format(
        "CREATE TABLE {0} (" +
                "{1} INTEGER PRIMARY KEY, " +
                "{2} INTEGER NOT NULL, " +
                "{3} TEXT, " +
                "{4} TEXT, " +
                "{5} TEXT, " +
                "{6} REAL, " +
                "{7} REAL, " +
                "{8} REAL, " +
                "{9} REAL, " +
                "{10} REAL, " +
                "{11} REAL, " +
                "{12} INTEGER" +
                ");",
                SCANNED_RECORDS_TABLE_NAME,
                SCANNED_RECORDS_FIELD_ID,
                SCANNED_RECORDS_FIELD_SERVICE_ID,
                SCANNED_RECORDS_FIELD_DEVICE_MAC,
                SCANNED_RECORDS_FIELD_DEVICE_NAME,
                SCANNED_RECORDS_FIELD_PROCESSED_DTTM,
                SCANNED_RECORDS_FIELD_MIN_RSSI,
                SCANNED_RECORDS_FIELD_MAX_RSSI,
                SCANNED_RECORDS_FIELD_AVG_RSSI,
                SCANNED_RECORDS_FIELD_MIN_TXPOWER,
                SCANNED_RECORDS_FIELD_MAX_TXPOWER,
                SCANNED_RECORDS_FIELD_AVG_TXPOWER,
                SCANNED_RECORDS_FIELD_PROCESSING_WINDOW_MILLIS
        ));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(MessageFormat.format(
                "DROP TABLE IF EXISTS {0};",
                SCANNED_RECORDS_TABLE_NAME
        ));
        onCreate(db);
    }
}
