package com.mvcion.proximitydetectionapp.common.preferences;

import android.content.Context;

import androidx.preference.PreferenceManager;

import java.util.HashMap;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PreferenceTemplateGetterArgs<T> {
    private Context context;
    private HashMap<String, T> map;
    private String key;
    private String defaultKey;
    private T defaultValue;

    public PreferenceTemplateGetter<T> buildGetter() {
        PreferenceTemplateGetter<T> ptg = new PreferenceTemplateGetter<>();
        ptg.setSharedPreferences(PreferenceManager.getDefaultSharedPreferences(context));
        ptg.setMap(map);
        ptg.setKey(key);
        ptg.setDefaultKey(defaultKey);
        ptg.setDefaultValue(defaultValue);
        return ptg;
    }
}
