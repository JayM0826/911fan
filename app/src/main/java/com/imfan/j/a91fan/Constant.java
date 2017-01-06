package com.imfan.j.a91fan;

/**
 * Created by J on 2017/1/4 0004.
 */

public class Constant {

    public static final String APP_ID = "wxbf4adad4b62dda38";
    public static final String AppSecret = "ba73583e638325f83945c8748dae711b";
    public static final String accessTokenUrl1 = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + APP_ID + "&secret=" + AppSecret + "&code=";
    public static final String accessTokenUrl2 = "&grant_type=authorization_code";
    public static final String refresh_tokenUrl = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=" + APP_ID + "&grant_type=refresh_token&refresh_token=";


}

