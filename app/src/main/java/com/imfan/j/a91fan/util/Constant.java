package com.imfan.j.a91fan.util;

/**
 * Created by J on 2017/1/4 0004.
 * 网易和微信的一些常量
 */

public class Constant {

    // 微信的相关数据
    public static final String WX_APP_ID = "wxbf4adad4b62dda38"; //
    public static final String WX_AppSecret = "ba73583e638325f83945c8748dae711b";
    public static final String accessTokenUrl1 = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + WX_APP_ID + "&secret=" + WX_AppSecret + "&code=";
    public static final String accessTokenUrl2 = "&grant_type=authorization_code";
    public static final String refresh_tokenUrl = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=" + WX_APP_ID + "&grant_type=refresh_token&refresh_token=";

    // 网易的相关数据
    public static final String NetEaseAPP_KEY = "44e48bb1800a8ad7e3c44d8a424d4b59";
    public static final String NetEaseAPP_SECRET = "2db8e34c71c5";

}

