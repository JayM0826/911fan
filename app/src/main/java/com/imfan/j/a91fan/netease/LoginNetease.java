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

import android.content.Context;
import android.icu.text.IDNA;
import android.util.Log;

import com.hyphenate.EMCallBack;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.imfan.j.a91fan.main.activity.MainActivity;
import com.imfan.j.a91fan.util.Cache;
import com.imfan.j.a91fan.util.CustomToast;
import com.imfan.j.a91fan.util.Preferences;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.netease.nim.uikit.NimUIKit;
import com.netease.nim.uikit.common.util.log.LogUtil;
import com.netease.nim.uikit.common.util.string.MD5;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.auth.LoginInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import cz.msebera.android.httpclient.Header;

import static com.imfan.j.a91fan.netease.NeteaseClient.addHeaders;
import static com.imfan.j.a91fan.netease.NeteaseClient.checkSum;
import static com.imfan.j.a91fan.netease.NeteaseClient.curTime;
import static com.imfan.j.a91fan.netease.NeteaseClient.nonce;
import static com.imfan.j.a91fan.util.Constant.NetEaseAPP_KEY;
import static com.imfan.j.a91fan.util.Constant.NetEaseAPP_SECRET;


/**
 * Created by jay on 17-2-8.
 */

public class LoginNetease {

    private static final String TAG = Class.class.getSimpleName();
    private static LoginNetease loginNetease;


    public static LoginNetease getInstance(){
        if (loginNetease == null){
            loginNetease = new LoginNetease();
        }
        return loginNetease;
    }



    public  void registerNetease(final Context context){
        curTime = String.valueOf((new Date()).getTime() / 1000L);
        checkSum = CheckSumBuilder.getCheckSum(NetEaseAPP_SECRET, nonce, curTime);

        RequestParams params = new RequestParams();
        params.add("accid", Preferences.getUserAccount());
        params.add("name", Preferences.getWxNickname());
        // params.add("token", "1"); 由网易自动生成
        addHeaders();

        NeteaseClient.post("user/create.action", params, new JsonHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
                LogUtil.i("Register：", "网易开始注册");

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject object) {
                // called when response HTTP status is "200 OK"
                LogUtil.i("Register：", "网易通信成功" + statusCode);
                try {
                    // 这里重复注册同一个id会使得code变为414
                    int code = object.getInt("code");
                    LogUtil.i("网易注册返回JSON：", "Json" + code);
                    if (code == 414) { // 说明我们只需要去更新我们的token就可以了
                        refreshNeteaseToken(context);
                    } else {
                        // 解析json数据
                        JSONObject jsonObject = object.getJSONObject("info");

                        String token = jsonObject.getString("token");
                        Preferences.setToken(token);
                        LogUtil.i("注册页网易口令：", token);

                        String accid = jsonObject.getString("accid");

                        Preferences.setWxUnionid(accid);

                        String name = jsonObject.getString("name");
                        Preferences.setWxNickname(name);

                        LogUtil.i("微信注册页账户:", accid);
                        LogUtil.i("微信注册页昵称:", name);
                        login(context);
                        // 初始化消息提醒配置
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                LogUtil.i("Register", "通信失败");
            }


            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });

    }



    /* **
        刷新网易的token
     */
    public void refreshNeteaseToken(final Context context) {
        curTime = String.valueOf((new Date()).getTime() / 1000L);
        checkSum = CheckSumBuilder.getCheckSum(NetEaseAPP_SECRET, nonce, curTime);
        RequestParams params = new RequestParams();
        LogUtil.i("ACCID:", Preferences.getUserAccount());
        params.add("accid", Preferences.getUserAccount().toLowerCase());
        addHeaders();

        NeteaseClient.post("user/refreshToken.action", params, new JsonHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
                LogUtil.i("refreshNeteaseToken()", "开始刷新网易token");

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject object) {
                // called when response HTTP status is "200 OK"
                LogUtil.i("refreshNeteaseToken()", "通信成功" + statusCode);
                try {

                    int code = object.getInt("code");
                    LogUtil.i("refreshNeteaseToken()", "Json:" + code);

                    // 解析json数据
                    JSONObject jsonObject = object.getJSONObject("info");

                    String token = jsonObject.getString("token");
                    Preferences.setToken(token);
                    LogUtil.i("refreshNeteaseToken()", "新的Token:" + token);
                    huanxinLoginAndRegister();
                    login(context);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                LogUtil.i("Register", "通信失败");
            }


            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });

    }

    public void login(final Context context) {

        // 登录
        NimUIKit.doLogin(new LoginInfo(Preferences.getUserAccount().toLowerCase(), Preferences.getNeteaseToken()), new RequestCallback<LoginInfo>() {
            @Override
            public void onSuccess(LoginInfo param) {
                LogUtil.i(TAG, "login success网易登录成功");
                initNotificationConfig();
                // 进入主界面
                MainActivity.start(context, null);
            }

            @Override
            public void onFailed(int code) {
                if (code == 302 || code == 404) {
                    LogUtil.e(TAG + "网易账号与密码：", Preferences.getUserAccount() + "    " + Preferences.getNeteaseToken());
                } else {
                    CustomToast.show(context, "网易登录失败: " + code);
                }
            }

            @Override
            public void onException(Throwable exception) {
                CustomToast.show(context, "网易无效输入");
            }
        });
    }

    private  void initNotificationConfig() {
        // 初始化消息提醒
        NIMClient.toggleNotification(UserPreferences.getNotificationToggle());

        // 加载状态栏配置
        StatusBarNotificationConfig statusBarNotificationConfig = UserPreferences.getStatusConfig();
        if (statusBarNotificationConfig == null) {
            statusBarNotificationConfig = Cache.getNotificationConfig();
            UserPreferences.setStatusConfig(statusBarNotificationConfig);
        }
        // 更新配置
        NIMClient.updateStatusBarNotificationConfig(statusBarNotificationConfig);
    }

    private void huanxinLoginAndRegister(){
        LogUtil.i("环信创建账户：", Preferences.getUserAccount().toLowerCase());
        Thread thread = new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    LogUtil.i("环信创建账户：", Preferences.getUserAccount().toLowerCase());
                    LogUtil.i("环信创建密码：", MD5.getStringMD5(Preferences.getUserAccount().toLowerCase()));
                    EMClient.getInstance().createAccount(Preferences.getUserAccount().toLowerCase(), MD5.getStringMD5(Preferences.getUserAccount().toLowerCase()));//同步方法
                } catch (HyphenateException e) {
                    if (e.getErrorCode() == EMError.USER_ALREADY_EXIST)
                        Log.d("TAG", "环信已经注册了");
                    else {
                        Log.d("TAG", "环信注册失败208错误" + e.getErrorCode() + "  " + e.getDescription());
                    }
                }

            }
        };

        thread.start();
    }


}
