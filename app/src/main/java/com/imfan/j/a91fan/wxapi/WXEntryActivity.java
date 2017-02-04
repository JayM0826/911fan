package com.imfan.j.a91fan.wxapi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

import com.imfan.j.a91fan.R;
import com.imfan.j.a91fan.main.MainActivity;
import com.imfan.j.a91fan.netease.CheckSumBuilder;
import com.imfan.j.a91fan.netease.NeteaseClient;
import com.imfan.j.a91fan.util.Preferences;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.netease.nim.uikit.NimUIKit;
import com.netease.nim.uikit.common.util.log.LogUtil;
import com.netease.nim.uikit.common.util.sys.NetworkUtil;
import com.netease.nimlib.sdk.AbortableFuture;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import cz.msebera.android.httpclient.Header;

import static com.imfan.j.a91fan.MainApplication.api;
import static com.imfan.j.a91fan.util.Constant.NetEaseAPP_KEY;
import static com.imfan.j.a91fan.util.Constant.NetEaseAPP_SECRET;
import static com.imfan.j.a91fan.util.Constant.WX_APP_ID;
import static com.imfan.j.a91fan.util.Constant.accessTokenUrl1;
import static com.imfan.j.a91fan.util.Constant.accessTokenUrl2;
import static com.imfan.j.a91fan.util.Constant.refresh_tokenUrl;

/**
 * OpenID只有一个，无论什么时候获取都不会变
 */


public class WXEntryActivity extends Activity implements IWXAPIEventHandler {


    private static final String TAG = "WXEntryActivity";
    static String nonce = "123456";
    static String curTime = null;
    static String checkSum = null;
    // 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
    private static String code, lang, country;
    private ImageButton imageButton;

    private String accessTokenUrl;
    private String access_token;
    private int expires_in;
    private String refresh_token;
    private String openid;
    private String scope;
    private String nickname;


    // 第三步获取用户个人信息（UnionID机制）
    /*接口说明
    此接口用于获取用户个人信息。开发者可通过OpenID来获取用户基本信息。
    特别需要注意的是，如果开发者拥有多个移动应用、网站应用和公众帐号，
    可通过获取用户基本信息中的unionid来区分用户的唯一性，因为只要是
    同一个微信开放平台帐号下的移动应用、网站应用和公众帐号，用户的
    unionid是唯一的。换句话说，同一用户，对同一个微信开放平台下的
    不同应用，unionid是相同的。请注意，在用户修改微信头像后，旧的微
    信头像URL将会失效，因此开发者应该自己在获取用户信息后，将头像图片
    保存下来，避免微信头像URL失效后的异常情况。*/
    private int sex; // 普通用户性别，1为男性，2为女性
    private String province;
    private String city;
    private String headimgurl;
    private JSONArray privilege; //用户特权信息，json数组，如微信沃卡用户为（chinaunicom,暂不保存
    private String unionid;
    // 检验授权凭证（access_token）是否有效
    private boolean access_tokenIsOK;
    // 第三步获取用户个人信息（UnionID机制）
    /*接口说明
    此接口用于获取用户个人信息。开发者可通过OpenID来获取用户基本信息。
    特别需要注意的是，如果开发者拥有多个移动应用、网站应用和公众帐号，
    可通过获取用户基本信息中的unionid来区分用户的唯一性，因为只要是
    同一个微信开放平台帐号下的移动应用、网站应用和公众帐号，用户的
    unionid是唯一的。换句话说，同一用户，对同一个微信开放平台下的
    不同应用，unionid是相同的。请注意，在用户修改微信头像后，旧的微
    信头像URL将会失效，因此开发者应该自己在获取用户信息后，将头像图片
    保存下来，避免微信头像URL失效后的异常情况。*/
    private AbortableFuture<LoginInfo> loginRequest;

    // 这两个start函数是启动这个activity的函数
    public static void start(Context context) {
        start(context, false);
    }

