package com.alertsystem.utils;

import android.content.Context;

public class SharedPreferences {

    private static SharedPreferences ourInstance = null;
    private static android.content.SharedPreferences settings = null;

    private static final String PREFS_NAME = "AlertSystem";

    private Context context;

    public SharedPreferences() {
        this.context = context;
    }


    public synchronized static SharedPreferences getInstance() {
        if (ourInstance == null) {
            ourInstance = new SharedPreferences();
        }
        return ourInstance;
    }

    private void SharedPreferencesUtil() {
    }

    public void initializeSharePreferences(Context context) {
        settings = context.getSharedPreferences
                (SharedPreferences.PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void saveSharedPreferencesString(String key, String value) {
        android.content.SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String fetchSharedPreferenesString(String key, String defaultValue) {
        return settings.getString(key, defaultValue);
    }

    public void saveSharedPreferencesLong(String key, long value) {
        android.content.SharedPreferences.Editor editor = settings.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    public long fetchSharedPreferenesLong(String key, long defaultValue) {
        return settings.getLong(key, defaultValue);
    }

    public boolean fetchSharedPreferenesBoolean(String key, boolean defaultValue) {
        return settings.getBoolean(key, defaultValue);
    }

    public void saveSharedPreferencesBoolean(String key, boolean value) {
        android.content.SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public int fetchSharedPreferenesInteger(String key, int defaultValue) {
        return settings.getInt(key, defaultValue);
    }

    public void saveSharedPreferencesInteger(String key, int value) {
        android.content.SharedPreferences settings = context.getSharedPreferences(SharedPreferences.PREFS_NAME, 0);
        android.content.SharedPreferences.Editor editor = settings.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public void clearPreferences() {
        android.content.SharedPreferences.Editor editor = settings.edit();
        editor.clear().apply();
        /*SharedPreferences settings = context.getSharedPreferences(SharedPreferencesUtils.PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.clear();
        editor.apply();
        editor.commit();*/
    }
}
