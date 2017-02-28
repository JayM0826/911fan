package com.imfan.j.a91fan.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by J on 2017/1/4 0004.
 */

/**
 * Created by J on 2017/1/4 0004.
 * 注意：这里的account与首选项里的account是一个,都是微信的unionid
 */

public class Preferences {


    private static final String HUANXIN_PASS = "password"; // 网易的token;
    private static final String NETEASE_TOKEN = "token"; // 网易的token;
    // 自建数据库创造的字段
    private static final String FAN_ID = "fan_id"; // 粉丝号，第多少号粉丝
    // 第一步获取到的信息字段
    private static String WX_CODE = "code";
    private static String WX_COUNTRY = "country";
    private static String WX_LANGUAGE = "lang";
    // 第二步获取到的字段
    private static String WX_ACCESS_TOKEN = "access_token";
    private static String WX_EXPIRES_IN = "expires_in";
    private static String WX_REFRESH_TOKEN = "refresh_token";
    private static String WX_OPENID = "openid"; // 第三方APP中是唯一的
    private static String WX_SCOPE = "scope";
    // 第三步获取到的信息字段
    private static String WX_NICKNAME = "nickname";
    private static String WX_SEX = "sex";
    private static String WX_PROVINCE = "province";
    private static String WX_CITY = "city";
    private static String WX_HEADIMGURL = "headimgurl";
    private static String WX_UNIONID = "unionid";

    public static String getFanId(){
        return getString(FAN_ID);
    }

    public static void setFanId(String fanId){
        saveString(FAN_ID, fanId);
    }

    public static String getUserAccount() {
        if (getWxUnionid() == null){
            return null;
        }else {
            return getWxUnionid().toLowerCase();
        }
    }

    public static String getNeteaseToken() {
        return getString(NETEASE_TOKEN);
    }

    public static void setToken(String token) {
        saveString(NETEASE_TOKEN, token);
        // 环信的密码
        saveString(HUANXIN_PASS, token);
    }

    public static String getWxLanguage() {
        return getString(WX_LANGUAGE);
    }

    public static void setWxLanguage(String wxLanguage) {
        saveString(WX_LANGUAGE, wxLanguage);
    }

    public static String getWxCountry() {
        return getString(WX_COUNTRY);
    }

    public static void setWxCountry(String wxCountry) {
        saveString(WX_COUNTRY, wxCountry);
    }

    public static String getWxCode() {
        return getString(WX_CODE);
    }

    public static void setWxCode(String wxCode) {
        saveString(WX_CODE, wxCode);
    }

    public static String getWxAccessToken() {
        return getString(WX_ACCESS_TOKEN);
    }

    public static void setWxAccessToken(String wxAccessToken) {
        saveString(WX_ACCESS_TOKEN, wxAccessToken);
    }

    public static int getWxExpiresIn() {
        return getInt(WX_EXPIRES_IN);
    }

    public static void setWxExpiresIn(int wxExpiresIn) {
        saveInt(WX_EXPIRES_IN, wxExpiresIn);
    }

    public static String getWxRefreshToken() {
        return getString(WX_REFRESH_TOKEN);
    }

    public static void setWxRefreshToken(String wxRefreshToken) {
        saveString(WX_REFRESH_TOKEN, wxRefreshToken);
    }

    public static String getWxrOpenid() {
        return getString(WX_OPENID);
    }

    public static void setWxOpenid(String wxOpenid) {
        saveString(WX_OPENID, wxOpenid);
    }

    public static String getWxScope() {
        return getString(WX_SCOPE);
    }

    public static void setWxScope(String wxScope) {
        saveString(WX_SCOPE, wxScope);
    }

    public static String getWxNickname() {
        return getString(WX_NICKNAME);
    }

    public static void setWxNickname(String wxNickname) {
        saveString(WX_NICKNAME, wxNickname);
    }

    public static int getWxSex() {
        return getInt(WX_SEX);
    }

    public static void setWxSex(int wxSex) {
        saveInt(WX_SEX, wxSex);
    }

    public static String getWxProvince() {
        return getString(WX_PROVINCE);
    }

    public static void setWxProvince(String wxProvince) {
        saveString(WX_PROVINCE, wxProvince);
    }

    public static String getWxCity() {
        return getString(WX_CITY);
    }

    public static void setWxCity(String wxCity) {
        saveString(WX_CITY, wxCity);
    }

    public static String getWxHeadimgurl() {
        return getString(WX_HEADIMGURL);
    }

    public static void setWxHeadimgurl(String wxHeadimgurl) {
        saveString(WX_HEADIMGURL, wxHeadimgurl);
    }

    public static String getWxUnionid() {
        return getString(WX_UNIONID);
    }

    public static void setWxUnionid(String wxUnionid) {
        saveString(WX_UNIONID, wxUnionid);
        ;
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


    /**
     * 创建首选项
     * @return 返回创建的首选项
     */
    static SharedPreferences getSharedPreferences() {

        return Cache.getContext().getSharedPreferences("userinfo", Context.MODE_PRIVATE);
    }
}