    public static void start(Context context, boolean kickOut) {
        Intent intent = new Intent(context, WXEntryActivity.class);
        // 这个Flags的功能详见Keep中的链接
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("KICK_OUT", kickOut);  // kickout 账号在其他设备登陆
        context.startActivity(intent); // 起动微信登陆界面
    }


    // 刷新或续期access_token
    //接口说明
    /*access_token是调用授权关系接口的调用凭证，由于access_token有效期（目前为2个小时）较短，当access_token超时后，可以使用refresh_token进行刷新，access_token刷新结果有两种：
            1.若access_token已超时，那么进行refresh_token会获取一个新的access_token，新的超时时间；
            2.若access_token未超时，那么进行refresh_token不会改变access_token，但超时时间会刷新，相当于续期access_token。
    refresh_token拥有较长的有效期（30天），当refresh_token失效的后，需要用户重新授权，所以，请开发者在refresh_token即将过期时（如第29天时），进行定时的自动刷新并保存好它。
}*/

    public static void addHeaders() {
        NeteaseClient.client.removeAllHeaders();
        NeteaseClient.client.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        NeteaseClient.client.addHeader("AppKey", NetEaseAPP_KEY);
        NeteaseClient.client.addHeader("Nonce", nonce);
        NeteaseClient.client.addHeader("CurTime", curTime);
        NeteaseClient.client.addHeader("CheckSum", checkSum);
    }

