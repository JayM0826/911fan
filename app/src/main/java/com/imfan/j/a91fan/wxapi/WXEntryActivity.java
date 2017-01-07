package com.imfan.j.a91fan.wxapi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.imfan.j.a91fan.R;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import static com.imfan.j.a91fan.Constant.APP_ID;
import static com.imfan.j.a91fan.Constant.accessTokenUrl1;
import static com.imfan.j.a91fan.Constant.accessTokenUrl2;
import static com.imfan.j.a91fan.Constant.refresh_tokenUrl;
import static com.imfan.j.a91fan.MainApplication.api;


public class WXEntryActivity extends Activity implements IWXAPIEventHandler {


    private static final String TAG = "WXEntryActivity";

    public static void start(Context context) {
        start(context, false);
    }

    public static void start(Context context, boolean kickOut) {
        Intent intent = new Intent(context, WXEntryActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("KICK_OUT", kickOut);  // kickout 账号在其他设备登陆
        context.startActivity(intent); // 起动微信登陆界面
    }

    private ImageButton imageButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); // 去掉状态栏
        setContentView(R.layout.activity_wxentry);

        //注册API
        api = WXAPIFactory.createWXAPI(this, APP_ID);
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
        // Toast.makeText(this, "request Code", Toast.LENGTH_LONG).show();
        // send oauth request
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "wechat_sdk_demo_test";
        api.sendReq(req);

    }

    // 第二步：通过code获取access_token
    private String accessTokenUrl;
    private String access_token;
    private int expires_in;
    private String refresh_token;
    private String openid;
    private String scope;


    private void getAccessToken() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(accessTokenUrl, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    access_token = response.getString("access_token");
                    expires_in = response.getInt("expires_in");
                    refresh_token = response.getString("refresh_token");
                    openid = response.getString("openid");
                    scope = response.getString("scope");

                    Log.i(TAG, "获取token成功");
                    Log.i("Token", access_token);
                    while (access_token.equals(null)) {
                        // 空语句，只要没有获取到access_token就等待
                    }


                    // 下一步将获取用户个人信息（UnionID机制），这里由于是异步的，所以要注意只有access_token成功，才可以获取个人信息
                    getUserInfo();

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


    // 第三步获取用户个人信息（UnionID机制）
    private String nickname;
    private int sex;
    private String province;
    private String city;
    private String headimgurl;
    private JSONArray privilege;
    private String unionid;

    private void getUserInfo() {
        String userInfoUrl = "https://api.weixin.qq.com/sns/userinfo?access_token=" + access_token + "&openid=" + openid;
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(userInfoUrl, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    openid = response.getString("openid");
                    nickname = response.getString("nickname");
                    sex = response.getInt("sex");
                    province = response.getString("province");
                    city = response.getString("city");
                    headimgurl = response.getString("headimgurl");
                    privilege = response.getJSONArray("privilege");
                    unionid = response.getString("unionid");
                    Log.i("UNIONID", unionid);
                    Log.i("OPENID", openid);
                    // Toast.makeText(WXEntryActivity.this, "获取个人信息成功" + nickname, Toast.LENGTH_LONG).show();
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

    // 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法

    private static String code, lang, country;

    public static String getCode() {
        return code;
    }


    public static String getLang() {
        return lang;
    }


    public static String getCountry() {
        return country;
    }


    public static void setLang(String lang) {
        WXEntryActivity.lang = lang;
    }

    public static void setCode(String code) {
        WXEntryActivity.code = code;
    }

    public static void setCountry(String country) {
        WXEntryActivity.country = country;
    }

    private static boolean getCodeIsOK = false;

    @Override
    public void onResp(BaseResp resp) {
        int result = 0;
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:  // 用户同意
                SendAuth.Resp newResp = (SendAuth.Resp) resp;
                result = R.string.errcode_success;
                Log.i(TAG, "返回码确认");
                code = newResp.code;
                accessTokenUrl = accessTokenUrl1 + code + accessTokenUrl2;
                lang = newResp.lang;
                country = newResp.country;
                //  getCodeIsOK = true;  // 正确得到code，为下一步做好判别工作，只有这一步成功才可以进行下一步
                while (code.equals(null)) {
                    // 空语句，只要没有获取到access_token就等待
                }
                getAccessToken(); // 获取token
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

    // 刷新或续期access_token
    //接口说明
    /*access_token是调用授权关系接口的调用凭证，由于access_token有效期（目前为2个小时）较短，当access_token超时后，可以使用refresh_token进行刷新，access_token刷新结果有两种：
            1.若access_token已超时，那么进行refresh_token会获取一个新的access_token，新的超时时间；
            2.若access_token未超时，那么进行refresh_token不会改变access_token，但超时时间会刷新，相当于续期access_token。
    refresh_token拥有较长的有效期（30天），当refresh_token失效的后，需要用户重新授权，所以，请开发者在refresh_token即将过期时（如第29天时），进行定时的自动刷新并保存好它。
}*/

    private void refresh_token() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(refresh_tokenUrl + refresh_token, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    access_token = response.getString("access_token");
                    expires_in = response.getInt("expires_in");
                    refresh_token = response.getString("refresh_token");
                    openid = response.getString("openid");
                    scope = response.getString("scope");
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

    // 检验授权凭证（access_token）是否有效
    private boolean access_tokenIsOK;

    private void checkAccess_token(){
        String url = "https://api.weixin.qq.com/sns/auth?access_token=" + access_token + " &openid=" + openid;
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new JsonHttpResponseHandler(){
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


