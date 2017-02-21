package com.imfan.j.a91fan.wxapi;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.imfan.j.a91fan.R;
import com.imfan.j.a91fan.netease.LoginNetease;
import com.imfan.j.a91fan.util.Cache;
import com.imfan.j.a91fan.util.CustomToast;
import com.imfan.j.a91fan.util.Preferences;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import com.netease.nim.uikit.common.activity.UI;
import com.netease.nim.uikit.common.util.log.LogUtil;
import com.netease.nim.uikit.common.util.sys.NetworkUtil;
import com.netease.nim.uikit.permission.MPermission;
import com.netease.nim.uikit.permission.annotation.OnMPermissionDenied;
import com.netease.nim.uikit.permission.annotation.OnMPermissionGranted;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;
import cz.msebera.android.httpclient.Header;

import static com.imfan.j.a91fan.MainApplication.api;
import static com.imfan.j.a91fan.util.Constant.WX_APP_ID;
import static com.imfan.j.a91fan.util.Constant.accessTokenUrl1;
import static com.imfan.j.a91fan.util.Constant.accessTokenUrl2;
import static com.imfan.j.a91fan.util.Constant.refresh_tokenUrl;

/**
 * OpenID只有一个，无论什么时候获取都不会变
 */


public class WXEntryActivity extends UI implements IWXAPIEventHandler {


    private static final String TAG = "WXEntryActivity";

    // 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
    private static String code, lang, country;
    private final int BASIC_PERMISSION_REQUEST_CODE = 110;
    private ImageButton imageButton;
    private String accessTokenUrl;
    private String access_token;
    private int expires_in;
    private String refresh_token;
    private String openid;
    private String scope;
    private String nickname;


    private int sex; // 普通用户性别，1为男性，2为女性
    private String province;
    private String city;
    private String headimgurl;
    private JSONArray privilege; //用户特权信息，json数组，如微信沃卡用户为（chinaunicom,暂不保存
    private String unionid;
private int i = -1;

    // 这两个start函数是启动这个activity的函数
    public static void start(Context context) {
        start(context, false);
    }

    public static void start(Context context, boolean kickOut) {
        Intent intent = new Intent(context, WXEntryActivity.class);
        // 这个Flags的功能详见Keep中的链接
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("KICK_OUT", kickOut);  // kickout 账号在其他设备登陆
        context.startActivity(intent); // 起动微信登陆界面
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); // 去掉状态栏
        setContentView(R.layout.activity_wxentry);

        requestBasicPermission();

        //注册微信API
        api = WXAPIFactory.createWXAPI(this, WX_APP_ID);
        api.handleIntent(getIntent(), this);


        imageButton = (ImageButton) findViewById(R.id.wxlogin);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 发起微信登陆
                final SweetAlertDialog pDialog = new SweetAlertDialog(WXEntryActivity.this, SweetAlertDialog.PROGRESS_TYPE)
                        .setTitleText("Loading");
                pDialog.show();
                pDialog.setCancelable(false);


