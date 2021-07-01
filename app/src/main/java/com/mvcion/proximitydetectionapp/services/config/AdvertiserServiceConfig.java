package com.mvcion.proximitydetectionapp.services.config;

import com.mvcion.proximitydetectionapp.common.preferences.DefaultPreferences;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class AdvertiserServiceConfig {
    @Setter private int serviceId;
    @Setter private int advertiserMode;
    @Setter private int advertiserTxPower;
    private final boolean includeDeviceName = true;
    private final boolean includeTxPowerLevel = true;
    private final boolean connectable = DefaultPreferences.isAdvertiseIsConnectableValue();
}
