package com.mvcion.proximitydetectionapp.services.config;

import android.bluetooth.le.AdvertiseSettings;

import com.mvcion.proximitydetectionapp.common.preferences.DefaultPreferences;

import org.jetbrains.annotations.NotNull;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;

@Getter
@NoArgsConstructor
public class AdvertiserServiceConfig implements Cloneable {
    @Setter private int serviceId;
    @Setter private AtomicInteger advertiserMode = new AtomicInteger(DefaultPreferences.getAdvertiseModeValue());
    @Setter private AtomicInteger advertiserTxPower = new AtomicInteger(DefaultPreferences.getAdvertiseTxPowerValue());
    private final boolean includeDeviceName = true;
    private final boolean includeTxPowerLevel = true;
    private final boolean connectable = DefaultPreferences.isAdvertiseIsConnectableValue();

    @SneakyThrows
    @NotNull
    @Override
    public String toString() {
        final HashMap<Integer, String> INVERSE_ADVERTISE_MODE_MAP = new HashMap<Integer, String>(){{
            put(AdvertiseSettings.ADVERTISE_MODE_LOW_POWER,   "LOW POWER");
            put(AdvertiseSettings.ADVERTISE_MODE_BALANCED,    "BALANCED");
            put(AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY, "LOW LATENCY");
        }};
        final HashMap<Integer, String> INVERSE_ADVERTISE_TX_POWER_MAP = new HashMap<Integer, String>(){{
            put(AdvertiseSettings.ADVERTISE_TX_POWER_ULTRA_LOW, "ULTRA LOW");
            put(AdvertiseSettings.ADVERTISE_TX_POWER_LOW,       "LOW");
            put(AdvertiseSettings.ADVERTISE_TX_POWER_MEDIUM,    "MEDIUM");
            put(AdvertiseSettings.ADVERTISE_TX_POWER_HIGH,      "HIGH");
        }};
        return MessageFormat.format(
                "Mode: {0}\nTxPower: {1}",
                INVERSE_ADVERTISE_MODE_MAP.get(advertiserMode.get()),
                INVERSE_ADVERTISE_TX_POWER_MAP.get(advertiserTxPower.get())
        );
    }

    @SneakyThrows
    @NotNull
    @Override
    public AdvertiserServiceConfig clone() {
        AdvertiserServiceConfig clone = (AdvertiserServiceConfig) super.clone();
        clone.getAdvertiserMode().set(this.advertiserMode.get());
        clone.getAdvertiserTxPower().set(this.advertiserTxPower.get());
        return clone;
    }
}