    /**
     * 刷新网易的token
     */
    static public void refreshNeteaseToken() {
        curTime = String.valueOf((new Date()).getTime() / 1000L);
        checkSum = CheckSumBuilder.getCheckSum(NetEaseAPP_SECRET, nonce, curTime);


        RequestParams params = new RequestParams();
        Log.i("ACCID:", Preferences.getUserAccount());
        params.add("accid", Preferences.getUserAccount().toLowerCase());
        Log.i("ACCID:", Preferences.getUserAccount());
        addHeaders();

        NeteaseClient.post("refreshToken.action", params, new JsonHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
                Log.i("refreshNeteaseToken()", "开始刷新网易token");

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject object) {
                // called when response HTTP status is "200 OK"
                Log.i("refreshNeteaseToken()", "通信成功" + statusCode);
                try {

                    int code = object.getInt("code");
                    Log.i("refreshNeteaseToken()", "Json:" + code);

                    // 解析json数据
                    JSONObject jsonObject = object.getJSONObject("info");

                    String token = jsonObject.getString("token");
                    Preferences.setToken(token);
                    Log.i("refreshNeteaseToken()", "新的Token:" + token);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.i("Register", "通信失败");
            }


            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });

    }

    /**
     * 注册网易
     */
    static public void doNetEaseRegister() {


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
                Log.i("Register", "开始注册");

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject object) {
                // called when response HTTP status is "200 OK"
                Log.i("Register", "通信成功" + statusCode);
                try {
                    // 这里重复注册同一个id会使得code变为414
                    int code = object.getInt("code");
                    Log.i("Register", "Json" + code);
                    if (code == 414) { // 说明我们只需要去更新我们的token就可以了
                        refreshNeteaseToken();
                    } else {
                        // 解析json数据
                        JSONObject jsonObject = object.getJSONObject("info");

                        String token = jsonObject.getString("token");
                        Preferences.setToken(token);
                        Log.i("TOKEN", token);

                        String accid = jsonObject.getString("accid");

                        Preferences.setWxUnionid(accid);
                        String name = jsonObject.getString("name");
                        Preferences.setWxNickname(name);

                        Log.i("accid", accid);
                        Log.i("name", name);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.i("Register", "通信失败");
            }


            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); // 去掉状态栏
        setContentView(R.layout.activity_wxentry);

        //注册微信API
        api = WXAPIFactory.createWXAPI(this, WX_APP_ID);
        api.handleIntent(getIntent(), this);


        imageButton = (ImageButton) findViewById(R.id.wxlogin);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 发起微信登陆
                sendAuthRequest();
            }
        });


    }

    // 第一步，请求CODE
    private void sendAuthRequest() {
        if (!NetworkUtil.isNetAvailable(this)) {
            Toast.makeText(this, R.string.network_is_not_available, Toast.LENGTH_SHORT).show();
            return;
        }

        // send oauth request
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "wechat_sdk_demo_test";
        api.sendReq(req);

    }

    // 第二步：通过code获取access_token
    private void getAccessToken() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(accessTokenUrl, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    access_token = response.getString("access_token");
                    Preferences.setWxAccessToken(access_token);


                    expires_in = response.getInt("expires_in");
                    Preferences.setWxExpiresIn(expires_in);

                    refresh_token = response.getString("refresh_token");
                    Preferences.setWxRefreshToken(refresh_token);

                    openid = response.getString("openid");
                    Preferences.setWxOpenid(openid);
                    Log.e(TAG, "获取access_token中的OpenID:" + openid);

                    scope = response.getString("scope");
                    Preferences.setWxScope(scope);

                    while (access_token.equals(null)) {
                        // 空语句，只要没有获取到access_token就等待
                        Log.e(TAG, "正在获取微信的access_token中");
                    }
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            Log.e(TAG, "获取微信的access_token成功");
                            getUserInfo();
                        }
                    };

                    new Handler().postDelayed(runnable, 500);

                    // 下一步将获取用户个人信息（UnionID机制），这里由于是异步的，所以要注意只有access_token成功，才可以获取个人信息


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i(TAG, "获取token失败");
                Toast.makeText(WXEntryActivity.this, "invalid code", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void login() {

        // 云信只提供消息通道，并不包含用户资料逻辑。
        // 开发者需要在管理后台或通过服务器接口将用户帐号和token同步到云信服务器。
        // 在这里直接使用同步到云信服务器的帐号和token登录。

        // 登录
        loginRequest = NimUIKit.doLogin(new LoginInfo(Preferences.getUserAccount(), Preferences.getNeteaseToken()), new RequestCallback<LoginInfo>() {
            @Override
            public void onSuccess(LoginInfo param) {
                LogUtil.i(TAG, "login success");
                Toast.makeText(WXEntryActivity.this, "登录非常成功", Toast.LENGTH_SHORT).show();
                loginRequest = null;
                // 初始化消息提醒配置
                // initNotificationConfig();
                // 进入主界面
                MainActivity.start(WXEntryActivity.this, null);
                finish();
            }

            @Override
            public void onFailed(int code) {
                loginRequest = null;
                if (code == 302 || code == 404) {
                    Log.e(TAG + "账号与密码：", Preferences.getUserAccount() + "    " + Preferences.getNeteaseToken());

                    Toast.makeText(WXEntryActivity.this, "账号或密码错误" + code, Toast.LENGTH_SHORT).show();
                    MainActivity.start(WXEntryActivity.this, null);
                    finish();
                } else {
                    Toast.makeText(WXEntryActivity.this, "登录失败: " + code, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onException(Throwable exception) {
                Toast.makeText(WXEntryActivity.this, "无效输入", Toast.LENGTH_LONG).show();
                loginRequest = null;
            }
        });
    }

    private void getUserInfo() {


        String userInfoUrl = "https://api.weixin.qq.com/sns/userinfo?access_token=" + access_token + "&openid=" + openid;
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(userInfoUrl, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {

                    nickname = response.getString("nickname");
                    Preferences.setWxNickname(nickname);

                    sex = response.getInt("sex");
                    Preferences.setWxSex(sex);

                    province = response.getString("province");
                    Preferences.setWxProvince(province);

                    city = response.getString("city");
                    Preferences.setWxCity(city);

                    headimgurl = response.getString("headimgurl");
                    Preferences.setWxHeadimgurl(headimgurl);

                    privilege = response.getJSONArray("privilege");


                    unionid = response.getString("unionid");
                    Preferences.setWxUnionid(unionid);
                    Log.i("UNIONID", unionid);
                    Log.i("UNIONID+1", Preferences.getUserAccount());

                    Toast.makeText(WXEntryActivity.this, "获取个人信息成功" + nickname, Toast.LENGTH_SHORT).show();
                    // CustomeActivityManager.getCustomeActivityManager().popAllActivity();

                    doNetEaseRegister();
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            login();
                        }
                    };
                    new Handler().postDelayed(runnable, 500);

                    finish();


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                try {
                    Toast.makeText(WXEntryActivity.this, errorResponse.getString("errmsg"), Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    // 刷新或续期access_token
    //接口说明
    /*access_token是调用授权关系接口的调用凭证，由于access_token有效期（目前为2个小时）较短，当access_token超时后，可以使用refresh_token进行刷新，access_token刷新结果有两种：
            1.若access_token已超时，那么进行refresh_token会获取一个新的access_token，新的超时时间；
            2.若access_token未超时，那么进行refresh_token不会改变access_token，但超时时间会刷新，相当于续期access_token。
    refresh_token拥有较长的有效期（30天），当refresh_token失效的后，需要用户重新授权，所以，请开发者在refresh_token即将过期时（如第29天时），进行定时的自动刷新并保存好它。
}*/

    // 微信发送请求到第三方应用时，会回调到该方法
    @Override
    public void onReq(BaseReq req) {
        switch (req.getType()) {
            case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
                // goToGetMsg();
                break;
            case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
                // goToShowMsg((ShowMessageFromWX.Req) req);
                break;
            default:
                break;
        }
    }

    @Override
    public void onResp(BaseResp resp) {
        int result = 0;
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:  // 用户同意
                SendAuth.Resp newResp = (SendAuth.Resp) resp;
                result = R.string.errcode_success;
                Log.i(TAG, "返回码确认");
                code = newResp.code;
                Preferences.setWxCode(code);
                accessTokenUrl = accessTokenUrl1 + code + accessTokenUrl2;
                lang = newResp.lang;
                Preferences.setWxLanguage(lang);
                country = newResp.country;
                Preferences.setWxCountry(country);
                //  getCodeIsOK = true;  // 正确得到code，为下一步做好判别工作，只有这一步成功才可以进行下一步
                while (code.equals(null)) {
                    // 空语句，只要没有获取到access_token就等待
                    Log.e(TAG, "正在努力的获取微信的code中...");
                }
                getAccessToken(); // 获取微信token
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:  // 用户取消
                result = R.string.errcode_cancel;

                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:  // 用户拒绝授权
                //  result = R.string.errcode_deny;
                break;
            default:
                result = R.string.errcode_unknown;
                break;
        }


        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
    }

    private void refresh_token() {

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(refresh_tokenUrl + refresh_token, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    access_token = response.getString("access_token");
                    Preferences.setWxAccessToken(access_token);

                    expires_in = response.getInt("expires_in");
                    Preferences.setWxExpiresIn(expires_in);

                    refresh_token = response.getString("refresh_token");
                    Preferences.setWxRefreshToken(refresh_token);

                    openid = response.getString("openid");
                    Log.e(TAG, "刷新token后的OpenID：" + openid);
                    Preferences.setWxOpenid(openid);

                    scope = response.getString("scope");
                    Preferences.setWxScope(scope);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                try {
                    Toast.makeText(WXEntryActivity.this, errorResponse.getString("errmsg"), Toast.LENGTH_LONG).show();
                    Log.i(TAG, "刷新token失败，错误码：" + errorResponse.getString("errcode"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void checkAccess_token() {
        String url = "https://api.weixin.qq.com/sns/auth?access_token=" + access_token + " &openid=" + openid;
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // "errcode":0,"errmsg":"ok"
                try {
                    access_tokenIsOK = true;
                    Toast.makeText(WXEntryActivity.this, response.getString("errmsg"), Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                try {
                    Toast.makeText(WXEntryActivity.this, errorResponse.getString("errmsg"), Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

}


