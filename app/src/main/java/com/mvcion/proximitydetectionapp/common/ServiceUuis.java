package com.mvcion.proximitydetectionapp.common;

import android.os.ParcelUuid;

import java.util.UUID;

public class ServiceUuis {

    private static final String serviceUuidStr = "0000180D-0000-1000-8000-00805f9b34fb";
    private static final ParcelUuid serviceUuid = new ParcelUuid(UUID.fromString(serviceUuidStr));

    public static String getServiceUuidStr() {
        return serviceUuidStr;
    }

    public static ParcelUuid getServiceUuid() {
        return serviceUuid;
    }
}
