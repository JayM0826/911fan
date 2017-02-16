package com.imfan.j.a91fan.netease;

import android.content.Context;
import android.widget.Toast;

import com.alibaba.fastjson.serializer.ClassSerializer;
import com.imfan.j.a91fan.config.UserPreferences;
import com.imfan.j.a91fan.main.activity.MainActivity;
import com.imfan.j.a91fan.netease.CheckSumBuilder;
import com.imfan.j.a91fan.netease.NeteaseClient;
import com.imfan.j.a91fan.util.Cache;
import com.imfan.j.a91fan.util.CustomToast;
import com.imfan.j.a91fan.util.Preferences;
import com.imfan.j.a91fan.wxapi.WXEntryActivity;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.netease.nim.uikit.NimUIKit;
import com.netease.nim.uikit.common.util.log.LogUtil;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.auth.LoginInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import cz.msebera.android.httpclient.Header;

import static com.imfan.j.a91fan.util.Constant.NetEaseAPP_KEY;
import static com.imfan.j.a91fan.util.Constant.NetEaseAPP_SECRET;


/**
 * Created by jay on 17-2-8.
 */

public class LoginNetease {

    private static final String TAG = Class.class.getSimpleName();
    static private String nonce = "123456"; // 当做随机数使用
    static private String curTime = null;
    static private String checkSum = null;

    public static void registerNetease(){
        curTime = String.valueOf((new Date()).getTime() / 1000L);
        checkSum = CheckSumBuilder.getCheckSum(NetEaseAPP_SECRET, nonce, curTime);

        RequestParams params = new RequestParams();
        params.add("accid", Preferences.getUserAccount().toLowerCase());
        params.add("name", Preferences.getWxNickname());
        // params.add("token", "1"); 由网易自动生成
        addHeaders();

        NeteaseClient.post("create.action", params, new JsonHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
                LogUtil.i("Register：", "开始注册");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject object) {
                // called when response HTTP status is "200 OK"
                LogUtil.i("Register：", "通信成功" + statusCode);
                try {
                    // 这里重复注册同一个id会使得code变为414
                    int code = object.getInt("code");
                    LogUtil.i("注册返回JSON：", "Json" + code);
                    if (code == 414) { // 说明我们只需要去更新我们的token就可以了
                        refreshNeteaseToken();
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

    private  static void addHeaders() {
        NeteaseClient.client.removeAllHeaders();
        NeteaseClient.client.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        NeteaseClient.client.addHeader("AppKey", NetEaseAPP_KEY);
        NeteaseClient.client.addHeader("Nonce", nonce);
        NeteaseClient.client.addHeader("CurTime", curTime);
        NeteaseClient.client.addHeader("CheckSum", checkSum);
    }


   /* **
       刷新网易的token
    */
    static public void refreshNeteaseToken() {
        curTime = String.valueOf((new Date()).getTime() / 1000L);
        checkSum = CheckSumBuilder.getCheckSum(NetEaseAPP_SECRET, nonce, curTime);


        RequestParams params = new RequestParams();
        LogUtil.i("ACCID:", Preferences.getUserAccount());
        params.add("accid", Preferences.getUserAccount().toLowerCase());
        LogUtil.i("ACCID:", Preferences.getUserAccount());
        addHeaders();

        NeteaseClient.post("refreshToken.action", params, new JsonHttpResponseHandler() {

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

    public  static void login(final Context context) {

        // 云信只提供消息通道，并不包含用户资料逻辑。
        // 开发者需要在管理后台或通过服务器接口将用户帐号和token同步到云信服务器。
        // 在这里直接使用同步到云信服务器的帐号和token登录。
        LogUtil.i("账户Account:", Preferences.getUserAccount());
        LogUtil.i("密码Password", Preferences.getNeteaseToken());

        // 登录
        NimUIKit.doLogin(new LoginInfo(Preferences.getUserAccount().toLowerCase(), Preferences.getNeteaseToken()), new RequestCallback<LoginInfo>() {
            @Override
            public void onSuccess(LoginInfo param) {
                LogUtil.i(TAG, "login success登录成功");
                // CustomToast.show(context, "登录非常成功");
            }

            @Override
            public void onFailed(int code) {
                if (code == 302 || code == 404) {
                    LogUtil.e(TAG + "账号与密码：", Preferences.getUserAccount() + "    " + Preferences.getNeteaseToken());


                } else {
                    CustomToast.show(context, "登录失败: " + code);
                }
            }

            @Override
            public void onException(Throwable exception) {
                CustomToast.show(context, "无效输入");
            }
        });
    }


}
