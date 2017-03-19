/*
 *
 *  * Created by J on  2017.
 *  * Copyright (c) 2017.  All rights reserved.
 *  *
 *  * Last modified 17-3-13 上午11:12
 *  *
 *  * Project name: 911fan
 *  *
 *  * Contact me:
 *  * WeChat:  worromoT_
 *  * Email: 2212131349@qq.com
 *  *
 *  * Warning:If my code is same as yours, then i copy you!
 *
 */

package com.imfan.j.a91fan.netease;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import static com.imfan.j.a91fan.util.Constant.NetEaseAPP_KEY;

/**
 * Created by J on 2017/1/5 0005.
 */

public class NeteaseClient {

    static public String nonce = "123456"; // 当做随机数使用
    static public String curTime = null;
    static public String checkSum = null;

        private static final String BASE_URL = "https://api.netease.im/nimserver/";

        public static AsyncHttpClient client = new AsyncHttpClient();

        public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
            client.get(getAbsoluteUrl(url), params, responseHandler);
        }

        public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
            client.post(getAbsoluteUrl(url), params, responseHandler);
        }


        private static String getAbsoluteUrl(String relativeUrl) {
            return BASE_URL + relativeUrl;
        }

        public static void addHeaders() {
            NeteaseClient.client.removeAllHeaders();
            NeteaseClient.client.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
            NeteaseClient.client.addHeader("AppKey", NetEaseAPP_KEY);
            NeteaseClient.client.addHeader("Nonce", nonce);
            NeteaseClient.client.addHeader("CurTime", curTime);
            NeteaseClient.client.addHeader("CheckSum", checkSum);
        }
}
