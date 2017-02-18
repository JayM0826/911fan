package com.imfan.j.a91fan.util;

import android.content.Context;

import com.netease.nim.uikit.NimUIKit;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;

/**
 * Created by J on 2017/1/4 0004.
 */

public class Cache {

    private static Context context;

    private static String account;

    private static StatusBarNotificationConfig notificationConfig;

    public static void clear() {
        account = null;
    }

    public static String getAccount() {
        return account;
    }

    public static void setAccount(String account) {
        Cache.account = account;
        NimUIKit.setAccount(account);
    }

    public static StatusBarNotificationConfig getNotificationConfig() {
        return notificationConfig;
    }

    public static void setNotificationConfig(StatusBarNotificationConfig notificationConfig) {
        Cache.notificationConfig = notificationConfig;
    }

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        Cache.context = context.getApplicationContext();
    }
}
