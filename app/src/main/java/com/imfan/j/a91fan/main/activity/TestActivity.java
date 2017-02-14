package com.imfan.j.a91fan.main.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.imfan.j.a91fan.R;
import com.imfan.j.a91fan.netease.CheckSumBuilder;
import com.imfan.j.a91fan.netease.NeteaseClient;
import com.imfan.j.a91fan.util.CustomToast;
import com.imfan.j.a91fan.util.Preferences;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.netease.nim.uikit.common.util.log.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import cz.msebera.android.httpclient.Header;

import static com.imfan.j.a91fan.util.Constant.NetEaseAPP_KEY;
import static com.imfan.j.a91fan.util.Constant.NetEaseAPP_SECRET;

public class TestActivity extends AppCompatActivity {

    private static final String TAG = Class.class.getSimpleName();
    static private String nonce = "123456"; // 当做随机数使用
    static private String curTime = null;
    static private String checkSum = null;

    private  static void addHeaders() {
        NeteaseClient.client.removeAllHeaders();
        NeteaseClient.client.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        NeteaseClient.client.addHeader("AppKey", NetEaseAPP_KEY);
        NeteaseClient.client.addHeader("Nonce", nonce);
        NeteaseClient.client.addHeader("CurTime", curTime);
        NeteaseClient.client.addHeader("CheckSum", checkSum);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        final EditText editText = (EditText)findViewById(R.id.test);
        Button button = (Button)findViewById(R.id.but);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                registerNetease(editText.getText().toString().toLowerCase());
            }
        });

    }

    private void registerNetease(String account){
        curTime = String.valueOf((new Date()).getTime() / 1000L);
        checkSum = CheckSumBuilder.getCheckSum(NetEaseAPP_SECRET, nonce, curTime);

        RequestParams params = new RequestParams();
        params.add("accid", account);
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
                        // refreshNeteaseToken();
                    } else {

                        CustomToast.show(TestActivity.this, "注册成功");
                        // 解析json数据
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
}
