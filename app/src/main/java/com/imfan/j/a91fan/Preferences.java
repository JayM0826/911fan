package com.imfan.j.a91fan;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by J on 2017/1/4 0004.
 */

public class Preferences {

    private static final String WX_KEY_USER_ACCOUNT = "account"; // 账户

    // 第一步获取到的信息字段
    private static String WX_KEY_USER_CODE = "code";
    private static String WX_KEY_USER_COUNTRY = "country";
    private static String WX_KEY_USER_LANGUAGE = "lang";

    public static String getWxKeyUserLanguage() {
        return getString(WX_KEY_USER_LANGUAGE);
    }

    public static void setWxKeyUserLanguage(String wxKeyUserLanguage) {
        saveString(WX_KEY_USER_LANGUAGE, wxKeyUserLanguage);
    }

    public static String getWxKeyUserCountry() {
        return getString(WX_KEY_USER_COUNTRY);
    }

    public static void setWxKeyUserCountry(String wxKeyUserCountry) {
        saveString(WX_KEY_USER_COUNTRY, wxKeyUserCountry);
    }

    public static String getWxKeyUserCode() {
        return getString(WX_KEY_USER_CODE);
    }

    public static void setWxKeyUserCode(String wxKeyUserCode) {
        saveString(WX_KEY_USER_CODE, wxKeyUserCode);
    }




    // 第二步获取到的字段
    private static String WX_KEY_USER_ACCESS_TOKEN = "access_token";
    private static String WX_KEY_USER_EXPIRES_IN = "expires_in";
    private static String WX_KEY_USER_REFRESH_TOKEN = "refresh_token";
    private static String WX_KEY_USER_OPENID = "openid"; // 第三方APP中是唯一的
    private static String WX_KEY_USER_SCOPE = "scope";






    public static String getWxKeyUserAccount() {
        return getString(WX_KEY_USER_ACCOUNT);
    }

    public static String getWxKeyUserAccessToken() {
        return getString(WX_KEY_USER_ACCESS_TOKEN);
    }

    public static void setWxKeyUserAccessToken(String wxKeyUserAccessToken) {
        saveString(WX_KEY_USER_ACCESS_TOKEN,  wxKeyUserAccessToken);
    }

    public static int getWxKeyUserExpiresIn() {
        return getInt(WX_KEY_USER_EXPIRES_IN);
    }

    public static void setWxKeyUserExpiresIn(String wxKeyUserExpiresIn) {
        saveString(WX_KEY_USER_EXPIRES_IN, wxKeyUserExpiresIn);
    }

    public static String getWxKeyUserRefreshToken() {
       return getString(WX_KEY_USER_REFRESH_TOKEN);
    }

    public static void setWxKeyUserRefreshToken(String wxKeyUserRefreshToken) {
        saveString(WX_KEY_USER_REFRESH_TOKEN, wxKeyUserRefreshToken);
    }

    public static String getWxKeyUserOpenid() {
        return getString(WX_KEY_USER_OPENID);
    }

    public static void setWxKeyUserOpenid(String wxKeyUserOpenid) {
        saveString(WX_KEY_USER_OPENID, wxKeyUserOpenid);
    }

    public static String getWxKeyUserScope() {
        return getString(WX_KEY_USER_SCOPE);
    }

    public static void setWxKeyUserScope(String wxKeyUserScope) {
        saveString(WX_KEY_USER_SCOPE, wxKeyUserScope);
    }


    // 第三步获取到的信息字段
    private static String WX_KEY_USER_NICKNAME = "nickname";
    private static String WX_KEY_USER_SEX = "sex";
    private static String WX_KEY_USER_PROVINCE = "province";
    private static String WX_KEY_USER_CITY = "city";
    private static String WX_KEY_USER_HEADIMGURL = "headimgurl";
    private static String WX_KEY_USER_UNIONID = "unionid";

    public static String getWxKeyUserNickname() {
        return getString(WX_KEY_USER_NICKNAME);
    }

    public static void setWxKeyUserNickname(String wxKeyUserNickname) {
        saveString(WX_KEY_USER_NICKNAME, wxKeyUserNickname);
    }

    public static int getWxKeyUserSex() {
        return getInt(WX_KEY_USER_SEX);
    }

    public static void setWxKeyUserSex(int wxKeyUserSex) {
        saveInt(WX_KEY_USER_SEX, wxKeyUserSex);
    }

    public static String getWxKeyUserProvince() {
        return getString(WX_KEY_USER_PROVINCE);
    }

    public static void setWxKeyUserProvince(String wxKeyUserProvince) {
        saveString(WX_KEY_USER_PROVINCE, wxKeyUserProvince);
    }

    public static String getWxKeyUserCity() {
        return getString(WX_KEY_USER_CITY);
    }

    public static void setWxKeyUserCity(String wxKeyUserCity) {
        saveString(WX_KEY_USER_CITY, wxKeyUserCity);
    }

    public static String getWxKeyUserHeadimgurl() {
        return getString(WX_KEY_USER_HEADIMGURL);
    }

    public static void setWxKeyUserHeadimgurl(String wxKeyUserHeadimgurl) {
        saveString(WX_KEY_USER_HEADIMGURL, wxKeyUserHeadimgurl);
    }

    public static String getWxKeyUserUnionid() {
        return getString(WX_KEY_USER_UNIONID);
    }

    public static void setWxKeyUserUnionid(String wxKeyUserUnionid) {
        saveString(WX_KEY_USER_UNIONID, wxKeyUserUnionid);;
    }

    // 保存string类型的数据
    private static void saveString(String key, String value) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(key, value);
        editor.commit();
    }

    private static String getString(String key) {
        return getSharedPreferences().getString(key, null);
    }


    // 保存string类型的数据
    private static void saveInt(String key, int value) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putInt(key, value);
        editor.commit();
    }

    private static int getInt(String key) {
        return getSharedPreferences().getInt(key, 0);
    }



    static SharedPreferences getSharedPreferences() {
        return Cache.getContext().getSharedPreferences("userinfo", Context.MODE_PRIVATE);
    }
}
