package com.mvcion.proximitydetectionapp.common.preferences;

import android.content.SharedPreferences;

import java.util.HashMap;

import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;

@Setter
@NoArgsConstructor
public class PreferenceTemplateGetter<T> {
    private HashMap<String, T> map;
    private SharedPreferences sharedPreferences;
    private String key;
    private String defaultKey;
    private T defaultValue;

    @SneakyThrows
    public T get() {
        T preference = map.get(sharedPreferences.getString(key, defaultKey));
        if (preference != null) {
            return preference;
        } else {
            return defaultValue;
        }
    }
}
