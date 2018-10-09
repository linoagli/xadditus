/**
 * xadditus-android Project.
 * com.linoagli.android.xadditus
 *
 * @author Faye-Lino Agli, username: linoagli
 */
package com.linoagli.android.xadditus.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefsHelper {
    public static void save(Context context, String key, String value) {
        SharedPreferences prefs = context.getSharedPreferences(key, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static void save(Context context, String key, int value) {
        SharedPreferences prefs = context.getSharedPreferences(key, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static void save(Context context, String key, long value) {
        SharedPreferences prefs = context.getSharedPreferences(key, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    public static void save(Context context, String key, float value) {
        SharedPreferences prefs = context.getSharedPreferences(key, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putFloat(key, value);
        editor.commit();
    }

    public static void save(Context context, String key, boolean value) {
        SharedPreferences prefs = context.getSharedPreferences(key, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static String load(Context context, String key, String defaultValue) {
        SharedPreferences prefs = context.getSharedPreferences(key, 0);
        return prefs.getString(key, defaultValue);
    }

    public static int load(Context context, String key, int defaultValue) {
        SharedPreferences prefs = context.getSharedPreferences(key, 0);
        return prefs.getInt(key, defaultValue);
    }

    public static long load(Context context, String key, long defaultValue) {
        SharedPreferences prefs = context.getSharedPreferences(key, 0);
        return prefs.getLong(key, defaultValue);
    }

    public static float load(Context context, String key, float defaultValue) {
        SharedPreferences prefs = context.getSharedPreferences(key, 0);
        return prefs.getFloat(key, defaultValue);
    }

    public static boolean load(Context context, String key, boolean defaultValue) {
        SharedPreferences prefs = context.getSharedPreferences(key, 0);
        return prefs.getBoolean(key, defaultValue);
    }
}