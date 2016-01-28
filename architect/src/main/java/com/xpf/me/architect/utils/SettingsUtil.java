package com.xpf.me.architect.utils;

import android.preference.PreferenceManager;

import com.xpf.me.architect.app.AppData;

/**
 * Created by pengfeixie on 16/1/28.
 */
public class SettingsUtil {

    public static boolean isDarkMode() {
        return PreferenceManager.getDefaultSharedPreferences(AppData.getContext()).getBoolean("theme_key", false);
    }

    public static boolean isShowFCV() {
        return PreferenceManager.getDefaultSharedPreferences(AppData.getContext()).getBoolean("no_dis_key", true);
    }

}
