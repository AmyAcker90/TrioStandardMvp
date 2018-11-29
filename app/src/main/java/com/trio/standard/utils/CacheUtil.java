package com.trio.standard.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by lixia on 2018/10/11.
 */

public class CacheUtil {

    private static Context mContext;
    private final static String name = "data";

    public static CacheUtil getInstance(Context context) {
        mContext = context;
        return CacheUtilHolder.mInstance;
    }

    private static class CacheUtilHolder {
        private static final CacheUtil mInstance = new CacheUtil();
    }

    public void putString(String key, String value) {
        SharedPreferences.Editor editor = mContext.getSharedPreferences(name, Context.MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String getString(String key) {
        SharedPreferences sp = mContext.getSharedPreferences(name, Context.MODE_PRIVATE);
        return sp.getString(key, null);
    }

    public void putBoolen(String key, boolean value) {
        SharedPreferences.Editor editor = mContext.getSharedPreferences(name, Context.MODE_PRIVATE).edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public boolean getBoolen(String key) {
        SharedPreferences sp = mContext.getSharedPreferences(name, Context.MODE_PRIVATE);
        return sp.getBoolean(key, false);
    }

    public void putInt(String key, int value) {
        SharedPreferences.Editor editor = mContext.getSharedPreferences(name, Context.MODE_PRIVATE).edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public int getInt(String key) {
        SharedPreferences sp = mContext.getSharedPreferences(name, Context.MODE_PRIVATE);
        return sp.getInt(key, 0);
    }

    public void putLong(String key, Long value) {
        SharedPreferences.Editor editor = mContext.getSharedPreferences(name, Context.MODE_PRIVATE).edit();
        editor.putLong(key, value);
        editor.commit();
    }

    public float getLong(String key) {
        SharedPreferences sp = mContext.getSharedPreferences(name, Context.MODE_PRIVATE);
        return sp.getLong(key, 0);
    }

    public void putFloat(String key, float value) {
        SharedPreferences.Editor editor = mContext.getSharedPreferences(name, Context.MODE_PRIVATE).edit();
        editor.putFloat(key, value);
        editor.commit();
    }

    public float getFloat(String key) {
        SharedPreferences sp = mContext.getSharedPreferences(name, Context.MODE_PRIVATE);
        return sp.getFloat(key, 0);
    }

}
