package com.imfan.j.a91fan;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by J on 2017/1/4 0004.
 */

public class Preferences {
    private static final String KEY_USER_ACCOUNT = "account"; // 账户
    private static final String KEY_USER_TOKEN = "token";  // 口令

    public static void saveUserAccount(String account) {
        saveString(KEY_USER_ACCOUNT, account);
    }

    public static String getUserAccount() {
        return getString(KEY_USER_ACCOUNT);
    }

    public static void saveUserToken(String token) {
        saveString(KEY_USER_TOKEN, token);
    }

    public static String getUserToken() {
        return getString(KEY_USER_TOKEN);
    }

    private static void saveString(String key, String value) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(key, value);
        editor.commit();
    }

    private static String getString(String key) {
        return getSharedPreferences().getString(key, null);
    }

    static SharedPreferences getSharedPreferences() {
        return Cache.getContext().getSharedPreferences("Demo", Context.MODE_PRIVATE);
    }
}