                new CountDownTimer(200 * 7, 200) {
                    public void onTick(long millisUntilFinished) {

                        // you can change the progress bar color by ProgressHelper every 800 millis
                        i++;
                        switch (i){
                            case 0:
                                pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.blue_btn_bg_color));
                                break;
                            case 1:
                                pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.material_deep_teal_50));
                                break;
                            case 2:
                                pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.success_stroke_color));
                                break;
                            case 3:
                                pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.material_deep_teal_20));
                                break;
                            case 4:
                                pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.material_blue_grey_80));
                                break;
                            case 5:
                                pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.warning_stroke_color));
                                break;
                            case 6:
                                pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.success_stroke_color));
                                break;
                        }
                    }

                    public void onFinish() {
                        i = -1;
                        pDialog.dismiss();
                    }
                }.start();
                sendAuthRequest();
            }
        });


    }

    // 第一步，请求CODE
    private void sendAuthRequest() {
        if (!NetworkUtil.isNetAvailable(this)) {
            CustomToast.show(this, R.string.network_is_not_available);
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
                    LogUtil.e(TAG, "获取access_token中的OpenID:" + openid);

                    scope = response.getString("scope");
                    Preferences.setWxScope(scope);

                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            LogUtil.e(TAG, "获取微信的access_token成功");
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
                LogUtil.e(TAG, "获取token失败");
                CustomToast.show(WXEntryActivity.this, "invalid code");
            }
        });
    }

    private void getUserInfo() {

   /* 第三步获取微信用户个人信息（UnionID机制）
    接口说明
    此接口用于获取用户个人信息。开发者可通过OpenID来获取用户基本信息。
    特别需要注意的是，如果开发者拥有多个移动应用、网站应用和公众帐号，
    可通过获取用户基本信息中的unionid来区分用户的唯一性，因为只要是
    同一个微信开放平台帐号下的移动应用、网站应用和公众帐号，用户的
    unionid是唯一的。换句话说，同一用户，对同一个微信开放平台下的
    不同应用，unionid是相同的。请注意，在用户修改微信头像后，旧的微
    信头像URL将会失效，因此开发者应该自己在获取用户信息后，将头像图片
    保存下来，避免微信头像URL失效后的异常情况。*/
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

                    Cache.setAccount(unionid.toLowerCase());



                    LoginNetease.getInstance().registerNetease(WXEntryActivity.this);




                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                try {
                    CustomToast.show(WXEntryActivity.this, errorResponse.getString("errmsg"));
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

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onResp(BaseResp resp) {
        int result = 0;
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:  // 用户同意
                SendAuth.Resp newResp = (SendAuth.Resp) resp;
                result = R.string.errcode_success;
                LogUtil.i(TAG, "返回码确认");
                // imageButton.setVisibility(View.GONE);
                code = newResp.code;
                Preferences.setWxCode(code);
                accessTokenUrl = accessTokenUrl1 + code + accessTokenUrl2;
                lang = newResp.lang;
                Preferences.setWxLanguage(lang);
                country = newResp.country;
                Preferences.setWxCountry(country);
                //  getCodeIsOK = true;  // 正确得到code，为下一步做好判别工作，只有这一步成功才可以进行下一步

                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        getAccessToken(); // 获取微信token
                    }
                };

                new Handler().postDelayed(runnable, 500);


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


        CustomToast.show(WXEntryActivity.this, result);
    }

    private void refresh_token() {
        // 刷新或续期微信access_token
        //接口说明
   /* access_token是调用授权关系接口的调用凭证，由于access_token有效期（目前为2个小时）较短，当access_token超时后，可以使用refresh_token进行刷新，access_token刷新结果有两种：
            1.若access_token已超时，那么进行refresh_token会获取一个新的access_token，新的超时时间；
            2.若access_token未超时，那么进行refresh_token不会改变access_token，但超时时间会刷新，相当于续期access_token。
    refresh_token拥有较长的有效期（30天），当refresh_token失效的后，需要用户重新授权，所以，请开发者在refresh_token即将过期时（如第29天时），进行定时的自动刷新并保存好它。
    */
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
                    LogUtil.e(TAG, "刷新token后的OpenID：" + openid);
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
                    CustomToast.show(WXEntryActivity.this, errorResponse.getString("errmsg"));
                    LogUtil.i(TAG, "刷新token失败，错误码：" + errorResponse.getString("errcode"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void checkAccess_token() {
        // 检验微信access_token的有效性
        String url = "https://api.weixin.qq.com/sns/auth?access_token=" + access_token + " &openid=" + openid;
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // "errcode":0,"errmsg":"ok"
                try {

                    CustomToast.show(WXEntryActivity.this, response.getString("errmsg"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                try {
                    CustomToast.show(WXEntryActivity.this, errorResponse.getString("errmsg"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }


    /**
     * 基本权限管理
     */
    private void requestBasicPermission() {
        MPermission.with(WXEntryActivity.this)
                .addRequestCode(BASIC_PERMISSION_REQUEST_CODE)
                .permissions(
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        android.Manifest.permission.CAMERA,
                        android.Manifest.permission.READ_PHONE_STATE,
                        android.Manifest.permission.RECORD_AUDIO,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                )
                .request();

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        MPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @OnMPermissionGranted(BASIC_PERMISSION_REQUEST_CODE)
    public void onBasicPermissionSuccess(){
        CustomToast.show(WXEntryActivity.this, "授权成功");

    }

    @OnMPermissionDenied(BASIC_PERMISSION_REQUEST_CODE)
    public void onBasicPermissionFailed(){
        CustomToast.show(WXEntryActivity.this, "授权失败");
    }




}


